package com.philoertel.sfjuryduty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Calendar;

public class EditDutyActivity extends AppCompatActivity {
    private DutiesLoader mDutiesLoader;
    private ArrayList<Duty> mDuties;
    private DatePicker mDatePicker;
    private EditText mGroupView;
    private int mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_duty);

        Intent intent = getIntent();
        mPosition =  intent.getIntExtra(DutyActivity.DUTY_ID_EXTRA, 0);
        mDutiesLoader = new DutiesLoader(getFilesDir());
        mDuties = mDutiesLoader.readDuties();
        Duty duty = mDuties.get(mPosition);

        mDatePicker = (DatePicker) findViewById(R.id.edit_duty_date_picker);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(duty.getDate());
        mDatePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        mGroupView = (EditText) findViewById(R.id.edit_duty_group_number);
        mGroupView.setText(duty.getGroup());

        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_cancel);
        toolbar.setTitle(R.string.edit_view_title);
        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);
    }

    public void saveDuty(View v) {
        Duty duty = Duty.fromYearMonthDayGroup(mDatePicker.getYear(), mDatePicker.getMonth() + 1,
                mDatePicker.getDayOfMonth(), mGroupView.getText().toString());
        mDuties.set(mPosition, duty);
        mDutiesLoader.saveDuties(mDuties);

        Intent intent = new Intent(getApplicationContext(), DutyActivity.class);
        intent.putExtra(DutyActivity.DUTY_ID_EXTRA, mPosition);
        startActivity(intent);
    }
}
