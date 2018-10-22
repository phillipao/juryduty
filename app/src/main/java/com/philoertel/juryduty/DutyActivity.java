package com.philoertel.juryduty;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.ReadableInstant;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.inject.Inject;

import static com.philoertel.juryduty.Annotations.Now;
import static java.text.DateFormat.getDateInstance;

public class DutyActivity extends AppCompatActivity implements Observer {
    public static final String DUTY_ID_EXTRA = "com.philoertel.juryduty.DUTY";

    @Inject CheckInAlarmSetter checkInAlarmSetter;
    @Inject DutiesLoader mDutiesLoader;
    @Inject InstructionsLoader mInstructionsLoader;
    @Inject @Now DateTime mNow;

    private Duty mDuty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((JuryDutyApplication) getApplication()).getComponent().inject(this);
        setContentView(R.layout.activity_duty);

        mInstructionsLoader.addObserver(this);

        Intent intent = getIntent();
        int position = intent.getIntExtra(DUTY_ID_EXTRA, 0);
        List<Duty> duties = mDutiesLoader.readDuties();
        mDuty = duties.get(position);

        initToolbar();
        displayDuty((RelativeLayout) findViewById(R.id.dutyLayout));
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(weekOf());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void displayDuty(RelativeLayout dutyLayout) {
        // TODO: audit all of this for correct use of TZ
        ReadableInstant normalizedNow = mNow.withTimeAtStartOfDay();
        int daysAhead = Days.daysBetween(normalizedNow,
                new DateTime(mDuty.getDate().getTime())).getDays();

        if (daysAhead > 3) {  // Period ending Thursday night, the week before.
            getLayoutInflater().inflate(R.layout.view_future_duty, dutyLayout);

            TextView daysLeftView = findViewById(R.id.daysLeftView);
            daysLeftView.setText(String.valueOf(daysAhead));
        } else if (daysAhead <= -4) {
            getLayoutInflater().inflate(R.layout.view_past_duty, dutyLayout);

            TextView daysAgoView = findViewById(R.id.daysAgoView);
            daysAgoView.setText(summarizeDutyTime(daysAhead)); // how many days ago it ended

            List<Instructions> instructionses = mInstructionsLoader.readInstructions();
            String groupCalledSummary = summarizeDutyOutcome(instructionses);
            TextView groupCalledView = findViewById(R.id.groupCalledView);
            groupCalledView.setText(groupCalledSummary);
        } else {
            getLayoutInflater().inflate(R.layout.view_current_duty, dutyLayout);

            TextView daysAgoView = findViewById(R.id.daysAgoView);
            if (daysAhead > 1) {  // So, Friday or Saturday before duty
                daysAgoView.setText(weekOf());
            } else {
                daysAgoView.setText(R.string.this_week);
            }

            List<Instructions> instructionses = mInstructionsLoader.readInstructions();
            summarizeDutyStatus(instructionses);
        }
        TextView groupView = findViewById(R.id.group);
        groupView.setText(mDuty.getGroup());
    }

    private String summarizeDutyTime(int startDaysAhead) {
        if (startDaysAhead > 2) {
            return getString(R.string.not_started_yet);
        } else if (startDaysAhead > -4) {
            return getString(R.string.this_week);
        } else if (startDaysAhead == -5) {
            return getString(R.string.ended_yesterday);
        } else {
            return getString(R.string.ended_n_days_ago, -startDaysAhead);
        }
    }

    private String summarizeDutyOutcome(Collection<Instructions> instructionses) {
        int matchingInstructions = 0;
        for (Instructions instructions : instructionses) {
            if (mDuty.overlapsWith(instructions)) {
                ++matchingInstructions;
                if (mDuty.calledBy(instructions)) {
                    return String.format(getString(R.string.your_group_was_called),
                            SimpleDateFormat.getDateInstance().format(
                                    instructions.getDateTime().toDate()));
                }
            }
        }
        if (matchingInstructions >= 5) {
            return getString(R.string.your_group_was_not_called);
        } else if (mDuty.getWeekInterval().contains(mNow)) {
            return getString(R.string.your_number_has_not_been_called);
        } else if (matchingInstructions == 0) {
            return getString(R.string.no_data_for_that_week);
        } else {
            return getString(R.string.data_is_incomplete);
        }
    }

    private void summarizeDutyStatus(Collection<Instructions> instructionses) {
        Map<DateTime, Instructions> instructionsByDate =
                InstructionsLoader.toMapByDate(instructionses);

        DateTime date = mDuty.getWeekInterval().getStart();
        setStatusView(date, (TextView) findViewById(R.id.mondayLabelView), instructionsByDate);
        setStatusView(date.plusDays(1), (TextView) findViewById(R.id.tuesdayLabelView), instructionsByDate);
        setStatusView(date.plusDays(2), (TextView) findViewById(R.id.wednesdayLabelView), instructionsByDate);
        setStatusView(date.plusDays(3), (TextView) findViewById(R.id.thursdayLabelView), instructionsByDate);
        setStatusView(date.plusDays(4), (TextView) findViewById(R.id.fridayLabelView), instructionsByDate);
    }

    private void setStatusView(DateTime date, TextView textView, Map<DateTime, Instructions> instructionsByDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String dayOfTheWeek = sdf.format(date.toDate());
        Instructions instructions = instructionsByDate.get(date);
        if (instructions == null) {
            textView.setText(getString(R.string.no_data, dayOfTheWeek));
        } else if (instructions.getReportingGroups().contains(mDuty.getGroup())) {
            textView.setText(getString(R.string.called, dayOfTheWeek));
        } else {
            textView.setText(getString(R.string.not_called, dayOfTheWeek));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_duty, menu);
        if (BuildConfig.DEBUG) {
            menu.add(Menu.NONE, R.id.action_debug, Menu.NONE, R.string.action_debugs);
        }
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

            case R.id.action_delete:
                deleteDuty();
                Toast toast = Toast.makeText(getApplicationContext(), R.string.toast_deleted, Toast.LENGTH_SHORT);
                toast.show();
                checkInAlarmSetter.setAlarms();
                finish();
                return true;

            case R.id.action_debug:
                intent = new Intent(getApplicationContext(), DebugActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteDuty() {
        Intent intent = getIntent();
        int position =  intent.getIntExtra(DUTY_ID_EXTRA, 0);
        List<Duty> duties = mDutiesLoader.readDuties();
        duties.remove(position);
        mDutiesLoader.saveDuties(duties);
    }

    private String weekOf() {
        return String.format(getString(R.string.week_of),
                getDateInstance().format(mDuty.getDate()));
    }

    @Override
    public void update(@Nullable Observable observable, Object o) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                displayDuty((RelativeLayout) findViewById(R.id.dutyLayout));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mInstructionsLoader.deleteObserver(this);
    }
}
