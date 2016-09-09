package studio.archangel.toolkitv2.models;

import org.json.JSONException;
import org.json.JSONObject;

import studio.archangel.toolkitv2.AngelApplication;

/**
 * 异常构建器
 * Created by Michael on 2015/2/26.
 */
public class AngelExceptionBuilder {
    public JSONObject jo;

    /**
     * 构建异常，将设备信息和应用版本号装入json
     *
     * @param object 原json
     */
    public AngelExceptionBuilder(JSONObject object) {
        jo = object;
        try {
            jo.put("device", AngelApplication.device_des);
            jo.put("version", AngelApplication.app_version_name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 省事的调用方法，没有前缀。
     *
     * @return
     */
    public AngelException build() {
        return build("");
    }

    /**
     * 构建异常。json的内容将作为异常的toString方法的返回值
     *
     * @param prefix 前缀。将加在在json信息之前
     * @return
     */
    public AngelException build(String prefix) {
        return new AngelException(prefix + jo.toString());
    }
}
