package com.philoertel.sfjuryduty;

import org.joda.time.DateTime;

/**
 * Created by poertel on 8/16/16.
 */
public class TestJuryDutyApplication extends JuryDutyApplication {
    static DutiesLoader dutiesLoader;
    static InstructionsLoader instructionsLoader;
    static DateTime now;

    @Override
    public JuryComponent getComponent() {
        return new JuryComponent() {
            @Override
            public DutiesLoader provideDutiesLoader() {
                return dutiesLoader;
            }

            @Override
            public InstructionsLoader provideInstructionsLoader() {
                return instructionsLoader;
            }

            @Override
            public DateTime provideNowDateTime() {
                return now;
            }

            @Override
            public void inject(DutyActivity activity) {
                activity.mDutiesLoader = dutiesLoader;
                activity.mNow = now;
            }
        };
    }
}
