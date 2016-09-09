package studio.archangel.toolkitv2.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import studio.archangel.toolkitv2.R;
import studio.archangel.toolkitv2.util.Util;
import studio.archangel.toolkitv2.util.ui.SelectorProvider;
import studio.archangel.toolkitv2.util.ui.ShapeProvider;

public class AngelOptionItem extends AngelMaterialLinearLayout {
    //    Context c;
    ImageView iv_icon, iv_arrow;
    TextView tv_title, tv_content, tv_subtitle;
    FrameLayout fl_custom;
    static int default_arrow_res = R.drawable.icon_arrow_right_grey;
    static int default_icon_back_res = R.color.blue;
    static int default_icon_radius = 0;
    static int default_icon_padding = 4;
    static int default_text_color = R.color.text_black;
//    public static int default_back = android.R.color.white;

//    public AngelOptionItem(Context context) {
//        super(context, null);
//        init(context);
//    }

    public AngelOptionItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_option_item, this);
        iv_icon = (ImageView) findViewById(R.id.widget_option_item_icon);
        iv_arrow = (ImageView) findViewById(R.id.widget_option_item_arrow);
        tv_title = (TextView) findViewById(R.id.widget_option_item_title);
        tv_subtitle = (TextView) findViewById(R.id.widget_option_item_subtitle);
        tv_content = (TextView) findViewById(R.id.widget_option_item_content);
        fl_custom = (FrameLayout) findViewById(R.id.widget_option_item_custom);
        iv_arrow.setImageResource(default_arrow_res);
//        if (!isInEditMode()) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AngelOptionItem);
        int color_icon_back = a.getColor(R.styleable.AngelOptionItem_aoi_icon_back, getResources().getColor(default_icon_back_res));
//
//        int color_icon_back = a.getResourceId(R.styleable.AngelOptionItem_aoi_icon_back, -1);
//        if (color_icon_back == -1) {
//            color_icon_back = default_icon_back_res;
//        }
        int color_text = a.getColor(R.styleable.AngelOptionItem_aoi_text_color, getResources().getColor(default_text_color));
        int color_text_content = a.getColor(R.styleable.AngelOptionItem_aoi_text_color_content, getResources().getColor(default_text_color));
        int radius = a.getDimensionPixelSize(R.styleable.AngelOptionItem_aoi_icon_radius, Util.getPX(getContext(), default_icon_radius));
//        if (radius == -1) {
//            radius = default_icon_radius;
//        }
        int size = a.getDimensionPixelSize(R.styleable.AngelOptionItem_aoi_title_text_size, Util.getPX(getContext(), 14));
        tv_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        int spacing = a.getDimensionPixelSize(R.styleable.AngelOptionItem_aoi_icon_title_spacing, -1);
        if (spacing != -1) {
            LayoutParams params = (LayoutParams) iv_icon.getLayoutParams();
            params.rightMargin = spacing;
        }
        tv_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        size = a.getDimensionPixelSize(R.styleable.AngelOptionItem_aoi_subtitle_text_size, Util.getPX(getContext(), 14));
        tv_subtitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        size = a.getDimensionPixelSize(R.styleable.AngelOptionItem_aoi_content_text_size, Util.getPX(getContext(), 12));
        tv_content.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        tv_title.setTextColor(color_text);
        tv_subtitle.setTextColor(color_text);
        tv_content.setTextColor(color_text_content);
        size = a.getDimensionPixelSize(R.styleable.AngelOptionItem_aoi_icon_size, Util.getPX(getContext(), 18));
        LayoutParams params = (LayoutParams) iv_icon.getLayoutParams();
        params.width = params.height = size;
        boolean circle = a.getBoolean(R.styleable.AngelOptionItem_aoi_icon_as_circle, false);
        if (circle) {
            radius = Util.getPX(getContext(), 12);
        }
        int padding = a.getDimensionPixelSize(R.styleable.AngelOptionItem_aoi_icon_padding, Util.getPX(getContext(), default_icon_padding));
//        if (padding == -1) {
//            if (isInEditMode()) {
//                padding = default_icon_padding;
//            } else {
//                padding = Util.getPX(getContext(),default_icon_padding);

//            }
//        }
        int res_icon = a.getResourceId(R.styleable.AngelOptionItem_aoi_icon_src, -1);
        if (res_icon == -1) {
            res_icon = R.color.white;
        }
//        if (!isInEditMode()) {
//        }
        setIcon(res_icon, color_icon_back, radius, padding);
        String title = a.getString(R.styleable.AngelOptionItem_aoi_title);
        if (title != null) {
            tv_title.setText(title);
        }
        title = a.getString(R.styleable.AngelOptionItem_aoi_subtitle);
        if (title != null) {
            tv_subtitle.setText(title);
            tv_subtitle.setVisibility(VISIBLE);
        }
        title = a.getString(R.styleable.AngelOptionItem_aoi_content);
        if (title != null) {
            tv_content.setText(title);
            tv_content.setVisibility(VISIBLE);
        }
        boolean b = a.getBoolean(R.styleable.AngelOptionItem_aoi_arrow_visible, true);
        boolean c = a.getBoolean(R.styleable.AngelOptionItem_aoi_arrow_gone, false);
        if (c) {
            iv_arrow.setVisibility(GONE);
        } else {
            if (!b) {
                iv_arrow.setVisibility(INVISIBLE);
            }
        }

        b = a.getBoolean(R.styleable.AngelOptionItem_aoi_icon_visible, true);
        iv_icon.setVisibility(b ? View.VISIBLE : View.GONE);
        a.recycle();
//        }

//        setBackgroundColor(getResources().getColor(default_back));

        int px = Util.getPX(getContext(), 16);
        if (getPaddingLeft() == 0 && getPaddingTop() == 0 && getPaddingRight() == 0 && getPaddingBottom() == 0) {
            setPadding(px, 0, px, 0);
        }
        px = Util.getPX(getContext(), 48);
        if (ViewCompat.getMinimumHeight(this) == 0) {
            setMinimumHeight(px);
        }
//        if (isInEditMode()) {
//        } else {
//            if (getPaddingLeft() == 0 && getPaddingTop() == 0 && getPaddingRight() == 0 && getPaddingBottom() == 0) {
//                int px = Util.getPX(16);
//                setPadding(px, 0, px, 0);
//            }
//            if (ViewCompat.getMinimumHeight(this) == 0) {
//                setMinimumHeight(Util.getPX(48));
//            }
//        }
    }

    public TextView getContentView() {
        return tv_content;
    }

    public String getContent() {
        return tv_content.getText().toString();
    }

    public String getTitle() {
        return tv_title.getText().toString();
    }

    public String getSubtitle() {
        return tv_subtitle.getText().toString();
    }

    public void setCustomView(View v) {
        fl_custom.setVisibility(View.VISIBLE);
        fl_custom.removeAllViews();
        fl_custom.addView(v);
    }

    public ImageView getIcon() {
        return iv_icon;
    }

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setIcon(int res_icon, int color, int round_corner_radius, int padding) {
        ShapeDrawable back = ShapeProvider.getRoundRect(round_corner_radius, color);
        SelectorProvider.setBackgroundFor(iv_icon, back);
        iv_icon.setPadding(padding, padding, padding, padding);
        iv_icon.setImageResource(res_icon);
    }

    public void setIcon(int res_icon, int res_color, int padding) {
        setIcon(res_icon, res_color, Util.getPX(default_icon_padding), padding);
    }

    public void setTitle(String title) {
        tv_title.setText(title);
    }

    public void setSubtitle(String subtitle) {
        tv_subtitle.setText(subtitle);
        setSubtitleVisible(true);
    }

    public void setArrowResource(int res_arrow) {
        iv_arrow.setImageResource(res_arrow);
    }

    public void setContent(String content) {
        tv_content.setText(content);
    }

    public void setContentColor(int color) {
        tv_content.setTextColor(color);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);
    }

    public void setArrowVisible(boolean visible) {
        iv_arrow.setVisibility(visible ? VISIBLE : GONE);
    }

    public void setIconVisible(boolean visible) {
        iv_icon.setVisibility(visible ? VISIBLE : GONE);
    }

    public void setSubtitleVisible(boolean visible) {
        tv_subtitle.setVisibility(visible ? VISIBLE : GONE);
    }

    public static int getDefaultArrowRes() {
        return default_arrow_res;
    }

    public static void setDefaultArrowRes(int default_arrow_res) {
        AngelOptionItem.default_arrow_res = default_arrow_res;
    }

    public static int getDefaultIconBackRes() {
        return default_icon_back_res;
    }

    public static void setDefaultIconBackRes(int default_icon_back_res) {
        AngelOptionItem.default_icon_back_res = default_icon_back_res;
    }

    public static int getDefaultIconRadius() {
        return default_icon_radius;
    }

    public static void setDefaultIconRadius(int default_icon_radius) {
        AngelOptionItem.default_icon_radius = default_icon_radius;
    }

    public static int getDefaultIconPadding() {
        return default_icon_padding;
    }

    public static void setDefaultIconPadding(int default_icon_padding) {
        AngelOptionItem.default_icon_padding = default_icon_padding;
    }
}
