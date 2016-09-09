package studio.archangel.toolkitv2.util;

//import android.content.Context;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import com.lidroid.xutils.HttpUtils;
//import com.lidroid.xutils.http.RequestParams;
//import com.lidroid.xutils.http.client.HttpRequest;
//import org.apache.http.client.CookieStore;
//import org.apache.http.conn.scheme.Scheme;
//import org.apache.http.conn.ssl.SSLSocketFactory;
//import org.apache.http.cookie.Cookie;
//import org.apache.http.impl.client.AbstractHttpClient;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import studio.archangel.toolkitv2.interfaces.NetProvider;
//import studio.archangel.toolkitv2.models.AngelExceptionBuilder;
//import studio.archangel.toolkitv2.util.text.Notifier;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.security.*;
//import java.security.cert.CertificateException;
//import java.util.HashMap;
//import java.util.Map;

/**
 * Created by Michael on 2015/4/3.
 */
public class Net {
//    public static HttpUtils client;
//
//    protected static ConnectivityManager manager;
//    protected static NetProvider provider;
//    protected static String cookie_value;
//
//    public static void setProvider(NetProvider p) {
//        provider = p;
//    }
//    public static String getUrl(String url) {
//        return provider.getServerRoot() + url;
//    }
//
//    public static String getUrlWithoutRoot(String url) {
//        return url.replace(provider.getServerRoot(), "");
//    }
//
//    public static HttpUtils getClient(Context c) {
//        if (client == null) {
//
//            client = new HttpUtils();
//        }
//        String cookie_name = provider.getCookieField();
//        if (cookie_name != null && !cookie_name.isEmpty() && cookie_value == null) {
//            CookieStore cs = ((AbstractHttpClient) client.getHttpClient()).getCookieStore();
//            for (Cookie cookie : cs.getCookies()) {
//                if (cookie.getName().equals(cookie_name)) {
//                    cookie_value = cookie.getValue();
//                    break;
//                }
//            }
//        }
//        if (getConnectionState(c) == 1) {
//            client.configTimeout(10000);
//        } else if (getConnectionState(c) == 0) {
//            client.configTimeout(30000);
//        } else if (getConnectionState(c) == -1) {
//            client.configTimeout(1);
//        }
//        return client;
//    }
//
//
//    /**
//     * 获取网络环境状态
//     *
//     * @return 1：Wifi，0：2G网 -1：未知
//     */
//    public static int getConnectionState(Context c) {
//        if (manager == null) {
//            manager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
//        }
//        NetworkInfo activeNetInfo = null;
//        try {
//            activeNetInfo = manager.getActiveNetworkInfo();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            if (activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
//                return 1;
//            } else if (activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
//                return 0;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return -1;
//    }
//
//
//    public static void handleErrorCode(Context c, String ret, JSONObject request_param, HashMap<String, String> extra_data) {
//        Notifier.showNormalMsg(c, provider.getErrorMsg());
//        JSONArray names = request_param.names();
//        if (names != null) {
//            for (int i = 0; i < names.length(); i++) {
//                String name = names.optString(i);
//                if (name.contains("pass")) {
//                    request_param.remove(name);
//                }
//            }
//        }
//        AngelExceptionBuilder builder = new AngelExceptionBuilder(request_param);
//        try {
//            builder.jo.put("return", ret);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        if (extra_data != null) {
//            for (Map.Entry<String, String> en : extra_data.entrySet()) {
//                try {
//                    builder.jo.put(en.getKey(), en.getValue());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        provider.reportError(c, builder.build("用户请求出错："));
//    }
//
//    public static RequestParams getParam() {
//        RequestParams p = new RequestParams("utf-8");
//        setCookie(p);
//        return p;
//    }
//
//    public static RequestParams getParam(HttpRequest.HttpMethod method, String key, String value) {
////        Logger.out("request parameters:" + key + "->" + value);
//        RequestParams p = new RequestParams("utf-8");
//        setCookie(p);
//        if (method == HttpRequest.HttpMethod.GET) {
//            p.addQueryStringParameter(key, value);
//        } else if (method == HttpRequest.HttpMethod.POST) {
//            if (value != null && value.startsWith("[!file]")) {
//                p.addBodyParameter(key, new File(value.replace("[!file]", "")));
//            } else {
//                p.addBodyParameter(key, value);
//            }
//        }
//
//        return p;
//    }
//
//    public static RequestParams getParam(HttpRequest.HttpMethod method, Map<String, String> map) {
////        Logger.out("request parameters:" + map);
//        RequestParams p = new RequestParams("utf-8");
//        setCookie(p);
//        for (Map.Entry<String, String> en : map.entrySet()) {
//            if (method == HttpRequest.HttpMethod.GET) {
//                p.addQueryStringParameter(en.getKey(), en.getValue());
//            } else if (method == HttpRequest.HttpMethod.POST) {
//                String value = en.getValue();
//                if (value != null && value.startsWith("[!file]")) {
//                    p.addBodyParameter(en.getKey(), new File(value.replace("[!file]", "")));
//                } else {
//                    p.addBodyParameter(en.getKey(), value);
//                }
//
//            }
//
//        }
//
//        return p;
//    }
//
//    public static void setCookie(RequestParams p) {
//        if (cookie_value != null) {
//            p.addHeader(provider.getCookieName(), cookie_value);
//        }
////        p.addHeader("Accept", "application/vnd.bangyang.v3+json");
//    }
//
//    public static void setGlobalCookieManually(String value) {
//        cookie_value = value;
//    }
}
