package studio.archangel.toolkitv2.util.ui;

import android.content.Context;
import android.view.View;

import studio.archangel.toolkitv2.R;
import studio.archangel.toolkitv2.util.Util;
import studio.archangel.toolkitv2.views.AngelMaterialProperties;

/**
 * Created by Administrator on 2015/9/18.
 */
public class TabConfig {
    static int default_tab_background_color_selected_res = R.color.white;
    static int default_tab_background_color_unselected_res = R.color.white;
    static int default_text_color_selected_res = R.color.blue;
    static int default_text_color_unselected_res = R.color.text_black;
    static int default_icon_tint_color_unselected_res = R.color.grey;
    boolean has_red_dot = false;
    boolean red_dot_with_text = true;
    boolean has_strip = false;
    boolean has_divider = true;
    boolean should_tint_icon = true;
    boolean is_special_button = false;

    int tab_background_color_selected;
    int tab_background_color_unselected;
    int text_color_selected;
    int text_color_unselected;
    int strip_color;
    int divider_color;
    String text;
    int text_size = -1;
    int icon_res_selected = R.color.trans;
    int icon_res = R.color.trans;
    int icon_size = -1;
    int icon_tint_color_selected;
    int icon_tint_color_unselected;
    int tab_height = 48;
    SpecialButtonConstructor constructor;
    AngelMaterialProperties.EffectMode mode = AngelMaterialProperties.EffectMode.dark;

    public TabConfig(Context context) {
        tab_background_color_selected = context.getResources().getColor(default_tab_background_color_selected_res);
        tab_background_color_unselected = context.getResources().getColor(default_tab_background_color_unselected_res);
        text_color_selected = context.getResources().getColor(default_text_color_selected_res);
        text_color_unselected = context.getResources().getColor(default_text_color_unselected_res);
        strip_color = text_color_selected;
        icon_tint_color_selected = text_color_selected;
        icon_tint_color_unselected = context.getResources().getColor(default_icon_tint_color_unselected_res);
        divider_color = context.getResources().getColor(R.color.divider_normal);
        tab_height = Util.getPX(tab_height);
    }

    public TabConfig setHasRedDot(boolean b) {
        has_red_dot = b;
        return this;
    }

    public TabConfig setRedDotWithText(boolean b) {
        red_dot_with_text = b;
        return this;
    }

    public TabConfig setHasStripe(boolean b) {
        has_strip = b;
        return this;
    }

    public TabConfig setHasDivider(boolean b) {
        has_divider = b;
        return this;
    }

    public TabConfig setText(String b) {
        text = b;
        return this;
    }

    public TabConfig setIsSpecialButton(boolean b) {
        is_special_button = b;
        return this;
    }

    public SpecialButtonConstructor getSpecialButtonConstructor() {
        return constructor;
    }

    public TabConfig setSpecialButtonConstructor(SpecialButtonConstructor constructor) {
        this.constructor = constructor;
        return this;
    }

    public boolean isSpecialButton() {
        return is_special_button;
    }

    public boolean shouldTintIcon() {
        return should_tint_icon;
    }

    public TabConfig setShouldTintIcon(boolean should_tint_icon) {
        this.should_tint_icon = should_tint_icon;
        return this;
    }

    public TabConfig setIconResSelected(int b) {
        icon_res_selected = b;
        return this;
    }

    public TabConfig setIconRes(int b) {
        icon_res = b;
        return this;
    }

    public TabConfig setIconSize(int b) {
        icon_size = b;
        return this;
    }

    public TabConfig setTextSizeInSP(int b) {
        text_size = b;
        return this;
    }

    public TabConfig setIconTintColorSelected(int b) {
        icon_tint_color_selected = b;
        return this;
    }

    public TabConfig setIconTintColorUnselected(int b) {
        icon_tint_color_unselected = b;
        return this;
    }

    public TabConfig setBackgroundColorSelected(int b) {
        tab_background_color_selected = b;
        return this;
    }

    public TabConfig setBackgroundColorUnselected(int b) {
        tab_background_color_unselected = b;
        return this;
    }

    public TabConfig setBackgroundEffectMode(AngelMaterialProperties.EffectMode b) {
        mode = b;
        return this;
    }

    public TabConfig setTextColorSelected(int b) {
        text_color_selected = b;
        return this;
    }

    public TabConfig setTextColorUnselected(int b) {
        text_color_unselected = b;
        return this;
    }

    public TabConfig setDividerColor(int b) {
        divider_color = b;
        return this;
    }

    public TabConfig setStripeColor(int b) {
        strip_color = b;
        return this;
    }

    public TabConfig setTabHeight(int b) {
        tab_height = b;
        return this;
    }

    public int getTabHeight() {
        return tab_height;
    }

    public boolean hasRedDot() {
        return has_red_dot;
    }

    public boolean isRedDotWithText() {
        return red_dot_with_text;
    }

    public boolean hasStrip() {
        return has_strip;
    }

    public boolean hasDivider() {
        return has_divider;
    }

    public int getTabBackgroundColorSelected() {
        return tab_background_color_selected;
    }

    public int getTabBackgroundColorUnselected() {
        return tab_background_color_unselected;
    }

    public int getTextColorSelected() {
        return text_color_selected;
    }

    public int getTextColorUnselected() {
        return text_color_unselected;
    }

    public int getStripeColor() {
        return strip_color;
    }

    public int getDividerColor() {
        return divider_color;
    }

    public String getText() {
        return text;
    }

    public int getIconResSelected() {
        return icon_res_selected;
    }

    public int getIconRes() {
        return icon_res;
    }

    public int getIconSize() {
        return icon_size;
    }

    public int getTextSize() {
        return text_size;
    }

    public int getIconTintColorSelected() {
        return icon_tint_color_selected;
    }

    public int getIconTintColorUnselected() {
        return icon_tint_color_unselected;
    }

    public AngelMaterialProperties.EffectMode getMode() {
        return mode;
    }

    public interface SpecialButtonConstructor {
        View construct();
    }
}