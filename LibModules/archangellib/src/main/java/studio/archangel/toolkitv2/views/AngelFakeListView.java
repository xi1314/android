/**
 *
 */
package studio.archangel.toolkitv2.views;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import java.util.ArrayList;

import studio.archangel.toolkitv2.adapters.CommonAdapter;
import studio.archangel.toolkitv2.interfaces.Constructable;
import studio.archangel.toolkitv2.interfaces.OnItemClickListener;
import studio.archangel.toolkitv2.interfaces.OnItemLongClickListener;

/**
 * 基于LinearLayout的假ListView。提供了类似ListView的接口，所以可以设置Adapter
 *
 * @author Michael
 */
public class AngelFakeListView extends LinearLayout implements Constructable {
    CommonAdapter<?> adapter;
    Context c;
    OnItemClickListener cl;
    OnItemLongClickListener lcl;
    boolean show_dividers = false;
    Drawable divider = null;
    int divider_height = 0;
    int spacing = 0;
    ArrayList<View> views;
    int divider_padding_left = 0;
    int divider_padding_right = 0;
    View header;

    public AngelFakeListView(Context context) {
        super(context);
        init(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public AngelFakeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    void init(Context c) {
        this.c = c;
        views = new ArrayList<View>();
        // setPadding(Util.getPX(5), Util.getPX(5), Util.getPX(5),
        // Util.getPX(5));
        setOrientation(LinearLayout.VERTICAL);
    }

    public void setDividerPaddingRight(int divider_padding_right) {
        this.divider_padding_right = divider_padding_right;
    }

    public void setDividerPaddingLeft(int divider_padding_left) {
        this.divider_padding_left = divider_padding_left;
    }

    public void setDividerVisible(boolean b) {
        show_dividers = b;
    }

    public void setDivider(Drawable d) {
        divider = d;
        show_dividers = true;
        divider_height = divider.getIntrinsicHeight();
    }

    public void setDividerHeight(int h) {
        show_dividers = true;
//        divider=getResources().getDrawable(R.color.trans);
        divider_height = h;
    }

    public void setAdapter(CommonAdapter<?> a) {
        if (adapter != null) {
            adapter.unregisterDataSetObserver(ob);
        }

        adapter = a;

        if (adapter != null) {
            adapter.registerDataSetObserver(ob);
        }

        refresh();
    }

    public void setSpacing(int gap) {
        spacing = gap;
    }

    public ArrayList<View> getViews() {
        return views;
    }

    public void refresh() {
//        Logger.out("refresh");
        removeAllViews();
        views.clear();
        // int height = 0;
        if (header != null) {
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

            header.setLayoutParams(lp);
            header.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            header.layout(0, 0, header.getMeasuredWidth(), header.getMeasuredHeight());
            addView(header);
        }
        View v = null;
        for (int i = header != null ? 1 : 0; i < adapter.getCount()+(header != null ? 1 : 0); i++) {

            v = adapter.getView(header != null ? i - 1 : i, null, AngelFakeListView.this);
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            if (i != 0) {
                lp.setMargins(0, spacing, 0, 0);
            }
            v.setLayoutParams(lp);
            v.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
            final int index = header != null ? i - 1 : i;
            v.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (cl != null) {
                        cl.onItemClick(index);
                    }
                }
            });
            v.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (lcl != null) {
                        lcl.onItemLongClick(index);
                        return true;
                    }
                    return false;
                }
            });

            // height += v.getHeight();
            // LogUtils.i(v.getHeight() + " " + height + " " + i);
            if (show_dividers) {
                ImageView iv_divider = new ImageView(c);
                iv_divider.setScaleType(ScaleType.FIT_XY);
                if (divider != null) {
                    iv_divider.setImageDrawable(divider);
                }

                iv_divider.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, divider_height));
                iv_divider.setPadding(divider_padding_left, 0, divider_padding_right, 0);
                iv_divider.setAdjustViewBounds(true);
                addView(v, 2 * i);
//                addViewInLayout(v, 2 * i, v.getLayoutParams(), true);
                if (i != adapter.getCount() - 1) {
                    addView(iv_divider, 2 * i + 1);
//                    addViewInLayout(iv_divider, 2 * i + 1, v.getLayoutParams(), true);

                }

            } else {
                addView(v, i);
//                addViewInLayout(v, i, v.getLayoutParams(), true);
            }
            views.add(v);

            // measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
            // MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        }
        invalidate();
    }

    public Object getItemAt(int i) {
        return adapter.getItem(i);
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        cl = l;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener l) {
        lcl = l;
    }

    @Override
    public void setValue(Message msg) {
        CommonAdapter<?> ca = (CommonAdapter<?>) msg.obj;
        setAdapter(ca);

    }

    private DataSetObserver ob = new DataSetObserver() {

        @Override
        public void onChanged() {
            refresh();
        }

        @Override
        public void onInvalidated() {
            refresh();
        }

    };

    public void setHeaderView(View h) {
        header = h;

    }
}
