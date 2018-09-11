package com.philoertel.sfjuryduty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.ReadableInstant;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static com.philoertel.sfjuryduty.Annotations.Now;
import static java.text.DateFormat.getDateInstance;

public class DutyActivity extends AppCompatActivity {
    private static final String TAG = "DutyActivity";
    public static final String DUTY_ID_EXTRA = "com.philoertel.sfjuryduty.DUTY";

    @Inject DutiesLoader mDutiesLoader;
    @Inject InstructionsLoader mInstructionsLoader;
    @Inject @Now DateTime mNow;

    private Duty mDuty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((JuryDutyApplication) getApplication()).getComponent().inject(this);
        setContentView(R.layout.activity_duty);

        Intent intent = getIntent();
        int position =  intent.getIntExtra(DUTY_ID_EXTRA, 0);
        List<Duty> duties = mDutiesLoader.readDuties();
        mDuty = duties.get(position);

        initToolbar();
        displayDuty((RelativeLayout) findViewById(R.id.dutyLayout));
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(formatDate(mDuty));
        setSupportActionBar(toolbar);
    }

    private void displayDuty(RelativeLayout dutyLayout) {
        // TODO: audit all of this for correct use of TZ
        ReadableInstant normalizedNow = mNow.withTimeAtStartOfDay();
        int daysAhead = Days.daysBetween(normalizedNow,
                new DateTime(mDuty.getDate().getTime())).getDays();

        if (daysAhead > 1) {
            getLayoutInflater().inflate(R.layout.view_future_duty, dutyLayout);

            TextView daysLeftView = (TextView) findViewById(R.id.daysLeftView);
            daysLeftView.setText(daysAhead + "");
        } else if (daysAhead <= -4) {
            getLayoutInflater().inflate(R.layout.view_past_duty, dutyLayout);

            TextView daysAgoView = (TextView) findViewById(R.id.daysAgoView);
            daysAgoView.setText(summarizeDutyTime(daysAhead)); // how many days ago it ended

            List<Instructions> instructionses = mInstructionsLoader.readInstructions();
            String groupCalledSummary = summarizeDutyOutcome(instructionses);
            TextView groupCalledView = (TextView) findViewById(R.id.groupCalledView);
            groupCalledView.setText(groupCalledSummary);
        } else {
            getLayoutInflater().inflate(R.layout.view_current_duty, dutyLayout);

            TextView daysAgoView = (TextView) findViewById(R.id.daysAgoView);
            daysAgoView.setText(R.string.this_week);

            List<Instructions> instructionses = mInstructionsLoader.readInstructions();
            summarizeDutyStatus(instructionses);
        }
        TextView groupView = (TextView) findViewById(R.id.group);
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
                    return String.format("Your group was called on %s.",
                            SimpleDateFormat.getDateInstance().format(
                                    instructions.getDateTime().toDate()));
                }
            }
        }
        if (matchingInstructions >= 5) {
            return "Your group was not called.";
        } else if (mDuty.getWeekInterval().contains(mNow)) {
            return "Your number has not been called yet.";
        } else if (matchingInstructions == 0) {
            return "We don't have any data for that week.";
        } else {
            return "Data for that week is incomplete.";
        }
    }

    private void summarizeDutyStatus(Collection<Instructions> instructionses) {
        Map<DateTime, Instructions> instructionsByDate = new HashMap<>();
        for (Instructions i : instructionses) {
            instructionsByDate.put(i.getDateTime(), i);
        }

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

            case R.id.action_debug:
                intent = new Intent(getApplicationContext(), DebugActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_duties:
                intent = new Intent(getApplicationContext(), DutiesActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private String formatDate(Duty duty) {
        return String.format("Week of %s",
                getDateInstance().format(duty.getDate()));
    }
}
