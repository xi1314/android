package studio.archangel.toolkitv2.util.networking;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import studio.archangel.toolkitv2.util.Logger;

/**
 * Created by Administrator on 2015/10/15.
 */
public class AngelNetLogger {
    public void log(String url, AngelNet.Method method, HashMap<String, Object> parameters) {
        JSONObject jo = new JSONObject();
        try {
            jo.put("url", url);
            jo.put("method", method.name());
            if (parameters != null) {
                for (Map.Entry<String, Object> en : parameters.entrySet()) {
                    Object value = en.getValue();
                    if (value instanceof File) {
                        jo.put(en.getKey(), AngelNet.file_prefix + ((File) value).getName());
                    } else {
                        jo.put(en.getKey(), value);
                    }
                }
            }
            Logger.out(jo.toString(4));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
