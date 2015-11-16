package com.philoertel.sfjuryduty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class DutiesActivity extends AppCompatActivity {
    public static final String DUTY_DATE_EXTRA = "com.philoertel.sfjuryduty.DATE";
    public static final String DUTY_GROUP_EXTRA = "com.philoertel.sfjuryduty.GROUP";
    public static final String DUTY_ID_EXTRA = "com.philoertel.sfjuryduty.DUTY";

    private ArrayList<Duty> duties;
    private ArrayAdapter<Duty> dutiesAdapter;
    private ListView lvDuties;
    private DutiesLoader dutiesLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duties);
        lvDuties = (ListView) findViewById(R.id.dutiesView);
        dutiesLoader = new DutiesLoader(getFilesDir());
        duties = dutiesLoader.readDuties();
        dutiesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, duties);
        lvDuties.setAdapter(dutiesAdapter);

        setupListViewListener();
    }

    private void setupListViewListener() {
        lvDuties.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DutyActivity.class);
                intent.putExtra(DUTY_ID_EXTRA, position);
                intent.putExtra(DUTY_DATE_EXTRA, duties.get(position).getDate().getTime());
                intent.putExtra(DUTY_GROUP_EXTRA, "" + duties.get(position).getGroup());
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
