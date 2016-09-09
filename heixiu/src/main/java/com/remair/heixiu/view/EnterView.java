package com.remair.heixiu.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.remair.heixiu.R;
import studio.archangel.toolkitv2.util.Util;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：wsk
 * 创建时间：16/8/11 下午2:59
 * 修改人：wsk
 * 修改时间：16/8/11 下午2:59
 * 修改备注：
 */
public class EnterView extends FrameLayout {
    private int grade;
    private String userName;
    private int width;
    private int scaleX;
    private AnimatorSet animatorSet;


    public EnterView(Context context, int width, int grade, String userName) {
        super(context);
        this.grade = grade;
        this.userName = userName;
        this.width = width;
        scaleX = Util.getPX(257);
        initView();
    }


    public EnterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public EnterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EnterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    private void initView() {
        View view = LayoutInflater.from(getContext())
                                  .inflate(R.layout.enter_room_item, this);
        RelativeLayout rl_view = (RelativeLayout) view
                .findViewById(R.id.rl_view);
        ImageView iv_effect = (ImageView) view.findViewById(R.id.iv_effect);
        TextView tv_enter_user = (TextView) view
                .findViewById(R.id.tv_enter_user);
        if (grade >= 10 && grade < 20) {
            rl_view.setBackgroundResource(R.drawable.enter_effects1);
            iv_effect.setImageResource(R.drawable.light_effect1);
        } else if (grade < 30) {
            rl_view.setBackgroundResource(R.drawable.enter_effects2);
            iv_effect.setImageResource(R.drawable.light_effect2);
        } else if (grade < 40) {
            rl_view.setBackgroundResource(R.drawable.enter_effects3);
            iv_effect.setImageResource(R.drawable.light_effect3);
        } else if (grade < 50) {
            rl_view.setBackgroundResource(R.drawable.enter_effects4);
            iv_effect.setImageResource(R.drawable.light_effect4);
        } else if (grade < 60) {
            rl_view.setBackgroundResource(R.drawable.enter_effects5);
            iv_effect.setImageResource(R.drawable.light_effect5);
        } else if (grade < 70) {
            rl_view.setBackgroundResource(R.drawable.enter_effects6);
            iv_effect.setImageResource(R.drawable.light_effect6);
        } else if (grade < 80) {

            rl_view.setBackgroundResource(R.drawable.enter_effects7);
            iv_effect.setImageResource(R.drawable.light_effect7);
        } else if (grade < 90) {

            rl_view.setBackgroundResource(R.drawable.enter_effects8);
            iv_effect.setImageResource(R.drawable.light_effect7);
        } else if (grade < 100) {

            rl_view.setBackgroundResource(R.drawable.enter_effects9);
            iv_effect.setImageResource(R.drawable.light_effect7);
        } else if (grade < 110) {

            rl_view.setBackgroundResource(R.drawable.enter_effects10);
            iv_effect.setImageResource(R.drawable.light_effect7);
        } else if (grade < 120) {
            rl_view.setBackgroundResource(R.drawable.enter_effects11);
            iv_effect.setImageResource(R.drawable.light_effect7);
        } else if (grade >= 120) {
            rl_view.setBackgroundResource(R.drawable.enter_effects12);
            iv_effect.setImageResource(R.drawable.light_effect7);
        }
        tv_enter_user.setText(userName);
        Paint paint = tv_enter_user.getPaint();
        String str = tv_enter_user.getText().toString();
        int textWidth = (int) paint.measureText(str);
        int textW = Util.getPX(textWidth);
        int textH = Util.getPX(40);
        if (textW > scaleX) {
            rl_view.setLayoutParams(new RelativeLayout.LayoutParams(textW, textH));
        } else {
            rl_view.setLayoutParams(new RelativeLayout.LayoutParams(scaleX, textH));
        }
        ObjectAnimator obectAnimator = ObjectAnimator
                .ofFloat(rl_view, "x", -width, 0);
        obectAnimator.setDuration(500);
        ObjectAnimator obectAnimator2 = null;
        if (textW > scaleX) {
            obectAnimator2 = ObjectAnimator.ofFloat(iv_effect, "x", 0, textW);
        } else {
            obectAnimator2 = ObjectAnimator.ofFloat(iv_effect, "x", 0, scaleX);
        }
        obectAnimator2.setDuration(1000);
        //ObjectAnimator obectAnimator3 = ObjectAnimator.ofFloat(view, "x", 0, width);
        ObjectAnimator obectAnimator3 = ObjectAnimator
                .ofFloat(view, "alpha", 1f, 0.0f);
        obectAnimator3.setDuration(500);
        animatorSet = new AnimatorSet();
        animatorSet.play(obectAnimator2).after(obectAnimator);
        animatorSet.play(obectAnimator3).after(obectAnimator2);
        animatorSet.start();
    }


    public void addAnimatorLister(AnimationListener listener) {
        if (animatorSet != null) {
            animatorSet.addListener(listener);
        }
    }
}
