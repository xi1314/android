package com.remair.heixiu.dialog;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import butterknife.BindView;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.ForbidRulesActivity;
import com.remair.heixiu.bean.ForbidBean;
import com.remair.heixiu.fragment.base.HxBaseDialogFragment;
import com.remair.heixiu.utils.RxViewUtil;
import rx.functions.Action1;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：Jw
 * 创建时间：16/9/1 下午4:45
 * 修改人：
 * 修改时间：16/9/1 下午4:45
 * 修改备注：
 */
public class BannedDialog extends HxBaseDialogFragment {

    private View mRootView;
    private Intent mIntent;
    @BindView(R.id.but_ok) TextView mBut;
    @BindView(R.id.tv_tit) TextView mtvTitle;
    @BindView(R.id.tv_datemm) TextView mtvDate;
    @BindView(R.id.tv_context) TextView mtvMsg;
    @BindView(R.id.tv_mmdate) TextView mtvMdate;
    @BindView(R.id.tv_url) TextView mtvUrl;
    @BindView(R.id.tv_qq1) TextView mtvQ1;
    @BindView(R.id.tv_qq2) TextView mtvQ2;


    @Override
    public View getRootView(LayoutInflater inflater, ViewGroup container) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mRootView = inflater.inflate(R.layout.dialog_banned, container);
        return mRootView;
    }


    @Override
    public void initData() {
        mtvUrl.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        RxViewUtil.viewBindClick(mBut, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                dismiss();
            }
        });
        RxViewUtil.viewBindClick(mtvUrl, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (mIntent == null) {
                    mIntent = new Intent(getActivity(), ForbidRulesActivity.class);
                }
                startActivity(mIntent);
                dismiss();
            }
        });
        Bundle arguments = getArguments();
        ForbidBean banned = arguments.getParcelable("banned");
        setDialogData(banned);
    }


    public void setDialogData(ForbidBean bean) {
        if (bean == null) {
            return;
        }
        mtvDate.setText(bean.forbid_freeTime + "天后解封");
        mtvMsg.setText(bean.forbid_reason);
        mtvMdate.setText(bean.forbid_penalize + "天," + bean.forbid_penalize);
    }
}
