package com.philoertel.juryduty;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.joda.time.DateTime;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import static com.philoertel.juryduty.Annotations.Now;

/**
 * Receives an alarm that it's time to check in.
 * <p>
 * If instructions aren't ready, tries to check in again and again, evntually notifying the user.
 * <p>
 * When instructions are found, process them. Give instructions to the user. Possibly set an alarm
 * for the following day.
 */
public class CheckInAlarmReceiver extends BroadcastReceiver {

    public static final String EXTRA_DATE =
            "com.philoertel.juryduty.CheckInAlarmReceiver.EXTRA_DATE";
    public static final String INSTRUCTIONS_URL_STR =
            "https://www.sfsuperiorcourt.org/divisions/jury-services/jury-reporting";
    private static final String TAG = "CheckInAlarmReceiver";
    @Inject @Now DateTime mNow;
    @Inject CheckInAlarmSetter mCheckInAlarmSetter;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        ((JuryDutyApplication) context.getApplicationContext()).getComponent().inject(this);
        AsyncTask.execute(new Runnable() {

            @Override
            public void run() {
                Log.d(TAG, "Starting CheckInAlarmReceiver.onReceive");
                String dtStr = intent.getStringExtra(EXTRA_DATE);
                if (dtStr == null) {
                    Log.e(TAG, "Received null date");
                    return;
                }
                DateTime day = DateTime.parse(dtStr);
                Log.d(TAG, "Received date " + day);
                URL instructionsUrl;
                try {
                    instructionsUrl = new URL(INSTRUCTIONS_URL_STR);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
                // TODO: Rewrite using Volley, to better handle network being down.
                String content;
                try {
                    content = downloadUrl(instructionsUrl);
                } catch (IOException e) {
                    Log.e(TAG, "Failed to download instructions", e);
                    if (giveUp(context, day)) {
                        return;
                    }
                    Log.i(TAG, "Rescheduling in 5 minutes");
                    CheckInAlarmSetter.schedule(context, day, mNow.getMillis() + 1000 * 60 * 5);
                    return;
                }

                Instructions parsedInstructions = Parser.parseInstructions(content);
                saveNewInstructions(parsedInstructions, context);

                if (parsedInstructions.getDateTime().isBefore(day)) {
                    Log.i(TAG, String.format("Instructions weren't ready yet (wanted for %s, got for %s)",
                            day.toString(), parsedInstructions.getDateTime().toString()));
                    if (giveUp(context, day)) {
                        return;
                    }
                    Log.i(TAG, "Rescheduling in 15 minutes");
                    CheckInAlarmSetter.schedule(context, day, mNow.getMillis() + 1000 * 60 * 15);
                    return;
                }

                DutiesLoader dutiesLoader = new DutiesLoader(context.getFilesDir());
                List<Duty> duties = dutiesLoader.readDuties();
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
                mCheckInAlarmSetter.setAlarms(context);
            }
        });
    }

    /**
     * Saves the new instructions. Also dedups existing instructions by date.
     */
    private void saveNewInstructions(Instructions newInstructions, Context context) {
        InstructionsLoader instructionsLoader = new InstructionsLoader(context.getFilesDir());
        List<Instructions> instructionses = instructionsLoader.readInstructions();
        Map<String, Instructions> instructionsByDate = new HashMap<>();
        for (Instructions i : instructionses) {
            instructionsByDate.put(i.getDateString(), i);
        }
        instructionsByDate.put(newInstructions.getDateString(), newInstructions);
        instructionsLoader.saveInstructions(new ArrayList<>(instructionsByDate.values()));
    }

    private boolean giveUp(Context context, DateTime day) {
        if (mNow.getHourOfDay() >= 19) {
            Notifier.createNoDataNotification(context, day);
            mCheckInAlarmSetter.setAlarms(context);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Given a URL, sets up a connection and gets the HTTP response body from the server.
     * If the network request is successful, it returns the response body in String form. Otherwise,
     * it will throw an IOException.
     */
    private String downloadUrl(URL url) throws IOException {
        InputStream stream = null;
        HttpsURLConnection connection = null;
        String result = null;
        try {
            connection = (HttpsURLConnection) url.openConnection();
            // Timeout for reading InputStream arbitrarily set to 3000ms.
            connection.setReadTimeout(3000);
            // Timeout for connection.connect() arbitrarily set to 3000ms.
            connection.setConnectTimeout(3000);
            // For this use case, set HTTP method to GET.
            connection.setRequestMethod("GET");
            // Open communications link (network traffic occurs here).
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            // Retrieve the response body as an InputStream.
            stream = connection.getInputStream();
            if (stream != null) {
                // Converts Stream to String with max length of 50kB.
                result = readStream(stream, 50000);
            }
        } finally {
            // Close Stream and disconnect HTTPS connection.
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }

    /**
     * Converts the contents of an InputStream to a String.
     */
    private String readStream(InputStream stream, int maxReadSize)
            throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] rawBuffer = new char[maxReadSize];
        int readSize;
        StringBuilder builder = new StringBuilder();
        while (((readSize = reader.read(rawBuffer)) != -1) && maxReadSize > 0) {
            if (readSize > maxReadSize) {
                readSize = maxReadSize;
            }
            builder.append(rawBuffer, 0, readSize);
            maxReadSize -= readSize;
        }
        return builder.toString();
    }
}
