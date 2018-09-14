package com.philoertel.juryduty;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

/**
 * BroadcastReceiver that receives BOOT_COMPLETED intent to set alarms for upcoming jury duty.
 */
public class BootCompletedReceiver extends BroadcastReceiver {

    @Inject
    CheckInAlarmSetter checkInAlarmSetter;

    @Override
    public void onReceive(Context context, Intent intent) {
        checkInAlarmSetter.setAlarms(context);
    }
}
