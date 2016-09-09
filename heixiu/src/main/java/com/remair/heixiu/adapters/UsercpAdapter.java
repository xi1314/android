package com.remair.heixiu.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.FriendMessageActivity;
import com.remair.heixiu.bean.RankList;
import com.remair.heixiu.utils.HXImageLoader;
import com.remair.heixiu.utils.Xtgrade;
import java.util.List;
import studio.archangel.toolkitv2.util.Util;

/**
 * Created by wsk on 16/4/5.
 */
public class UsercpAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HERDER = 0;
    private static final int TYPE_NUMBERONE = 1;
    private static final int TYPE_NUMBERTWO = 2;
    private static final int TYPE_NUMBERTHREE = 3;
    private static final int TYPE_NUMBEROTHER = 4;
    private Context context;
    private List<RankList> list;
    private String usercp;
    private final int mPx;
    private final int mPx1;

    /**
     * 声明一个接口，用于实现点击事件
     */
    public interface OnItemClickListener {
        void onItemClick(int position, View view, Object data);

        void OnItemLongClick(int position, View view);
    }

    private OnItemClickListener mOnItemClickListener;


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    public UsercpAdapter(Context context, List<RankList> list, String usercp) {
        this.context = context;
        this.list = list;
        this.usercp = usercp;
        mPx = Util.getPX(50);
        mPx1 = Util.getPX(36);
    }
    public static class UsecpNumberOneHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageview_iamgeview2) SimpleDraweeView imageview_iamgeview2;
        @BindView(R.id.imageview_iamgeview3) SimpleDraweeView imageview_iamgeview3;
        @BindView(R.id.textview_iamgeview2) TextView textview_iamgeview2;
        @BindView(R.id.meilizhi_textviewcunzaigan2)
        TextView meilizhi_textviewcunzaigan2;
        @BindView(R.id.meilizhi_textviewcunzaigan3)
        TextView meilizhi_textviewcunzaigan3;
        @BindView(R.id.textview_iamgeview3) TextView textview_iamgeview3;


        public UsecpNumberOneHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class UsecpNumberOtherHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_rank) TextView txt_rank;
        @BindView(R.id.user_portrait) SimpleDraweeView user_portrait;
        @BindView(R.id.txt_username) TextView txt_username;
        @BindView(R.id.img_gender) ImageView img_gender;
        @BindView(R.id.img_level) SimpleDraweeView img_level;
        @BindView(R.id.txt_coin_count) TextView txt_coin_count;
        @BindView(R.id.txt_coin_count1) TextView txt_coin_count1;
        @BindView(R.id.txt_coin_count2) TextView txt_coin_count2;
        @BindView(R.id.tv_level) TextView tv_level;


        public UsecpNumberOtherHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_NUMBERONE) {
            View view = LayoutInflater.from(parent.getContext())
                                      .inflate(R.layout.utem_paihangbang, parent, false);
            UsecpNumberOneHolder usecpNumberOneHolder = new UsecpNumberOneHolder(view);
            return usecpNumberOneHolder;
        } else {
            View view = LayoutInflater.from(parent.getContext())
                                      .inflate(R.layout.cell_gift_contributor, parent, false);
            UsecpNumberOtherHolder usecpNumberOneHolder = new UsecpNumberOtherHolder(view);
            return usecpNumberOneHolder;
        }
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            if (list.size() == 2) {
                //这个代表只有一个用胡
                HXImageLoader
                        .loadImage(((UsecpNumberOneHolder) holder).imageview_iamgeview2, list
                                .get(position + 1).photo, mPx, mPx);
                ((UsecpNumberOneHolder) holder).textview_iamgeview2
                        .setText(list.get(position + 1).nickname);
                ((UsecpNumberOneHolder) holder).meilizhi_textviewcunzaigan2
                        .setText(Xtgrade.moneynumber(
                                list.get(position + 1).charm_value_sent + ""));

                ((UsecpNumberOneHolder) holder).textview_iamgeview3
                        .setText(list.get(position + 1).nickname);
                ((UsecpNumberOneHolder) holder).meilizhi_textviewcunzaigan3
                        .setText(Xtgrade.moneynumber(
                                list.get(position + 1).charm_value_sent + ""));
                ((UsecpNumberOneHolder) holder).imageview_iamgeview3
                        .setVisibility(View.GONE);
                ((UsecpNumberOneHolder) holder).textview_iamgeview3
                        .setVisibility(View.GONE);
                ((UsecpNumberOneHolder) holder).meilizhi_textviewcunzaigan3
                        .setVisibility(View.GONE);
            } else {
                HXImageLoader
                        .loadImage(((UsecpNumberOneHolder) holder).imageview_iamgeview2, list
                                .get(position + 1).photo, mPx, mPx);
                HXImageLoader
                        .loadImage(((UsecpNumberOneHolder) holder).imageview_iamgeview3, list
                                .get(position + 2).photo, mPx, mPx);
                ((UsecpNumberOneHolder) holder).textview_iamgeview2
                        .setText(list.get(position + 1).nickname);
                ((UsecpNumberOneHolder) holder).textview_iamgeview3
                        .setText(list.get(position + 2).nickname);
                ((UsecpNumberOneHolder) holder).meilizhi_textviewcunzaigan2
                        .setText(Xtgrade.moneynumber(
                                list.get(position + 1).charm_value_sent + ""));
                ((UsecpNumberOneHolder) holder).meilizhi_textviewcunzaigan3
                        .setText(Xtgrade.moneynumber(
                                list.get(position + 2).charm_value_sent + ""));
            }
        } else {
            ((UsecpNumberOtherHolder) holder).txt_rank
                    .setText((position + 3) + "");
            HXImageLoader
                    .loadImage(((UsecpNumberOtherHolder) holder).user_portrait, list
                            .get(position + 2).photo, mPx1, mPx1);
            ((UsecpNumberOtherHolder) holder).txt_username
                    .setText(list.get(position + 2).nickname);
            if (list.get(position + 2).gender == 0) {
                ((UsecpNumberOtherHolder) holder).img_gender
                        .setImageResource(R.drawable.sex_man);
            } else {
                ((UsecpNumberOtherHolder) holder).img_gender
                        .setImageResource(R.drawable.sex_woman);
            }
            ((UsecpNumberOtherHolder) holder).txt_coin_count.setText(Xtgrade
                    .moneynumber(list.get(position + 2).charm_value_sent + ""));
            ((UsecpNumberOtherHolder) holder).txt_coin_count1.setText("贡献");
            ((UsecpNumberOtherHolder) holder).txt_coin_count1
                    .setTextColor(Color.parseColor("#bFFFFFFF"));
            ((UsecpNumberOtherHolder) holder).txt_coin_count2.setText("存在感");
            ((UsecpNumberOtherHolder) holder).txt_coin_count2
                    .setTextColor(Color.parseColor("#bFFFFFFF"));
            Xtgrade.mXtgrade(list.get(position +
                    2).grade, ((UsecpNumberOtherHolder) holder).img_level, ((UsecpNumberOtherHolder) holder).tv_level);
        }
        if (mOnItemClickListener != null) {
            if (position == 0) {
                ((UsecpNumberOneHolder) holder).imageview_iamgeview2
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(context, FriendMessageActivity.class);
                                intent.putExtra("viewed_user_id", list
                                        .get(1).user_id);
                                context.startActivity(intent);
                            }
                        });
                ((UsecpNumberOneHolder) holder).imageview_iamgeview3
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, FriendMessageActivity.class);
                                intent.putExtra("viewed_user_id", list
                                        .get(2).user_id);
                                context.startActivity(intent);
                            }
                        });
            } else {
                ((UsecpNumberOtherHolder) holder).itemView
                        .setTag(list.get(position + 2));
                ((UsecpNumberOtherHolder) holder).itemView
                        .setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                int LayoutPosition = holder
                                        .getLayoutPosition(); //得到布局的position
                                mOnItemClickListener
                                        .onItemClick(LayoutPosition, holder.itemView, holder.itemView
                                                .getTag());
                            }
                        });
                //longclickListener
                ((UsecpNumberOtherHolder) holder).itemView
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
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_NUMBERONE;
        } else {
            return TYPE_NUMBEROTHER;
        }
    }


    @Override
    public int getItemCount() {
        if (list.size() == 2) {
            return 1;
        } else {
            return list.size() - 2;
        }
    }
}
