package org.example.Doc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class JsonManager implements FileNameReader, DataWriter {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String readFileName(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                System.err.println("File not found: " + filePath);
                return null;
            }

            JsonNode root = mapper.readTree(file);
            JsonNode nameNode = root.get("name_file");

            if (nameNode != null && nameNode.isTextual()) {
                return nameNode.asText();
            }
            return null;
        } catch (IOException e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void write(String date, Map<String, Map<String, String>> scheduleData, String filename) {
        try {
            File file = new File(filename);
            FileManager fileManager = new FileManager();
            fileManager.createDirectories(filename);

            ObjectNode rootNode = mapper.createObjectNode();
            ObjectNode scheduleNode = mapper.createObjectNode();
            scheduleNode.put("date", date != null ? date : "");

            ArrayNode lessonsNode = mapper.createArrayNode();
            for (Map.Entry<String, Map<String, String>> entry : scheduleData.entrySet()) {
                String period = entry.getKey();
                Map<String, String> periodData = entry.getValue();

                ObjectNode lessonNode = mapper.createObjectNode();
                lessonNode.put("number", Integer.parseInt(period));
                lessonNode.put("on_schedule", periodData.get("on_schedule"));
                lessonNode.put("changes", periodData.get("changes"));
                lessonNode.put("auditorium", periodData.get("auditorium"));

                lessonsNode.add(lessonNode);
            }

            scheduleNode.set("lessons", lessonsNode);
            rootNode.set("schedule", scheduleNode);

            mapper.writerWithDefaultPrettyPrinter().writeValue(file, rootNode);
            System.out.println("Data successfully written to: " + filename);

        } catch (Exception e) {
            throw new RuntimeException("Failed to write JSON file: " + filename, e);
        }
    }
}