package com.philoertel.juryduty;

import android.content.Intent;
import android.widget.TextView;

import com.google.common.collect.Lists;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

/** Tests for {@link DutyActivity}. */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,
        application = TestJuryDutyApplication.class)
public class DutyActivityTest {

    // 2016-01-04 through 2016-01-08
    private static Duty duty = new Duty("2016-W01", "200");
    private static final Intent INTENT = new Intent(Intent.ACTION_VIEW);
    static {
        INTENT.putExtra(DutyActivity.DUTY_ID_EXTRA, 0 /* position */);
    }

    @Mock private CheckInAlarmSetter mockCheckInAlarmSetter;
    @Rule public MockitoRule rule = MockitoJUnit.rule();
    private DateTime mockNow = new DateTime(2016, 1, 1, 12, 0, DateTimeZone.forID("US/Pacific"));

    @Before
    public void setUp() {
        DutiesLoader dutiesLoader = new DutiesLoader(RuntimeEnvironment.application.getFilesDir());
        InstructionsLoader instructionsLoader = new InstructionsLoader(RuntimeEnvironment.application.getFilesDir());

        TestJuryDutyApplication.checkInAlarmSetter = mockCheckInAlarmSetter;
        TestJuryDutyApplication.dutiesLoader = dutiesLoader;
        TestJuryDutyApplication.instructionsLoader = instructionsLoader;
        TestJuryDutyApplication.now = mockNow;

        dutiesLoader.saveDuties(Lists.newArrayList(duty));
    }

    @Test
    public void fourDaysFromNow() {
        TestJuryDutyApplication.now = new DateTime(2015, 12, 31, 12, 0,
                DateTimeZone.forID("US/Pacific"));
        DutyActivity activity = Robolectric.buildActivity(DutyActivity.class,
                INTENT).create().get();

        TextView daysLeftView = activity.findViewById(R.id.daysLeftView);
        assertThat(daysLeftView.getText().toString()).isEqualTo("4");
    }

    @Test
    public void inTwoDays() {
        TestJuryDutyApplication.now = new DateTime(2016, 1, 2, 12, 0,
                DateTimeZone.forID("US/Pacific"));
        DutyActivity activity = Robolectric.buildActivity(DutyActivity.class,
                INTENT).create().get();

        TextView daysAgoView = activity.findViewById(R.id.daysAgoView);
        assertThat(daysAgoView.getText().toString()).startsWith("Week of ");
    }

    @Test
    public void startsToday() {
        TestJuryDutyApplication.now = new DateTime(2016, 1, 4, 12, 0,
                DateTimeZone.forID("US/Pacific"));
        DutyActivity activity = Robolectric.buildActivity(DutyActivity.class,
                INTENT).create().get();

        TextView daysAgoView = activity.findViewById(R.id.daysAgoView);
        assertThat(daysAgoView.getText().toString()).isEqualTo("This week");
    }
}
