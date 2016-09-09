package com.remair.heixiu.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import com.remair.heixiu.EventConstants;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.base.HXBaseActivity;
import com.remair.heixiu.activity.base.RefreshListActivity;
import com.remair.heixiu.bean.AttentionBean;
import com.remair.heixiu.bean.UserInfo;
import com.remair.heixiu.common.Supplier;
import com.remair.heixiu.net.HXHttpUtil;
import com.remair.heixiu.net.HttpSubscriber;
import com.remair.heixiu.utils.LayoutManagerUtil;
import com.remair.heixiu.view.recycler.item.BlacklistUserItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import kale.adapter.item.AdapterItem;
import org.simple.eventbus.Subscriber;
import rx.Subscription;

/**
 * Created by JXHIUUI on 2016/3/1.
 */
public class BlackListActivity extends RefreshListActivity<AttentionBean> {
    @BindView(R.id.title_txt) TextView mTitleTxt;
    @BindView(R.id.title_left_image) ImageButton mTitleLeftImage;
    private List<AttentionBean> attList;
    HashMap<String, Object> params;
    @BindView(R.id.act_record_balck_hint) LinearLayout act_record_balck_hint;
    int user_id;
    protected HXBaseActivity mActivity;


    protected void initData() {
        mTitleTxt.setText("黑名单");
        mActivity = this;
        attList = new ArrayList<>();
        if (mPtrFrameLayout != null) {
            mPtrFrameLayout.autoRefresh();
        }
    }


    protected int getLayoutId() {
        return R.layout.act_blacklist;
    }


    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return LayoutManagerUtil.getLinearLayoutManager(this);
    }


    @Override
    protected void requestData(boolean getMore) {
        if (HXApp.getInstance().getUserInfo() == null) {
            return;
        }
        Subscription subscribe = HXHttpUtil.getInstance().blacklist(mPage)
                                           .subscribe(new HttpSubscriber<List<AttentionBean>>() {
                                               @Override
                                               public void onNext(List<AttentionBean> attentionBeens) {

                                                   if (attentionBeens != null &&
                                                           !attentionBeens
                                                                   .isEmpty() &&
                                                           act_record_balck_hint
                                                                   .getVisibility() !=
                                                                   View.GONE) {
                                                       act_record_balck_hint
                                                               .setVisibility(View.GONE);
                                                   }

                                                   attList = attentionBeens;
                                                   onDataSuccess(attentionBeens);
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
    protected Supplier<AdapterItem<AttentionBean>, AttentionBean> getAdapterItemSupplier() {
        return new Supplier<AdapterItem<AttentionBean>, AttentionBean>() {
            @Override
            public AdapterItem<AttentionBean> get(AttentionBean friendInfo) {
                UserInfo userInfo = HXApp.getInstance().getUserInfo();
                if (userInfo == null || mActivity == null) {
                    return null;
                }
                return new BlacklistUserItem();
            }
        };
    }


    @Override
    protected String getHint1Text() {
        return null;
    }


    @Override
    protected String getHint2Text() {
        return null;
    }


    @Subscriber(tag = EventConstants.BLACKISITEM_CHECK)
    public void onEvent(final AttentionBean event) {
        Subscription subscribe = HXHttpUtil.getInstance()
                                           .pushBlack(event.user_id)
                                           .subscribe(new HttpSubscriber() {
                                               @Override
                                               public void onNext(Object o) {
                                                   attList.remove(event);
                                                   onDataSuccess(attList);
                                               }
                                           });
        addSubscription(subscribe);
    }


    @Subscriber(tag = EventConstants.BLACKISTV_ATT_GZCHECK)
    public void onEventItem(final AttentionBean event) {
        Subscription subscribe = HXHttpUtil.getInstance()
                                           .pushBlack(event.user_id)
                                           .subscribe(new HttpSubscriber() {
                                               @Override
                                               public void onNext(Object o) {
                                                   attList.remove(event);
                                                   onDataSuccess(attList);
                                               }
                                           });
        addSubscription(subscribe);
    }
}
