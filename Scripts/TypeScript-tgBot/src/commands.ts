export type MyBotCommand = { command: string; description: string };

export const defaultCommands: MyBotCommand[] = [
    { command: "start", description: "Начало работы с ботом" },
    { command: "get", description: "Получить расписание"}
];
