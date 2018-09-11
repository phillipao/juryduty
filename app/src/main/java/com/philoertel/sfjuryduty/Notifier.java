package com.philoertel.sfjuryduty;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.text.format.DateFormat;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;

/**
 * Static class that creates notifications.
 */
public class Notifier {
    static void createPositiveNotification(Context context, int dutyIndex, DateTime dateTime) {
        createNotification(context, dutyIndex,
                context.getString(R.string.jury_duty_notice, formatDate(context, dateTime)));
    }

    static void createNegativeNotification(Context context, int dutyIndex, DateTime dateTime) {
        createNotification(context, dutyIndex,
                context.getString(R.string.no_jury_duty_notice, formatDate(context, dateTime)));
    }

    private static String formatDate(Context context, DateTime dateTime) {
        SimpleDateFormat format = (SimpleDateFormat) DateFormat.getDateFormat(context.getApplicationContext());
        String pattern = format.toPattern();
        DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern);
        return dateTime.toString(fmt);
    }

    private static void createNotification(Context context, int dutyIndex, String message) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.drawable.ic_action_add)
                .setContentTitle(context.getString(R.string.jury_duty_notification_title))
                .setContentText(message)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        Intent dutyIntent = new Intent(context, DutyActivity.class);
        dutyIntent.putExtra(DutyActivity.DUTY_ID_EXTRA, dutyIndex);
        PendingIntent dutyPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        dutyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(dutyPendingIntent);
        int mNotificationId = 1;
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    private Notifier() {}
}
