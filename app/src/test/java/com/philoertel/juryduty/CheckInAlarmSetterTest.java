package com.philoertel.juryduty;

import android.app.AlarmManager;
import android.content.Context;

import com.google.common.collect.Lists;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlarmManager;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

/**
 * Tests for {@link CheckInAlarmSetter}.
 */
@RunWith(RobolectricTestRunner.class)
@Config(application = TestJuryDutyApplication.class)
public class CheckInAlarmSetterTest {

    private DutiesLoader dutiesLoader;
    private InstructionsLoader instructionsLoader;

    private static final List<Duty> NO_DUTY = Lists.newArrayList();
    private static final ArrayList<Duty> DUTY_THIS_WEEK = Lists.newArrayList(Duty.fromYearMonthDayGroup(2016, 1, 4, "666"));

    private ShadowAlarmManager shadowAlarmManager;

    @Before
    public void setUp() {
        AlarmManager alarmManager = (AlarmManager)RuntimeEnvironment.application.getSystemService(Context.ALARM_SERVICE);
        shadowAlarmManager = Shadows.shadowOf(alarmManager);

        dutiesLoader = new DutiesLoader(RuntimeEnvironment.application.getFilesDir());
        instructionsLoader = new InstructionsLoader(RuntimeEnvironment.application.getFilesDir());
    }

    @Test
    public void noDuty() {
        setter(makeDateTime(2016, 1, 3, 10), NO_DUTY).setAlarms();
        assertThat(shadowAlarmManager.getNextScheduledAlarm()).isNull();
    }

    @Test
    public void fridayMorningWeekBefore() {
        setter(makeDateTime(2016, 1, 1, 10), DUTY_THIS_WEEK).setAlarms();
        DateTime fivePm = makeDateTime(2016, 1, 1, 17);
        assertThat(triggerTime()).isEqualTo(fivePm);
    }

    @Test
    public void fridayNightWeekBefore() {
        DateTime now = makeDateTime(2016, 1, 1, 18);
        setter(now, DUTY_THIS_WEEK).setAlarms();
        assertThat(triggerTime()).isLessThan(now);
    }

    @Test
    public void sundayMorning() {
        DateTime now = makeDateTime(2016, 1, 3, 10);
        setter(now, DUTY_THIS_WEEK).setAlarms();
        DateTime fivePm = makeDateTime(2016, 1, 3, 17);
        assertThat(triggerTime()).isLessThan(now);
    }

    @Test
    public void sundayNight() {
        DateTime now = makeDateTime(2016, 1, 3, 18);
        setter(now, DUTY_THIS_WEEK).setAlarms();
        assertThat(triggerTime()).isLessThan(now);
    }

    @Test
    public void mondayMorning() {
        DateTime now = makeDateTime(2016, 1, 4, 10);
        setter(now, DUTY_THIS_WEEK).setAlarms();
        assertThat(triggerTime()).isLessThan(now);
    }

    @Test
    public void mondayNight() {
        DateTime now = makeDateTime(2016, 1, 4, 18);
        setter(now, DUTY_THIS_WEEK).setAlarms();
        assertThat(triggerTime()).isLessThan(now);
    }

    @Test
    public void tuesdayMorning() {
        DateTime now = makeDateTime(2016, 1, 5, 10);
        setter(now, DUTY_THIS_WEEK).setAlarms();
        assertThat(triggerTime()).isLessThan(now);
    }

    @Test
    public void tuesdayNight() {
        DateTime now = makeDateTime(2016, 1, 5, 18);
        setter(now, DUTY_THIS_WEEK).setAlarms();
        assertThat(triggerTime()).isLessThan(now);
    }

    @Test
    public void wednesdayMorning() {
        DateTime now = makeDateTime(2016, 1, 6, 10);
        setter(now, DUTY_THIS_WEEK).setAlarms();
        assertThat(triggerTime()).isLessThan(now);
    }

    @Test
    public void wednesdayNight() {
        DateTime now = makeDateTime(2016, 1, 6, 18);
        setter(now, DUTY_THIS_WEEK).setAlarms();
        assertThat(triggerTime()).isLessThan(now);
    }

    @Test
    public void thursdayMorning() {
        DateTime now = makeDateTime(2016, 1, 7, 10);
        setter(now, DUTY_THIS_WEEK).setAlarms();
        assertThat(triggerTime()).isLessThan(now);
    }

    @Test
    public void thursdayNight() {
        DateTime now = makeDateTime(2016, 1, 7, 18);
        setter(now, DUTY_THIS_WEEK).setAlarms();
        assertThat(triggerTime()).isLessThan(now);
    }

    @Test
    public void fridayMorning() {
        DateTime now = makeDateTime(2016, 1, 8, 10);
        setter(now, DUTY_THIS_WEEK).setAlarms();
        assertThat(triggerTime()).isLessThan(now);
    }

    @Test
    public void sundayMorning_HaveMondayAlready() {
        instructionsLoader.saveInstructions(Lists.newArrayList(
                        new Instructions(makeDateTime(2016, 1, 4, 0), new ArrayList<String>())));
        setter(makeDateTime(2016, 1, 3, 10), DUTY_THIS_WEEK).setAlarms();
        assertThat(triggerTime()).isEqualTo(makeDateTime(2016, 1, 4, 17));
    }

    @Test
    public void sundayNight_HaveMondayAlready() {
        instructionsLoader.saveInstructions(Lists.newArrayList(
                        new Instructions(makeDateTime(2016, 1, 4, 0), new ArrayList<String>())));
        setter(makeDateTime(2016, 1, 3, 18), DUTY_THIS_WEEK).setAlarms();
        assertThat(triggerTime()).isEqualTo(makeDateTime(2016, 1, 4, 17));
    }

    @Test
    public void mondayNight_HaveTuesdayButNotMonday() {
        instructionsLoader.saveInstructions(Lists.newArrayList(
                        new Instructions(makeDateTime(2016, 1, 5, 0), new ArrayList<String>())));
        setter(makeDateTime(2016, 1, 4, 17), DUTY_THIS_WEEK).setAlarms();
        assertThat(triggerTime()).isEqualTo(makeDateTime(2016, 1, 5, 17));
    }

    private CheckInAlarmSetter setter(DateTime now, List<Duty> duties) {
        dutiesLoader.saveDuties(duties);
        return new CheckInAlarmSetter(now, RuntimeEnvironment.application, dutiesLoader, instructionsLoader);
    }

    private DateTime triggerTime() {
        return new DateTime(shadowAlarmManager.getNextScheduledAlarm().triggerAtTime, DateTimeZone.forID("US/Pacific"));
    }

    private static DateTime makeDateTime(int year, int month, int day, int hour) {
        return new DateTime(year, month, day, hour, 0, DateTimeZone.forID("US/Pacific"));
    }
}
