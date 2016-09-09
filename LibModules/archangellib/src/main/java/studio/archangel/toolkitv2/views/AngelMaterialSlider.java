package studio.archangel.toolkitv2.views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import studio.archangel.toolkitv2.R;
import studio.archangel.toolkitv2.util.Util;

public class AngelMaterialSlider extends AngelMaterialRelativeLayout {
    final static String MATERIALDESIGNXML = "http://schemas.android.com/apk/res-auto";

    final static String ANDROIDXML = "http://schemas.android.com/apk/res/android";
    // Event when slider change value
    public interface OnValueChangedListener {
        public void onValueChanged(int value);
    }

    int backgroundColor = Color.parseColor("#4CAF50");
    int secondaryColor = Color.parseColor("#4CAF50");
    Ball ball;
    NumberIndicator numberIndicator;

    boolean showNumberIndicator = false;
    boolean press = false;
    boolean showBuffer = false;
    boolean zoomed = false;
    int value = 0;
    int buffer;
    int max = 100;
    int min = 0;
    int bar_height;
    int pressed_radius;
    int disable_color = Color.parseColor("#B0B0B0");
    OnValueChangedListener onValueChangedListener;

    public AngelMaterialSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttributes(attrs);
        secondaryColor = makeSecondaryColor(disable_color);
        bar_height = Util.getPX(2);
    }

    public void setBufferColor(int res_id) {
        secondaryColor = makeSecondaryColor(res_id);
    }

    public void setShowBuffer(boolean b) {
        showBuffer = b;
    }

    public void setBuffer(int buffer) {
        this.buffer = buffer;
    }

    // Set atributtes of XML to View
    protected void setAttributes(AttributeSet attrs) {

        setBackgroundResource(R.drawable.background_transparent);

        // Set size of view
        setMinimumHeight(Util.getPX(48));
        setMinimumWidth(Util.getPX(80));

        // Set background Color
        // Color by resource
        int bacgroundColor = attrs.getAttributeResourceValue(ANDROIDXML,
                "background", -1);
        if (bacgroundColor != -1) {
            setBackgroundColor(getResources().getColor(bacgroundColor));
        } else {
            // Color by hexadecimal
            int background = attrs.getAttributeIntValue(ANDROIDXML, "background", -1);
            if (background != -1)
                setBackgroundColor(background);
        }

        showNumberIndicator = attrs.getAttributeBooleanValue(MATERIALDESIGNXML,
                "showNumberIndicator", false);
        min = attrs.getAttributeIntValue(MATERIALDESIGNXML, "min", 0);
        max = attrs.getAttributeIntValue(MATERIALDESIGNXML, "max", 0);
        value = attrs.getAttributeIntValue(MATERIALDESIGNXML, "value", min);

        ball = new Ball(getContext());
        LayoutParams params = new LayoutParams(Util.getPX(16), Util.getPX(16));
        params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        ball.setLayoutParams(params);
        addView(ball);

        // Set if slider content number indicator
        // TODO
        if (showNumberIndicator) {
            numberIndicator = new NumberIndicator(getContext());
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!placedBall)
            placeBall();
//        Logger.out(value);
        //back
        Bitmap bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas temp = new Canvas(bitmap);
//        Canvas temp = canvas;
        Paint paint = new Paint();
        temp.drawARGB(0, 0, 0, 0);
        paint.setColor(disable_color);
        temp.drawRect(pressed_radius, getHeight() / 2 - bar_height / 2, getWidth() - pressed_radius, getHeight() / 2 + bar_height / 2, paint);
//        temp.drawRect(pressed_radius, pressed_radius - bar_height / 2, pressed_radius, pressed_radius + bar_height / 2, paint);
        //secondary
        if (showBuffer) {
            paint.setColor(secondaryColor);
            int divider = (int) (buffer / 100.0 * (getWidth() - 2 * pressed_radius) + pressed_radius);
            temp.drawRect(pressed_radius, getHeight() / 2 - bar_height / 2, divider, getHeight() / 2 + bar_height / 2, paint);
        }
        //value
        paint.setColor(backgroundColor);
        int divider = (int) (value / 100.0 * (getWidth() - 2 * pressed_radius) + pressed_radius);
        temp.drawRect(pressed_radius, getHeight() / 2 - bar_height / 2, divider, getHeight() / 2 + bar_height / 2, paint);
//        temp.drawRect(pressed_radius, pressed_radius - bar_height / 2, getWidth() -  divider, pressed_radius + bar_height / 2, paint);
        //circle
//        Paint circle = new Paint();
//        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(value == min ? disable_color : backgroundColor);
        temp.drawCircle((float) (ball.getX() + ball.getWidth() / 2.0), (float) (ball.getY() + ball.getHeight() / 2.0), (float) (ball.getWidth() / 2.0), paint);
        //trans
//        canvas.drawBitmap(bitmap, 0, 0, paint);
        if (value == min) {
//            bitmap = Bitmap.createBitmap(canvas.getWidth(),
//                    canvas.getHeight(), Bitmap.Config.ARGB_8888);
//            temp = new Canvas(bitmap);
//            temp.drawARGB(0, 0, 0, 0);
//            Paint trans=new Paint();
//            trans.setAntiAlias(true);
            paint.setColor(getResources().getColor(R.color.red));

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

            temp.drawCircle((float) (ball.getX() + ball.getWidth() / 2.0), (float) (ball.getY() + ball.getHeight() / 2.0), (float) ((ball.getWidth() / 2.0) - Util.getPX(2)), paint);
//            paint = new Paint();
//            canvas.drawBitmap(bitmap, 0, 0, paint);
            paint.setXfermode(null);
//            paint.setXfermode(null);
        }
        canvas.drawBitmap(bitmap, 0, 0, paint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        isLastTouch = true;
//        if (isEnabled()) {
//            if (event.getAction() == MotionEvent.ACTION_DOWN
//                    || event.getAction() == MotionEvent.ACTION_MOVE) {
//                if (numberIndicator != null
//                        && numberIndicator.isShowing() == false)
//                    numberIndicator.show();
//                if ((event.getX() <= getWidth() && event.getX() >= 0)) {
//                    press = true;
//                    // calculate value
//                    int newValue = 0;
////                    int divider = (int) (value / 100.0 * (getWidth() - 2 * pressed_radius) + pressed_radius);
//                    float x = event.getX();
//
//                    if (x < pressed_radius) {
//                        ball.setX((float) (pressed_radius - ball.getWidth() / 2.0));
//                        newValue = 0;
//                    } else if (x > getWidth() - pressed_radius) {
//                        ball.setX((float) (getWidth() - pressed_radius - ball.getWidth() / 2.0));
//                        newValue = 100;
//                    } else {
//                        newValue = (int) ((x - pressed_radius) * 1.0 / (getWidth() - pressed_radius * 2) * max);
//                        ball.setX((float) (x - ball.getWidth() / 2.0));
//                    }
//                    Logger.out("onTouchEvent x:" + x + " newValue:" + newValue);
////                    float division = (ball.xFin - ball.xIni) / (max - min);
////                    if (event.getX() > ball.xFin) {
////                        newValue = max;
////                    } else if (event.getX() < ball.xIni) {
////                        newValue = min;
////                    } else {
////                        newValue = min + (int) ((event.getX() - ball.xIni) / division);
////                    }
//                    if (value != newValue) {
//                        value = newValue;
////                        Logger.out("onTouchEvent onValueChanged 1");
//                        if (onValueChangedListener != null) {
////                            Logger.out("onTouchEvent onValueChanged 2");
//                            onValueChangedListener.onValueChanged(newValue);
//                        }
//                    }
//                    // move ball indicator
//
////                    x = (x < ball.xIni) ? ball.xIni : x;
////                    x = (x > ball.xFin) ? ball.xFin : x;
////                    ball.setX(x);
//
//                    ball.changeBackground();
//
//                    // If slider has number indicator
//                    if (numberIndicator != null) {
//                        // move number indicator
//                        numberIndicator.indicator.x = x;
//                        numberIndicator.indicator.finalY = Utils
//                                .getRelativeTop(this) - getHeight() / 2;
//                        numberIndicator.indicator.finalSize = getHeight() / 2;
//                        numberIndicator.numberIndicator.setText("");
//                    }
//
//                } else {
//                    press = false;
//                    isLastTouch = false;
//                    if (numberIndicator != null)
//                        numberIndicator.dismiss();
//
//                }
//
//            } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                if (numberIndicator != null)
//                    numberIndicator.dismiss();
//                isLastTouch = false;
//                press = false;
//                if ((event.getX() <= getWidth() && event.getX() >= 0)) {
//
//                }
//            }
//            invalidate();
//        }
        return true;
    }

    int makeSecondaryColor(int base) {
        int r = Color.red(base);
        int g = Color.green(base);
        int b = Color.blue(base);
        r = (int) (r * 7 / 8.0);
        g = (int) (g * 7 / 8.0);
        b = (int) (b * 7 / 8.0);
        return Color.rgb(r, g, b);
//        return Color.argb(255*3/4, Color.red(base), Color.green(base), Color.blue(base));
    }
//    /**
//     * Make a dark color to press effect
//     *
//     * @return
//     */
//    protected int makePressColor() {
//        int r = (this.backgroundColor >> 16) & 0xFF;
//        int g = (this.backgroundColor >> 8) & 0xFF;
//        int b = (this.backgroundColor >> 0) & 0xFF;
//        r = (r - 30 < 0) ? 0 : r - 30;
//        g = (g - 30 < 0) ? 0 : g - 30;
//        b = (b - 30 < 0) ? 0 : b - 30;
//        return Color.argb(70, r, g, b);
//    }

    private void placeBall() {
        ball.setX(0);
//        ball.setX(getHeight() / 2 - ball.getWidth() / 2);
//        ball.xIni = ball.getX();
//        ball.xFin = getWidth() - getHeight() / 2 - ball.getWidth() / 2;
//        ball.xCen = getWidth() / 2 - ball.getWidth() / 2;
        placedBall = true;
    }

    // GETERS & SETTERS

    public OnValueChangedListener getOnValueChangedListener() {
        return onValueChangedListener;
    }

    public void setOnValueChangedListener(
            OnValueChangedListener onValueChangedListener) {
        this.onValueChangedListener = onValueChangedListener;
    }

    public int getValue() {
        return value;
    }

    public void setValue(final int value) {
        if (placedBall == false)
            post(new Runnable() {

                @Override
                public void run() {
                    setValue(value);
                }
            });
        else {
            this.value = value;
//            float division = (ball.xFin - ball.xIni) / max;
//            float division = (ball.xFin - ball.xIni) / max;
            int divider = (int) (value / 100.0 * (getWidth() - 2 * pressed_radius) + pressed_radius);
//            Logger.out("setValue divider:" + divider + " value:" + value);
//            ball.setX(value * division + getHeight() / 2 - ball.getWidth() / 2);
            ball.setX(divider);
            ball.changeBackground();
            invalidate();
        }

    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public boolean isShowNumberIndicator() {
        return showNumberIndicator;
    }

    public void setShowNumberIndicator(boolean showNumberIndicator) {
        this.showNumberIndicator = showNumberIndicator;
        numberIndicator = (showNumberIndicator) ? new NumberIndicator(
                getContext()) : null;
    }

//    @Override
//    public void setBackgroundColor(int color) {
//        backgroundColor = color;
////        secondaryColor = makeSecondaryColor(backgroundColor);
//        if (isEnabled())
//            beforeBackground = backgroundColor;
//    }

    boolean placedBall = false;

    class Ball extends View {

//        float xIni, xFin, xCen;

        public Ball(Context context) {
            super(context);
//            setBackgroundResource(R.drawable.background_switch_ball_uncheck);
            setBackgroundResource(R.color.trans);

        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            pressed_radius = getMeasuredWidth() / 2 + Util.getPX(4);
        }

        public void changeBackground() {
//            if (value != min) {
//                setBackgroundResource(R.drawable.background_checkbox);
//                LayerDrawable layer = (LayerDrawable) getBackground();
//                GradientDrawable shape = (GradientDrawable) layer
//                        .findDrawableByLayerId(R.id.shape_bacground);
//                shape.setColor(backgroundColor);
//            } else {
//                setBackgroundResource(R.drawable.background_switch_ball_uncheck);
//            }
        }

    }

    // Slider Number Indicator

    class NumberIndicator extends Dialog {

        Indicator indicator;
        TextView numberIndicator;

        public NumberIndicator(Context context) {
            super(context, android.R.style.Theme_Translucent);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.number_indicator_spinner);
            setCanceledOnTouchOutside(false);

            RelativeLayout content = (RelativeLayout) this
                    .findViewById(R.id.number_indicator_spinner_content);
            indicator = new Indicator(this.getContext());
            content.addView(indicator);

            numberIndicator = new TextView(getContext());
            numberIndicator.setTextColor(Color.WHITE);
            numberIndicator.setGravity(Gravity.CENTER);
            content.addView(numberIndicator);

            indicator.setLayoutParams(new LayoutParams(
                    LayoutParams.FILL_PARENT,
                    LayoutParams.FILL_PARENT));
        }

        @Override
        public void dismiss() {
            super.dismiss();
            indicator.y = 0;
            indicator.size = 0;
            indicator.animate = true;
        }

        @Override
        public void onBackPressed() {
        }

    }

    class Indicator extends RelativeLayout {

        // Position of number indicator
        float x = 0;
        float y = 0;
        // Size of number indicator
        float size = 0;

        // Final y position after animation
        float finalY = 0;
        // Final size after animation
        float finalSize = 0;

        boolean animate = true;

        boolean numberIndicatorResize = false;

        public Indicator(Context context) {
            super(context);
            setBackgroundColor(getResources().getColor(
                    android.R.color.transparent));
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if (numberIndicatorResize == false) {
                LayoutParams params = (LayoutParams) numberIndicator.numberIndicator
                        .getLayoutParams();
                params.height = (int) finalSize * 2;
                params.width = (int) finalSize * 2;
                numberIndicator.numberIndicator.setLayoutParams(params);
            }

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(backgroundColor);
            if (animate) {
                if (y == 0)
                    y = finalY + finalSize * 2;
                y -= Util.getPX(6);
                size += Util.getPX(2);
            }
//            canvas.drawCircle(
//                    ball.getX()
//                            + Utils.getRelativeLeft((View) ball.getParent())
//                            + ball.getWidth() / 2, y, size, paint);
//            if (animate && size >= finalSize)
//                animate = false;
//            if (animate == false) {
//                numberIndicator.numberIndicator
//                        .setX(
//                                (ball.getX()
//                                        + Utils.getRelativeLeft((View) ball
//                                        .getParent()) + ball.getWidth() / 2)
//                                        - size);
//                numberIndicator.numberIndicator.setY(y - size);
//                numberIndicator.numberIndicator.setText(value + "");
//            }

            invalidate();
        }

    }

}
