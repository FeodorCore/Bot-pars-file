import "dotenv/config";
import { Bot } from "grammy";
import { defaultCommands } from "./commands";
import { startHandler, getHandler, callbackQueryHandler } from "./handlers";

const token = process.env.BOT_TOKEN;
if (!token) {
    console.error("BOT_TOKEN is not defined in .env");
    process.exit(1);
}

const bot = new Bot(token);

(async () => {
    try {
        await bot.api.setMyCommands(defaultCommands);
        console.log("Commands set");
    } catch (err) {
        console.error("Failed to set commands", err);
    }
})();

bot.command("start", startHandler);
bot.command("get", getHandler);
bot.on("callback_query:data", callbackQueryHandler);

bot.catch((err) => {
    console.error("Bot error:", err);
});

bot.start({
    onStart: (info) => {
        console.log(`Bot started as @${info.username}`);
    }
});
