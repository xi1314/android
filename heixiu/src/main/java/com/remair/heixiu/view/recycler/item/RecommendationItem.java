package com.remair.heixiu.view.recycler.item;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import butterknife.BindView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.remair.heixiu.R;
import com.remair.heixiu.bean.FriendInfo;
import com.remair.heixiu.utils.HXImageLoader;
import com.remair.heixiu.utils.RxViewUtil;
import com.remair.heixiu.utils.Utils;
import com.remair.heixiu.view.recycler.SimpleItem;

/**
 * 项目名称：Android
 * 类描述：推荐列表
 * 创建人：wsk
 * 创建时间：16/8/31 下午8:54
 * 修改人：wsk
 * 修改时间：16/8/31 下午8:54
 * 修改备注：
 */
public class RecommendationItem extends SimpleItem<FriendInfo> {
    @BindView(R.id.chat_img) SimpleDraweeView chat_img;
    @BindView(R.id.corn_btn) Button corn_btn;
    @BindView(R.id.chat_img_button) ImageView chat_time1;

    boolean iscuntean = false;
    private int px82;
    View.OnClickListener mItemOnClickListener;


    public RecommendationItem(View.OnClickListener itemOnClickListener) {
        mItemOnClickListener=itemOnClickListener;
    }


    @Override
    public int getLayoutResId() {
        return R.layout.item_person_messageqq;
    }


    @Override
    public void setViews() {
        super.setViews();
        px82 = Utils.getPX(82);
        RxViewUtil.viewBindClick(mRoot, mItemOnClickListener);
    }


    @Override
    public void handleData(FriendInfo friendInfo, int i) {
        mRoot.setTag(friendInfo);
        HXImageLoader.loadImage(chat_img, friendInfo.photo, px82, px82);
        if (friendInfo.relation_type == 0) {
            corn_btn.setText("＋关注");
        } else {
            corn_btn.setText("已关注");
        }
        RxViewUtil.viewBindClick(chat_img, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!iscuntean) {
                    chat_time1.setVisibility(View.GONE);
                    iscuntean = true;
                } else {
                    chat_time1.setVisibility(View.VISIBLE);
                    iscuntean = false;
                }
            }
        });

    }
}
