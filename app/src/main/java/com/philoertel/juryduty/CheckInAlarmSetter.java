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
    private Context mContext;
    private final DutiesLoader mDutiesLoader;
    private final InstructionsLoader mInstructionsLoader;

    @Inject
    public CheckInAlarmSetter(
            @Now DateTime now,
            Context context,
            DutiesLoader dutiesLoader,
            InstructionsLoader instructionsLoader) {
        this.mNow = now;
        this.mContext = context;
        this.mDutiesLoader = dutiesLoader;
        this.mInstructionsLoader = instructionsLoader;
    }

    /**
     * Schedule an alarm at {@code when} for {@code day}.
     */
    void schedule(DateTime day, long when) {
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(
                Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(mContext, CheckInAlarmReceiver.class);
        alarmIntent.putExtra(CheckInAlarmReceiver.EXTRA_DATE, day.toString());
        PendingIntent pi = PendingIntent.getBroadcast(mContext, 0,
                alarmIntent, 0);

        alarmManager.set(AlarmManager.RTC, when, pi);
    }

    private static void dlog(String msg, Object... args) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format(msg, args));
        }
    }

    public void setAlarms() {
        List<Duty> duties = mDutiesLoader.readDuties();
        dlog("Loaded %d duties", duties.size());
        sortByDate(duties);

        List<Instructions> instructionsList = mInstructionsLoader.readInstructions();
        Map<DateTime, Instructions> instByDate = InstructionsLoader.toMapByDate(instructionsList);
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
                if (mNow.isAfter(day.plusDays(1)) || laterInstructionsHaveBeenSeen(day, instructionsList)) {
                    continue;  // too late. missed 'em.
                }
                schedule(day, day.minusHours(7).getMillis());
                break dutyLoop;  // Just schedule one day
            }
        }
    }

    private boolean laterInstructionsHaveBeenSeen(DateTime day, List<Instructions> instructionsList) {
        for (Instructions instructions : instructionsList) {
            if (instructions.getDateTime().isAfter(day)) {
                return true;
            }
        }
        return false;
    }

    private void sortByDate(List<Duty> input) {
        Collections.sort(input, new Comparator<Duty>() {
            public int compare(Duty duty1, Duty duty2) {
                return duty1.getDate().compareTo(duty2.getDate());
            }
        });
    }
}
