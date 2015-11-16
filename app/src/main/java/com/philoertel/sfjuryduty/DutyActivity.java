package com.philoertel.sfjuryduty;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.Date;

public class DutyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int position =  intent.getIntExtra(DutiesActivity.DUTY_ID_EXTRA, 0);
        long time = intent.getLongExtra(DutiesActivity.DUTY_DATE_EXTRA, 0);
        Date date = new Date(time);
        String group = intent.getStringExtra(DutiesActivity.DUTY_GROUP_EXTRA);
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(group);
        setContentView(textView);
        setContentView(R.layout.activity_duty);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
