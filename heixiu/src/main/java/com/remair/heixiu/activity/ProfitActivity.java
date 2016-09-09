package com.remair.heixiu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import com.badoo.mobile.util.WeakHandler;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.HXJavaNet;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.base.HXBaseActivity;
import com.remair.heixiu.bean.AuthStatusBean;
import com.remair.heixiu.bean.Profitincomebean;
import com.remair.heixiu.bean.UserInfo;
import com.remair.heixiu.net.HXHttpUtil;
import com.remair.heixiu.net.HttpSubscriber;
import com.remair.heixiu.utils.RxViewUtil;
import com.remair.heixiu.view.LineGraphicView;
import com.remair.heixiu.view.ShowDialog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import rx.functions.Action1;
import studio.archangel.toolkitv2.util.networking.AngelNetCallBack;
import studio.archangel.toolkitv2.util.text.Notifier;
import studio.archangel.toolkitv2.views.AngelMaterialButton;

/**
 * Created by Michael
 * profit page
 */
public class ProfitActivity extends HXBaseActivity implements PlatformActionListener {
    @BindView(R.id.act_profit_current) TextView tv_current;
    @BindView(R.id.act_profit_total) TextView tv_total;
    @BindView(R.id.act_profit_today) TextView tv_today;
    @BindView(R.id.act_profit_withdraw) AngelMaterialButton amb_withdraw;
    @BindView(R.id.ll_my_case) TextView act_profit_cash;
    @BindView(R.id.protocol) TextView protocol;
    double todayMoney;
    double monthMoney;
    long cancurrent;
    private final int requsetCode = 11111;//判断是否绑定过手机号
    private static final int MSG_AUTH_CANCEL = 2;
    private static final int MSG_AUTH_ERROR = 3;
    private static final int MSG_AUTH_COMPLETE = 4;
    ShowDialog showDialog;
    protected HXBaseActivity mActivity;
    WeakHandler handler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_AUTH_CANCEL:
                    //取消授权
                    Notifier.showShortMsg(ProfitActivity.this, "授权操作已取消!");
                    break;
                case MSG_AUTH_ERROR:
                    //授权失败
                    Notifier.showShortMsg(ProfitActivity.this, "授权操作失败!");
                    break;
                case MSG_AUTH_COMPLETE:
                    //授权成功

                    Object[] obj = (Object[]) msg.obj;
                    final Platform platform = (Platform) obj[1];
                    final String openid = platform.getDb().getUserId();

                    //绑定微信
                    HashMap<String, Object> params = new HashMap<>();
                    params.put("user_id", HXApp.getInstance()
                                               .getUserInfo().user_id);
                    params.put("openid", openid);
                    HXJavaNet
                            .post(HXJavaNet.url_bindwithdraw, params, new AngelNetCallBack() {
                                @Override
                                public void onSuccess(int ret_code, String ret_data, String msg) {

                                    UserInfo userInfo = HXApp.getInstance()
                                                             .getUserInfo();
                                    if (ret_code == 200 && userInfo != null) {
                                        userInfo.wx_withdraw_openid = openid;
                                        HXApp.getInstance()
                                             .setUserInfo(userInfo);

                                        isBind = true;
                                        Notifier.showShortMsg(ProfitActivity.this, "授权成功！");
                                    } else {
                                        Notifier.showShortMsg(getApplicationContext(), msg);
                                    }
                                }


                                @Override
                                public void onFailure(String msg) {
                                    Notifier.showShortMsg(getApplicationContext(), msg);
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
    @BindView(R.id.title_left_image) ImageButton mTitleLeftImage;
    private boolean isBind = false;


    @Override
    protected void initUI() {

        setContentView(R.layout.act_profit);
        ButterKnife.bind(this);
        mActivity = ProfitActivity.this;
        tu = (LineGraphicView) findViewById(R.id.line_graphic);
        Profitincomebean profitincomebean = getIntent()
                .getParcelableExtra("yue");
        try {
            xRawDatas = new ArrayList<>();
            yList = new ArrayList<>();
            if (profitincomebean != null &&
                    profitincomebean.week_list.size() > 0) {
                //请求成功
                List<Profitincomebean.Week_list> week_lists = profitincomebean.week_list;
                int j = 0;
                for (int i = 0; i < week_lists.size(); i++) {
                    //找最大的值
                    if (week_lists.size() >= 2) {
                        for (int k = 0; k < week_lists.size(); k++) {
                            if (week_lists.get(k).income > j) {
                                j = (int) week_lists.get(k).income;
                            }
                        }
                    } else {
                        j = (int) week_lists.get(0).income;
                    }
                    xRawDatas.add(week_lists.get(i).time.substring(5,10));
                    if (week_lists.get(i).income < 1) {
                        yList.add(1.0);
                    } else {
                        yList.add(week_lists.get(i).income);
                    }
                }
                yList.add(0, 1.0);
                xRawDatas.add(0, "");
                if (j > 10000) {
                    tu.setData(yList, xRawDatas, (j + 1000), (j + 1000) / 4);
                } else if (j > 1000) {
                    tu.setData(yList, xRawDatas, (j + 100), (j + 100) / 4);
                } else if (j > 100) {
                    tu.setData(yList, xRawDatas, (j + 10), (j + 10) / 4);
                } else if (j > 4) {
                    tu.setData(yList, xRawDatas, j + 5, (j + 5) / 4);
                } else {
                    tu.setData(yList, xRawDatas, 4, 1);
                }
            } else {
                //请求失败

                yList.add(1.0);
                xRawDatas.add(0, "");
                yList.add(1.0);
                xRawDatas.add("");
                yList.add(1.0);
                xRawDatas.add("");
                yList.add(1.0);
                xRawDatas.add("");
                yList.add(1.0);
                xRawDatas.add("");
                tu.setData(yList, xRawDatas, 4, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ShareSDK.initSDK(this);

        //是否绑定微信

        UserInfo userInfo = HXApp.getInstance().getUserInfo();
        if (userInfo == null || TextUtils.isEmpty(userInfo.wx_withdraw_openid))
        {
            isBind = false;
        } else

        {
            isBind = true;
        }

        RxViewUtil.viewBindClick(amb_withdraw, new Action1<Void>()

                {

                    @Override
                    public void call(Void aVoid) {
                        showDialog();
                    }
                }

        );

        act_profit_cash.setOnClickListener(new View.OnClickListener()

                                           {
                                               @Override
                                               public void onClick(View v) {
                                                   Intent intent = new Intent(ProfitActivity.this, Frag_caseActivity.class);
                                                   startActivity(intent);
                                               }
                                           }

        );
        protocol.setOnClickListener(new View.OnClickListener()

                                    {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(ProfitActivity.this, ProtocolActivity.class);
                                            intent.putExtra("type", 2);
                                            startActivity(intent);
                                        }
                                    }

        );
    }


    @Override
    protected void initData() {

        RxViewUtil.viewBindClick(mTitleLeftImage, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    LineGraphicView tu;
    ArrayList<Double> yList;
    ArrayList<String> xRawDatas;


    @Override
    protected void onResume() {
        super.onResume();
        requestUserInfo();
    }


    private void requestUserInfo() {

        final UserInfo userInfo = HXApp.getInstance().getUserInfo();
        if (userInfo == null) {
            return;
        }
        HXHttpUtil.getInstance().income()
                  .subscribe(new HttpSubscriber<Profitincomebean>() {
                      @Override
                      public void onNext(Profitincomebean profitincomebean) {
                          todayMoney = profitincomebean.today_money;//今日可提现金额
                          double total = profitincomebean.total_money;    //总共提现金额
                          cancurrent = profitincomebean.charm_value_valid;//可兑换的魅力值总数
                          userInfo.income_charm_value = cancurrent;
                          monthMoney = profitincomebean.month_already_total_money;//当月提现金额
                          tv_current.setText(String.format(Locale
                                  .getDefault(), "¥%.2f", userInfo.my_income));//总收益

                          tv_total.setText(String
                                  .format(Locale.getDefault(), "%.2f元", total));
                          tv_today.setText(String.format(Locale
                                  .getDefault(), "%.2f元", todayMoney));
                      }
                  });
    }


    private void showDialog() {
        UserInfo userinfo = HXApp.getInstance().getUserInfo();
        if (userinfo == null) {
            return;
        }
        HXHttpUtil.getInstance().authStatus(userinfo.user_id)
                  .subscribe(new HttpSubscriber<AuthStatusBean>() {
                      @Override
                      public void onNext(AuthStatusBean authStatusBean) {

                          int status = authStatusBean.status;
                          if (status == 0) {//审核中
                              showDialog = new ShowDialog(ProfitActivity.this, getResources()
                                      .getString(R.string.awaiting_review), getResources()
                                      .getString(R.string.see_progress), getResources()
                                      .getString(R.string.waiting));
                              showDialog.show();
                              showDialog
                                      .setClicklistener(new ShowDialog.ClickListenerInterface() {
                                          @Override
                                          public void doConfirm() {
                                              Intent intent = new Intent(mActivity, CertificationiActivity.class);
                                              startActivity(intent);
                                              showDialog.dismiss();
                                          }


                                          @Override
                                          public void doCancel() {
                                              showDialog.dismiss();
                                          }
                                      });
                          } else if (status == 1) {//审核通过
                              String phone_num = HXApp.getInstance()
                                                      .getUserInfo().phone_num;
                              boolean ispassword = HXApp.getInstance()
                                                        .getUserInfo().password;

                              if (null != phone_num &&
                                      !"".equals(phone_num)) {//已认证手机号

                                  if (ispassword) {
                                      Intent intent = new Intent(mActivity, IWantWithdrawActivity.class);
                                      intent.putExtra("todayMoney", todayMoney);
                                      intent.putExtra("monthMoney", monthMoney);
                                      startActivity(intent);
                                  } else {
                                      Intent intent = new Intent(ProfitActivity.this, BindPhoneNumActivity.class);
                                      //1绑定手机没有设置密码
                                      intent.putExtra("isbind", "3");
                                      startActivityForResult(intent, requsetCode);
                                  }
                              } else {//手机号认证
                                  Intent intent = new Intent(ProfitActivity.this, BindPhoneNumActivity.class);
                                  //0没有绑定手机
                                  intent.putExtra("isbind", "0");
                                  startActivityForResult(intent, requsetCode);
                              }
                          } else if (status == 2) {//未通过
                              showDialog = new ShowDialog(ProfitActivity.this, getResources()
                                      .getString(R.string.afresh_attestation), getResources()
                                      .getString(R.string.go_to), getResources()
                                      .getString(R.string.withhold));
                              showDialog.show();
                              showDialog
                                      .setClicklistener(new ShowDialog.ClickListenerInterface() {
                                          @Override
                                          public void doConfirm() {
                                              Intent intent = new Intent(mActivity, CertificationiActivity.class);
                                              startActivity(intent);
                                              showDialog.dismiss();
                                          }


                                          @Override
                                          public void doCancel() {
                                              showDialog.dismiss();
                                          }
                                      });
                          } else if (status == 3) {//未提交审核
                              showDialog = new ShowDialog(ProfitActivity.this, getResources()
                                      .getString(R.string.go_attestation), getResources()
                                      .getString(R.string.go_to), getResources()
                                      .getString(R.string.withhold));
                              showDialog.show();
                              showDialog
                                      .setClicklistener(new ShowDialog.ClickListenerInterface() {
                                          @Override
                                          public void doConfirm() {
                                              Intent intent = new Intent(mActivity, CertificationiActivity.class);
                                              startActivity(intent);
                                              showDialog.dismiss();
                                          }


                                          @Override
                                          public void doCancel() {
                                              showDialog.dismiss();
                                          }
                                      });
                          } else if (status == 4) {//提交过小框incomplete_message
                              showDialog = new ShowDialog(ProfitActivity.this, getResources()
                                      .getString(R.string.incomplete_message), getResources()
                                      .getString(R.string.go_perfect), getResources()
                                      .getString(R.string.withhold));
                              showDialog.show();
                              showDialog
                                      .setClicklistener(new ShowDialog.ClickListenerInterface() {
                                          @Override
                                          public void doConfirm() {
                                              Intent intent = new Intent(mActivity, CertificationiActivity.class);
                                              startActivity(intent);
                                              showDialog.dismiss();
                                          }


                                          @Override
                                          public void doCancel() {
                                              showDialog.dismiss();
                                          }
                                      });
                          }
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


    @Override
    protected void onDestroy() {
        if (null != showDialog) {
            showDialog = null;
        }
        super.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 22222) {
            if (data.getIntExtra("code", 0) == 1) {//认证手机号成功
                Intent intent = new Intent(mActivity, IWantWithdrawActivity.class);
                intent.putExtra("todayMoney", todayMoney);
                intent.putExtra("monthMoney", monthMoney);
                startActivity(intent);
            }
        }
    }
}