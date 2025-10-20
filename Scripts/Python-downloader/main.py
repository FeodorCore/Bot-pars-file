import requests
from bs4 import BeautifulSoup
from urllib.parse import urljoin
from pathlib import Path
import os
import sys

class FileDownloader:
    def __init__(self, base_url, headers, download_dir):
        self.base_url = base_url
        self.headers = headers
        self.download_dir = download_dir
        self.download_dir.mkdir(parents=True, exist_ok=True)

    def download_first_file(self):
        html = self.fetch_page()
        links = self.extract_links(html)
        if not links:
            print("Файлы не найдены.")
            return None
        self.download_file(links[0])
        sys.exit(2)

    def fetch_page(self):
        response = requests.get(self.base_url, headers=self.headers)
        response.raise_for_status()
        return response.text

    def extract_links(self, html):
        soup = BeautifulSoup(html, "html.parser")
        info_widget = soup.find("div", class_="info-widget")
        if not info_widget:
            return []
        links = info_widget.find_all("a", href=True)
        return [urljoin(self.base_url, a["href"]) for a in links]

    def download_file(self, file_url):
        filename = os.path.basename(file_url)
        filepath = self.download_dir / filename

        print(f"Скачиваю: {file_url}")
        response = requests.get(file_url, headers=self.headers, stream=True)
        response.raise_for_status()

        with open(filepath, "wb") as f:
            for chunk in response.iter_content(chunk_size=8192):
                f.write(chunk)

        print(f"Файл сохранён как {filepath}")
        return filepath

if __name__ == "__main__":
    base_url = "https://gtec-bks.by/"
    headers = {"User-Agent": ("Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                "AppleWebKit/537.36 (KHTML, like Gecko) "
                "Chrome/118.0.0.0 Safari/537.36")}

    script_dir = Path(__file__).resolve().parent
    scripts_dir = script_dir.parent
    download_dir = scripts_dir / "Input-Output" / "Download-doc"

    downloader = FileDownloader(base_url = base_url, headers=headers, download_dir=download_dir)
    downloader.download_first_file()