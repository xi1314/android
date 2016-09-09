package com.remair.heixiu.net;

import android.text.TextUtils;
import com.remair.heixiu.bean.HttpResult;
import rx.functions.Func1;

/**
 * 项目名称：Android
 * 类描述：将网络请求返回的数据判断code是否正常，正常则返回data实体
 * 创建人：LiuJun
 * 创建时间：16/8/25 11:51
 * 修改人：LiuJun
 * 修改时间：16/8/25 11:51
 * 修改备注：
 */
public class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {

    @Override
    public T call(HttpResult<T> tHttpResult) {
        if (!TextUtils.equals(tHttpResult.code, ApiException.TRUE)) {
            throw new ApiException(tHttpResult.code, tHttpResult.msg);
        }
        return tHttpResult.data;
    }
}
