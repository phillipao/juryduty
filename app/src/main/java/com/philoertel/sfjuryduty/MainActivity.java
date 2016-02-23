package com.philoertel.sfjuryduty;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.philoertel.sfjuryduty.accounts.GenericAccountService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public static final String AUTHORITY = "com.philoertel.sfjuryduty.provider";
    public static final String ACCOUNT_TYPE = "philoertel.com";
    public static final String ACCOUNT = "default_account";

    Account mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAccount = CreateSyncAccount(this);
        // Turn on automatic syncing for the default account and authority
        ContentResolver.setSyncAutomatically(mAccount, AUTHORITY, true);
        TriggerRefresh();

        setContentView(R.layout.activity_main);

        DutiesLoader dutiesLoader = new DutiesLoader(getFilesDir());
        ArrayList<Duty> duties = dutiesLoader.readDuties();

        Date today = Calendar.getInstance().getTime();
        Duty nextDuty = null;
        Integer nextDutyId = null;
        for (int i = 0; i < duties.size(); ++i) {
            Duty duty = duties.get(i);
            if (duty.getDate().after(today) && (nextDuty == null || duty.getDate().before(nextDuty.getDate()))) {
                nextDutyId = i;
                nextDuty = duty;
            }
        }

        if (nextDuty == null) {
            Intent intent = new Intent(getApplicationContext(), AddDutyActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getApplicationContext(), DutyActivity.class);
            intent.putExtra(DutyActivity.DUTY_ID_EXTRA, nextDutyId);
            startActivity(intent);
        }
    }

    /**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    public static Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(ACCOUNT_SERVICE);
        // Add the account and account type, no password or user data.
        accountManager.addAccountExplicitly(newAccount, null, null);
        return newAccount;
    }

    /**
     * Helper method to trigger an immediate sync ("refresh").
     *
     * <p>This should only be used when we need to preempt the normal sync schedule. Typically, this
     * means the user has pressed the "refresh" button.
     *
     * Note that SYNC_EXTRAS_MANUAL will cause an immediate sync, without any optimization to
     * preserve battery life. If you know new data is available (perhaps via a GCM notification),
     * but the user is not actively waiting for that data, you should omit this flag; this will give
     * the OS additional freedom in scheduling your sync request.
     */
    public static void TriggerRefresh() {
        Bundle b = new Bundle();
        // Disable sync backoff and ignore sync preferences. In other words...perform sync NOW!
        b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        b.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(GenericAccountService.GetAccount(ACCOUNT_TYPE), AUTHORITY, b);
    }
}
