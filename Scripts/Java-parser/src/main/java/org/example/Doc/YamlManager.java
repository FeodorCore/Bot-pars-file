package org.example.Doc;

import org.yaml.snakeyaml.Yaml;



import java.util.Map;
import java.io.File;


public class YamlManager implements DataWriter {
    private final Yaml yaml = new Yaml();

    @Override
    public void write(String date, Map<String, Map<String, String>> sheduleData, String filename){
        try{
            File file = new File(filename);
            FileManager fileManager = new FileManager();




        } catch (Exception e) {
            System.err.println("Data for date " + date + " is identical to existing, skipping write");
        }

    }


}
