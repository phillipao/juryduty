package com.philoertel.juryduty;

import org.joda.time.DateTime;

import dagger.Component;
import javax.inject.Singleton;

import static com.philoertel.juryduty.Annotations.Now;

/**
 * Component for providing dependencies.
 */
@Singleton
@Component(modules = {JuryModule.class})
public interface JuryComponent {
    @Singleton DutiesLoader provideDutiesLoader();
    @Singleton InstructionsLoader provideInstructionsLoader();

    @Now DateTime provideNowDateTime();

    void inject(AddDutyActivity activity);

    void inject(BootCompletedReceiver receiver);

    void inject(CheckInAlarmReceiver receiver);
    void inject(DebugActivity activity);
    void inject(DutiesActivity activity);
    void inject(DutyActivity activity);
}
