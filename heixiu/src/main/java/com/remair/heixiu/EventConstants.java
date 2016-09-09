package com.remair.heixiu;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：LiuJun
 * 创建时间：16/8/12 12:00
 * 修改人：LiuJun
 * 修改时间：16/8/12 12:00
 * 修改备注：
 */
public interface EventConstants {
    String PREFIX = BuildConfig.APPLICATION_ID;
    String ACTIVITY_LOGOUT = PREFIX + "all_activity_logout";
    String UPDATE_HEIDOU_OR_MONEY = PREFIX +
            "update_heidou_or_money";//我的嘿豆、钻石界面进行兑换后，更新嘿豆、钻石数量
    String UPDATE_EXIT_LOGING = PREFIX + "UPDATE_exit_loging";//同一账号多台设备登陆
    String UPDATE_XGPUSHMANAGER = PREFIX + "XGPushManager";//信鸽推送的在房间提示
    String ISONFORCEOFFLINE = PREFIX + "isonForceOffline";//信鸽推送的在房间提示
    String BLACKISTV_ATT_GZCHECK = PREFIX + "blackistv_att_gzcheck";//黑名单删除
    String BLACKISITEM_CHECK = PREFIX + "blackisitem_check";//黑名单删除
    String LOGIN_EXPIRE = PREFIX + "login_expire";//登录过期，重新登录
    String DIANMONDVOIDCHECK = PREFIX + "dianmondvoidcheck";//钻石兑换点击事件


}
