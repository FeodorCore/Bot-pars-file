package org.example.common;

import java.util.Map;

public interface DataWriter {
    void write(String date, Map<String, Map<String, String>> scheduleData, String filename);
}