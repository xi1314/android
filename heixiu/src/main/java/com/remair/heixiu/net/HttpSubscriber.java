package com.remair.heixiu.net;

import android.text.TextUtils;
import com.apkfuns.logutils.LogUtils;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.remair.heixiu.HXApp;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import studio.archangel.toolkitv2.util.text.Notifier;

/**
 * 项目名称：Android
 * 类描述：网络请求订阅者
 * 创建人：LiuJun
 * 创建时间：16/8/24 21:25
 * 修改人：LiuJun
 * 修改时间：16/8/24 21:25
 * 修改备注：
 */
public abstract class HttpSubscriber<T> extends Subscriber<T> {

    @Override
    public void onCompleted() {
    }
    

    @Override
    public void onError(Throwable e) {
        String msg;
        if (e instanceof HttpException) {
            msg = "网络连接出错";
        } else if (e instanceof ApiException) {
            ApiException apiException = (ApiException) e;
            if (TextUtils.equals(apiException
                    .getCode(), ApiException.LOGIN_FAILURE)) {
                HXApp.getInstance().goLogin(true, false);
                return;
            }
            msg = (apiException).getErrMsg();
        } else if (e instanceof JsonIOException ||
                e instanceof JsonParseException) {
            msg = "数据解析出错";
        } else if (e instanceof ConnectException) {
            msg = "网络连接失败";
        } else if (e instanceof SocketTimeoutException) {
            msg = "网络连接超时";
        } else {
            msg = "请求失败";
        }
        Notifier.showShortMsg(HXApp.getInstance(), msg);
        LogUtils.d(e);
    }
}
