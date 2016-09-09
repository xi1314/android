package com.remair.heixiu.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.HXJavaNet;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.base.RefreshListActivity;
import com.remair.heixiu.bean.RecommendBean;
import com.remair.heixiu.bean.UserInfo;
import com.remair.heixiu.common.Supplier;
import com.remair.heixiu.net.HXHttpUtil;
import com.remair.heixiu.net.HttpSubscriber;
import com.remair.heixiu.utils.LayoutManagerUtil;
import com.remair.heixiu.view.recycler.item.RecommendItem;
import java.util.HashMap;
import java.util.List;
import kale.adapter.item.AdapterItem;
import rx.Subscription;
import studio.archangel.toolkitv2.util.networking.AngelNetCallBack;
import studio.archangel.toolkitv2.util.text.Notifier;

/**
 * Created by JXHIUUI on 2016/3/1.
 */
public class PushActivity extends RefreshListActivity<RecommendBean.ItemsBean> implements View.OnClickListener {
    @BindView(R.id.push_on) ImageView mPush;
    @BindView(R.id.aoi_push) View mAoiPush;
    @BindView(R.id.title_txt) TextView title_txt;
    private boolean isPushOn = true;

    private UserInfo userInfo;
    HashMap<String, Object> params;


    @Override
    public int getLayoutId() {
        return R.layout.act_push;
    }


    @Override
    protected void initData() {
        title_txt.setText("推送管理");
        userInfo = HXApp.getInstance().getUserInfo();
        if (userInfo == null) {
            return;
        }
        mPush.setOnClickListener(this);
        mAoiPush.setOnClickListener(this);
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
                                           .pushManage(userInfo.user_id)
                                           .subscribe(new HttpSubscriber<RecommendBean>() {
                                               @Override
                                               public void onNext(RecommendBean recommendBean) {
                                                   List<RecommendBean.ItemsBean> itemsBeanList = recommendBean.items;
                                                   onDataSuccess(itemsBeanList);
                                               }
                                           });
        addSubscription(subscribe);
    }


    @Override
    protected Supplier<AdapterItem<RecommendBean.ItemsBean>, RecommendBean.ItemsBean> getAdapterItemSupplier() {
        return new Supplier<AdapterItem<RecommendBean.ItemsBean>, RecommendBean.ItemsBean>() {
            @Override
            public AdapterItem<RecommendBean.ItemsBean> get(RecommendBean.ItemsBean itemsBean) {
                UserInfo userInfo = HXApp.getInstance().getUserInfo();
                if (userInfo == null || mActivity == null) {
                    return null;
                }
                return new RecommendItem(mActivity, null);
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


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.push_on:
            case R.id.aoi_push:
                if (isPushOn) {
                    mPush.setImageResource(R.drawable.icon_push_off);
                    isPushOn = false;
                } else {
                    mPush.setImageResource(R.drawable.icon_push_on);
                    isPushOn = true;
                }
                HashMap<String, Object> params = new HashMap<>();
                params.put("user_id", userInfo.user_id);
                params.put("accept_push", isPushOn ? 1 : 0);
                //HXApp.getInstance().getUserInfo().token = "dfsf";
                HXJavaNet
                        .post(HXJavaNet.url_push_edituser, params, new AngelNetCallBack() {
                            @Override
                            public void onSuccess(int ret_code, String ret_data, String msg) {
                                if (ret_code == 200) {
                                    mRecycleView.setVisibility(isPushOn
                                                               ? View.VISIBLE
                                                               : View.INVISIBLE);
                                } else {
                                    Notifier.showShortMsg(getApplicationContext(), "操作失败，请重试");
                                }
                            }


                            @Override
                            public void onFailure(String msg) {
                                Notifier.showShortMsg(getApplicationContext(), "操作失败，请重试");
                            }
                        });
                break;
        }
    }
}
