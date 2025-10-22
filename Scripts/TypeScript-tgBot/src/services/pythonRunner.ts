import { spawn } from "child_process";
import fs from "fs";
import path from "path";

export async function runPythonScript(): Promise<string> {
    return new Promise((resolve, reject) => {
        const scriptPath = "X:\\Bot-pars-file\\running_scripts.py";
        const pythonExecutable = "X:\\Bot-pars-file\\venv\\Scripts\\python.exe";

        console.log(`Путь к скрипту: ${scriptPath}`);
        console.log(`Путь к Python: ${pythonExecutable}`);

        if (!fs.existsSync(scriptPath)) {
            reject(`Файл не найден: ${scriptPath}`);
            return;
        }

        if (!fs.existsSync(pythonExecutable)) {
            reject(`Python в виртуальном окружении не найден: ${pythonExecutable}`);
            return;
        }

        const pythonProcess = spawn(pythonExecutable, [scriptPath], {
            cwd: "X:\\Bot-pars-file"
        });

        let stdout = "";
        let stderr = "";

        pythonProcess.stdout.on("data", (data) => {
            const output = data.toString();
            stdout += output;
            console.log(`Python stdout: ${output}`);
        });

        pythonProcess.stderr.on("data", (data) => {
            const error = data.toString();
            stderr += error;
            console.error(`Python stderr: ${error}`);
        });

        pythonProcess.on("close", (code) => {
            console.log(`Python процесс завершился с кодом: ${code}`);
            if (code === 2) {
                try {
                    const jsonPath = path.join("X:\\Bot-pars-file", "Scripts", "Input-Output", "json-file", "formatted.json");
                    if (fs.existsSync(jsonPath)) {
                        const jsonData = JSON.parse(fs.readFileSync(jsonPath, 'utf8'));
                        resolve(formatSchedule(jsonData));
                    } else {
                        resolve("✅ Скрипт выполнен успешно, но файл formatted.json не найден.");
                    }
                } catch (error) {
                    reject(`Ошибка при чтении JSON файла: ${error}`);
                }
            } else {
                reject(`Скрипт завершился с ошибкой. Код: ${code}\nОшибка: ${stderr}`);
            }
        });

        pythonProcess.on("error", (error) => {
            console.error(`Ошибка запуска Python: ${error}`);
            reject(`Ошибка запуска скрипта: ${error.message}`);
        });
    });
}

function formatSchedule(data: any): string {
    let message = `📅 *Расписание на ${data.date}*\n\n`;

    if (data.schedule) {
        for (const [lessonNumber, lesson] of Object.entries(data.schedule)) {
            message += `*${lessonNumber}.* ${lesson}\n`;
        }
    }

    return message;
}
