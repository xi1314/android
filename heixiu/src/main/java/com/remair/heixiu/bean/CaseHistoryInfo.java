package com.remair.heixiu.bean;

/**
 * Created by wsk on 16/4/7.
 */
public class CaseHistoryInfo {
    /**
     * "virtual_currency_amount": 100,//钻石数目
     * "time": "2016-04-05 20:48:50",//兑换时间
     * "charm_value": 260,//兑换所需魅力值
     * "status": 1,//状态,0:处理中,1:成功(基本都是实时成功)
     * "user_id": 485 //兑换人的id
     */
    public int user_id;//兑换人的id
    public int status;//状态,0:处理中,1:成功(基本都是实时成功)
    public int charm_value;//兑换所需魅力值
    public String time;//兑换时间
    public int coin;//钻石数目
    public int heidou;
}
