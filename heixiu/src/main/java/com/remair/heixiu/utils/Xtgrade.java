package com.remair.heixiu.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.remair.heixiu.R;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Chuxi on 2016/4/5.
 */
public class Xtgrade {
    static int widthPx = Utils.getPX(31);
    static int heightPx = Utils.getPX(15);
    static int widthPx1 = Utils.getPX(25);


    public static void mXtgrade(int i, SimpleDraweeView iv, TextView tv) {
        if (i <= 10) {
            Uri uri = HXImageLoader.getUriFromLocal(R.drawable.icon_level_star);
            iv.setImageURI(uri);
            if (i == 0) {
                tv.setText("" + 1);
            } else {
                tv.setText("" + i);
            }
        } else if (i <= 20 && i > 10) {
            Uri uri = HXImageLoader.getUriFromLocal(R.drawable.icon_level_moon);
            iv.setImageURI(uri);
            tv.setText("" + i);
        } else if (i <= 30 && i > 20) {
            Uri uri = HXImageLoader.getUriFromLocal(R.drawable.icon_level_san);
            iv.setImageURI(uri);
            tv.setText("" + i);
        } else if (i <= 40 && i > 30) {
            HXImageLoader
                    .loadGifFromLocal(iv, R.drawable.icon_level_crown, widthPx, heightPx);
            tv.setText("" + i);
        } else if (i <= 50 && i > 40) {
            HXImageLoader
                    .loadGifFromLocal(iv, R.drawable.icon_level_crown1, widthPx, heightPx);
            tv.setText("" + i);
        } else {
            HXImageLoader
                    .loadGifFromLocal(iv, R.drawable.icon_level_crown2, widthPx, heightPx);
            tv.setText("" + i);
        }
    }


    public static void mChatMessagegrade(Context context, int i, SimpleDraweeView iv, TextView tv) {
        if (i <= 10) {
            Uri uri = HXImageLoader.getUriFromLocal(R.drawable.grade_one);
            iv.setImageURI(uri);
            if (i == 0) {
                tv.setText("" + 1);
            } else {
                tv.setText("" + i);
            }
        } else if (i <= 20 && i > 10) {
            Uri uri = HXImageLoader.getUriFromLocal(R.drawable.grade_two);
            iv.setImageURI(uri);
            tv.setText("" + i);
        } else if (i <= 30 && i > 20) {
            Uri uri = HXImageLoader.getUriFromLocal(R.drawable.grade_three);
            iv.setImageURI(uri);
            tv.setText("" + i);
        } else if (i <= 40 && i > 30) {
            HXImageLoader
                    .loadGifFromLocal(iv, R.drawable.grade_four, widthPx1, widthPx1);
            tv.setText("" + i);
        } else if (i <= 50 && i > 40) {
            HXImageLoader
                    .loadGifFromLocal(iv, R.drawable.grade_fifve, widthPx1, widthPx1);
            tv.setText("" + i);
        } else {
            HXImageLoader
                    .loadGifFromLocal(iv, R.drawable.grade_six, widthPx1, widthPx1);
            tv.setText("" + i);
        }
    }


    public static String moneynumber(String ii) {
        DecimalFormat decimalFormat = new DecimalFormat("0.0");

        long i = Long.parseLong(ii);
        if (i < 10000000) {
            return i >= 10000
                   ? decimalFormat.format((i / 10000.0)) + "万"
                   : i + "";
        } else if (i < 100000000) {
            return decimalFormat.format((i / 10000000.0)) + "千万";
        } else {
            return decimalFormat.format((i / 100000000.0)) + "亿";
        }
    }


    public static String double2Str(double d) {
        return String.format(Locale.getDefault(), "%.2f", d);
    }


    public static boolean checkpassword(String string) {
        String regEx2 = "^(?!^\\d+$)(?!^[a-zA-Z]+$)[0-9_a-zA-Z]{8,16}$";
        Pattern pattern = Pattern.compile(regEx2);
        Matcher matcher = pattern.matcher(string);
        return !matcher.matches();
    }
}
