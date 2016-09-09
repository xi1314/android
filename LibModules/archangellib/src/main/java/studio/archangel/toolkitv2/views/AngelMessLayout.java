/**
 *
 */
package studio.archangel.toolkitv2.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;

import java.util.ArrayList;

import studio.archangel.toolkitv2.R;
import studio.archangel.toolkitv2.interfaces.Constructable;
import studio.archangel.toolkitv2.util.Logger;
import studio.archangel.toolkitv2.util.Util;

/**
 * @author Administrator
 */
@SuppressWarnings("deprecation")
public class AngelMessLayout extends AbsoluteLayout implements Constructable {
    static int default_spacing_horizontal = 8;
    static int default_spacing_vertical = 8;
    public ArrayList<View> views;
    int spacing_h = 0;
    int spacing_v = 0;
    boolean single_line = false;
    boolean is_vertical = false;
    OnRelayoutListener listener;

    public AngelMessLayout(Context context) {
        this(context, null);
    }

    public AngelMessLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AngelMessLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AngelMessLayout);

        spacing_h = a.getDimensionPixelSize(R.styleable.AngelMessLayout_ame_spacing_horizontal, Util.getPX(context, default_spacing_horizontal));
        spacing_v = a.getDimensionPixelSize(R.styleable.AngelMessLayout_ame_spacing_vertical, Util.getPX(context, default_spacing_vertical));

        single_line = a.getBoolean(R.styleable.AngelMessLayout_ame_single_line, false);
        is_vertical = a.getBoolean(R.styleable.AngelMessLayout_ame_is_vertical, false);
        a.recycle();
        views = new ArrayList<>();
    }

    public boolean isSingleLine() {
        return single_line;
    }

    public void setSingleLine(boolean b) {
        this.single_line = b;
    }

    public boolean isVertical() {
        return is_vertical;
    }

    public void setIsVertical(boolean is_vertical) {
        this.is_vertical = is_vertical;
    }

    public void setData(ArrayList<? extends Object> models, int presetW, int presetH) {
        views.clear();
        removeAllViews();
        if (listener == null) {
            Logger.out("listener not set");
            return;
        }
        int w = presetW;
        if (w == -1) {
            w = this.getWidth();
        }
        int h = presetH;
        if (h == -1) {
            h = this.getHeight();
        }
        int x = 0;
        int y = 0;
        for (int i = 0; i < models.size(); i++) {
            Object m = models.get(i);
            View v = null;
            if (i < views.size()) {
                v = views.get(i);
                if (v == null) {
                    v = listener.onCreateNewLayout(i, m);
                    views.remove(i);
                    views.add(i, v);
                    addView(v, i);
                } else {
                    listener.onRelayout(v, i, m);
                }
            } else {
                v = listener.onCreateNewLayout(i, m);
                views.add(v);
                addView(v);
            }
            v.setVisibility(View.VISIBLE);
        }
        if (models.size() < views.size()) {
            for (int i = models.size(); i < views.size(); i++) {
                views.get(i).setVisibility(View.GONE);
            }
        }
        boolean no_space = false;
        if (!is_vertical) {
            for (View v : views) {
                v.invalidate();
                v.measure(w, h);
                int mw = v.getMeasuredWidth();
                int mh = v.getMeasuredHeight();
                if (x + mw > w - getPaddingRight() - getPaddingLeft()) {
                    if (single_line) {
                        no_space = true;
                    }
                    x = 0;
                    y += mh + spacing_v;
                }
                LayoutParams params = (LayoutParams) v.getLayoutParams();
                params.x = x;
                params.y = y;
                params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                x += mw + spacing_h;
                v.setVisibility(no_space ? View.GONE : View.VISIBLE);
            }
        } else {
            for (View v : views) {
                v.invalidate();
                v.measure(w, h);
                int mw = v.getMeasuredWidth();
                int mh = v.getMeasuredHeight();
                if (y + mh > h - getPaddingBottom() - getPaddingTop()) {
                    if (single_line) {
                        no_space = true;
                    }
                    y = 0;
                    x += mw + spacing_h;
                }
                LayoutParams params = (LayoutParams) v.getLayoutParams();
                params.x = x;
                params.y = y;
                params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                y += mh + spacing_v;
                v.setVisibility(no_space ? View.GONE : View.VISIBLE);
            }
        }

    }

    public void setSpacing(int h, int v) {
        spacing_h = h;
        spacing_v = v;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    public void setValue(Message msg) {
    }

    public OnRelayoutListener getOnRelayoutListener() {
        return listener;
    }

    public void setOnRelayoutListener(OnRelayoutListener listener) {
        this.listener = listener;
    }

    public interface OnRelayoutListener {
        void onRelayout(View view, int index, Object model);

        View onCreateNewLayout(int index, Object model);
    }
}
