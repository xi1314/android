package com.remair.heixiu.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.badoo.mobile.util.WeakHandler;
import com.remair.heixiu.DemoConstants;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.AvActivity;
import com.remair.heixiu.bean.LiveVideoBean;
import com.remair.heixiu.bean.UserInfo;
import com.remair.heixiu.common.Supplier;
import com.remair.heixiu.controllers.QavsdkControl;
import com.remair.heixiu.fragment.base.RefreshListFragment;
import com.remair.heixiu.net.HXHttpUtil;
import com.remair.heixiu.net.HttpSubscriber;
import com.remair.heixiu.utils.LayoutManagerUtil;
import com.remair.heixiu.utils.Utils;
import com.remair.heixiu.view.ShowDialog;
import com.remair.heixiu.view.recycler.item.LiveNewItem;
import in.srain.cube.views.ptr.PtrFrameLayout;
import java.util.ArrayList;
import java.util.List;
import kale.adapter.item.AdapterItem;
import rx.Subscription;
import studio.archangel.toolkitv2.util.Logger;
import studio.archangel.toolkitv2.util.text.Notifier;

/**
 * Created by Michael
 * live tab in home page
 */
public class ShowListFragment extends RefreshListFragment<LiveVideoBean> {
    @Nullable @BindView(R.id.ptrFrameLayout)
    protected PtrFrameLayout mPtrFrameLayout;
    @BindView(R.id.recycleView) protected RecyclerView mRecycleView;
    List<LiveVideoBean> gridshows;
    QavsdkControl mQavsdkControl;
    UserInfo mSelfUserInfo;
    Context mContext;
    private int roomNum;
    private int mHostIdentifier;
    private int type;
    public static LiveVideoBean homeliveVideoBean = null;
    private ShowDialog showDialog;
    private WeakHandler handler;
    private SharedPreferences sp;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (HXApp.getInstance().getUserInfo() == null) {
                return;
            }
            if (getUserVisibleHint() && isResumed()) {
                if (HXApp.getInstance().configMessage.list_fresh_time > 5) {
                    requestData(false);
                    Logger.out("刷新" + type);
                }
            }
            handler.postDelayed(runnable,
                    HXApp.getInstance().configMessage.list_fresh_time * 1000);
        }
    };


    @Override
    public View getRootView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.frag_show_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void initData() {
        mContext = getContext();
        sp = mContext
                .getSharedPreferences(DemoConstants.LOCAL_DATA, Context.MODE_PRIVATE);
        HXApp mHXApp = HXApp.getInstance();
        mQavsdkControl = HXApp.getInstance().getQavsdkControl();
        mSelfUserInfo = mHXApp.getUserInfo();
        gridshows = new ArrayList<>();
        Bundle extras = getArguments();
        if (extras == null) {
            Notifier.showNormalMsg(mContext, "数据错误");
            return;
        }
        type = extras.getInt("type", -1);
        if (type == -1) {
            Notifier.showNormalMsg(mContext, "数据错误");
        }
        handler = new WeakHandler();
        //if (mPtrFrameLayout != null) {
        //    mPtrFrameLayout.autoRefresh();
        //    hasMore=false;
        //}
        loadData(false);
    }


    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return LayoutManagerUtil.getGridLayoutManager(mContext, 3);
    }


    @Override
    protected void requestData(boolean getMore) {
        Subscription subscribe = HXHttpUtil.getInstance()
                                           .liveLastestList(mSelfUserInfo.user_id)
                                           .subscribe(new HttpSubscriber<List<LiveVideoBean>>() {
                                               @Override
                                               public void onNext(List<LiveVideoBean> liveVideoBeenList) {
                                                   gridshows.clear();
                                                   if (null !=
                                                           liveVideoBeenList &&
                                                           liveVideoBeenList
                                                                   .size() >
                                                                   0) {

                                                       for (LiveVideoBean videoBean : liveVideoBeenList) {
                                                           if (videoBean.state ==
                                                                   3) {
                                                               gridshows
                                                                       .add(0, videoBean);
                                                           } else {
                                                               gridshows
                                                                       .add(videoBean);
                                                           }
                                                       }
                                                       onDataSuccess(gridshows);
                                                       hasMore = false;
                                                   } else {
                                                       onDataSuccess(null);
                                                   }
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
    protected Supplier<AdapterItem<LiveVideoBean>, LiveVideoBean> getAdapterItemSupplier() {
        return new Supplier<AdapterItem<LiveVideoBean>, LiveVideoBean>() {
            @Override
            public AdapterItem<LiveVideoBean> get(LiveVideoBean liveVideoBean) {
                return new LiveNewItem(mContext, mItemOnClick);
            }
        };
    }


    View.OnClickListener mItemOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mQavsdkControl.getAVContext() == null) {
                return;
            }
            if (!Utils.isNetworkAvailable(mContext)) {
                Notifier.showShortMsg(mContext, "无网络连接");
                return;
            }
            if (mSelfUserInfo.forbid == 1) {
                ford();//是否封号
                return;
            }
            LiveVideoBean liveVideoBean = (LiveVideoBean) view.getTag();
            if (liveVideoBean != null &&
                    liveVideoBean.user_id == (mSelfUserInfo.user_id)) {
                Notifier.showNormalMsg(mContext,
                        "you can't join a " + "not exist room");
                return;
            }
            HXApp.getInstance().getUserInfo().isCreater = false;
            roomNum = liveVideoBean.room_num;
            mHostIdentifier = liveVideoBean.user_id;
            homeliveVideoBean = liveVideoBean;
            HXApp.getInstance().setRoomCoverPath(liveVideoBean.cover_image);

            Intent intent = new Intent(getActivity(), AvActivity.class);
            intent.putExtra(Utils.EXTRA_ROOM_NUM, roomNum);
            intent.putExtra(Utils.EXTRA_GROUP_ID, liveVideoBean.group_id);
            intent.putExtra(Utils.EXTRA_SELF_IDENTIFIER, mHostIdentifier);
            intent.putExtra(Utils.EXTRA_PRAISE_NUM, liveVideoBean.praise_num);
            intent.putExtra("nickname", liveVideoBean.nickname);
            intent.putExtra("photo", liveVideoBean.photo);
            intent.putExtra("identity", liveVideoBean.identity);
            startActivity(intent);
        }
    };


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    protected String getHint2Text() {
        return null;
    }


    @Override
    protected String getHint1Text() {
        return getResources().getString(R.string.new_hint1);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void ford() {
        showDialog = new ShowDialog(getActivity(), getString(R.string.sethonor), getString(R.string.exit), "");
        showDialog.show();
        showDialog.setClicklistener(new ShowDialog.ClickListenerInterface() {
            @Override
            public void doConfirm() {
                showDialog.dismiss();
            }


            @Override
            public void doCancel() {
            }
        });
    }
}
