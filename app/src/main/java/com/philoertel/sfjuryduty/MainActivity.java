package com.philoertel.sfjuryduty;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.philoertel.sfjuryduty.accounts.GenericAccountService;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private static final String AUTHORITY = "com.philoertel.sfjuryduty.provider";
    private static final String ACCOUNT_TYPE = "philoertel.com";
    private static final String ACCOUNT = "default_account";

    private Account mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAccount = CreateSyncAccount(this);
        // Turn on automatic syncing for the default account and authority
        ContentResolver.setSyncAutomatically(mAccount, AUTHORITY, true);
        TriggerRefresh();

        Intent intent = new Intent(this, DutiesActivity.class);
        startActivity(intent);

        // Destroy the activity so it doesn't end up in the back stack.
        finish();
    }

    /**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    private static Account CreateSyncAccount(Context context) {
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
    private static void TriggerRefresh() {
        Bundle b = new Bundle();
        // Disable sync backoff and ignore sync preferences. In other words...perform sync NOW!
        b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        b.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(GenericAccountService.GetAccount(ACCOUNT_TYPE), AUTHORITY, b);
    }
}
