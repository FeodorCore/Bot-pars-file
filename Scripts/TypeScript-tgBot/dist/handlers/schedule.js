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
Object.defineProperty(exports, "__esModule", { value: true });
exports.getHandler = getHandler;
exports.callbackQueryHandler = callbackQueryHandler;
const pythonRunner_1 = require("../services/pythonRunner");
const helpers_1 = require("../utils/helpers");
function getHandler(ctx) {
    return __awaiter(this, void 0, void 0, function* () {
        yield handleGetRequest(ctx);
    });
}
function callbackQueryHandler(ctx) {
    return __awaiter(this, void 0, void 0, function* () {
        var _a;
        const data = (_a = ctx.callbackQuery) === null || _a === void 0 ? void 0 : _a.data;
        if (!data)
            return;
        if (data === "get_file") {
            yield handleGetRequest(ctx);
            yield ctx.answerCallbackQuery();
            return;
        }
        yield ctx.answerCallbackQuery();
    });
}
function handleGetRequest(ctx) {
    return __awaiter(this, void 0, void 0, function* () {
        let waitingMsg;
        try {
            waitingMsg = yield ctx.reply("⏳ Запускаю скачивание и парсинг данных...");
            const result = yield (0, pythonRunner_1.runPythonScript)();
            yield (0, helpers_1.deleteMessage)(ctx, waitingMsg === null || waitingMsg === void 0 ? void 0 : waitingMsg.message_id);
            yield ctx.reply(result, { parse_mode: "Markdown" });
        }
        catch (error) {
            yield (0, helpers_1.deleteMessage)(ctx, waitingMsg === null || waitingMsg === void 0 ? void 0 : waitingMsg.message_id);
            console.error("Ошибка выполнения скрипта:", error);
            yield ctx.reply(`Произошла ошибка при выполнении скрипта: ${error}`);
        }
    });
}
