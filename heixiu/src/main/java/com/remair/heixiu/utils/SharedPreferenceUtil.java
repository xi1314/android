package com.remair.heixiu.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import java.util.Set;

public class SharedPreferenceUtil {

    public static Context context;

    public static SharedPreferences sharedPreferences;
    public static Editor editor;


    public static void setContext(Context con) {
        context = con.getApplicationContext();
        sharedPreferences = context
                .getSharedPreferences("nbvSharepre", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //		if("".equals(getString("webAddress", ""))){
        //			putString("webAddress", "http://camera.neobv.com:9002/WebService.asmx");
        //		}
    }


    /**
     * @author wht
     * 从SharedPreferences文件中获取保存的Int数据
     */
    public static void putInt(String key, int value) {
        editor.putInt(key, value).commit();
    }


    /**
     * 向SharedPreferences文件中获取Int数据
     *
     * @param defauleValue Ĭ默认值ֵ
     */
    public static int getInt(String key, int defauleValue) {
        try {
            return sharedPreferences.getInt(key, defauleValue);
        } catch (Exception e) {
            return defauleValue;
        }
    }


    public static void putString(String key, String value) {
        editor.putString(key, value).commit();
    }


    public static String getString(String key, String defaultValue) {
        try {
            return sharedPreferences.getString(key, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }


    public static void putStringSet(String key, Set<String> value) {
        editor.putStringSet(key, value).commit();
    }


    public static Set<String> getStringSet(String key, Set<String> defaultValue) {
        try {
            return sharedPreferences.getStringSet(key, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }


    public static void Remove(String key) {
        try {
            editor.remove(key).commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * @author wht
     * 向SharedPreferences文件中保存Boolean数据
     */
    public static void putBoolean(String key, Boolean value) {
        editor.putBoolean(key, value).commit();
    }


    public static Boolean getBoolean(String key, Boolean defaultVlaue) {
        try {
            return sharedPreferences.getBoolean(key, defaultVlaue);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return defaultVlaue;
        }
    }


    public static void putLong(String key, long value) {
        editor.putLong(key, value).commit();
    }


    public static long getLong(String key, long defaultVlaue) {
        try {
            return sharedPreferences.getLong(key, defaultVlaue);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return defaultVlaue;
        }
    }
}
