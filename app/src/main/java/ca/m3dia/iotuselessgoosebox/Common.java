package ca.m3dia.iotuselessgoosebox;

import android.content.Context;

import io.particle.android.sdk.cloud.ParticleCloudException;
import io.particle.android.sdk.cloud.ParticleCloudSDK;
import io.particle.android.sdk.cloud.ParticleDevice;
import io.particle.android.sdk.devicesetup.ParticleDeviceSetupLibrary;

/**
 * Created by Umar Bhutta.
 */
public class Common {
    public static final String EMAIL = "email@emaildomain.com";
    public static final String PASSWORD = "password";
    public static final String DEVICE_ID = "31001c000e47343432313031";

    public static ParticleDevice currDevice;

    public static final String[] presetTitles = new String[]{"On Her Feet", "Watchin' You", "Ha, Gotcha", "Hot Headed"};
    public static final String[] presetDescriptions = new String[]{"Alarmed sound with slow open, rash finish", "Fast sequence with reappearance", "Fakes an open, quick subsequent sequence", "Failure to turn off light in anger"};

    public static void authenticate(Context ctx) {
        ParticleDeviceSetupLibrary.init(ctx, MainActivity.class);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ParticleCloudSDK.getCloud().logIn(EMAIL, PASSWORD);
                    currDevice = ParticleCloudSDK.getCloud().getDevice(DEVICE_ID);
                } catch (ParticleCloudException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
