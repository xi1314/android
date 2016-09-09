package com.remair.heixiu.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.LoginHelper;
import com.remair.heixiu.LoginView;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.base.HXBaseActivity;
import com.remair.heixiu.bean.SendCodeBean;
import com.remair.heixiu.bean.UserInfo;
import com.remair.heixiu.net.HXHttpUtil;
import com.remair.heixiu.net.HttpSubscriber;
import com.remair.heixiu.utils.RxViewUtil;
import com.remair.heixiu.view.LoadingDialog;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import rx.Subscription;
import studio.archangel.toolkitv2.util.CountDownHandler;
import studio.archangel.toolkitv2.util.Utissuanfa;
import studio.archangel.toolkitv2.util.text.Notifier;
import studio.archangel.toolkitv2.views.AngelLoadingDialog;
import studio.archangel.toolkitv2.views.AngelMaterialButton;

/**
 * Created by JXHIUUI on 2016/3/15.
 */
public class ForgetPwdActivity extends HXBaseActivity implements View.OnClickListener, LoginView {
    @BindView(R.id.act_send_code) TextView act_send_code;
    @BindView(R.id.iv_delete) ImageView iv_delete;
    @BindView(R.id.act_test_send) TextView act_test_send;
    @BindView(R.id.act_register_psw) EditText act_register_psw;
    @BindView(R.id.act_register_finish) AngelMaterialButton act_register_finish;
    @BindView(R.id.iv_showhint) ImageView iv_showhint;
    @BindView(R.id.icon_delete_password) ImageView icon_delete_password;
    @BindView(R.id.act_register_phone) EditText register_phone;
    @BindView(R.id.linearLayout) LinearLayout linearLayout;
    @BindView(R.id.icon_delete_phone) ImageView delete_phone;
    @BindView(R.id.title_txt) TextView title_txt;
    @BindView(R.id.title_left_image) ImageButton title_left_image;
    boolean isShow = false;
    private String number;
    private LoginHelper loginHelper;
    private Context mContext;
    private LoadingDialog dialog;
    private AngelLoadingDialog angelDialog;


    @Override
    protected void initUI() {
        setContentView(R.layout.act_forget_pwd);
        mContext = getApplication();
        ButterKnife.bind(this);
        title_txt.setText(getString(R.string.modifypsw));
    }


    @Override
    protected void initData() {
        initListener();
        loginHelper = new LoginHelper(mContext, this);
    }


    private void initListener() {
        iv_showhint.setOnClickListener(this);
        iv_delete.setOnClickListener(this);
        act_register_finish.setOnClickListener(this);
        act_test_send.setOnClickListener(this);
        icon_delete_password.setOnClickListener(this);
        delete_phone.setOnClickListener(this);
        act_send_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                iv_delete.setVisibility(View.VISIBLE);
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        act_register_psw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                icon_delete_password.setVisibility(View.VISIBLE);
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        register_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                delete_phone.setVisibility(View.VISIBLE);
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        RxViewUtil.viewBindClick(title_left_image, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_showhint:
                if (!isShow) {
                    iv_showhint.setImageResource(R.drawable.login_psw_show);
                    isShow = true;
                    act_register_psw
                            .setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    iv_showhint.setImageResource(R.drawable.login_psw_hide);
                    isShow = false;
                    act_register_psw.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;
            case R.id.iv_delete:
                act_send_code.setText("");
                iv_delete.setVisibility(View.GONE);
                break;
            case R.id.icon_delete_password:
                act_register_psw.setText("");
                icon_delete_password.setVisibility(View.INVISIBLE);
                break;
            case R.id.icon_delete_phone:
                register_phone.setText("");
                delete_phone.setVisibility(View.INVISIBLE);
                break;
            case R.id.act_register_finish://注册
                if (number == null || number.isEmpty()) {
                    Notifier.showShortMsg(mContext, "请输入手机号码");
                    return;
                }
                String code = act_send_code.getText().toString().trim();
                if (code == null || code.isEmpty()) {
                    Notifier.showShortMsg(mContext, "请获取验证码");
                    return;
                }
                final String password = act_register_psw.getText().toString()
                                                        .trim();
                if (password == null || password.isEmpty()) {
                    Notifier.showShortMsg(mContext, "请输入密码");
                    return;
                }
                String regEx2 = "^(?!^\\d+$)(?!^[a-zA-Z]+$)[0-9_a-zA-Z]{8,16}$";
                Pattern pattern = Pattern.compile(regEx2);
                Matcher matcher = pattern.matcher(password);
                if (!matcher.matches()) {
                    Notifier.showShortMsg(mContext, "输入的密码安全性较低,请重新输入");
                    return;
                }
                Subscription subscrip = HXHttpUtil.getInstance()
                                                  .resetPwd(number, code, Utissuanfa
                                                          .md5(password))
                                                  .subscribe(new HttpSubscriber<Object>() {
                                                      @Override
                                                      public void onNext(Object o) {
                                                          login(number, password);
                                                      }


                                                      @Override
                                                      public void onError(Throwable e) {
                                                          super.onError(e);
                                                      }
                                                  });
                addSubscription(subscrip);
                break;
            case R.id.act_test_send://发送验证码
                sendTestCode();
                break;
        }
    }


    //发送验证码
    private void sendTestCode() {
        number = register_phone.getText().toString().trim();
        if (number.isEmpty()) {
            Notifier.showShortMsg(this, "请输入电话号码");
            return;
        }
        if (number.length() != 11 || !number.startsWith("1")) {
            Notifier.showNormalMsg(mContext, "手机号格式不正确");
            return;
        }
        Subscription subscrip =  HXHttpUtil.getInstance().forgetPwdSendSms(number)
                  .subscribe(new HttpSubscriber<SendCodeBean>() {
                      @Override
                      public void onStart() {
                          super.onStart();
                          dialog = new LoadingDialog(ForgetPwdActivity
                                  .this, R.style.dialog);
                      }


                      @Override
                      public void onNext(SendCodeBean sendCodeBean) {
                          if (!HXApp.isTest) {
                              Notifier.showLongMsg(getApplicationContext(),
                                      "验证码：" + sendCodeBean.sms_code);
                              act_send_code.setText(sendCodeBean.sms_code);
                          } else {
                              Notifier.showNormalMsg(getApplicationContext(), "验证码已发送");
                              CountDownHandler handler1 = new CountDownHandler(act_test_send, 60, new CountDownHandler.CountDownListener() {
                                  @Override
                                  public void onStart(Object target, int total) {
                                      act_test_send.setText(total + "S后发送");
                                      act_test_send.setEnabled(false);
                                      act_test_send.setTextColor(getResources()
                                              .getColor(R.color.white));
                                      act_test_send
                                              .setBackgroundResource(R.drawable.shape_round_corner_gray);
                                      act_test_send.setOnClickListener(null);
                                  }


                                  @Override
                                  public void onCountDown(Object target, int count) {
                                      iv_delete.setVisibility(View.VISIBLE);
                                      act_test_send.setText(count + "S后发送");
                                  }


                                  @Override
                                  public void onEnd(Object target) {
                                      act_test_send.setText("发送验证码");
                                      act_test_send
                                              .setBackgroundResource(R.drawable.shape_round_corner_blue);
                                      act_test_send.setEnabled(true);
                                      act_test_send.setTextColor(getResources()
                                              .getColor(R.color.blue));
                                      act_test_send
                                              .setOnClickListener(ForgetPwdActivity.this);
                                      iv_delete.setVisibility(View.GONE);
                                  }
                              });
                              handler1.start();
                          }
                      }


                      @Override
                      public void onError(Throwable e) {
                          super.onError(e);
                          if (dialog != null) {
                              dialog.dismiss();
                          }
                      }
                  });
        addSubscription(subscrip);
    }


    private void login(String phone_num, String password) {
        Subscription subscrip = HXHttpUtil.getInstance().loginPwd(phone_num,
                Utissuanfa.md5(password))
                  .subscribe(new HttpSubscriber<UserInfo>() {
                      @Override
                      public void onStart() {
                          super.onStart();
                          angelDialog = new AngelLoadingDialog(ForgetPwdActivity.this, R.color.hx_main);
                      }


                      @Override
                      public void onNext(UserInfo userInfo) {
                          HXApp.getInstance().setUserInfo(userInfo);
                          HXApp.getInstance().setAccount(userInfo.phone_num);
                          loginHelper.imLogin(userInfo.user_id + "", userInfo.tlsSig);
                          angelDialog.dismiss();
                      }


                      @Override
                      public void onError(Throwable e) {
                          super.onError(e);
                          angelDialog.dismiss();
                          finish();
                      }
                  });
        addSubscription(subscrip);
    }


    @Override
    public void loginSucc() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void loginFail() {
        Notifier.showNormalMsg(mContext, "请重新登录");
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loginHelper != null) {
            loginHelper.onDestory();
        }
    }
}
