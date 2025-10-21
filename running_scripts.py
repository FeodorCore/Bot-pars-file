import subprocess
import sys

class running_scripts:
    def __init__(self, python_downloader_path, java_parser_path, python_formatted_path):
        self.python_downloader_path = python_downloader_path
        self.java_parser_path = java_parser_path
        self.python_formatted_path = python_formatted_path

    def Python_downloader(self):
        script_python = subprocess.run([sys.executable, self.python_downloader_path])
        exit_code = script_python.returncode
        if exit_code == 2:
            print("The Python program worked correctly.")
            self.Java_parser()

    def Java_parser(self):
        script_java = subprocess.run(["java", "-jar", self.java_parser_path])
        exit_code = script_java.returncode
        if exit_code == 2:
            print("The Java program worked correctly.")
            self.Python_formated()

    def Python_formated(self):
        script_python = subprocess.run([sys.executable, self.python_formatted_path])
        exit_code = script_python.returncode
        if exit_code == 2:
            print("The Python program worked correctly.")
        
if __name__ == "__main__":
    runner = running_scripts(python_downloader_path = r"Scripts\Python-downloader\main.py", 
                             java_parser_path = r"Scripts\Java-parser\target\Java-parser-1.0-SNAPSHOT.jar",
                             python_formatted_path= r"Scripts\Python-formatted\main.py")
    runner.Python_downloader()