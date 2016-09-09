package studio.archangel.toolkitv2.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

import studio.archangel.toolkitv2.R;
import studio.archangel.toolkitv2.util.Util;

/**
 * Created by Michael on 2015/6/9.
 */
public class AngelDashLine extends View {
    private Paint paint;
    private Path path;
    private PathEffect effects;

    public AngelDashLine(Context context) {
        this(context, null);
    }

    public AngelDashLine(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AngelDashLine(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        path = new Path();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AngelDashLine);
        int color = a.getColor(R.styleable.AngelDashLine_adl_dash_color, getResources().getColor(R.color.divider_normal));
        paint.setColor(color);
        int width = a.getDimensionPixelOffset(R.styleable.AngelDashLine_adl_line_width, 1);
        paint.setStrokeWidth(width);
        int length = a.getDimensionPixelOffset(R.styleable.AngelDashLine_adl_dash_length, isInEditMode() ? 4 : Util.getPX(4));
        int gap = a.getDimensionPixelOffset(R.styleable.AngelDashLine_adl_dash_gap, isInEditMode() ? 2 : Util.getPX(2));
        effects = new DashPathEffect(new float[]{length, gap, length, gap}, 0);
        int res = a.getResourceId(R.styleable.AngelDashLine_adl_background, R.color.trans);
        setBackgroundResource(res);
        a.recycle();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setPathEffect(effects);
        int height = getMeasuredHeight();
        int width = getMeasuredWidth();
        if (height <= width) {
            // horizontal
            path.moveTo(0, (float) (height / 2.0));
            path.lineTo(width, (float) (height / 2.0));
            canvas.drawPath(path, paint);
        } else {
            // vertical
            path.moveTo((float) (width / 2.0), 0);
            path.lineTo((float) (width / 2.0), height);
            canvas.drawPath(path, paint);
        }

    }
}