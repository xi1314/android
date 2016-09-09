package com.remair.heixiu.view.recycler.item;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import butterknife.BindView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.remair.heixiu.R;
import com.remair.heixiu.bean.ConcernListBean;
import com.remair.heixiu.bean.LiveList;
import com.remair.heixiu.utils.HXImageLoader;
import com.remair.heixiu.utils.RxViewUtil;
import com.remair.heixiu.utils.Utils;
import com.remair.heixiu.view.recycler.SimpleItem;

/**
 * 项目名称：Android
 * 类描述：关注列表直播
 * 创建人：wsk
 * 创建时间：16/8/30 下午6:10
 * 修改人：wsk
 * 修改时间：16/8/30 下午6:10
 * 修改备注：
 */
public class ConcernListItem extends SimpleItem<ConcernListBean> {
    @BindView(R.id.item_show_back) SimpleDraweeView iv_back;
    @BindView(R.id.item_show_amount) TextView tv_amount;
    @BindView(R.id.item_show_avatar) SimpleDraweeView iv_avatar;
    @BindView(R.id.item_show_name) TextView tv_name;
    @BindView(R.id.item_show_location) TextView tv_location;
    @BindView(R.id.item_show_des) TextView item_show_des;
    @BindView(R.id.tv_type) TextView mTvType;
    private int mWidth;
    private int px34;
    private int px42;
    WindowManager wm;
    private Context mContext;
    View.OnClickListener mItemOnClickListener;

    public ConcernListItem(Context context,View.OnClickListener itemOnClick) {
        mContext = context;
        mItemOnClickListener=itemOnClick;
    }


    @Override
    public int getLayoutResId() {
        return R.layout.item_show;
    }


    @Override
    public void setViews() {
        super.setViews();
        wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        mWidth = displayMetrics.widthPixels;
        px34 = Utils.getPX(34);
        px42 = Utils.getPX(42);
        RxViewUtil.viewBindClick(mRoot, mItemOnClickListener);
    }


    @Override
    public void handleData(ConcernListBean concernListBean, int i) {
        LiveList liveList = (LiveList) concernListBean;
        mRoot.setTag(liveList);

        HXImageLoader
                .loadImage(iv_avatar, liveList.photo, px34, px34);
        HXImageLoader
                .loadImage(iv_back, liveList.cover_image, mWidth, mWidth);
        int viewing_num = liveList.viewing_num;
        if (viewing_num < 0) {
            viewing_num = 0;
        }
        tv_amount
                .setText(viewing_num + "人正在观看");
        tv_name.setText(liveList.nickname);
        tv_location.setText(liveList.address);
        if (liveList.title.isEmpty()) {
            item_show_des
                    .setVisibility(View.GONE);
        } else {
            item_show_des
                    .setText(liveList.title);
        }
        if (liveList.liveType == 0) {
            mTvType.setText("直播中");
        } else {
            mTvType.setText("私密直播中");
        }
    }
}
