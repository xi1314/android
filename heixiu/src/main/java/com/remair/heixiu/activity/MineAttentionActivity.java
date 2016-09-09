package com.remair.heixiu.activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.BindView;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.base.RefreshListActivity;
import com.remair.heixiu.bean.FriendInfo;
import com.remair.heixiu.bean.UserInfo;
import com.remair.heixiu.common.Supplier;
import com.remair.heixiu.net.HXHttpUtil;
import com.remair.heixiu.net.HttpSubscriber;
import com.remair.heixiu.utils.LayoutManagerUtil;
import com.remair.heixiu.utils.RxViewUtil;
import com.remair.heixiu.view.recycler.item.CommonUserItem;
import java.util.List;
import kale.adapter.item.AdapterItem;
import rx.Subscription;
import rx.functions.Action1;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：JXH
 * 创建时间：2016/3/2 13:57
 * 修改人：JXH
 * 修改时间：2016/3/2 13:57
 * 修改备注：
 */
public class MineAttentionActivity extends RefreshListActivity<FriendInfo> {

    @BindView(R.id.title_txt) TextView mTitleTxt;
    @BindView(R.id.title_right_image) ImageButton mTitleRightImage;
    int userId;


    @Override
    public int getLayoutId() {
        return R.layout.act_mineattention;
    }


    @Override
    protected void initData() {
        userId = getIntent().getIntExtra("user_id", 0);
        mTitleTxt.setText("关注");
        mTitleRightImage.setImageResource(R.drawable.icon_search_wihte);
        mTitleRightImage.setVisibility(View.VISIBLE);
        RxViewUtil.viewBindClick(mTitleRightImage, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent it = new Intent(mActivity, SearchActivity.class);
                startActivity(it);
            }
        });
        if (mPtrFrameLayout != null) {
            mPtrFrameLayout.autoRefresh();
        }
    }


    @Override
    protected void requestData(boolean getMore) {
        if (HXApp.getInstance().getUserInfo() == null) {
            return;
        }
        Subscription subscribe = HXHttpUtil.getInstance()
                                           .friendList(userId, mPage)
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
        //添加RX订阅管理，在activity关闭的时候取消订阅
        addSubscription(subscribe);
    }


    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return LayoutManagerUtil.getLinearLayoutManager(this);
    }


    @Override
    public Supplier<AdapterItem<FriendInfo>, FriendInfo> getAdapterItemSupplier() {
        return new Supplier<AdapterItem<FriendInfo>, FriendInfo>() {
            @Override
            public AdapterItem<FriendInfo> get(FriendInfo friendInfo) {
                UserInfo userInfo = HXApp.getInstance().getUserInfo();
                if (userInfo == null || mActivity == null) {
                    return null;
                }
                boolean b = userInfo.user_id == userId;
                return new CommonUserItem(mActivity, b, null, mItemOnClick);
            }
        };
    }


    @Override
    protected String getHint1Text() {
        return getString(R.string.att_hint1);
    }


    @Override
    protected String getHint2Text() {
        return getString(R.string.att_hint2);
    }


    View.OnClickListener mItemOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getTag() == null ||
                    !(v.getTag() instanceof FriendInfo) ||
                    mActivity == null) {
                return;
            }
            FriendInfo friendInfo = (FriendInfo) v.getTag();
            Intent intent = new Intent(mActivity, FriendMessageActivity.class);
            intent.putExtra("viewed_user_id", friendInfo.user_id);
            MineAttentionActivity.this.startActivity(intent);
        }
    };
}