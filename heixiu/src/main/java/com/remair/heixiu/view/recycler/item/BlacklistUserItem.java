package com.remair.heixiu.view.recycler.item;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.remair.heixiu.EventConstants;
import com.remair.heixiu.R;
import com.remair.heixiu.bean.AttentionBean;
import com.remair.heixiu.utils.HXImageLoader;
import com.remair.heixiu.utils.RxViewUtil;
import com.remair.heixiu.utils.Utils;
import com.remair.heixiu.utils.Xtgrade;
import com.remair.heixiu.view.recycler.SimpleItem;
import org.simple.eventbus.EventBus;

/**
 * 项目名称：Android
 * 类描述：
 * 修改人：Liuyu
 * 修改时间：16/8/26 22:32
 * 修改备注：
 */
public class BlacklistUserItem extends SimpleItem<AttentionBean> {
    @BindView(R.id.chat_img) SimpleDraweeView chat_img;//头像
    @BindView(R.id.chat_name) TextView chat_name;//名称
    @BindView(R.id.chat_sex) ImageView chat_sex;//性别
    @BindView(R.id.tv_grade) TextView tv_grade;//等级
    @BindView(R.id.chat_msg) TextView chat_msg;//签名
    @BindView(R.id.iv_grade) SimpleDraweeView iv_grade;
    @BindView(R.id.tv_att_gz) TextView tv_att_gz;
    View.OnClickListener mItemOnClickListener;
    AttentionBean mFriendInfo;
    int chatImgW;
    int mPosition;


    @Override
    public void setViews() {
        super.setViews();
        chatImgW = Utils.getPX(42);
        RxViewUtil.viewBindClick(mRoot, mOnClickListener);
        RxViewUtil.viewBindClick(tv_att_gz, mOnClickListener);
    }


    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            EventBus.getDefault()
                    .post(mFriendInfo, EventConstants.BLACKISTV_ATT_GZCHECK);
        }
    };


    @Override
    public int getLayoutResId() {
        return R.layout.item_black;
    }


    @Override
    public void handleData(AttentionBean mattentionbean, final int position) {
        mRoot.setTag(mattentionbean);
        mFriendInfo = mattentionbean;
        mPosition = position;
        HXImageLoader
                .loadImage(chat_img, mattentionbean.photo, chatImgW, chatImgW);
        chat_name.setText(mattentionbean.nickname);
        chat_msg.setText(mattentionbean.signature);
        if (mattentionbean.gender == 0) {
            chat_sex.setImageResource(R.drawable.sex_man);
        } else if (mattentionbean.gender == 1) {
            chat_sex.setImageResource(R.drawable.sex_woman);
        }
        Xtgrade.mXtgrade(mattentionbean.grade, iv_grade, tv_grade);
    }
}
