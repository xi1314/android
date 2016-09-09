package com.remair.heixiu.activity;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.base.HXBaseActivity;
import com.remair.heixiu.net.HXHttpUtil;
import com.remair.heixiu.net.HttpSubscriber;
import com.remair.heixiu.utils.RxViewUtil;
import com.remair.heixiu.utils.Utils;
import com.tencent.av.sdk.AVConstants;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import rx.functions.Action1;
import studio.archangel.toolkitv2.util.Util;
import studio.archangel.toolkitv2.util.text.Notifier;
import studio.archangel.toolkitv2.views.AngelMaterialButton;

/**
 * 反馈页面
 * Created by JXHIUUI on 2016/3/4.
 */
public class FeedbackActivity extends HXBaseActivity {
    @BindView(R.id.btn_commit) AngelMaterialButton btn_commit;
    @BindView(R.id.et_fb_des) EditText et_fb_des;
    @BindView(R.id.et_fb_phone) EditText et_fb_phone;
    @BindView(R.id.title_txt) TextView mTitleTxt;
    @BindView(R.id.title_left_image) ImageButton mTitleLeftImage;


    @Override
    protected void initUI() {
        setContentView(R.layout.act_feedback);
        ButterKnife.bind(this);
        mTitleTxt.setText("用户反馈");
    }


    @Override
    protected void initData() {
        RxViewUtil.viewBindClick(mTitleLeftImage, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Util.hideInputBoard(mActivity);
                onBackPressed();
            }
        });
        RxViewUtil.viewBindClick(btn_commit, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                String des = et_fb_des.getText().toString().trim();
                if (TextUtils.isEmpty(des)) {
                    Notifier.showShortMsg(FeedbackActivity.this, "请输入您的问题或建议！");
                    return;
                }
                String phone = et_fb_phone.getText().toString().trim();
                String regEx2 = "^[0-9]*$";
                Pattern pattern = Pattern.compile(regEx2);
                Matcher matcher = pattern.matcher(phone);
                if (!matcher.matches()) {
                    Notifier.showShortMsg(FeedbackActivity.this, "请输入正确的电话号码！");
                    return;
                }
                if (!phone.isEmpty() && phone != null) {
                    if (!phone.startsWith("1") || phone.length() != 11) {
                        Notifier.showShortMsg(FeedbackActivity.this, "请输入正确的电话号码！");
                        return;
                    }
                }
                //向服务器发送数据

                String network = null;
                int netWorkType = Utils.getNetWorkType(getApplicationContext());
                if (netWorkType == AVConstants.NETTYPE_WIFI) {
                    network = "WIFI";
                } else if (netWorkType == AVConstants.NETTYPE_2G ||
                        netWorkType == AVConstants.NETTYPE_3G ||
                        netWorkType == AVConstants.NETTYPE_4G) {
                    network = "移动网络";
                } else if (netWorkType == AVConstants.NETTYPE_NONE) {
                    network = "无网络";
                }
                HXHttpUtil.getInstance().feedback(des, phone, network)
                          .subscribe(new HttpSubscriber<Object>() {

                              @Override
                              public void onNext(Object o) {
                                  Notifier.showShortMsg(getApplication(), "您的反馈已收到，感谢支持");
                              }


                              @Override
                              public void onError(Throwable e) {
                                  super.onError(e);
                              }
                          });
                finish();
            }
        });
    }
}
