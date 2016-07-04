package com.philoertel.sfjuryduty;

import android.app.Application;

import dagger.Module;
import dagger.Provides;

/** Module for providing dependencies. */
@Module
public class JuryModule {

    private Application application;

    public JuryModule(Application application) {
        this.application = application;
    }

    @Provides
    DutiesLoader provideDutiesLoader() {
        return new DutiesLoader(application.getFilesDir());
    }

    @Provides
    InstructionsLoader provideInstructionsLoader() {
        return new InstructionsLoader(application.getFilesDir());
    }
}
