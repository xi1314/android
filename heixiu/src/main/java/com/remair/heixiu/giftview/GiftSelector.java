package com.remair.heixiu.giftview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.remair.heixiu.R;
import com.remair.heixiu.bean.Gift;
import com.remair.heixiu.utils.HXUtil;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import studio.archangel.toolkitv2.util.Util;
import studio.archangel.toolkitv2.views.AngelPageIndicator;

/**
 * Created by Michael
 * custom view for selecting gift
 */
public class GiftSelector extends FrameLayout {
    @BindView(R.id.view_gift_selector_pager) ViewPager pager;
    @BindView(R.id.view_gift_selector_indicator) AngelPageIndicator indicator;
    GiftSelectorPage.OnGiftSelectedListener listener;
    private GiftPagerAdapter adapter;


    public GiftSelector(Context context) {
        this(context, null);
    }


    public GiftSelector(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public GiftSelector(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_gift_selector, this, true);
        ButterKnife.bind(this);
        if (isInEditMode()) {
            return;
        }

        HXUtil.fetchGiftData(new Util.OnDataFetchedListener() {
            @Override
            public void onDataFetched(Object data) {
                try {
                    JSONArray ja = new JSONArray(data.toString());
                    ArrayList<Gift> list = new ArrayList<>();
                    for (int i = 0; i < ja.length(); i++) {
                        Gift obj = new Gift(ja.optJSONObject(i));
                        list.add(obj);
                    }
                    adapter = new GiftPagerAdapter(context);
                    adapter.setData(list);
                    pager.setAdapter(adapter);
                    adapter.setOnGiftSelectedListener(new GiftSelectorPage.OnGiftSelectedListener() {
                        @Override
                        public void onGiftSelected(GiftWrapper gift, int position) {
                            if (listener != null) {
                                listener.onGiftSelected(gift, position);
                            }
                        }
                    });
                    indicator
                            .setUnitColorRes(R.color.white, R.color.text_grey, R.color.trans);
                    indicator.setUnitRadius(Util.getPX(6));
                    indicator.setCount(adapter.getCount());
                    indicator.setSelection(0);
                    pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }


                        @Override
                        public void onPageSelected(int position) {
                            indicator.setSelection(position);
                        }


                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void setOnGiftSelectedListener(GiftSelectorPage.OnGiftSelectedListener listener) {
        this.listener = listener;
    }
}
