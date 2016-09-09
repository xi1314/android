package studio.archangel.toolkitv2.animations;

import android.view.animation.Interpolator;

/**
 * Created by Michael on 2014/12/28.
 */
public class SineInterpolator implements Interpolator {
    @Override
    public float getInterpolation(float input) {
        return (float) Math.sin(input * Math.PI / 2);
    }
}
