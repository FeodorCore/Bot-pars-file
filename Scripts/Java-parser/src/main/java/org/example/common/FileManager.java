package org.example.Doc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {
    private static final String BASE_PATH = "Scripts/Input-Output/";

    public String getBasePath() {
        return BASE_PATH;
    }

    public boolean fileExists(String filePath) {
        return new File(filePath).exists();
    }

    public void createDirectories(String filePath) {
        try {
            Path path = Paths.get(filePath).getParent();
            if (path != null) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directories for: " + filePath, e);
        }
    }

    public String buildFilePath(String subdirectory, String fileName) {
        return BASE_PATH + subdirectory + "/" + fileName;
    }

    // Дополнительный метод для проверки доступности файла
    public boolean isFileAccessible(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.canRead();
    }
}