package com.remair.heixiu.net;

import android.text.TextUtils;
import com.remair.heixiu.BuildConfig;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.bean.UserInfo;
import com.tencent.android.tpush.XGPushConfig;
import java.io.IOException;
import java.util.UUID;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import studio.archangel.toolkitv2.util.Utissuanfa;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：LiuJun
 * 创建时间：16/8/24 17:39
 * 修改人：LiuJun
 * 修改时间：16/8/24 17:39
 * 修改备注：
 */
public class CommonInterceptor implements Interceptor {

    private static final String SECRET_KEY = "qowi2kv9r5wsj18hjcas83ichsaol3hosi";

    public static final String TOKEN = "3df88675-d88b-4bfa-81b7-2fe1afe133ee";


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        request = getRequest(request);
        return chain.proceed(request);
    }


    private Request getRequest(Request request) {
        Request.Builder builder = request.newBuilder();
        UserInfo userInfo = HXApp.getInstance().getUserInfo();
        String remair = Utissuanfa.md5(userInfo == null
                                       ? "0" + SECRET_KEY
                                       : userInfo.user_id + SECRET_KEY);
        String timestamp = String.valueOf(System.currentTimeMillis());
        String reverseTime = new StringBuilder(timestamp).reverse().toString();
        String before = UUID.randomUUID().toString().substring(0, 7);
        String middle = Utissuanfa.md5(remair + timestamp);
        String after = UUID.randomUUID().toString().substring(0, 3);
        String sign = before + middle + after;
        String token = (userInfo == null || TextUtils.isEmpty(userInfo.token))
                       ? TOKEN
                       : userInfo.token;
        builder.addHeader("Common-Version", getVersionName())
               .addHeader("Common-Device", android.os.Build.MODEL)
               .addHeader("Common-Op", "android")
               .addHeader("Common-Remair", remair)
               .addHeader("Common-DeviceId", getDeviceId())
               .addHeader("Common-Platform", String.valueOf(0)).build();

        // process post body inject
        if (canInjectIntoBody(request)) {
            FormBody.Builder formBodyBuilder = new FormBody.Builder();
            formBodyBuilder.add("timestamp", reverseTime);
            formBodyBuilder.add("sign", sign);
            formBodyBuilder.add("token", token);
            String postBodyString = bodyToString(request.body());
            boolean empty = TextUtils.isEmpty(postBodyString);
            formBodyBuilder.add("session_user_id",
                    userInfo == null ? "-1" : String.valueOf(userInfo.user_id));
            RequestBody formBody = formBodyBuilder.build();
            StringBuilder strBuilder = new StringBuilder(postBodyString);
            if (!empty) {
                strBuilder.append("&");
            }
            strBuilder.append(bodyToString(formBody));
            RequestBody body = RequestBody.create(MediaType
                    .parse("application/x-www-form-urlencoded;charset=UTF-8"), strBuilder
                    .toString());
            builder.post(body);
        } else {
            // can't inject into body, then inject into url
            injectParamsIntoUrl(request, builder, reverseTime, sign);
        }
        request = builder.build();
        return request;
    }


    private boolean canInjectIntoBody(Request request) {
        if (request == null) {
            return false;
        }
        if (!TextUtils.equals(request.method(), "POST")) {
            return false;
        }
        RequestBody body = request.body();
        if (body == null) {
            return false;
        }
        return true;
        //MediaType mediaType = body.contentType();
        //return mediaType != null &&
        //        TextUtils.equals(mediaType.subtype(), "x-www-form-urlencoded");
    }


    // func to inject params into url
    private void injectParamsIntoUrl(Request request, Request.Builder requestBuilder, String timestamp, String sign) {
        HttpUrl.Builder httpUrlBuilder = request.url().newBuilder();
        httpUrlBuilder.addQueryParameter("timestamp", timestamp);
        httpUrlBuilder.addQueryParameter("sign", sign);
        requestBuilder.url(httpUrlBuilder.build());
    }


    private static String bodyToString(final RequestBody request) {
        try {
            final Buffer buffer = new Buffer();
            if (request != null) {
                request.writeTo(buffer);
            } else {
                return "";
            }
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }


    public String getVersionName() {
        return BuildConfig.versionNumber;
    }


    public String getDeviceId() {
        return XGPushConfig.getToken(HXApp.getInstance());
    }
}
