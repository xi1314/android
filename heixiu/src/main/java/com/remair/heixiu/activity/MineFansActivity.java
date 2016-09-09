package com.remair.heixiu.activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.base.RefreshListActivity;
import com.remair.heixiu.bean.FriendInfo;
import com.remair.heixiu.common.Supplier;
import com.remair.heixiu.net.HXHttpUtil;
import com.remair.heixiu.net.HttpSubscriber;
import com.remair.heixiu.utils.LayoutManagerUtil;
import com.remair.heixiu.view.recycler.item.CommonUserItem;
import java.util.List;
import kale.adapter.item.AdapterItem;
import rx.Subscription;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：JXH
 * 创建时间：2016/3/2 13:58
 * 修改人：JXH
 * 修改时间：2016/3/2 13:58
 * 修改备注：
 */
public class MineFansActivity extends RefreshListActivity<FriendInfo> {

    @BindView(R.id.title_txt) TextView mTitleText;
    private int mUserId;


    @Override
    protected void initData() {
        mTitleText.setText(R.string.fans);
        mUserId = getIntent().getIntExtra("user_id", 0);
        if (mPtrFrameLayout != null) {
            mPtrFrameLayout.autoRefresh();
        }
    }


    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return LayoutManagerUtil.getLinearLayoutManager(this);
    }


    @Override
    protected void requestData(boolean getMore) {
        Subscription subscribe = HXHttpUtil.getInstance()
                                           .fansList(mUserId, mUserId, mPage)
                                           .subscribe(new HttpSubscriber<List<FriendInfo>>() {
                                               @Override
                                               public void onNext(List<FriendInfo> friendInfos) {
                                                   onDataSuccess(friendInfos);
                                               }


                                               @Override
                                               public void onError(Throwable e) {
                                                   super.onError(e);
                                                   onDataSuccess(null);
                                               }
                                           });
        addSubscription(subscribe);
    }


    @Override
    protected Supplier<AdapterItem<FriendInfo>, FriendInfo> getAdapterItemSupplier() {
        return new Supplier<AdapterItem<FriendInfo>, FriendInfo>() {
            @Override
            public AdapterItem<FriendInfo> get(FriendInfo friendInfo) {
                if (null == mActivity) {
                    return null;
                }
                return new CommonUserItem(mActivity, true, null, itemClicklisnter);
            }
        };
    }


    @Override
    protected String getHint1Text() {
        return getResources().getString(R.string.fans_hint1);
    }


    @Override
    protected String getHint2Text() {
        return getResources().getString(R.string.fans_hint2);
    }


    View.OnClickListener itemClicklisnter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FriendInfo friendInfo = (FriendInfo) v.getTag();
            startActivity(new Intent(MineFansActivity.this,
                    FriendMessageActivity.class)
                    .putExtra("viewed_user_id", friendInfo.user_id));
        }
    };
}
