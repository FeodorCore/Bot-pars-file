package org.example.Doc;

import java.util.Map;

public interface DataWriter {
    void write(String date, Map<String, Map<String, String>> sheduleData, String filename);
}
