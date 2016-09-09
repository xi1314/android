package studio.archangel.toolkitv2.views;

/**
 * Collect from http://stackoverflow.com/questions/11095222/android-imageview-change-tint-to-simulate-button-click/18724834#18724834
 */

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import studio.archangel.toolkitv2.R;
import studio.archangel.toolkitv2.util.Logger;

public class TintableImageView extends ImageView {

    private ColorStateList tint;

    public TintableImageView(Context context) {
        super(context);
    }

    public TintableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public TintableImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TintableImageView, defStyle, 0);
        tint = a.getColorStateList(R.styleable.TintableImageView_tint);
        Logger.out(tint);
        a.recycle();
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (tint != null) {
            if (tint.isStateful()) {
                updateTintColor();
            } else {
                setColorFilter(tint.getDefaultColor());
            }
        }
    }

    public void setColorFilter(ColorStateList tint) {
        this.tint = tint;
        super.setColorFilter(tint.getColorForState(getDrawableState(), 0));
    }

    private void updateTintColor() {
        int color = tint.getColorForState(getDrawableState(), 0);
        setColorFilter(color);
    }

}