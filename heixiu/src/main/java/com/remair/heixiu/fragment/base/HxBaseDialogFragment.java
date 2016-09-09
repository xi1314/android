package com.remair.heixiu.fragment.base;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：Jw
 * 创建时间：16/8/31 15:29
 * 修改人：LiuJun
 * 修改时间：16/9/1 15:12
 * 修改备注：修改类名，删除不用的方法
 */
public abstract class HxBaseDialogFragment extends DialogFragment {

    private CompositeSubscription mCompositeSubscription;
    private Unbinder mUnbinder;


    public abstract View getRootView(LayoutInflater inflater, ViewGroup container);

    public abstract void initData();


    protected void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(s);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = getRootView(inflater, container);
        mUnbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (this.mCompositeSubscription != null) {
            this.mCompositeSubscription.unsubscribe();
        }
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }
}
