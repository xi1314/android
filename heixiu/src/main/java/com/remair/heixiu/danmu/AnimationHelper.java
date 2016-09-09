package com.remair.heixiu.danmu;

import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * Created by Chuxi on 2016/3/2.
 */
//平移动画
public class AnimationHelper {
    /**
     * 创建平移动画
     */
    public static Animation createTranslateAnim(int fromX, int toX, int width) {

        TranslateAnimation tlAnim = new TranslateAnimation(fromX, toX, 0, 0);
        //自动计算时间
        long duration = (long) (Math.abs(toX - fromX) * 1.0f / width * 8000);
        tlAnim.setDuration(duration);
        tlAnim.setInterpolator(new DecelerateAccelerateInterpolator());
        tlAnim.setFillAfter(true);

        return tlAnim;
    }
}
