package org.example.Doc;

import org.yaml.snakeyaml.Yaml;



import java.util.Map;
import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedHashMap;

public class YamlManager implements DataWriter {
    private final Yaml yaml = new Yaml();

    @Override
    public void write(String date, Map<String, Map<String, String>> scheduleData, String filename){
        try{
            File file = new File(filename);
            FileManager fileManager = new FileManager();
            fileManager.createDirectories(filename);

            Map<String, Object> existingData = readExistingData(file);
            Map<String, Map<String, String>> lessonData = prepareLessonData(scheduleData);


        } catch (Exception e) {
            System.err.println("Data for date " + date + " is identical to existing, skipping write");

        }

    }

    private Map<String, Object> readExistingData(File file){
        try {
            if (file.exists()) {
                try (FileInputStream fis = new FileInputStream(file)){
                    Map<String, Object> data = yaml.load(fis);
                    return data != null ? data : new LinkedHashMap<>();
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading existing YAML data: " + e.getMessage());
        }
        return new LinkedHashMap<>();
    }

    private Map<String, Map<String, String>> prepareLessonData(Map<String, Map<String, String>> scheduleData){
        Map<String, Map<String, String>> lessonData = new LinkedHashMap<>();
        for (Map.Entry<String, Map<String, String>> entry : scheduleData.entrySet()){
            Map<String, String> lessonInfo = new LinkedHashMap<>();
            lessonInfo.put("on_schedule", entry.getValue().get("on_schedule"));
            lessonInfo.put("changes", entry.getValue().get("changes"));
            lessonInfo.put("auditorium", entry.getValue().get("auditorium"));
            lessonData.put(entry.getKey(), lessonInfo);
        }
        return lessonData;

    }

    private String findUniqueDateKey(String date, Map<String, Object> existingData, Map<String, Map<String, String>> lessonData){
        String finalDateKey = date;
        int counter = 1;

        while (existingData.containsKey(finalDateKey)) {
            Map<String, Map<String, String>> existingLessonData = (Map<String, Map<String, String>>) existingData.get(finalDateKey);
        }
    }

}
