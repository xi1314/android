package studio.archangel.toolkitv2.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import studio.archangel.toolkitv2.R;
import studio.archangel.toolkitv2.util.Logger;
import studio.archangel.toolkitv2.util.Util;
import studio.archangel.toolkitv2.util.ui.AnimationProvider;

/**
 * Created by Administrator on 2016/1/6.
 */
public class AngelSwitch extends FrameLayout {
    static float scale_max = 0.1f;
    static int margin_on = 20;
    static int margin_off = 0;
    static float alpha_on = 0.5f;
    static float alpha_off = 0.26f;
    static int color_off_oval = Color.parseColor("#FAFAFA");
    static int color_off_track = Color.parseColor("#000000");
    static int default_color_main = R.color.blue;
    View track;
    int color_main;

    AngelMaterialRelativeLayout background;
    RelativeLayout foreground;
    GradientDrawable drawable_track;
    GradientDrawable drawable_oval;
    boolean is_checked = true;
    boolean is_animating = false;
    RelativeLayout.LayoutParams params;
    private OnCheckChangedListener listener;

    public AngelSwitch(Context context) {
        this(context, null);
    }

    public AngelSwitch(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AngelSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_switch, this, true);
        track = findViewById(R.id.view_switch_track);
        LayerDrawable ld = (LayerDrawable) track.getBackground();
        drawable_track = (GradientDrawable) ld.findDrawableByLayerId(R.id.shape_switch_back_shape);
        drawable_track.mutate();
        background = (AngelMaterialRelativeLayout) findViewById(R.id.view_switch_orb_back);
        foreground = (RelativeLayout) findViewById(R.id.view_switch_orb_fore);
        View v = findViewById(R.id.view_switch_orb_oval);
        ld = (LayerDrawable) v.getBackground();
        drawable_oval = (GradientDrawable) ld.findDrawableByLayerId(R.id.shape_switch_fore_shape);
        drawable_oval.mutate();
        background.setClickable(false);
        params = (RelativeLayout.LayoutParams) background.getLayoutParams();
//        setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                if (!isClickable()) {
////                    return;
////                }
//                setChecked(!is_checked, true);
//            }
//        });
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AngelSwitch);
        setMainColor(a.getColor(R.styleable.AngelSwitch_as_color_main, getResources().getColor(default_color_main)));
        setChecked(a.getBoolean(R.styleable.AngelSwitch_as_checked, false), false);
        setClickable(a.getBoolean(R.styleable.AngelSwitch_as_clickable, true));
        a.recycle();
        track.setAlpha(isChecked() ? alpha_on : alpha_off);

    }

    float x0, y0;
    //    long last_touch_time = 0;
    int last_moving_x = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            x0 = event.getX();
            y0 = event.getY();
//            last_touch_time = SystemClock.uptimeMillis();
        } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
//            if (SystemClock.uptimeMillis() - last_touch_time < 350) {
//                animate(!is_checked);
//                return super.onTouchEvent(event);
//            }
            float xt = event.getX();
            float yt = event.getY();
            if (Util.getDistance(new Point((int) x0, (int) y0), new Point((int) xt, (int) yt)) < Util.getPX(16)) {
                setChecked(!is_checked, true);
                return super.onTouchEvent(event);
            }
//            boolean status = !is_checked;
            int x = (int) (params.leftMargin + xt - x0);
            if (x > Util.getPX(margin_on)) {
                x = Util.getPX(margin_on);
            }
            if (x < Util.getPX(margin_off)) {
                x = Util.getPX(margin_off);
            }
            Logger.out("up x:" + x);
            params.leftMargin = x;
            background.animateRipple();
            is_animating = true;
            if (x > Util.getPX(margin_on + margin_off) / 2 / 2) {
                animate(true, x);
                is_checked = true;
            } else {
                animate(false, x);
                is_checked = false;
            }
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float xt = event.getX();
            boolean status = !is_checked;
            int x1 = Util.getPX(status ? margin_off : margin_on);
            int x2 = Util.getPX(status ? margin_on : margin_off);
            int color1_oval = status ? color_off_oval : color_main;
            int color2_oval = status ? color_main : color_off_oval;
            int color1_track = status ? color_off_track : color_main;
            int color2_track = status ? color_main : color_off_track;
            int x = (int) (params.leftMargin + xt - x0);
            if (x > Util.getPX(margin_on)) {
                x = Util.getPX(margin_on);
            }
            if (x < Util.getPX(margin_off)) {
                x = Util.getPX(margin_off);
            }
            Logger.out("moving x:" + x);
            if (last_moving_x == x) {
                return super.onTouchEvent(event);
            }
            last_moving_x = x;
            params.leftMargin = x;
            update(!is_checked, x, x1, x2, color1_oval, color2_oval, color1_track, color2_track);
        }
        return super.onTouchEvent(event);
    }

    public void setMainColor(int color) {
        color_main = color;
        int color_oval = is_checked ? color_main : color_off_oval;
        int color_track = is_checked ? color_main : color_off_track;
        drawable_oval.setColor(color_oval);
        drawable_track.setColor(color_track);

    }

    public boolean isChecked() {
        return is_checked;
    }

    /**
     *
     * @param b
     */
    public void setChecked(boolean b) {
        setChecked(b, true);
    }

    public void setChecked(boolean status, boolean user_action) {
        if (user_action) {
            if (is_animating) {
                return;
            }
            background.animateRipple();
            animate(status);
            is_checked = status;
            is_animating = true;
        } else {
            is_checked = status;
            int x = Util.getPX(is_checked ? margin_on : margin_off);
            int color_oval = is_checked ? color_main : color_off_oval;
            int color_track = is_checked ? color_main : color_off_track;
            float alpha = is_checked ? alpha_on : alpha_off;
            params.leftMargin = x;
            foreground.setScaleX(1);
            track.setAlpha(alpha);
            drawable_oval.setColor(color_oval);
            drawable_track.setColor(color_track);
            background.requestLayout();
        }
        if (listener != null) {
            listener.onCheckChanged(is_checked, user_action);
        }
    }

    void animate(final boolean status) {
        animate(status, -1);
    }

    void animate(final boolean status, int x0) {
        final int x1 = Util.getPX(status ? margin_off : margin_on);
        final int x2 = Util.getPX(status ? margin_on : margin_off);
        final int color1_oval = status ? color_off_oval : color_main;
        final int color2_oval = status ? color_main : color_off_oval;
        final int color1_track = status ? color_off_track : color_main;
        final int color2_track = status ? color_main : color_off_track;

        ValueAnimator animator = ObjectAnimator.ofInt(x0 == -1 ? x1 : x0, x2);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(350);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int x = (int) animation.getAnimatedValue();
                params.leftMargin = x;
                update(status, x, x1, x2, color1_oval, color2_oval, color1_track, color2_track);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                is_animating = false;
            }

        });
        animator.start();
    }

    void update(boolean status, int x, int x1, int x2, int color1_oval, int color2_oval, int color1_track, int color2_track) {
        float scale = (float) (1 - Math.sin((x - Util.getPX(Math.min(x1, x2))) * Math.PI / Util.getPX(margin_on - margin_off)) * scale_max);

//        Logger.out("scale:" + scale);

        float a1 = status ? alpha_off : alpha_on;
        float a2 = status ? alpha_on : alpha_off;
        foreground.setScaleX(scale);
        foreground.setScaleY(scale);

        float alpha = (float) AnimationProvider.getAnimatedValueLinear(x1, x2, x, a1, a2);
        int color_oval = AnimationProvider.getAnimatedColorLinear(x1, x2, x, color1_oval, color2_oval);
        int color_track = AnimationProvider.getAnimatedColorLinear(x1, x2, x, color1_track, color2_track);
        track.setAlpha(alpha);
        drawable_oval.setColor(color_oval);
        drawable_track.setColor(color_track);
//                    invalidate();
        background.requestLayout();
    }

    public void setOnCheckChangedListener(OnCheckChangedListener listener) {
        this.listener = listener;
    }

    public interface OnCheckChangedListener {
        void onCheckChanged(boolean new_status, boolean user_action);
    }
}

