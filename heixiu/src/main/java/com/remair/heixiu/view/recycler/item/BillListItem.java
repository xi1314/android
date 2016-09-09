package com.remair.heixiu.view.recycler.item;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.FriendMessageActivity;
import com.remair.heixiu.bean.FansInfoBean;
import com.remair.heixiu.utils.HXImageLoader;
import com.remair.heixiu.utils.RxViewUtil;
import com.remair.heixiu.utils.Utils;
import com.remair.heixiu.utils.Xtgrade;
import com.remair.heixiu.view.recycler.SimpleItem;
import rx.functions.Action1;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：JXH
 * 创建时间：2016/8/29 16:41
 * 修改人：JXH
 * 修改时间：2016/8/29 16:41
 * 修改备注：
 */
public class BillListItem extends SimpleItem<FansInfoBean> {

    @BindView(R.id.iv_bill_grade) ImageView mIvBillGrade;
    @BindView(R.id.tv_bill_grade) TextView mTvBillGrade;
    @BindView(R.id.chat_img) SimpleDraweeView mChatImg;
    @BindView(R.id.chat_name) TextView mChatName;
    @BindView(R.id.chat_sex) ImageView mChatSex;
    @BindView(R.id.iv_grade) SimpleDraweeView mIvGrade;
    @BindView(R.id.tv_grade) TextView mTvGrade;
    @BindView(R.id.el_message) LinearLayout mElMessage;
    @BindView(R.id.chat_msg) TextView mChatMsg;
    @BindView(R.id.phone_name) TextView mPhoneName;
    @BindView(R.id.tv_value) TextView mTvValue;
    @BindView(R.id.ll_item) LinearLayout mLlItem;
    private int type;
    private final int mPx;
    FansInfoBean mFansInfoBean;


    public BillListItem(int type) {
        this.type = type;
        mPx = Utils.getPX(42);
    }


    @Override
    public int getLayoutResId() {
        return R.layout.item_bill_list;
    }


    @Override
    public void setViews() {
        super.setViews();
        RxViewUtil.viewBindClick(mRoot, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (mFansInfoBean != null && mRoot.getContext() != null) {
                    Intent intent = new Intent(mRoot
                            .getContext(), FriendMessageActivity.class);
                    intent.putExtra("viewed_user_id", mFansInfoBean.user_id);
                    mRoot.getContext().startActivity(intent);
                }
            }
        });
    }


    @Override
    public void handleData(FansInfoBean fansInfoBean, int i) {
        mFansInfoBean = fansInfoBean;
        if (type == 1) {
            if (i <= 2) {
                mIvBillGrade.setVisibility(View.VISIBLE);
                mTvBillGrade.setVisibility(View.GONE);
                if (i == 0) {
                    mIvBillGrade.setImageResource(R.drawable.bill_list_jg);
                } else if (i == 1) {
                    mIvBillGrade.setImageResource(R.drawable.bill_list_yg);
                } else if (i == 2) {
                    mIvBillGrade.setImageResource(R.drawable.bill_list_tg);
                }
            } else {
                mIvBillGrade.setVisibility(View.GONE);
                mTvBillGrade.setVisibility(View.VISIBLE);
            }
        }
        if (type == 0) {
            mTvBillGrade.setText(String.valueOf(i + 4));
        } else {
            mTvBillGrade.setText(String.valueOf(i + 1));
        }

        HXImageLoader.loadImage(mChatImg, fansInfoBean.photo, mPx, mPx);
        mChatName.setText(fansInfoBean.nickname);
        if (fansInfoBean.gender == 0) {
            mChatSex.setImageResource(R.drawable.sex_man);
        } else {
            mChatSex.setImageResource(R.drawable.sex_woman);
        }

        Xtgrade.mXtgrade(fansInfoBean.grade, mIvGrade, mTvGrade);
        mChatMsg.setText(fansInfoBean.signature);
        mTvValue.setText(Xtgrade
                .moneynumber(fansInfoBean.charm_value_sent + ""));
    }
}
