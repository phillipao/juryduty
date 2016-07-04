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

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

public class DutyActivity extends AppCompatActivity {
    private static final String TAG = "DutyActivity";
    public static final String DUTY_ID_EXTRA = "com.philoertel.sfjuryduty.DUTY";

    @Inject DutiesLoader dutiesLoader;
    @Inject InstructionsLoader instructionsLoader;
    private Duty mDuty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JuryDutyApplication.inject(this);
        setContentView(R.layout.activity_duty);

        Intent intent = getIntent();
        int position =  intent.getIntExtra(DUTY_ID_EXTRA, 0);
        List<Duty> duties = dutiesLoader.readDuties();
        mDuty = duties.get(position);

        initToolbar();
        displayDuty();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(formatDate(mDuty));
        setSupportActionBar(toolbar);
    }

    private void displayDuty() {
        // TODO: audit all of this for correct use of TZ
        DateTime now = new DateTime();
        int daysAhead = Days.daysBetween(now, new DateTime(mDuty.getDate().getTime())).getDays();

        if (daysAhead > 0) {
            getLayoutInflater().inflate(R.layout.view_future_duty,
                    (RelativeLayout) findViewById(R.id.dutyLayout));

            TextView daysLeftView = (TextView) findViewById(R.id.daysLeftView);
            daysLeftView.setText(daysAhead + "");
        } else if (daysAhead <= -4) {
            getLayoutInflater().inflate(R.layout.view_past_duty,
                    (RelativeLayout) findViewById(R.id.dutyLayout));

            TextView daysAgoView = (TextView) findViewById(R.id.daysAgoView);
            daysAgoView.setText(summarizeDutyTime(daysAhead)); // how many days ago it ended

            List<Instructions> instructionses = instructionsLoader.readInstructions();
            String groupCalledSummary = summarizeDutyOutcome(instructionses);
            TextView groupCalledView = (TextView) findViewById(R.id.groupCalledView);
            groupCalledView.setText(groupCalledSummary);
        } else {
            getLayoutInflater().inflate(R.layout.view_past_duty,
                    (RelativeLayout) findViewById(R.id.dutyLayout));

            TextView daysAgoView = (TextView) findViewById(R.id.daysAgoView);
            daysAgoView.setText(R.string.this_week);

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
        } else if (mDuty.getWeekInterval().contains(DateTime.now())) {
            return "Your number has not been called yet.";
        } else if (matchingInstructions == 0) {
            return "We don't have any data for that week.";
        } else {
            return "Data for that week is incomplete.";
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
                SimpleDateFormat.getDateInstance().format(duty.getDate()));
    }
}
