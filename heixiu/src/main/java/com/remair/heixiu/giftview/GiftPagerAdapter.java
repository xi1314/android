package com.remair.heixiu.giftview;

/**
 * Created by Michael
 * adapter for custom view for displaying gifts
 */

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import com.remair.heixiu.bean.Gift;
import java.util.ArrayList;

public class GiftPagerAdapter extends PagerAdapter {

    public static final int items_per_page = 10;
    GiftWrapper last_selected_gift = null;
    GiftSelectorPage.OnGiftSelectedListener listener;
    private ArrayList<GiftWrapper> gifts;
    private Context c;
    private GiftSelectorPageAdapter last_adapter;

    public GiftPagerAdapter(Context cx) {
        super();
        c = cx.getApplicationContext();
    }


    public void setData(ArrayList<Gift> g) {
        gifts = new ArrayList<>(g.size());
        for (Gift gift : g) {
            GiftWrapper wrapper = new GiftWrapper();
            wrapper.gift = gift;
            gifts.add(wrapper);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        int count = (int) Math.ceil(gifts.size() * 1.0 / items_per_page);
//        Logger.out("count=" + count);
        return count;
    }


    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final GiftSelectorPage page = new GiftSelectorPage(c);
        final ArrayList<GiftWrapper> sub = new ArrayList<>();
        GiftWrapper giftWrapper = null;
        for (int i = items_per_page * position; i < Math.min(gifts.size(), items_per_page * (position + 1)); i++) {
            sub.add(gifts.get(i));
        }
        page.setData(sub);
        page.setOnGiftSelectedListener(new GiftSelectorPage.OnGiftSelectedListener() {
            @Override
            public void onGiftSelected(GiftWrapper gift, int position) {
                if (last_selected_gift != null) {
                    last_selected_gift.is_selected = false;
                    if (last_adapter != null) {
                        last_adapter.notifyDataSetChanged();
                    }
                }
                last_selected_gift = gift;
                last_selected_gift.is_selected = true;
                last_adapter = page.adapter;
                last_adapter.notifyDataSetChanged();

                if (listener != null) {
                    listener.onGiftSelected(last_selected_gift, position);
                }
            }
        });
        container.addView(page, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return page;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void setOnGiftSelectedListener(GiftSelectorPage.OnGiftSelectedListener listener) {
        this.listener = listener;
    }
}
