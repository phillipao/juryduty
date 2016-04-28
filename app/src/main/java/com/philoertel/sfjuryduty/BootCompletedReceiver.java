package com.philoertel.sfjuryduty;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.joda.time.DateTime;

import java.util.Collection;

/**
 * BroadcastReceiver that receives BOOT_COMPLETED intent to set alarms for upcoming jury duty.
 */
public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        new NoDataAlarmSetter().setAlarms(context);
    }
}
