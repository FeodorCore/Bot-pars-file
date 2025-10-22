import { Context } from "grammy";
import { getChoiceKeyboard } from "../keyboard";

export async function startHandler(ctx: Context) {
    await ctx.reply("Привет, Это бот для получения для актуального рассписания группы П-32.", {
        reply_markup: getChoiceKeyboard()
    });
}
