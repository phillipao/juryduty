package com.philoertel.sfjuryduty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Calendar;

public class AddDutyActivity extends AppCompatActivity {
    private DutiesLoader dutiesLoader;
    private ArrayList<Duty> duties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_duty);
        dutiesLoader = new DutiesLoader(getFilesDir());
        duties = dutiesLoader.readDuties();
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
        duties.add(duty);
        dutiesLoader.saveDuties(duties);

        Intent intent = new Intent(getApplicationContext(), DutiesActivity.class);
        startActivity(intent);
    }
}
