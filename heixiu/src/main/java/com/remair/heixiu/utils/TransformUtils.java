package com.remair.heixiu.utils;

import com.remair.heixiu.bean.HttpResult;
import com.remair.heixiu.net.ApiException;
import com.remair.heixiu.net.HttpResultFunc;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 项目名称：Android
 * 类描述：Transformer转换器工具类
 * 创建人：LiuJun
 * 创建时间：16/8/25 15:20
 * 修改人：LiuJun
 * 修改时间：16/8/25 15:20
 * 修改备注：
 */
public class TransformUtils {

    /**
     * 调用接口时校验服务器返回code是否等于200
     * 不等于200时抛出{@link ApiException}异常
     */
    public static <T> Observable.Transformer<HttpResult<T>, T> dataValidation() {
        return new Observable.Transformer<HttpResult<T>, T>() {
            @Override
            public Observable<T> call(Observable<HttpResult<T>> httpResultObservable) {
                return httpResultObservable.map(new HttpResultFunc<T>());
            }
        };
    }


    /**
     * 默认线程设置，网络请求在IO线程，数据返回在主线程
     */
    public static <T> Observable.Transformer<T, T> defaultSchedulers() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
            }
        };
    }


    /**
     * 设置网络请求和数据返回都在IO线程
     */
    public static <T> Observable.Transformer<T, T> all_io() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable.observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io());
            }
        };
    }
}
