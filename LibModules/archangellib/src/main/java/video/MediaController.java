package video;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import studio.archangel.toolkitv2.R;

public class MediaController {
    private Toast t;
    private AngelMediaControllerView tv;

    private Context context;
    int color = -1;
    boolean is_volume = false, is_brightness = false, show_progress = false;
    int icon = -1;

    public MediaController(Context context) {
        this.context = context;
    }

    public void setViewColor(int res_id) {
        color = res_id;
    }

    public void show() {
        show(0);
    }

    public void setIcon(int i) {
        icon = i;
    }

    public void setIsVolume(boolean b) {
        is_volume = b;

    }

    public void setIsBrightness(boolean b) {
        is_brightness = b;
    }

    public void setShowProgressBar(boolean b) {
        show_progress = b;
    }

    public void show(float progress) {
        if (t == null) {
            t = new Toast(context);
            View layout = LayoutInflater.from(context).inflate(R.layout.vv, null);
            tv = (AngelMediaControllerView) layout.findViewById(R.id.video_controller_view);

            t.setView(layout);
            t.setGravity(Gravity.CENTER, 0, 0);
            t.setDuration(Toast.LENGTH_SHORT);
        }
        if (color != -1) {
            tv.setColor(color);
        }
        if (icon != -1) {
            tv.setIcon(icon);
        }
        tv.setIsVolume(is_volume);
        tv.setIsBrightness(is_brightness);
        tv.setProgressBarVisible(show_progress);
        tv.setProgress((int) progress);
        t.show();
    }
}
