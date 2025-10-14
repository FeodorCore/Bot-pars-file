package org.example.Doc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;


import java.util.Map;
import java.io.File;
import java.io.IOException;

public class JsonManager implements FileNameReader, DataWriter{
    private static final ObjectMapper mapper = new ObjectMapper();


    @Override
    public String readFileName(String filePath){
        try{
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
        }catch (IOException e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void write(String date, Map<String, Map<String, String>> sheduleData, String filename){

    }
}
