package studio.archangel.toolkitv2.util.networking;

import android.os.Looper;
import android.text.TextUtils;
import com.badoo.mobile.util.WeakHandler;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;
import studio.archangel.toolkitv2.interfaces.AngelNetConfig;
import studio.archangel.toolkitv2.interfaces.AngelNetProgressCallBack;
import studio.archangel.toolkitv2.util.Logger;
import studio.archangel.toolkitv2.util.Util;
import studio.archangel.toolkitv2.util.Utissuanfa;

/**
 * Created by Administrator on 2015/10/14.
 */
public abstract class AngelNet implements Executor {
    //    static final int CALLBACK_START = 1000;
    //    static final int CALLBACK_SUCCESSFUL = 1001;
    //    static final int CALLBACK_FAILED = 1002;
    //    static final int CALLBACK_UPDATE = 1003;

    public enum Method {
        GET, POST
    }

    int time_out = 5;
    //    static AngelNet instance;
    public static final String file_prefix = "[file]";
    OkHttpClient client;
    AngelNetLogger logger;
    public AngelNetConfig config;
    boolean use_raw_format = false;


    public OkHttpClient getClient() {
        if (client == null) {
            client = new OkHttpClient.Builder()
                    .connectTimeout(time_out * 1000, TimeUnit.MILLISECONDS)
                    .build();
        }
        return client;
    }


    WeakHandler handler = null;


    @Override
    public void execute(Runnable r) {
        handler.post(r);
    }


    public AngelNet(AngelNetConfig c) {
        client = new OkHttpClient();

        logger = new AngelNetLogger();
        config = c;

        handler = new WeakHandler(Looper.getMainLooper());
    }


    public void setLogger(AngelNetLogger logger) {
        this.logger = logger;
    }


    public String doGetUrl(String url) {
        return config.getServerRoot() + url;
    }


    public String doGetNewUrl(String url) {
        return config.getNewServerRoot() + url;
    }


    public String doGetUrl(String url, int time_out) {
        this.time_out = time_out;
        return config.getNewServerRoot() + url;
    }


    public void doGet(String url, final AngelNetCallBack callback, String b) {
        doGet(url, null, callback, b);
    }


    public void doPost(String url, final AngelNetCallBack callback, String b) {
        doPost(url, null, callback, b);
    }


    public void doGet(String url, String key, Object value, final AngelNetCallBack callback, String b) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put(key, value);
        doGet(url, parameters, callback, b);
    }


    public void doPost(String url, String key, Object value, final AngelNetCallBack callback, String b) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put(key, value);
        doPost(url, parameters, callback, b);
    }


    public void doGet(String url, HashMap<String, Object> parameters, final AngelNetCallBack callback, String b) {
        call(Method.GET, url, parameters, callback, b);
    }


    public void doPost(String url, HashMap<String, Object> parameters, final AngelNetCallBack callback, String b) {
        call(Method.POST, url, parameters, callback, b);
    }


    public String doGetSync(String url) throws IOException {
        return doGetSync(url, null);
    }


    public String doPostSync(String url) throws IOException {
        return doPostSync(url, null);
    }


    public String doGetSync(String url, String key, Object value) throws IOException {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put(key, value);
        return doGetSync(url, parameters);
    }


    public String doPostSync(String url, String key, Object value) throws IOException {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put(key, value);
        return doPostSync(url, parameters);
    }


    public String doPostSync(String url, String key, Object value, String b) throws IOException {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put(key, value);
        return doPostSync(url, parameters, b);
    }


    public String doGetSync(String url, HashMap<String, Object> parameters) throws IOException {
        return callSync(Method.GET, url, parameters);
    }


    public String doPostSync(String url, HashMap<String, Object> parameters) throws IOException {
        return callSync(Method.POST, url, parameters);
    }


    public String doPostSync(String url, HashMap<String, Object> parameters, String b) throws IOException {
        return callSync(Method.POST, url, parameters, b);
    }


    private Request buildRequest(final Method method, final String url, final HashMap<String, Object> parameters, final AngelNetCallBack callback) {
        HttpUrl.Builder url_builder = new HttpUrl.Builder();
        Request.Builder requestBuilder = new Request.Builder();
        if (method == Method.GET) {
            if (parameters != null) {
                for (Map.Entry<String, Object> en : parameters.entrySet()) {
                    Object value = en.getValue();
                    if (value == null) {
                        continue;
                    }
                    if (value instanceof File) {
                        throw new IllegalArgumentException("You can not use GET method to upload file.");
                    }
                    url_builder
                            .addQueryParameter(en.getKey(), value.toString());
                }
            }

            requestBuilder.url(url_builder.build().url());
        } else if (method == Method.POST) {
            MultipartBody.Builder multi_builder = new MultipartBody.Builder();
            RequestBody body;
            if (parameters != null) {
                if (use_raw_format) {
                    JSONObject jo = new JSONObject();
                    for (Map.Entry<String, Object> en : parameters.entrySet()) {
                        Object value = en.getValue();
                        if (value == null) {
                            continue;
                        }
                        if (value instanceof File) {

                            Logger.out("skipped file parameter.");
                        } else {
                            try {
                                jo.put(en.getKey(), en.getValue().toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    body = RequestBody
                            .create(MediaType.parse("text; charset=utf-8"), jo
                                    .toString());
                } else {
                    for (Map.Entry<String, Object> en : parameters.entrySet()) {
                        Object value = en.getValue();
                        if (value == null) {
                            continue;
                        }
                        if (value instanceof File) {
                            multi_builder.addFormDataPart("file", ((File) value)
                                    .getName(), new AngelNetProgressRequestBody((File) value, "text/x-markdown; charset=utf-8", (AngelNetProgressCallBack) callback, this));
                        } else {
                            multi_builder.addFormDataPart(en.getKey(), value
                                    .toString());
                        }
                    }
                    body = multi_builder.build();
                }
            } else {
                body = RequestBody
                        .create(MediaType.parse("text; charset=utf-8"), "");
            }
            requestBuilder.url(url).post(body);
        }
        requestBuilder.header("Common-Version", getVersionName())
                      .header("Common-Device", android.os.Build.MODEL)
                      .header("Common-Op", "android")
                      .header("Common-Remair", bb.toLowerCase())
                      .header("Common-DeviceId", getDeviceId())
                      .header("Common-Platform", String.valueOf(0)).build();
        return requestBuilder.build();
    }


    public void setUseRawFormat(boolean use_raw_format) {
        this.use_raw_format = use_raw_format;
    }


    private String callSync(final Method method, final String url, final HashMap<String, Object> parameters) throws IOException {
        Request request = buildRequest(method, url, parameters, null);
        Response response = getClient().newCall(request).execute();
        ResponseBody body = response.body();
        if (body == null) {
            return "";
        }
        String s = body.string();
        if (!TextUtils.isEmpty(s) && !dataValidation(s)) {
            return "";
        }
        return s;
    }


    private String callSync(final Method method, final String url, final HashMap<String, Object> parameters, String b) throws IOException {
        if (b.equals("")) {
            bb = Utissuanfa.md5("3681849ec90940187fd66eacb567dfed");
        } else {
            this.bb = b;
        }
        String ss = UUID.randomUUID().toString().substring(0, 8);
        String sss = new StringBuilder(ss).reverse().toString();

        if (parameters != null) {
            String bbb = bb.toLowerCase() + ss.toLowerCase();
            Utissuanfa.md5(bbb);
            parameters.put("session_user_id", getUserId());
            parameters.put("token", getToken());
            parameters.put("timestamp", sss);
            parameters.put("sign",
                    UUID.randomUUID().toString().substring(0, 7).toUpperCase() +
                            Utissuanfa.md5(bbb) +
                            UUID.randomUUID().toString().substring(0, 3)
                                .toUpperCase());
        }
        Request request = buildRequest(method, url, parameters, null);
        Response response = getClient().newCall(request).execute();
        ResponseBody body = response.body();
        if (body == null) {
            return "";
        }
        String s = body.string();
        if (!TextUtils.isEmpty(s) && !dataValidation(s)) {
            return "";
        }
        return s;
    }


    private int i = 0;
    private String bb;


    private void call(final Method method, final String url, final HashMap<String, Object> parameters, final AngelNetCallBack callback, String b) {
        if (b.equals("")) {
            bb = Utissuanfa.md5("3681849ec90940187fd66eacb567dfed");
        } else {
            this.bb = b;
        }

        if (!Util.isNetWorkAvailable()) {
            if (callback != null) {
                execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure("无网络连接");
                    }
                });
            }
            return;
        }
        if (callback != null) {
            execute(new Runnable() {
                @Override
                public void run() {
                    callback.onStart();
                }
            });
            //            callback.onStart();
        }
        String ss = UUID.randomUUID().toString().substring(0, 8);
        String sss = new StringBuilder(ss).reverse().toString();

        if (parameters != null) {
            String bbb = bb.toLowerCase() + ss.toLowerCase();
            Utissuanfa.md5(bbb);
            parameters.put("session_user_id", getUserId());
            parameters.put("token", getToken());
            parameters.put("timestamp", sss);
            parameters.put("sign",
                    UUID.randomUUID().toString().substring(0, 7).toUpperCase() +
                            Utissuanfa.md5(bbb) +
                            UUID.randomUUID().toString().substring(0, 3)
                                .toUpperCase());
        }
        Request request = buildRequest(method, url, parameters, callback);
        getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                i++;
                if (i != 1) {
                    return;
                } else {
                    call(method, url, parameters, callback, bb);
                }

                logger.log(url, method, parameters);
                e.printStackTrace();
                if (callback != null) {
                    execute(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFailure(config.getErrorMsg());
                        }
                    });
                    //                    callback.onFailure(config.getErrorMsg());
                }
                if (e.toString().contains("ENETUNREACH") ||
                        e.toString().contains("ECONNREFUSED")) {
                    Logger.out("okhttp client reset!");
                    client = null;
                }
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {
                logger.log(url, method, parameters);
                ResponseBody body = response.body();
                final String s;
                if (body == null) {
                    s = "";
                } else {
                    s = body.string();
                }

                i = 0;

                if (!TextUtils.isEmpty(s) && !dataValidation(s)) {
                    return;
                }

                if (callback != null) {
                    execute(new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess(s, config);
                        }
                    });
                }
            }
        });
    }


    public abstract String getDeviceId();

    public abstract String getVersionName();

    public abstract String getToken();

    public abstract int getUserId();

    protected abstract boolean dataValidation(String result);
}
