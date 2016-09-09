package com.remair.heixiu.bean;

import android.graphics.Color;
import android.widget.ImageView;

/**
 * Created by Chuxi on 2016/3/2.
 */
public class TanmuBean {
    public String[] items;
    public int color;
    public int minTextSize;
    public float range;

    public ImageView im;


    public TanmuBean() {
        color = Color.parseColor("#eeeeee");
        minTextSize = 16;
        range = 0.5f;
    }
}