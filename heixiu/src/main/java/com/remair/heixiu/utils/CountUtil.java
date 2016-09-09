package com.remair.heixiu.utils;

import android.app.Activity;
import android.content.Context;
import com.umeng.analytics.MobclickAgent;

public class CountUtil {

    public static void init(Context context) {
        //禁止默认的页面统计方式，这样将不会再自动统计Activity。
        //MobclickAgent.openActivityDurationTrack(false);
        //String channel = AnalyticsConfig.getChannel(context);
    }


    /**
     * 友盟统计用户ID
     *
     * @param id 用户ID
     */
    public static void onProfileSignIn(String id) {
        MobclickAgent.onProfileSignIn(id);
    }


    /**
     * 友盟统计用户ID
     *
     * @param provider 账号来源。如果用户通过第三方账号登陆，可以调用此接口进行统计。不能以下划线"_"开头，使用大写字母和数字标识，长度小于32
     * 字节;
     * @param id 用户ID
     */
    public static void onProfileSignIn(String provider, String id) {
        MobclickAgent.onProfileSignIn(provider, id);
    }


    /**
     * 退出登录，取消友盟统计用户功能
     */
    public static void onProfileSignOff() {
        MobclickAgent.onProfileSignOff();
    }


    public static void onResume(Activity a) {
        MobclickAgent.onResume(a);
    }


    public static void onPause(Activity a) {
        MobclickAgent.onPause(a);
    }


    /**
     * 上传异常信息到友盟和talkingData
     */
    public static void onError(Context context, Throwable e) {
        MobclickAgent.reportError(context, e);
    }


    public static void onPageStart(Context context, String name) {
        MobclickAgent.onPageStart(name);
    }


    public static void onPageEnd(Context context, String name) {
        MobclickAgent.onPageEnd(name);
    }
}
