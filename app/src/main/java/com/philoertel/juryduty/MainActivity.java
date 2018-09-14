package com.philoertel.juryduty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, DutiesActivity.class);
        startActivity(intent);

        // Destroy the activity so it doesn't end up in the back stack.
        finish();
    }
}
