import { InlineKeyboard } from "grammy";

export const getChoiceKeyboard = () =>
    new InlineKeyboard().text("Получить", "get_file");
