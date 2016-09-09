package com.remair.heixiu.fragment.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：LiuJun
 * 创建时间：16/8/27 15:16
 * 修改人：LiuJun
 * 修改时间：16/8/27 15:16
 * 修改备注：
 */
public abstract class HXBaseFragment extends Fragment {

    private CompositeSubscription mCompositeSubscription;


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
        return getRootView(inflater, container);
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
    }


    /**
     * 删除Fragment,而且是弹出栈，彈出頂部棧Fragment
     */
    public void removeFragment() {
        getChildFragmentManager().popBackStackImmediate();
    }


    /**
     * 执行 ft.addToBackStack(null); 圧栈，返回的时候弹出栈
     */
    public void addFragmentAddStack(Fragment fragment, int addLayID) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(addLayID, fragment);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }


    /**
     * 不执行 ft.addToBackStack(null);
     */
    public void addFragment(Fragment fragment, int addLayID) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(addLayID, fragment);
        ft.commitAllowingStateLoss();
    }


    /**
     * 执行 ft.addToBackStack(null); 圧栈，返回的时候弹出栈
     */
    public void replaceFragmentAddStack(Fragment fragment, int replaceLayID) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(replaceLayID, fragment);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }


    /**
     * 不执行 ft.addToBackStack(null);
     */
    public void replaceFragment(Fragment fragment, int replaceLayID) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(replaceLayID, fragment);
        ft.commitAllowingStateLoss();
    }


    /**
     * 执行 ft.addToBackStack(null); 圧栈，返回的时候弹出栈
     */
    public void addFragmentAddStack(Fragment fragment, int addLayID, int enter, int exit, int popEnter, int popExit) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.setCustomAnimations(enter, exit, popEnter, popExit);
        ft.add(addLayID, fragment);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }


    /**
     * 不执行 ft.addToBackStack(null);
     */
    public void addFragment(Fragment fragment, int addLayID, int enter, int exit, int popEnter, int popExit) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.setCustomAnimations(enter, exit, popEnter, popExit);

        ft.add(addLayID, fragment);
        ft.commitAllowingStateLoss();
    }


    /**
     * 执行 ft.addToBackStack(null); 圧栈，返回的时候弹出栈，可以设置动画
     */
    public void replaceFragmentAddStack(Fragment fragment, int replaceLayID, int enter, int exit, int popEnter, int popExit) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.setCustomAnimations(enter, exit, popEnter, popExit);
        ft.replace(replaceLayID, fragment);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }


    /**
     * 不执行 ft.addToBackStack(null);可以设置动画
     */
    public void replaceFragment(Fragment fragment, int replaceLayID, int enter, int exit, int popEnter, int popExit) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.setCustomAnimations(enter, exit, popEnter, popExit);
        ft.replace(replaceLayID, fragment);
        ft.commitAllowingStateLoss();
    }


    public String getPageName() {
        return getClass().getSimpleName();
    }
}
