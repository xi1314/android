package com.remair.heixiu.view;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.remair.heixiu.R;
import com.remair.heixiu.utils.RxViewUtil;
import rx.functions.Action1;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：JXH
 * 创建时间：2016/8/26 19:28
 * 修改人：JXH
 * 修改时间：2016/8/26 19:28
 * 修改备注：
 */
public class ClosureDialog extends DialogFragment {
    @BindView(R.id.rb_baoli) Button mRbBaoli;
    @BindView(R.id.rb_gg) Button mRbGg;
    @BindView(R.id.rb_zz) Button mRbZz;
    @BindView(R.id.rb_qq) Button mRbQq;
    @BindView(R.id.rb_three) Button mRbThree;
    @BindView(R.id.rb_seven) Button mRbSeven;
    @BindView(R.id.rb_ever) Button mRbEver;
    @BindView(R.id.tv_sure) TextView mTvSure;

    protected ToastDialog mDialogFragment;
    protected Bundle mArguments;
    protected int reason = -1, day;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArguments = getArguments();
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.dialog);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.act_closure, null);
        ButterKnife.bind(this, view);
        init();
        return view;
    }


    private void init() {
        RxViewUtil.viewBindClick(mTvSure, new Action1<Void>() {

            @Override
            public void call(Void aVoid) {
                if (reason == -1 || day == 0) {
                    return;
                }
                if (mDialogFragment == null) {
                    mDialogFragment = new ToastDialog();
                }
                mArguments.putInt("reason", reason);
                mArguments.putInt("day", day);
                mDialogFragment.setArguments(mArguments);
                mDialogFragment.show(getFragmentManager(), "ToastDialog");
            }
        });
    }


    @OnClick({ R.id.rb_baoli, R.id.rb_gg, R.id.rb_zz, R.id.rb_qq, R.id.rb_three,
                     R.id.rb_seven, R.id.rb_ever, R.id.tv_sure })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rb_baoli:
                setBackColor(mRbBaoli, mRbGg, mRbZz, mRbQq);
                reason = 0;
                break;
            case R.id.rb_gg:
                setBackColor(mRbGg, mRbBaoli, mRbZz, mRbQq);
                reason = 1;
                break;
            case R.id.rb_zz:
                setBackColor(mRbZz, mRbBaoli, mRbGg, mRbQq);
                reason = 2;
                break;
            case R.id.rb_qq:
                setBackColor(mRbQq, mRbBaoli, mRbGg, mRbZz);
                reason = 3;
                break;
            case R.id.rb_three:
                setBackColor(mRbThree, mRbSeven, mRbEver);
                day = 3;
                break;
            case R.id.rb_seven:
                setBackColor(mRbSeven, mRbThree, mRbEver);
                day = 7;
                break;
            case R.id.rb_ever:
                setBackColor(mRbEver, mRbThree, mRbSeven);
                day = 9999;
                break;
            case R.id.tv_sure:
                break;
        }
    }


    protected void setBackColor(View view1, View view2, View view3, View view4) {
        view1.setBackgroundResource(R.drawable.shape_button_red);
        view2.setBackgroundResource(R.drawable.shape_button_gray);
        view3.setBackgroundResource(R.drawable.shape_button_gray);
        view4.setBackgroundResource(R.drawable.shape_button_gray);
    }


    protected void setBackColor(View view1, View view2, View view3) {
        view1.setBackgroundResource(R.drawable.shape_button_red);
        view2.setBackgroundResource(R.drawable.shape_button_gray);
        view3.setBackgroundResource(R.drawable.shape_button_gray);
    }
}
