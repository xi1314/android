package com.remair.heixiu.activity;

import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.base.HXBaseActivity;
import com.remair.heixiu.net.HXHttpUtil;
import com.remair.heixiu.net.HttpSubscriber;
import com.remair.heixiu.utils.Utils;
import java.util.HashMap;
import rx.Subscription;
import studio.archangel.toolkitv2.util.Util;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：JXH
 * 创建时间：2016/3/7 16:13
 * 修改人：JXH
 * 修改时间：2016/3/7 16:13
 * 修改备注：
 */
public class SignatureActivity extends HXBaseActivity {
    @BindView(R.id.tv_size) TextView tv_size;
    @BindView(R.id.title_txt) TextView mTitleTxt;
    @BindView(R.id.title_left_image) ImageButton mTitleLeftImage;
    @BindView(R.id.title_right_text) TextView mTitleRightText;
    @BindView(R.id.et_change_signature) EditText mEtChangeSignature;
    private String mSignature;


    @Override
    protected void initUI() {
        setContentView(R.layout.act_signature);
        ButterKnife.bind(this);
    }


    @Override
    protected void initData() {
        mTitleTxt.setText(getString(R.string.sign));
        mTitleRightText.setText(getString(R.string.save));
        mEtChangeSignature.setText(getIntent().getStringExtra("signature"));
        mEtChangeSignature.setSelection(mEtChangeSignature.getText().length());
        mEtChangeSignature.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_ENTER;
            }
        });
        Util.setInputLimit(mEtChangeSignature, tv_size, 32);
    }


    private void saveSign() {
        mSignature = mEtChangeSignature.getText().toString().trim();
        if (mSignature.contains(" ")) {
            mSignature = mSignature.replaceAll(" ", "");
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", getIntent().getIntExtra("user_id", 0) + "");
        params.put("signature", mSignature);
        Subscription subscribe = HXHttpUtil.getInstance().editUser(params)
                                           .subscribe(new HttpSubscriber<Object>() {
                                               @Override
                                               public void onNext(Object o) {
                                                   Utils.hideInputBoard(mEtChangeSignature, mActivity);
                                                   HXApp.getInstance()
                                                        .getUserInfo().signature = mSignature;
                                                   setResult(HXApp.result_ok);
                                                   finish();
                                               }
                                           });
        addSubscription(subscribe);
    }


    @OnClick({ R.id.title_left_image, R.id.title_right_text,
                     R.id.et_change_signature })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_image:
                Utils.hideInputBoard(mEtChangeSignature, mActivity);
                finish();
                break;
            case R.id.title_right_text:
                saveSign();
                break;
        }
    }
}
