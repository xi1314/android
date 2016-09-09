package com.remair.heixiu.net;

/**
 * 项目名称：Android
 * 类描述：
 * 一个空的网络请求订阅者
 * 只需要调用接口，而不需要关心接口返回数据时可以使用
 *
 * 创建人：LiuJun
 * 创建时间：16/8/25 15:52
 * 修改人：LiuJun
 * 修改时间：16/8/25 15:52
 * 修改备注：
 */
public class EmptySubscriber extends HttpSubscriber {

    boolean isShowErr;//是否显示错误信息


    public EmptySubscriber() {
        this(false);//默认不显示错误信息
    }


    public EmptySubscriber(boolean isShowErr) {
        this.isShowErr = isShowErr;
    }


    @Override
    public void onError(Throwable e) {
        if (isShowErr) {
            super.onError(e);
        }
    }


    @Override
    public void onNext(Object o) {

    }
}
