package com.philoertel.juryduty;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.inject.Inject;

public class DutiesActivity extends AppCompatActivity implements Observer {
    private ArrayList<Duty> duties = new ArrayList<>();
    private ArrayAdapter<Duty> dutiesAdapter;

    @Inject DutiesLoader mDutiesLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((JuryDutyApplication) getApplication()).getComponent().inject(this);
        setContentView(R.layout.activity_duties);
        ListView lvDuties = findViewById(R.id.dutiesView);
        lvDuties.setEmptyView(findViewById(R.id.emptyDutiesView));
        dutiesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, duties);
        lvDuties.setAdapter(dutiesAdapter);

        initToolbar();
        setupListViewListener(lvDuties);

        mDutiesLoader.addObserver(this);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.duties_title);
        setSupportActionBar(toolbar);
    }

    private void setupListViewListener(ListView lvDuties) {
        lvDuties.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DutyActivity.class);
                intent.putExtra(DutyActivity.DUTY_ID_EXTRA, position);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        duties = mDutiesLoader.readDuties();
        dutiesAdapter.clear();
        dutiesAdapter.addAll(duties);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_duties, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home: {
                finish();
                return true;
            }

            case R.id.action_debug: {
                Intent intent = new Intent(getApplicationContext(), DebugActivity.class);
                startActivity(intent);
                return true;
            }

            case R.id.action_refresh: {
                Intent alarmIntent = new Intent(this, CheckInAlarmReceiver.class);
                alarmIntent.putExtra(CheckInAlarmReceiver.EXTRA_DATE, DateTime.now().toString());
                sendBroadcast(alarmIntent);
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onAddButtonClick(View v) {
        Intent intent = new Intent(getApplicationContext(), AddDutyActivity.class);
        startActivity(intent);
    }

    @Override
    public void update(@Nullable Observable observable, Object o) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                duties = mDutiesLoader.readDuties();
                dutiesAdapter.clear();
                dutiesAdapter.addAll(duties);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDutiesLoader.deleteObserver(this);
    }
}
