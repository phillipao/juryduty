package com.philoertel.sfjuryduty;

import android.content.Intent;
import android.widget.TextView;

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
    @Mock private DutiesLoader mockDutiesLoader;
    @Mock private InstructionsLoader mockInstructionsLoader;
    @Rule public MockitoRule rule = MockitoJUnit.rule();
    private DateTime mockNow = new DateTime(2016, 1, 1, 12, 0, DateTimeZone.forID("US/Pacific"));

    @Before
    public void setUp() {
        TestJuryDutyApplication.checkInAlarmSetter = mockCheckInAlarmSetter;
        TestJuryDutyApplication.dutiesLoader = mockDutiesLoader;
        TestJuryDutyApplication.instructionsLoader = mockInstructionsLoader;
        TestJuryDutyApplication.now = mockNow;

        ArrayList<Duty> duties = new ArrayList<>();
        duties.add(duty);
        when(mockDutiesLoader.readDuties()).thenReturn(duties);
        ArrayList<Instructions> instructions = new ArrayList<>();
        when(mockInstructionsLoader.readInstructions()).thenReturn(instructions);
    }

    @Test
    public void threeDaysFromNow() {
        TestJuryDutyApplication.now = new DateTime(2016, 1, 1, 12, 0,
                DateTimeZone.forID("US/Pacific"));
        DutyActivity activity = Robolectric.buildActivity(DutyActivity.class).withIntent(
                INTENT).create().get();

        TextView daysLeftView = (TextView) activity.findViewById(R.id.daysLeftView);
        assertThat(daysLeftView.getText().toString()).isEqualTo("3");
    }

    @Test
    public void inTwoDays() {
        TestJuryDutyApplication.now = new DateTime(2016, 1, 2, 12, 0,
                DateTimeZone.forID("US/Pacific"));
        DutyActivity activity = Robolectric.buildActivity(DutyActivity.class).withIntent(
                INTENT).create().get();

        TextView daysLeftView = (TextView) activity.findViewById(R.id.daysLeftView);
        assertThat(daysLeftView.getText().toString()).isEqualTo("2");
    }

    @Test
    public void startsToday() {
        TestJuryDutyApplication.now = new DateTime(2016, 1, 4, 12, 0,
                DateTimeZone.forID("US/Pacific"));
        DutyActivity activity = Robolectric.buildActivity(DutyActivity.class).withIntent(
                INTENT).create().get();

        TextView daysAgoView = (TextView) activity.findViewById(R.id.daysAgoView);
        assertThat(daysAgoView.getText().toString()).isEqualTo("This week");
    }
}
