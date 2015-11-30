package com.philoertel.sfjuryduty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DutyActivity extends AppCompatActivity {
    public static final String DUTY_ID_EXTRA = "com.philoertel.sfjuryduty.DUTY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duty);

        Intent intent = getIntent();
        int position =  intent.getIntExtra(DUTY_ID_EXTRA, 0);
        DutiesLoader dutiesLoader = new DutiesLoader(getFilesDir());
        ArrayList<Duty> duties = dutiesLoader.readDuties();
        Duty duty = duties.get(position);
        TextView dateView = (TextView) findViewById(R.id.date);
        dateView.setText(SimpleDateFormat.getDateInstance().format(duty.getDate()));
        TextView groupView = (TextView) findViewById(R.id.group);
        groupView.setText(duty.getGroup() + "");

        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_duty, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_add_duty:
                intent = new Intent(getApplicationContext(), AddDutyActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_duties:
                intent = new Intent(getApplicationContext(), DutiesActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
