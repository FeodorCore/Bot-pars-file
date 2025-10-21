package org.example.common;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DocumentProcessor {
    private final FileNameReader fileNameReader;
    private final Parser primaryParser;
    private final Parser fallbackParser;
    private final List<DataWriter> dataWriters;
    private final FileManager fileManager;

    public DocumentProcessor(FileNameReader fileNameReader, Parser primaryParser, Parser fallbackParser, List<DataWriter> dataWriters, FileManager fileManager) {
        this.fileNameReader = fileNameReader;
        this.primaryParser = primaryParser;
        this.fallbackParser = fallbackParser;
        this.dataWriters = dataWriters;
        this.fileManager = fileManager;
    }

    public void processDocument() {
        try {
            String nameFile = fileNameReader.readFileName(fileManager.buildFilePath("json-file", "name_doc.json"));
            if (!isValidFileName(nameFile)) {
                return;
            }

            String filePath = fileManager.buildFilePath("Download-doc", nameFile);
            if (!fileManager.fileExists(filePath)) {
                handleMissingFile(nameFile, filePath);
                return;
            }

            processDocumentFile(filePath);

        } catch (Exception e) {
            System.err.println("Error processing document: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean isValidFileName(String nameFile) {
        if (nameFile == null || nameFile.isEmpty()) {
            System.err.println("File name not found in configuration");
            return false;
        }
        return true;
    }

    private void handleMissingFile(String nameFile, String filePath) {
        System.err.println("File not found: " + filePath);
        System.err.println("Please place file " + nameFile + " in folder " +
                fileManager.getBasePath() + "Download-doc");
    }

    private void processDocumentFile(String filePath) {
        System.out.println("Reading file: " + filePath);

        DocumentContent content = null;
        String parserType = "DOCX";

        try {
            content = primaryParser.parse(filePath);
        } catch (Exception e) {
            System.out.println("DOCX parsing failed, trying DOC: " + e.getMessage());
            parserType = "DOC";
            try {
                content = fallbackParser.parse(filePath);
            } catch (Exception ex) {
                System.err.println("Both DOCX and DOC parsing failed:");
                System.err.println("DOCX error: " + e.getMessage());
                System.err.println("DOC error: " + ex.getMessage());
                return;
            }
        }

        System.out.println("Successfully parsed with " + parserType + " parser");

        if (content != null && content.hasDate() && !content.isEmpty()) {
            writeData(content);
            System.exit(2);
        } else {
            System.out.println("Date not found in document or document is empty");
        }
    }

    private void writeData(DocumentContent content) {
        System.out.println("Found date: " + content.getDate());

        for (DataWriter writer : dataWriters) {
            try {
                String outputPath = getOutputPath(writer);
                writer.write(content.getDate(), convertScheduleData(content.getScheduleData()), outputPath);
            } catch (Exception e) {
                System.err.println("Error writing data with " + writer.getClass().getSimpleName() + ": " + e.getMessage());
            }
        }
    }

    private Map<String, Map<String, String>> convertScheduleData(Map<String, LessonData> lessonData) {
        Map<String, Map<String, String>> result = new LinkedHashMap<>();
        for (Map.Entry<String, LessonData> entry : lessonData.entrySet()) {
            Map<String, String> data = new LinkedHashMap<>();
            LessonData lesson = entry.getValue();
            data.put("on_schedule", lesson.getOnSchedule());
            data.put("changes", lesson.getChanges());
            data.put("auditorium", lesson.getAuditorium());
            result.put(entry.getKey(), data);
        }
        return result;
    }

    private String getOutputPath(DataWriter writer) {
        if (writer instanceof JsonManager) {
            return fileManager.buildFilePath("json-file", "broadcast_pars.json");
        } else if (writer instanceof YamlManager) {
            return fileManager.buildFilePath("yaml-file", "history.yaml");
        }
        throw new IllegalArgumentException("Unknown writer type: " + writer.getClass().getSimpleName());
    }
}