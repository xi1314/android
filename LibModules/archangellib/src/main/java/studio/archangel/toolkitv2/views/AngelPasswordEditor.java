package studio.archangel.toolkitv2.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.marvinlabs.widget.aspectratio.ConstrainedImageView;

import java.util.ArrayList;

import studio.archangel.toolkitv2.AngelApplication;
import studio.archangel.toolkitv2.R;
import studio.archangel.toolkitv2.util.Util;

/**
 * Created by Michael on 2015/7/1.
 */
public class AngelPasswordEditor extends LinearLayout {
    static final int default_count = 6;
    static final int default_color_background = R.color.white;
    static final int default_color_border = R.color.grey;
    static final int default_color_divider = R.color.divider_normal;
    int count = 6;
    int divider_color;
    int grid_size = 48;
    boolean forced_large_mode = false;
    GradientDrawable shape;
    ArrayList<View> dots;
    StringBuilder builder;
    OnDotAmountChangedListener listener;

    public AngelPasswordEditor(Context context) {
        this(context, null);
    }

    public AngelPasswordEditor(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AngelPasswordEditor(Context context, AttributeSet attrs, int def) {
        super(context, attrs, def);
        setBackgroundResource(R.drawable.layer_angel_password_editor);
        LayerDrawable ld = (LayerDrawable) getBackground();
        shape = (GradientDrawable) ld.findDrawableByLayerId(R.id.layer_angel_password_editor_back);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AngelPasswordEditor);
        int color = a.getColor(R.styleable.AngelPasswordEditor_ape_background_color, getResources().getColor(default_color_border));
        shape.setStroke((isInEditMode() ? 1 : Util.getPX(1)), color);
        color = a.getColor(R.styleable.AngelPasswordEditor_ape_border_color, getResources().getColor(default_color_background));
        shape.setColor(color);
        divider_color = a.getColor(R.styleable.AngelPasswordEditor_ape_divider_color, getResources().getColor(default_color_divider));
        count = a.getInteger(R.styleable.AngelPasswordEditor_ape_count, default_count);
        forced_large_mode = a.getBoolean(R.styleable.AngelPasswordEditor_ape_large_mode, false);
        dots = new ArrayList<>();
        a.recycle();
        if (AngelApplication.screen_width_dp <= 320 && !forced_large_mode) {
            grid_size = 36;
        }
        initCells();
        builder = new StringBuilder(count);

    }

    public OnDotAmountChangedListener getOnDotAmountChangedListener() {
        return listener;
    }

    public void setOnDotAmountChangedListener(OnDotAmountChangedListener listener) {
        this.listener = listener;
    }

    void initCells() {
        removeAllViews();
        for (int i = 0; i < count; i++) {
            if (i != 0) {
                ImageView divider = new ImageView(getContext());
                divider.setBackgroundColor(divider_color);
                addView(divider);
                LinearLayout.LayoutParams p = (LayoutParams) divider.getLayoutParams();
                p.width = 1;
                p.height = ViewGroup.LayoutParams.MATCH_PARENT;
            }
            ConstrainedImageView iv = new ConstrainedImageView(getContext());
            iv.setAspectRatio(1, 1);
            iv.setImageResource(R.drawable.shape_oval_black);
            if (isInEditMode()) {
                iv.setPadding(grid_size / 3, grid_size / 3, grid_size / 3, grid_size / 3);
            } else {
                iv.setPadding(Util.getPX(grid_size / 3), Util.getPX(grid_size / 3), Util.getPX(grid_size / 3), Util.getPX(grid_size / 3));
            }
//            iv.setAlpha(0);
            iv.setScaleX(0);
            iv.setScaleY(0);
//            iv.setVisibility(View.INVISIBLE);
            dots.add(iv);
            addView(iv);
            LinearLayout.LayoutParams p = (LayoutParams) iv.getLayoutParams();
            if (isInEditMode()) {
                p.width = grid_size;
                p.height = grid_size;
            } else {
                p.width = Util.getPX(grid_size);
                p.height = Util.getPX(grid_size);
            }
        }
    }

    public String getText() {
        return builder.toString();
    }

    public void addChar(String character) {
        if (builder.length() >= count) {
            return;
        }
        builder = builder.append(character);
        animateAddDot();
        if (listener != null) {
            listener.onDotAmountChanged(count, builder.length());
        }
//        setText(builder.toString());
    }

    public void deleteLastChar() {
        if (builder.length() < 1) {
            return;
        }
        animateRemoveLastDot();
        builder = builder.delete(builder.length() - 1, builder.length());
        if (listener != null) {
            listener.onDotAmountChanged(count, builder.length());
        }
//        setText(builder.toString());
    }

    public void removeAllChars() {
        animateRemoveAllDot();
        builder = builder.delete(0, builder.length());
        if (listener != null) {
            listener.onDotAmountChanged(count, builder.length());
        }
//        setText(builder.toString());
    }

    public void setText(String t) {
        if (builder.length() >= count) {
            return;
        }
        if (builder.length() + t.length() >= count) {
            t = t.substring(0, count - builder.length() - 1);
        }
        builder = builder.append(t);
        animateAddAllDot();
        if (listener != null) {
            listener.onDotAmountChanged(count, builder.length());
        }
    }
//    void setText(String t) {
//        int length = t.length();
//        setDotAmount(length);
//        if (listener != null) {
//            listener.onDotAmountChanged(count, length);
//        }
//    }

    void animateAddDot() {
        animateDot(dots.get(builder.length() - 1), 1, new OvershootInterpolator());
    }

    void animateAddAllDot() {
        for (int i = 0; i < count; i++) {
            animateDot(dots.get(i), 1, new OvershootInterpolator());
        }
    }

    void animateRemoveLastDot() {
        animateDot(dots.get(builder.length() - 1), 0, new AnticipateInterpolator());

    }

    void animateRemoveAllDot() {
        for (int i = 0; i < count; i++) {
            animateDot(dots.get(i), 0, new AnticipateInterpolator());
        }
    }

    void animateDot(View dot, float scale, Interpolator interpolator) {
        dot.animate().scaleX(scale).scaleY(scale).setDuration(200).setInterpolator(interpolator).start();
    }

//    public void setDotAmount(int amount) {
//        for (int i = 0; i < count; i++) {
//            View dot = dots.GET(i);
//            boolean show = i < amount;
//            float scale = show ? 1 : 0;
//            Interpolator interpolator = null;
//            if (show) {
//                interpolator = new OvershootInterpolator();
//            } else {
//                interpolator = new AnticipateInterpolator();
//            }
//
//            ViewPropertyAnimator animator = dot.animate();
//
//            animator.scaleX(scale).scaleY(scale).setDuration(200).setInterpolator(interpolator).start();
//
////            if (show) {
////                if (dot.getScaleX() != 1) {
////                    dot.animate().scaleX(1).scaleY(1).setDuration(200).setInterpolator(new OvershootInterpolator()).start();
////                }
////            } else {
////                if (dot.getScaleX() != 0) {
////                    dot.animate().scaleX(0).scaleY(0).setDuration(200).setInterpolator(new AnticipateOvershootInterpolator()).start();
////                }
////
////            }
////            dot.setVisibility(i < amount ? View.VISIBLE : View.INVISIBLE);
//        }
//    }

    public interface OnDotAmountChangedListener {
        void onDotAmountChanged(int max, int amount);
    }
}
