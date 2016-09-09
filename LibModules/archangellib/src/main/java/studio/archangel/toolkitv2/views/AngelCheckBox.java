package studio.archangel.toolkitv2.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import studio.archangel.toolkitv2.R;
import studio.archangel.toolkitv2.util.Util;

/**
 * Created by Michael on 2014/11/28.
 */
public class AngelCheckBox extends LinearLayout {
    static int default_color_main = R.color.blue;
    static int default_color_border = R.color.grey;
    static int default_color_tick = R.color.white;
    static int default_color_text = R.color.text_black;
    static int default_color_text_unchecked = R.color.text_grey;
    static int default_text_size = 14;
    static int default_box_size = 18;
    Bitmap tick;
    AngelCheckView box;
    TextView text;
    int color_main;
    int color_border;
    //    int color_tick;
    int color_text;
    int color_text_unchecked;
    boolean is_checked = false;

    OnCheckChangedListener listener;
    AngelMaterialRelativeLayout amr;

    public AngelCheckBox(Context context) {
        this(context, null);
    }

    public AngelCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AngelCheckBox(Context context, AttributeSet attrs, int def) {
        super(context, attrs, def);
        init(attrs);
    }

    void init(AttributeSet attrs) {
        setOrientation(HORIZONTAL);
        amr = new AngelMaterialRelativeLayout(getContext());
        amr.setShapeBackgroundColor(getResources().getColor(R.color.trans));
        if (!isInEditMode()) {
            amr.setBorderStyle(AngelMaterialProperties.BorderStyle.round);
        }
        LayoutParams param = new LayoutParams(Util.getPX(getContext(), 36), Util.getPX(getContext(), 36));
        amr.setLayoutParams(param);
        box = new AngelCheckView(getContext());
        text = new TextView(getContext());

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AngelCheckBox);
        int box_size = a.getDimensionPixelSize(R.styleable.AngelCheckBox_acb_box_size, Util.getPX(getContext(), default_box_size));

        RelativeLayout.LayoutParams param2 = new RelativeLayout.LayoutParams(box_size, box_size);
        param2.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        box.setBoxSize(box_size);
        box.setLayoutParams(param2);
        amr.addView(box);
        addView(amr);

        String text = a.getString(R.styleable.AngelCheckBox_acb_text);
        if (text != null) {
            this.text.setText(text);
        }
        param = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        param.gravity = Gravity.CENTER_VERTICAL;
        this.text.setLayoutParams(param);
        addView(this.text);

        color_main = a.getColor(R.styleable.AngelCheckBox_acb_color_main, getResources().getColor(default_color_main));
        color_border = a.getColor(R.styleable.AngelCheckBox_acb_color_border, getResources().getColor(default_color_border));
//        color_tick = a.getColor(R.styleable.AngelCheckBox_acb_color_tick, getResources().getColor(default_color_tick));
        color_text = a.getColor(R.styleable.AngelCheckBox_acb_color_text, getResources().getColor(default_color_text));
        color_text_unchecked = a.getColor(R.styleable.AngelCheckBox_acb_color_text_unchecked, getResources().getColor(default_color_text_unchecked));


        setChecked(a.getBoolean(R.styleable.AngelCheckBox_acb_checked, false));
        boolean disable_user_click = a.getBoolean(R.styleable.AngelCheckBox_acb_disable_user_click, false);
//        if (!isInEditMode()) {
        int text_size = a.getDimensionPixelSize(R.styleable.AngelCheckBox_acb_text_size, Util.getPXfromSP(getContext(), default_text_size));
        this.text.setTextSize(TypedValue.COMPLEX_UNIT_PX, text_size);
//        }
        amr.setClickable(false);
        a.recycle();
        setBorderColor(color_border);
        setMainColor(color_main);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                amr.animateRipple();

                setChecked(!is_checked, true, true);
            }
        });
        setClickable(!disable_user_click);

    }

    public AngelCheckView getCheckBox() {
        return box;
    }

    public AngelMaterialRelativeLayout getRippleLayout() {
        return amr;
    }

    public static void setDefaultColorMain(int default_color_main) {
        AngelCheckBox.default_color_main = default_color_main;
    }

    public static void setDefaultColorTick(int default_color_tick) {
        AngelCheckBox.default_color_tick = default_color_tick;
    }

    public static void setDefaultColorBorder(int default_color_border) {
        AngelCheckBox.default_color_border = default_color_border;
    }

    public static void setDefaultColorText(int default_color_text) {
        AngelCheckBox.default_color_text = default_color_text;
    }

    public static void setDefaultColorTextUnchecked(int default_color_text_unchecked) {
        AngelCheckBox.default_color_text_unchecked = default_color_text_unchecked;
    }

    public static void setDefaultTextSize(int default_color_text_size) {
        AngelCheckBox.default_text_size = default_color_text_size;
    }

    public void setEnabled(boolean b) {
        box.setEnabled(b);
        if (b && is_checked) {
            text.setTextColor(color_text);
        } else {
            text.setTextColor(color_text_unchecked);
        }

    }

    public boolean isEnabled() {
        return box.isEnabled();
    }

    public boolean isChecked() {
        return is_checked;
    }

    public void setChecked(boolean checked) {
        setChecked(checked, true, true);
    }

    public void setChecked(boolean checked, boolean should_animate, boolean user_action) {
        box.setChecked(checked, should_animate, user_action);
    }

    public void setBorderColor(int color) {
        box.setBorderColor(color);
    }

    public void setMainColor(int color) {
        box.setMainColor(color);
    }

    public void setText(String s) {
        if (s == null || s.isEmpty()) {
            text.setVisibility(View.GONE);
        } else {
            text.setVisibility(View.VISIBLE);
            text.setText(s);
        }
    }

    public String getText() {
        return text.getText().toString();
    }

    public void setOnCheckChangedListener(OnCheckChangedListener listener) {
        this.listener = listener;
    }

    public TextView getTextView() {
        return text;
    }

    public interface OnCheckChangedListener {
        void onCheckChanged(boolean new_status, boolean user_action);
    }

    public class AngelCheckView extends View {
        static final long default_duration = 200;
        static final float rate = 8f;
        float size;
        int color_main, color_border;
        float border_width;
        Paint paint;
        boolean should_update = false;
        long anim_start_time = 0;
        long duration;
        float border_corner;
        RectF rect = new RectF();
        Rect src = new Rect();
        Bitmap buffer;

        public AngelCheckView(Context context) {
            this(context, null);
        }

        public AngelCheckView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public AngelCheckView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStyle(Paint.Style.FILL);
            paint.setFilterBitmap(true);
            duration = default_duration;
//            if (tick == null) {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inPreferQualityOverSpeed = true;
            opts.inSampleSize = 1;
            opts.inDither = true;
            tick = BitmapFactory.decodeResource(getResources(), R.drawable.icon_ok_white_small, opts);

//            }
        }

        @Override
        public void setEnabled(boolean enabled) {
            super.setEnabled(enabled);
            setAlpha(enabled ? 1 : 0.5f);
        }

        void setMainColor(int c) {
            color_main = c;
        }

        void setBorderColor(int c) {
            color_border = c;
        }


        void setBoxSize(float s) {
            size = s;
            border_width = (float) (size * 1.0 / rate);
            border_corner = border_width;
            if (buffer != null && !buffer.isRecycled()) {
                buffer.recycle();
            }
            buffer = Bitmap.createBitmap((int) size, (int) size, Bitmap.Config.ARGB_8888);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (!should_update) {
                long time;
                if (is_checked) {
                    time = duration;
                } else {
                    time = 0;
                }
                updateCanvas(canvas, time);
                return;
            }
            long time = SystemClock.uptimeMillis() - anim_start_time;
            if (is_checked) {

            } else {
                time = duration - time;
            }
            boolean end = false;
            if (time > duration) {
                time = duration;
                end = true;
            } else if (time < 0) {
                time = 0;
                end = true;
            }
            updateCanvas(canvas, time);
            if (end) {
                should_update = false;
            }
            invalidate();
        }

        void updateCanvas(Canvas canvas, long time) {
            Canvas canvas_temp = new Canvas(buffer);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            canvas_temp.drawPaint(paint);
            paint.setXfermode(null);
            paint.setColor(getColor(time, color_border, color_main));
            text.setTextColor(getColor(time, color_text_unchecked, color_text));
            float outer_box_width = getOuterBoxWidth(time);
            rect.top = rect.left = (float) ((size - outer_box_width) / 2.0);
            rect.bottom = rect.right = size - rect.left;
            canvas_temp.drawRoundRect(rect, border_corner, border_corner, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            float inner_box_width = getInnerBoxWidth(time);
            if (inner_box_width > 0) {
                rect.top = rect.left = (float) ((size - inner_box_width) / 2.0);
                rect.bottom = rect.right = size - rect.left;
                canvas_temp.drawRect(rect, paint);
            }
            float tick_canvas_rect_width = getTickRectWidth(time);
            float inner_box_original_width = getInnerBoxOriginalWidth();
            float tick_source_rect_width = tick_canvas_rect_width * tick.getHeight() / inner_box_original_width;
            if (tick_canvas_rect_width > 0) {
                rect.top = (float) ((size - inner_box_original_width) / 2.0);
                rect.bottom = size - rect.top;
                rect.left = (float) ((size - tick_canvas_rect_width) / 2.0);
                rect.right = size - rect.left;
                src.top = 0;
                src.bottom = tick.getHeight();
                src.left = (int) ((tick.getWidth() - tick_source_rect_width) / 2);
                src.right = tick.getWidth() - src.left;
                canvas_temp.drawBitmap(tick, src, rect, paint);
            }
            paint.setXfermode(null);
            canvas.drawBitmap(buffer, 0, 0, paint);
        }

        void setChecked(boolean b, boolean user_action) {
            setChecked(b, true, user_action);
        }

        void setChecked(final boolean check, boolean should_animate, final boolean user_action) {
            if (!isEnabled()) {
                return;
            }
            if (check == is_checked) {
                return;
            }
            is_checked = check;
            if (listener != null) {
                listener.onCheckChanged(check, user_action);
            }
            if (!should_animate) {
                return;
            }
            anim_start_time = SystemClock.uptimeMillis();
            should_update = true;
            invalidate();
        }

        float getOuterBoxWidth(long x) {
            return size - (float) (Math.sin(Math.PI * x * 1.0 / duration) * (size * 1.0 / rate));
        }

        float getInnerBoxWidth(long x) {
            if (x < duration / 2.0) {
                return (float) (Math.cos(Math.PI * x * 1.0 / duration) * getInnerBoxOriginalWidth());
            } else {
                return 0;
            }
        }

        float getTickRectWidth(long x) {
            if (x < duration / 2.0) {
                return 0;
            } else {
                return (float) (getInnerBoxOriginalWidth() - Math.sin(Math.PI * x * 1.0 / duration) * getInnerBoxOriginalWidth());
            }
        }

        float getInnerBoxOriginalWidth() {
            return size - 2 * border_width;
        }

        int getColor(long time, int c0, int c1) {
            int a0 = Color.alpha(c0);
            int r0 = Color.red(c0);
            int g0 = Color.green(c0);
            int b0 = Color.blue(c0);
            int a1 = Color.alpha(c1);
            int r1 = Color.red(c1);
            int g1 = Color.green(c1);
            int b1 = Color.blue(c1);
            return Color.argb(getColorFacet(a0, a1, time), getColorFacet(r0, r1, time), getColorFacet(g0, g1, time), getColorFacet(b0, b1, time));
        }

        int getColorFacet(int c0, int c1, long time) {
            return (int) ((c1 - c0) * 1.0 / duration * time + c0);
        }
    }


}
