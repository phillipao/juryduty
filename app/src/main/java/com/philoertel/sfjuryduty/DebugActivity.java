package com.philoertel.sfjuryduty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.joda.time.DateTime;

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

    public void fireNoDataAlarm(View v) {
        Intent alarmIntent = new Intent(this, NoDataAlarmReceiver.class);
        alarmIntent.putExtra(NoDataAlarmReceiver.EXTRA_DATE, DateTime.now());
        sendBroadcast(alarmIntent);
    }
}
