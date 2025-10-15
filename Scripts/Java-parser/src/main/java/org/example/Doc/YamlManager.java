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

            String finalDateKey = findUniqueDateKey(date, existingData, lessonData);
            if (finalDateKey == null) {
                System.out.println("Data for date " + date + " is identical to existing, skipping write")
                return;
            }

            existingData.put(finalDateKey, lessonData);
            writeDataToFile(file, existingData);
            System.out.println("Data successfully appended to: " + filename)


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
            if (isDataIdentical(lessonData, existingLessonData)) {
                return null;
            }
            counter++;
            finalDateKey = date + "(" + counter + ")";
        }
        return finalDateKey;
    }

    private boolean isDataIdentical(Map<String, Map<String, String>> newData, Map<String, Map<String, String>> existingData){

        if (newData.size() != existingData.size()) return false;
        for (String period : newData.keySet()) {
            if (!existingData.containsKey(period)) return false;

        Map<String, String> newLesson = newData.get(period);
        Map<String, String> existingLesson = existingData.get(period);

            if (!equalsWithNullCheck(newLesson.get("on_schedule"), existingLesson.get("on_schedule")) ||
                    !equalsWithNullCheck(newLesson.get("changes"), existingLesson.get("changes")) ||
                    !equalsWithNullCheck(newLesson.get("auditorium"), existingLesson.get("auditorium"))) {
                return false;
            }
        }
        return true;
    }

    private boolean equalsWithNullCheck(String str1, String str2) {
        if (str1 == null && str2 == null ) return true;
        if (str1 == null || str2 == null ) return false;
        return str1.equals(str2);
    }

    private void writeDataToFile(File file, Map<String, Object> data){

    }


}
