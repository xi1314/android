package com.remair.heixiu.bean;

import org.json.JSONException;
import org.json.JSONObject;
import studio.archangel.toolkitv2.interfaces.JSONizable;
import studio.archangel.toolkitv2.util.text.AmountProvider;

/**
 * Created by Michael
 * record of user's withdrawing
 */
public class WithdrawRecord implements JSONizable {
    public String title, date, status;
    public double amount = 0;


    public WithdrawRecord(JSONObject jo, int type) {
        if (jo == null) {
            return;
        }
        if (type == 0) {
            amount = jo.optDouble("money");
            title = "提现:" +
                    AmountProvider.getCurrencyString((float) amount, 2) + "元";
            date = jo.optString("create_time");
            status = jo.optString("status_desc");
        }
        if (type == 1) {
            title = "兑换:" + jo.optInt("virtual_currency_amount") + "钻石";
            date = jo.optString("time");
            int state = jo.optInt("status");
            if (state == 0) {
                status = "处理中";
            } else {
                status = "成功";
            }
        }
    }


    @Override
    public JSONObject toJson() {
        JSONObject jo = new JSONObject();
        try {
            jo.put("money", amount);
            jo.put("create_time", date);
            jo.put("status_desc", status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo;
    }
}
