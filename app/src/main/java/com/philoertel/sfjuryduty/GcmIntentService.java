package com.philoertel.sfjuryduty;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmListenerService;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service to receive intents from GCM, and show a toast for them.
 */
public class GcmIntentService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {
        Logger.getLogger("GCM_RECEIVED").log(Level.INFO, data.toString());
        showToast(data.getString("message"));
    }

    protected void showToast(final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
