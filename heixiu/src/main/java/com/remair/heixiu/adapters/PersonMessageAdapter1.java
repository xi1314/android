package com.remair.heixiu.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.HXJavaNet;
import com.remair.heixiu.R;
import com.remair.heixiu.bean.FriendInfo;
import com.remair.heixiu.utils.HXImageLoader;
import com.remair.heixiu.utils.Utils;
import com.remair.heixiu.utils.Xtgrade;
import java.util.HashMap;
import java.util.List;
import studio.archangel.toolkitv2.adapters.CommonRecyclerAdapter;
import studio.archangel.toolkitv2.util.networking.AngelNetCallBack;
import studio.archangel.toolkitv2.viewholders.AngelCommonViewHolder;

/**
 * Created by wsk on 16/3/1.
 */
public class PersonMessageAdapter1 extends CommonRecyclerAdapter {

    private final int px82;


    public PersonMessageAdapter1(Context context, List<FriendInfo> mList) {
        super(context, new int[] {
                R.layout.item_person_messageqq }, null, mList, false);
        px82 = Utils.getPX(82);
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
        @BindView(R.id.chat_name) TextView chat_name;
        @BindView(R.id.chat_sex) ImageView chat_sex;
        @BindView(R.id.iv_grade) SimpleDraweeView iv_grade;
        @BindView(R.id.tv_grade) TextView tv_grade;
        @BindView(R.id.chat_msg) TextView chat_msg;
        @BindView(R.id.chat_time) Button chat_time;
        @BindView(R.id.chat_img_button) ImageView chat_time1;

        boolean iscuntean = false;


        public ItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }


        @Override
        public void render(Context context, Object model) {
            final FriendInfo friendInfo = (FriendInfo) model;

            HXImageLoader.loadImage(chat_img, friendInfo.photo, px82, px82);
            chat_name.setText(friendInfo.nickname);
            chat_msg.setText(friendInfo.signature + "");
            tv_grade.setText(friendInfo.grade + "");
            if (friendInfo.gender == 0) {
                chat_sex.setImageResource(R.drawable.sex_man);
            } else {
                chat_sex.setImageResource(R.drawable.sex_woman);
            }
            Xtgrade.mXtgrade(friendInfo.grade, iv_grade, tv_grade);
            if (friendInfo.relation_type == 0) {
                chat_time.setText("＋关注");
            } else {
                chat_time.setText("已关注");
            }

            chat_img.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (!iscuntean) {
                        chat_time1.setVisibility(View.GONE);
                        iscuntean = true;
                    } else {
                        chat_time1.setVisibility(View.VISIBLE);
                        iscuntean = false;
                    }
                }
            });

            chat_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                                            chat_time.setText("已关注");
                                        }
                                    }


                                    @Override
                                    public void onFailure(String msg) {
                                        chat_time.setText("＋关注");
                                    }
                                });
                        iscuntean = true;
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
                                            chat_time.setText("＋关注");
                                        }
                                    }


                                    @Override
                                    public void onFailure(String msg) {
                                        chat_time.setText("已关注");
                                    }
                                });
                        iscuntean = false;
                    }
                }
            });
        }
    }
}
