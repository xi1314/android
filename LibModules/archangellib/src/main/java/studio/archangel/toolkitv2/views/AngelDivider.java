package studio.archangel.toolkitv2.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import studio.archangel.toolkitv2.R;

/**
 * Created by Michael on 2015/3/30.
 */
public class AngelDivider extends ImageView {
    static int default_divider_color_res = R.color.divider_normal;
    static int default_background_color_res = R.color.white;
//    static int default_height = 1;

    public static void setDefaultColorResource(int id) {
        default_divider_color_res = id;
    }

    public AngelDivider(Context context) {
        this(context, null);
    }

    public AngelDivider(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AngelDivider(Context context, AttributeSet attrs, int def) {
        super(context, attrs, def);
        setScaleType(ScaleType.FIT_XY);
        if (getDrawable() == null) {
            setImageResource(default_divider_color_res);
        }
        Drawable background = getBackground();
        if (background == null) {
            setBackgroundColor(getResources().getColor(default_background_color_res));
        }
//        setMinimumHeight(default_height);
    }

}
