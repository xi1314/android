package com.remair.heixiu.oss;

import android.text.TextUtils;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.HXJavaNet;
import com.remair.heixiu.bean.UserInfo;
import org.json.JSONException;
import org.json.JSONObject;
import studio.archangel.toolkitv2.util.Logger;
import studio.archangel.toolkitv2.util.text.Notifier;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：LiuJun
 * 创建时间：16/8/10 19:02
 * 修改人：LiuJun
 * 修改时间：16/8/10 19:02
 * 修改备注：
 */
public class HXOSSFCProvider extends OSSFederationCredentialProvider {

    private static class SingletonHolder {
        private static final HXOSSFCProvider INSTANCE = new HXOSSFCProvider();
    }


    public static synchronized HXOSSFCProvider getInstance() {
        return SingletonHolder.INSTANCE;
    }


    private HXOSSFCProvider() {
    }


    @Override
    public OSSFederationToken getFederationToken() {
        UserInfo userInfo = HXApp.getInstance().getUserInfo();
        if (userInfo == null) {
            return null;
        }
        OSSFederationToken token = null;
        try {
            String result = HXJavaNet
                    .postSync(HXJavaNet.url_meta_oss_sign, "user_id", userInfo.user_id);
            if (TextUtils.isEmpty(result)) {
                return null;
            }
            JSONObject jo = new JSONObject(result);
            int status = jo.optInt("code");
            String json = jo.optString("data");
            String readable_msg = jo.optString("msg");
            Logger.out(status + " " + json + " " + readable_msg);
            if (status == 200) {
                try {
                    jo = new JSONObject(json);
                    token = new OSSFederationToken(jo.optString("ak"), jo
                            .optString("as"), jo.optString("token"), jo
                            .optLong("exp"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Notifier.showNormalMsg(HXApp.getInstance(), readable_msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }
}
