package com.remair.heixiu.danmu;

import android.view.animation.Interpolator;

/**
 * Created by Chuxi on 2016/3/2.
 */
//动画
public class DecelerateAccelerateInterpolator implements Interpolator {

    //input从0～1，返回值也从0～1.返回值的曲线表征速度加减趋势
    @Override
    public float getInterpolation(float input) {
        return (float) (Math.tan((input * 2 - 1) / 4 * Math.PI)) / 2.0f + 0.5f;
    }
}