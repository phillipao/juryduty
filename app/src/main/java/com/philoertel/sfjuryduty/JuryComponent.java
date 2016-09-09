package com.philoertel.sfjuryduty;

import org.joda.time.DateTime;

import dagger.Component;

import static com.philoertel.sfjuryduty.Annotations.Now;

/**
 * Component for providing dependencies.
 */
@Component(modules = {JuryModule.class})
public interface JuryComponent {
    DutiesLoader provideDutiesLoader();
    InstructionsLoader provideInstructionsLoader();

    @Now DateTime provideNowDateTime();

    void inject(DutyActivity activity);
}
