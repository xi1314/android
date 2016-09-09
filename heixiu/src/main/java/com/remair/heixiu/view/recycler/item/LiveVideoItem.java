package com.remair.heixiu.view.recycler.item;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import butterknife.BindView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.remair.heixiu.R;
import com.remair.heixiu.bean.LiveVideoBean;
import com.remair.heixiu.utils.HXImageLoader;
import com.remair.heixiu.utils.RxViewUtil;
import com.remair.heixiu.utils.Utils;
import com.remair.heixiu.view.recycler.SimpleItem;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：wsk
 * 创建时间：16/8/30 下午2:59
 * 修改人：wsk
 * 修改时间：16/8/30 下午2:59
 * 修改备注：
 */
public class LiveVideoItem extends SimpleItem<LiveVideoBean> {
    @BindView(R.id.item_show_back) SimpleDraweeView iv_back;
    @BindView(R.id.item_show_amount) TextView tv_amount;
    @BindView(R.id.item_show_avatar) SimpleDraweeView iv_avatar;
    @BindView(R.id.item_show_name) TextView tv_name;
    @BindView(R.id.item_show_location) TextView tv_location;
    @BindView(R.id.item_show_des) TextView item_show_des;
    Context mContext;
    WindowManager wm;
    private int mWidth;
    private int px34;
    View.OnClickListener mItemOnClickListener;


    public LiveVideoItem(Context context, View.OnClickListener itemOnClick) {
        this.mContext = context;
        this.mItemOnClickListener = itemOnClick;
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
        RxViewUtil.viewBindClick(mRoot, mItemOnClickListener);
    }


    @Override
    public void handleData(LiveVideoBean liveVideoBean, int i) {
        mRoot.setTag(liveVideoBean);
        HXImageLoader.loadImage(iv_avatar, liveVideoBean.photo, px34, px34);
        HXImageLoader
                .loadImage(iv_back, liveVideoBean.cover_image, mWidth, mWidth);
        int viewing_num = liveVideoBean.viewing_num;
        if (viewing_num < 0) {
            viewing_num = 0;
        }
        tv_amount.setText(viewing_num + "人正在观看");
        tv_name.setText(liveVideoBean.nickname);
        tv_location.setText(liveVideoBean.address);
        if (liveVideoBean.title.isEmpty()) {
            item_show_des.setVisibility(View.GONE);
        } else {
            item_show_des.setText(liveVideoBean.title);
        }
    }
}
