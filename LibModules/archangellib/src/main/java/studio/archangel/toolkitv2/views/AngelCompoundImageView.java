package studio.archangel.toolkitv2.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import studio.archangel.toolkitv2.R;
import studio.archangel.toolkitv2.util.Util;

/**
 * Created by Michael on 2015/4/10.
 */
public class AngelCompoundImageView extends RelativeLayout {
    //    int color_back;
    SimpleDraweeView image;
    TextView text;
    GradientDrawable shape;

    public AngelCompoundImageView(Context context) {
        this(context, null);
    }

    public AngelCompoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AngelCompoundImageView(Context context, AttributeSet attrs, int def) {
        super(context, attrs, def);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_compound_image, this, true);
        image = (SimpleDraweeView) findViewById(R.id.view_compound_image_image);
        text = (TextView) findViewById(R.id.view_compound_image_text);
        LayerDrawable layer = (LayerDrawable) text.getBackground();
        shape = (GradientDrawable) layer.findDrawableByLayerId(R.id.shape_compound_image_back_oval);
        if (!isInEditMode()) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AngelCompoundImageView);
            int color = a.getColor(R.styleable.AngelCompoundImageView_aciv_background, -1);
            if (color != -1) {
                setImageColor(color);
            }
            boolean is_round = a.getBoolean(R.styleable.AngelCompoundImageView_aciv_round, true);
            GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(getResources());
            RoundingParams params = new RoundingParams();
            params.setRoundAsCircle(is_round);

            if (!is_round) {
                int radius = a.getDimensionPixelSize(R.styleable.AngelCompoundImageView_aciv_corner_radius, Util.getPX(2));
                params.setCornersRadius(Util.getDP(radius));
                shape.setCornerRadius(radius);
//                Logger.out("dp:" + Util.getDP(radius) + " px:" + radius);
            }
            int size = a.getDimensionPixelSize(R.styleable.AngelCompoundImageView_aciv_text_size, isInEditMode() ? 16 : Util.getPX(16));
            text.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
            GenericDraweeHierarchy hierarchy = builder.setRoundingParams(params).build();
            image.setHierarchy(hierarchy);
            a.recycle();
        }
    }

    public SimpleDraweeView getImageView() {
        image.setVisibility(View.VISIBLE);
        text.setVisibility(View.GONE);
        return image;

    }

    public TextView getTextView() {
        return text;

    }

    public void setImageFromName(String name, boolean auto_short) {
        image.setVisibility(View.GONE);
        text.setVisibility(View.VISIBLE);
        text.setText(auto_short ? Util.getShortName(name) : name);
    }

    public void setImageColor(int color) {
        shape.setStroke(Util.getPX(1), color);
        text.setTextColor(color);
    }
}

