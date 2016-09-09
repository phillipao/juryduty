package com.philoertel.sfjuryduty;

import android.app.Application;

/** The main Jury Duty app. */
public class JuryDutyApplication extends Application {
    private JuryComponent juryComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        juryComponent = DaggerJuryComponent.builder().juryModule(new JuryModule(this)).build();
    }

    JuryComponent getComponent() { return juryComponent; }
}
