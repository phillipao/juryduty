package com.philoertel.sfjuryduty;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for loading Duty objects from disk and saving back to disk.
 *
 * <p>Jackson does most of the serialization work.
 */
class DutiesLoader {
    private static final String DATA_FILE = "duties.txt";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final File filesDir;

    public DutiesLoader(File filesDir) {
        this.filesDir = filesDir;
    }

    public ArrayList<Duty> readDuties() {
        File dutiesFile = new File(filesDir, DATA_FILE);
        if (!dutiesFile.exists()) {
            try {
                dutiesFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ArrayList<String> lines;
        try {
            lines = new ArrayList<>(FileUtils.readLines(dutiesFile));
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        ArrayList<Duty> newDuties = new ArrayList<>();
        ObjectReader reader = OBJECT_MAPPER.readerFor(Duty.class);
        for (String line : lines) {
            Duty duty;
            try {
                duty = reader.readValue(line);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            newDuties.add(duty);
        }
        return newDuties;
    }

    public void saveDuties(List<Duty> duties) {
        File dutiesFile = new File(filesDir, DATA_FILE);
        ArrayList<String> lines = new ArrayList<>();
        for (Duty duty : duties) {
            ObjectWriter writer = OBJECT_MAPPER.writer();
            try {
                lines.add(writer.writeValueAsString(duty));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        try {
            FileUtils.writeLines(dutiesFile, lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
