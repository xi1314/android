/**
 *
 */
package studio.archangel.toolkitv2.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Space;

import java.util.ArrayList;

import studio.archangel.toolkitv2.R;
import studio.archangel.toolkitv2.adapters.CommonAdapter;
import studio.archangel.toolkitv2.interfaces.Constructable;
import studio.archangel.toolkitv2.interfaces.OnItemClickListener;
import studio.archangel.toolkitv2.interfaces.OnItemLongClickListener;

/**
 * 基于LinearLayout的假GridView。提供了类似GridView的接口，所以可以设置Adapter
 *
 * @author Michael
 */
public class AngelFakeGridView extends LinearLayout implements Constructable {
    CommonAdapter<?> adapter;
    Context c;
    OnItemClickListener cl;
    OnItemLongClickListener lcl;
    ArrayList<View> views;
    int spacing_horizontal = 0;
    int spacing_vertical = 0;
    int col_count = 3;

    public AngelFakeGridView(Context context) {
        this(context, null);
    }

    public AngelFakeGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AngelFakeGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.c = context;
        views = new ArrayList<>();
        setOrientation(LinearLayout.VERTICAL);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AngelFakeGridView);
        col_count = a.getInteger(R.styleable.AngelFakeGridView_afg_col_count, col_count);
        spacing_horizontal = a.getDimensionPixelSize(R.styleable.AngelFakeGridView_afg_spacing_horizontal, 0);
        spacing_vertical = a.getDimensionPixelSize(R.styleable.AngelFakeGridView_afg_spacing_vertical, 0);

        a.recycle();
        views = new ArrayList<>();
    }

    public int getColumnCount() {
        return col_count;
    }

    public void setColumnCount(int col_count) {
        this.col_count = col_count;
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

    public ArrayList<View> getViews() {
        return views;
    }

    public void setVerticalSpacing(int spacing_vertical) {
        this.spacing_vertical = spacing_vertical;
    }

    public void setHorizontalSpacing(int spacing_horizontal) {
        this.spacing_horizontal = spacing_horizontal;
    }

    public void refresh() {
//        Logger.out("refresh");
        removeAllViews();
        views.clear();
        // int height = 0;
        View v = null;
        LinearLayout row = null;
        for (int i = 0; i < adapter.getCount(); i++) {
            if (i % col_count == 0) {
                if (i != 0 && spacing_vertical > 0) {
                    Space dummy = new Space(getContext());
                    LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, spacing_vertical);
                    dummy.setLayoutParams(params);
                    dummy.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                    dummy.layout(0, 0, dummy.getMeasuredWidth(), dummy.getMeasuredHeight());
                    addView(dummy);
                }
                row = new LinearLayout(getContext());
                LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                row.setLayoutParams(lp);
                addView(row);
            }
            if(i % col_count != 0 && spacing_horizontal > 0){
                Space dummy = new Space(getContext());
                LayoutParams params = new LayoutParams(spacing_horizontal, 1);
                dummy.setLayoutParams(params);
                dummy.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                dummy.layout(0, 0, dummy.getMeasuredWidth(), dummy.getMeasuredHeight());
                if (row != null) {
                    row.addView(dummy);
                }
            }
            v = adapter.getView(i, null, AngelFakeGridView.this);
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            lp.weight = 1;
            v.setLayoutParams(lp);
            v.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
            final int index = i;
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
            if (row != null) {
                row.addView(v);
            }
            views.add(v);
            if (i == adapter.getCount() - 1 && row != null && row.getChildCount() < col_count) {
                int count = col_count - row.getChildCount();
                for (int j = 0; j < count; j++) {
                    Space dummy = new Space(getContext());
                    LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                    params.weight = 1;
                    dummy.setLayoutParams(params);
                    dummy.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                    dummy.layout(0, 0, dummy.getMeasuredWidth(), dummy.getMeasuredHeight());
                    row.addView(dummy);
                }
            }
        }
        invalidate();
    }

    public Object getItemAt(int i) {
        return adapter.getItem(i);
    }

    public OnItemLongClickListener getOnItemLongClickListener() {
        return lcl;
    }

    public OnItemClickListener getOnItemClickListener() {
        return cl;
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
}
