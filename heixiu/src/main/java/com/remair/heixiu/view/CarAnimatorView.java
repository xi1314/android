package com.remair.heixiu.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.remair.heixiu.R;

/**
 * Created by JXHIUUI on 2016/6/15.
 */
public class CarAnimatorView extends FrameLayout {

    private AnimatorSet animatorSet;
    private AnimationDrawable drawableBehind;
    private AnimationDrawable drawableAfter;
    private RelativeLayout carParent;
    private ObjectAnimator scaleX;
    private ObjectAnimator scaleY;

    /**
     * 汽车动画
     *
     * @param context
     * @param width   屏幕宽
     * @param height  屏幕高
     */


    public CarAnimatorView(Context context, AttributeSet attrs, int width, int height) {
        super(context, attrs);
        initview(context, width, height);
    }

    public CarAnimatorView(Context context, int width, int height) {
        super(context);
        initview(context, width, height);
    }

    private void initview(Context context, final int width, final int height) {
        View view = LayoutInflater.from(context).inflate(R.layout.act_car, this);
        carParent = (RelativeLayout) view.findViewById(R.id.carparent);
        if (carParent != null) {
            ImageView car = (ImageView) carParent.findViewById(R.id.car);
            ImageView light = (ImageView) carParent.findViewById(R.id.light);
            ImageView behindWheel = (ImageView) carParent.findViewById(R.id.behindWheel);
            ImageView afterWheel = (ImageView) carParent.findViewById(R.id.afterwheel);

            int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            carParent.measure(w, h);
            ObjectAnimator lightAnimatorX1 = ObjectAnimator.ofFloat(carParent, "x", width, 0.15f * width);
            lightAnimatorX1.setDuration(4000);
            ObjectAnimator lightAnimatorY1 = ObjectAnimator.ofFloat(carParent, "y", 0, 0.4f * height);
            lightAnimatorY1.setDuration(4000);
            scaleX = ObjectAnimator.ofFloat(carParent, "scaleX", 1f, 1.2f).setDuration(4000);
            scaleY = ObjectAnimator.ofFloat(carParent, "scaleY", 1f, 1.2f).setDuration(4000);

            ObjectAnimator alpha1 = ObjectAnimator.ofFloat(light, "alpha", 0);
            ObjectAnimator alpha = ObjectAnimator.ofFloat(light, "alpha", 0, 1, 0, 1, 0);
            alpha.setDuration(2000);

            ObjectAnimator lightAnimatorX2 = ObjectAnimator.ofFloat(carParent, "x", 0.15f * width, -1.5f * carParent.getMeasuredWidth());
            lightAnimatorX2.setDuration(4000);
            ObjectAnimator lightAnimatorY2 = ObjectAnimator.ofFloat(carParent, "y", 0.4f * height, height);
            lightAnimatorY2.setDuration(4000);

            behindWheel.setImageResource(R.drawable.behindanimation);
            drawableBehind = (AnimationDrawable) behindWheel.getDrawable();

            afterWheel.setImageResource(R.drawable.afteranimation);
            drawableAfter = (AnimationDrawable) afterWheel.getDrawable();

            animatorSet = new AnimatorSet();
            animatorSet.play(lightAnimatorX1).with(lightAnimatorY1).with(alpha1);

            animatorSet.play(alpha).after(lightAnimatorX1).after(4000);
            animatorSet.play(lightAnimatorX2).with(lightAnimatorY2).after(alpha);
        }

    }


    public void startAnimator() {
        if (animatorSet != null && drawableBehind != null && drawableAfter != null) {
            animatorSet.start();
            drawableAfter.start();
            drawableBehind.start();
            scaleX.start();
            scaleY.start();
        }
    }

    public void addAnimatorLister(AnimationListener listener) {
        if (animatorSet != null && drawableBehind != null && drawableAfter != null) {
            animatorSet.addListener(listener);
        }
    }
}

