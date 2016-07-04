package com.philoertel.sfjuryduty;

import android.app.Application;

/** The main Jury Duty app. */
public class JuryDutyApplication extends Application {
    private static JuryComponent juryComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        juryComponent = DaggerJuryComponent.builder().juryModule(new JuryModule(this)).build();
    }

    public static void inject(DutyActivity activity) {
        juryComponent.inject(activity);
    }
}
