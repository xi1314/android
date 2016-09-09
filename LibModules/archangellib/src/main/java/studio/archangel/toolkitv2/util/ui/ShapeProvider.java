package studio.archangel.toolkitv2.util.ui;

import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;

/**
 * Created by Michael on 2015/1/26.
 */
public class ShapeProvider {
    /**
     * 获得圆角矩形的Drawable
     *
     * @param radius
     * @param color
     * @return Drawable
     */
    public static ShapeDrawable getRoundRect(int radius, int color) {
        int px = radius;
//        int px = Util.getPX(radius);

        float[] outerR = new float[]{px, px, px, px, px, px, px, px};
        RoundRectShape s = new RoundRectShape(outerR, null, null);

        ShapeDrawable sd = new ShapeDrawable(s);
        sd.getPaint().setColor(color);
        sd.getPaint().setStyle(Paint.Style.FILL);
        return sd;
    }

    /**
     * 获得矩形的Drawable
     *
     * @param color
     * @return Drawable
     */
    public static ShapeDrawable getRect(int color) {
        RectShape s = new RectShape();

        ShapeDrawable sd = new ShapeDrawable(s);
        sd.getPaint().setColor(color);
        sd.getPaint().setStyle(Paint.Style.FILL);
        return sd;
    }

    /**
     * 获得圆形的Drawable
     *
     * @param color
     * @return Drawable
     */
    public static ShapeDrawable getOval(int color) {
//        int px = Util.getPX(radius);
//        float[] outerR = new float[]{px, px, px, px, px, px, px, px};
//        RoundRectShape s = new RoundRectShape(outerR, null, null);
        OvalShape s = new OvalShape();
        ShapeDrawable sd = new ShapeDrawable(s);
        sd.getPaint().setColor(color);
        sd.getPaint().setStyle(Paint.Style.FILL);
        return sd;
    }

    public static ShapeDrawable getLowerRoundRect(int radius, int color) {
//        int px = Util.getPX(radius);
        int px = radius;
        float[] outerR = new float[]{0, 0, 0, 0, px, px, px, px};
        RoundRectShape s = new RoundRectShape(outerR, null, null);

        ShapeDrawable sd = new ShapeDrawable(s);
        sd.getPaint().setColor(color);
        sd.getPaint().setStyle(Paint.Style.FILL);
        return sd;
    }

    public static ShapeDrawable getStrokeRoundRect(int radius, int thickness, int color) {
//        int px = Util.getPX(radius);
        int px = radius;
        float[] outerR = new float[]{px, px, px, px, px, px, px, px};
        float[] interR = new float[]{px / 2, px / 2, px / 2, px / 2, px / 2, px / 2, px / 2, px / 2};
        RoundRectShape s = new RoundRectShape(outerR, new RectF(thickness, thickness, thickness, thickness), interR);

        ShapeDrawable sd = new ShapeDrawable(s);
        sd.getPaint().setColor(color);
        sd.getPaint().setStyle(Paint.Style.FILL);
        return sd;
    }

    /**
     * 获得渐变的Drawable
     *
     * @param color_start 渐变的开始颜色
     * @param color_end   渐变的结束颜色
     * @param orientation 渐变方向
     * @return Drawable
     */
    public static GradientDrawable getGradientRect(int color_start, int color_end, GradientDrawable.Orientation orientation) {

        GradientDrawable gd = new GradientDrawable(orientation, new int[]{color_start, color_end});
        return gd;
    }
}
