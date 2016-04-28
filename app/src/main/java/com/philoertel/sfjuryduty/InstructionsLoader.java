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
 * Class responsible for loading Instructions objects from disk and saving back to disk.
 *
 * <p>Jackson does most of the serialization work.
 */
class InstructionsLoader {
    private static final String DATA_FILE = "instructions.txt";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final File filesDir;

    public InstructionsLoader(File filesDir) {
        this.filesDir = filesDir;
    }

    public ArrayList<Instructions> readInstructions() {
        File instructionsFile = new File(filesDir, DATA_FILE);
        if (!instructionsFile.exists()) {
            try {
                instructionsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ArrayList<String> lines;
        try {
            lines = new ArrayList<>(FileUtils.readLines(instructionsFile));
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        ArrayList<Instructions> newInstructions = new ArrayList<>();

        ObjectReader reader = OBJECT_MAPPER.readerFor(Instructions.class);
        for (String line : lines) {
            Instructions instructions;
            try {
                instructions = reader.readValue(line);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            newInstructions.add(instructions);
        }
        return newInstructions;
    }

    public void saveInstructions(List<Instructions> instructionss) {
        ObjectWriter writer = OBJECT_MAPPER.writer();
        File instructionsFile = new File(filesDir, DATA_FILE);
        ArrayList<String> lines = new ArrayList<>();
        for (Instructions instructions : instructionss) {
            String line;
            try {
                line = writer.writeValueAsString(instructions);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                continue;
            }
            lines.add(line);
        }
        try {
            FileUtils.writeLines(instructionsFile, lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
