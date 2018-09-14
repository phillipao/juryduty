package com.philoertel.juryduty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.joda.time.DateTime;

import java.util.List;

public class DebugActivity extends AppCompatActivity {

    private static final String TAG = "DebugActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.debug_view_title);
        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);
    }

    public void fireHasDutyNotification(View view) {
        DutiesLoader dutiesLoader = new DutiesLoader(getFilesDir());
        List<Duty> duties = dutiesLoader.readDuties();
        if (!duties.isEmpty()) {
            Notifier.createPositiveNotification(this, 0, DateTime.now());
        }
    }

    public void fireHasNotDutyNotification(View view) {
        DutiesLoader dutiesLoader = new DutiesLoader(getFilesDir());
        List<Duty> duties = dutiesLoader.readDuties();
        if (!duties.isEmpty()) {
            Notifier.createNegativeNotification(this, 0, DateTime.now());
        }
    }
}