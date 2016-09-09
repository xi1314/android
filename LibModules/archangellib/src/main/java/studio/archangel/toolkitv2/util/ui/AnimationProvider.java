package studio.archangel.toolkitv2.util.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import studio.archangel.toolkitv2.R;

/**
 * Created by Administrator on 2015/9/26.
 */
public class AnimationProvider {

    public static Animation getHintAnimation(Context context, final View v, final Object value, final OnValueSetListener l) {
        if (l == null) {
            return null;
        }
        final Animation subset = AnimationUtils.loadAnimation(context, R.anim.anim_zoom_text);
        subset.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        AlphaAnimation aa = new AlphaAnimation(1, 0);
        aa.setDuration(100);
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                l.onValueSet(v, value);
                v.setVisibility(View.INVISIBLE);
                v.startAnimation(subset);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return aa;
    }

    public interface OnValueSetListener {
        void onValueSet(View v, Object value);
    }


    public static double getAnimatedValueLinear(double x1, double x2, double x, double y1, double y2) {
        return ((y2 - y1) * x * 1.0 / (x2 - x1) + y1 - (y2 - y1) * x1 * 1.0 / (x2 - x1));
    }

    public static int getAnimatedValueLinear(int x1, int x2, int x, int y1, int y2) {
        return (int) ((y2 - y1) * x * 1.0 / (x2 - x1) + y1 - (y2 - y1) * x1 * 1.0 / (x2 - x1));
    }

    public static int getAnimatedColorLinear(int x1, int x2, int x, int color0, int color1) {
        int a0 = Color.alpha(color0);
        int r0 = Color.red(color0);
        int g0 = Color.green(color0);
        int b0 = Color.blue(color0);
        int a1 = Color.alpha(color1);
        int r1 = Color.red(color1);
        int g1 = Color.green(color1);
        int b1 = Color.blue(color1);
        int a = getAnimatedValueLinear(x1, x2, x, a0, a1);
        int r = getAnimatedValueLinear(x1, x2, x, r0, r1);
        int g = getAnimatedValueLinear(x1, x2, x, g0, g1);
        int b = getAnimatedValueLinear(x1, x2, x, b0, b1);
        return Color.argb(a, r, g, b);
    }
}
