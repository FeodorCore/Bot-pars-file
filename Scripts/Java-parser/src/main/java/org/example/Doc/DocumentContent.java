package org.example.Doc;

import java.util.Collections;
import java.util.Map;

public class DocumentContent {
    private final String date;
    private final Map<String, LessonData> scheduleData;

    public DocumentContent(String date, Map<String, LessonData> scheduleData) {
        this.date = date;
        this.scheduleData = scheduleData != null ?
                Collections.unmodifiableMap(scheduleData) : Collections.emptyMap();
    }

    public String getDate() {
        return date;
    }

    public Map<String, LessonData> getScheduleData() {
        return scheduleData;
    }

    public boolean hasDate() {
        return date != null && !date.isEmpty();
    }

    public boolean isEmpty() {
        return scheduleData.isEmpty();
    }
}