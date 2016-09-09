package com.remair.heixiu.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.remair.heixiu.R;
import com.remair.heixiu.bean.ReplayBean;
import com.remair.heixiu.utils.HXImageLoader;
import java.util.List;
import studio.archangel.toolkitv2.adapters.CommonRecyclerAdapter;
import studio.archangel.toolkitv2.util.Util;
import studio.archangel.toolkitv2.viewholders.AngelCommonViewHolder;

/**
 * Created by wsk on 16/3/10.
 */
public class ReplayAdapter extends CommonRecyclerAdapter implements ItemSlideHelper.Callback {

    private final int px42;
    private Context c;
    private RecyclerView mRecyclerView;
    private DeleteListener deleteListener;
    public ItemSlideHelper itemSlideHelper;


    public boolean isEnableSlide() {
        return enableSlide;
    }


    private boolean enableSlide;//是否可以滑动
    private ReplayViewHolder viewItem;


    public ReplayAdapter(Context c, List i, DeleteListener deleteListener, Boolean enableSlide) {
        super(c, new int[] { R.layout.item_zhibo }, null, i, false);
        this.c = c;
        this.enableSlide = enableSlide;
        this.deleteListener = deleteListener;
        px42 = Util.getPX(42);
    }


    @Override
    protected RecyclerView.ViewHolder onCreateHeaderVH(View v) {
        return null;
    }


    @Override
    protected RecyclerView.ViewHolder onCreateItemVH(View v) {

        ReplayViewHolder replayViewHolder = new ReplayViewHolder(v);
        this.viewItem = replayViewHolder;
        return replayViewHolder;
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        itemSlideHelper = new ItemSlideHelper(mRecyclerView.getContext(), this);
        itemSlideHelper.setEnableSlide(enableSlide);
        mRecyclerView.addOnItemTouchListener(itemSlideHelper);
    }


    @Override
    public int getHorizontalRange(RecyclerView.ViewHolder holder) {

        if (holder.itemView instanceof LinearLayout) {
            ViewGroup viewGroup = (ViewGroup) holder.itemView;
            if (viewGroup.getChildCount() == 2) {
                return viewGroup.getChildAt(1).getLayoutParams().width;
            }
        }
        return 0;
    }


    @Override
    public RecyclerView.ViewHolder getChildViewHolder(View childView) {
        return mRecyclerView.getChildViewHolder(childView);
    }


    @Override
    public View findTargetView(float x, float y) {
        return mRecyclerView.findChildViewUnder(x, y);
    }


    @Override
    public void deleteItem(RecyclerView.ViewHolder viewHolder) {
        if (deleteListener != null) {
            deleteListener.deleteItem(viewHolder);
        }
    }


    @Override
    public boolean setEnableSlide() {
        if (itemEnableSlide != null) {
            boolean b = itemEnableSlide.setItemSlide();
            return b;
        }
        return true;
    }


    public void setSlideState(ItemEnableSlide itemEnableSlide) {
        this.itemEnableSlide = itemEnableSlide;
    }


    public class ReplayViewHolder extends AngelCommonViewHolder {
        @BindView(R.id.chat_img) SimpleDraweeView chat_img;
        @BindView(R.id.chat_name) TextView chat_name;
        @BindView(R.id.tv_time) TextView tv_time;
        @BindView(R.id.tv_hot_count) TextView tv_hot_count;
        @BindView(R.id.chat_msg) TextView chat_msg;
        @BindView(R.id.linearlayout) LinearLayout linearlayout;


        public ReplayViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }


        @Override
        public void render(final Context context, Object model) {
            ReplayBean bean = (ReplayBean) model;
            linearlayout.setVisibility(View.VISIBLE);
            HXImageLoader
                    .loadImage(chat_img, bean.userEntity.photo, px42, px42);
            chat_name.setText(bean.userEntity.nickname);
            int second = bean.duration;
            String formatTime;
            String hs, ms, ss;
            long h, m, s;
            h = second / 3600;
            m = (second % 3600) / 60;
            s = (second % 3600) % 60;
            if (h < 10) {
                hs = "0" + h;
            } else {
                hs = "" + h;
            }

            if (m < 10) {
                ms = "0" + m;
            } else {
                ms = "" + m;
            }

            if (s < 10) {
                ss = "0" + s;
            } else {
                ss = "" + s;
            }
            formatTime = hs + ":" + ms + ":" + ss;
            tv_time.setText(formatTime);
            tv_hot_count.setText(bean.record_count + "");
            chat_msg.setText(bean.endTime + "");
        }
    }

    public interface DeleteListener {
        void deleteItem(RecyclerView.ViewHolder viewHolder);
    }

    private ItemEnableSlide itemEnableSlide;

    public interface ItemEnableSlide {
        boolean setItemSlide();
    }
}



