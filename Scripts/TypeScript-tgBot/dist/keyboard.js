"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.getChoiceKeyboard = void 0;
const grammy_1 = require("grammy");
const getChoiceKeyboard = () => new grammy_1.InlineKeyboard().text("Получить", "get_file");
exports.getChoiceKeyboard = getChoiceKeyboard;
