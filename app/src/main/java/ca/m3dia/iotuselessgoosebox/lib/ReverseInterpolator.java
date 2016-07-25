package ca.m3dia.iotuselessgoosebox.lib;

import android.view.animation.Interpolator;

/**
 * Created by Datatellit1 on 7/25/2016.
 */
public class ReverseInterpolator implements Interpolator {
    @Override
    public float getInterpolation(float paramFloat) {
        return Math.abs(paramFloat -1f);
    }
}
