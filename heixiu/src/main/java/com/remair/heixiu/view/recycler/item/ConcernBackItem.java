package com.remair.heixiu.view.recycler.item;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.remair.heixiu.R;
import com.remair.heixiu.bean.ConcernListBean;
import com.remair.heixiu.bean.ReplayList;
import com.remair.heixiu.utils.HXImageLoader;
import com.remair.heixiu.utils.RxViewUtil;
import com.remair.heixiu.utils.Utils;
import com.remair.heixiu.view.recycler.SimpleItem;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：wsk
 * 创建时间：16/8/30 下午6:39
 * 修改人：wsk
 * 修改时间：16/8/30 下午6:39
 * 修改备注：
 */
public class ConcernBackItem extends SimpleItem<ConcernListBean> {
    @BindView(R.id.chat_img) SimpleDraweeView chat_img;

    @BindView(R.id.chat_name) TextView chat_name;
    @BindView(R.id.chat_sex) ImageView chat_sex;
    @BindView(R.id.iv_grade) ImageView iv_grade;
    @BindView(R.id.tv_grade) TextView tv_grade;
    @BindView(R.id.chat_msg) TextView chat_msg;
    @BindView(R.id.chat_time) Button chat_time;
    @BindView(R.id.tv_time) TextView tv_time;
    @BindView(R.id.tv_hot_count) TextView tv_hot_count;
    private int px42;
    private Context mContext;
    View.OnClickListener mItemOnClickListener;



    public ConcernBackItem(Context context,View.OnClickListener itemOnClick) {
        mContext=context;
        mItemOnClickListener=itemOnClick;

    }


    @Override
    public int getLayoutResId() {
        return R.layout.item_zhibo;
    }


    @Override
    public void setViews() {
        super.setViews();
        px42 = Utils.getPX(42);
        RxViewUtil.viewBindClick(mRoot, mItemOnClickListener);
        
    }


    @Override
    public void handleData(ConcernListBean concernListBean, int i) {
        ReplayList replayList = (ReplayList) concernListBean;
        mRoot.setTag(replayList);
        HXImageLoader
                .loadImage(chat_img, replayList.cover_image, px42, px42);
        chat_name.setText(replayList.title);
        tv_time
                .setText(replayList.duration + "");
        int viewing_num = replayList.viewing_num;
        if (viewing_num < 0) {
            viewing_num = 0;
        }
        tv_hot_count
                .setText(viewing_num + "");

    }
}
