import { Context } from "grammy";

export async function deleteMessage(ctx: Context, messageId?: number) {
    if (!messageId || !ctx.chat?.id) return;

    try {
        await ctx.api.deleteMessage(ctx.chat.id, messageId);
    } catch (e) {
        console.error("Не удалось удалить сообщение:", e);
    }
}
