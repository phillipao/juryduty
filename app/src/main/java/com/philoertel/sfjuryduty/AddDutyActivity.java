package com.philoertel.sfjuryduty;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddDutyActivity extends AppCompatActivity {
    private DutiesLoader dutiesLoader;
    private ArrayList<Duty> duties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_duty);
        dutiesLoader = new DutiesLoader(getFilesDir());
        duties = dutiesLoader.readDuties();

        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.action_add_duty);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_cancel);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home: {
                Context context = getApplicationContext();
                CharSequence text = "Canceled";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                finish();
                return true;
            }

            case R.id.action_save: {
                addDuty();
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void addDuty(View v) {
        addDuty();
    }

    private void addDuty() {
        EditText etNewDuty = (EditText) findViewById(R.id.newDutyNumberText);
        DatePicker datePicker = (DatePicker) findViewById(R.id.newDutyDatePicker);
        Duty duty = Duty.fromYearMonthDayGroup(datePicker.getYear(), datePicker.getMonth() + 1,
                datePicker.getDayOfMonth(), etNewDuty.getText().toString());
        duties.add(duty);
        dutiesLoader.saveDuties(duties);

        InstructionsLoader loader = new InstructionsLoader(getFilesDir());
        List<Instructions> instructionses = loader.readInstructions();
        for (Instructions instructions : instructionses) {
            if (duty.calledBy(instructions)) {
                SyncAdapter.createNotification(this, instructions);
            }
        }

        Toast toast = Toast.makeText(getApplicationContext(), "Saved!", Toast.LENGTH_SHORT);
        toast.show();
        Intent intent = new Intent(getApplicationContext(), DutyActivity.class);
        intent.putExtra(DutyActivity.DUTY_ID_EXTRA, duties.size() - 1);
        startActivity(intent);
    }
}
