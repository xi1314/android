package com.remair.heixiu.adapters;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

/**
 * Created by JXHIUUI on 2016/6/10.
 */
public class RechargePagerAdapter extends PagerAdapter {

    ArrayList<View> views;
    String[] strings;

    public RechargePagerAdapter(ArrayList<View> views, String[] strings) {
        this.views = views;
        this.strings = strings;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return strings[position];
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = views.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

}
