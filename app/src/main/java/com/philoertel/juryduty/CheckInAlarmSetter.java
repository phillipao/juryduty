package com.philoertel.juryduty;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.joda.time.DateTime;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static com.philoertel.juryduty.Annotations.Now;

/**
 * Class to set CheckInAlarms for all pending duties.
 */
class CheckInAlarmSetter {

    private static final String TAG = "CheckInAlarmSetter";

    private DateTime mNow;

    @Inject
    public CheckInAlarmSetter(@Now DateTime now) {
        this.mNow = now;
    }

    /**
     * Schedule an alarm at {@code when} for {@code day}.
     */
    static void schedule(Context context, DateTime day, long when) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(
                Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, CheckInAlarmReceiver.class);
        alarmIntent.putExtra(CheckInAlarmReceiver.EXTRA_DATE, day.toString());
        PendingIntent pi = PendingIntent.getBroadcast(context, 0,
                alarmIntent, 0);

        alarmManager.set(AlarmManager.RTC, when, pi);
    }

    static void dlog(String msg, Object... args) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format(msg, args));
        }
    }

    public void setAlarms(Context context) {
        DutiesLoader dutiesLoader = new DutiesLoader(context.getFilesDir());
        List<Duty> duties = dutiesLoader.readDuties();
        dlog("Loaded %d duties", duties.size());
        sortByDate(duties);

        InstructionsLoader instructionsLoader = new InstructionsLoader(context.getFilesDir());
        Map<DateTime, Instructions> instByDate =
                InstructionsLoader.toMapByDate(instructionsLoader.readInstructions());
        dlog("Loaded %d instructions", instByDate.size());

        dutyLoop:
        for (Duty duty : duties) {
            if (duty.getWeekInterval().isBefore(mNow)) {
                dlog("Skipping old week");
                continue;
            }
            DateTime day = duty.getWeekInterval().getStart();
            for (int i = 0; i++ < 5; day = day.plusDays(1)) {
                Instructions instructions = instByDate.get(day);
                if (instructions != null) {
                    if (instructions.getReportingGroups().contains(duty.getGroup())) {
                        dlog("already called");
                        break;  // You won't be called again. Don't set an alarm.
                    } else {
                        dlog("instructions already present, but not called");
                        continue;
                    }
                }
                if (mNow.isAfter(day.plusDays(1))) {
                    continue;  // too late. missed 'em.
                }
                schedule(context, day, day.minusHours(7).getMillis());
                break dutyLoop;  // Just schedule one day
            }
        }
    }

    private void sortByDate(List<Duty> input) {
        Collections.sort(input, new Comparator<Duty>() {
            public int compare(Duty duty1, Duty duty2) {
                return duty1.getDate().compareTo(duty2.getDate());
            }
        });
    }
}
