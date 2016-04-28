package com.philoertel.sfjuryduty;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import org.joda.time.DateTime;

import java.util.Collection;

/**
 * Class to set NoDataAlarms for all pending duties.
 */
class NoDataAlarmSetter {

    public void setAlarms(Context context) {
        DutiesLoader dutiesLoader = new DutiesLoader(context.getFilesDir());
        Collection<Duty> duties = dutiesLoader.readDuties();
        InstructionsLoader instructionsLoader = new InstructionsLoader(context.getFilesDir());
        Collection<Instructions> instructionses = instructionsLoader.readInstructions();
        for (Duty duty : duties) {
            if (duty.getWeekInterval().isBeforeNow()) {
                continue;
            }
            DateTime day = duty.getWeekInterval().getStart();
            days:
            for (int i = 0; i++ < 5; day = day.plusDays(1)) {
                for (Instructions instructions : instructionses) {
                    if (instructions.getDateTime().isEqual(day)) {
                        if (instructions.getReportingGroups().contains(duty.getGroup())) {
                            break days;  // You won't be called again. Don't set an alarm.
                        } else {
                            continue days;
                        }
                    }
                }
                if (day.minusHours(5).isBeforeNow()) {
                    continue;
                }
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(
                        Context.ALARM_SERVICE);
                Intent alarmIntent = new Intent(context, NoDataAlarmReceiver.class);
                alarmIntent.putExtra(NoDataAlarmReceiver.EXTRA_DATE, day);
                PendingIntent pi = PendingIntent.getBroadcast(context, 0,
                        alarmIntent, 0);

                alarmManager.set(AlarmManager.RTC, day.minusHours(5).getMillis(), pi);
            }
        }
    }
}
