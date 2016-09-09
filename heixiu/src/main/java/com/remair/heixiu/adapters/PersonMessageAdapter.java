package com.remair.heixiu.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.HXJavaNet;
import com.remair.heixiu.R;
import com.remair.heixiu.bean.FriendInfo;
import com.remair.heixiu.utils.GetMessageUtil;
import com.remair.heixiu.utils.HXImageLoader;
import com.remair.heixiu.utils.Utils;
import com.remair.heixiu.utils.Xtgrade;
import com.remair.heixiu.view.LoadingDialog;
import java.util.HashMap;
import java.util.List;
import studio.archangel.toolkitv2.adapters.CommonRecyclerAdapter;
import studio.archangel.toolkitv2.util.networking.AngelNetCallBack;
import studio.archangel.toolkitv2.util.text.Notifier;
import studio.archangel.toolkitv2.viewholders.AngelCommonViewHolder;

/**
 * Created by wsk on 16/3/1.
 */
public class PersonMessageAdapter extends CommonRecyclerAdapter {
    private final int px42;
    LoadingDialog loadingDialog;
    boolean bisuser;


    public PersonMessageAdapter(Context context, List<FriendInfo> mList, AttentionListener listener, boolean bisuser) {
        super(context, new int[] {
                R.layout.item_person_message }, null, mList, false);
        this.attentionListener = listener;
        this.bisuser = bisuser;
        px42 = Utils.getPX(42);
    }


    @Override
    protected RecyclerView.ViewHolder onCreateHeaderVH(View v) {
        return null;
    }


    @Override
    protected RecyclerView.ViewHolder onCreateItemVH(View v) {
        return new ItemViewHolder(v);
    }


    public class ItemViewHolder extends AngelCommonViewHolder {
        @BindView(R.id.chat_img) SimpleDraweeView chat_img;
        @BindView(R.id.iv_secret) ImageView mIvSecret;
        @BindView(R.id.iv_public) ImageView mIvPub;
        @BindView(R.id.chat_name) TextView chat_name;
        @BindView(R.id.chat_sex) ImageView chat_sex;
        @BindView(R.id.iv_grade) SimpleDraweeView iv_grade;
        @BindView(R.id.tv_grade) TextView tv_grade;
        @BindView(R.id.chat_msg) TextView chat_msg;
        @BindView(R.id.chat_time) ImageButton chat_time;
        @BindView(R.id.phone_name) TextView phone_name;
        boolean iscuntean = false;


        public ItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }


        @Override
        public void render(final Context context, Object model) {
            final FriendInfo friendInfo = (FriendInfo) model;
            mIvSecret.setVisibility(View.GONE);
            mIvPub.setVisibility(View.GONE);
            HXImageLoader.loadImage(chat_img, friendInfo.photo, px42, px42);
            if (friendInfo.liveState == 2) {
                //私密直播
                mIvSecret.setVisibility(View.VISIBLE);
            }
            if (friendInfo.liveState == 1) {
                //公开直播
                mIvPub.setVisibility(View.VISIBLE);
            }
            chat_name.setText(friendInfo.nickname);
            if (null == friendInfo.signature) {
                phone_name.setText("好友名称:" + friendInfo.phonename);
            } else {
                chat_msg.setText(friendInfo.signature + "");
            }
            tv_grade.setText(friendInfo.grade + "");
            if (friendInfo.gender == 0) {
                chat_sex.setImageResource(R.drawable.sex_man);
            } else {
                chat_sex.setImageResource(R.drawable.sex_woman);
            }
            Xtgrade.mXtgrade(friendInfo.grade, iv_grade, tv_grade);

            if (!bisuser) {
                chat_time.setVisibility(View.GONE);
            } else {
                chat_time.setVisibility(View.VISIBLE);
            }
            if (friendInfo.relation_type == 0) {
                chat_time.setImageResource(R.drawable.add_friend);
                iscuntean = false;
            } else if (friendInfo.relation_type == 1) {
                chat_time.setImageResource(R.drawable.added_friend);
                iscuntean = true;
            } else {
                chat_time.setImageResource(R.drawable.add_friend);
                iscuntean = false;
            }
            chat_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (loadingDialog == null) {
                        loadingDialog = new LoadingDialog(context, R.style.dialog);
                    }
                    loadingDialog.show();
                    if (!iscuntean) {
                        HashMap<String, Object> parater = new HashMap<String, Object>();
                        parater.put("user_id", HXApp.getInstance()
                                                    .getUserInfo().user_id);
                        parater.put("passive_user_id", friendInfo.user_id);
                        HXJavaNet
                                .post(HXJavaNet.url_concern, parater, new AngelNetCallBack() {
                                    @Override
                                    public void onSuccess(int ret_code, String ret_data, String msg) {
                                        if (ret_code == 200) {
                                            chat_time
                                                    .setImageResource(R.drawable.added_friend);
                                            friendInfo.relation_type = 1;
                                            GetMessageUtil
                                                    .chageSavaData(context,
                                                            friendInfo.user_id +
                                                                    "", true);
                                            if (attentionListener != null) {
                                                attentionListener
                                                        .attention(getAdapterPosition());
                                            }
                                        } else {
                                            Notifier.showShortMsg(context, "请重试");
                                        }
                                        loadingDialog.todismiss();
                                    }


                                    @Override
                                    public void onFailure(String msg) {
                                        chat_time
                                                .setImageResource(R.drawable.add_friend);
                                        loadingDialog.todismiss();
                                    }
                                });
                        iscuntean = !iscuntean;
                    } else {
                        HashMap<String, Object> parater = new HashMap<String, Object>();
                        parater.put("user_id", HXApp.getInstance()
                                                    .getUserInfo().user_id);
                        parater.put("passive_user_id", friendInfo.user_id);
                        HXJavaNet
                                .post(HXJavaNet.url_unconcern, parater, new AngelNetCallBack() {
                                    @Override
                                    public void onSuccess(int ret_code, String ret_data, String msg) {
                                        if (ret_code == 200) {
                                            chat_time
                                                    .setImageResource(R.drawable.add_friend);
                                            friendInfo.relation_type = 0;
                                            GetMessageUtil
                                                    .chageSavaData(context,
                                                            friendInfo.user_id +
                                                                    "", false);
                                            if (attentionListener != null) {
                                                attentionListener
                                                        .unAttention(getAdapterPosition());
                                            }
                                        } else {
                                            Notifier.showShortMsg(context, "请重试");
                                        }
                                        loadingDialog.todismiss();
                                    }


                                    @Override
                                    public void onFailure(String msg) {
                                        chat_time
                                                .setImageResource(R.drawable.added_friend);
                                        loadingDialog.todismiss();
                                    }
                                });
                        iscuntean = !iscuntean;
                    }
                }
            });
        }
    }

    public AttentionListener attentionListener;

    public interface AttentionListener {
        void attention(int postion);

        void unAttention(int postion);
    }
}
