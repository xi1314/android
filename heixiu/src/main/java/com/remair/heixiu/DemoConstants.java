package com.remair.heixiu;

public interface DemoConstants {

    int DEMO_ERROR_BASE = -99999999;
    /**
     * 空指针
     */
    int DEMO_ERROR_NULL_POINTER = DEMO_ERROR_BASE + 1;
    //public static final int APPID = 1400003386;
    int APPID = 1400005880;
    String ACCOUNTTYPE = "1840";
    String LOCAL_DATA = "local_data";
    String LOACL_USERINFO = "loacl_userinfo";
    String LOCAL_ENTERED = "local_entered";//进入过的私密直播间
    int FLV = 1;
    int HLS = 2;
    int RTMP = 5;
    int REQUEST_GUANZHU = 100;//关注搜索请求码
    String GREEN_CAR = HXApp.isTest ? "18" : "24";//绿色跑车
    String FLOWER_RAIN = HXApp.isTest ? "58" : "84";//花瓣雨
    String FLOWER_A = HXApp.isTest ? "57" : "83";//玫瑰花
    String RED_CAR = HXApp.isTest ? "33" : "58";//红色跑车
    String FLY_BIG = HXApp.isTest ? "19" : "19";//飞机
    String AIRCRAFT = HXApp.isTest ? "55" : "82";//航母
    int CAROUSEL_ID = HXApp.isTest ? 42 : 14;//轮播图充值
    String WX_APPID = HXApp.getInstance().recharge_appid == 0
                      ? "wx579f9c1d84b02376"
                      : "wx56f8a533542cb7d7";
}
