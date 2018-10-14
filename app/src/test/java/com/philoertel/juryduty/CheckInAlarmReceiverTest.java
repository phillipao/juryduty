package com.philoertel.juryduty;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowNotificationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.google.common.truth.Truth.assertThat;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,
        application = TestJuryDutyApplication.class)
public class CheckInAlarmReceiverTest {
    @Rule public MockitoRule rule = MockitoJUnit.rule();
    private CountDownLatch latch = new CountDownLatch(1);
    @Mock private CheckInAlarmSetter mockSetter;
    @Mock private Downloader mockDownloader;

    private InstructionsLoader instructionsLoader;

    private ShadowNotificationManager shadowNotificationManager;

    @Before
    public void setup() {
        instructionsLoader = new InstructionsLoader(RuntimeEnvironment.application.getFilesDir());
        DutiesLoader dutiesLoader = new DutiesLoader(RuntimeEnvironment.application.getFilesDir());

        TestJuryDutyApplication.now = new DateTime(2016, 1, 3, 17, 0);
        TestJuryDutyApplication.countDownLatch = latch;
        TestJuryDutyApplication.checkInAlarmSetter = mockSetter;
        TestJuryDutyApplication.downloader = mockDownloader;
        TestJuryDutyApplication.dutiesLoader = dutiesLoader;
        TestJuryDutyApplication.instructionsLoader = instructionsLoader;

        NotificationManager notificationManager =
                (NotificationManager)RuntimeEnvironment.application.getSystemService(Context.NOTIFICATION_SERVICE);
        shadowNotificationManager = Shadows.shadowOf(notificationManager);
    }

    @Test
    public void testBroadcastReceiverRegistered() {
        List<ShadowApplication.Wrapper> registeredReceivers =
                ShadowApplication.getInstance().getRegisteredReceivers();

        assertThat(registeredReceivers).isNotEmpty();

        boolean receiverFound = false;
        for (ShadowApplication.Wrapper wrapper : registeredReceivers) {
            if (CheckInAlarmReceiver.class.getSimpleName().equals(
                    wrapper.broadcastReceiver.getClass().getSimpleName())) {
                receiverFound = true;
                break;
            }
        }
        assertTrue(receiverFound);
    }

    @Test
    public void parseError() throws Exception {
        when(mockDownloader.downloadUrl()).thenReturn("foo");

        DateTime day = new DateTime(2016, 1, 1, 17, 0);
        Intent intent = new Intent(RuntimeEnvironment.application, CheckInAlarmReceiver.class);
        intent.putExtra(CheckInAlarmReceiver.EXTRA_DATE, day.toString());
        receive(intent);

        assertThat(shadowNotificationManager.getAllNotifications()).hasSize(1);
        assertThat(instructionsLoader.readInstructions()).isEmpty();
    }

    @Test
    public void testReceive() throws Exception {
        when(mockDownloader.downloadUrl()).thenReturn(
                "GROUPS REPORTING: There are no groups scheduled to report on Tuesday, August 28, "
                        + "2018. GROUPS ...");

        DateTime day = new DateTime(2016, 1, 1, 17, 0);
        Intent intent = new Intent(RuntimeEnvironment.application, CheckInAlarmReceiver.class);
        intent.putExtra(CheckInAlarmReceiver.EXTRA_DATE, day.toString());
        receive(intent);

        DateTime instructionsDate = new DateTime(2018, 8, 28, 0, 0);
        assertThat(instructionsLoader.readInstructions())
                .containsExactly(new Instructions(instructionsDate, new ArrayList<String>()));
        assertThat(shadowNotificationManager.getAllNotifications()).isEmpty();
    }

    /**
     * Not sure if Android framework guarantees that onReceive will only be called once per object.
     * Since the object only has one CountDownLatch, this test just makes sure it's safe to call
     * countDown again (it is, because that's how countDown works. But I wanted to test it.)
     */
    @Test
    public void receiveTwice() throws Exception {
        when(mockDownloader.downloadUrl()).thenReturn("foo"); // error is fine. Just need to execute.

        DateTime day = new DateTime(2016, 1, 1, 17, 0);
        Intent intent = new Intent(RuntimeEnvironment.application, CheckInAlarmReceiver.class);
        intent.putExtra(CheckInAlarmReceiver.EXTRA_DATE, day.toString());
        receive(intent);
        receive(intent);
    }

    private void receive(Intent intent) throws Exception {
        CheckInAlarmReceiver receiver = new CheckInAlarmReceiver();
        receiver.onReceive(RuntimeEnvironment.application, intent);
        assertTrue(latch.await(15, TimeUnit.SECONDS));
    }
}
