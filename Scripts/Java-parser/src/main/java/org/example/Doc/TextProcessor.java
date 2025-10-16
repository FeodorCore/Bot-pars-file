package org.example.Doc;

public class TextProcessor {

    public String cleanText(String text) {
        if (text == null) {
            return "";
        }
        return text.replaceAll("\\s+", " ").trim();
    }

    public void debugText(String text, String marker) {
        System.out.println("=== DEBUG TEXT ===");
        String[] lines = text.split("\n");
        boolean foundMarker = false;

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains(marker)) {
                foundMarker = true;
                System.out.println("Line " + i + ": " + lines[i].trim());
                String[] parts = lines[i].split("\\t+");
                for (int j = 0; j < parts.length; j++) {
                    System.out.println(" Part " + j + ": '" + parts[j].trim() + "'");
                }
            }
        }

        if (!foundMarker) {
            System.out.println("Marker '" + marker + "' not found in text");
        }
        System.out.println("=== END DEBUG ===");
    }

    // Дополнительный метод для нормализации текста
    public String normalizeText(String text) {
        if (text == null) return "";
        return text.replaceAll("[\\r\\n\\t]", " ").replaceAll("\\s+", " ").trim();
    }
}