package studio.archangel.toolkitv2.util.ui;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.view.View;

/**
 * Selector提供者
 * Created by Michael on 2014/11/7.
 */
public class SelectorProvider {
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressWarnings("deprecation")
    public static void setBackgroundFor(View v, Drawable drawable) {
        int paddingLeft = v.getPaddingLeft();
        int paddingRight = v.getPaddingRight();
        int paddingTop = v.getPaddingTop();
        int paddingBottom = v.getPaddingBottom();
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            v.setBackgroundDrawable(drawable);
        } else {
            v.setBackground(drawable);
        }
        v.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

    /**
     * 获取矩形的Selector。会基于{@code color_normal}生成一个深色版本用于press状态的显示
     *
     * @param color_normal 颜色
     * @return 返回Selector
     */
    public static StateListDrawable getSelector(int color_normal) {
        return getRoundSelector(0, color_normal);
    }

    public static StateListDrawable getSelector(int color_selected, int color_unselected) {
        StateListDrawable states = new StateListDrawable();
        ShapeDrawable selected = ShapeProvider.getRoundRect(0, color_selected);
        states.addState(new int[]{android.R.attr.state_selected}, selected);
        states.addState(new int[]{android.R.attr.state_pressed}, selected);
        states.addState(new int[]{android.R.attr.state_focused}, selected);
        states.addState(new int[]{}, ShapeProvider.getRoundRect(0, color_unselected));
        return states;

    }

    public static ColorStateList getColorStateList(int color_selected, int color_unselected) {
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_selected},
                new int[]{android.R.attr.state_pressed},
                new int[]{android.R.attr.state_checked},
                new int[]{}};
        int[] colors = {color_selected, color_selected, color_selected, color_unselected};
        return new ColorStateList(states, colors);
    }

    /**
     * 获取圆角矩形的Selector。会基于{@code color_normal}生成一个深色版本用于press状态的显示
     *
     * @param radius 圆角半径。单位：像素
     * @param normal 颜色
     * @return Selector
     */
    public static StateListDrawable getRoundSelector(int radius, int normal) {
        int r = Color.red(normal) * 7 / 8;
        int g = Color.green(normal) * 7 / 8;
        int b = Color.blue(normal) * 7 / 8;
        StateListDrawable states = new StateListDrawable();
        ShapeDrawable selected = ShapeProvider.getRoundRect(radius, Color.rgb(r, g, b));
        states.addState(new int[]{android.R.attr.state_selected}, selected);
        states.addState(new int[]{android.R.attr.state_pressed}, selected);
        states.addState(new int[]{android.R.attr.state_focused}, selected);
        states.addState(new int[]{}, ShapeProvider.getRoundRect(radius, normal));
        return states;
    }

    /**
     * 获取圆形的Selector。会基于{@code color_normal}生成一个深色版本用于press状态的显示
     *
     * @param normal 颜色
     * @return Selector
     */
    public static StateListDrawable getOvalSelector(int normal) {
        int r = Color.red(normal) * 7 / 8;
        int g = Color.green(normal) * 7 / 8;
        int b = Color.blue(normal) * 7 / 8;
        StateListDrawable states = new StateListDrawable();
        ShapeDrawable selected = ShapeProvider.getOval(Color.rgb(r, g, b));
        states.addState(new int[]{android.R.attr.state_selected}, selected);
        states.addState(new int[]{android.R.attr.state_pressed}, selected);
        states.addState(new int[]{android.R.attr.state_focused}, selected);
        states.addState(new int[]{}, ShapeProvider.getOval(normal));
        return states;
    }

    /**
     * 获取下圆角矩形的Selector。会基于{@code color_normal}生成一个深色版本用于press状态的显示
     *
     * @param radius 圆角半径。单位：像素
     * @param normal 颜色
     * @return Selector
     */
    public static StateListDrawable getLowerRoundSelector(int radius, int normal) {
        int r = Color.red(normal) * 7 / 8;
        int g = Color.green(normal) * 7 / 8;
        int b = Color.blue(normal) * 7 / 8;
        StateListDrawable states = new StateListDrawable();
        ShapeDrawable selected = ShapeProvider.getLowerRoundRect(radius, Color.rgb(r, g, b));
        states.addState(new int[]{android.R.attr.state_selected}, selected);
        states.addState(new int[]{android.R.attr.state_pressed}, selected);
        states.addState(new int[]{android.R.attr.state_focused}, selected);
        states.addState(new int[]{}, ShapeProvider.getLowerRoundRect(radius, normal));
        return states;
    }

    /**
     * 获取描边的圆角矩形的Selector。会基于{@code color_normal}生成一个深色版本用于press状态的显示
     *
     * @param radius 圆角半径。单位：像素
     * @param normal 颜色
     * @return Selector
     */
    public static StateListDrawable getStrokeRoundSelector(int radius, int thickness, int normal) {
        int r = Color.red(normal) * 7 / 8;
        int g = Color.green(normal) * 7 / 8;
        int b = Color.blue(normal) * 7 / 8;
        StateListDrawable states = new StateListDrawable();
        ShapeDrawable selected = ShapeProvider.getRoundRect(radius, Color.rgb(r, g, b));
        states.addState(new int[]{android.R.attr.state_selected}, selected);
        states.addState(new int[]{android.R.attr.state_pressed}, selected);
        states.addState(new int[]{android.R.attr.state_focused}, selected);
        states.addState(new int[]{}, ShapeProvider.getStrokeRoundRect(radius, thickness, normal));
        return states;
    }

    /**
     * 获取渐变的Selector。会基于{@code color_normal}生成一个深色版本用于press状态的显示
     *
     * @param color_start         正常状态，渐变的开始颜色
     * @param color_end           正常状态，渐变的结束颜色
     * @param color_pressed_start 按下状态，渐变的开始颜色
     * @param color_pressed_end   按下状态，渐变的结束颜色
     * @param orientation         渐变方向
     * @return Selector
     */
    public static StateListDrawable getGradientSelector(int color_start, int color_end, int color_pressed_start, int color_pressed_end, GradientDrawable.Orientation orientation) {
        StateListDrawable states = new StateListDrawable();
        GradientDrawable selected = ShapeProvider.getGradientRect(color_pressed_start, color_pressed_end, orientation);
        states.addState(new int[]{android.R.attr.state_selected}, selected);
        states.addState(new int[]{android.R.attr.state_pressed}, selected);
        states.addState(new int[]{android.R.attr.state_focused}, selected);
        states.addState(new int[]{}, ShapeProvider.getGradientRect(color_start, color_end, orientation));
        return states;
    }


}
