package com.remair.heixiu.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.remair.heixiu.R;

/**
 * Created by JXHIUUI on 2016/6/29.
 */
public class RedcarAnimatorView extends FrameLayout {
    private AnimatorSet animatorSet;
    private AnimationDrawable drawableBehind;
    private AnimationDrawable drawableAfter;
    private RelativeLayout carParent;
    private ScaleAnimation scaleAnimation;
    private TranslateAnimation translateAnimation1;
    private TranslateAnimation translateAnimation2;
    private ObjectAnimator scaleX;
    private ObjectAnimator scaleY;

    public RedcarAnimatorView(Context context, int width, int height) {
        super(context);
        initview(context, width, height);
    }

    public RedcarAnimatorView(Context context, AttributeSet attrs, int width, int height) {
        super(context, attrs);
        initview(context, width, height);
    }

    public RedcarAnimatorView(Context context, AttributeSet attrs, int defStyleAttr, int width, int height) {
        super(context, attrs, defStyleAttr);
        initview(context, width, height);
    }

    private void initview(Context context, final int width, final int height) {
        View view = LayoutInflater.from(context).inflate(R.layout.redcar_anim, this);
        carParent = (RelativeLayout) view.findViewById(R.id.rl_redcar);
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
            ObjectAnimator lightAnimatorY1 = ObjectAnimator.ofFloat(carParent, "y", 0, 0.3f * height);
            lightAnimatorY1.setDuration(4000);
            scaleX = ObjectAnimator.ofFloat(carParent, "scaleX", 1f, 1.4f).setDuration(4000);
            scaleY = ObjectAnimator.ofFloat(carParent, "scaleY", 1f, 1.4f).setDuration(4000);
            ObjectAnimator alpha1 = ObjectAnimator.ofFloat(light, "alpha", 0);
            ObjectAnimator alpha = ObjectAnimator.ofFloat(light, "alpha", 0, 1, 0, 1, 0);
            alpha.setDuration(2000);

            ObjectAnimator lightAnimatorX2 = ObjectAnimator.ofFloat(carParent, "x", 0.15f * width, -1.5f * carParent.getMeasuredWidth());
            lightAnimatorX2.setDuration(4000);
            ObjectAnimator lightAnimatorY2 = ObjectAnimator.ofFloat(carParent, "y", 0.3f * height, height);
            lightAnimatorY2.setDuration(4000);

            behindWheel.setImageResource(R.drawable.redcar_behindanimation);
            drawableBehind = (AnimationDrawable) behindWheel.getDrawable();

            afterWheel.setImageResource(R.drawable.redcar_afteranimation);
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
//        animatorSet.start();
//        translateAnimation1.start();
//        translateAnimation2.start();
//        drawableAfter.start();
//        drawableBehind.start();

    }

    public void addAnimatorLister(AnimationListener listener) {
        if (animatorSet != null && drawableBehind != null && drawableAfter != null) {
            animatorSet.addListener(listener);
        }
//        if (animatorSet != null && drawableBehind != null && drawableAfter != null) {
//            animatorSet.addListener(listener);
//        }
    }
}
