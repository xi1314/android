/**
 *
 */
package studio.archangel.toolkitv2.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import studio.archangel.toolkitv2.R;

/**
 * @author Michael
 */
public class AngelActionBar extends RelativeLayout {
    static String default_left_text = "返回";
    static int default_foreground_color = -1;
    static int default_background_res = -1;
    static int default_text_color = -1;
    static int default_arrow_drawable = R.drawable.icon_actionbar_arrow;
    static int default_arrow_color = R.color.blue;
    static DisplayMode default_display_mode_left = DisplayMode.arrow;
    static DisplayMode default_display_mode_right = DisplayMode.none;
    static AngelMaterialProperties.EffectMode default_effect_mode = AngelMaterialProperties.EffectMode.dark;

    public enum DisplayMode {
        arrow, text, image, custom, none
    }

    public enum DisplayPosition {
        left, right, title
    }

    OnClickListener listener_title, listener_arrow, listener_right, listener_left;

    TextView tv_title, tv_arrow;
    AngelMaterialLinearLayout ll_arrow;

    FrameLayout fl_title, fl_left, fl_right;
    AngelMaterialButton tv_right, tv_left;
    ImageView iv_arrow, iv_right, iv_left;
    AngelMaterialRelativeLayout rl_right, rl_left;

    int color_text;
    Context c;

    public AngelActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * @param context
     */
    public AngelActionBar(Context context) {
        super(context);
        init(context);
    }

    void init(Context context) {
        c = context;
        if (default_foreground_color == -1) {
//            default_color = context.getResources().getColor(R.color.blue);
            default_foreground_color = R.color.blue;
            default_text_color = default_foreground_color;
        }
        if (default_background_res == -1) {
//            default_color = context.getResources().getColor(R.color.blue);
            default_background_res = R.color.white;
        }
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_actionbar, this);
        tv_title = (TextView) findViewById(R.id.widget_actionbar_title);
        fl_title = (FrameLayout) findViewById(R.id.widget_actionbar_title_custom);


        ll_arrow = (AngelMaterialLinearLayout) findViewById(R.id.widget_actionbar_arrow_container);
        tv_arrow = (TextView) findViewById(R.id.widget_actionbar_arrow_text);
        iv_arrow = (ImageView) findViewById(R.id.widget_actionbar_arrow);
        iv_arrow.setImageResource(default_arrow_drawable);
        iv_arrow.getDrawable().setColorFilter(getResources().getColor(default_arrow_color), PorterDuff.Mode.SRC_IN);
        rl_left = (AngelMaterialRelativeLayout) findViewById(R.id.widget_actionbar_left_image);
        iv_left = (ImageView) findViewById(R.id.widget_actionbar_left_image_content);
        tv_left = (AngelMaterialButton) findViewById(R.id.widget_actionbar_left);
        fl_left = (FrameLayout) findViewById(R.id.widget_actionbar_left_custom);

        rl_right = (AngelMaterialRelativeLayout) findViewById(R.id.widget_actionbar_right_image);
        iv_right = (ImageView) findViewById(R.id.widget_actionbar_right_image_content);
        tv_right = (AngelMaterialButton) findViewById(R.id.widget_actionbar_right);
        fl_right = (FrameLayout) findViewById(R.id.widget_actionbar_right_custom);

        ll_arrow.setEffectMode(default_effect_mode);
        rl_left.setEffectMode(default_effect_mode);
        rl_right.setEffectMode(default_effect_mode);
        tv_title.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener_title != null) {
                    listener_title.onClick(v);
                }
            }
        });

        ll_arrow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener_arrow != null) {
                    listener_arrow.onClick(v);
                } else {
//                    Context c = getContext();
                    if (c instanceof Activity) {
                        ((Activity) c).onBackPressed();
                    } else if (c instanceof FragmentActivity) {
                        ((FragmentActivity) c).onBackPressed();
                    }
                }
            }
        });

        tv_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener_right != null) {
                    listener_right.onClick(v);
                }
            }
        });

        rl_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener_right != null) {
                    listener_right.onClick(v);
                }
            }
        });

        tv_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener_left != null) {
                    listener_left.onClick(v);
                }
            }
        });

        rl_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener_left != null) {
                    listener_left.onClick(v);
                }
            }
        });

        color_text = getResources().getColor(default_text_color);
//        color_text = color_main;
        setMainColor(default_background_res);
        if (default_text_color != -1) {
            color_text = getResources().getColor(default_text_color);
            setTextColor(color_text);
        }
        setDisplayMode(DisplayPosition.left, default_display_mode_left);
        setDisplayMode(DisplayPosition.right, default_display_mode_right);
    }

    public void setTitleListener(OnClickListener listener_title) {
        this.listener_title = listener_title;
    }

    public void setArrowListener(OnClickListener listener_arrow) {
        this.listener_arrow = listener_arrow;
    }

    public void setArrowColor(int color) {
        iv_arrow.getDrawable().mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    public Drawable getArrowDrawable() {
        return iv_arrow.getDrawable();
    }

    public void setRightListener(OnClickListener listener_right) {
        this.listener_right = listener_right;
    }

    public void setLeftListener(OnClickListener listener_arrow) {
        this.listener_left = listener_arrow;
    }

    public void setMainColor(int c) {
//        color_main = c;
//        setBackgroundColor(c);
        setBackgroundResource(c);
//        tv_title.setTextColor(color_main);
//        tv_arrow.setTextColor(color_main);
//        tv_left.setTextColor(color_main);
//        tv_right.setTextColor(color_main);
    }

    public void setTextColor(int c) {
        color_text = c;
        tv_title.setTextColor(color_text);
        tv_arrow.setTextColor(color_text);
        tv_left.setTextColor(color_text);
        tv_right.setTextColor(color_text);
    }

    public void setTitleText(String s) {
        if (s == null) {
            s = "";
        }
        tv_title.setText(s);
        setDisplayMode(DisplayPosition.title, DisplayMode.text);
    }

    public String getTitleText() {
        return tv_title.getText().toString();
    }

    public void setTitleCustomView(View v) {
        fl_title.removeAllViews();
        fl_title.addView(v);
        setDisplayMode(DisplayPosition.title, DisplayMode.custom);
    }

    public void setArrowText(String s) {
        if (s == null) {
            s = default_left_text;
        }
        tv_arrow.setText(s);
        setDisplayMode(DisplayPosition.left, DisplayMode.arrow);
    }

    public void setLeftText(String s) {
        if (s == null) {
            s = "";
        }
        tv_left.setText(s);
        setDisplayMode(DisplayPosition.left, DisplayMode.text);
    }

    public void setLeftImage(int res) {
        iv_left.setImageResource(res);
        setDisplayMode(DisplayPosition.left, DisplayMode.image);
    }

    public void setLeftImage(Drawable d) {
        iv_left.setImageDrawable(d);
        setDisplayMode(DisplayPosition.left, DisplayMode.image);
    }

    public void setLeftCustomView(View v) {
        fl_left.removeAllViews();
        fl_left.addView(v);
        setDisplayMode(DisplayPosition.left, DisplayMode.custom);
    }

    public void setRightText(String s) {
        if (s == null) {
            s = "";
        }
        tv_right.setText(s);
        setDisplayMode(DisplayPosition.right, DisplayMode.text);
    }

    public void setRightImage(int res) {
        iv_right.setImageResource(res);
        setDisplayMode(DisplayPosition.right, DisplayMode.image);
    }

    public void setRightCustomView(View v) {
        fl_right.removeAllViews();
        fl_right.addView(v);
        setDisplayMode(DisplayPosition.right, DisplayMode.custom);
    }

    public void setDisplayMode(DisplayPosition position, DisplayMode mode) {
        switch (position) {
            case left: {
                switch (mode) {
                    case arrow: {
                        fl_left.setVisibility(View.GONE);
                        ll_arrow.setVisibility(View.VISIBLE);
                        tv_left.setVisibility(View.GONE);
                        rl_left.setVisibility(View.GONE);
                        break;
                    }
                    case text: {
                        fl_left.setVisibility(View.GONE);
                        ll_arrow.setVisibility(View.GONE);
                        tv_left.setVisibility(View.VISIBLE);
                        rl_left.setVisibility(View.GONE);
                        break;
                    }
                    case image: {
                        fl_left.setVisibility(View.GONE);
                        ll_arrow.setVisibility(View.GONE);
                        tv_left.setVisibility(View.GONE);
                        rl_left.setVisibility(View.VISIBLE);
                        break;
                    }
                    case none: {
                        fl_left.setVisibility(View.GONE);
                        ll_arrow.setVisibility(View.GONE);
                        tv_left.setVisibility(View.GONE);
                        rl_left.setVisibility(View.GONE);
                        break;
                    }
                    case custom: {
                        fl_left.setVisibility(View.VISIBLE);
                        ll_arrow.setVisibility(View.GONE);
                        tv_left.setVisibility(View.GONE);
                        rl_left.setVisibility(View.GONE);
                        break;
                    }
                }
                break;
            }
            case right: {
                switch (mode) {
                    case custom: {
                        fl_right.setVisibility(View.VISIBLE);
                        tv_right.setVisibility(View.GONE);
                        rl_right.setVisibility(View.GONE);
                        break;
                    }
                    case text: {
                        fl_right.setVisibility(View.GONE);
                        tv_right.setVisibility(View.VISIBLE);
                        rl_right.setVisibility(View.GONE);
                        break;
                    }
                    case image: {
                        fl_right.setVisibility(View.GONE);
                        tv_right.setVisibility(View.GONE);
                        rl_right.setVisibility(View.VISIBLE);
                        break;
                    }
                    case none: {
                        fl_right.setVisibility(View.GONE);
                        tv_right.setVisibility(View.GONE);
                        rl_right.setVisibility(View.GONE);
                        break;
                    }
                }
                break;
            }
            case title: {
                switch (mode) {
                    case custom: {
                        fl_title.setVisibility(View.VISIBLE);
                        tv_title.setVisibility(View.GONE);
                        break;
                    }



                    case text: {
                        fl_title.setVisibility(View.GONE);
                        tv_title.setVisibility(View.VISIBLE);
                        break;
                    }
                    case none: {
                        fl_title.setVisibility(View.GONE);
                        tv_title.setVisibility(View.GONE);
                        break;
                    }



                }
                break;
            }
        }
    }

    public AngelMaterialButton getLeftButton() {
        return tv_left;
    }

    public AngelMaterialButton getRightButton() {
        return tv_right;
    }

    public AngelMaterialRelativeLayout getRightImageButton() {
        return rl_right;
    }

    public AngelMaterialLinearLayout getArrowButton() {
        return ll_arrow;
    }

    public TextView getTitleTextView() {
        return tv_title;
    }

    public static String getDefaultLeftText() {
        return default_left_text;
    }

    public static void setDefaultLeftText(String default_left_text) {
        AngelActionBar.default_left_text = default_left_text;
    }

    public static int getDefaultForegroundColor() {
        return default_foreground_color;
    }

    public static void setDefaultForegroundColor(int default_color) {
        AngelActionBar.default_foreground_color = default_color;
    }

    public static int getDefaultBackgroundResource() {
        return default_background_res;
    }

    public static void setDefaultBackgroundResource(int default_background_res) {
        AngelActionBar.default_background_res = default_background_res;
    }

    public static int getDefaultArrowDrawable() {
        return default_arrow_drawable;
    }

    public static void setDefaultArrowDrawable(int default_arrow_drawable) {
        AngelActionBar.default_arrow_drawable = default_arrow_drawable;
    }

    public static void setDefaultArrowColor(int color) {
        AngelActionBar.default_arrow_color = color;
    }

    public static int getDefaultTextColor() {
        return default_text_color;
    }

    public static void setDefaultTextColor(int default_text_color) {
        AngelActionBar.default_text_color = default_text_color;
    }

    public static DisplayMode getDefualtDisplayModeLeft() {
        return default_display_mode_left;
    }

    public static void setDefualtDisplayModeLeft(DisplayMode defualt_display_mode_left) {
        AngelActionBar.default_display_mode_left = defualt_display_mode_left;
    }

    public static DisplayMode getDefualtDisplayModeRight() {
        return default_display_mode_right;
    }

    public static void setDefualtDisplayModeRight(DisplayMode defualt_display_mode_right) {
        AngelActionBar.default_display_mode_right = defualt_display_mode_right;
    }

    public static AngelMaterialProperties.EffectMode getDefaultEffectMode() {
        return default_effect_mode;
    }

    public static void setDefaultEffectMode(AngelMaterialProperties.EffectMode default_effect_mode) {
        AngelActionBar.default_effect_mode = default_effect_mode;
    }
}
