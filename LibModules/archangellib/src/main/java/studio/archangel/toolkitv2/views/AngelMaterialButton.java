package studio.archangel.toolkitv2.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Field;

import studio.archangel.toolkitv2.R;
import studio.archangel.toolkitv2.util.Util;

/**
 * Created by Michael on 2015/5/11.
 */
public class AngelMaterialButton extends AngelMaterialRelativeLayout {
    final static String android_name_space = "http://schemas.android.com/apk/res/android";
    static int default_text_size = 16;
    static int default_padding_horizontal = 16;
    static int default_padding_vertical = 2;
    static int default_min_width = 72;
    static int default_min_height = 36;
    TextView tv_text;
    private String name;

    public AngelMaterialButton(Context context) {
        this(context, null);
    }

    public AngelMaterialButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AngelMaterialButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        tv_text = new TextView(context, null, R.style.AngelTheme_TextViewTheme);

        tv_text.setBackgroundResource(R.color.trans);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AngelMaterialButton);
        String text = a.getString(R.styleable.AngelMaterialButton_amb_text);
        if (text != null) {
            tv_text.setText(text);
            name = text;
        }
        int color = a.getColor(R.styleable.AngelMaterialButton_amb_text_color, R.color.text_white);
        tv_text.setTextColor(color);
        int text_style = a.getInt(R.styleable.AngelMaterialButton_amb_text_style, 0);
        tv_text.setTypeface(tv_text.getTypeface(), text_style);
        int gravity = a.getInt(R.styleable.AngelMaterialButton_amb_text_gravity, 1);
        RelativeLayout.LayoutParams param = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        param.addRule(CENTER_VERTICAL, TRUE);
        switch (gravity) {
            case 2: {//left
                param.addRule(ALIGN_PARENT_LEFT, TRUE);
                break;
            }
            case 3: {//right
                param.addRule(ALIGN_PARENT_RIGHT, TRUE);
                break;
            }
            case 1:
            default: {
                param.addRule(CENTER_IN_PARENT, TRUE);
            }
        }
        tv_text.setLayoutParams(param);
//        if (!isInEditMode()) {
        int text_size = a.getDimensionPixelSize(R.styleable.AngelMaterialButton_amb_text_size, Util.getPXfromSP(getContext(), default_text_size));
        tv_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, text_size);
//            setDefaultPadding(attrs);
//            setMinSize(attrs);


//            setPadding(Util.getPX(default_padding_horizontal), Util.getPX(default_padding_vertical), Util.getPX(default_padding_horizontal), Util.getPX(default_padding_vertical));
//            setMinimumWidth(Util.getPX(default_min_width));
//            setMinimumHeight(Util.getPX(default_min_height));
        setPaddingAndMinSize(attrs);
//        }
        a.recycle();

        addView(tv_text);

    }

    void setPaddingAndMinSize(AttributeSet attrs) {
        if (attrs == null) {
            setPadding(
                    Util.getPX(getContext(), default_padding_horizontal),
                    Util.getPX(getContext(), default_padding_vertical),
                    Util.getPX(getContext(), default_padding_horizontal),
                    Util.getPX(getContext(), default_padding_vertical));
            return;
        }
        String space = "http://schemas.android.com/apk/res/android";
        int w = 0;
        int h = 0;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            try {
                Field field = getClass().getDeclaredField("mMinWidth");
                field.setAccessible(true);
                w = (int) field.get(this);
                field = getClass().getDeclaredField("mMinHeight");
                field.setAccessible(true);
                h = (int) field.get(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            w = getMinimumWidth();
            h = getMinimumHeight();
        }
        boolean has_left = attrs.getAttributeValue(space, "paddingLeft") != null;
        boolean has_top = attrs.getAttributeValue(space, "paddingTop") != null;
        boolean has_right = attrs.getAttributeValue(space, "paddingRight") != null;
        boolean has_bottom = attrs.getAttributeValue(space, "paddingBottom") != null;
        boolean has_min_width = attrs.getAttributeValue(space, "minWidth") != null;
        boolean has_min_height = attrs.getAttributeValue(space, "minHeight") != null;
        setMinimumWidth(has_min_width ? w : Util.getPX(getContext(), default_min_width));
        setMinimumHeight(has_min_height ? h : Util.getPX(getContext(), default_min_height));
        int padding_left = getPaddingLeft();
        int padding_top = getPaddingTop();
        int padding_right = getPaddingRight();
        int padding_bottom = getPaddingBottom();
        setPadding(
                has_left ? padding_left : Util.getPX(getContext(), default_padding_horizontal),
                has_top ? padding_top : Util.getPX(getContext(), default_padding_vertical),
                has_right ? padding_right : Util.getPX(getContext(), default_padding_horizontal),
                has_bottom ? padding_bottom : Util.getPX(getContext(), default_padding_vertical));

    }

//    protected void setDefaultPadding(AttributeSet attrs) {
////        String padding_text = attrs.getAttributeValue(ANDROIDXML, "padding");
//        float padding = fixValue(attrs.getAttributeValue(android_name_space, "padding"));
////        int padding = attrs.getAttributeIntValue(ANDROIDXML, "padding", -1);
//        float paddingLeft;
//        float paddingTop;
//        float paddingRight;
//        float paddingBottom;
//        if (padding == -1) {
//
//            paddingLeft = fixValue(attrs.getAttributeValue(android_name_space, "paddingLeft"));
//            paddingTop = fixValue(attrs.getAttributeValue(android_name_space, "paddingTop"));
//            paddingRight = fixValue(attrs.getAttributeValue(android_name_space, "paddingRight"));
//            paddingBottom = fixValue(attrs.getAttributeValue(android_name_space, "paddingBottom"));
//            paddingLeft = (paddingLeft == -1) ? 16 : paddingLeft;
//            paddingTop = (paddingTop == -1) ? 2 : paddingTop;
//            paddingRight = (paddingRight == -1) ? 16 : paddingRight;
//            paddingBottom = (paddingBottom == -1) ? 2 : paddingBottom;
//        } else {
//            paddingLeft = paddingTop = paddingRight = paddingBottom = padding;
//        }
//        setPadding(Util.getPX(paddingLeft), Util.getPX(paddingTop), Util.getPX(paddingRight), Util.getPX(paddingBottom));
//
//    }
//
//    protected void setMinSize(AttributeSet attrs) {
//        int width = (int) fixValue(attrs.getAttributeValue(android_name_space, "minWidth"));
//        int height = (int) fixValue(attrs.getAttributeValue(android_name_space, "minHeight"));
//
//        setMinimumWidth(Util.getPX(width == -1 ? default_min_width : width));
//        setMinimumHeight(Util.getPX(height == -1 ? default_min_height : height));
//    }

    float fixValue(String value_with_unit) {
        if (value_with_unit == null) {
            return -1;
        }
        float value = -1;
        value_with_unit = value_with_unit.replace("dp", "");
        value_with_unit = value_with_unit.replace("dip", "");
        value_with_unit = value_with_unit.replace("sp", "");
        try {
            value = Float.parseFloat(value_with_unit);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return value;
    }

    public void setTextColor(int color) {
        tv_text.setTextColor(color);
    }

    public void setText(String text) {
        tv_text.setText(text);
    }

    public TextView getTextView() {
        return tv_text;
    }

    public String getText() {
        return tv_text.getText().toString();
    }

    public void setTextSize(int size) {
        tv_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    public static void setDefaultTextSize(int default_text_size) {
        AngelMaterialButton.default_text_size = default_text_size;
    }

    public static void setDefaultPaddingHorizontal(int default_padding_horizontal) {
        AngelMaterialButton.default_padding_horizontal = default_padding_horizontal;
    }

    public static void setDefaultPaddingVertical(int default_padding_vertical) {
        AngelMaterialButton.default_padding_vertical = default_padding_vertical;
    }

    public static void setDefaultMinWidth(int default_min_width) {
        AngelMaterialButton.default_min_width = default_min_width;
    }

    public static void setDefaultMinHeight(int default_min_height) {
        AngelMaterialButton.default_min_height = default_min_height;
    }
}
