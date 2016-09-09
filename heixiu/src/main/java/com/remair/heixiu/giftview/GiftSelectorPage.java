package com.remair.heixiu.giftview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.remair.heixiu.R;
import java.util.ArrayList;
import studio.archangel.toolkitv2.adapters.CommonRecyclerAdapter;

/**
 * Created by Michael
 * custom view for displaying gift page
 */
public class GiftSelectorPage extends FrameLayout {
    public GiftSelectorPageAdapter adapter;
    @BindView(R.id.view_gift_grid)
    RecyclerView grid;
    ArrayList<GiftWrapper> gifts;
    OnGiftSelectedListener listener;
    private int flag = -1;

    public GiftSelectorPage(Context context) {
        this(context, null);
    }

    public GiftSelectorPage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GiftSelectorPage(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_gift_page, this, true);
        ButterKnife.bind(this);
        gifts = new ArrayList<>();
        GridLayoutManager lm = new GridLayoutManager(context, GiftPagerAdapter.items_per_page / 2);
        grid.setLayoutManager(lm);
        adapter = new GiftSelectorPageAdapter(context, gifts);
        grid.setAdapter(adapter);
        adapter.setRecyclerView(grid);

        adapter.setOnItemClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                GiftWrapper wrapper = gifts.get(position);
                wrapper.is_selected = true;
                if (flag != position) {
                    wrapper.count = 1;
                    flag = position;
                } else {
                    if (wrapper.count == 1) {
                        wrapper.count = 9;
                    } else if (wrapper.count == 9) {
                        wrapper.count = 99;
                    } else if (wrapper.count == 99) {
                        wrapper.count = 999;
                    } else if (wrapper.count == 999) {
                        wrapper.count = 1;
                    }
                }
                if (listener != null) {
                    listener.onGiftSelected(wrapper, position);
                }
//                adapter.notifyDataSetChanged();
            }
        });
    }

    public void setOnGiftSelectedListener(OnGiftSelectedListener listener) {
        this.listener = listener;
    }

    public void setData(ArrayList<GiftWrapper> g) {
        gifts.clear();
//        if (g.size()>0){
//            g.get(0).is_selected=true;
//        }
        gifts.addAll(g);
        adapter.notifyDataSetChanged();
    }

    public interface OnGiftSelectedListener {
        void onGiftSelected(GiftWrapper gift, int position);
    }
}
