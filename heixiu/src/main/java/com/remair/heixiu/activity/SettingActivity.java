package com.remair.heixiu.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.HXJavaNet;
import com.remair.heixiu.LoginHelper;
import com.remair.heixiu.LogoutView;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.base.HXBaseActivity;
import com.remair.heixiu.utils.FileUtils;
import com.remair.heixiu.utils.RxViewUtil;
import com.remair.heixiu.view.LoadingDialog;
import java.io.File;
import rx.functions.Action1;
import studio.archangel.toolkitv2.util.text.Notifier;
import studio.archangel.toolkitv2.views.AngelOptionItem;

/**
 * Created by Michael
 * setting page
 */

public class SettingActivity extends HXBaseActivity implements LogoutView {
    @BindView(R.id.act_setting_banned) AngelOptionItem aoi_banned;
    @BindView(R.id.act_setting_push) AngelOptionItem aoi_push;
    @BindView(R.id.act_setting_about) AngelOptionItem aoi_about;
    @BindView(R.id.act_setting_feedback) AngelOptionItem aoi_feedback;
    @BindView(R.id.act_setting_logout) AngelOptionItem actSettingLogout;
    @BindView(R.id.act_setting_cacha) AngelOptionItem act_setting_cacha;
    @BindView(R.id.act_Account_Security) AngelOptionItem act_Account_Security;
    @BindView(R.id.title_txt) TextView mTitleTxt;
    @BindView(R.id.title_left_image) ImageButton mTitleLeftImage;
    private LoadingDialog dialog;
    private LoginHelper loginHelper;
    protected HXBaseActivity mActivity;


    @Override
    protected void initUI() {
        setContentView(R.layout.act_setting);
        mActivity = this;
        ButterKnife.bind(this);
        mTitleTxt.setText("设置");
    }


    @Override
    protected void initData() {
        RxViewUtil.viewBindClick(mTitleLeftImage, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });
        loginHelper = new LoginHelper(getApplicationContext(), this);

        RxViewUtil.viewBindClick(aoi_banned, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent it = new Intent(mActivity, BlackListActivity.class);
                startActivity(it);
            }
        });

        RxViewUtil.viewBindClick(aoi_push, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent it = new Intent(mActivity, PushActivity.class);
                startActivity(it);
            }
        });

        RxViewUtil.viewBindClick(aoi_about, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent it = new Intent(mActivity, AboutMeActivity.class);
                startActivity(it);
            }
        });

        RxViewUtil.viewBindClick(aoi_feedback, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent it = new Intent(mActivity, FeedbackActivity.class);
                startActivity(it);
            }
        });

        RxViewUtil.viewBindClick(act_Account_Security, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent it = new Intent(mActivity, AccountSecurityActivity.class);
                startActivity(it);
            }
        });

        //获取消息
        new Thread() {

            private long b;


            @Override
            public void run() {
                String scache = getApplication().getCacheDir()
                                                .getAbsolutePath();
                File f = new File(scache);
                b = FileUtils.getFolderSize(f);
                b = b / 1024 / 1024;
                act_setting_cacha.post(new Runnable() {
                    @Override
                    public void run() {
                        if (act_setting_cacha != null) {
                            act_setting_cacha.setContent(b + "M");
                        }
                    }
                });
            }
        }.start();
        //清理缓存
        act_setting_cacha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new LoadingDialog(mActivity, R.style.dialog);
                dialog.show();

                try {
                    String scache = getApplication().getCacheDir()
                                                    .getAbsolutePath();
                    FileUtils.delAllFile(scache);

                    if (dialog != null) {
                        dialog.todismiss();
                    }
                    act_setting_cacha.setContent("0M");
                } catch (Exception e) {
                    dialog.todismiss();
                    act_setting_cacha.setContent("0M");
                }
            }
        });

        //退出
        actSettingLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog == null) {
                    dialog = new LoadingDialog(SettingActivity.this, R.style.dialog);
                }
                dialog.show();
                HXJavaNet.post(HXJavaNet.url_logout, "user_id", HXApp
                        .getInstance().getUserInfo().user_id, null);
                loginHelper.imLogout();
            }
        });
    }


    @Override
    public void logoutSucc() {
        HXApp.getInstance().setUserInfo(null);
        if (dialog != null) {
            dialog.todismiss();
        }
        Intent it = new Intent(mActivity, SplashActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(it);
        finish();
    }


    @Override
    public void logoutFail() {
        if (dialog != null) {
            dialog.todismiss();
        }
        Notifier.showShortMsg(getApplication(), "请重试");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loginHelper != null) {
            loginHelper.onDestory();
        }
    }
}
