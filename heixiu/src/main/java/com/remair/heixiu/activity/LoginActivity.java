package com.remair.heixiu.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
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
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import com.remair.heixiu.EventConstants;
import com.remair.heixiu.HXActivity;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.HXJavaNet;
import com.remair.heixiu.LoginHelper;
import com.remair.heixiu.LoginView;
import com.remair.heixiu.R;
import com.remair.heixiu.bean.ForbidBean;
import com.remair.heixiu.bean.ForbidMsgBean;
import com.remair.heixiu.bean.UserInfo;
import com.remair.heixiu.controllers.QavsdkControl;
import com.remair.heixiu.dialog.BannedDialog;
import com.remair.heixiu.net.ApiException;
import com.remair.heixiu.net.HXHttpUtil;
import com.remair.heixiu.net.HttpSubscriber;
import com.remair.heixiu.utils.HXUtil;
import com.remair.heixiu.utils.RxViewUtil;
import com.remair.heixiu.utils.Utils;
import com.remair.heixiu.view.LoadingDialog;
import com.remair.heixiu.view.PanningView;
import com.remair.heixiu.view.ShowDialog;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import rx.functions.Action1;
import studio.archangel.toolkitv2.util.CountDownHandler;
import studio.archangel.toolkitv2.util.Logger;
import studio.archangel.toolkitv2.util.Utissuanfa;
import studio.archangel.toolkitv2.util.networking.AngelNetCallBack;
import studio.archangel.toolkitv2.util.text.Notifier;
import studio.archangel.toolkitv2.views.AngelMaterialButton;

/**
 * Created by JXHIUUI on 2016/3/2.
 */
public class LoginActivity extends HXActivity implements View.OnClickListener, LoginView {

    @BindView(R.id.ll_weibo) LinearLayout llweibo;
    @BindView(R.id.ll_qq) LinearLayout llqq;
    @BindView(R.id.ll_weixin) LinearLayout llweixin;
    @BindView(R.id.iv_visible) ImageView visible;
    @BindView(R.id.et_phone) EditText et_phone;
    @BindView(R.id.et_password) EditText et_password;
    @BindView(R.id.icon_delete_phone) ImageView delete_phone;
    @BindView(R.id.icon_delete_password) ImageView delete_password;
    @BindView(R.id.btn_login) AngelMaterialButton btn_login;
    @BindView(R.id.tv_register) TextView tv_register;
    @BindView(R.id.custom_layout) RelativeLayout custom_layout;
    @BindView(R.id.pan_view) PanningView pan_view;
    @BindView(R.id.forget) TextView forget;

    private static final int[] drawables = new int[] { R.drawable.bg1 };
    private int mDrawableIndex = 0;
    private static final int MSG_SMSSDK_CALLBACK = 1;
    private static final int MSG_AUTH_CANCEL = 2;
    private static final int MSG_AUTH_ERROR = 3;
    private static final int MSG_AUTH_COMPLETE = 4;

    boolean isOnForceOffline;
    boolean login_expire;
    boolean showDalog;
    private BannedDialog mBannedDialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            Log.e("tag", msg.obj + "");
            switch (msg.what) {
                case MSG_AUTH_CANCEL:
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    //取消授权
                    Notifier.showShortMsg(getApplicationContext(), "授权操作已取消!");
                    break;
                case MSG_AUTH_ERROR:
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    //授权失败
                    //                    Notifier.showShortMsg(LoginActivity.this, "授权操作失败!");
                    break;
                case MSG_AUTH_COMPLETE:
                    //授权成功
                    if (dialog == null) {
                        dialog = new LoadingDialog(getSelf(), R.style.dialog);
                    }
                    dialog.show();
                    //                    Notifier.showShortMsg(LoginActivity.this, "授权成功，正在跳转登录操作...");
                    Object[] objs = (Object[]) msg.obj;
                    Platform platform = (Platform) objs[1];
                    HashMap res = (HashMap) objs[0];
                    //获取数据
                    PlatformDb db = platform.getDb();
                    HashMap<String, Object> params = new HashMap<>();
                    String userGender = db.getUserGender();
                    if (null == userGender || "".equals(userGender)) {
                        params.put("gender", 1);
                    } else {
                        if (userGender.equals("m")) {
                            params.put("gender", 0);
                        } else {
                            params.put("gender", 1);
                        }
                    }
                    if (platform.getName().equals("Wechat")) {
                        params.put("unionid", res.get("unionid"));
                    }
                    params.put("open_id", db.getUserId());
                    params.put("nickname", db.getUserName());
                    params.put("photo", db.getUserIcon());
                    params.put("device", "android");

                    Logger.out(params.toString());
                    String url = null;
                    String name = platform.getName();
                    if (name.equals("SinaWeibo")) {
                        url = HXJavaNet.url_sinablog;
                    } else if (name.equals("Wechat")) {
                        url = HXJavaNet.url_wxlogin_new;
                    } else if (name.equals("QQ")) {
                        url = HXJavaNet.url_qqlogin;
                    }
                    if (url == null) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        Notifier.showShortMsg(getApplicationContext(), "登录失败");
                        return;
                    }

                    HXJavaNet.post(url, params, new AngelNetCallBack() {
                        @Override
                        public void onSuccess(int ret_code, String ret_data, String msg) {
                            if (ret_code == 200) {
                                UserInfo userInfo = initUserInfo(ret_data);
                                loginHelper.imLogin(userInfo.user_id + "", userInfo.tlsSig);
                                initImageDir();
                            } else if (ret_code == 503) {
                                Notifier.showShortMsg(getApplicationContext(), msg);
                            }
                        }


                        @Override
                        public void onFailure(String msg) {
                            if (dialog != null) {
                                dialog.dismiss();
                            }
                            Notifier.showShortMsg(getApplicationContext(), "登录失败");
                        }
                    });

                    //删除授权信息
                    if (platform.isValid()) {
                        platform.removeAccount();
                    }
                    break;
                case 100:
                    mDrawableIndex++;
                    if (mDrawableIndex >= drawables.length) {
                        mDrawableIndex = 0;
                    }
                    pan_view.setImageResource(drawables[mDrawableIndex]);
                    Message obtain = Message.obtain();
                    obtain.what = 100;
                    handler.sendMessageDelayed(obtain, 15000);
                    break;
            }
            return;
        }
    };

    public View.OnClickListener loginListener;
    private long exitTime = 0;
    private boolean isVisible = false;
    private QavsdkControl mQavsdkControl;
    private LoginHelper loginHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginHelper = new LoginHelper(getApplicationContext(), this);
        setContentView(R.layout.act_login);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mBannedDialog = new BannedDialog();
        isOnForceOffline = intent.getBooleanExtra(EventConstants.ISONFORCEOFFLINE, false);
        login_expire = intent.getBooleanExtra(EventConstants.LOGIN_EXPIRE, false);
        mQavsdkControl = HXApp.getInstance().getQavsdkControl();
         /*第三方登录*/
        ShareSDK.initSDK(getApplicationContext());

        loginListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = et_phone.getText().toString().trim();
                if (phone.isEmpty()) {
                    return;
                }
                if (phone.length() != 11 || !phone.startsWith("1")) {
                    Notifier.showNormalMsg(getApplicationContext(), "手机号格式不正确");
                    return;
                }
                HXJavaNet.post(HXJavaNet.url_login_sendsms, "phone_num", phone, new AngelNetCallBack() {
                    @Override
                    public void onStart() {
                        dialog = new LoadingDialog(getSelf(), R.style.dialog);
                    }


                    @Override
                    public void onSuccess(final int status, final String json, final String readable_msg) {
                        Logger.out(status + " " + json + " " +
                                readable_msg);
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog1) {
                                if (status == 200) {
                                    if (!HXApp.isTest) {
                                        try {
                                            Notifier.showLongMsg(getApplicationContext(),
                                                    "验证码：" + new JSONObject(json).optString("sms_code"));
                                            et_password.setText(new JSONObject(json).optString("sms_code"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        Notifier.showNormalMsg(getApplicationContext(), "验证码已发送");
                                        CountDownHandler handler1 = new CountDownHandler(visible, 60, new CountDownHandler.CountDownListener() {
                                            @Override
                                            public void onStart(Object target, int total) {
                                                visible.setOnClickListener(null);
                                            }


                                            @Override
                                            public void onCountDown(Object target, int count) {
                                            }


                                            @Override
                                            public void onEnd(Object target) {
                                                RxViewUtil.viewBindClick(visible, loginListener);
                                            }
                                        });
                                        handler1.start();
                                    }
                                } else {
                                    Notifier.showNormalMsg(getApplicationContext(), readable_msg);
                                }
                            }
                        });
                        dialog.dismiss();
                    }


                    @Override
                    public void onFailure(String msg) {
                        dialog.dismiss();
                    }
                });
            }
        };

        pan_view.startPanning();

        Message obtain = Message.obtain();
        obtain.what = 200;
        handler.sendMessageDelayed(obtain, 5000);

        //        打开注册页
        RxViewUtil.viewBindClick(tv_register, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(LoginActivity.this, RegisterTwoActivity.class);
                startActivity(intent);
            }
        });

        //密码可见
        RxViewUtil.viewBindClick(visible, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (!isVisible) {
                    visible.setImageResource(R.drawable.login_psw_show);
                    isVisible = true;
                    et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    visible.setImageResource(R.drawable.selector_visible);
                    isVisible = false;
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        //登录按钮
        RxViewUtil.viewBindClick(btn_login, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                prepareLogin();
            }
        });
        et_phone.setText(HXApp.getInstance().getAccount());
        et_password.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                    prepareLogin();
                    return true;
                } else {
                    return false;
                }
            }
        });

        RxViewUtil.viewBindClick(forget, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getSelf(), ForgetPwdActivity.class);
                startActivity(intent);
            }
        });

        RxViewUtil.viewBindClick(delete_phone, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                et_phone.setText("");
            }
        });
        String trim = et_phone.getText().toString().trim();
        if (!TextUtils.isEmpty(trim)) {
            et_phone.setSelection(trim.length());
            delete_phone.setVisibility(View.VISIBLE);
        }

        //电话栏的删除按钮
        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_phone.getText().length() > 0) {
                    delete_phone.setVisibility(View.VISIBLE);
                } else {
                    delete_phone.setVisibility(View.INVISIBLE);
                }
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //密码栏的删除按钮
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_password.getText().length() > 0) {
                    delete_password.setVisibility(View.VISIBLE);
                    delete_password.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            et_password.setText("");
                        }
                    });
                } else {
                    delete_password.setVisibility(View.INVISIBLE);
                }
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //微信登录
        RxViewUtil.viewBindClick(llweixin, this);
        //qq登录
        RxViewUtil.viewBindClick(llqq, this);
        //微博登录
        RxViewUtil.viewBindClick(llweibo, this);

        if (isOnForceOffline) {

            final ShowDialog mdialog = new ShowDialog(LoginActivity.this, "系统检测到您的账号有可能在另一台设备上登录，同一时间只能保持一台设备在线，如不是本人操作，请尽快修改密码", "确定", "");
            mdialog.setCanceledOnTouchOutside(false);
            mdialog.setClicklistener(new ShowDialog.ClickListenerInterface() {
                @Override
                public void doConfirm() {

                    mdialog.dismiss();
                }


                @Override
                public void doCancel() {

                    mdialog.dismiss();
                }
            });
            mdialog.show();
        } else if (login_expire) {
            Notifier.showShortMsg(this, getString(R.string.login_expire));
        }
    }


    void prepareLogin() {
        final String phone = et_phone.getText().toString().trim();
        if (phone.isEmpty()) {
            Notifier.showShortMsg(getApplicationContext(), "请输入电话号码");
            return;
        }
        if (HXApp.isTest) {
            if (phone.length() != 11 || !phone.startsWith("1")) {
                Notifier.showNormalMsg(getApplicationContext(), "手机号格式不正确");
                return;
            }
        }
        String code = et_password.getText().toString().trim();
        if (code.isEmpty()) {
            Notifier.showShortMsg(getApplicationContext(), "请输入密码");
            return;
        }
        if (dialog == null) {
            dialog = new LoadingDialog(getSelf(), R.style.dialog);
        }
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        HXHttpUtil.getInstance().loginPwd(phone, Utissuanfa.md5(code)).subscribe(new HttpSubscriber<UserInfo>() {
            @Override
            public void onNext(UserInfo userInfo) {
                if (userInfo == null) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    Notifier.showShortMsg(getApplicationContext(), "请重试");
                    return;
                }
                HXApp.getInstance().setUserInfo(userInfo);
                if (userInfo.gender == -1 || TextUtils.isEmpty(userInfo.photo)) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    Notifier.showShortMsg(getApplicationContext(), "请设置个人资料");
                    Intent intent = new Intent(LoginActivity.this, SetProfileActivity.class);
                    intent.putExtra("user_id", userInfo.user_id);
                    intent.putExtra("phone_num", phone);
                    intent.putExtra("identity", userInfo.identity);
                    intent.putExtra("pwd", et_password.getText().toString().trim());
                    startActivityForResult(intent, 100);
                    return;
                }
                loginHelper.imLogin(userInfo.user_id + "", userInfo.tlsSig);
                HXApp.getInstance().setAccount(userInfo.phone_num);
                initImageDir();
            }


            @Override
            public void onError(Throwable e) {
                //需要显示错误信息时要调用父类的onError方法
                super.onError(e);
                if (e instanceof ApiException) {
                    ApiException apiException = (ApiException) e;
                    if (apiException.LOGIN_FORBIND.equals(apiException.getCode())) {
                        forbidReason(apiException.getForbidReasonBean());
                    }
                }
                if (dialog != null) {
                    dialog.dismiss();
                }
            }


            @Override
            public void onCompleted() {
                super.onCompleted();
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
    }


    //初始化userinfo对象，并且本地保存数据
    UserInfo initUserInfo(String json) {
        UserInfo userInfo = Utils.jsonToBean(json, UserInfo.class);
        HXApp.getInstance().setUserInfo(userInfo);
        return userInfo;
    }


    //封禁提示
    private void forbidReason(ForbidMsgBean reasonBean) {
        HXHttpUtil.getInstance().forbidReason(reasonBean.userId).subscribe(new HttpSubscriber<ForbidBean>() {
            @Override
            public void onNext(ForbidBean msg) {
                if (msg == null) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putParcelable("banned", msg);
                mBannedDialog.setArguments(bundle);
                mBannedDialog.show(getSupportFragmentManager(), "banned");
            }
        });
    }


    private void initImageDir() {
        File sd = Environment.getExternalStorageDirectory();
        String image = sd.getPath() + "/image";
        File file = new File(image);
        if (!file.exists()) {
            file.mkdir();
        }
    }


    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Notifier.showShortMsg(getApplicationContext(), "再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            HXApp.getInstance().AppExit();
            super.onBackPressed();
        }
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.dialog_fade_in, R.anim.dialog_fade_out);
    }

    //PlatformActionListener的回调方法

    PlatformActionListener platformActionListener = new LoginPlatformActionListener() {
        @Override
        public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
            if (action == Platform.ACTION_USER_INFOR) {
                Message msg = new Message();
                msg.what = MSG_AUTH_COMPLETE;
                msg.obj = new Object[] { res, platform };
                handler.sendMessage(msg);
            }
        }


        @Override
        public void onError(Platform platform, int action, Throwable t) {
            if (action == Platform.ACTION_USER_INFOR) {
                handler.sendEmptyMessage(MSG_AUTH_ERROR);
            }
            t.printStackTrace();
        }


        @Override
        public void onCancel(Platform platform, int action) {
            if (action == Platform.ACTION_USER_INFOR) {
                handler.sendEmptyMessage(MSG_AUTH_CANCEL);
            }
        }
    };
    WeakReference<PlatformActionListener> reference = new WeakReference<PlatformActionListener>(platformActionListener);


    @Override
    public void onClick(View v) {
        if (dialog == null) {
            dialog = new LoadingDialog(getSelf(), R.style.dialog);
        }
        dialog.show();
        showDalog = true;
        switch (v.getId()) {
            case R.id.ll_weibo:
                //微博登录
                Platform sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                sinaWeibo.SSOSetting(false);
                if (reference.get() != null) {
                    sinaWeibo.setPlatformActionListener(reference.get());
                }
                sinaWeibo.showUser(null);
                break;
            case R.id.ll_weixin:
                //微信登录
                Platform weChat = ShareSDK.getPlatform(Wechat.NAME);
                weChat.SSOSetting(false);
                if (reference.get() != null) {
                    weChat.setPlatformActionListener(reference.get());
                }
                weChat.showUser(null);
                break;
            case R.id.ll_qq:
                // QQ登录
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                qq.SSOSetting(false);
                if (reference.get() != null) {
                    qq.setPlatformActionListener(reference.get());
                }
                qq.showUser(null);
                break;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (dialog != null) {
            dialog.dismiss();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (showDalog) {
            if (dialog == null) {
                dialog = new LoadingDialog(getSelf(), R.style.dialog);
            }
            dialog.show();
        }
        showDalog = false;
    }


    @Override
    protected void onDestroy() {
        ShareSDK.stopSDK();
        super.onDestroy();
        if (loginHelper != null) {
            loginHelper.onDestory();
        }
        if (null != pan_view) {
            handler.removeCallbacksAndMessages(null);
            pan_view.stopPanning();
            pan_view = null;
        }
        HXUtil.fixInputMethodManagerLeak(this);
    }


    /**
     * qavsdk 注册信息
     */
    private void startContext(String sig, String id, String tag) {
        if (!mQavsdkControl.hasAVContext()) {
            if ("".equals(sig)) {
                finish();
                return;
            }
            int code = mQavsdkControl.startContext(id, sig, tag);
            Logger.out("startContext mLoginErrorCode   " + code);
        } else {
            mQavsdkControl.stopContext();
            mQavsdkControl.setIsInStopContext(false);
        }
    }


    @Override
    public void loginSucc() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        finish();
    }


    @Override
    public void loginFail() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        Notifier.showShortMsg(getApplicationContext(), "聊天服务初始化失败，请重试");
    }


    private static class LoginPlatformActionListener implements PlatformActionListener {

        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

        }


        @Override
        public void onError(Platform platform, int i, Throwable throwable) {

        }


        @Override
        public void onCancel(Platform platform, int i) {

        }
    }
}
