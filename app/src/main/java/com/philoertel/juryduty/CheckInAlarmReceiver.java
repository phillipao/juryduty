package com.philoertel.juryduty;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.inject.Inject;

import static com.philoertel.juryduty.Annotations.Now;

/**
 * Receives an alarm that it's time to check in.
 * <p>
 * If instructions aren't ready, tries to check in again and again, eventually notifying the user.
 * <p>
 * When instructions are found, process them. Give instructions to the user. Possibly set an alarm
 * for the following day.
 */
public class CheckInAlarmReceiver extends BroadcastReceiver {

    public static final String EXTRA_DATE =
            "com.philoertel.juryduty.CheckInAlarmReceiver.EXTRA_DATE";
    private static final String TAG = "CheckInAlarmReceiver";
    @Inject @Now DateTime mNow;
    @Inject CheckInAlarmSetter mCheckInAlarmSetter;
    @Inject CountDownLatch mLatch;
    @Inject Downloader mDownloader;
    @Inject DutiesLoader mDutiesLoader;
    @Inject InstructionsLoader mInstructionsLoader;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        ((JuryDutyApplication) context.getApplicationContext()).getComponent().inject(this);
        AsyncTask.execute(new Runnable() {

            @Override
            public void run() {
                try {
                    receiveAsync(intent, context);
                } finally {
                    mLatch.countDown();
                }
            }
        });
    }

    private void receiveAsync(Intent intent, Context context) {
        Log.d(TAG, "Starting CheckInAlarmReceiver.onReceive");
        String dtStr = intent.getStringExtra(EXTRA_DATE);
        if (dtStr == null) {
            Log.e(TAG, "Received null date");
            return;
        }
        DateTime day = DateTime.parse(dtStr);
        Log.d(TAG, "Received date " + day);
        // TODO: Rewrite using Volley, to better handle network being down.
        String content;
        try {
            content = mDownloader.downloadUrl();
        } catch (IOException e) {
            Log.e(TAG, "Failed to download instructions", e);
            if (giveUp(context, day)) {
                return;
            }
            Log.i(TAG, "Rescheduling in 5 minutes");
            mCheckInAlarmSetter.schedule(day, mNow.getMillis() + 1000 * 60 * 5);
            return;
        }

        Instructions parsedInstructions = null;
        try {
            parsedInstructions = Parser.parseInstructions(content);
        } catch (Parser.ParseException e) {
            Log.w(TAG, "Error parsing instructions. Rescheduling in 8h", e);
            mCheckInAlarmSetter.schedule(day, mNow.getMillis() + 1000 * 60 * 60 * 8);
            Notifier.createParsingErrorNotification(context);
            return;
        }
        saveNewInstructions(parsedInstructions, context);

        if (parsedInstructions.getDateTime().isBefore(day)) {
            Log.i(TAG, String.format("Instructions weren't ready yet (wanted for %s, got for %s)",
                    day.toString(), parsedInstructions.getDateTime().toString()));
            if (giveUp(context, day)) {
                return;
            }
            Log.i(TAG, "Rescheduling in 15 minutes");
            mCheckInAlarmSetter.schedule(day, mNow.getMillis() + 1000 * 60 * 15);
            return;
        }

        List<Duty> duties = mDutiesLoader.readDuties();
        for (int i = 0; i < duties.size(); ++i) {
            Duty duty = duties.get(i);
            if (duty.overlapsWith(parsedInstructions)) {
                if (duty.calledBy(parsedInstructions)) {
                    Notifier.createPositiveNotification(context, i, parsedInstructions.getDateTime());
                    return;  // no more alarms! you've been called!
                } else {
                    Notifier.createNegativeNotification(context, i, parsedInstructions.getDateTime());
                }
            }
        }
        mCheckInAlarmSetter.setAlarms();
    }

    /**
     * Saves the new instructions. Also dedups existing instructions by date.
     */
    private void saveNewInstructions(Instructions newInstructions, Context context) {
        List<Instructions> instructionses = mInstructionsLoader.readInstructions();
        Map<String, Instructions> instructionsByDate = new HashMap<>();
        for (Instructions i : instructionses) {
            instructionsByDate.put(i.getDateString(), i);
        }
        instructionsByDate.put(newInstructions.getDateString(), newInstructions);
        mInstructionsLoader.saveInstructions(new ArrayList<>(instructionsByDate.values()));
    }

    private boolean giveUp(Context context, DateTime day) {
        if (mNow.getHourOfDay() >= 19) {
            Notifier.createNoDataNotification(context, day);
            mCheckInAlarmSetter.setAlarms();
            return true;
        } else {
            return false;
        }
    }
}
