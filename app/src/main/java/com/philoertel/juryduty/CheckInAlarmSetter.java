package com.philoertel.juryduty;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

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

    private static void dlog(String msg, Object... args) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format(msg, args));
        }
    }

    /**
     * Looks at all saved duties and instructions and schedules the next alarm in AlarmManager.
     *
     * This incurs some disk actvity to load duties and instructions, but it's the easiest way to
     * schedule alarms.
     *
     * The only time it's not appropriate to call this is when rescheduling a failed check-in,
     * because it doesn't know about previous attempts, so it doesn't implement any back-off logic.
     * In that case, use {@link #schedule(DateTime, long)} directly.
     */
    void setAlarms() {
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
                scheduleForDay(day);
                break dutyLoop;  // Just schedule one day
            }
        }
    }

    /**
     * Schedules check-in for the given day.
     *
     * The main thing this does is know when to check in for each day. That's
     * generally the evening before, or the Friday before, for a Monday.
     */
    private void scheduleForDay(DateTime day) {
        if (day.getDayOfWeek() == DateTimeConstants.MONDAY) {
            schedule(day, day.minusDays(2).minusHours(7).getMillis());
        } else {
            schedule(day, day.minusHours(7).getMillis());
        }
    }

    /**
     * Schedule an alarm at {@code when} for {@code day}.
     *
     * It's generally easier to use {@link #setAlarms()}, which figures out when to set the next
     * alarm. This is used for retries.
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
