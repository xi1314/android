package com.remair.heixiu.activity;

import android.content.Intent;
import android.widget.FrameLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.LoginHelper;
import com.remair.heixiu.LoginView;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.base.HXBaseActivity;
import com.remair.heixiu.bean.UserInfo;
import com.tencent.TIMLogLevel;
import com.tencent.TIMManager;
import studio.archangel.toolkitv2.util.text.Notifier;

/**
 * Created by Michael
 * splash page
 */
public class SplashActivity extends HXBaseActivity implements LoginView {

    @BindView(R.id.splash) FrameLayout mSplash;
    private long exitTime = 0;
    private LoginHelper loginHelper;


    @Override
    protected void initUI() {
        setContentView(R.layout.act_splash);
        ButterKnife.bind(this);
    }


    @Override
    protected void initData() {
        TIMManager.getInstance().setLogLevel(TIMLogLevel.OFF);
        TIMManager.getInstance().disableStorage();
        //一定执行在主线程
        TIMManager.getInstance().init(getApplicationContext());
        UserInfo userInfo = HXApp.getInstance().getUserInfo();
        loginHelper = new LoginHelper(getApplicationContext(), this);

        if (userInfo != null) {
            loginHelper.imLogin(userInfo.user_id + "", userInfo.tlsSig);
        } else {
            mSplash.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(mActivity, LoginActivity.class);
                    i.putExtras(getIntent());
                    startActivity(i);
                    finish();
                }
            }, 1000);
        }
    }


    @Override
    protected void onDestroy() {
        if (loginHelper != null) {
            loginHelper.onDestory();
        }
        super.onDestroy();
        loginHelper.onDestory();
    }


    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Notifier.showShortMsg(this, "再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            HXApp.getInstance().AppExit();
            super.onBackPressed();
        }
    }


    @Override
    public void loginSucc() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finish();
    }


    @Override
    public void loginFail() {
        Notifier.showShortMsg(getApplicationContext(), getString(R.string.login_toast));
        HXApp.getInstance().setUserInfo(null);
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);

        finish();
    }
}