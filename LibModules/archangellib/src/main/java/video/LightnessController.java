package video;

import android.app.Activity;
import android.content.ContentResolver;
import android.provider.Settings.System;
import android.view.WindowManager;

import studio.archangel.toolkitv2.util.text.Notifier;

public class LightnessController {

    // 判断是否开启了自动亮度调节
    public static boolean isAutoBrightness(Activity act) {
        boolean automicBrightness = false;
        ContentResolver aContentResolver = act.getContentResolver();
        try {
            automicBrightness = System.getInt(aContentResolver,
                    System.SCREEN_BRIGHTNESS_MODE) == System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        } catch (Exception e) {
//			Toast.makeText(act, "无法获取亮度", Toast.LENGTH_SHORT).show();
            Notifier.showNormalMsg(act, "无法获取亮度");
        }
        return automicBrightness;
    }

    // 改变亮度
    public static void setLightness(Activity act, int value) {
        try {
            if (value < 1) {
                value = 1;
            } else if (value > 255) {
                value = 255;
            }
            System.putInt(act.getContentResolver(), System.SCREEN_BRIGHTNESS, value);
            WindowManager.LayoutParams lp = act.getWindow().getAttributes();

            lp.screenBrightness = value / 255f;

//            Logger.out("setLightness:" + lp.screenBrightness);
            act.getWindow().setAttributes(lp);
        } catch (Exception e) {
//			Toast.makeText(act, "无法改变亮度", Toast.LENGTH_SHORT).show();
            Notifier.showNormalMsg(act, "无法改变亮度");
        }
    }

    // 获取亮度
    public static int getLightness(Activity act) {
        return System.getInt(act.getContentResolver(), System.SCREEN_BRIGHTNESS, -1);
    }

    // 停止自动亮度调节
    public static void stopAutoBrightness(Activity activity) {
        System.putInt(activity.getContentResolver(),
                System.SCREEN_BRIGHTNESS_MODE,
                System.SCREEN_BRIGHTNESS_MODE_MANUAL);
    }

    // 开启亮度自动调节
    public static void startAutoBrightness(Activity activity) {
        System.putInt(activity.getContentResolver(),
                System.SCREEN_BRIGHTNESS_MODE,
                System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
    }
}
