package com.remair.heixiu.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：LiuJun
 * 创建时间：16/8/23 17:58
 * 修改人：LiuJun
 * 修改时间：16/8/23 17:58
 * 修改备注：
 */
public class UiUtil {

    private static int displayWidth = 0; // 显示器宽
    private static int displayHeight = 0; // 显示器高


    public static int getDisplayWidth(Context context) {
        refresh(context);
        return displayWidth;
    }


    public static int getDisplayHeight(Context context) {
        refresh(context);
        return displayHeight;
    }


    private static void refresh(Context context) {
        if (displayWidth > 0 && displayHeight > 0) {
            return;
        }
        init(context);
    }


    private static void init(Context context) {
        try {
            DisplayMetrics dm = new DisplayMetrics();
            WindowManager wm = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(dm);
            displayWidth = dm.widthPixels;
            displayHeight = dm.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources()
                                       .getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }


    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources()
                                       .getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
