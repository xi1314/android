package com.remair.heixiu.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.remair.heixiu.R;

/**
 * Created by JXHIUUI on 2016/7/30.
 */
public class AircraftAnimationView extends FrameLayout {
    public CylinderImageView ivAircraft;
    ImageView ivBoard;
    ImageView ivAir;
    ViewGroup viewAirCArft;
    private ObjectAnimator x1;
    private ObjectAnimator x2;
    private ObjectAnimator scaleX1;
    private ObjectAnimator scaleY1;
    private ObjectAnimator x3;
    private AnimatorSet animatorSet1;
    private AnimatorSet animatorSet2;
    private ObjectAnimator scaleX2;
    private ObjectAnimator scaleY2;
    private ObjectAnimator x4;
    private ObjectAnimator scaleX3;
    private ObjectAnimator scaleY3;
    private AnimatorSet animatorSet3;
    private ObjectAnimator scaleX4;
    private ObjectAnimator scaleY4;
    private AnimatorSet animatorSet4;
    private ObjectAnimator y2;
    private ObjectAnimator y3;
    private ObjectAnimator y4;
    private AnimationListener listener;


    public AircraftAnimationView(Context context) {
        super(context);
        initView(context);
        initAnimator();
    }


    public AircraftAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initAnimator();
    }


    public AircraftAnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initAnimator();
    }


    private void initView(Context context) {
        viewAirCArft = (ViewGroup) LayoutInflater.from(context)
                                                 .inflate(R.layout.animator_air, this, true);
        ivAir = (ImageView) findViewById(R.id.iv_air);
        ivAircraft = (CylinderImageView) findViewById(R.id.cylinder);
        ivBoard = (ImageView) findViewById(R.id.iv_board);
    }


    private void initAnimator() {
        //测量航母控件的宽高
        int w = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int h = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        ivBoard.measure(w, h);
        int measuredWidth = ivBoard.getMeasuredWidth();
        int measuredHeight = ivBoard.getMeasuredHeight();

        //轮船动画
        x1 = ObjectAnimator.ofFloat(ivBoard, "x",
                -0.6f * measuredWidth,
                -0.1f * measuredWidth,
                -0.1f * measuredWidth, 0.7f * measuredWidth);
        scaleX1 = ObjectAnimator
                .ofFloat(ivBoard, "scaleX", 0.5f, 1.1f, 1.1f, 1.1f);
        scaleY1 = ObjectAnimator
                .ofFloat(ivBoard, "scaleY", 0.5f, 1.1f, 1.1f, 1.1f);
        animatorSet1 = new AnimatorSet();
        animatorSet1.play(x1).with(scaleY1).with(scaleX1);
        animatorSet1.setDuration(20 * 1000);

        //飞出动画
        x2 = ObjectAnimator
                .ofFloat(ivAir, "x", -0.3f * measuredWidth, measuredWidth);
        y2 = ObjectAnimator.ofFloat(ivAir, "y", 600, 0);
        scaleX2 = ObjectAnimator.ofFloat(ivAir, "scaleX", 0.3f, 1.2f);
        scaleY2 = ObjectAnimator.ofFloat(ivAir, "scaleY", 0.3f, 1.2f);
        animatorSet2 = new AnimatorSet();
        animatorSet2.setDuration(5 * 1000);

        //飞机动画
        x3 = ObjectAnimator
                .ofFloat(ivAir, "x", measuredWidth, -0.5f * measuredWidth);
        y3 = ObjectAnimator.ofFloat(ivAir, "y", 300, 0);
        scaleX3 = ObjectAnimator.ofFloat(ivAir, "scaleX", 0.3f, 1.2f);
        scaleY3 = ObjectAnimator.ofFloat(ivAir, "scaleY", 0.3f, 1.2f);
        animatorSet3 = new AnimatorSet();
        animatorSet3.setDuration(5 * 1000);

        x4 = ObjectAnimator
                .ofFloat(ivAir, "x", -0.3f * measuredWidth, measuredWidth);
        y4 = ObjectAnimator.ofFloat(ivAir, "y", 600, 0);
        scaleX4 = ObjectAnimator.ofFloat(ivAir, "scaleX", 0.3f, 1.2f);
        scaleY4 = ObjectAnimator.ofFloat(ivAir, "scaleY", 0.3f, 1.2f);
        animatorSet4 = new AnimatorSet();
        animatorSet4.setDuration(5 * 1000);
    }


    public void start() {
        animatorSet1.start();
        if (listener != null) {
            animatorSet1.addListener(listener);
        }

        animatorSet2.setInterpolator(new AccelerateInterpolator());
        animatorSet2.playTogether(x2, y2, scaleX2, scaleY2);
        animatorSet2.start();

        animatorSet2.addListener(new AnimationListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ivAir.setImageResource(R.drawable.air_right);
                animatorSet3.playTogether(x3, y3, scaleX3, scaleY3);
                animatorSet3.start();
                animatorSet3.addListener(new AnimationListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ivAir.setImageResource(R.drawable.air_left);
                        animatorSet4.playTogether(x4, y4, scaleX4, scaleY4);
                        animatorSet4.start();
                    }
                });
            }
        });

        ivAircraft.start();
    }


    public void setonListener(AnimationListener listener) {
        this.listener = listener;
    }
}
