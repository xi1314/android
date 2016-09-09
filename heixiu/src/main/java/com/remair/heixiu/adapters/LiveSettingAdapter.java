package com.remair.heixiu.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.remair.heixiu.R;
import com.remair.heixiu.bean.UserInfo;
import java.util.List;
import studio.archangel.toolkitv2.util.Util;

/**
 * Created by wsk on 16/4/5.
 */
public class LiveSettingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_NUMBERONE = 1;
    private static final int TYPE_NUMBEROTHER = 4;
    private Context context;
    private RecyclerView mRecyclervire;
    private List<UserInfo> list;
    private int type;
    private final int mPx;
    private final int mPx1;

    /**
     * 声明一个接口，用于实现点击事件
     */
    public interface OnItemClickListener {
        void onItemClick(int position, View view, Object data);

        void OnItemLongClick(int position, View view, Object data);
    }

    private OnItemClickListener mOnItemClickListener;


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    public LiveSettingAdapter(Context context, RecyclerView mRecyclervire, List<UserInfo> list, int type) {
        this.context = context;
        this.mRecyclervire = mRecyclervire;
        this.list = list;
        this.type = type;
        mPx = Util.getPX(50);
        mPx1 = Util.getPX(36);
    }


    public static class LiveSettingOneHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.setting_tv) TextView setting_tv;
        @BindView(R.id.settingto_tv) TextView settingto_tv;


        public LiveSettingOneHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class LiveSettingHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.chat_img) SimpleDraweeView chat_img;
        @BindView(R.id.chat_msg_count) TextView chat_msg_count;
        @BindView(R.id.chat_name) TextView chat_name;
        @BindView(R.id.chat_sex) ImageView chat_sex;
        @BindView(R.id.chat_levl) SimpleDraweeView chat_levl;
        @BindView(R.id.chat_grad) TextView chat_grad;
        @BindView(R.id.chat_msg) TextView chat_msg;
        @BindView(R.id.chat_msg_tip) RelativeLayout chat_msg_tip;
        @BindView(R.id.rl_grade) RelativeLayout rl_grade;
        @BindView(R.id.chat_private) TextView chat_private;


        public LiveSettingHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_NUMBERONE) {
            View view = LayoutInflater.from(parent.getContext())
                                      .inflate(R.layout.live_setting_text, parent, false);
            LiveSettingOneHolder liveSettingOneHolder = new LiveSettingOneHolder(view);
            return liveSettingOneHolder;
        } else {
            View view = LayoutInflater.from(parent.getContext())
                                      .inflate(R.layout.item_chat, parent, false);
            LiveSettingHolder liveSettingHolder = new LiveSettingHolder(view);
            return liveSettingHolder;
        }
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            if (type==0){
                ((LiveSettingOneHolder) holder).setting_tv
                        .setText(context.getResources()
                                        .getString(R.string.permanent_manager_text));
            }else if(type==1){
                ((LiveSettingOneHolder) holder).setting_tv
                        .setText(context.getResources()
                                        .getString(R.string.temporary_manager_text));
            }
            ((LiveSettingOneHolder) holder).settingto_tv
                    .setText(context.getResources()
                                    .getString(R.string.permanent_manager_explain));
        } else {
            ((LiveSettingHolder) holder).chat_sex.setVisibility(View.GONE);
            ((LiveSettingHolder) holder).chat_levl.setVisibility(View.GONE);
            ((LiveSettingHolder) holder).chat_grad.setVisibility(View.GONE);
            ((LiveSettingHolder) holder).chat_msg_tip.setVisibility(View.GONE);
            //HXImageLoader.loadImage(((LiveSettingHolder) holder).chat_img, list
            //        .get(position + 1).getPhoto(), mPx1, mPx1);
            //((LiveSettingHolder) holder).chat_name
            //        .setText(list.get(position + 1).getNickname());
            //if (list.get(position + 1).getGender() == 0) {
            //    ((LiveSettingHolder) holder).chat_sex
            //            .setImageResource(R.drawable.sex_man);
            //} else {
            //    ((LiveSettingHolder) holder).chat_sex
            //            .setImageResource(R.drawable.sex_woman);
            //}
            //Xtgrade.mXtgrade(list.get(position + 1)
            //                     .getGrade(), ((LiveSettingHolder) holder).iv_grade, ((LiveSettingHolder) holder).tv_grade);
        }
        if (mOnItemClickListener != null) {
            if (position == 0) {
                //((LiveSettingOneHolder) holder).imageview_iamgeview2
                //        .setOnClickListener(new View.OnClickListener() {
                //            @Override
                //            public void onClick(View v) {
                //
                //                Intent intent = new Intent(context, FriendMessageActivity.class);
                //                intent.putExtra("viewed_user_id", list.get(1)
                //                                                      .getUser_id());
                //                context.startActivity(intent);
                //            }
                //        });
                //((LiveSettingOneHolder) holder).imageview_iamgeview3
                //        .setOnClickListener(new View.OnClickListener() {
                //            @Override
                //            public void onClick(View v) {
                //                Intent intent = new Intent(context, FriendMessageActivity.class);
                //                intent.putExtra("viewed_user_id", list.get(2)
                //                                                      .getUser_id());
                //                context.startActivity(intent);
                //            }
                //        });
            } else {
                //((LiveSettingHolder) holder).itemView
                //        .setTag(list.get(position + 1));
                //((LiveSettingHolder) holder).itemView
                //        .setOnClickListener(new View.OnClickListener() {
                //
                //            @Override
                //            public void onClick(View v) {
                //                int LayoutPosition = holder
                //                        .getLayoutPosition(); //得到布局的position
                //                mOnItemClickListener
                //                        .onItemClick(LayoutPosition, holder.itemView, holder.itemView
                //                                .getTag());
                //            }
                //        });
                ////longclickListener
                //((LiveSettingHolder) holder).itemView
                //        .setOnLongClickListener(new View.OnLongClickListener() {
                //            @Override
                //            public boolean onLongClick(View v) {
                //                int LayoutPosition = holder
                //                        .getLayoutPosition(); //得到布局的position
                //                mOnItemClickListener
                //                        .OnItemLongClick(LayoutPosition, holder.itemView, holder.itemView
                //                                .getTag());
                //                return false;
                //            }
                //        });
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
        //return list.size() + 1;
        return 4;
    }
}
