package com.remair.heixiu.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.remair.heixiu.R;
import com.remair.heixiu.bean.LiveAttentionInfo;
import com.remair.heixiu.bean.LiveList;
import com.remair.heixiu.bean.ReplayList;
import com.remair.heixiu.utils.HXImageLoader;
import com.remair.heixiu.utils.Utils;

/**
 * Created by wsk on 16/3/10.
 */
public class ConcernLiveAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_ITEM = 0;
    public static final int TYPE_FILTER = 1;
    public static final int TYPE_SEPARATOR = 2;
    private final int mWidth;
    private final int px34;
    private final int px42;
    public LiveAttentionInfo info;
    WindowManager wm;


    public ConcernLiveAdapter(Context context, LiveAttentionInfo info) {
        this.info = info;
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        mWidth = displayMetrics.widthPixels;
        px34 = Utils.getPX(34);
        px42 = Utils.getPX(42);
    }


    /**
     * 声明一个接口，用于实现点击事件
     */
    public interface OnItemClickListener {
        void OnItemClick(int position, View view);

        void OnItemLongClick(int position, View view);
    }

    private OnItemClickListener mOnItemClickListener;


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    public static class LiveViewHolder extends RecyclerView.ViewHolder {//直播item
        @BindView(R.id.item_show_back) SimpleDraweeView iv_back;
        @BindView(R.id.item_show_amount) TextView tv_amount;
        @BindView(R.id.item_show_avatar) SimpleDraweeView iv_avatar;
        @BindView(R.id.item_show_name) TextView tv_name;
        @BindView(R.id.item_show_location) TextView tv_location;
        @BindView(R.id.item_show_des) TextView item_show_des;
        @BindView(R.id.tv_type) TextView mTvType;


        public LiveViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public void setLiveInfo(LiveAttentionInfo liveInfo) {
        this.info = liveInfo;
        this.notifyDataSetChanged();
    }


    public static class ReplayTitleHolder extends RecyclerView.ViewHolder {//回放头


        public ReplayTitleHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class ReplayViewHolder extends RecyclerView.ViewHolder {//回放item
        @BindView(R.id.chat_img) SimpleDraweeView chat_img;

        @BindView(R.id.chat_name) TextView chat_name;
        @BindView(R.id.chat_sex) ImageView chat_sex;
        @BindView(R.id.iv_grade) ImageView iv_grade;
        @BindView(R.id.tv_grade) TextView tv_grade;
        @BindView(R.id.chat_msg) TextView chat_msg;
        @BindView(R.id.chat_time) Button chat_time;
        @BindView(R.id.tv_time) TextView tv_time;
        @BindView(R.id.tv_hot_count) TextView tv_hot_count;


        public ReplayViewHolder(View itemView) {
            super(itemView);
            chat_img = (SimpleDraweeView) itemView.findViewById(R.id.chat_img);
            chat_name = (TextView) itemView.findViewById(R.id.chat_name);
            chat_sex = (ImageView) itemView.findViewById(R.id.chat_sex);
            iv_grade = (ImageView) itemView.findViewById(R.id.iv_grade);
            tv_grade = (TextView) itemView.findViewById(R.id.tv_grade);
            chat_msg = (TextView) itemView.findViewById(R.id.chat_msg);
            chat_time = (Button) itemView.findViewById(R.id.chat_time);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_hot_count = (TextView) itemView.findViewById(R.id.tv_hot_count);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                                      .inflate(R.layout.item_show, parent, false);
            LiveViewHolder liveViewHolder = new LiveViewHolder(view);
            return liveViewHolder;
        } else if (viewType == TYPE_FILTER) {
            View view = LayoutInflater.from(parent.getContext())
                                      .inflate(R.layout.item_huifang, parent, false);
            ReplayTitleHolder viewHolder = new ReplayTitleHolder(view);
            return viewHolder;
        } else {
            View view = LayoutInflater.from(parent.getContext())
                                      .inflate(R.layout.item_zhibo, parent, false);
            ReplayViewHolder viewHolder = new ReplayViewHolder(view);
            return viewHolder;
        }
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof LiveViewHolder) {//直播
            if (info.liveList.size() > 0) {
                LiveList liveList = info.liveList.get(position);

                HXImageLoader
                        .loadImage(((LiveViewHolder) holder).iv_avatar, liveList.photo, px34, px34);
                HXImageLoader
                        .loadImage(((LiveViewHolder) holder).iv_back, liveList.cover_image, mWidth, mWidth);
                int viewing_num = liveList.viewing_num;
                if (viewing_num < 0) {
                    viewing_num = 0;
                }
                ((LiveViewHolder) holder).tv_amount
                        .setText(viewing_num + "人正在观看");
                ((LiveViewHolder) holder).tv_name.setText(liveList.nickname);
                ((LiveViewHolder) holder).tv_location.setText(liveList.address);
                if (liveList.title.isEmpty()) {
                    ((LiveViewHolder) holder).item_show_des
                            .setVisibility(View.GONE);
                } else {
                    ((LiveViewHolder) holder).item_show_des
                            .setText(info.liveList.get(position).title);
                }
                if (liveList.liveType == 0) {
                    ((LiveViewHolder) holder).mTvType.setText("直播中");
                } else {
                    ((LiveViewHolder) holder).mTvType.setText("私密直播中");
                }
            }
        } else if (holder instanceof ReplayTitleHolder) {//回放投
            if (info.replayList.size() > 0) {

            }
        } else if (holder instanceof ReplayViewHolder) {//回放

            for (int i = 0; i < info.replayList.size(); i++) {
                ReplayList replayList = info.replayList.get(i);
                HXImageLoader
                        .loadImage(((ReplayViewHolder) holder).chat_img, replayList.cover_image, px42, px42);
                ((ReplayViewHolder) holder).chat_name.setText(replayList.title);
                ((ReplayViewHolder) holder).tv_time
                        .setText(replayList.duration + "");
                int viewing_num = replayList.viewing_num;
                if (viewing_num < 0) {
                    viewing_num = 0;
                }
                ((ReplayViewHolder) holder).tv_hot_count
                        .setText(viewing_num + "");
            }
        }

        if (mOnItemClickListener != null) {

            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int LayoutPosition = holder
                            .getLayoutPosition(); //得到布局的position
                    mOnItemClickListener
                            .OnItemClick(LayoutPosition, holder.itemView);
                }
            });

            //longclickListener
            holder.itemView
                    .setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            int LayoutPosition = holder
                                    .getLayoutPosition(); //得到布局的position
                            mOnItemClickListener
                                    .OnItemLongClick(LayoutPosition, holder.itemView);
                            return false;
                        }
                    });
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (null == info || position < 0 || position > getItemCount()) {
            return TYPE_ITEM;
        }
        if (info.liveList.size() > 0) {
            for (int i = 0; i < info.liveList.size(); i++) {
                if (i == position) {
                    return TYPE_ITEM;
                }
            }
        }

        if (info.replayList.size() > 0) {
            for (int i = 0; i <= info.replayList.size(); i++) {
                if (info.liveList.size() > 0) {
                    if (position == info.liveList.size()) {
                        return TYPE_FILTER;
                    } else {
                        return TYPE_SEPARATOR;
                    }
                } else {
                    if (position == 0) {
                        return TYPE_FILTER;
                    } else {
                        return TYPE_SEPARATOR;
                    }
                }
            }
        }
        return -1;
    }


    @Override
    public int getItemCount() {

        if (null != info && null != info.liveList &&
                null != info.replayList) {
            if (info.liveList.size() > 0 && info.replayList.size() <= 0) {
                return info.liveList.size();
            } else if (info.liveList.size() <= 0 &&
                    info.replayList.size() > 0) {
                return info.replayList.size() + 1;
            } else if (info.liveList.size() > 0 && info.replayList.size() > 0) {
                return info.liveList.size() + info.replayList.size() + 1;
            }
        } else {
            return 0;
        }
        return 0;
    }
}
