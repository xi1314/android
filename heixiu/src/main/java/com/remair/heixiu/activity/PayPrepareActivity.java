package com.remair.heixiu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.remair.heixiu.DemoConstants;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.base.HXBaseActivity;
import com.remair.heixiu.bean.UserInfo;
import com.remair.heixiu.bean.WechatReqPayBean;
import com.remair.heixiu.net.HXHttpUtil;
import com.remair.heixiu.net.HttpSubscriber;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import studio.archangel.toolkitv2.util.text.Notifier;

/**
 * Created by Michael
 * dummy page for payment
 */
public class PayPrepareActivity extends HXBaseActivity {
    //public static final String appid_wx = "wx579f9c1d84b02376";
    //public static final String appid_wx = "wx56f8a533542cb7d7";
    int recharge_amount = -1;
    int type;
    Context mContext;
    /*   WeakHandler alipay_handler = new WeakHandler(new Handler.Callback() {
           @Override
           public boolean handleMessage(Message msg) {
               AlipayResult payResult = new AlipayResult((String) msg.obj);
               String resultInfo = payResult.getResult();
               Logger.out(resultInfo);
               String resultStatus = payResult.getResultStatus();
               if (resultStatus.equals("9000")) {
                   Notifier.showShortMsg(mContext, "支付成功");
                   setResult(HXApp.result_ok);
               } else {
                   // 判断resultStatus 为非“9000”则代表可能支付失败
                   // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                   if (resultStatus.equals("8000")) {
                       Notifier.showShortMsg(mContext, "支付结果确认中");
                       setResult(HXApp.result_cancel);
                   } else {
                       // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                       Notifier.showShortMsg(mContext, "支付失败");
                       setResult(HXApp.result_fail);
                   }
               }
               finish();
               return false;
           }
       });*/
    private UserInfo mUserInfo;


    @Override
    protected void initUI() {

    }


    @Override
    protected void initData() {
        mUserInfo = HXApp.getInstance().getUserInfo();
        if (mUserInfo == null) {
            finish();
            return;
        }
        Bundle extras = getIntent().getExtras();
        mContext = getApplicationContext();
        if (extras == null) {
            finish();
            return;
        }
        type = extras.getInt("type", -1);
        if (type == -1) {
            finish();
            return;
        }
        if (type == 1) {//order
            recharge_amount = extras.getInt("recharge_amount");
            String method = extras.getString("method", null);
            if (method == null) {
                finish();
                return;
            }
            if (method.equals("alipay")) {
                //alipayForOrder();
            } else if (method.equals("wechat")) {
                //wechatPayForOrder();
            } else {
                finish();
            }
        } else if (type == 0) {//recharge
            recharge_amount = extras.getInt("recharge_amount", -1);
            if (recharge_amount == -1) {
                finish();
                return;
            }
            String method = extras.getString("method", null);
            if (method == null) {
                finish();
                return;
            }
            if (method.equals("alipay")) {
                //alipayRecharge();
            } else if (method.equals("wechat")) {
                wechatPayRecharge();
            } else {
                finish();
            }
        } else {
            finish();
        }
    }


    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        Notifier.showShortMsg(mContext, resultCode + data.toString());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //void alipayForOrder() {
    //    HashMap<String, Object> para = new HashMap<>();
    //    para.put("user_id", HXApp.instance.getCurrentUserId());
    //    para.put("order_id", order_id);
    //    HXJavaNet.post(HXJavaNet.url_order_pay_with_alipay, para, new AngelNetCallBack() {
    //        @Override
    //        public void onStart() {
    //            dialog = new LoadingDialog(getSelf(), R.style.dialog);
    //            dialog.show();
    //        }
    //
    //        @Override
    //        public void onSuccess(final int status, final String json, final String readable_msg) {
    //            Logger.out(status + " " + json + " " + readable_msg);
    //            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
    //                @Override
    //                public void onDismiss(DialogInterface dialog) {
    //                    if (status == 1001) {
    //                        try {
    //                            JSONObject jo = new JSONObject(json);
    //                            final String orderInfo = jo.optString("params"); // 订单信息
    //                            Runnable payRunnable = new Runnable() {
    //                                @Override
    //                                public void run() {
    //                                    PayTask alipay = new PayTask(getSelf());
    //                                    String result = alipay.pay(orderInfo);
    //                                    Message msg = new Message();
    //                                    msg.what = 0;
    //                                    msg.obj = result;
    //                                    alipay_handler.sendMessage(msg);
    //                                }
    //                            };
    //                            Thread payThread = new Thread(payRunnable);
    //                            payThread.start();
    //                        } catch (JSONException e) {
    //                            e.printStackTrace();
    //                        }
    //                    } else {
    //                        Notifier.showNormalMsg(mContext, readable_msg);
    //                        setResult(HXApp.result_cancel);
    //                        finish();
    //                    }
    //                }
    //            });
    //            dialog.dismiss();
    //        }
    //
    //        @Override
    //        public void onFailure(String msg) {
    //            Notifier.showNormalMsg(mContext, msg);
    //            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
    //                @Override
    //                public void onDismiss(DialogInterface dialog) {
    //                    setResult(HXApp.result_cancel);
    //                    finish();
    //                }
    //            });
    //            dialog.dismiss();
    //
    //        }
    //    });
    //
    //}


   /* void alipayRecharge() {
        HashMap<String, Object> para = new HashMap<>();
        para.put("user_id", HXApp.getInstance().getUserInfo().user_id);
        para.put("money", recharge_amount);
        para.put("device", "android");
        HXJavaNet
                .post(HXJavaNet.url_recharge_with_alipay, para, new AngelNetCallBack() {
                    @Override
                    public void onStart() {
                        dialog = new LoadingDialog(getSelf(), R.style.dialog);
                        dialog.show();
                    }


                    @Override
                    public void onSuccess(final int status, final String json, final String readable_msg) {
                        Logger.out(status + " " + json + " " + readable_msg);
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                if (status == 200) {
                                    try {
                                        JSONObject jo = new JSONObject(json);
                                        final String orderInfo = jo
                                                .optString("aliPayStr"); // 订单信息
                                        Runnable payRunnable = new Runnable() {
                                            @Override
                                            public void run() {
                                                PayTask alipay = new PayTask(getSelf());
                                                String result = alipay
                                                        .pay(orderInfo);
                                                Message msg = new Message();
                                                msg.what = 0;
                                                msg.obj = result;
                                                alipay_handler.sendMessage(msg);
                                            }
                                        };
                                        Thread payThread = new Thread(payRunnable);
                                        payThread.start();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Notifier.showNormalMsg(mContext, readable_msg);
                                    setResult(HXApp.result_cancel);
                                    finish();
                                }
                            }
                        });
                        dialog.dismiss();
                    }


                    @Override
                    public void onFailure(String msg) {
                        Notifier.showNormalMsg(mContext, msg);
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                setResult(HXApp.result_cancel);
                                finish();
                            }
                        });
                        dialog.dismiss();
                    }
                });
    }
*/


    void wechatPayRecharge() {
        final IWXAPI msgApi = WXAPIFactory.createWXAPI(mContext, null);
        msgApi.registerApp(DemoConstants.WX_APPID);

        boolean mIsWXAppInstalledAndSupported = msgApi.isWXAppInstalled() &&
                msgApi.isWXAppSupportAPI();

        if (!mIsWXAppInstalledAndSupported) {
            Notifier.showShortMsg(mContext, getString(R.string.weixin_hint));
            finish();
            return;
        }

        HXHttpUtil.getInstance().wxRecharge(mUserInfo.user_id, recharge_amount)
                  .subscribe(new HttpSubscriber<WechatReqPayBean>() {
                      @Override
                      public void onNext(final WechatReqPayBean payReq) {
                          Runnable payRunnable = new Runnable() {
                              @Override
                              public void run() {
                                  PayReq request = new PayReq();
                                  request.appId = payReq.appid;
                                  request.partnerId = payReq.partnerid;
                                  request.prepayId = payReq.prepayid;
                                  request.packageValue = payReq.pkg;
                                  request.nonceStr = payReq.noncestr;
                                  request.timeStamp = payReq.timestamp;
                                  request.sign = payReq.sign;
                                  msgApi.sendReq(request);
                              }
                          };
                          Thread payThread = new Thread(payRunnable);
                          payThread.start();
                          setResult(HXApp.result_ok);
                          finish();
                      }


                      @Override
                      public void onError(Throwable e) {
                          super.onError(e);
                          setResult(HXApp.result_cancel);
                          finish();
                      }
                  });
    }

    /*

    void wechatPayForOrder() {
        final IWXAPI msgApi = WXAPIFactory.createWXAPI(mContext, null);
        msgApi.registerApp(DemoConstants.WX_APPID);
        HashMap<String, Object> para = new HashMap<>();
        para.put("user_id", mUserInfo.user_id);
        //para.put("order_id", order_id);
        //para.put("appid", appid_wx);
        para.put("money", recharge_amount);
        HXJavaNet
                .post(HXJavaNet.url_order_pay_with_wechat, para, new AngelNetCallBack() {
                    @Override
                    public void onStart() {
                        dialog = new LoadingDialog(mActivity, R.style.dialog);
                        dialog.show();
                    }


                    @Override
                    public void onSuccess(final int status, final String json, final String readable_msg) {
                        Logger.out(status + " " + json + " " + readable_msg);
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                if (status == 200) {
                                    try {
                                        final JSONObject jo = new JSONObject(json);
                                        final String orderInfo = jo
                                                .optString("params"); // 订单信息
                                        Runnable payRunnable = new Runnable() {
                                            @Override
                                            public void run() {
                                                PayReq request = new PayReq();
                                                request.appId = jo
                                                        .optString("appid");
                                                request.partnerId = jo
                                                        .optString("partnerid");
                                                request.prepayId = jo
                                                        .optString("prepayid");
                                                request.packageValue = jo
                                                        .optString("pkg");
                                                request.nonceStr = jo
                                                        .optString("noncestr");
                                                request.timeStamp = jo
                                                        .optString("timestamp");
                                                request.sign = jo
                                                        .optString("sign");
                                                msgApi.sendReq(request);
                                            }
                                        };
                                        Thread payThread = new Thread(payRunnable);
                                        payThread.start();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Notifier.showNormalMsg(mContext, readable_msg);
                                    setResult(HXApp.result_cancel);
                                    finish();
                                }
                            }
                        });
                        dialog.dismiss();
                    }


                    @Override
                    public void onFailure(String msg) {
                        Notifier.showNormalMsg(mContext, msg);
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                setResult(HXApp.result_cancel);
                                finish();
                            }
                        });
                        dialog.dismiss();
                    }
                });
    }

    */


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, requestCode + "", Toast.LENGTH_SHORT).show();
    }
}
