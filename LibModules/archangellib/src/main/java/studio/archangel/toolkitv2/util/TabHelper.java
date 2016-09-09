package studio.archangel.toolkitv2.util;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import studio.archangel.toolkitv2.R;
import studio.archangel.toolkitv2.fragments.AngelFragmentV4;
import studio.archangel.toolkitv2.util.ui.SelectorProvider;
import studio.archangel.toolkitv2.util.ui.TabConfig;
import studio.archangel.toolkitv2.views.AngelMaterialProperties;
import studio.archangel.toolkitv2.views.AngelMaterialRelativeLayout;
import studio.archangel.toolkitv2.views.AngelRedDot;
import studio.archangel.toolkitv2.views.AngelTabHost;

/**
 * Created by Michael on 2015/4/8.
 */
public class TabHelper {
    AngelTabHost host;
    Activity act;
    int res_layout_tab = R.layout.view_tab;
    String last_selected_tab_tag = null;

    public TabHelper(Activity a, AngelTabHost h) {
        act = a;
        host = h;
        host.getTabWidget().setDividerDrawable(null);
    }

    public void addTab(TabConfig config, Class<? extends AngelFragmentV4> fragment) {
        if (!config.isSpecialButton()) {
            TabHost.TabSpec tab = host.newTabSpec(config.getText());
            View layout = act.getLayoutInflater().inflate(res_layout_tab, null);
            final AngelMaterialRelativeLayout back = (AngelMaterialRelativeLayout) layout.findViewById(R.id.view_tab_back);
            TextView text = (TextView) layout.findViewById(R.id.view_tab_text);
            ImageView icon = (ImageView) layout.findViewById(R.id.view_tab_icon);
            ImageView divider = (ImageView) layout.findViewById(R.id.view_tab_divider);
            AngelRedDot red_dot = (AngelRedDot) layout.findViewById(R.id.view_tab_red_dot);
            View strip = layout.findViewById(R.id.view_tab_strip);
            Drawable drawable_unselected = act.getResources().getDrawable(config.getIconRes());
            StateListDrawable drawable = new StateListDrawable();
            if (config.shouldTintIcon()) {
                drawable_unselected.mutate().setColorFilter(config.getIconTintColorUnselected(), PorterDuff.Mode.SRC_IN);
//            Drawable drawable_selected = act.getResources().getDrawable(config.getIconRes());
                Drawable drawable_selected = drawable_unselected.getConstantState().newDrawable().mutate();
//            drawable_selected = createDrawable(act, drawable_selected, config.getIconTintColorSelected());
                drawable_selected.setColorFilter(config.getIconTintColorSelected(), PorterDuff.Mode.SRC_IN);
//            drawable_selected.setColorFilter(new PorterDuffColorFilter(config.getIconTintColorSelected(), PorterDuff.Mode.SRC_IN));
                drawable.addState(new int[]{android.R.attr.state_selected}, drawable_selected);
                drawable.addState(new int[]{android.R.attr.state_pressed}, drawable_selected);
                drawable.addState(new int[]{android.R.attr.state_focused}, drawable_selected);
                drawable.addState(new int[]{}, drawable_unselected);
                icon.setImageDrawable(drawable);
            } else {
                Drawable drawable_selected = act.getResources().getDrawable(config.getIconResSelected());
                drawable.addState(new int[]{android.R.attr.state_selected}, drawable_selected);
                drawable.addState(new int[]{android.R.attr.state_pressed}, drawable_selected);
                drawable.addState(new int[]{android.R.attr.state_focused}, drawable_selected);
                drawable.addState(new int[]{}, drawable_unselected);
                icon.setImageDrawable(drawable);
            }
            int size = config.getIconSize();
            if (size != -1) {
                ViewGroup.LayoutParams params = icon.getLayoutParams();
                params.width = params.height = size;
            }
            size = config.getTextSize();
            if (size != -1) {
                text.setTextSize(config.getTextSize());
            }
            back.setClickable(false);
            back.setEffectMode(config.getMode());
            back.setShapeBackgroundColor(config.getTabBackgroundColorUnselected());
//            back.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    back.setSelected(true);
//                }
//            });
            text.setText(config.getText());
            int[][] states = new int[][]{
                    new int[]{android.R.attr.state_selected},
                    new int[]{android.R.attr.state_pressed},
                    new int[]{android.R.attr.state_checked},
                    new int[]{}};
            int[] colors = {config.getTextColorSelected(), config.getTextColorSelected(), config.getTextColorSelected(), config.getTextColorUnselected()};
            text.setTextColor(new ColorStateList(states, colors));
            SelectorProvider.setBackgroundFor(strip, SelectorProvider.getSelector(config.getTextColorSelected(), config.getTextColorUnselected()));
            strip.setVisibility(config.hasStrip() ? View.VISIBLE : View.GONE);
            divider.setImageDrawable(new ColorDrawable(config.getDividerColor()));
            divider.setVisibility(config.hasDivider() ? View.VISIBLE : View.GONE);
            if (config.hasRedDot()) {
                red_dot.setVisibility(View.VISIBLE);
                red_dot.setStyle(config.isRedDotWithText() ? AngelMaterialProperties.RedDotStyle.text : AngelMaterialProperties.RedDotStyle.simple);
//            ViewGroup.LayoutParams params = red_dot.getLayoutParams();
//            if (config.isRedDotWithText()) {
//                params.width = Util.getPX(16);
//                params.height = Util.getPX(16);
//            } else {
//                params.width = Util.getPX(8);
//                params.height = Util.getPX(8);
//            }
            } else {
                red_dot.setVisibility(View.GONE);
            }
            tab.setIndicator(layout);
            host.addTab(tab, fragment, null);
            layout.getLayoutParams().height = config.getTabHeight();

            if (last_selected_tab_tag == null) {
                last_selected_tab_tag = config.getText();
            }
        } else {
            TabHost.TabSpec tab = host.newTabSpec(config.getText());
            View back = config.getSpecialButtonConstructor().construct();
            tab.setIndicator(back);
            host.addTab(tab, fragment, null, true);
            back.getLayoutParams().height = config.getTabHeight();
        }

    }

    public void refreshTabStatus(String new_selected_tab_tag) {
        int count = host.getTabWidget().getTabCount();
        for (int i = 0; i < count; i++) {
            View layout = host.getTabWidget().getChildTabViewAt(i);
            if (layout == null) {
                continue;
            }
            if (host.isSpecialButtonTab(i)) {
                continue;
            }
            AngelMaterialRelativeLayout back = (AngelMaterialRelativeLayout) layout.findViewById(R.id.view_tab_back);
            TextView text = (TextView) layout.findViewById(R.id.view_tab_text);
            ImageView icon = (ImageView) layout.findViewById(R.id.view_tab_icon);
            AngelRedDot red_dot = (AngelRedDot) layout.findViewById(R.id.view_tab_red_dot);
            View strip = layout.findViewById(R.id.view_tab_strip);
            boolean selected = text.getText().toString().equals(new_selected_tab_tag);
            if(selected){
                back.animateRipple();
            }
            text.setSelected(selected);
            icon.setSelected(selected);
            red_dot.setSelected(selected);
            strip.setSelected(selected);
        }

    }


    public AngelRedDot getRedDotAt(int index) {
        View tab = host.getTabWidget().getChildTabViewAt(index);
        try {
            return (AngelRedDot) tab.findViewById(R.id.view_tab_red_dot);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
