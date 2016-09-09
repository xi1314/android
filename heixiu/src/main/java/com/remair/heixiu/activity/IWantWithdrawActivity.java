package com.remair.heixiu.activity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

import com.badoo.mobile.util.WeakHandler;
import com.remair.heixiu.DemoConstants;
import com.remair.heixiu.HXActivity;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.HXJavaNet;
import com.remair.heixiu.R;
import com.remair.heixiu.bean.UserInfo;
import com.remair.heixiu.utils.Xtgrade;
import com.remair.heixiu.view.LoadingDialog;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.HashMap;

import studio.archangel.toolkitv2.util.Util;
import studio.archangel.toolkitv2.util.networking.AngelNetCallBack;
import studio.archangel.toolkitv2.util.text.Notifier;
import studio.archangel.toolkitv2.views.AngelMaterialButton;

/**
 * Created by wsk on 16/5/26.
 */
public class IWantWithdrawActivity extends HXActivity implements View.OnClickListener, PlatformActionListener {
    @BindView(R.id.tv_money) TextView tv_money;
    @BindView(R.id.et_withdraw) EditText et_withdraw;
    @BindView(R.id.iv_checkbox) ImageView iv_checkbox;
    @BindView(R.id.labor_agreement) TextView labor_agreement;
    @BindView(R.id.tv_tips2) TextView tv_withdraw_amout;
    @BindView(R.id.btn_submit) AngelMaterialButton btn_submit;
    @BindView(R.id.rl_with_cusses) RelativeLayout rl_with_cusses;
    @BindView(R.id.ll_withdraw) LinearLayout ll_withdraw;
    private boolean ischeckbox = false;
    private static final int MSG_AUTH_CANCEL = 2;
    private static final int MSG_AUTH_ERROR = 3;
    private static final int MSG_AUTH_COMPLETE = 4;
    private boolean isBind = false;
    private double todayMoney, monthMoney;
    WeakHandler handler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what) {
                case MSG_AUTH_CANCEL:
                    //取消授权
                    Notifier.showShortMsg(IWantWithdrawActivity.this, "授权操作已取消!");
                    break;
                case MSG_AUTH_ERROR:
                    //授权失败
                    Notifier.showShortMsg(IWantWithdrawActivity.this, "授权操作失败!");
                    break;
                case MSG_AUTH_COMPLETE:
                    //授权成功
                    final UserInfo userInfo = HXApp.getInstance().getUserInfo();
                    if (userInfo == null) {
                        return true;
                    }
                    Object[] obj = (Object[]) msg.obj;
                    final Platform platform = (Platform) obj[1];
                    final String openid = platform.getDb().getUserId();

                    //绑定微信
                    HashMap<String, Object> params = new HashMap<>();
                    params.put("user_id", userInfo.user_id);
                    params.put("openid", openid);
                    HXJavaNet
                            .post(HXJavaNet.url_bindwithdraw, params, new AngelNetCallBack() {
                                @Override
                                public void onSuccess(int ret_code, String ret_data, String msg) {
                                    if (ret_code == 200) {

                                        userInfo.wx_withdraw_openid = openid;

                                        isBind = true;
                                        Notifier.showShortMsg(IWantWithdrawActivity.this, "授权成功！");
                                    } else {
                                        Notifier.showShortMsg(mContext, msg);
                                    }
                                }


                                @Override
                                public void onFailure(String msg) {
                                    Notifier.showShortMsg(mContext, msg);
                                }
                            });
                    if (platform.isValid()) {
                        platform.removeAccount();
                    }

                    break;
            }
            return false;
        }
    });
    private Application mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UserInfo userInfo = HXApp.getInstance().getUserInfo();
        if (userInfo == null) {
            finish();
            return;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wantwithdraw_activity);
        ButterKnife.bind(this);
        mContext = getApplication();
        Util.setupActionBar(getSelf(), "我要提现");

        //是否绑定微信

        isBind = !(TextUtils.isEmpty(userInfo.wx_withdraw_openid));

        iv_checkbox.setOnClickListener(this);
        labor_agreement.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        btn_submit
                .setBackgroundColor(getResources().getColor(R.color.text_grey));
        todayMoney = getIntent().getDoubleExtra("todayMoney", 0.0);
        monthMoney = getIntent().getDoubleExtra("monthMoney", 0.0);
        tv_money.setText(Xtgrade.double2Str(monthMoney) + "元");
        ll_withdraw.setVisibility(View.VISIBLE);
        rl_with_cusses.setVisibility(View.GONE);
        tv_withdraw_amout.setText(getString(R.string.withdraw_message2, HXApp
                .getInstance().configMessage.money_limit, HXApp.getInstance()
                .configMessage.money_limit));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_checkbox:
                if (!ischeckbox) {
                    iv_checkbox.setImageResource(R.drawable.checkbox_selected);
                    btn_submit.setBackgroundColor(getResources()
                            .getColor(R.color.hx_main));
                    ischeckbox = true;
                } else {
                    iv_checkbox.setImageResource(R.drawable.checkbox_hint);
                    btn_submit.setBackgroundColor(getResources()
                            .getColor(R.color.text_grey));
                    ischeckbox = false;
                }
                break;
            case R.id.labor_agreement://协议
                Intent intentxy = new Intent(IWantWithdrawActivity.this, ProtocolActivity.class);
                intentxy.putExtra("type", 3);
                startActivity(intentxy);
                break;
            case R.id.btn_submit://提现
                if (ischeckbox) {
                    withdrawing();
                } else {
                    Notifier.showShortMsg(mContext, "您还没有勾选同意劳务协议");
                }

                break;
        }
    }


    private void withdrawing() {
        if (isBind) {
            if (todayMoney > 0) {
                String count = et_withdraw.getText().toString();
                if (count.isEmpty()) {
                    Notifier.showShortMsg(this, "取款金额不能为空");
                    return;
                }
                double withdrawCount = Double
                        .parseDouble(et_withdraw.getText().toString().trim());
                if (withdrawCount > todayMoney) {
                    Notifier.showShortMsg(this, "取款金额不能大于可提取金额");
                    return;
                }
                if (withdrawCount > 0 &&
                        withdrawCount <= HXApp.getInstance().configMessage.money_limit) {
                    withdraw(withdrawCount);
                } else {
                    Notifier.showShortMsg(mContext,
                            "金额大于" + HXApp.getInstance().configMessage.money_limit +
                                    "元,请到微信公众号提取");
                }
            } else {
                Notifier.showShortMsg(mContext, "可提现金额不足");
            }
        } else {
            IWXAPI msgApi = WXAPIFactory.createWXAPI(mContext, null);
            msgApi.registerApp(DemoConstants.WX_APPID);

            boolean sIsWXAppInstalledAndSupported = msgApi.isWXAppInstalled() &&
                    msgApi.isWXAppSupportAPI();

            if (!sIsWXAppInstalledAndSupported) {
                Notifier.showShortMsg(mContext, "您没有安装微信，请安装后再进行支付！");
                return;
            }
            Notifier.showShortMsg(mContext, "您还没有绑定微信，请授权绑定微信");
            Platform weChat = ShareSDK.getPlatform(Wechat.NAME);
            if (weChat == null) {
                return;
            }
            weChat.SSOSetting(false);
            weChat.setPlatformActionListener(IWantWithdrawActivity.this);
            weChat.showUser(null);
        }
    }


    private void withdraw(double withdrawCount) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user_id", HXApp.getInstance().getUserInfo().user_id);
        hashMap.put("withdraw_money", withdrawCount);
        HXJavaNet
                .post(HXJavaNet.url_withdrawne, hashMap, new AngelNetCallBack() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        if (null == dialog) {
                            dialog = new LoadingDialog(getSelf(), R.style.dialog);
                        }
                    }


                    @Override
                    public void onSuccess(final int ret_code, String ret_data, final String msg) {
                        if (ret_code == 200) {
                            ll_withdraw.setVisibility(View.GONE);
                            rl_with_cusses.setVisibility(View.VISIBLE);
                        } else {
                            Notifier.showShortMsg(mContext, msg);
                        }
                        dialog.dismiss();
                    }


                    @Override
                    public void onFailure(String msg) {
                        Notifier.showShortMsg(mContext, msg);
                        dialog.dismiss();
                    }
                });
    }


    @Override
    public void onComplete(Platform platform, int action, HashMap<String, Object> hashMap) {
        if (action == Platform.ACTION_USER_INFOR) {
            Message msg = new Message();
            msg.what = MSG_AUTH_COMPLETE;
            msg.obj = new Object[] { platform.getName(), platform };
            handler.sendMessage(msg);
        }
    }


    @Override
    public void onError(Platform platform, int action, Throwable throwable) {
        if (action == Platform.ACTION_USER_INFOR) {
            handler.sendEmptyMessage(MSG_AUTH_ERROR);
        }

        throwable.printStackTrace();
    }


    @Override
    public void onCancel(Platform platform, int action) {
        if (action == Platform.ACTION_USER_INFOR) {
            handler.sendEmptyMessage(MSG_AUTH_CANCEL);
        }
    }
}
