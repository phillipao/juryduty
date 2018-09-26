package com.philoertel.juryduty;

import android.app.Application;
import android.content.Context;

import org.joda.time.DateTime;

import dagger.Module;
import dagger.Provides;

import static com.philoertel.juryduty.Annotations.Now;

/** Module for providing dependencies. */
@Module
public class JuryModule {

    private Application application;

    public JuryModule(Application application) {
        this.application = application;
    }

    @Provides
    Context provideContext() { return application; }

    @Provides
    DutiesLoader provideDutiesLoader() {
        return new DutiesLoader(application.getFilesDir());
    }

    @Provides
    InstructionsLoader provideInstructionsLoader() {
        return new InstructionsLoader(application.getFilesDir());
    }

    @Provides
    @Now
    DateTime provideNowDateTime() {
        return new DateTime();
    }
}
