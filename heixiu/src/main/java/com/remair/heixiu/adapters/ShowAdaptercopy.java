package com.remair.heixiu.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.remair.heixiu.R;
import com.remair.heixiu.bean.LiveVideoBean;
import com.remair.heixiu.pull_rectclerview.RefreshRecyclerView;
import com.remair.heixiu.utils.HXImageLoader;
import com.remair.heixiu.utils.Utils;
import java.util.ArrayList;

/**
 * Created by Michael
 * adapter for shows
 */
public class ShowAdaptercopy extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int mWidthPixels;
    private final int mPx;

    public interface OnItemClickListener {

        void onItemClick(int position);
    }

    public interface OnItemToClickListener {

        void onItemClick(View view, Object data);
    }

    OnItemClickListener listener;
    OnItemToClickListener toClickListener;


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public void setOnItemToClickListener(OnItemToClickListener listener) {

        this.toClickListener = listener;
    }


    private ArrayList list;
    WindowManager wm;


    public ShowAdaptercopy(Context context, RefreshRecyclerView mRecyclervire, ArrayList list) {
        this.list = list;
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        mWidthPixels = displayMetrics.widthPixels;
        mPx = Utils.getPX(34);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.item_showcopy, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }


    ;


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (toClickListener != null) {

            holder.itemView.setTag(list.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toClickListener.onItemClick(holder.itemView, holder.itemView
                            .getTag());
                }
            });
        }

        final LiveVideoBean info = (LiveVideoBean) list.get(position);
        HXImageLoader.loadImage(((ItemViewHolder) holder).iv_avatar, info
                .photo, mPx, mPx);
        HXImageLoader.loadImage(((ItemViewHolder) holder).iv_back, info
                .cover_image, mWidthPixels, mWidthPixels);
        int viewing_num = info.viewing_num;
        if (viewing_num < 0) {
            viewing_num = 0;
        }
        ((ItemViewHolder) holder).tv_amount.setText(viewing_num + "");
        ((ItemViewHolder) holder).tv_name.setText(info.nickname);
        ((ItemViewHolder) holder).tv_location.setText(info.address);
        ((ItemViewHolder) holder).item_show_des.setText(info.title);
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_show_back) SimpleDraweeView iv_back;

        @BindView(R.id.item_show_amount) TextView tv_amount;
        @BindView(R.id.item_show_avatar) SimpleDraweeView iv_avatar;
        @BindView(R.id.item_show_name) TextView tv_name;
        @BindView(R.id.item_show_location) TextView tv_location;
        @BindView(R.id.item_show_des) TextView item_show_des;

        public ItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
