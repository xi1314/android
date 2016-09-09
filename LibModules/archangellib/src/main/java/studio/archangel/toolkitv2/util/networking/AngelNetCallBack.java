package studio.archangel.toolkitv2.util.networking;

import org.json.JSONException;
import org.json.JSONObject;

import studio.archangel.toolkitv2.interfaces.AngelNetConfig;
import studio.archangel.toolkitv2.util.Logger;

/**
 * Created by Administrator on 2015/10/14.
 */
public abstract class AngelNetCallBack {

    public void onStart() {
    }


    public void onSuccess(String response, AngelNetConfig config) {
        Logger.out("AngelNetCallBack response:" + response);
        JSONObject jo;
        try {
            jo = new JSONObject(response);
            int status = jo.optInt(config.getReturnCodeName());
            String json = jo.optString(config.getReturnDataName());
            String readable_msg = jo.optString(config.getReturnMsgName());
            onSuccess(status, json, readable_msg);
        } catch (JSONException e) {
            Logger.out(response);
            e.printStackTrace();
            onFailure("数据格式错误");
        }
    }


    public abstract void onSuccess(int ret_code, String ret_data, String msg);

    public abstract void onFailure(String msg);
}
