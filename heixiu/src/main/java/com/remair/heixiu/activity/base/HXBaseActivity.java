package com.remair.heixiu.activity.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import com.remair.heixiu.EventConstants;
import com.remair.heixiu.R;
import com.remair.heixiu.utils.CountUtil;
import com.remair.heixiu.utils.StatusBarUtil;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：LiuJun
 * 创建时间：16/8/24 10:50
 * 修改人：LiuJun
 * 修改时间：16/8/24 10:50
 * 修改备注：
 */
public abstract class HXBaseActivity extends AppCompatActivity {

    private CompositeSubscription mCompositeSubscription;

    protected HXBaseActivity mActivity;


    protected abstract void initUI();

    protected abstract void initData();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        initUI();
        initData();
        initStatusBar();
        EventBus.getDefault().registerSticky(this);
    }


    protected void initStatusBar() {
        StatusBarUtil.setColor(this, ContextCompat
                .getColor(this, R.color.hx_main), 0);
    }


    @Override
    protected void onResume() {
        super.onResume();
        CountUtil.onResume(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        CountUtil.onPause(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (this.mCompositeSubscription != null) {
            this.mCompositeSubscription.unsubscribe();
        }
    }


    protected void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(s);
    }


    @Subscriber(tag = EventConstants.ACTIVITY_LOGOUT)
    protected void logout(String b) {
        finish();
    }


    /**
     * 删除Fragment,但是并不出栈
     */
    public void removeFragment(Fragment fragment) {
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager()
                .beginTransaction();
        ft.remove(fragment);
        ft.commitAllowingStateLoss();
    }


    /**
     * 删除Fragment,而且是弹出栈，彈出頂部棧Fragment
     */
    public void removeFragment() {
        getSupportFragmentManager().popBackStackImmediate();
    }


    /**
     * 执行 ft.addToBackStack(null); 圧栈，返回的时候弹出栈
     */
    public void addFragmentAddStack(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(fragmentRoot(), fragment);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }


    /**
     * 执行 ft.addToBackStack(null); 圧栈，返回的时候弹出栈
     */
    public void addFragmentAddStack(Fragment fragment, int addLayID) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(addLayID, fragment);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }


    /**
     * 不执行 ft.addToBackStack(null);
     */
    public void addFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(fragmentRoot(), fragment);
        ft.commitAllowingStateLoss();
    }


    /**
     * 不执行 ft.addToBackStack(null);
     */
    public void addFragment(Fragment fragment, int addLayID) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(addLayID, fragment);
        ft.commitAllowingStateLoss();
    }


    /**
     * 执行 ft.addToBackStack(null); 圧栈，返回的时候弹出栈
     */
    public void replaceFragmentAddStack(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(fragmentRoot(), fragment);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }


    /**
     * 执行 ft.addToBackStack(null); 圧栈，返回的时候弹出栈
     */
    public void replaceFragmentAddStack(Fragment fragment, String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(fragmentRoot(), fragment, tag);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }


    /**
     * 执行 ft.addToBackStack(null); 圧栈，返回的时候弹出栈
     */
    public void replaceFragmentAddStack(Fragment fragment, int replaceLayID) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(replaceLayID, fragment);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }


    /**
     * 不执行 ft.addToBackStack(null);
     */
    public void replaceFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(fragmentRoot(), fragment);
        ft.commitAllowingStateLoss();
    }


    /**
     * 不执行 ft.addToBackStack(null);
     */
    public void replaceFragment(Fragment fragment, int replaceLayID) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(replaceLayID, fragment);
        ft.commitAllowingStateLoss();
    }

    /*********************************************************** 动画区域 Start **************************/
    /**
     * 执行 ft.addToBackStack(null); 圧栈，返回的时候弹出栈
     */
    public void addFragmentAddStack(Fragment fragment, int enter, int exit, int popEnter, int popExit) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(enter, exit, popEnter, popExit);
        ft.add(fragmentRoot(), fragment);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }


    /**
     * 执行 ft.addToBackStack(null); 圧栈，返回的时候弹出栈
     */
    public void addFragmentAddStack(Fragment fragment, int addLayID, int enter, int exit, int popEnter, int popExit) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(enter, exit, popEnter, popExit);
        ft.add(addLayID, fragment);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }


    /**
     * 不执行 ft.addToBackStack(null);
     */
    public void addFragment(Fragment fragment, int enter, int exit, int popEnter, int popExit) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(enter, exit, popEnter, popExit);
        ft.add(fragmentRoot(), fragment);
        ft.commitAllowingStateLoss();
    }


    /**
     * 不执行 ft.addToBackStack(null);
     */
    public void addFragment(Fragment fragment, int addLayID, int enter, int exit, int popEnter, int popExit) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(enter, exit, popEnter, popExit);

        ft.add(addLayID, fragment);
        ft.commitAllowingStateLoss();
    }


    /**
     * 执行 ft.addToBackStack(null); 圧栈，返回的时候弹出栈，可以设置动画
     */
    public void replaceFragmentAddStack(Fragment fragment, int enter, int exit, int popEnter, int popExit) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(enter, exit, popEnter, popExit);
        ft.replace(fragmentRoot(), fragment);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }


    /**
     * 执行 ft.addToBackStack(null); 圧栈，返回的时候弹出栈，可以设置动画
     */
    public void replaceFragmentAddStack(Fragment fragment, int enter, int exit, int popEnter, int popExit, String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(enter, exit, popEnter, popExit);
        ft.replace(fragmentRoot(), fragment, tag);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }


    /**
     * 执行 ft.addToBackStack(null); 圧栈，返回的时候弹出栈，可以设置动画
     */
    public void replaceFragmentAddStack(Fragment fragment, int replaceLayID, int enter, int exit, int popEnter, int popExit) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(enter, exit, popEnter, popExit);
        ft.replace(replaceLayID, fragment);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }


    /**
     * 不执行 ft.addToBackStack(null);,可以设置动画
     */
    public void replaceFragment(Fragment fragment, int enter, int exit, int popEnter, int popExit) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(enter, exit, popEnter, popExit);
        ft.replace(fragmentRoot(), fragment);
        ft.commitAllowingStateLoss();
    }


    /**
     * 不执行 ft.addToBackStack(null);可以设置动画
     */
    public void replaceFragment(Fragment fragment, int replaceLayID, int enter, int exit, int popEnter, int popExit) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(enter, exit, popEnter, popExit);
        ft.replace(replaceLayID, fragment);
        ft.commitAllowingStateLoss();
    }


    /**
     * Fragment注入的默认布局id
     */
    protected int fragmentRoot() {
        return 0;
    }
}
