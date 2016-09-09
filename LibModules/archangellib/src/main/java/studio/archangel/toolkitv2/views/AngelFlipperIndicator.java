/**
 *
 */
package studio.archangel.toolkitv2.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import studio.archangel.toolkitv2.R;
import studio.archangel.toolkitv2.util.Util;

/**
 * @author Administrator
 */
@Deprecated
public class AngelFlipperIndicator extends LinearLayout {
    ArrayList<AngelFlipperIndicatorUnit> unitList;
    //int drawable_selected, drawable_unselected;

    /**
     * @param context
     */
    public AngelFlipperIndicator(Context context) {
        super(context);
        init(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public AngelFlipperIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context c) {
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.flipper_indicator, this);
        unitList = new ArrayList<AngelFlipperIndicatorUnit>();
    }

    public void setUnitCount(int count, int drawable_selected, int drawable_unselected) {
        this.removeAllViews();
        unitList.clear();
        for (int i = 0; i < count; i++) {
            boolean b = (i == 0);
            AngelFlipperIndicatorUnit fiu = new AngelFlipperIndicatorUnit(getContext(), b, drawable_selected, drawable_unselected);
            unitList.add(fiu);
            this.addView(fiu);
        }
    }

    public void setSelectedUnit(int index) {

        for (int i = 0; i < unitList.size(); i++) {
            try {

                AngelFlipperIndicatorUnit flu = unitList.get(i);
                if (i == index) {
                    flu.setStat(true);
                } else {
                    flu.setStat(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

@Deprecated
class AngelFlipperIndicatorUnit extends RelativeLayout {
    ImageView dot_unselected, dot_selected;
    boolean selectedStat = false;
    int drawable_selected, drawable_unselected;
    float scale_factor = 1.5f;
    boolean measured = false;
    // AnimationSet anim_select_green, anim_unselect_green, anim_select_grey,
    // anim_unselect_grey;

    /**
     * @param context
     */
    public AngelFlipperIndicatorUnit(Context context, boolean def, int c_s, int c_us) {
        super(context);
        drawable_selected = c_s;
        drawable_unselected = c_us;
        init(context, def);
    }

    /**
     * @param context
     * @param attrs
     */
    public AngelFlipperIndicatorUnit(Context context, AttributeSet attrs, boolean def) {
        super(context, attrs);
        init(context, def);
    }

    public void init(Context c, boolean def) {
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.flipper_indicator_unit, this);
        dot_unselected = (ImageView) findViewById(R.id.flipper_indicator_unit_dot_unslected);
        dot_selected = (ImageView) findViewById(R.id.flipper_indicator_unit_dot_selected);

        dot_selected.setImageResource(drawable_selected);
        dot_unselected.setImageResource(drawable_unselected);
        // initAnim();
        setStat(def);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!measured) {
            int size = dot_unselected.getMeasuredWidth();
            if (size == 0) {
                return;
            }
            try {
                int scaled_size = size + Util.getPX(2);
                scale_factor = (float) (scaled_size * 1.0 / size);
                measured = true;
            } catch (Exception e) {
                e.printStackTrace();

            }

        }
    }

    /**
     * 设置选中状态
     *
     * @param selected 新的状态
     */
    public void setStat(boolean selected) {
        if (selectedStat == selected) {
            return;
        }

        if (selectedStat) {// 之前已选中
            ViewPropertyAnimator an = dot_selected.animate().scaleX(1f).scaleY(1f).alpha(0f);
            an.scaleX(1f).scaleY(1f).alpha(0f);
            an.setDuration(200);
            an.start();
            // dot_grey.startAnimation(anim_unselect_grey);
            // dot_green.startAnimation(anim_unselect_green);
        } else {// 之前未选中
            ViewPropertyAnimator an = dot_selected.animate().scaleX(scale_factor).scaleY(scale_factor).alpha(1f);
            an.setDuration(200);
            dot_selected.setVisibility(View.VISIBLE);
            an.start();
            // dot_grey.startAnimation(anim_select_grey);
            // dot_green.startAnimation(anim_select_green);
        }

        selectedStat = selected;

    }
}
