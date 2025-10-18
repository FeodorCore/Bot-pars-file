package org.example.common;

import java.util.Map;

public class ScheduleLineProcessor {
    private static final int REQUIRED_PARTS = 5;
    private static final String SCHEDULE_MARKER = "П-32";

    private final TextProcessor textProcessor;

    public ScheduleLineProcessor(TextProcessor textProcessor) {
        this.textProcessor = textProcessor;
    }

    public void processLine(String line, Map<String, LessonData> scheduleData) {
        if (!line.contains(SCHEDULE_MARKER)) {
            return;
        }

        System.out.println("Processing line: " + line.trim());
        String[] parts = line.split("\\t+");
        String[] cleanParts = cleanParts(parts);

        if (isValidScheduleLine(cleanParts)) {
            addScheduleData(cleanParts, scheduleData);
        } else {
            System.out.println("Failed to extract data from line. Found parts: " + cleanParts.length);
        }
    }

    private String[] cleanParts(String[] parts) {
        String[] cleanParts = new String[REQUIRED_PARTS];
        int cleanIndex = 0;
        for (String part : parts) {
            String trimmedPart = part.trim();
            if (!trimmedPart.isEmpty() && cleanIndex < REQUIRED_PARTS) {
                cleanParts[cleanIndex++] = trimmedPart;
            }
        }
        return cleanParts;
    }

    private boolean isValidScheduleLine(String[] parts) {
        return parts.length >= REQUIRED_PARTS && SCHEDULE_MARKER.equals(parts[0]);
    }

    private void addScheduleData(String[] parts, Map<String, LessonData> scheduleData) {
        String period = parts[1];
        LessonData lessonData = new LessonData(
                textProcessor.cleanText(parts[2]),
                textProcessor.cleanText(parts[3]),
                textProcessor.cleanText(parts[4])
        );

        scheduleData.put(period, lessonData);

        System.out.println("Found lesson " + period + ":");
        System.out.println(" On schedule: " + parts[2]);
        System.out.println(" Changes: " + parts[3]);
        System.out.println(" Auditorium: " + parts[4]);
    }
}
