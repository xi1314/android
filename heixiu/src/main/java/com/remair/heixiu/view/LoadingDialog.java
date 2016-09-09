package com.remair.heixiu.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.remair.heixiu.R;

/**
 * Created by wsk on 16/7/13.
 */
public class LoadingDialog extends Dialog {

    private Context context;
    private AnimationDrawable animateDrawableleft, animateDrawableright;


    public LoadingDialog(Context context) {
        super(context);
        this.context=context;
        init(context);
    }


    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context=context;
        init(context);
    }


    public LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context=context;
        init(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.loading_dialog, null);
        setContentView(view);
        RelativeLayout ll_pull = (RelativeLayout) view
                .findViewById(R.id.ll_pull);
        ImageView ptr_classic_header_below = (ImageView) view
                .findViewById(R.id.ptr_classic_header_below);
        ImageView ptr_classic_header_left = (ImageView) view
                .findViewById(R.id.ptr_classic_header_left);
        ImageView ptr_classic_header_right = (ImageView) view
                .findViewById(R.id.ptr_classic_header_right);
        ImageView ptr_classic_header_rotate_view = (ImageView) view
                .findViewById(R.id.ptr_classic_header_rotate_view);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        //        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        int w = View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        lp.height = view.getMeasuredHeight();
        lp.width = view.getMeasuredWidth();
        dialogWindow.setAttributes(lp);

        animateDrawableleft = (AnimationDrawable) getContext().getResources()
                                                              .getDrawable(R.drawable.anim_bat_left);
        animateDrawableright = (AnimationDrawable) getContext().getResources()
                                                               .getDrawable(R.drawable.anim_bat_right);
        ptr_classic_header_left.setImageDrawable(animateDrawableleft);
        ptr_classic_header_right.setImageDrawable(animateDrawableright);
    }


    @Override
    public void show() {
        if (context == null) {
            return;
        }
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }
        super.show();
        animateDrawableleft.start();
        animateDrawableright.start();
    }


    public void todismiss() {
        this.dismiss();
        animateDrawableleft.stop();
        animateDrawableright.stop();
    }
}
