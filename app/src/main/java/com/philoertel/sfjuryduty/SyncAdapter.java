package com.philoertel.sfjuryduty;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedScanList;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */
class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = "SyncAdapter";
    // Global variables
    // Define a variable to contain a content resolver instance
    private final ContentResolver mContentResolver;

    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();

    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        Log.d(TAG, "Syncing now");

        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getContext(),
                "us-east-1:0d0a2fc4-d596-45cc-8475-63abc794723b", // Identity Pool ID
                Regions.US_EAST_1);

        AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
        DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        PaginatedScanList<Instructions> result = mapper.scan(Instructions.class, scanExpression);
        Log.d(TAG, "Found " + result.size() + " sets of instructions");
        InstructionsLoader loader = new InstructionsLoader(getContext().getFilesDir());
        List<Instructions> previousInstructions = loader.readInstructions();

        Set<Instructions> newSet = new HashSet<>(result);
        newSet.removeAll(previousInstructions);
        if (!newSet.isEmpty()) {
            loader.saveInstructions(result);
        }

        DutiesLoader dutiesLoader = new DutiesLoader(getContext().getFilesDir());
        List<Duty> duties = dutiesLoader.readDuties();

        for (int i = 0; i < duties.size(); ++i) {
            Duty duty = duties.get(i);
            for (Instructions instructions : newSet) {
                if (duty.overlapsWith(instructions)) {
                    if (duty.calledBy(instructions)) {
                        Notifier.createPositiveNotification(getContext(), i, duty);
                    } else {
                        Notifier.createNegativeNotification(getContext(), i, duty);
                    }
                }
            }
        }

        Log.d(TAG, "Done syncing");
    }
}
