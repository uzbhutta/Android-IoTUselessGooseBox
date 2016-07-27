package ca.m3dia.iotuselessgoosebox;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import io.particle.android.sdk.cloud.ParticleCloudException;
import io.particle.android.sdk.cloud.ParticleCloudSDK;
import io.particle.android.sdk.cloud.ParticleDevice;

/**
 * Created by Datatellit1 on 7/21/2016.
 */
public class Common {
    public static ParticleDevice currDevice;
    public static String mEmail, mPassword, mDeviceId;

    public static final String[] presetTitles = new String[]{"On Her Feet", "Watchin' You", "Ha, Gotcha", "Hot Headed"};
    public static final String[] presetDescriptions = new String[]{"Alarmed sound with slow open, rash finish", "Fast sequence with reappearance", "Fakes an open, quick subsequent sequence", "Failure to turn off light in anger"};

    public static void authenticate() throws IOException {
        Scanner in = new Scanner(new FileReader("authenticate.txt"));
        ArrayList<String> info = new ArrayList<>();

        int i = 0;
        while(in.hasNext()) {
            info.set(i, in.nextLine());
            i++;
        }

        mEmail = info.get(0);
        mPassword = info.get(1);
        mDeviceId = info.get(2);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ParticleCloudSDK.getCloud().logIn(Common.mEmail, Common.mPassword);
                    currDevice = ParticleCloudSDK.getCloud().getDevice(Common.mDeviceId);
                } catch (ParticleCloudException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
