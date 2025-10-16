package org.example.Doc;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DocumentParser implements Parser {
    private static final Pattern DATE_PATTERN =
            Pattern.compile("\\d{1,2}\\s+[а-яё]+\\s+\\d{4}\\s+г\\.,\\s+[а-яё]+");

    private final TextProcessor textProcessor;
    private final ScheduleLineProcessor lineProcessor;

    public DocumentParser(TextProcessor textProcessor, ScheduleLineProcessor lineProcessor) {
        this.textProcessor = textProcessor;
        this.lineProcessor = lineProcessor;
    }

    @Override
    public DocumentContent parse(String filePath) {
        validateFilePath(filePath);

        try (InputStream is = new FileInputStream(new File(filePath));
             HWPFDocument document = new HWPFDocument(is);
             WordExtractor extractor = new WordExtractor(document)) {

            String text = extractor.getText();
            textProcessor.debugText(text, "П-32");

            String date = extractDate(text);
            Map<String, LessonData> scheduleData = parseScheduleData(text);

            return new DocumentContent(date, scheduleData);

        } catch (Exception e) {
            throw new DocumentParseException("Failed to parse document: " + filePath, e);
        }
    }

    private void validateFilePath(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }
        if (!new File(filePath).exists()) {
            throw new DocumentParseException("File not found: " + filePath);
        }
    }

    private String extractDate(String text) {
        Matcher matcher = DATE_PATTERN.matcher(text);
        return matcher.find() ? matcher.group() : null;
    }

    private Map<String, LessonData> parseScheduleData(String text) {
        Map<String, LessonData> scheduleData = new HashMap<>();
        String[] lines = text.split("\n");

        for (String line : lines) {
            lineProcessor.processLine(line, scheduleData);
        }
        return scheduleData;
    }
}