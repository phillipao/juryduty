package com.philoertel.sfjuryduty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class DutiesActivity extends AppCompatActivity {
    private ArrayList<Duty> duties = new ArrayList<>();
    private ArrayAdapter<Duty> dutiesAdapter;
    private DutiesLoader dutiesLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duties);
        ListView lvDuties = (ListView) findViewById(R.id.dutiesView);
        dutiesLoader = new DutiesLoader(getFilesDir());
        dutiesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, duties);
        lvDuties.setAdapter(dutiesAdapter);

        initToolbar();
        setupListViewListener(lvDuties);
    }

    @Override
    protected void onResume() {
        super.onResume();
        duties = dutiesLoader.readDuties();
        dutiesAdapter.notifyDataSetChanged();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.duties_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home: {
                finish();
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);

        }
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
        lvDuties.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                duties.remove(position);
                dutiesAdapter.notifyDataSetChanged();
                dutiesLoader.saveDuties(duties);
                return true;
            }
        });
    }

    public void onAddButtonClick(View v) {
        Intent intent = new Intent(getApplicationContext(), AddDutyActivity.class);
        startActivity(intent);
    }
}
