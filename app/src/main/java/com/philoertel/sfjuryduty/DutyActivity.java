package com.philoertel.sfjuryduty;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DutyActivity extends AppCompatActivity {
    public static final String DUTY_DATE_EXTRA = "com.philoertel.sfjuryduty.DATE";
    public static final String DUTY_GROUP_EXTRA = "com.philoertel.sfjuryduty.GROUP";
    public static final String DUTY_ID_EXTRA = "com.philoertel.sfjuryduty.DUTY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duty);

        Intent intent = getIntent();
        int position =  intent.getIntExtra(DUTY_ID_EXTRA, 0);
        long time = intent.getLongExtra(DUTY_DATE_EXTRA, 0);
        Date date = new Date(time);
        String group = intent.getStringExtra(DUTY_GROUP_EXTRA);
        TextView dateView = (TextView) findViewById(R.id.date);
        dateView.setText(SimpleDateFormat.getDateInstance().format(date));
        TextView groupView = (TextView) findViewById(R.id.group);
        groupView.setText(group);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_duties:
                Intent intent = new Intent(getApplicationContext(), DutiesActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
