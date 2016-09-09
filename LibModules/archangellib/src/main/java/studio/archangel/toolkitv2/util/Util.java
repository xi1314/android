/**
 *
 */
package studio.archangel.toolkitv2.util;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Application;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.util.SimpleArrayMap;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.TextView;
import com.jakewharton.rxbinding.view.RxView;
import java.io.File;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import rx.Subscriber;
import studio.archangel.toolkitv2.AngelApplication;
import studio.archangel.toolkitv2.R;
import studio.archangel.toolkitv2.activities.AngelActivity;
import studio.archangel.toolkitv2.activities.AngelActivityV4;
import studio.archangel.toolkitv2.views.AngelActionBar;

/**
 * 一些实用方法
 *
 * @author Michael
 */
public class Util {
    public static Context app_context;
    public final static String namespace_android = "http://schemas.android.com/apk/res/android";
    private static PowerManager.WakeLock wakeLock = null;


    public static void setApplicationContext(Application app) {
        app_context = app;
    }


    @Deprecated
    public static int createDarkerColor(int color) {
        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        if (r + g + b == 0) {
            return Color.argb(Math.min(a + 15, 255), 0, 0, 0);
        }
        //        a = (int) (a * 7 / 8.0);
        r = (int) (r * 7 / 8.0);
        g = (int) (g * 7 / 8.0);
        b = (int) (b * 7 / 8.0);
        return Color.rgb(r, g, b);
    }


    /**
     * 获取变暗的按下颜色
     *
     * @param color 原来的颜色
     */
    public static int getDarkerColor(int color) {
        return getPressedColor(color, true, 0.2f);
    }


    /**
     * 获取明亮的按下颜色
     *
     * @param color 原来的颜色
     */
    public static int getLighterColor(int color) {
        return getPressedColor(color, false, 0.2f);
    }


    /**
     * 获取禁用的按钮颜色（去色）
     *
     * @param color 原来的颜色
     */
    public static int getDisabledColor(int color) {
        //        float[] hsv = new float[3];
        //        int alpha = Color.alpha(color);
        //        Color.RGBToHSV(Color.red(color), Color.green(color), Color.blue(color), hsv);
        //        hsv[1] = 0;
        //        return Color.HSVToColor(alpha, hsv);
        return Color.parseColor("#b3b3b3");
    }


    /**
     * 获取按下颜色
     *
     * @param color 原来的颜色
     * @param darker 是否变暗。true则变暗，false则变亮
     * @param value 变化的数值，取值为[0f,1f]。这将作用在颜色的明度和饱和度上
     * @return 按下的颜色
     */
    public static int getPressedColor(int color, boolean darker, float value) {
        //        Logger.out("color origin:(" + Color.alpha(color) + "," + Color.red(color) + "," + Color.green(color) + "," + Color.blue(color) + ") darker:" + darker + " value:" + value);
        if (value < 0 || value > 1) {
            value = 0.2f;
        }
        float[] hsv = new float[3];
        int alpha = Color.alpha(color);
        Color.RGBToHSV(Color.red(color), Color.green(color), Color
                .blue(color), hsv);
        float hue = hsv[0];
        float sat = hsv[1];
        float bright = hsv[2];
        if (alpha == 255) {
            if (darker) {
                bright = Math.max(bright - value, 0f);
            } else {
                bright = Math.min(bright + value, 1f);
            }
        } else {

            if (darker) {
                bright = 0;
            } else {
                bright = 1;
            }
            //            if (alpha != 0) {
            //                Logger.out("Warning::Using alpha in (0,255) may lead to unexpected result. This will be improved later.");
            //            }
            alpha = (int) Math.min(alpha + 255 * 1.5 * value, 255);
        }

        int convert = Color.HSVToColor(alpha, new float[] { hue, sat, bright });
        //        Logger.out("color converted:(" + Color.alpha(convert) + "," + Color.red(convert) + "," + Color.green(convert) + "," + Color.blue(convert) + ")");
        return convert;
    }


    /**
     * 计算两点之间距离
     *
     * @param a 点A
     * @param b 点B
     * @return AB间距离
     */
    public static float getDistance(Point a, Point b) {
        return (float) Math
                .sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }


    /**
     * 隐藏软键盘
     *
     * @param v 正在输入内容（调用软键盘）的控件
     * @param c 上下文
     */
    public static void hideInputBoard(View v, Context c) {

        //        if (c instanceof Activity) {
        //            ((Activity) c).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //        } else {
        //            InputMethodManager imm = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        //            imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
        //        }
        InputMethodManager imm = (InputMethodManager) c
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


    /**
     * 隐藏软键盘
     *
     * @param a 正在显示软键盘的界面
     * 有可能没效果！
     */
    public static void hideInputBoard(Activity a) {
        //        InputMethodManager imm = (InputMethodManager) app_context.getSystemService(Context.INPUT_METHOD_SERVICE);
        //        View v = a.getCurrentFocus();
        //        if (v != null) {
        //            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        //        }
        InputMethodManager imm = (InputMethodManager) a
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = a.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(a);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    /**
     * 显示软键盘
     *
     * @param target 需要输入内容的控件
     * @param a 正在显示软键盘的界面
     */
    public static void showInputBoard(View target, Activity a) {
        InputMethodManager keyboard = (InputMethodManager) a
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.showSoftInput(target, InputMethodManager.SHOW_FORCED);
    }


    public static void setupActionBar(AngelActivityV4 act, String title) {
        setupActionBar(act, title, true);
    }


    public static void setupActionBar(AngelActivity act, String title) {
        setupActionBar(act, title, true);
    }


    public static void setupActionBar(AngelActivityV4 act, String title, boolean shouldSetTopPadding) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            shouldSetTopPadding = false;
        }
        setupActionBar(act, title, null, shouldSetTopPadding);
    }


    public static void setupActionBar(AngelActivity act, String title, boolean shouldSetTopPadding) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            shouldSetTopPadding = false;
        }
        setupActionBar(act, title, null, shouldSetTopPadding);
    }


    public static void setupActionBar(AngelActivity act, String title, String left, boolean shouldSetTopPadding) {
        ActionBar bar = act.getActionBar();
        if (bar == null) {
            return;
        }
        bar.setBackgroundDrawable(app_context.getResources()
                                             .getDrawable(R.color.trans));
        bar.setSplitBackgroundDrawable(app_context.getResources()
                                                  .getDrawable(R.color.trans));
        bar.setIcon(app_context.getResources().getDrawable(R.color.trans));
        bar.setDisplayHomeAsUpEnabled(false);
        bar.setDisplayShowCustomEnabled(true);
        bar.setDisplayShowHomeEnabled(false);
        bar.setTitle("");
        AngelActionBar aab = act.getAngelActionBar();
        if (aab == null) {
            aab = new AngelActionBar(act);
            act.setAngelActionBar(aab);
        }
        bar.setCustomView(aab);
        aab.setTitleText(title);
        aab.setArrowText(left);
        if (shouldSetTopPadding) {
            setContentViewExtraTopPadding(act,
                    getPX(48) + AngelApplication.status_bar_height);
        }
        //        bar.setBackgroundDrawable(app_context.getResources().getDrawable(AngelActionBar.getDefaultBackgroundResource()));

    }


    public static void setupActionBar(AngelActivityV4 act, String title, String left, boolean shouldSetTopPadding) {
        ActionBar bar = act.getActionBar();
        if (bar == null) {
            return;
        }
        bar.setIcon(app_context.getResources().getDrawable(R.color.trans));
        bar.setDisplayHomeAsUpEnabled(false);
        bar.setDisplayShowCustomEnabled(true);
        bar.setDisplayShowHomeEnabled(false);
        bar.setTitle("");
        AngelActionBar aab = act.getAngelActionBar();
        if (aab == null) {
            aab = new AngelActionBar(act);
            act.setAngelActionBar(aab);
        }
        bar.setCustomView(aab);
        aab.setTitleText(title);
        aab.setArrowText(left);
        if (shouldSetTopPadding) {
            setContentViewExtraTopPadding(act,
                    getPX(48) + AngelApplication.status_bar_height);
        }
        //        bar.setBackgroundDrawable(app_context.getResources().getDrawable(AngelActionBar.getDefaultBackgroundResource()));
        bar.setBackgroundDrawable(app_context.getResources()
                                             .getDrawable(R.color.trans));
    }


    public static void setupActionBar1(AngelActivityV4 act, View title, String left, boolean shouldSetTopPadding) {
        ActionBar bar = act.getActionBar();
        if (bar == null) {
            return;
        }
        bar.setIcon(app_context.getResources().getDrawable(R.color.trans));
        bar.setDisplayHomeAsUpEnabled(false);
        bar.setDisplayShowCustomEnabled(true);
        bar.setDisplayShowHomeEnabled(false);
        bar.setTitle("");
        AngelActionBar aab = act.getAngelActionBar();
        if (aab == null) {
            aab = new AngelActionBar(act);
            act.setAngelActionBar(aab);
        }
        bar.setCustomView(aab);
        aab.setTitleCustomView(title);
        aab.setArrowText(left);
        if (shouldSetTopPadding) {
            setContentViewExtraTopPadding(act,
                    getPX(48) + AngelApplication.status_bar_height);
        }
        //        bar.setBackgroundDrawable(app_context.getResources().getDrawable(AngelActionBar.getDefaultBackgroundResource()));
        bar.setBackgroundDrawable(app_context.getResources()
                                             .getDrawable(R.color.trans));
    }


    public static void setContentViewTopPadding(Activity act, int padding) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && false) {
            try {
                View content = act.findViewById(android.R.id.content);
                if (content == null) {
                    return;
                }
                content.setPadding(content.getPaddingLeft(), padding, content
                        .getPaddingRight(), content.getPaddingBottom());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static int getContentViewTopPadding(Activity act) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && false) {
            try {
                View content = act.findViewById(android.R.id.content);
                if (content == null) {
                    return 0;
                }
                return content.getPaddingTop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return 0;
    }


    public static void setContentViewExtraTopPadding(Activity act, int padding) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && false) {
            try {
                View content = act.findViewById(android.R.id.content);
                if (content == null) {
                    return;
                }
                content.setPadding(content.getPaddingLeft(),
                        content.getPaddingTop() + padding, content
                                .getPaddingRight(), content.getPaddingBottom());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static AnimatorSet getColorAnimator(int color0, int color1, long duration, final OnColorChangedListener listener) {
        int a0 = Color.alpha(color0);
        int r0 = Color.red(color0);
        int g0 = Color.green(color0);
        int b0 = Color.blue(color0);
        int a1 = Color.alpha(color1);
        int r1 = Color.red(color1);
        int g1 = Color.green(color1);
        int b1 = Color.blue(color1);
        final ValueAnimator a_a = ValueAnimator.ofInt(a0, a1);
        final ValueAnimator a_r = ValueAnimator.ofInt(r0, r1);
        final ValueAnimator a_g = ValueAnimator.ofInt(g0, g1);
        final ValueAnimator a_b = ValueAnimator.ofInt(b0, b1);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(a_a, a_r, a_g, a_b);
        set.setDuration(duration);
        a_a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (listener != null) {
                    int color = Color
                            .argb((int) a_a.getAnimatedValue(), (int) a_r
                                    .getAnimatedValue(), (int) a_g
                                    .getAnimatedValue(), (int) a_b
                                    .getAnimatedValue());
                    listener.onColorChanged(color);
                }
            }
        });
        return set;
    }


    public interface OnColorChangedListener {
        void onColorChanged(int color);
    }


    /**
     * 将sp转换为px
     *
     * @param spValue sp值
     * @return 相应的px
     */
    public static int getPXfromSP(Context context, float spValue) {
        float fontScale = context.getResources()
                                 .getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    /**
     * 将sp转换为px
     *
     * @param spValue sp值
     * @return 相应的px
     */
    public static int getPXfromSP(float spValue) {
        //        float fontScale = app_context.getResources().getDisplayMetrics().scaledDensity;
        //        return (int) (spValue * fontScale + 0.5f);
        return getPXfromSP(app_context, spValue);
    }


    /**
     * 将dp转换为px
     *
     * @param dipValue dp值
     * @return 相应的px
     */
    public static int getPX(float dipValue) {
        return getPX(app_context, dipValue);
    }


    /**
     * 将dp转换为px
     *
     * @param dipValue dp值
     * @return 相应的px
     */
    public static int getPX(Context c, float dipValue) {
        final float scale = c.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    /**
     * 将px转换为dip
     *
     * @param pxValue px值
     * @return 相应的dp
     */
    public static int getDP(float pxValue) {
        final float scale = app_context.getResources()
                                       .getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    public static int getReversedColor(int color) {
        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return Color.argb(a, 255 - r, 255 - g, 255 - b);
    }


    /**
     * 为指定的EditText设置输入限制，并把提示信息显示到指定的TextView
     *
     * @param et 要设置输入限制的EditText
     * @param tv 用来显示提示信息的TextView
     * @param max 最大输入长度
     */
    public static void setInputLimit(final EditText et, final TextView tv, final int max) {
        int l = et.getText().toString().length();
        if (l <= max) {
            tv.setText(max - l + "");
            tv.setTextColor(et.getContext().getResources()
                              .getColor(R.color.color_999));
        } else {
            tv.setText("已超出" + (l - max) + "字");
            tv.setTextColor(et.getContext().getResources()
                              .getColor(R.color.red));
        }
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }


            @Override
            public void afterTextChanged(Editable s) {
                int l = s.length();
                if (l <= max) {
                    tv.setText(max - l + "");
                    tv.setTextColor(et.getContext().getResources()
                                      .getColor(R.color.color_999));
                } else {
                    tv.setText("已超出" + (l - max) + "字");
                    tv.setTextColor(et.getContext().getResources()
                                      .getColor(R.color.red));
                }
            }
        });
    }


    public static int getColor(int id) {
        return app_context.getResources().getColor(id);
    }


    public static String getColorNote(int color, boolean with_alpha) {
        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        StringBuilder sb = new StringBuilder("#");
        String s;
        int[] array = new int[] { a, r, g, b };
        for (int i = 0; i < array.length; i++) {
            int value = array[i];
            if (i == 0 && !with_alpha) {
                continue;
            }
            s = Integer.toHexString(value);
            if (s.length() == 1) {
                sb.append("0").append(s);
            } else {
                sb.append(s);
            }
        }

        return sb.toString();
    }


    /**
     * 获得屏幕锁
     *
     * @param c 上下文
     * @return 屏幕锁
     */
    static PowerManager.WakeLock getWakeLock(Context c) {
        if (wakeLock != null) {
            return wakeLock;
        }
        try {
            PowerManager powerManager = (PowerManager) c
                    .getSystemService(Context.POWER_SERVICE);
            int field = 0x00000020;

            // Yeah, this is hidden field.
            field = PowerManager.class
                    .getField("PROXIMITY_SCREEN_OFF_WAKE_LOCK").getInt(null);

            wakeLock = powerManager.newWakeLock(field, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wakeLock;
    }


    /**
     * 激活近物传感器。当被遮挡时关闭屏幕
     *
     * @param c 上下文
     */
    public static void enableProximitySensor(Context c) {
        PowerManager.WakeLock wakeLock = getWakeLock(c);
        if (!wakeLock.isHeld()) {
            wakeLock.acquire();
        }
    }


    /**
     * 关闭近物传感器
     *
     * @param c 上下文
     */
    public static void disableProximitySensor(Context c) {
        PowerManager.WakeLock wakeLock = getWakeLock(c);
        if (wakeLock.isHeld()) {
            wakeLock.release();
        }
    }


    /**
     * 强制显示Actionbar的Overflow图标（三个点）
     */
    public static void setForceOverFlowIcon(Application a) {
        try {
            ViewConfiguration config = ViewConfiguration.get(a);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignore
        }
    }


    /**
     * 获取用户友好的文件大小
     *
     * @param size 文件大小，单位：字节
     */
    public static String getFileSize(float size) {
        try {
            if (size < 1024) {
                return String.format("%dbytes", (int) size);
            } else {
                size /= 1024;
                if (size < 1024) {
                    return String.format("%.1fKB", size);
                } else {
                    size /= 1024;
                    if (size < 1024) {
                        return String.format("%.1fMB", size);
                    } else {
                        size /= 1024;
                        return String.format("%.1fGB", size);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }


    public static String getFileSize(String path) {
        return getFileSize(new File(path));
    }


    public static String getFileSize(File f) {
        float size = f.length();
        return getFileSize(size);
    }


    public static void setDefaultFont(Context context, String staticTypefaceFieldName, String fontAssetName) {
        final Typeface regular = Typeface
                .createFromAsset(context.getAssets(), fontAssetName);
        replaceFont(staticTypefaceFieldName, regular);
    }


    static void replaceFont(String staticTypefaceFieldName, final Typeface newTypeface) {
        try {
            final Field staticField = Typeface.class
                    .getDeclaredField(staticTypefaceFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public static String getString(int id) {
        String string = null;
        if (app_context == null) {
            return "";
        }
        try {
            string = app_context.getResources().getString(id);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            string = "";
        }
        return string;
    }


    public static void fillStrings(ArrayList<String> list, int[] ids) {
        if (list == null) {
            list = new ArrayList<>();
        }
        for (int id : ids) {
            list.add(getString(id));
        }
    }


    private static final SimpleArrayMap<String, Typeface> font_cache = new SimpleArrayMap<>();


    public static void setFont(Context context, TextView text, String font_file) {
        synchronized (font_cache) {
            Typeface tf = font_cache.get(font_file);
            if (tf == null) {
                try {
                    tf = Typeface
                            .createFromAsset(context.getAssets(), font_file);
                    font_cache.put(font_file, tf);
                    //            tf.hashCode();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            text.setTypeface(tf);
        }
    }


    public static String getShortName(String name) {
        if (name == null) {
            return "";
        }
        String short_name = name.trim();
        if (short_name.isEmpty()) {
            short_name = "？";
        } else {
            Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]+");
            Matcher m = p.matcher(name);
            if (m.find()) {
                short_name = short_name.substring(m.start(), m.end());
                if (short_name.length() != 0) {
                    short_name = short_name.substring(
                            short_name.length() - 1, short_name.length());
                } else {
                    short_name = "？";
                }
            } else {
                p = Pattern.compile("[a-zA-Z]+");
                m = p.matcher(name);
                if (m.find()) {
                    short_name = short_name.substring(m.start(), m.end());
                    short_name = short_name.substring(0, 1).toUpperCase();
                } else {
                    short_name = "？";
                }
            }
        }
        return short_name;
    }


    /**
     * MD5数字签名
     *
     * @param src 待加密字符串
     */
    public static String md5(String src) {
        // 定义数字签名方法, 可用：MD5, SHA-1
        MessageDigest md = null;
        byte[] b = null;
        try {
            md = MessageDigest.getInstance("MD5");
            b = md.digest(src.getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return byte2HexStr(b);
    }

    /**
     * BASE64编码
     * @param src
     * @return
     * @throws Exception
     */

    /**
     * 字节数组转化为大写16进制字符串
     */
    private static String byte2HexStr(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            String s = Integer.toHexString(b[i] & 0xFF);
            if (s.length() == 1) {
                sb.append("0");
            }
            sb.append(s.toUpperCase());
        }
        return sb.toString();
    }


    public static void removeOnGlobalLayoutListener(View target, ViewTreeObserver.OnGlobalLayoutListener listener) {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                target.getViewTreeObserver()
                      .removeGlobalOnLayoutListener(listener);
            } else {
                target.getViewTreeObserver()
                      .removeOnGlobalLayoutListener(listener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void setBackgroundResourceWithOriginPadding(View v, int res) {
        int paddingLeft = v.getPaddingLeft();
        int paddingTop = v.getPaddingTop();
        int paddingRight = v.getPaddingRight();
        int paddingBottom = v.getPaddingBottom();
        v.setBackgroundResource(res);
        v.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }


    public static void setBackgroundDrawableWithOriginPadding(View v, Drawable drawable) {
        int paddingLeft = v.getPaddingLeft();
        int paddingTop = v.getPaddingTop();
        int paddingRight = v.getPaddingRight();
        int paddingBottom = v.getPaddingBottom();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            v.setBackground(drawable);
        } else {
            v.setBackgroundDrawable(drawable);
        }
        v.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }


    /**
     * Get a file path from a Uri. This will GET the the path for Storage
     * Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @author paulburke
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" +
                            split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri
                        .parse("content://downloads/public_downloads"), Long
                        .valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };

        try {
            cursor = context.getContentResolver()
                            .query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents"
                .equals(uri.getAuthority());
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents"
                .equals(uri.getAuthority());
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents"
                .equals(uri.getAuthority());
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content"
                .equals(uri.getAuthority());
    }


    public static void rewindAnimationDrawableToFirstFrame(AnimationDrawable d) {
        int current = -1;

        Drawable currentFrame;
        currentFrame = d.getCurrent();

        int total = d.getNumberOfFrames();
        for (int i = 0; i < total; i++) {
            if (d.getFrame(i) == currentFrame) {
                current = i;
                break;
            }
        }
        if (current != -1 && current != 0) {
            for (int i = 0; i < total - current; i++) {
                d.run();
            }
        }
    }


    public static String getStringFromArray(Object[] array) {
        return getStringFromArray(array, ",");
    }


    public static String getStringFromArray(List array) {
        return getStringFromArray(array, ",");
    }


    public static String getStringFromArray(List array, String separator) {
        if (array == null || array.size() == 0) {
            return "";
        }
        return getStringFromArray(array
                .toArray(new Object[array.size()]), separator);
    }


    public static String getStringFromArray(Object[] array, String separator) {
        if (array == null || array.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i].toString()).append(separator);
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }


    public static void refreshItemInCollectionView(AbsListView container, Object item) {
        int start = container.getFirstVisiblePosition();
        for (int i = start, j = container.getLastVisiblePosition(); i <= j;
             i++) {
            if (item == container.getItemAtPosition(i)) {
                View view = container.getChildAt(i - start);
                container.getAdapter().getView(i, view, container);
                break;
            }
        }
    }


    public static boolean isWithinViewsBound(float x, float y, View v) {
        if (v == null) {
            return false;
        }
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        return x > location[0] && x < location[0] + v.getWidth() &&
                y > location[1] && y < location[1] + v.getHeight();
    }


    public static String getDeviceId(Context c) {
        TelephonyManager telephonyManager = (TelephonyManager) c
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }


    public enum NetWorkStatus {
        mobile, wifi, none
    }


    public static boolean isNetWorkAvailable() {
        return getNetWorkStatus() != NetWorkStatus.none;
    }


    public static NetWorkStatus getNetWorkStatus() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ConnectivityManager cm = (ConnectivityManager) app_context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = cm.getNetworkInfo(cm.getActiveNetwork());
                if (info != null) { // connected to the internet
                    if (info.getType() == ConnectivityManager.TYPE_WIFI &&
                            info.isAvailable()) {
                        return NetWorkStatus.wifi;
                    } else if (
                            info.getType() == ConnectivityManager.TYPE_MOBILE &&
                                    info.isAvailable()) {
                        return NetWorkStatus.mobile;
                    }
                }
                return NetWorkStatus.none;
            } else {
                ConnectivityManager conMan = (ConnectivityManager) app_context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mobile = conMan
                        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                NetworkInfo wifi = conMan
                        .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if (wifi.isAvailable()) {
                    return NetWorkStatus.wifi;
                } else if (mobile.isAvailable()) {
                    return NetWorkStatus.mobile;
                } else {
                    return NetWorkStatus.none;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return NetWorkStatus.none;
    }


    /**
     * 获取字符串长度，中文算两个
     *
     * @param value 目标字符串
     */
    public static int getRealLength(String value) {
        int valueLength = 0;
        String chinese = "[\u4e00-\u9fa5]";
        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            if (temp.matches(chinese)) {
                valueLength += 2;
            } else {
                valueLength += 1;
            }
        }
        return valueLength;
    }


    /**
     * 从头截取字符串到目标长度
     *
     * @param value 目标字符串
     * @param limit 目标长度，中文算两个
     */
    public static String getCutString(String value, int limit) {
        int valueLength = 0;
        String chinese = "[\u4e00-\u9fa5]";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            if (temp.matches(chinese)) {
                valueLength += 2;
            } else {
                valueLength += 1;
            }
            if (valueLength > limit) {
                break;
            }
            sb = sb.append(temp);
        }
        return sb.toString();
    }


    public static void lazyLoad(final AngelActivity act, final long delay, final Runnable r) {

        act.getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                act.handler.postDelayed(r, delay);
            }
        });
    }


    public static void lazyLoad(final AngelActivityV4 act, final long delay, final Runnable r) {

        act.getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                act.handler.postDelayed(r, delay);
            }
        });
    }


    /**
     * 防止用户抽风点击
     */
    public static void setOnClickListenerFor(final View v, final View.OnClickListener listener) {
        RxView.clicks(v).throttleFirst(1, TimeUnit.SECONDS)
              .subscribe(new Subscriber<Void>() {
                  @Override
                  public void onCompleted() {

                  }


                  @Override
                  public void onError(Throwable e) {

                  }


                  @Override
                  public void onNext(Void aVoid) {
                      if (listener != null) {
                          listener.onClick(v);
                      }
                  }
              });
    }


    public interface OnDataFetchedListener {
        void onDataFetched(Object data);
    }
}
