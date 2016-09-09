package studio.archangel.toolkitv2.views;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import studio.archangel.toolkitv2.R;
import studio.archangel.toolkitv2.util.Logger;

public class AngelLoadingDialog extends Dialog {
    AngelProgressCircle circle;
    AngelProgressBarHorizontal bar;
    LinearLayout ll;
    //    RelativeLayout mask;
    TextView tv_content;
    boolean is_indeterminate;
    Activity act;
    FragmentActivity act_v4;
    int max = 0;
    String act_name = null;

    public AngelLoadingDialog(Activity a, int res_color) {
        this(a, res_color, true);
    }

    public AngelLoadingDialog(Activity a, int res_color, boolean e) {
        super(a, e ? R.style.AngelProgressDialogEternal : R.style.AngelProgressDialogEternal);
//        super(a, R.style.AngelProgressDialog);
        act = a;
        act_name = act.getLocalClassName();
        init(res_color, e);
    }

    public AngelLoadingDialog(FragmentActivity a, int res_color) {
        this(a, res_color, true);
    }

    public AngelLoadingDialog(FragmentActivity a, int res_color, boolean e) {
        super(a, e ? R.style.AngelProgressDialogEternal : R.style.AngelProgressDialogEternal);
//        super(a,R.style.AngelProgressDialog);
        act_v4 = a;
        act_name = act_v4.getLocalClassName();
        init(res_color, e);

    }

    void init(int res_color, boolean e) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        try {
            Logger.outUpper("Activity:" + act_name, 3);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        is_indeterminate = e;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
//        if (!is_indeterminate) {
        lp.dimAmount = 0.25f;
//        }
        getWindow().setAttributes(lp);
        setTitle(null);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setOnCancelListener(null);
        setContentView(R.layout.view_dialog_loading);
        bar = (AngelProgressBarHorizontal) findViewById(R.id.dialog_progress_pb);
//        mask = (RelativeLayout) findViewById(R.id.dialog_progress_mask);
        ll = (LinearLayout) findViewById(R.id.dialog_progress_pb_layout);
//        mask.setVisibility(e ? View.GONE : View.VISIBLE);
        ll.setVisibility(e ? View.GONE : View.VISIBLE);
        circle = (AngelProgressCircle) findViewById(R.id.dialog_progress_pb_eternal);
        circle.setVisibility(e ? View.VISIBLE : View.GONE);
        tv_content = (TextView) findViewById(R.id.dialog_progress_content);
        Context context = (act != null) ? act.getBaseContext() : act_v4.getBaseContext();
        bar.setBackgroundColor(context.getResources().getColor(res_color));
        circle.setBarColor(context.getResources().getColor(res_color));
        if ((act != null && act.isFinishing()) || (act_v4 != null && act_v4.isFinishing())) {
            return;
        }
        show();
    }

    public void setMainColor(int color) {
        bar.setBackgroundColor(color);
        circle.setBarColor(color);
    }

    public TextView getContentTextView() {
        return tv_content;
    }

    public void setContentTextView(String content) {
        tv_content.setText(content);
    }


    public void setMax(int m) {
        if (is_indeterminate) {
            Logger.out("类型错误，无法设置最大值");
            return;
        }
        max = m;
        bar.setMax(m);
    }

    public int getMax() {
        return max;
    }

    public void setProgress(int p) {
        if (is_indeterminate) {
            Logger.out("类型错误，无法设置进度");
            return;
        }
        bar.setProgress(p);
    }

    public int getProgress() {
        return bar.getProgress();
    }

    public void forceDismiss() {
        if (AngelLoadingDialog.this != null && AngelLoadingDialog.this.isShowing()) {
            super.dismiss();
        }
    }

    @Override
    public void dismiss() {

        if (is_indeterminate) {//circle
            ObjectAnimator anim_width = ObjectAnimator.ofInt(circle, "BarWidthImmediately", circle.getBarWidth(), 0);
            ObjectAnimator anim_alpha = ObjectAnimator.ofFloat(circle, "Alpha", circle.getAlpha(), 0);
            AnimatorSet set = new AnimatorSet();
            set.setDuration(350);
            set.playTogether(anim_width, anim_alpha);
            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if ((act != null || act_v4 != null) && AngelLoadingDialog.this != null && AngelLoadingDialog.this.isShowing()) {
                        try {
                            AngelLoadingDialog.super.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            set.start();
        } else {//progressbar
            int height = ll.getHeight();

            height = (int) ((height * 0.4f));
            AnimatorSet set = new AnimatorSet();
            ObjectAnimator anim1 = ObjectAnimator.ofFloat(ll, "translationY", 0, -height);
            ObjectAnimator anim2 = ObjectAnimator.ofFloat(ll, "alpha", 1.0f, 0.0f);
//            ObjectAnimator anim3 = ObjectAnimator.ofFloat(mask, "alpha", 1.0f, 0.0f);
            set.playTogether(anim1, anim2);
            set.setInterpolator(new AccelerateInterpolator());
            set.setDuration(400);
            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if ((act != null || act_v4 != null) && AngelLoadingDialog.this != null && AngelLoadingDialog.this.isShowing()) {
                        try {
                            AngelLoadingDialog.super.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            set.start();
        }

    }

}
