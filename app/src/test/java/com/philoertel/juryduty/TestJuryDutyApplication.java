package com.philoertel.juryduty;

import org.joda.time.DateTime;

/**
 * Created by poertel on 8/16/16.
 */
public class TestJuryDutyApplication extends JuryDutyApplication {
    static CheckInAlarmSetter checkInAlarmSetter;
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
                activity.mInstructionsLoader = instructionsLoader;
                activity.mNow = now;
            }

            @Override
            public void inject(AddDutyActivity activity) {
                activity.checkInAlarmSetter = checkInAlarmSetter;
            }

            @Override
            public void inject(BootCompletedReceiver receiver) {
                receiver.checkInAlarmSetter = checkInAlarmSetter;
            }

            @Override
            public void inject(CheckInAlarmReceiver receiver) {
                receiver.mCheckInAlarmSetter = checkInAlarmSetter;
                receiver.mNow = now;
            }
        };
    }
}
