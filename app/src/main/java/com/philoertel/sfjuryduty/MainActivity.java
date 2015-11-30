package com.philoertel.sfjuryduty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DutiesLoader dutiesLoader = new DutiesLoader(getFilesDir());
        ArrayList<Duty> duties = dutiesLoader.readDuties();

        Date today = Calendar.getInstance().getTime();
        Duty nextDuty = null;
        Integer nextDutyId = null;
        for (int i = 0; i < duties.size(); ++i) {
            Duty duty = duties.get(i);
            if (duty.getDate().after(today) && (nextDuty == null || duty.getDate().before(nextDuty.getDate()))) {
                nextDutyId = i;
                nextDuty = duty;
            }
        }

        if (nextDuty == null) {
            Intent intent = new Intent(getApplicationContext(), AddDutyActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getApplicationContext(), DutyActivity.class);
            intent.putExtra(DutyActivity.DUTY_ID_EXTRA, nextDutyId);
            startActivity(intent);
        }

    }
}
