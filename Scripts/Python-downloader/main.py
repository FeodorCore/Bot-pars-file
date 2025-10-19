import requests
from bs4 import BeautifulSoup
from urllib.parse import urljoin
from pathlib import Path
import os


class FileDownloader:
    def __init__(self, base_url, headers=None, download_dir=None):
        self.base_url = base_url
        self.headers = headers or {
            "User-Agent": (
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                "AppleWebKit/537.36 (KHTML, like Gecko) "
                "Chrome/118.0.0.0 Safari/537.36"
            )
        }
        script_dir = Path(__file__).resolve().parent
        scripts_dir = script_dir

        if script_dir.name.lower() == "python-downloader":
            scripts_dir = script_dir.parent

        default_target = scripts_dir / "Input-Output" / "Download-doc"

        self.download_dir = Path(download_dir) if download_dir else default_target
        self.download_dir.mkdir(parents=True, exist_ok=True)

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

    def download_first_file(self):
        html = self.fetch_page()
        links = self.extract_links(html)
        if not links:
            print("Файлы не найдены.")
            return None
        return self.download_file(links[0])

if __name__ == "__main__":
    downloader = FileDownloader("https://gtec-bks.by/")
    downloader.download_first_file()
