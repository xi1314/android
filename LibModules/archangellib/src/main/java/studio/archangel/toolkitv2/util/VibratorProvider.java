package studio.archangel.toolkitv2.util;

import android.content.Context;
import android.os.Vibrator;

/**
 * 震动提供者
 * Created by Michael on 2014/10/23.
 */
public class VibratorProvider {

    /**
     * 获取振动器
     *
     * @param c 上下文
     * @return 振动器
     */
    public static Vibrator getVibrator(Context c) {
        Vibrator v = (Vibrator) c.getSystemService(Context.VIBRATOR_SERVICE);
        return v;
    }
}
