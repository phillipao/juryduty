package com.philoertel.sfjuryduty;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DutiesActivity extends AppCompatActivity {
    private static final String DATA_FILE = "duties.txt";

    private ArrayList<Duty> duties;
    private ArrayAdapter<Duty> dutiesAdapter;
    private ListView lvDuties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duties);
        lvDuties = (ListView) findViewById(R.id.dutiesView);
        readDuties();
        dutiesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, duties);
        lvDuties.setAdapter(dutiesAdapter);

        setupListViewListener();
    }

    private void setupListViewListener() {
        lvDuties.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                duties.remove(position);
                dutiesAdapter.notifyDataSetChanged();
                saveDuties();
                return true;
            }
        });
    }

    public void addDuty(View v) {
        EditText etNewDuty = (EditText) findViewById(R.id.newDutyNumberText);
        DatePicker datePicker = (DatePicker) findViewById(R.id.newDutyDatePicker);
        Calendar calendar = Calendar.getInstance();
        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
        int group;
        try {
            group = Integer.valueOf(etNewDuty.getText().toString());
        } catch (NumberFormatException e) {
            return;
        }
        Duty duty = new Duty(calendar.getTime(), group);
        dutiesAdapter.add(duty);
        etNewDuty.setText("");
        saveDuties();
    }

    private void readDuties() {
        File filesDir = getFilesDir();
        File dutiesFile = new File(filesDir, DATA_FILE);
        ArrayList<String> lines = new ArrayList<>();
        try {
            lines = new ArrayList<>(FileUtils.readLines(dutiesFile));
        } catch (IOException e) {
            duties = new ArrayList<>();
            e.printStackTrace();
            return;
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
        duties = newDuties;
    }

    private void saveDuties() {
        File filesDir = getFilesDir();
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
