package com.remair.heixiu.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.base.HXBaseActivity;
import com.remair.heixiu.utils.RxViewUtil;
import rx.functions.Action1;
import studio.archangel.toolkitv2.views.AngelOptionItem;

/**
 * Created by yly on 2016/8/11.
 */
public class AccountSecurityActivity extends HXBaseActivity {

    @BindView(R.id.text_Account_Security) AngelOptionItem text_Account_Security;
    @BindView(R.id.text_Account_password) AngelOptionItem text_Account_password;
    @BindView(R.id.title_txt) TextView mTitleTxt;
    @BindView(R.id.title_left_image) ImageButton mTitleLeftImage;



    @Override
    protected void initUI() {
        setContentView(R.layout.act_accoutsecurity);
        ButterKnife.bind(this);
    }


    @Override
    protected void initData() {
        mTitleTxt.setText("账号与安全");

        RxViewUtil.viewBindClick(mTitleLeftImage, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });

        final String phone_num = HXApp.getInstance().getUserInfo().phone_num;
        final boolean ispassword = HXApp.getInstance().getUserInfo().password;
        if (null != phone_num && !"".equals(phone_num)) {//已认证手机号
            if (ispassword) {
                text_Account_Security
                        .setContent("已绑定" + phone_num.substring(0, 3) + "****" +
                                phone_num.substring(7, 11));
                //  text_Account_password.setContent("修改密码");
                RxViewUtil
                        .viewBindClick(text_Account_password, new Action1<Void>() {
                            @Override
                            public void call(Void aVoid) {
                                Intent intent = new Intent(AccountSecurityActivity.this, BindPhoneNumActivity.class);
                                intent.putExtra("isbind", "2");
                                startActivity(intent);
                                finish();
                            }
                        });
            } else {
                text_Account_Security
                        .setContent("已绑定" + phone_num.substring(0, 3) + "****" +
                                phone_num.substring(7, 11));
                text_Account_password.setContent("设置密码");
                RxViewUtil
                        .viewBindClick(text_Account_password, new Action1<Void>() {
                            @Override
                            public void call(Void aVoid) {
                                Intent intent = new Intent(AccountSecurityActivity.this, BindPhoneNumActivity.class);
                                //1绑定手机没有设置密码
                                intent.putExtra("isbind", "1");
                                startActivity(intent);
                                finish();
                            }
                        });
            }
        } else {
            text_Account_password.setVisibility(View.GONE);
            RxViewUtil
                    .viewBindClick(text_Account_Security, new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            Intent intent = new Intent(AccountSecurityActivity.this, BindPhoneNumActivity.class);
                            //0没有绑定手机
                            intent.putExtra("isbind", "0");
                            startActivity(intent);
                            finish();
                        }
                    });
        }
    }



}
