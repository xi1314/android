package com.remair.heixiu.bean;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：wsk
 * 创建时间：16/8/29 下午9:04
 * 修改人：wsk
 * 修改时间：16/8/29 下午9:04
 * 修改备注：
 */
public class ConfigBean {

    public String iap_recharge_switch;
    public String ali_recharge_switch = "0";//支付宝充值
    public String wx_recharge_switch;
    public String recharge_switch= "2";//充值界面是否显示
    public String withdraw_switch= "1";//是否可提现;
    public String recharge_appid;
    public int switch_activity;
    public String swith_domain;
    public int enter_room_grade_limit;
    public int heart_gz_time= 0;//心跳
    public int list_fresh_time=20;//列表刷新时间
    public double money_limit;
    public int heartbeat_interval = 5;//心跳
    public int auth_control;
    public int time_out=5;//心跳
    public int live_grade_limit;
    public String ios_content;
    public String ios_force_update;
    public String current_version_max_ios;
    public String network_packet_control;
    public String switch_exchange;
    public String environment;
    public String share_url;
    public Android android;

    public class Android {
        public String force_update;
        public String apk_url;
        public String update_info;
        public String version_code;
    }
}
