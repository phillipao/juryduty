package com.philoertel.sfjuryduty;

import android.util.Log;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class responsible for loading Duty objects from disk and saving back to disk.
 */
public class DutiesLoader {
    private static final String DATA_FILE = "duties.txt";

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
        for (String line : lines) {
            String[] split = line.split(",");
            if (split.length != 2) {
                Log.w("duty", String.format("Invalid duty input %s", line));
                continue;
            }
            long millis;
            try {
                millis = Long.parseLong(split[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                continue;
            }
            int group;
            try {
                group = Integer.parseInt(split[1]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                continue;
            }
            newDuties.add(new Duty(new Date(millis), group));
        }
        return newDuties;
    }

    public void saveDuties(List<Duty> duties) {
        File dutiesFile = new File(filesDir, DATA_FILE);
        ArrayList<String> lines = new ArrayList<>();
        for (Duty duty : duties) {
            String line = duty.getDate().getTime() + "," + duty.getGroup();
            lines.add(line);
        }
        try {
            FileUtils.writeLines(dutiesFile, lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
