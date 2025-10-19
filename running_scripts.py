import subprocess

result = subprocess.run(["java", "-jar", r"Scripts\Java-parser\target\Java-parser-1.0-SNAPSHOT.jar"])
exit_code = result.returncode
if exit_code == 0:
    print("The program was not fully completed.")
elif exit_code == 1:
    print("The program worked incorrectly.")
elif exit_code == 2:
    print("The program worked correctly.")
else:
    print(f"Unexpected exit code: {exit_code}")