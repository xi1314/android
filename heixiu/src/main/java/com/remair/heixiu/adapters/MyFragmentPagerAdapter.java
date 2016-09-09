package com.remair.heixiu.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
import com.remair.heixiu.activity.MinediamondActivity;
import com.remair.heixiu.fragment.DiamondExchanFragement;

/**
 * 项目名称：heixu
 * 类描述：
 * 创建人：LiuJun
 * 创建时间：2016/8/31 20:52
 * 修改人：LiuJun
 * 修改时间：2016/8/31 20:52
 * 修改备注：
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private final int PAGER_COUNT = 2;
    private DiamondExchanFragement myFragment1 = null;
    private DiamondExchanFragement myFragment2 = null;


    public MyFragmentPagerAdapter(FragmentManager fm,int typedimo) {
        super(fm);
        //1代表钻石//2代表黑豆
        if(typedimo==1) {
            myFragment1 = new DiamondExchanFragement();
            Bundle mBundle = new Bundle();
            mBundle.putInt("type", 1);
            myFragment1.setArguments(mBundle);
            myFragment2 = new DiamondExchanFragement();
            Bundle mBundle2 = new Bundle();
            mBundle2.putInt("type", 2);
            myFragment2.setArguments(mBundle2);
        }else if(typedimo==2){

            myFragment1 = new DiamondExchanFragement();
            Bundle mBundle = new Bundle();
            mBundle.putInt("type", 3);
            myFragment1.setArguments(mBundle);
            myFragment2 = new DiamondExchanFragement();
            Bundle mBundle2 = new Bundle();
            mBundle2.putInt("type", 4);
            myFragment2.setArguments(mBundle2);

        }else if(typedimo==3){
            myFragment1 = new DiamondExchanFragement();
            Bundle mBundle = new Bundle();
            mBundle.putInt("type", 4);
            myFragment1.setArguments(mBundle);
            myFragment2 = new DiamondExchanFragement();
            Bundle mBundle2 = new Bundle();
            mBundle2.putInt("type", 2);
            myFragment2.setArguments(mBundle2);

        }
    }


    @Override
    public int getCount() {
        return PAGER_COUNT;
    }


    @Override
    public Object instantiateItem(ViewGroup vg, int position) {
        return super.instantiateItem(vg, position);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        System.out.println("position Destory" + position);
        super.destroyItem(container, position, object);
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case MinediamondActivity.PAGE_ONE:
                fragment = myFragment1;
                break;
            case MinediamondActivity.PAGE_TWO:
                fragment = myFragment2;
                break;
        }
        return fragment;
    }
}