package com.philoertel.sfjuryduty;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import org.joda.time.DateTime;

import java.util.Collection;

/**
 * Receives an alarm the evening before the next jury duty day. If instructions haven't been posted
 * yet, sends a notification with a warning and a link to the website.
 */
public class NoDataAlarmReceiver extends BroadcastReceiver {

    public static final String EXTRA_DATE =
            "com.philoertel.sfjuryduty.NoDataAlarmReceiver.EXTRA_DATE";
    private static final String TAG = "NoDataAlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Starting NoDataAlarmReceiver.onReceive");
        DateTime day = (DateTime) intent.getSerializableExtra(EXTRA_DATE);
        InstructionsLoader instructionsLoader = new InstructionsLoader(context.getFilesDir());
        Collection<Instructions> instructionses = instructionsLoader.readInstructions();
        boolean hasInstructions = hasInstructionsForDay(day, instructionses);
        Log.d(TAG,
                "Instructions " + (hasInstructions ? "were" : "were not") + " present for " + day);
        if (!hasInstructions) {
            createNotification(context);
        }
        // If not called, set an alarm for the following day.
    }

    private boolean hasInstructionsForDay(DateTime day, Collection<Instructions> instructionses) {
        for (Instructions instructions : instructionses) {
            if (instructions.getDateTime().isEqual(day)) {
                return true;
            }
        }
        return false;
    }

    private void createNotification(Context context) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.drawable.ic_action_add)
                .setContentTitle(context.getString(R.string.jury_duty_notification_title))
                .setContentText(context.getString(R.string.no_data_notification_text))
                .setAutoCancel(true);
        Intent urlIntent = new Intent(Intent.ACTION_VIEW).setType("text/html").setData(
                Uri.parse("http://www.sfsuperiorcourt.org/divisions/jury-services/jury-reporting"));
        PendingIntent dutyPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        urlIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(dutyPendingIntent);
        int mNotificationId = 1;
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
}
