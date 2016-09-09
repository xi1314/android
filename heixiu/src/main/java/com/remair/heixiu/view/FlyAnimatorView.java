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
public class FlyAnimatorView extends FrameLayout {

    private AnimatorSet animatorSet;
    private AnimationDrawable wingLeft;
    private AnimationDrawable wingRight;

    public FlyAnimatorView(Context context, int width, int height) {
        super(context);
        initview(context, width, height);
    }

    public FlyAnimatorView(Context context, AttributeSet attrs, int width, int height) {
        super(context, attrs);
        initview(context, width, height);
    }

    private void initview(Context context, int width, int height) {
        View view_fly = LayoutInflater.from(context).inflate(R.layout.act_fly, this);
        ImageView fly_bg = (ImageView) findViewById(R.id.fly_bg);//背景
//        hs_fly_bg = (HorizontalScrollView) findViewById(R.id.hs_fly_bg);
        RelativeLayout fly_body = (RelativeLayout) findViewById(R.id.fly_body);
        ImageView left_wing = (ImageView) findViewById(R.id.left_wing);
        ImageView right_wing = (ImageView) findViewById(R.id.right_wing);

        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        fly_body.measure(w, h);
        fly_bg.measure(w, h);
//        hs_fly_bg.measure(w, h);
        int measuredWidth_flybody = fly_body.getMeasuredWidth();
        int measuredWidth_flybg = fly_bg.getMeasuredWidth();
//        measuredWidth_hs_bg = hs_fly_bg.getMeasuredWidth();


        ObjectAnimator x1 = ObjectAnimator.ofFloat(fly_body, "x", width, -0.8f * measuredWidth_flybody);
        ObjectAnimator x2 = ObjectAnimator.ofFloat(fly_bg, "scaleY", 1.4f);
        x2.setDuration(20);
        x2.start();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(fly_body, "scaleX", 0.6f, 1f, 1.3f, 1f, 0.5f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(fly_body, "scaleY", 0.6f, 1f, 1.3f, 1f, 0.5f);
        animatorSet = new AnimatorSet();
        animatorSet.play(x1).with(scaleX).with(scaleY);
        animatorSet.setDuration(20000);

        right_wing.setImageResource(R.drawable.fly_left);
        left_wing.setImageResource(R.drawable.fly_left);
        wingLeft = (AnimationDrawable) left_wing.getDrawable();
        wingRight = (AnimationDrawable) right_wing.getDrawable();
    }


    public void startAnimator() {
        if (animatorSet != null) {
            animatorSet.start();
            wingLeft.start();
            wingRight.start();
        }
    }

    public void addAnimatorLister(AnimationListener listener) {
        if (animatorSet != null) {
            animatorSet.addListener(listener);
        }
    }
}
