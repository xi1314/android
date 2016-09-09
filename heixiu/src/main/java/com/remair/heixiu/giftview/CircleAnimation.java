package com.remair.heixiu.giftview;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

/**
 * Created by wsk on 16/4/13.
 */
public class CircleAnimation {
    public static void startCWAnimation(View animateView, float fromDegree, float toDegree) {
        RotateAnimation cwAnimation = new RotateAnimation(fromDegree, toDegree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        cwAnimation.setFillAfter(true);
        animateView.startAnimation(cwAnimation);
    }
    public static void startRotateAnimation(View animateView) {
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(1000);
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setInterpolator(new LinearInterpolator());
        animateView.startAnimation(rotate);
    }
    public static void stopRotateAnmiation(View animatedView) {
        animatedView.getAnimation().cancel();
    }
    public static void startScaleAnimation(View animateView) {
        Animation scaleAnimation = new ScaleAnimation(0f, 1f, 0f, 1f,Animation.RELATIVE_TO_SELF, 0.1f, Animation.RELATIVE_TO_SELF,
                0.1f);
//        Animation scaleAnimation = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1f,
//                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
//                0.5f);
        //设置动画时间
        scaleAnimation.setDuration(1000);
        scaleAnimation.setRepeatCount(0);
        scaleAnimation.setInterpolator(new DecelerateInterpolator());
        animateView.startAnimation(scaleAnimation);
    }

    public static void startScaleAnimationtwo(View animateView) {
        Animation scaleAnimation = new ScaleAnimation(1.4f, 1f, 1.4f, 1f,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
//        Animation scaleAnimation = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1f,
//                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
//                0.5f);
        //设置动画时间
        scaleAnimation.setDuration(1000);
        scaleAnimation.setRepeatCount(0);
        scaleAnimation.setInterpolator(new DecelerateInterpolator());
        animateView.startAnimation(scaleAnimation);
    }


}
