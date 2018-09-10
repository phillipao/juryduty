package com.philoertel.sfjuryduty;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.joda.time.DateTime;

import java.util.Collection;

import javax.inject.Inject;

import static com.philoertel.sfjuryduty.Annotations.Now;

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
        alarmIntent.putExtra(CheckInAlarmReceiver.EXTRA_DATE, day);
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
        dlog("Setting alarms");
        DutiesLoader dutiesLoader = new DutiesLoader(context.getFilesDir());
        Collection<Duty> duties = dutiesLoader.readDuties();
        dlog("Loaded %d duties", duties.size());
        InstructionsLoader instructionsLoader = new InstructionsLoader(context.getFilesDir());
        Collection<Instructions> instructionses = instructionsLoader.readInstructions();
        dlog("Loaded %d instructions", instructionses.size());
        for (Duty duty : duties) {
            if (duty.getWeekInterval().isBefore(mNow)) {
                dlog("Skipping old week");
                continue;
            }
            DateTime day = duty.getWeekInterval().getStart();
            days:
            for (int i = 0; i++ < 5; day = day.plusDays(1)) {
                for (Instructions instructions : instructionses) {
                    if (instructions.getDateTime().isEqual(day)) {
                        if (instructions.getReportingGroups().contains(duty.getGroup())) {
                            dlog("already called");
                            break days;  // You won't be called again. Don't set an alarm.
                        } else {
                            dlog("instructions already present, but not called");
                            continue days;
                        }
                    }
                }
                if (day.minusHours(7).isBefore(mNow)) {
                    schedule(context, day, mNow.getMillis());
                } else {
                    schedule(context, day, day.minusHours(7).getMillis());
                }
                break;  // Just schedule one day
            }
        }
    }
}
