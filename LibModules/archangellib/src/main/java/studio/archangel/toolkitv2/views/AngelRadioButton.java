package studio.archangel.toolkitv2.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
import studio.archangel.toolkitv2.util.ui.RadioButtonGroup;

/**
 * Created by Michael on 2014/11/28.
 */
public class AngelRadioButton extends LinearLayout {
    static int default_color_main = R.color.blue;
    static int default_color_border = R.color.grey;
    static int default_color_text = R.color.text_black;
    static int default_color_text_unchecked = R.color.text_grey;
    static int default_text_size = 14;
    static int default_box_size = 20;
    //    static int default_box_border = 1;
//    static long duration = 200;
    AngelRadioView box;
    TextView text;
    int color_main;
    int color_border;
    int color_text;
    int color_text_unchecked;
    boolean is_checked = false;
    RadioButtonGroup group;
    OnCheckChangedListener listener;
    AngelMaterialRelativeLayout amr;

    public AngelRadioButton(Context context) {
        this(context, null);
    }

    public AngelRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AngelRadioButton(Context context, AttributeSet attrs, int def) {
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
        box = new AngelRadioView(getContext());
        text = new TextView(getContext());

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AngelRadioButton);
        int box_size = a.getDimensionPixelSize(R.styleable.AngelRadioButton_arb_box_size, Util.getPX(getContext(), default_box_size));

        RelativeLayout.LayoutParams param2 = new RelativeLayout.LayoutParams(box_size, box_size);
        param2.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        box.setBoxSize(box_size);
        box.setLayoutParams(param2);
        amr.addView(box);
        addView(amr);

        String text = a.getString(R.styleable.AngelRadioButton_arb_text);
        if (text != null) {
            this.text.setText(text);
        }
        param = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        param.gravity = Gravity.CENTER_VERTICAL;
        this.text.setLayoutParams(param);
        addView(this.text);

        color_main = a.getColor(R.styleable.AngelRadioButton_arb_color_main, getResources().getColor(default_color_main));
        color_border = a.getColor(R.styleable.AngelRadioButton_arb_color_border, getResources().getColor(default_color_border));
        color_text = a.getColor(R.styleable.AngelRadioButton_arb_color_text, getResources().getColor(default_color_text));
        color_text_unchecked = a.getColor(R.styleable.AngelRadioButton_arb_color_text_unchecked, getResources().getColor(default_color_text_unchecked));
        setChecked(a.getBoolean(R.styleable.AngelRadioButton_arb_checked, false), false);
        boolean disable_user_click = a.getBoolean(R.styleable.AngelCheckBox_acb_disable_user_click, false);
//        if (!isInEditMode()) {
            int text_size = a.getDimensionPixelSize(R.styleable.AngelRadioButton_arb_text_size, Util.getPXfromSP(getContext(),default_text_size));
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
                if (isChecked()) {
                    return;
                }
                setChecked(!is_checked, true);
            }
        });
        setClickable(!disable_user_click);
    }

    public AngelRadioView getRadioButton() {
        return box;
    }

    public AngelMaterialRelativeLayout getRippleLayout() {
        return amr;
    }

    public void setEnabled(boolean b) {
        box.setEnabled(b);
        if (b && is_checked) {
            text.setTextColor(color_text);
        } else {
            text.setTextColor(color_text_unchecked);
        }

    }

    public static void setDefaultColorMain(int default_color_main) {
        AngelRadioButton.default_color_main = default_color_main;
    }

    public static void setDefaultColorBorder(int default_color_border) {
        AngelRadioButton.default_color_border = default_color_border;
    }

    public static void setDefaultColorText(int default_color_text) {
        AngelRadioButton.default_color_text = default_color_text;
    }

    public static void setDefaultColorTextUnchecked(int default_color_text_unchecked) {
        AngelRadioButton.default_color_text_unchecked = default_color_text_unchecked;
    }

    public static void setDefaultTextSize(int default_text_size) {
        AngelRadioButton.default_text_size = default_text_size;
    }

    public boolean isEnabled() {
        return box.isEnabled();
    }

    public boolean isChecked() {
        return is_checked;
    }

    public RadioButtonGroup getButtonGroup() {
        return group;
    }

    public void setButtonGroup(RadioButtonGroup group) {
        this.group = group;
    }

    public void setChecked(boolean checked) {
        setChecked(checked, false);
    }

    public void setChecked(boolean checked, boolean user_action) {
        box.setChecked(checked, user_action);
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

    public TextView getTextView() {
        return text;
    }

    public void setOnCheckChangedListener(OnCheckChangedListener listener) {
        this.listener = listener;
    }

    public interface OnCheckChangedListener {
        void onCheckChanged(boolean new_status, boolean user_action);
    }

    public class AngelRadioView extends View {
        static final long default_duration = 200;
        static final float rate = 8f;
        float size;
        int color_main, color_border;
        float border_width;
        Paint paint;
        boolean should_update = false;
        long anim_start_time = 0;
        long duration;

        public AngelRadioView(Context context) {
            this(context, null);
        }

        public AngelRadioView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public AngelRadioView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStyle(Paint.Style.STROKE);
            duration = default_duration;
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
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(getColor(time, color_border, color_main));
            text.setTextColor(getColor(time, color_text_unchecked, color_text));
            paint.setStrokeWidth(border_width / 2f);

            float radius = this.size / 2f;
            float r_out = getOuterRadius(time);
            canvas.drawCircle(radius, radius, r_out - border_width / 2f, paint);
            float r_in = getInnerRadius(time);
            if (r_in != -1) {
                float stroke_width = getInnerStrokeWidth(time);
                paint.setStrokeWidth(stroke_width * 2f);
                paint.setStyle(should_update ? Paint.Style.STROKE : Paint.Style.FILL_AND_STROKE);
                canvas.drawCircle(radius, radius, r_in - stroke_width, paint);
            }
        }

        void setChecked(boolean b, boolean user_action) {
            setChecked(b, true, user_action);
        }

        void setChecked(final boolean check, boolean should_animate, final boolean user_action) {
//            Logger.out(AngelRadioButton.this.getTag() + " " + is_checked + "=>" + check);
            if (check == is_checked) {
                return;
            }
            is_checked = check;
            if (listener != null) {
                listener.onCheckChanged(check, user_action);
            }
            if (group != null && user_action) {
                group.refreshButtonStatus(AngelRadioButton.this);
            }
            if (!should_animate) {
                return;
            }
            anim_start_time = SystemClock.uptimeMillis();
            should_update = true;
            invalidate();
        }

        float getOuterRadius(long time) {
            float radius = this.size / 2f;
            return (float) (-radius / (rate / 2) * Math.sin(Math.PI * 1.0 / duration * time) + radius);
        }

        float getInnerRadius(long time) {
            if (time < duration / 2f) {
                return -1;
            } else {
                float radius = this.size / 2f;
                return (float) (-radius / (rate) * Math.sin(Math.PI * 1.0 / duration * time - Math.PI / 2) + radius - radius / (rate / 2));
            }
        }

        float getInnerStrokeWidth(long time) {
            if (time < duration / 2f) {
                return -1;
            } else {
                float radius = this.size / 2f;
                return radius / (rate / 4) / duration * time - radius / rate;
            }
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
