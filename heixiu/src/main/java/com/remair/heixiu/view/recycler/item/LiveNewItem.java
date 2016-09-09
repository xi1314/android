package com.remair.heixiu.view.recycler.item;

import android.content.Context;
import android.view.View;
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
 * 创建时间：16/8/30 下午7:40
 * 修改人：wsk
 * 修改时间：16/8/30 下午7:40
 * 修改备注：
 */
public class LiveNewItem extends SimpleItem<LiveVideoBean> {
    @BindView(R.id.item_grid) SimpleDraweeView item_grid;
    @BindView(R.id.tv_nickname) TextView tv_nickname;
    @BindView(R.id.tv_noauditing) TextView tv_noauditing;
    int px;
    private Context mContext;
    View.OnClickListener mItemOnClickListener;


    public LiveNewItem(Context context, View.OnClickListener itemOnClick) {
        mContext = context;
        mItemOnClickListener = itemOnClick;
    }


    @Override
    public int getLayoutResId() {
        return R.layout.item_grid;
    }


    @Override
    public void setViews() {
        super.setViews();
        px = Utils.getPX(130);
        RxViewUtil.viewBindClick(mRoot, mItemOnClickListener);
    }


    @Override
    public void handleData(LiveVideoBean liveVideoBean, int i) {
        final LiveVideoBean info = liveVideoBean;
        mRoot.setTag(info);
        tv_nickname.setText(info.nickname);
        if (info.state == 3) {
            tv_noauditing.setVisibility(View.VISIBLE);
        } else {
            tv_noauditing.setVisibility(View.GONE);
        }
        HXImageLoader.loadImage(item_grid, info.photo, px, px);
    }
}
