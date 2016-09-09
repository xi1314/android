package com.remair.heixiu.view;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.remair.heixiu.R;
import com.remair.heixiu.net.HXHttpUtil;
import com.remair.heixiu.net.HttpSubscriber;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：JXH
 * 创建时间：2016/9/1 14:52
 * 修改人：JXH
 * 修改时间：2016/9/1 14:52
 * 修改备注：
 */
public class ToastDialog extends DialogFragment {
    @BindView(R.id.close_id) TextView mCloseId;
    @BindView(R.id.close_nickname) TextView mCloseNickname;
    @BindView(R.id.close_reason) TextView mCloseReason;
    @BindView(R.id.close_day) TextView mCloseDay;
    @BindView(R.id.close_confirm) TextView mCloseConfirm;
    private int mHandle_id;
    private int mReason;
    private int mDay;
    private Bundle mArguments;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.HXTheme_AppCompat_Dialog);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.toast_dialog, null);
        ButterKnife.bind(this, view);
        mArguments = getArguments();
        mHandle_id = mArguments.getInt("handle_id");
        mCloseId.setText(mHandle_id + "");
        String nickname = mArguments.getString("nickname");
        mCloseNickname.setText(nickname);
        mReason = mArguments.getInt("reason");
        String reasonStr = null;
        switch (mReason) {
            case 0:
                reasonStr = "色情暴力";
                break;
            case 1:
                reasonStr = "广告营销";
                break;
            case 2:
                reasonStr = "政治敏感";
                break;
            case 3:
                reasonStr = "侵犯版权";
                break;
        }
        mCloseReason.setText(reasonStr);
        mDay = mArguments.getInt("day");
        mCloseDay.setText(mDay + "");
        return view;
    }


    @OnClick(R.id.close_confirm)
    public void onClick() {

        HXHttpUtil.getInstance().forbidUser(mHandle_id, mArguments
                .getString("handle_identity"), mArguments
                .getInt("user_id"), mArguments
                .getString("forbid_identity"), mArguments
                .getInt("room_num"), mArguments
                .getInt("type"), mReason, mDay, mArguments.getString("url"))
                  .subscribe(new HttpSubscriber<Object>() {
                      @Override
                      public void onNext(Object o) {
                          dismiss();
                      }
                  });
    }
}
