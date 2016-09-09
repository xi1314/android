package com.remair.heixiu.activity;

import android.text.TextUtils;
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
import studio.archangel.toolkitv2.util.text.Notifier;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：JXH
 * 创建时间：2016/3/1 14:17
 * 修改人：JXH
 * 修改时间：2016/3/1 14:17
 * 修改备注：
 */
public class ChangeNameActivity extends HXBaseActivity {
    @BindView(R.id.title_txt) TextView mTitleTxt;
    @BindView(R.id.title_left_image) ImageButton mTitleLeftImage;
    @BindView(R.id.title_right_text) TextView mTitleRightText;
    @BindView(R.id.et_change_name) EditText mEtChangeName;


    @Override
    protected void initUI() {
        setContentView(R.layout.act_change_name);
        ButterKnife.bind(this);
    }


    @Override
    protected void initData() {
        mTitleTxt.setText(getString(R.string.change_name));
        mTitleRightText.setText(getString(R.string.save));
        mEtChangeName.setText(getIntent().getStringExtra("nickname"));
        mEtChangeName.setSelection(mEtChangeName.getText().length());
        mEtChangeName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_ENTER;
            }
        });
    }


    @OnClick({ R.id.title_left_image, R.id.ig_delete, R.id.title_right_text })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_image:
                Utils.hideInputBoard(mEtChangeName, mActivity);
                finish();
                break;
            case R.id.title_right_text:
                saveListener();
                break;
            case R.id.ig_delete:
                mEtChangeName.setText("");
                break;
        }
    }


    private void saveListener() {

        String name = mEtChangeName.getText().toString().trim();
        if (name.contains(" ")) {
            name = name.replaceAll(" ", "");
        }
        if (TextUtils.isEmpty(name)) {
            Notifier.showShortMsg(getApplication(), getString(R.string.name_toast));
            return;
        }
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("user_id", getIntent().getIntExtra("user_id", 0) + "");
        hashMap.put("nickname", name);
        final String finalName = name;
        Subscription subscribe = HXHttpUtil.getInstance().editUser(hashMap)
                                           .subscribe(new HttpSubscriber<Object>() {
                                               @Override
                                               public void onNext(Object o) {
                                                   Utils.hideInputBoard(mEtChangeName, mActivity);
                                                   HXApp.getInstance()
                                                        .getUserInfo().nickname = finalName;
                                                   setResult(HXApp.result_ok);
                                                   finish();
                                               }
                                           });
        addSubscription(subscribe);
    }
}