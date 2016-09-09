package com.remair.heixiu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.remair.heixiu.HXActivity;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.HXJavaNet;
import com.remair.heixiu.R;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import studio.archangel.toolkitv2.util.CountDownHandler;
import studio.archangel.toolkitv2.util.Util;
import studio.archangel.toolkitv2.util.networking.AngelNetCallBack;
import studio.archangel.toolkitv2.util.text.Notifier;
import studio.archangel.toolkitv2.views.AngelMaterialButton;

/**
 * Created by wsk on 16/5/29.
 */
public class PhoCerActivity extends HXActivity implements View.OnClickListener {
    @BindView(R.id.et_phone_num) EditText et_phone_num;
    @BindView(R.id.tv_get_captcha) TextView tv_get_captcha;
    @BindView(R.id.et_withcode) EditText et_withcode;
    @BindView(R.id.btn_submit) AngelMaterialButton btn_submit;
    private final int resultcode = 22222;
    CountDownHandler handler1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_phonecertification);
        ButterKnife.bind(this);
        Util.setupActionBar(getSelf(), "我要提现");
        tv_get_captcha.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_get_captcha:
                String number = et_phone_num.getText().toString().trim();
                if (number.isEmpty()) {
                    Notifier.showShortMsg(getApplication(), "请输入电话号码");
                    return;
                }
                if (number.length() != 11 || !number.startsWith("1")) {
                    Notifier.showNormalMsg(getApplication(), "手机号格式不正确");
                    return;
                }
                handler1 = new CountDownHandler(tv_get_captcha, 60, new CountDownHandler.CountDownListener() {
                    @Override
                    public void onStart(Object target, int total) {
                        tv_get_captcha.setText(total + "S后发送");
                        tv_get_captcha.setEnabled(false);
                        tv_get_captcha.setTextColor(getResources()
                                .getColor(R.color.white));
                        tv_get_captcha
                                .setBackgroundResource(R.drawable.shape_timerto);
                        tv_get_captcha.setOnClickListener(null);
                    }


                    @Override
                    public void onCountDown(Object target, int count) {
                        tv_get_captcha.setText(count + "S后发送");
                    }


                    @Override
                    public void onEnd(Object target) {
                        tv_get_captcha.setText("发送验证码");
                        tv_get_captcha
                                .setBackgroundResource(R.drawable.shape_time);
                        tv_get_captcha.setEnabled(true);
                        tv_get_captcha
                                .setOnClickListener(PhoCerActivity.this);
                    }
                });
                handler1.start();
                HXJavaNet
                        .post(HXJavaNet.url_bindWithdrawSendSms, "phone_num", number, new AngelNetCallBack() {
                            @Override
                            public void onSuccess(int ret_code, String ret_data, String msg) {
                                if (ret_code == 200) {
                                    try {
                                        if (!"".equals(ret_data)) {
                                            JSONObject jsonObject = new JSONObject(ret_data);
                                            String smsCode = jsonObject
                                                    .getString("sms_code");
                                            et_withcode.setText(smsCode);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Notifier.showShortMsg(getApplication(), msg);
                                }
                            }


                            @Override
                            public void onFailure(String msg) {
                                Notifier.showShortMsg(getApplication(), msg);
                            }
                        });
                break;
            case R.id.btn_submit:
                String phoneNum = et_phone_num.getText().toString().trim();
                String code = et_withcode.getText().toString().trim();
                if (phoneNum.isEmpty()) {
                    Notifier.showShortMsg(getApplication(), "请输入电话号码");
                    return;
                }
                if (code.isEmpty()) {
                    Notifier.showShortMsg(getApplication(), "请输入验证码");
                    return;
                }
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("user_id", HXApp.getInstance()
                                            .getUserInfo().user_id);
                hashMap.put("phone", phoneNum);
                hashMap.put("code", code);
                HXJavaNet
                        .post(HXJavaNet.url_bindWxWithdrawPhone, hashMap, new AngelNetCallBack() {
                            @Override
                            public void onSuccess(int ret_code, String ret_data, String msg) {
                                if (ret_code == 200) {
                                    Intent intent = new Intent();
                                    intent.putExtra("code", 1);
                                    setResult(resultcode, intent);
                                } else {
                                    Intent intent = new Intent();
                                    intent.putExtra("code", 0);
                                    setResult(resultcode, intent);
                                }
                                PhoCerActivity.this.finish();
                            }


                            @Override
                            public void onFailure(String msg) {
                                Intent intent = new Intent();
                                intent.putExtra("code", 0);
                                setResult(resultcode, intent);
                                PhoCerActivity.this.finish();
                            }
                        });
                break;
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != handler1) {
            handler1 = null;
        }
    }
}
