package studio.archangel.toolkitv2.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.badoo.mobile.util.WeakHandler;

import studio.archangel.toolkitv2.R;
import studio.archangel.toolkitv2.util.Util;

public class AngelMaterialLinearLayout extends LinearLayout {
    static int default_border_radius = 4;
    private AngelMaterialProperties.EffectMode effect_mode;
    private AngelMaterialProperties.TriggerMode trigger_mode;
    private AngelMaterialProperties.BorderStyle border_style;
    private AngelMaterialProperties.FillStyle fill_style;
    private OnClickListener listener;
    protected boolean draw_effect = false;

    private long duration_total = 100;
    private long duration_ripple = (long) (duration_total * 2.0 / 3);
    private long duration_alpha = (long) (duration_total * 1.0 / 3);
    private long time_start = -1;

    private float radius;
    private int frame_max;

    private float x = -1, y = -1;

    protected int color_normal;
    protected int color_pressed;

    protected Paint paint = null;
    protected Rect src = new Rect();
    protected Rect dst = new Rect();

    GradientDrawable shape_background;
    GradientDrawable shape_foreground;
    int color_foreground;
    int color_background;
    Bitmap canvas_bitmap;
    boolean clicked = false;

    public AngelMaterialLinearLayout(Context context) {
        this(context, null);
    }

    public AngelMaterialLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AngelMaterialLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        int padding_left = getPaddingLeft();
//        int padding_right = getPaddingRight();
//        int padding_top = getPaddingTop();
//        int padding_bottom = getPaddingBottom();
        Util.setBackgroundResourceWithOriginPadding(this, R.drawable.layer_angel_material_layout);
        /*Since padding property will be remove after set resource, we must save it manually and then reset them...*/
//        setBackgroundResource(R.drawable.layer_angel_material_layout);
//        setPadding(padding_left, padding_top, padding_right, padding_bottom);
        try {
            LayerDrawable layer = (LayerDrawable) getBackground();
            shape_background = (GradientDrawable) layer.findDrawableByLayerId(R.id.layer_angel_material_layout_background);
            shape_foreground = (GradientDrawable) layer.findDrawableByLayerId(R.id.layer_angel_material_layout_foreground);
            shape_background.mutate();
            shape_foreground.mutate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AngelMaterialLinearLayout);
        color_background = a.getColor(R.styleable.AngelMaterialLinearLayout_aml_color_background, R.color.blue);
        color_foreground = a.getColor(R.styleable.AngelMaterialLinearLayout_aml_color_foreground, R.color.white);
        setShapeBackgroundColor(color_background);
        setShapeForegroundColor(color_foreground);
        int index = a.getInt(R.styleable.AngelMaterialLinearLayout_aml_mode, 0);
        setEffectMode(AngelMaterialProperties.effect_mode_array[index]);
        index = a.getInt(R.styleable.AngelMaterialLinearLayout_aml_trigger_mode, 1);
        setTriggerMode(AngelMaterialProperties.trigger_mode_array[index]);
        index = a.getInt(R.styleable.AngelMaterialLinearLayout_aml_fill, 0);
        setFillStyle(AngelMaterialProperties.fill_style_array[index]);
        index = a.getInt(R.styleable.AngelMaterialLinearLayout_aml_border, 0);
        int radius = a.getDimensionPixelSize(R.styleable.AngelMaterialLinearLayout_aml_border_radius, isInEditMode() ? 4 : Util.getPX(4));
        setBorderStyle(AngelMaterialProperties.border_style_array[index], radius);
        a.recycle();
        setClickable(true);
    }

    public void setEffectMode(AngelMaterialProperties.EffectMode mode) {
        if (this.effect_mode != mode) {
            this.effect_mode = mode;
            if (fill_style == AngelMaterialProperties.FillStyle.fill) {
                generateColors(color_background);
            } else if (fill_style == AngelMaterialProperties.FillStyle.border) {
                generateColors(color_foreground);
            }
        }
    }

    public void setTriggerMode(AngelMaterialProperties.TriggerMode mode) {
        if (this.trigger_mode != mode) {
            this.trigger_mode = mode;
        }
    }

    public void setBorderStyle(AngelMaterialProperties.BorderStyle style) {
        setBorderStyle(style, Util.getPX(default_border_radius));
    }

    public void setBorderStyle(AngelMaterialProperties.BorderStyle style, float radius) {
        if (this.border_style != style) {
            this.border_style = style;
            if (style == AngelMaterialProperties.BorderStyle.flat) {
                setCornerRadius(0);
            } else if (style == AngelMaterialProperties.BorderStyle.round_corner) {
                setCornerRadius(radius);
            } else if (style == AngelMaterialProperties.BorderStyle.round) {
                int h = getHeight();
                if (h <= 0) {
                    return;
                }
                setCornerRadius(h / 2);
            }
        }
    }

    public void setFillStyle(AngelMaterialProperties.FillStyle style) {
        if (this.fill_style != style) {
            this.fill_style = style;
            if (style == AngelMaterialProperties.FillStyle.fill) {
                generateColors(color_background);
                shape_background.setColor(color_normal);
                shape_foreground.setColor(getResources().getColor(R.color.trans));
            } else if (style == AngelMaterialProperties.FillStyle.border) {
                generateColors(color_foreground);
                shape_foreground.setColor(color_normal);
            }
            generateCanvas();
        }
    }

    @Override
    public void setBackgroundColor(int color) {
        setShapeBackgroundColor(color);
    }

    public void setShapeBackgroundColor(int color) {
        color_background = color;
        if (fill_style == AngelMaterialProperties.FillStyle.fill) {
            shape_foreground.setColor(color_background);
            generateColors(color_background);
        }
        shape_background.setColor(color_background);

    }

    public void setShapeForegroundColor(int color) {
        color_foreground = color;
        if (fill_style == AngelMaterialProperties.FillStyle.border) {
            generateColors(color_foreground);
        }
        shape_foreground.setColor(color_foreground);
    }

    private void setCornerRadius(float r) {
        shape_background.setCornerRadius(r);
        if (isInEditMode()) {
            shape_foreground.setCornerRadius(Math.max(r - 1, 0));
        } else {
            shape_foreground.setCornerRadius(Math.max(r - Util.getPX(1), 0));
        }
//        Logger.out("radius:" + r + "," + Math.max(r - Util.getPX(1), 0));
        generateCanvas();
    }

    public Bitmap getFrameBitmap(int frame) {
        Bitmap bitmap;
//        Logger.out(frame + "/" + frame_max);
        bitmap = makeBitmap(frame);
        return bitmap;
    }

    private Bitmap makeBitmap(int frame) {
        Bitmap output = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        canvas.drawARGB(0, 0, 0, 0);
        radius = (float) getRippleSize(frame, frame_max, Util.getDistance(new Point(0, 0), new Point(getWidth(), getHeight())) / 3);
        int color = getBgColor(frame, frame_max, color_normal, color_pressed);
//        Logger.out("("+Color.alpha(color)+","+Color.red(color)+","+Color.green(color)+","+Color.blue(color)+")");
        canvas.drawColor(color);
        if (frame < frame_max / 2) {
            canvas.drawCircle(x, y, radius, paint);
        }

        if (canvas_bitmap == null) {
            generateCanvas();
        }
        if (canvas_bitmap != null) {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            canvas.drawBitmap(canvas_bitmap, 0, 0, paint);
            paint.setXfermode(null);
        }
        return output;
    }

    protected void generateCanvas() {
        GradientDrawable canvas_drawable = null;
        Rect rect = null;
        if (fill_style == AngelMaterialProperties.FillStyle.fill) {
            canvas_drawable = (GradientDrawable) shape_background.getConstantState().newDrawable();
            rect = shape_background.getBounds();
        } else if (fill_style == AngelMaterialProperties.FillStyle.border) {
            canvas_drawable = (GradientDrawable) shape_foreground.getConstantState().newDrawable();
            rect = shape_foreground.getBounds();
        }

        if (canvas_drawable == null || getWidth() == 0 || getHeight() == 0) {
            return;
        }

        canvas_drawable.setAlpha(255);
        canvas_drawable.setColor(R.color.black);
        canvas_bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        canvas_drawable.setBounds(rect.left, rect.top, rect.right, rect.bottom);
        Canvas canvas_mask = new Canvas(canvas_bitmap);
        canvas_drawable.draw(canvas_mask);
    }

    WeakHandler handler_click = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            System.gc();
            if (listener != null)
                listener.onClick(AngelMaterialLinearLayout.this);
            return false;
        }
    });

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode()) {
            return;
        }
        if (draw_effect) {
            long moment = System.currentTimeMillis() - time_start;
            int frame = getFrameIndex(moment);
            Bitmap bitmap = getFrameBitmap(frame);
            canvas.drawBitmap(bitmap, src, dst, null);
//            if (frame == frame_max) {
//            if (frame >= frame_max * 0.4 && !clicked) {
//
//                clicked = true;
//                handler_click.sendEmptyMessage(0);
//            }
//            if (frame == frame_max) {
//                draw_effect = false;
//                clicked = false;
//            }
            if (frame == frame_max) {
                draw_effect = false;
                if (trigger_mode == AngelMaterialProperties.TriggerMode.end) {
                    handler_click.sendEmptyMessage(0);
                }
            }
            invalidate();
        }
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        if (isInEditMode()) {
            return;
        }
        if (!gainFocus) {
            x = -1;
            y = -1;
        }
    }

    public void animateRipple() {
        x = getWidth() / 2f;
        y = getHeight() / 2f;
        draw_effect = true;
        time_start = System.currentTimeMillis();
        invalidate();
    }

    @Override
    public boolean performClick() {
        if (isInEditMode()) {
            return false;
        }
        draw_effect = true;
        if (trigger_mode == AngelMaterialProperties.TriggerMode.start) {
            handler_click.sendEmptyMessage(0);
        }
        time_start = System.currentTimeMillis();
        invalidate();
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isInEditMode()) {
            return false;
        }
        if (!isClickable()) {
            return false;
        }
        if (isEnabled()) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if ((event.getX() <= getWidth() && event.getX() >= 0) && (event.getY() <= getHeight() && event.getY() >= 0)) {
                    int rippleSize = 3;
                    radius = getHeight() / rippleSize;
                    x = event.getX();
                    y = event.getY();
                    return performClick();
                } else {
                    x = -1;
                    y = -1;
                }

            }
        } else {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (listener != null)
                    listener.onClick(AngelMaterialLinearLayout.this);
                return listener != null;
            }

        }

        return true;
    }


    /**
     * 获取某一瞬间对应的Bitmap的index
     *
     * @param moment 时间点
     * @return 对应的帧的index
     */
    protected int getFrameIndex(long moment) {
        if (moment < 0) {
            return 0;
        } else if (moment > duration_total) {
            return frame_max;
        } else {
            return (int) (frame_max * 1.0 * moment / duration_total);
        }
    }

    protected int getPressedColor(int color_normal, AngelMaterialProperties.EffectMode mode) {
        if (mode == AngelMaterialProperties.EffectMode.dark) {
            return Util.getDarkerColor(color_normal);
        } else if (mode == AngelMaterialProperties.EffectMode.light) {
            return Util.getLighterColor(color_normal);
        } else {
            return color_normal;
        }
    }

    public AngelMaterialProperties.EffectMode getEffectMode() {
        return effect_mode;
    }

    public AngelMaterialProperties.BorderStyle getBorderStyle() {
        return border_style;
    }

    public AngelMaterialProperties.FillStyle getFillStyle() {
        return fill_style;
    }

    public void generateColors(int color) {
        if (paint == null) {
            return;
        }
        color_normal = color;
        color_pressed = getPressedColor(color_normal, effect_mode);
        paint.setColor(color_pressed);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isInEditMode()) {
            return;
        }

        src.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
        dst.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        float fps = display.getRefreshRate();
        float frames_ripple = (float) (fps * duration_ripple / 1000.0);
        float frames_alpha = (float) (fps * duration_alpha / 1000.0);
        frame_max = (int) (frames_ripple + frames_alpha);

        if (border_style == AngelMaterialProperties.BorderStyle.round) {
            setCornerRadius(getMeasuredHeight() / 2);
        }
        if (canvas_bitmap != null) {
            canvas_bitmap.recycle();
            canvas_bitmap = null;
        }
    }

    public void setEffectDuration(long d) {
        duration_total = d;
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        listener = l;
    }

    private static double getRippleSize(int f, int fn, double max) {
        if (f > fn / 2) {
            return max;
        }
        double a = Math.PI / fn;
        return max * Math.sin(a * f);
    }

    private static int getBgColor(int f, int fn, int c0, int cn) {
        int a0 = Color.alpha(c0);
        int r0 = Color.red(c0);
        int g0 = Color.green(c0);
        int b0 = Color.blue(c0);
        int an = Color.alpha(cn);
        int rn = Color.red(cn);
        int gn = Color.green(cn);
        int bn = Color.blue(cn);
        int ax = getColor(f, fn, a0, an);
        int rx = getColor(f, fn, r0, rn);
        int gx = getColor(f, fn, g0, gn);
        int bx = getColor(f, fn, b0, bn);
//        Logger.out("(" + ax + "," + rx + "," + gx + "," + bx + ")");
        int result = Color.argb(ax, rx, gx, bx);
        return result;
    }

    private static int getColor(int f, int fn, int c0, int cn) {
        double a = cn - c0;
        double b = Math.PI / fn;
        double c = c0;
        double result = a * Math.sin(b * f) + c;
        return (int) result;
    }
}
