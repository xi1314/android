package com.remair.heixiu;

import android.text.TextUtils;
import com.remair.heixiu.bean.UserInfo;
import com.remair.heixiu.net.ApiException;
import com.remair.heixiu.net.CommonInterceptor;
import com.tencent.android.tpush.XGPushConfig;
import java.io.IOException;
import java.util.HashMap;
import okhttp3.OkHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import studio.archangel.toolkitv2.interfaces.AngelNetConfig;
import studio.archangel.toolkitv2.util.Utissuanfa;
import studio.archangel.toolkitv2.util.networking.AngelNet;
import studio.archangel.toolkitv2.util.networking.AngelNetCallBack;

/**
 * Created by Michael
 * http util for java server
 */
public class HXJavaNet extends AngelNet {
    public static final String url_newExchange = "newExchange";//从黑豆接口
    public static final String url_pushBlack = "pushBlack";//从黑名单中移除
    public static final String url_trecommendUserList = "recommendUserList";//推荐关注
    public static final String url_concernBatch = "concernBatch";//批量关注
    public static final String url_barragePay = "barragePay";//弹幕付费
    public static final String url_heartbeat = "heartbeat";

    public static final String url_liveClose = "liveClose";
    public static final String url_liveCloseInfo = "liveCloseInfo";
    public static final String url_live_like = "livePraise";
    /*    public static final String url_onlineUsersInRoom = "/onlineUsersInRoom";*/
    public static final String url_showRoom = "showRoom";
    public static final String url_hideLiveInfo = "hideLiveInfo";
    public static final String url_changePhoto = "changePhoto";
    public static final String url_onlineUsersInRoomByPage = "onlineUsersInRoomByPage";
    public static final String url_meta_gift = "giftList";
    public static final String url_sms_send = "sms";
    public static final String url_user_login = "user/login";
    public static final String url_user_sign = "tls/getTlsSig";
    public static final String url_user_edit_profile = "user/updateUserInfo";
    public static final String url_user_logout = "user/logout";
    public static final String url_live_create = "live_create";
    public static final String url_meta_oss_sign = "getStsToken";
    public static final String url_user_withdraw_record = "withdrawRecord";
    public static final String url_recharge_with_alipay = "aliRecharge";
    public static final String url_recharge_with_wechat = "v1.2.0/wxRecharge";
    public static final String url_order_pay_with_alipay = "";
    public static final String url_order_pay_with_wechat = "v1.2.0/wxRecharge";
    public static final String url_gift_send = "giveCommodity";
    public static final String url_search = "live_search";
    public static final String url_live_list_follow = "live_attention_list";
    public static final String requesturl = "image_post.php";
    public static final String rootUrl = "image_get.php";
    /***
     * new接口  wsk
     *****/
    public static final String url_friend_list = "friendList";//好友（关注人）列表
    public static final String url_concern = "concern";//关注
    public static final String url_unconcern = "unconcern";//取消关注
    public static final String url_user_infos = "getUserInfo";//获取个人数据信息
    public static final String url_forbidUser = "forbidUser";//获取个人数据信息
    public static final String url_black_list = "blacklist";//黑名单
    public static final String url_friendByPhone = "friendByPhone";//黑名单
    public static final String url_about_us = "aboutUs";//关于我们
    public static final String url_push_manage = "pushManage";//推送管理界面数据
    public static final String url_alirecharge = "aliRecharge";//支付宝充值
    public static final String url_login_sendsms = "loginSendSms";//短信验证号码
    public static final String url_push_editattention = "editAttentionPush";//编辑是否接收关注用户的直播通知
    public static final String url_push_edituser = "editUserPush";//编辑用户是否接收推送总开关
    public static final String url_sts_token = "getStsToken";//获取sts 授权信息
    public static final String url_enter_room = "enterRoom";//进入房间接口
    public static final String url_user_search = "userSearch";//搜索用户
    public static final String url_fans_List = "fansList";// 粉丝列表
    public static final String url_pull_black = "pullBlack";//拉黑
    public static final String url_share_url = "shareUrl";//分享
    public static final String url_complain = "complain";//举报
    public static final String url_reportPraise = "reportPraise";//回报点赞数
    public static final String url_exchange = "v1.2.0/exchange";//兑换钻石
    public static final String url_exchange_history = "v1.2.0/exchangeHistory";//兑换钻石纪录
    public static final String url_recharge_history = "v1.2.0/rechargeHistory";//充值纪录
    public static final String url_carousel_list = "v1.2.0/carouselList";//轮播图
    public static final String url_authInfo = "v1.2.0/authInfo";//实名认证信息
    public static final String url_authStatus = "v1.2.0/authStatus";//实名认证状态
    public static final String url_auth = "v1.2.0/auth";//实名认证
    public static final String url_firstsimpleauth = "v1.5.0/firstSimpleAuth";//初次小框认证
    public static final String url_fileid = "liveUpdateFileId";//关闭直播
    public static final String url_exchangeInfo = "v1.2.0/exchangeInfo";//兑换钻石信息
    public static final String url_gift_list_refresh_tag = "gift_list_refresh_tag";//刷新礼物列表标识
    public static final String url_give_user_gift = "giveUserGift";//私聊赠送礼物
    public static final String url_send_gift_msg = "sendGiftMsg";//赠送礼物发送消息
    //后面都是新加的
    public static final String url_live_create_room = "startLive";//创建房间
    public static final String url_live_list_hot = "liveHotList";//热门页
    public static final String url_live_last = "liveLastestList";//最新页

    public static final String url_live_attention = "liveAttentionList";// 直播关注列表
    public static final String url_randomroomnum = "randomRoomNum";//获取房间号
    public static final String url_edituser = "v2.0.2/editUser";//编辑用户信息
    //    public static final String url_edituser = "editUser";//编辑用户信息
    public static final String url_getuserinfo = "getUserInfo";//获取用户信息
    public static final String url_logout = "logout";//登出
    public static final String url_sendsms = "registSendSms";//发送注册验证码
    public static final String url_forgetsendsms = "forgetPwdSendSms";//发送忘记密码验证码
    public static final String url_regist = "regist";//注册
    public static final String url_resetPwd = "resetPwd";//重置密码
    public static final String url_loginpwd = "loginPwd";//密码登录
    public static final String url_loginCommon = "loginCommon";//判断登录接口
    public static final String url_newincome = "v1.2.0/income";//我的收益新
    public static final String url_feedback = "feedback";//反馈
    public static final String url_config = "config";//配置信息
    public static final String url_givegift = "giveGift";//赠送礼物
    public static final String url_qqlogin = "loginQq";//qq登录
    public static final String url_wxlogin = "loginWx";//微信登录
    public static final String url_wxlogin_new = "v1.2.0/loginWx";//微信登录 新
    public static final String url_sinablog = "loginSinaMicroBlog";//新浪微博登录
    public static final String url_location = "location";//新浪微博登录
    public static final String url_withdrawnew = "v1.2.0/withdraw";//微信提现 new
    public static final String url_withdrawne = "v1.5.0/withdraw";//微信提现 new 1.5
    public static final String url_bindwithdraw = "bindWxWithdrawOpenid";//绑定微信
    public static final String url_bindWxWithdrawPhone = "bindWxWithdrawPhone";//提现绑定手机号
    public static final String url_bindWithdrawSendSms = "bindWithdrawSendSms";//提现绑定验证码
    public static final String url_replayList = "recordLiveinfo";// 回放列表
    public static final String url_deleteRecordMovie = "deleteRecordMovie";// 批量删除回放视频
    public static final String url_editRecordCount = "editRecordCount";// 编辑回放视频
    public static final String url_liveEditChannelId = "liveEditChannelId";// 旁路直播
    public static final String url_grade = "grade";// 等级
    public static final String url_rechargehistory = "v1.3.0/rechargeHistory";// 钻石记录，包括现金和存在感
    public static final String url_heidouChargeHistory = "heidouChargeHistory";// 嘿豆兑换记录
    public static final String url_charmRanking = "charmRanking";// 存在感粉丝榜
    public static final String url_contributionRanking = "contributionRanking";// 存在感贡献榜
    public static final String url_shareRoom = "shareRoom";// 分享房间
    public static final String url_lookPosition = "lookPosition";// 分享房间
    //2.2
    public static final String url_editUserPassword = "editUserPassword";// 修改密码
    public static final String url_bindWxPhoneAndPsd = "bindWxPhoneAndPsd";// 绑定手机


    public static HXJavaNet getInstance() {
        return Singleton.INSTANCE;
    }


    private static class Singleton {
        private static HXJavaNet INSTANCE = new HXJavaNet();
    }


    private HXJavaNet() {
        super(new AngelNetConfig() {

                  @Override
                  public String getServerRoot() {
                      return BuildConfig.DOMAIN_NAME;
                  }


                  @Override
                  public String getNewServerRoot() {
                      return BuildConfig.DOMAIN_NAME;
                  }


                  @Override
                  public String getErrorMsg() {
                      return "网络出错了";
                  }


                  @Override
                  public String getReturnCodeName() {
                      return "code";
                  }


                  @Override
                  public String getReturnDataName() {
                      return "data";
                  }


                  @Override
                  public String getReturnMsgName() {
                      return "msg";
                  }
              }

        );
    }


    public static String getUrl(String url) {
        return getInstance().doGetUrl(url);
    }


    public static String getNewUrl(String url) {
        return getInstance().doGetNewUrl(url);
    }


    public static String getUrl(String url, int time_out) {
        return getInstance().doGetUrl(url, time_out);
    }


    public static void post(String url, AngelNetCallBack callback) {
        UserInfo userInfo = HXApp.getInstance().getUserInfo();
        if (userInfo == null) {
            callback.onFailure("未登录");
            return;
        }
        String url_b = userInfo.user_id + "qowi2kv9r5wsj18hjcas83ichsaol3hosi";
        if (url_b.equals("")) {
            url_b = "";
        } else {
            url_b = Utissuanfa.md5(url_b);
        }
        getInstance().doPost(getUrl(url), callback, url_b);
    }


    public static void post(String url, String key, Object value, AngelNetCallBack callback) {
        UserInfo userInfo = HXApp.getInstance().getUserInfo();
        if (userInfo == null) {
            callback.onFailure("未登录");
            return;
        }
        String url_b = userInfo.user_id + "qowi2kv9r5wsj18hjcas83ichsaol3hosi";
        if (url_b.equals("")) {
            url_b = "";
        } else {
            url_b = Utissuanfa.md5(url_b);
        }
        getInstance().doPost(getUrl(url), key, value, callback, url_b);
    }


    //config,获取验证码，登录，注册这些接口userid可以为0
    public static void post(String url, HashMap<String, Object> parameters, AngelNetCallBack callback) {
        UserInfo userInfo = HXApp.getInstance().getUserInfo();
        String url_b;
        if (userInfo == null) {
            url_b = "0qowi2kv9r5wsj18hjcas83ichsaol3hosi";
        } else {
            url_b = userInfo.user_id + "qowi2kv9r5wsj18hjcas83ichsaol3hosi";
        }

        if (url_b.equals("")) {
            callback.onFailure("未登录");
            return;
        } else {
            url_b = Utissuanfa.md5(url_b);
        }
        getInstance().doPost(getUrl(url), parameters, callback, url_b);
    }


    public static void postNew(String url, HashMap<String, Object> parameters, AngelNetCallBack callback) {
        UserInfo userInfo = HXApp.getInstance().getUserInfo();
        if (userInfo == null) {
            callback.onFailure("未登录");
            return;
        }
        String url_b = userInfo.user_id + "qowi2kv9r5wsj18hjcas83ichsaol3hosi";
        if (url_b.equals("")) {
            url_b = "";
        } else {
            url_b = Utissuanfa.md5(url_b);
        }
        getInstance().doPost(getNewUrl(url), parameters, callback, url_b);
    }


    public static void post(String url, int time_out, HashMap<String, Object> parameters, AngelNetCallBack callback) {
        UserInfo userInfo = HXApp.getInstance().getUserInfo();
        if (userInfo == null) {
            callback.onFailure("未登录");
            return;
        }
        String url_b = userInfo.user_id + "qowi2kv9r5wsj18hjcas83ichsaol3hosi";
        if (url_b.equals("")) {
            url_b = "";
        } else {
            url_b = Utissuanfa.md5(url_b);
        }
        getInstance()
                .doPost(getUrl(url, time_out), parameters, callback, url_b);
    }


    public static String postSync(String url) throws IOException {
        return getInstance().doPostSync(getUrl(url));
    }


    public static String postSync(String url, HashMap<String, Object> parameters) throws IOException {
        return getInstance().doPostSync(getUrl(url), parameters);
    }


    public static String postSync(String url, String key, Object value) throws IOException {
        UserInfo userInfo = HXApp.getInstance().getUserInfo();
        if (userInfo == null) {
            return "{\"code\": \"-100\",\"msg\": \"未登录\",}";
        }
        String url_b = userInfo.user_id + "qowi2kv9r5wsj18hjcas83ichsaol3hosi";
        if (url_b.equals("")) {
            url_b = "";
        } else {
            url_b = Utissuanfa.md5(url_b);
        }
        return getInstance().doPostSync(getUrl(url), key, value, url_b);
    }


    public static void postToRawUrl(String url, HashMap<String, Object> parameters, AngelNetCallBack callback) {
        UserInfo userInfo = HXApp.getInstance().getUserInfo();
        if (userInfo == null) {
            callback.onFailure("未登录");
            return;
        }
        String url_b = userInfo.user_id + "qowi2kv9r5wsj18hjcas83ichsaol3hosi";
        if (url_b.equals("")) {
            url_b = "";
        } else {
            url_b = Utissuanfa.md5(url_b);
        }
        getInstance().doPost(url, parameters, callback, url_b);
    }


    @Override
    public void doPost(String url, HashMap<String, Object> parameters, AngelNetCallBack callback, String url_b) {
        super.doPost(url, rearrangePostParams(parameters), callback, url_b);
    }


    @Override
    public String doPostSync(String url, HashMap<String, Object> parameters, String b) throws IOException {
        return super.doPostSync(url, rearrangePostParams(parameters), b);
    }


    @Override
    public String getDeviceId() {
        return XGPushConfig.getToken(HXApp.getInstance());
    }


    @Override
    public String getVersionName() {
        return BuildConfig.versionNumber;
    }


    @Override
    public String getToken() {
        UserInfo userInfo = HXApp.getInstance().getUserInfo();
        return (userInfo == null || TextUtils.isEmpty(userInfo.token))
               ? CommonInterceptor.TOKEN
               : userInfo.token;
    }


    @Override
    public int getUserId() {
        UserInfo userInfo = HXApp.getInstance().getUserInfo();
        return userInfo == null ? -1 : userInfo.user_id;
    }


    @Override
    protected boolean dataValidation(String result) {
        boolean flag = true;
        try {
            JSONObject object = new JSONObject(result);
            String code = object.optString("code");
            if (TextUtils.equals(code, ApiException.LOGIN_FAILURE)) {
                execute(new Runnable() {
                    @Override
                    public void run() {
                        HXApp.getInstance().goLogin(true, false);
                    }
                });
                flag = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }


    HashMap<String, Object> rearrangePostParams(HashMap<String, Object> parameters) {
        //        HashMap<String, Object> rearranged = new HashMap<>();
        //        JSONObject jo = new JSONObject();
        //        if (parameters != null) {
        //            for (Map.Entry<String, Object> en : parameters.entrySet()) {
        //                String key = en.getKey();
        //                Object value = en.getValue();
        //                if (value instanceof File) {
        //                    rearranged.put(key, value);
        //                    continue;
        //                }
        //                try {
        //                    jo.put(key, value);
        //                } catch (JSONException e) {
        //                    e.printStackTrace();
        //                }
        //            }
        //        }
        //        rearranged.put(api_version_key, api_version_value);
        //        rearranged.put(unique_key, jo.toString());
        //        return rearranged;
        return parameters;
    }


    public static OkHttpClient getOkhttpClient() {
        return getInstance().getClient();
    }
}
