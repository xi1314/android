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
import com.remair.heixiu.activity.PwdAvActivity;
import com.remair.heixiu.bean.ConcernListBean;
import com.remair.heixiu.bean.LiveAttentionInfo;
import com.remair.heixiu.bean.LiveList;
import com.remair.heixiu.bean.ReplayList;
import com.remair.heixiu.bean.UserInfo;
import com.remair.heixiu.common.Supplier;
import com.remair.heixiu.controllers.QavsdkControl;
import com.remair.heixiu.fragment.base.RefreshListFragment;
import com.remair.heixiu.net.HXHttpUtil;
import com.remair.heixiu.net.HttpSubscriber;
import com.remair.heixiu.utils.LayoutManagerUtil;
import com.remair.heixiu.utils.Utils;
import com.remair.heixiu.view.ShowDialog;
import com.remair.heixiu.view.recycler.item.ConcernBackItem;
import com.remair.heixiu.view.recycler.item.ConcernListItem;
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
public class ConcernListFragment extends RefreshListFragment<ConcernListBean> {
    @Nullable @BindView(R.id.ptrFrameLayout)
    protected PtrFrameLayout mPtrFrameLayout;
    @BindView(R.id.recycleView) protected RecyclerView mRecycleView;
    QavsdkControl mQavsdkControl;
    UserInfo mSelfUserInfo;
    Context mContext;
    private int roomNum;
    private int mHostIdentifier;
    private int type;
    private ShowDialog showDialog;
    private WeakHandler handler;
    private SharedPreferences sp;
    private LiveAttentionInfo liveAttentionInfos;
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
        if (mPtrFrameLayout != null) {
            mPtrFrameLayout.autoRefresh();
            hasMore = false;
        }
    }


    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return LayoutManagerUtil.getLinearLayoutManager(mContext);
    }


    @Override
    protected void requestData(boolean getMore) {
        Subscription subscribe = HXHttpUtil.getInstance()
                                           .liveAttentionList(mSelfUserInfo.user_id)
                                           .subscribe(new HttpSubscriber<LiveAttentionInfo>() {
                                               @Override
                                               public void onNext(LiveAttentionInfo liveAttentionInfo) {
                                                   liveAttentionInfos = liveAttentionInfo;
                                                   List<? super
                                                           ConcernListBean> listBeen = new ArrayList<>();
                                                   if (null !=
                                                           liveAttentionInfo.liveList &&
                                                           liveAttentionInfo.liveList
                                                                   .size() >
                                                                   0) {
                                                       listBeen.addAll(liveAttentionInfo.liveList);
                                                   }
                                                   if (null !=
                                                           liveAttentionInfo.replayList &&
                                                           liveAttentionInfo.replayList
                                                                   .size() >
                                                                   0) {
                                                       listBeen.addAll(liveAttentionInfo.replayList);
                                                   }

                                                   if (listBeen.size() > 0) {
                                                       onDataSuccess((List<ConcernListBean>) listBeen);
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
    protected Supplier<AdapterItem<ConcernListBean>, ConcernListBean> getAdapterItemSupplier() {
        return new Supplier<AdapterItem<ConcernListBean>, ConcernListBean>() {
            @Override
            public AdapterItem<ConcernListBean> get(ConcernListBean concernListBean) {
                if (concernListBean instanceof LiveList) {
                    return new ConcernListItem(mContext, mItemOnClick);
                } else if (concernListBean instanceof ReplayList) {
                    return new ConcernBackItem(mContext, mItemOnClick);
                } else {
                    return null;
                }
            }
        };
    }


    View.OnClickListener mItemOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Object object = view.getTag();
            if (!Utils.isNetworkAvailable(mContext)) {
                Notifier.showShortMsg(mContext, "无网络连接");
                return;
            }
            if (mQavsdkControl.getAVContext() == null) {
                return;
            }

            if (mSelfUserInfo.forbid == 1) {
                ford();//是否封号
                return;
            }
            if (object.getClass().isAssignableFrom(LiveList.class)) {
                LiveList liveInfo = (LiveList) object;
                HXApp.getInstance().getUserInfo().isCreater = false;
                roomNum = liveInfo.room_num;
                mHostIdentifier = liveInfo.user_id;
                HXApp.getInstance().setRoomCoverPath(liveInfo.cover_image);

                Intent intent = new Intent(getActivity(), AvActivity.class);
                intent.putExtra(Utils.EXTRA_ROOM_NUM, roomNum);
                intent.putExtra(Utils.EXTRA_GROUP_ID, liveInfo.group_id);
                intent.putExtra(Utils.EXTRA_SELF_IDENTIFIER, mHostIdentifier);
                intent.putExtra("nickname", liveInfo.nickname);
                intent.putExtra("identity", liveInfo.identity);
                intent.putExtra("photo", liveInfo.photo);
                startActivity(intent);
            } else if (object.getClass().isAssignableFrom(ReplayList.class)) {
                ReplayList liveInfo = (ReplayList) object;
                String string = sp.getString(liveInfo.room_num + "", "");
                if (string.isEmpty()) {
                    startActivity(new Intent(getContext(), PwdAvActivity.class)
                            .putExtra("personinfo", liveInfo));
                    return;
                }
            }
        }
    };


    @Override
    public void onResume() {
        super.onResume();
        Logger.out("type+" + type + "+onResume");
    }


    @Override
    public void onPause() {
        super.onPause();
        Logger.out("type+" + type + "+onpause");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Logger.out("type+" + type + "+onDestroyView");
    }


    @Override
    protected String getHint2Text() {
        return getResources().getString(R.string.att_hint2);
    }


    @Override
    protected String getHint1Text() {
        return getResources().getString(R.string.att_hint1);
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
