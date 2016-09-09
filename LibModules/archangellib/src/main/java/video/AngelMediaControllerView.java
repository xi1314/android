package video;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import studio.archangel.toolkitv2.R;
import studio.archangel.toolkitv2.views.AngelProgressBarHorizontal;

public class AngelMediaControllerView extends RelativeLayout {

    // 进度
    int progress = 0;
    ImageView icon;
    AngelProgressBarHorizontal pb;
    boolean is_volume = false;
    boolean is_brightness = false;
    int res_icon;

    public AngelMediaControllerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public AngelMediaControllerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AngelMediaControllerView(Context context) {
        super(context);
        init(context);
    }

    public void setProgressBarVisible(boolean b) {
        pb.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    public boolean showProgressBar() {
        return pb.getVisibility() != View.GONE;
    }

    public void setIcon(int res) {
        res_icon = res;
    }

    public void setIsVolume(boolean b) {
        is_volume = b;
    }

    public void setIsBrightness(boolean b) {
        is_brightness = b;
    }

    void init(Context c) {
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.media_controller_view, this);
        icon = (ImageView) findViewById(R.id.media_controller_view_icon);
        pb = (AngelProgressBarHorizontal) findViewById(R.id.media_controller_view_pb);
    }

    public void setColor(int res_id) {
        pb.setBackgroundColor(getResources().getColor(res_id));
    }

    /**
     * 设置进度
     *
     * @param p 范围(0-100)
     */
    public void setProgress(int p) {
        progress = p;
        if (progress >= 100)
            progress = 100;
        if (progress <= 0)
            progress = 0;
        if (is_volume) {
            icon.setImageResource(progress == 0 ? R.drawable.icon_volume_off : R.drawable.icon_volume);
        } else if (is_brightness) {
            icon.setImageResource(progress == 0 ? R.drawable.icon_media_brightness_off : R.drawable.icon_media_brightness);
        } else {
            icon.setImageResource(res_icon);
        }
        if (showProgressBar()) {
            pb.setMax(100);
            pb.setProgress(progress);
        }

        postInvalidate();
    }
}
