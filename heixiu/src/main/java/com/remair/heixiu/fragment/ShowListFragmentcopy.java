package com.remair.heixiu.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.badoo.mobile.util.WeakHandler;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.OnClickLister;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.AvActivity;
import com.remair.heixiu.activity.WheelViewMessageActivity;
import com.remair.heixiu.bean.LiveVideoBean;
import com.remair.heixiu.bean.UserInfo;
import com.remair.heixiu.bean.WhellBean;
import com.remair.heixiu.common.Supplier;
import com.remair.heixiu.controllers.QavsdkControl;
import com.remair.heixiu.fragment.base.RefreshListFragment;
import com.remair.heixiu.net.HXHttpUtil;
import com.remair.heixiu.net.HttpSubscriber;
import com.remair.heixiu.utils.LayoutManagerUtil;
import com.remair.heixiu.utils.RSAUtils;
import com.remair.heixiu.utils.Utils;
import com.remair.heixiu.view.Kanner;
import com.remair.heixiu.view.ShowDialog;
import com.remair.heixiu.view.recycler.item.LiveVideoGridItem;
import com.remair.heixiu.view.recycler.item.LiveVideoItem;
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
public class ShowListFragmentcopy extends RefreshListFragment<LiveVideoBean> implements OnClickLister {
    @Nullable @BindView(R.id.ptrFrameLayout)
    protected PtrFrameLayout mPtrFrameLayout;
    @BindView(R.id.recycleView) protected RecyclerView mRecycleView;
    ArrayList<LiveVideoBean> shows;
    ArrayList<LiveVideoBean> gridshows;
    private String encrypt;
    QavsdkControl mQavsdkControl;
    UserInfo mSelfUserInfo;
    private int roomNum;
    private int mHostIdentifier;
    private int type;
    public static LiveVideoBean homeliveVideoBean = null;
    private ShowDialog showDialog;
    private boolean ListOrGrid = false;//列表布局显示切换
    private WeakHandler handler;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
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
    private Context mContext;


    private void initdata(final Kanner mkanner) {
        final UserInfo userInfo = HXApp.getInstance().getUserInfo();
        if (userInfo == null) {
            return;
        }
        Subscription subscribe = HXHttpUtil.getInstance()
                                           .carouselList(userInfo.user_id)
                                           .subscribe(new HttpSubscriber<List<WhellBean>>() {
                                               @Override
                                               public void onNext(final List<WhellBean> whellBeans) {
                                                   if (whellBeans.size() == 0) {
                                                       mkanner.setVisibility(View.GONE);
                                                   } else {
                                                       List<String> urlList = new ArrayList<>();

                                                       for (WhellBean whellBean : whellBeans) {

                                                           urlList.add(whellBean.image);
                                                       }

                                                       mkanner.setImagesUrl(urlList);

                                                       mkanner.setOnItemClickListener(new Kanner.OnItemClickListener() {

                                                           @Override
                                                           public void OnItemClick(View view, int position) {
                                                               if (null !=
                                                                       whellBeans &&
                                                                       whellBeans
                                                                               .size() >
                                                                               0) {
                                                                   try {
                                                                       String source =
                                                                               userInfo.identity +
                                                                                       "," +
                                                                                       userInfo.user_id;
                                                                       encrypt = RSAUtils
                                                                               .encrypt(source);
                                                                       Logger.out(encrypt);
                                                                   } catch (Exception e) {
                                                                       e.printStackTrace();
                                                                   }
                                                                   Intent intent = new Intent(mContext, WheelViewMessageActivity.class);
                                                                   WhellBean whellBean = whellBeans
                                                                           .get(position);
                                                                   if (null !=
                                                                           whellBean) {
                                                                       intent.putExtra("url", whellBean.url);
                                                                       intent.putExtra("title", whellBean.title);
                                                                       intent.putExtra("carousel_id", whellBean.carousel_id);
                                                                       if (encrypt !=
                                                                               null) {
                                                                           intent.putExtra("data", encrypt);
                                                                       }
                                                                       startActivity(intent);
                                                                   }
                                                               }
                                                           }
                                                       });
                                                   }
                                               }


                                               @Override
                                               public void onError(Throwable e) {
                                                   super.onError(e);
                                                   mkanner.setVisibility(View.GONE);
                                               }
                                           });
        addSubscription(subscribe);
    }


    @Override
    public View getRootView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.frag_show_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void initData() {
        mContext = getContext();
        handler = new WeakHandler();
        handler.postDelayed(runnable,
                HXApp.getInstance().configMessage.list_fresh_time * 1000);
        HXApp mHXApp = HXApp.getInstance();
        mQavsdkControl = HXApp.getInstance().getQavsdkControl();
        mSelfUserInfo = mHXApp.getUserInfo();
        type = getArguments().getInt("type", -1);
        shows = new ArrayList<>();
        gridshows = new ArrayList<>();
        if (mPtrFrameLayout != null) {
            mPtrFrameLayout.autoRefresh();
            hasMore = false;
        }
    }


    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        if (HXApp.getInstance().ListOrGrid) {
            return LayoutManagerUtil.getGridLayoutManager(getContext(), 2);
        } else {
            return LayoutManagerUtil.getLinearLayoutManager(getContext());
        }
    }


    @Override
    protected void requestData(boolean getMore) {
        int user_id = HXApp.getInstance().getUserInfo().user_id;
        Subscription subscribe = HXHttpUtil.getInstance().liveHotList(user_id)
                                           .subscribe(new HttpSubscriber<List<LiveVideoBean>>() {
                                               @Override
                                               public void onNext(List<LiveVideoBean> liveVideoBeanLists) {
                                                   if (null != shows) {
                                                       shows.clear();
                                                   }
                                                   onDataSuccess(liveVideoBeanLists);
                                                   if (liveVideoBeanLists
                                                           .size() > 0) {
                                                       onDataSuccess(liveVideoBeanLists);
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


                                               @Override
                                               public void onCompleted() {
                                                   super.onCompleted();
                                               }
                                           });
        addSubscription(subscribe);
    }


    @Override
    public View initHeaderView() {
        mview = LayoutInflater.from(mContext)
                              .inflate(R.layout.item_showtwo, null);
        final Kanner mkanner = (Kanner) mview
                .findViewById(R.id.network_indicate_view);
        initdata(mkanner);

        return mview;
    }


    @Override
    protected Supplier<AdapterItem<LiveVideoBean>, LiveVideoBean> getAdapterItemSupplier() {
        return new Supplier<AdapterItem<LiveVideoBean>, LiveVideoBean>() {
            @Override
            public AdapterItem<LiveVideoBean> get(LiveVideoBean liveVideoBean) {
                UserInfo userInfo = HXApp.getInstance().getUserInfo();
                if (userInfo == null || mContext == null) {
                    return null;
                }
                if (HXApp.getInstance().ListOrGrid) {
                    return new LiveVideoGridItem(mContext, mItemOnClick);
                } else {
                    return new LiveVideoItem(mContext, mItemOnClick);
                }
            }
        };
    }


    View.OnClickListener mItemOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mQavsdkControl.getAVContext() == null) {
                Logger.out("请重启应用");
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
            if (view.getTag() == null ||
                    !(view.getTag() instanceof LiveVideoBean) ||
                    mContext == null) {
                return;
            }
            LiveVideoBean liveVideoBean = (LiveVideoBean) view.getTag();
            if (liveVideoBean != null &&
                    liveVideoBean.user_id == mSelfUserInfo.user_id) {
                Notifier.showShortMsg(mContext, "you can't join a not exist room");
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
            intent.putExtra("nickname", liveVideoBean.nickname);
            intent.putExtra("viewed_num", liveVideoBean.viewing_num);
            intent.putExtra("praise_num", liveVideoBean.praise_num);
            intent.putExtra("photo", liveVideoBean.photo);
            intent.putExtra("identity", liveVideoBean.identity);
            startActivity(intent);
        }
    };

    View mview;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }


    @Override
    public void onResume() {
        super.onResume();
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


    @Override
    public void onClick() {
        getLayoutManager();
        requestData(true);
        getAdapterItemSupplier();
    }
}
