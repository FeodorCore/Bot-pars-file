package org.example.Doc;

import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

public class DocumentProcessor {
    private final FileNameReader fileNameReader;
    private final Parser documentParser;
    private final List<DataWriter> dataWriters;
    private final FileManager fileManager;

    public DocumentProcessor(FileNameReader fileNameReader,
                             Parser documentParser,
                             List<DataWriter> dataWriters,
                             FileManager fileManager) {
        this.fileNameReader = fileNameReader;
        this.documentParser = documentParser;
        this.dataWriters = dataWriters;
        this.fileManager = fileManager;
    }

    public void processDocument() {
        try {
            String nameFile = fileNameReader.readFileName(
                    fileManager.buildFilePath("json-file", "name_doc.json"));

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
        DocumentContent content = documentParser.parse(filePath);

        if (content != null && content.hasDate() && !content.isEmpty()) {
            writeData(content);
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