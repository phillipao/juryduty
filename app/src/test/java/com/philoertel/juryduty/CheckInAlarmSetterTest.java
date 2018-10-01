package com.philoertel.juryduty;

import android.app.AlarmManager;
import android.content.Context;

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
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlarmManager;

import java.util.ArrayList;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link CheckInAlarmSetter}.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,
        application = TestJuryDutyApplication.class)
public class CheckInAlarmSetterTest {

    @Rule public MockitoRule rule = MockitoJUnit.rule();
    @Mock private DutiesLoader dutyThisWeek;
    @Mock private DutiesLoader noDuty;

    private InstructionsLoader instructionsLoader;

    private ShadowAlarmManager shadowAlarmManager;

    private final ArrayList<Duty> duties = Lists.newArrayList(Duty.fromYearMonthDayGroup(2016, 1, 4, "666"));

    @Before
    public void setUp() {
        AlarmManager alarmManager = (AlarmManager)RuntimeEnvironment.application.getSystemService(Context.ALARM_SERVICE);
        shadowAlarmManager = Shadows.shadowOf(alarmManager);

        when(dutyThisWeek.readDuties()).thenReturn(duties);
        when(noDuty.readDuties()).thenReturn(Lists.<Duty>newArrayList());

        instructionsLoader = new InstructionsLoader(RuntimeEnvironment.application.getFilesDir());
    }

    @Test
    public void noDuty() {
        setter(makeDateTime(2016, 1, 3, 10), noDuty).setAlarms();
        assertThat(shadowAlarmManager.getNextScheduledAlarm()).isNull();
    }

    @Test
    public void sundayMorning() {
        setter(makeDateTime(2016, 1, 3, 10), dutyThisWeek).setAlarms();
        DateTime fivePm = makeDateTime(2016, 1, 3, 17);
        assertThat(triggerTime()).isEqualTo(fivePm);
    }

    @Test
    public void sundayNight() {
        DateTime now = makeDateTime(2016, 1, 3, 18);
        setter(now, dutyThisWeek).setAlarms();
        assertThat(triggerTime()).isLessThan(now);
    }

    @Test
    public void mondayMorning() {
        DateTime now = makeDateTime(2016, 1, 4, 10);
        setter(now, dutyThisWeek).setAlarms();
        assertThat(triggerTime()).isLessThan(now);
    }

    @Test
    public void mondayNight() {
        DateTime now = makeDateTime(2016, 1, 4, 18);
        setter(now, dutyThisWeek).setAlarms();
        assertThat(triggerTime()).isLessThan(now);
    }

    @Test
    public void tuesdayMorning() {
        DateTime now = makeDateTime(2016, 1, 5, 10);
        setter(now, dutyThisWeek).setAlarms();
        assertThat(triggerTime()).isLessThan(now);
    }

    @Test
    public void tuesdayNight() {
        DateTime now = makeDateTime(2016, 1, 5, 18);
        setter(now, dutyThisWeek).setAlarms();
        assertThat(triggerTime()).isLessThan(now);
    }

    @Test
    public void wednesdayMorning() {
        DateTime now = makeDateTime(2016, 1, 6, 10);
        setter(now, dutyThisWeek).setAlarms();
        assertThat(triggerTime()).isLessThan(now);
    }

    @Test
    public void wednesdayNight() {
        DateTime now = makeDateTime(2016, 1, 6, 18);
        setter(now, dutyThisWeek).setAlarms();
        assertThat(triggerTime()).isLessThan(now);
    }

    @Test
    public void thursdayMorning() {
        DateTime now = makeDateTime(2016, 1, 7, 10);
        setter(now, dutyThisWeek).setAlarms();
        assertThat(triggerTime()).isLessThan(now);
    }

    @Test
    public void thursdayNight() {
        DateTime now = makeDateTime(2016, 1, 7, 18);
        setter(now, dutyThisWeek).setAlarms();
        assertThat(triggerTime()).isLessThan(now);
    }

    @Test
    public void fridayMorning() {
        DateTime now = makeDateTime(2016, 1, 8, 10);
        setter(now, dutyThisWeek).setAlarms();
        assertThat(triggerTime()).isLessThan(now);
    }

    @Test
    public void sundayMorning_HaveMondayAlready() {
        instructionsLoader.saveInstructions(Lists.newArrayList(
                        new Instructions(makeDateTime(2016, 1, 4, 0), new ArrayList<String>())));
        setter(makeDateTime(2016, 1, 3, 10), dutyThisWeek).setAlarms();
        assertThat(triggerTime()).isEqualTo(makeDateTime(2016, 1, 4, 17));
    }

    @Test
    public void sundayNight_HaveMondayAlready() {
        instructionsLoader.saveInstructions(Lists.newArrayList(
                        new Instructions(makeDateTime(2016, 1, 4, 0), new ArrayList<String>())));
        setter(makeDateTime(2016, 1, 3, 18), dutyThisWeek).setAlarms();
        assertThat(triggerTime()).isEqualTo(makeDateTime(2016, 1, 4, 17));
    }

    @Test
    public void mondayNight_HaveTuesdayButNotMonday() {
        instructionsLoader.saveInstructions(Lists.newArrayList(
                        new Instructions(makeDateTime(2016, 1, 5, 0), new ArrayList<String>())));
        setter(makeDateTime(2016, 1, 4, 17), dutyThisWeek).setAlarms();
        assertThat(triggerTime()).isEqualTo(makeDateTime(2016, 1, 5, 17));
    }

    private CheckInAlarmSetter setter(DateTime now, DutiesLoader dutiesLoader) {
        return new CheckInAlarmSetter(now, RuntimeEnvironment.application, dutiesLoader, instructionsLoader);
    }

    private DateTime triggerTime() {
        return new DateTime(shadowAlarmManager.getNextScheduledAlarm().triggerAtTime, DateTimeZone.forID("US/Pacific"));
    }

    private static DateTime makeDateTime(int year, int month, int day, int hour) {
        return new DateTime(year, month, day, hour, 0, DateTimeZone.forID("US/Pacific"));
    }
}
