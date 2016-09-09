package com.remair.heixiu.activity;

import android.content.Intent;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.HXJavaNet;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.base.HXBaseActivity;
import com.remair.heixiu.bean.UserInfo;
import com.remair.heixiu.net.HXHttpUtil;
import com.remair.heixiu.net.HttpSubscriber;
import com.remair.heixiu.utils.RxViewUtil;
import com.remair.heixiu.utils.Xtgrade;
import com.remair.heixiu.view.LoadingDialog;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import rx.functions.Action1;
import studio.archangel.toolkitv2.util.CountDownHandler;
import studio.archangel.toolkitv2.util.Utissuanfa;
import studio.archangel.toolkitv2.util.networking.AngelNetCallBack;
import studio.archangel.toolkitv2.util.text.Notifier;

/**
 * Created by JXHIUUI on 2016/7/21.
 */
public class BindPhoneNumActivity extends HXBaseActivity implements View.OnClickListener {
    @BindView(R.id.et_phone) EditText etPhone;
    @BindView(R.id.et_check) EditText etCheck;
    @BindView(R.id.et_password) EditText et_password;
    @BindView(R.id.iv_visible) ImageView iv_visible;
    //布局0为绑定手机
    @BindView(R.id.BindPhone1) RelativeLayout BindPhone1;
    @BindView(R.id.sure_etphone) TextView sure_etphone;
    @BindView(R.id.text_yanzhengma_hint) TextView text_yanzhengma_hint;
    @BindView(R.id.text_passwordhit) TextView text_passwordhit;

    //布局1设置密码
    @BindView(R.id.seting_password) RelativeLayout seting_password;
    @BindView(R.id.et_new_password1) EditText et_new_password1;
    @BindView(R.id.et_new_password1show) ImageView et_new_password1show;
    @BindView(R.id.et_new_phone1) TextView et_new_phone1;
    @BindView(R.id.linearLayout_shouyi) LinearLayout linearLayout_shouyi;
    //布局2修改密码
    @BindView(R.id.seting_updatepassword) RelativeLayout seting_updatepassword;
    @BindView(R.id.et_password_orld2) EditText et_password_orld2;
    @BindView(R.id.et_password_new2) EditText et_password_new2;
    @BindView(R.id.iv_visible2) ImageView iv_visible2;
    @BindView(R.id.et_password_new2show) ImageView et_password_new2show;
    @BindView(R.id.title_txt) TextView mTitleTxt;
    @BindView(R.id.title_right_text) TextView mTitleRightText;

    private String number;
    private LoadingDialog dialog;
    String isbind;
    private final int resultcode = 22222;

    TextView tvSend;


    @Override
    protected void initUI() {

        setContentView(R.layout.act_phone_num);
        ButterKnife.bind(this);
    }


    @Override
    protected void initData() {

        tvSend = (TextView) findViewById(R.id.tv_send1);
        isbind = getIntent().getStringExtra("isbind");
        String phone = null;

        mTitleRightText.setText("确认");
        mTitleRightText.setVisibility(View.VISIBLE);

        if ("2".equals(isbind)) {
            mTitleTxt.setText("修改密码");
            seting_updatepassword.setVisibility(View.VISIBLE);
        } else if ("1".equals(isbind)) {
            mTitleTxt.setText("设置密码");

            seting_password.setVisibility(View.VISIBLE);
        } else if ("3".equals(isbind)) {
            mTitleTxt.setText("设置密码");
            seting_password.setVisibility(View.VISIBLE);
            linearLayout_shouyi.setVisibility(View.VISIBLE);
            final String phone_num = HXApp.getInstance()
                                          .getUserInfo().phone_num;
            et_new_phone1.setText(phone_num.substring(0, 3) + "****" +
                    phone_num.substring(7, 11));
        } else {
            mTitleTxt.setText("绑定手机");
            BindPhone1.setVisibility(View.VISIBLE);
        }

        RxViewUtil.viewBindClick(mTitleRightText, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if ("2".equals(isbind)) {
                    bindsetingupdatePhone();
                } else if ("1".equals(isbind)) {
                    bindsetingPhone();
                } else if ("3".equals(isbind)) {
                    bindsetingPhone();
                } else {
                    bindPhone();
                }
            }
        });

        if (phone != null && !phone.isEmpty()) {
            etPhone.setText(phone);
            etPhone.setSelection(phone.length());
        }
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode();
            }
        });

        initview();
    }


    private void initview() {

        et_password_orld2.setOnFocusChangeListener(new View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    iv_visible2.setVisibility(View.VISIBLE);
                } else {
                    iv_visible2.setVisibility(View.GONE);
                    // 此处为失去焦点时的处理内容
                }
            }
        });

        et_password_orld2.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);

        et_password_new2.setOnFocusChangeListener(new View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    et_password_new2show.setVisibility(View.VISIBLE);
                } else {
                    et_password_new2show.setVisibility(View.GONE);
                    // 此处为失去焦点时的处理内容
                }
            }
        });

        et_new_password1.setOnFocusChangeListener(new View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    et_new_password1show.setVisibility(View.VISIBLE);
                } else {
                    et_new_password1show.setVisibility(View.GONE);
                    // 此处为失去焦点时的处理内容
                }
            }
        });

        et_password.setOnFocusChangeListener(new View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    iv_visible.setVisibility(View.VISIBLE);
                } else {
                    iv_visible.setVisibility(View.GONE);
                    text_passwordhit.setVisibility(View.GONE);
                    // 此处为失去焦点时的处理内容
                }
            }
        });

        et_password_orld2.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);
        et_password_new2.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);
        et_new_password1.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);
        et_password.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);

        RxViewUtil.viewBindClick(iv_visible, this);
        RxViewUtil.viewBindClick(et_new_password1show, this);
        RxViewUtil.viewBindClick(iv_visible2, this);
        RxViewUtil.viewBindClick(et_password_new2show, this);
    }


    boolean isShow = false;
    boolean isShow1 = false;
    boolean isShow2 = false;
    boolean isShow3 = false;


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_visible:
                if (!isShow) {
                    iv_visible.setImageResource(R.drawable.hintpassword);
                    isShow = true;
                    et_password
                            .setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    et_password.setSelection(et_password.length());
                } else {
                    iv_visible.setImageResource(R.drawable.showpassword);
                    isShow = false;
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    et_password.setSelection(et_password.length());
                }
                break;

            case R.id.et_new_password1show:
                if (!isShow1) {
                    et_new_password1show
                            .setImageResource(R.drawable.hintpassword);
                    isShow1 = true;
                    et_new_password1
                            .setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    et_new_password1.setSelection(et_new_password1.length());
                } else {
                    et_new_password1show
                            .setImageResource(R.drawable.showpassword);
                    isShow1 = false;
                    et_new_password1.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    et_new_password1.setSelection(et_new_password1.length());
                }
                break;

            case R.id.iv_visible2:
                if (!isShow2) {
                    iv_visible2.setImageResource(R.drawable.hintpassword);
                    isShow2 = true;
                    et_password_orld2
                            .setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    et_password_orld2.setSelection(et_password_orld2.length());
                } else {
                    iv_visible2.setImageResource(R.drawable.showpassword);
                    isShow2 = false;
                    et_password_orld2.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    et_password_orld2.setSelection(et_password_orld2.length());
                }
                break;

            case R.id.et_password_new2show:
                if (!isShow3) {
                    et_password_new2show
                            .setImageResource(R.drawable.hintpassword);
                    isShow3 = true;
                    et_password_new2
                            .setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    et_password_new2.setSelection(et_password_new2.length());
                } else {
                    et_password_new2show
                            .setImageResource(R.drawable.showpassword);
                    isShow3 = false;
                    et_password_new2.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    et_password_new2.setSelection(et_password_new2.length());
                }
                break;
        }
    }


    private void bindsetingupdatePhone() {
         String passwordOld = et_password_orld2.getText().toString().trim();
        String passwordNew = et_password_new2.getText().toString().trim();
        if (passwordOld.isEmpty()) {
            Notifier.showShortMsg(this, "请输入旧密码");
            return;
        } else {
            passwordOld = Utissuanfa.md5(passwordOld);
        }

        if (passwordNew.isEmpty()) {
            Notifier.showShortMsg(this, "请输入新密码");
            return;
        } else if (Xtgrade.checkpassword(passwordNew)) {
            Notifier.showShortMsg(this, "登录密码必数字和字母的混合且大于八个字符");
            return;
        } else {
            passwordNew = Utissuanfa.md5(passwordNew);
        }
        if (passwordNew.equals(passwordOld)) {
            Notifier.showShortMsg(this, "新密码不能和旧密码相同");

            return;
        }


        HXHttpUtil.getInstance().editUserPassword(passwordOld, passwordNew)
                  .subscribe(new HttpSubscriber<String>() {
                      @Override
                      public void onNext(String s) {
                          Notifier.showShortMsg(BindPhoneNumActivity.this, "修改密码成功");
                          finish();
                      }


                      @Override
                      public void onError(Throwable e) {
                          super.onError(e);
                          Notifier.showShortMsg(BindPhoneNumActivity.this, "修改密码失败");
                      }
                  });
    }


    private void bindsetingPhone() {

        String password = et_new_password1.getText().toString().trim();
        String number = HXApp.getInstance().getUserInfo().phone_num;

        if (password.isEmpty()) {
            Notifier.showShortMsg(this, "请输入密码");
            return;
        } else if (Xtgrade.checkpassword(password)) {
            Notifier.showShortMsg(this, "登录密码必数字和字母的混合且大于八个字符");
            return;
        } else {
            password = Utissuanfa.md5(password).toLowerCase();
        }

        if (number.isEmpty()) {
            Notifier.showShortMsg(this, "请重新绑定");
            return;
        }

        HXHttpUtil.getInstance().bindWxPhoneAndPsd(number, password)
                  .subscribe(new HttpSubscriber<String>() {
                      @Override
                      public void onNext(String s) {
                          Notifier.showShortMsg(BindPhoneNumActivity.this, "绑定手机成功");
                          //体现绑定手机号的时候会走
                          UserInfo userinfo = HXApp.getInstance().getUserInfo();
                          if (null != userinfo) {
                              HXApp.getInstance().getUserInfo().password = true;
                          }

                          Intent intent = new Intent();
                          intent.putExtra("code", 1);
                          setResult(resultcode, intent);
                          finish();
                      }


                      @Override
                      public void onError(Throwable e) {
                          super.onError(e);
                      }
                  });
    }


    private void bindPhone() {
        number = etPhone.getText().toString().trim();
        String trim = etCheck.getText().toString().trim();

        String password = et_password.getText().toString().trim();
        if (number.isEmpty()) {
            Notifier.showShortMsg(this, "请输入电话号码");
            return;
        }
        if (trim.isEmpty()) {
            Notifier.showShortMsg(this, "请输入验证码");
            return;
        }

        if (password.isEmpty()) {
            Notifier.showShortMsg(this, "请输入密码");
            return;
        } else if (Xtgrade.checkpassword(password)) {
            Notifier.showShortMsg(this, "登录密码必数字和字母的混合且大于八个字符");
            text_passwordhit.setVisibility(View.VISIBLE);
            return;
        }
        HXHttpUtil.getInstance()
                  .bindWxPhoneAndPsd(number, Utissuanfa.md5(password)
                                                       .toLowerCase(), trim)
                  .subscribe(new HttpSubscriber<String>() {
                      @Override
                      public void onNext(String s) {
                          Notifier.showShortMsg(BindPhoneNumActivity.this, "绑定手机成功");
                          //    sp.edit().putString(DemoConstants.LOCAL_PHONE, number).apply();
                          Notifier.showShortMsg(getApplicationContext(), "绑定成功");
                          UserInfo userinfo = HXApp.getInstance().getUserInfo();
                          if (null != userinfo) {
                              HXApp.getInstance()
                                   .getUserInfo().phone_num = number;
                              HXApp.getInstance().getUserInfo().password = true;
                          }
                          setResult(HXApp.result_ok);
                          //体现绑定手机号的时候会走
                          Intent intent = new Intent();
                          intent.putExtra("code", 1);
                          setResult(resultcode, intent);
                          finish();
                      }


                      @Override
                      public void onError(Throwable e) {
                          super.onError(e);
                      }
                  });
    }


    //发送验证码
    private void sendCode() {
        number = etPhone.getText().toString().trim();
        sure_etphone.setVisibility(View.VISIBLE);
        if (number.isEmpty()) {
            Notifier.showShortMsg(getApplication(), "请输入电话号码");
            sure_etphone.setText("请输入手机号码");
            return;
        }
        if (number.length() != 11 || !number.startsWith("1")) {
            Notifier.showNormalMsg(getApplication(), "手机号格式不正确");
            sure_etphone.setText("请输入正确的手机号码");
            return;
        }

        HashMap<String, Object> pamars = new HashMap<>();
        pamars.put("phone_num", number);
        HXJavaNet
                .post(HXJavaNet.url_bindWithdrawSendSms, pamars, new AngelNetCallBack() {
                    @Override
                    public void onStart() {
                        if (dialog == null) {
                            dialog = new LoadingDialog(BindPhoneNumActivity
                                    .this, R.style.dialog);
                        }
                        dialog.show();
                    }


                    @Override
                    public void onSuccess(final int status, final String json, final String msg) {
                        dialog.todismiss();
                        if (status == 200) {
                            sure_etphone.setVisibility(View.GONE);
                            if (!HXApp.isTest) {
                                try {
                                    Notifier.showLongMsg(getApplication(),
                                            "验证码：" + new JSONObject(json)
                                                    .optString("sms_code"));
                                    etCheck.setText(new JSONObject(json)
                                            .optString("sms_code"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Notifier.showNormalMsg(getApplicationContext(), "验证码已发送");
                            }

                            CountDownHandler handler1 = new CountDownHandler(tvSend, 60, new CountDownHandler.CountDownListener() {
                                @Override
                                public void onStart(Object target, int total) {
                                    tvSend.setText(total + "S后发送");
                                    tvSend.setEnabled(false);
                                    tvSend.setTextColor(getResources()
                                            .getColor(R.color.white));
                                    tvSend.setBackgroundResource(R.drawable.shape_round_corner_gray);
                                    tvSend.setOnClickListener(null);
                                }


                                @Override
                                public void onCountDown(Object target, int count) {
                                    tvSend.setText(count + "S后发送");
                                }


                                @Override
                                public void onEnd(Object target) {
                                    tvSend.setText("发送验证码");
                                    tvSend.setTextColor(getResources()
                                            .getColor(R.color.input_text_color));
                                    tvSend.setBackgroundResource(R.drawable.shape_check);
                                    tvSend.setEnabled(true);
                                    tvSend.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            sendCode();
                                        }
                                    });
                                }
                            });
                            handler1.start();
                        } else {
                            Notifier.showLongMsg(getApplicationContext(), msg);
                        }
                    }


                    @Override
                    public void onFailure(String msg) {
                        Notifier.showLongMsg(getApplicationContext(), "出错了，请重试");
                        dialog.todismiss();
                    }
                });
    }

}
