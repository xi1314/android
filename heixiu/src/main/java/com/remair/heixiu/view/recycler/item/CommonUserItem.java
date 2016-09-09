package com.remair.heixiu.view.recycler.item;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.HXJavaNet;
import com.remair.heixiu.R;
import com.remair.heixiu.bean.FriendInfo;
import com.remair.heixiu.bean.UserInfo;
import com.remair.heixiu.utils.GetMessageUtil;
import com.remair.heixiu.utils.HXImageLoader;
import com.remair.heixiu.utils.RxViewUtil;
import com.remair.heixiu.utils.Utils;
import com.remair.heixiu.utils.Xtgrade;
import com.remair.heixiu.view.LoadingDialog;
import com.remair.heixiu.view.recycler.SimpleItem;
import java.util.HashMap;
import rx.functions.Action1;
import studio.archangel.toolkitv2.util.networking.AngelNetCallBack;
import studio.archangel.toolkitv2.util.text.Notifier;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：LiuJun
 * 创建时间：16/8/26 22:32
 * 修改人：LiuJun
 * 修改时间：16/8/26 22:32
 * 修改备注：
 */
public class CommonUserItem extends SimpleItem<FriendInfo> {

    @BindView(R.id.chat_img) SimpleDraweeView mChatImg;
    @BindView(R.id.chat_name) TextView mChatName;
    @BindView(R.id.chat_sex) ImageView mChatSex;
    @BindView(R.id.iv_grade) SimpleDraweeView mIvGrade;
    @BindView(R.id.tv_grade) TextView mTvGrade;
    @BindView(R.id.chat_msg) TextView mChatMsg;
    @BindView(R.id.phone_name) TextView mPhoneName;
    @BindView(R.id.chat_time) ImageButton mChatTime;
    @BindView(R.id.iv_secret) ImageView mIvSecret;
    @BindView(R.id.iv_public) ImageView mIvPublic;

    Context mContext;

    AttentionListener mAttentionListener;

    View.OnClickListener mItemOnClickListener;

    boolean mBisuser;

    boolean iscuntean = false;

    int chatImgW;

    LoadingDialog loadingDialog;

    FriendInfo mFriendInfo;

    int mPosition;


    public CommonUserItem(Context context, boolean bisuser, AttentionListener attentionListener, View.OnClickListener itemOnClick) {
        mBisuser = bisuser;
        mContext = context;
        mAttentionListener = attentionListener;
        mItemOnClickListener = itemOnClick;
    }


    @Override
    public void setViews() {
        super.setViews();
        chatImgW = Utils.getPX(42);
        RxViewUtil.viewBindClick(mRoot, mItemOnClickListener);
        RxViewUtil.viewBindClick(mChatTime, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (mFriendInfo != null) {
                    onChatTimeClick();
                }
            }
        });
    }


    @Override
    public int getLayoutResId() {
        return R.layout.item_person_message;
    }


    @Override
    public void handleData(final FriendInfo friendInfo, final int position) {
        mRoot.setTag(friendInfo);
        mFriendInfo = friendInfo;
        mPosition = position;
        mIvSecret.setVisibility(View.GONE);
        mIvPublic.setVisibility(View.GONE);
        HXImageLoader.loadImage(mChatImg, friendInfo.photo, chatImgW, chatImgW);
        //if (friendInfo.liveState == 2) {
        //    //私密直播
        //    mIvSecret.setVisibility(View.VISIBLE);
        //}
        //if (friendInfo.liveState == 1) {
        //    //公开直播
        //    mIvPublic.setVisibility(View.VISIBLE);
        //}
        mChatName.setText(friendInfo.nickname);
        if (null == friendInfo.signature) {
            mPhoneName.setText( mContext.getString(R.string.charm_value)+
                    friendInfo
                    .charm_value);
        } else {
            mChatMsg.setText(friendInfo.signature + "");
        }
        mTvGrade.setText(friendInfo.grade + "");
        if (friendInfo.gender == 0) {
            mChatSex.setImageResource(R.drawable.sex_man);
        } else {
            mChatSex.setImageResource(R.drawable.sex_woman);
        }
        Xtgrade.mXtgrade(friendInfo.grade, mIvGrade, mTvGrade);

        if (!mBisuser) {
            mChatTime.setVisibility(View.GONE);
        } else {
            mChatTime.setVisibility(View.VISIBLE);
        }
        if (friendInfo.relation_type == 0) {
            mChatTime.setImageResource(R.drawable.add_friend);
            iscuntean = false;
        } else if (friendInfo.relation_type == 1) {
            mChatTime.setImageResource(R.drawable.added_friend);
            iscuntean = true;
        } else {
            mChatTime.setImageResource(R.drawable.add_friend);
            iscuntean = false;
        }
    }


    void onChatTimeClick() {
        UserInfo userInfo = HXApp.getInstance().getUserInfo();
        if (userInfo == null) {
            return;
        }
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(mContext, R.style.dialog);
        }
        loadingDialog.show();
        if (!iscuntean) {
            HashMap<String, Object> parater = new HashMap<>();
            parater.put("user_id", userInfo.user_id);
            parater.put("passive_user_id", mFriendInfo.user_id);
            HXJavaNet
                    .post(HXJavaNet.url_concern, parater, new AngelNetCallBack() {
                        @Override
                        public void onSuccess(int ret_code, String ret_data, String msg) {
                            if (ret_code == 200) {
                                mChatTime
                                        .setImageResource(R.drawable.added_friend);
                                mFriendInfo.relation_type = 1;
                                GetMessageUtil.chageSavaData(mContext,
                                        mFriendInfo.user_id + "", true);
                                if (mAttentionListener != null) {
                                    mAttentionListener.attention(mPosition);
                                }
                            } else {
                                Notifier.showShortMsg(mContext, "请重试");
                            }
                            loadingDialog.todismiss();
                        }


                        @Override
                        public void onFailure(String msg) {
                            mChatTime.setImageResource(R.drawable.add_friend);
                            loadingDialog.todismiss();
                        }
                    });
            iscuntean = !iscuntean;
        } else {
            HashMap<String, Object> parater = new HashMap<>();
            parater.put("user_id", userInfo.user_id);
            parater.put("passive_user_id", mFriendInfo.user_id);
            HXJavaNet
                    .post(HXJavaNet.url_unconcern, parater, new AngelNetCallBack() {
                        @Override
                        public void onSuccess(int ret_code, String ret_data, String msg) {
                            if (ret_code == 200) {
                                mChatTime
                                        .setImageResource(R.drawable.add_friend);
                                mFriendInfo.relation_type = 0;
                                GetMessageUtil.chageSavaData(mContext,
                                        mFriendInfo.user_id + "", false);
                                if (mAttentionListener != null) {
                                    mAttentionListener.unAttention(mPosition);
                                }
                            } else {
                                Notifier.showShortMsg(mContext, "请重试");
                            }
                            loadingDialog.todismiss();
                        }


                        @Override
                        public void onFailure(String msg) {
                            mChatTime.setImageResource(R.drawable.added_friend);
                            loadingDialog.todismiss();
                        }
                    });
            iscuntean = !iscuntean;
        }
    }


    public interface AttentionListener {
        void attention(int postion);

        void unAttention(int postion);
    }
}
