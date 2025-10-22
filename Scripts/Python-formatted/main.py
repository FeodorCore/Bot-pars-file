import json
import re
import sys
from pathlib import Path
from typing import Dict, Any


class Python_formatted:

    DAYS_MAPPING = {
        'понедельник': 'Понедельник',
        'вторник': 'Вторник',
        'среда': 'Среда',
        'четверг': 'Четверг',
        'пятница': 'Пятница',
        'суббота': 'Суббота',
        'воскресенье': 'Воскресенье'
    }

    def __init__(self, script_dir: Path):
        self.script_dir = script_dir
        self.json_dir = script_dir.parent / "Input-Output" / "json-file"
        self.broadcast_data: Dict[str, Any] = {}
        self.weekly_data: Dict[str, Any] = {}
        self.result: Dict[str, Any] = {}

    def run(self):
        try:
            self.load_data()
            self.merge_schedule()
            self.save_result()
            print(f"File formatted.")
            return 2
        except Exception as e:
            print(f"Error: {e}")
            return 1

    def load_data(self):
        with open(self.json_dir / 'broadcast_pars.json', 'r', encoding='utf-8') as f:
            self.broadcast_data = json.load(f)

        with open(self.json_dir / 'weekly_schedule.json', 'r', encoding='utf-8') as f:
            self.weekly_data = json.load(f)

    def normalize_day(self, raw_date: str) -> str:
        day_match = re.search(r'([а-я]+)\s*$', raw_date.lower())
        day_key = day_match.group(1) if day_match else None
        return self.DAYS_MAPPING.get(day_key, day_key)

    def merge_schedule(self) :
        raw_date = self.broadcast_data['schedule']['date']
        normalized_day = self.normalize_day(raw_date)

        daily_schedule = self.weekly_data.get(normalized_day, {}).copy()

        for lesson in self.broadcast_data['schedule']['lessons']:
            lesson_num = str(lesson['number'])
            if lesson_num in daily_schedule:
                changes_text = lesson['changes']
                auditorium = lesson.get('auditorium', '')
                if auditorium:
                    changes_text += f" ({auditorium})"
                daily_schedule[lesson_num] = changes_text

        self.result = {
            "date": raw_date,
            "schedule": daily_schedule
        }

    def save_result(self, filename: str = 'formatted.json') -> None:
        with open(self.json_dir / filename, 'w', encoding='utf-8') as f:
            json.dump(self.result, f, ensure_ascii=False, indent=2)


if __name__ == "__main__":
    script_dir = Path(__file__).resolve().parent
    processor = Python_formatted(script_dir)
    exit_code = processor.run()
    sys.exit(exit_code)