"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.runPythonScript = runPythonScript;
const child_process_1 = require("child_process");
const fs_1 = __importDefault(require("fs"));
const path_1 = __importDefault(require("path"));
function runPythonScript() {
    return __awaiter(this, void 0, void 0, function* () {
        return new Promise((resolve, reject) => {
            const scriptPath = "X:\\Bot-pars-file\\running_scripts.py";
            const pythonExecutable = "X:\\Bot-pars-file\\venv\\Scripts\\python.exe";
            console.log(`Путь к скрипту: ${scriptPath}`);
            console.log(`Путь к Python: ${pythonExecutable}`);
            if (!fs_1.default.existsSync(scriptPath)) {
                reject(`Файл не найден: ${scriptPath}`);
                return;
            }
            if (!fs_1.default.existsSync(pythonExecutable)) {
                reject(`Python в виртуальном окружении не найден: ${pythonExecutable}`);
                return;
            }
            const pythonProcess = (0, child_process_1.spawn)(pythonExecutable, [scriptPath], {
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
                        const jsonPath = path_1.default.join("X:\\Bot-pars-file", "Scripts", "Input-Output", "json-file", "formatted.json");
                        if (fs_1.default.existsSync(jsonPath)) {
                            const jsonData = JSON.parse(fs_1.default.readFileSync(jsonPath, 'utf8'));
                            resolve(formatSchedule(jsonData));
                        }
                        else {
                            resolve("✅ Скрипт выполнен успешно, но файл formatted.json не найден.");
                        }
                    }
                    catch (error) {
                        reject(`Ошибка при чтении JSON файла: ${error}`);
                    }
                }
                else {
                    reject(`Скрипт завершился с ошибкой. Код: ${code}\nОшибка: ${stderr}`);
                }
            });
            pythonProcess.on("error", (error) => {
                console.error(`Ошибка запуска Python: ${error}`);
                reject(`Ошибка запуска скрипта: ${error.message}`);
            });
        });
    });
}
function formatSchedule(data) {
    let message = `📅 *Расписание на ${data.date}*\n\n`;
    if (data.schedule) {
        for (const [lessonNumber, lesson] of Object.entries(data.schedule)) {
            message += `*${lessonNumber}.* ${lesson}\n`;
        }
    }
    return message;
}
