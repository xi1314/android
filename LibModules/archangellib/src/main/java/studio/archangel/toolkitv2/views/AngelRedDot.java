package studio.archangel.toolkitv2.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import studio.archangel.toolkitv2.R;
import studio.archangel.toolkitv2.util.Util;

/**
 * Created by Administrator on 2015/12/08.
 */


public class AngelRedDot extends TextView {
    static final int COUNT_MAX = 9;
    AngelMaterialProperties.RedDotStyle style = AngelMaterialProperties.RedDotStyle.text;
    private int count = 0;

    public AngelRedDot(Context context) {
        this(context, null);
    }

    public AngelRedDot(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AngelRedDot(Context context, AttributeSet attrs, int def) {
        super(context, attrs, def);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AngelRedDot);
        int index = a.getInt(R.styleable.AngelRedDot_ard_style, 0);
        setStyle(AngelMaterialProperties.reddot_style_array[index]);
        a.recycle();
        Util.setBackgroundResourceWithOriginPadding(this, R.drawable.shape_oval_red);
        setTextColor(getResources().getColor(R.color.text_white));
        setTextSize(10);
    }

    public void setStyle(AngelMaterialProperties.RedDotStyle s) {
        style = s;
        ViewGroup.LayoutParams params = getLayoutParams();
        if (params != null) {
            params.width = Util.getPX(getContext(), style == AngelMaterialProperties.RedDotStyle.text ? 16 : 8);
            params.height = Util.getPX(getContext(), style == AngelMaterialProperties.RedDotStyle.text ? 16 : 8);
            requestLayout();
        }
    }

    public void setCount(int c) {
        count = c;
        refreshText();
    }

    public void addCount(int c) {
        count += c;
        refreshText();
    }

    void refreshText() {
        String text = count + "";
        if (count > COUNT_MAX) {
            text = COUNT_MAX + "+";
        }
        setDotText(text);
        setVisibility(count > 0 ? View.VISIBLE : View.GONE);
    }

    public void setDotText(String s) {
        super.setText(s);

    }
}
