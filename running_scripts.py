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
            return True
        print(f"Python downloader failed with exit code {exit_code}.")
        return False

    def Java_parser(self):
        script_java = subprocess.run(["java", "-jar", self.java_parser_path])
        exit_code = script_java.returncode
        if exit_code == 2:
            print("The Java program worked correctly.")
            return True
        print(f"Java parser failed with exit code {exit_code}.")
        return False

    def Python_formated(self):
        script_python = subprocess.run([sys.executable, self.python_formatted_path])
        exit_code = script_python.returncode
        if exit_code == 2:
            print("The Python program worked correctly.")
            return True
        print(f"Python formatted failed with exit code {exit_code}.")
        return False

if __name__ == "__main__":
    runner = running_scripts(
        python_downloader_path = r"Scripts\Python-downloader\main.py",
        java_parser_path = r"Scripts\Java-parser\target\Java-parser-1.0-SNAPSHOT.jar",
        python_formatted_path= r"Scripts\Python-formatted\main.py"
    )

    if not runner.Python_downloader():
        sys.exit(1)
    if not runner.Java_parser():
        sys.exit(1)
    if not runner.Python_formated():
        sys.exit(1)

    print("All steps succeeded. Exiting with code 2.")
    sys.exit(2)
