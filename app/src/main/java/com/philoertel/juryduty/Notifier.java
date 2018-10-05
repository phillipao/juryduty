package com.philoertel.juryduty;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateFormat;
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;

/**
 * Static class that creates notifications.
 */
public class Notifier {

    private static final String TAG = "Notifier";
    private static final String CHANNEL_ID = "12345";

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
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_action_add)
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
        builder.setContentIntent(dutyPendingIntent);
        int mNotificationId = 1;
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        createNotificationChannel(context);
        mNotifyMgr.notify(mNotificationId, builder.build());
    }

    /**
     * Creates a notification that instruction parsing failed, telling the user to head to the
     * court website.
     */
    static void createParsingErrorNotification(Context context) {
        Log.i(TAG, "Error parsing instructions");
        String notificationText = context.getString(R.string.parsing_error_notification);
        createNotificationLinkingToWebsite(context, notificationText);
    }

    /** Creates a notification that instructions aren't available. */
    static void createNoDataNotification(Context context, DateTime day) {
        Log.i(TAG, "Giving up for " + day.toString());
        String notificationText = context.getString(R.string.no_data_notification_text,
                DateTimeFormat.forPattern("MM/dd/yyyy").print(day));
        createNotificationLinkingToWebsite(context, notificationText);
    }

    private static void createNotificationLinkingToWebsite(Context context, String text) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_action_add)
                .setContentTitle(context.getString(R.string.jury_duty_notification_title))
                .setContentText(text)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text));
        Intent urlIntent = new Intent(Intent.ACTION_VIEW).setType("text/html").setData(
                Uri.parse(Downloader.INSTRUCTIONS_URL_STR));
        PendingIntent dutyPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        urlIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(dutyPendingIntent);
        int notificationId = 1;
        NotificationManager notifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel(context);
        notifyMgr.notify(notificationId, builder.build());
    }

    private static void createNotificationChannel(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private Notifier() {}
}
