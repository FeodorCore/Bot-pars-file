package org.example;

import org.example.common.*;
import org.example.Doc.DocumentParser;
import org.example.Docx.DocxDocumentParser;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Dependency Injection configuration
            FileManager fileManager = new FileManager();
            JsonManager jsonManager = new JsonManager();
            YamlManager yamlManager = new YamlManager();
            TextProcessor textProcessor = new TextProcessor();
            ScheduleLineProcessor lineProcessor = new ScheduleLineProcessor(textProcessor);

            // Create both DOCX and DOC parsers
            Parser docxParser = new DocxDocumentParser(textProcessor, lineProcessor);
            Parser docParser = new DocumentParser(textProcessor, lineProcessor);

            List<DataWriter> dataWriters = Arrays.asList(jsonManager, yamlManager);

            // Try DOCX first, then DOC as fallback
            DocumentProcessor processor = createDocumentProcessor(
                    jsonManager, docxParser, docParser, dataWriters, fileManager);

            processor.processDocument();

        } catch (Exception e) {
            System.err.println("Application error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static DocumentProcessor createDocumentProcessor(
            FileNameReader fileNameReader,
            Parser primaryParser,
            Parser fallbackParser,
            List<DataWriter> dataWriters,
            FileManager fileManager) {

        return new DocumentProcessor(fileNameReader, primaryParser, fallbackParser, dataWriters, fileManager);
    }
}