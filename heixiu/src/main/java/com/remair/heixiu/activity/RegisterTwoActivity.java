package com.remair.heixiu.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.base.HXBaseActivity;
import com.remair.heixiu.bean.UserInfo;
import com.remair.heixiu.net.HXHttpUtil;
import com.remair.heixiu.net.HttpSubscriber;
import com.remair.heixiu.utils.RxViewUtil;
import com.remair.heixiu.view.LoadingDialog;
import com.remair.heixiu.view.PanningView;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;
import studio.archangel.toolkitv2.util.CountDownHandler;
import studio.archangel.toolkitv2.util.Utissuanfa;
import studio.archangel.toolkitv2.util.text.Notifier;
import studio.archangel.toolkitv2.views.AngelMaterialButton;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：JXH
 * 创建时间：16/3/2 21:02
 * 修改人：JXH
 * 修改时间：16/3/2 21:02
 * 修改备注：
 */
public class RegisterTwoActivity extends HXBaseActivity implements View.OnClickListener, TextWatcher {
    @BindView(R.id.act_send_code) TextView act_send_code;
    @BindView(R.id.iv_delete) ImageView iv_delete;
    @BindView(R.id.act_test_send) TextView act_test_send;
    @BindView(R.id.act_register_psw) EditText act_register_psw;
    @BindView(R.id.act_register_finish) AngelMaterialButton act_register_finish;
    @BindView(R.id.iv_showhint) ImageView iv_showhint;
    @BindView(R.id.icon_delete_password) ImageView icon_delete_password;
    @BindView(R.id.act_register_phone) EditText register_phone;
    @BindView(R.id.iv_finish) ImageView iv_finish;
    @BindView(R.id.pan_view) PanningView pan_view;
    @BindView(R.id.icon_delete_phone) ImageView icon_delete_phone;
    @BindView(R.id.tv_agreement) TextView tv_agreement;
    boolean isShow = false;
    protected String number;
    protected Context mContext;
    protected LoadingDialog dialog;


    @Override
    protected void initUI() {
        setContentView(R.layout.register_two_activity);
        ButterKnife.bind(this);
    }


    @Override
    protected void initData() {
        mContext = getApplicationContext();
        pan_view.startPanning();
        initdata();
    }


    private void initdata() {
        iv_showhint.setOnClickListener(this);
        iv_delete.setOnClickListener(this);
        icon_delete_password.setOnClickListener(this);
        icon_delete_phone.setOnClickListener(this);
        RxViewUtil.viewBindClick(iv_finish, this);
        RxViewUtil.viewBindClick(act_register_finish, this);
        RxViewUtil.viewBindClick(act_test_send, this);
        RxViewUtil.viewBindClick(tv_agreement, this);
        act_send_code.addTextChangedListener(this);
        act_register_psw.addTextChangedListener(this);
        register_phone.addTextChangedListener(this);
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
                icon_delete_phone.setVisibility(View.INVISIBLE);
                break;
            case R.id.iv_finish:
                finish();
                break;
            case R.id.tv_agreement:
                Intent intent = new Intent(mContext, ProtocolActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
            case R.id.act_register_finish://注册
                startRegist();
                break;
            case R.id.act_test_send://发送验证码
                sendTestCode();
                break;
        }
    }


    protected void startRegist() {
        number = register_phone.getText().toString().trim();
        if (number.isEmpty()) {
            Notifier.showShortMsg(mContext, getString(R.string.regist_phone));
            return;
        }
        final String code = act_send_code.getText().toString().trim();
        if (code.isEmpty()) {
            Notifier.showShortMsg(mContext, getString(R.string.regist_code));
            return;
        }
        final String password = act_register_psw.getText().toString().trim();
        if (password.isEmpty()) {
            Notifier.showShortMsg(getApplicationContext(), getString(R.string.regist_pwd));
            return;
        }
        String regEx2 = "^(?!^\\d+$)(?!^[a-zA-Z]+$)[0-9_a-zA-Z]{8,16}$";
        Pattern pattern = Pattern.compile(regEx2);
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            Notifier.showShortMsg(mContext, getString(R.string.pwd_err_hint));
            return;
        }
        if (dialog == null) {
            dialog = new LoadingDialog(mActivity, R.style.dialog);
        }
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        final String value = Utissuanfa.md5(password);

        HXHttpUtil.getInstance().register(number, code, value)
                  .subscribe(new HttpSubscriber<Object>() {
                      @Override
                      public void onNext(Object o) {
                          login(number, value);
                      }


                      @Override
                      public void onError(Throwable e) {
                          super.onError(e);
                          if (dialog != null) {
                              dialog.dismiss();
                          }
                      }
                  });
    }


    void login(String phone, String pwd) {
        HXHttpUtil.getInstance().loginPwd(phone,pwd)
                  .subscribe(new HttpSubscriber<UserInfo>() {
                      @Override
                      public void onNext(UserInfo userInfo) {
                          if (userInfo == null) {
                              if (dialog != null) {
                                  dialog.dismiss();
                              }
                              Notifier.showShortMsg(getApplicationContext(), getString(R.string.retry));
                              return;
                          }
                          HXApp.getInstance().setAccount(userInfo.phone_num);
                          HXApp.getInstance().setUserInfo(userInfo);
                          Notifier.showShortMsg(mContext, getString(R.string.regist_suc));
                          Intent intent = new Intent(mActivity, SetProfileActivity.class);
                          startActivity(intent);
                          finish();
                      }


                      @Override
                      public void onError(Throwable e) {
                          super.onError(e);
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


    //发送验证码
    private void sendTestCode() {
        number = register_phone.getText().toString().trim();
        if (number.isEmpty()) {
            Notifier.showShortMsg(mContext, getString(R.string.regist_phone_hint));
            return;
        }
        if (number.length() != 11 || !number.startsWith("1")) {
            Notifier.showNormalMsg(mContext, getString(R.string.regist_phone_hint1));
            return;
        }
        HXHttpUtil.getInstance().registSendSms(number).subscribe(new HttpSubscriber<Object>() {
            @Override
            public void onStart() {
                super.onStart();
                dialog = new LoadingDialog(mActivity, R.style.dialog);
            }


            @Override
            public void onNext(Object o) {
                if (dialog != null) {
                    dialog.todismiss();
                }
                if (!HXApp.isTest) {
                    try {
                        Notifier.showLongMsg(mContext, "验证码：" +
                                new JSONObject(o.toString()).optString("sms_code"));
                        act_send_code.setText(new JSONObject(o.toString())
                                .optString("sms_code"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Notifier.showNormalMsg(mContext, getString(R.string.regist_code_suc));
                }
                CountDownHandler handler1 = new CountDownHandler(act_test_send, 60, new CountDownHandler.CountDownListener() {
                    @Override
                    public void onStart(Object target, int total) {
                        act_test_send.setText(total + "S后发送");
                        act_test_send.setEnabled(false);
                        act_test_send.setTextColor(getResources()
                                .getColor(R.color.give_zuan));
                        act_test_send
                                .setBackgroundResource(R.drawable.shape_round_corner_back);
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
                                .setBackgroundResource(R.drawable.shape_round_corner_back);
                        act_test_send.setEnabled(true);
                        RxViewUtil
                                .viewBindClick(act_test_send, RegisterTwoActivity.this);
                        iv_delete.setVisibility(View.GONE);
                    }
                });
                handler1.start();
                try {
                    JSONObject jsonObject = new JSONObject(o.toString());
                    String sms_code = jsonObject.optString("sms_code");
                    act_send_code.setText(sms_code);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onError(Throwable e) {
                super.onError(e);
                Notifier.showNormalMsg(mContext, e.getMessage().toString());
                if (dialog != null) {
                    dialog.todismiss();
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != pan_view) {
            pan_view.stopPanning();
            pan_view = null;
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }


    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        View rootview = getWindow().getDecorView();
        int viewId = rootview.findFocus().getId();
        switch (viewId) {
            case R.id.act_send_code:
                iv_delete.setVisibility(View.VISIBLE);
                break;
            case R.id.act_register_psw:
                icon_delete_password.setVisibility(View.VISIBLE);
                break;
            case R.id.act_register_phone:
                icon_delete_phone.setVisibility(View.VISIBLE);
                break;
        }
    }


    @Override
    public void afterTextChanged(Editable s) {

    }
}
