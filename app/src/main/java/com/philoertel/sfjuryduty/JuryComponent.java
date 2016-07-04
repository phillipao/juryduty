package com.philoertel.sfjuryduty;

import dagger.Component;

/**
 * Component for providing dependencies.
 */
@Component(modules = {JuryModule.class})
public interface JuryComponent {
    DutiesLoader provideDutiesLoader();
    InstructionsLoader provideInstructionsLoader();
    void inject(DutyActivity activity);
}
