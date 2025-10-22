import { Context } from "grammy";
import { runPythonScript } from "../services/pythonRunner";
import { deleteMessage } from "../utils/helpers";

export async function getHandler(ctx: Context) {
    await handleGetRequest(ctx);
}

export async function callbackQueryHandler(ctx: Context) {
    const data = ctx.callbackQuery?.data;
    if (!data) return;

    if (data === "get_file") {
        await handleGetRequest(ctx);
        await ctx.answerCallbackQuery();
        return;
    }

    await ctx.answerCallbackQuery();
}

async function handleGetRequest(ctx: Context) {
    let waitingMsg;
    try {
        waitingMsg = await ctx.reply("⏳ Запускаю скачивание и парсинг данных...");

        const result = await runPythonScript();
        await deleteMessage(ctx, waitingMsg?.message_id);
        await ctx.reply(result, { parse_mode: "Markdown" });

    } catch (error) {
        await deleteMessage(ctx, waitingMsg?.message_id);
        console.error("Ошибка выполнения скрипта:", error);
        await ctx.reply(`Произошла ошибка при выполнении скрипта: ${error}`);
    }
}
