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
require("dotenv/config");
const grammy_1 = require("grammy");
const commands_1 = require("./commands");
const handlers_1 = require("./handlers");
const token = process.env.BOT_TOKEN;
if (!token) {
    console.error("BOT_TOKEN is not defined in .env");
    process.exit(1);
}
const bot = new grammy_1.Bot(token);
(() => __awaiter(void 0, void 0, void 0, function* () {
    try {
        yield bot.api.setMyCommands(commands_1.defaultCommands);
        console.log("Commands set");
    }
    catch (err) {
        console.error("Failed to set commands", err);
    }
}))();
bot.command("start", handlers_1.startHandler);
bot.command("get", handlers_1.getHandler);
bot.on("callback_query:data", handlers_1.callbackQueryHandler);
bot.catch((err) => {
    console.error("Bot error:", err);
});
bot.start({
    onStart: (info) => {
        console.log(`Bot started as @${info.username}`);
    }
});
