package com.remair.heixiu.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.R;
import com.remair.heixiu.bean.FansInfoBean;
import com.remair.heixiu.bean.RankListBean;
import com.remair.heixiu.bean.UserInfo;
import com.remair.heixiu.common.Supplier;
import com.remair.heixiu.fragment.base.RefreshListFragment;
import com.remair.heixiu.net.HXHttpUtil;
import com.remair.heixiu.net.HttpSubscriber;
import com.remair.heixiu.utils.LayoutManagerUtil;
import com.remair.heixiu.view.BillListHeadView;
import com.remair.heixiu.view.BlankHintView;
import com.remair.heixiu.view.recycler.HXCommonAdapter;
import com.remair.heixiu.view.recycler.item.BillListItem;
import java.util.ArrayList;
import java.util.List;
import kale.adapter.RcvAdapterWrapper;
import kale.adapter.item.AdapterItem;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import rx.Subscription;
import rx.functions.Func1;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：JXH
 * 创建时间：2016/7/18 15:24
 * 修改人：JXH
 * 修改时间：2016/7/18 15:24
 * 修改备注：
 */
public class  BillListFragment extends RefreshListFragment<FansInfoBean> {

    private int type = -1;//0粉丝榜 1贡献榜
    UserInfo userInfo;
    private int viewed_user_id = 0;
    private BillListHeadView mHeaderView;
    private List<FansInfoBean> mfansList;
    private boolean haveFoot = true;


    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return LayoutManagerUtil.getLinearLayoutManager(getActivity());
    }


    @Override
    protected Supplier<AdapterItem<FansInfoBean>, FansInfoBean> getAdapterItemSupplier() {
        return new Supplier<AdapterItem<FansInfoBean>, FansInfoBean>() {
            @Override
            public AdapterItem<FansInfoBean> get(FansInfoBean fansInfoBean) {
                return new BillListItem(type);
            }
        };
    }


    @Override
    public View getRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_bill, container, false);
    }


    @Override
    public void initData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            type = arguments.getInt("type");
            viewed_user_id = arguments.getInt("viewed_user_id");
        }
        if (type == -1 || viewed_user_id == 0) {
            return;
        }
        userInfo = HXApp.getInstance().getUserInfo();
        if (null == userInfo) {
            return;
        }
        //userInfo.token = "dfsd";
        loadData(false);
    }


    @Override
    protected String getHint2Text() {
        if (type == 0) {
            return getString(R.string.bill_self_hint2);
        } else {
            return getString(R.string.bill_fans_hint2);
        }
    }


    @Override
    protected String getHint1Text() {
        if (type == 0) {
            return getString(R.string.bill_self_hint1);
        } else {
            return getString(R.string.bill_fans_hint1);
        }
    }


    @Override
    protected void requestData(final boolean getMore) {
        Subscription subscribe = null;
        if (type == 0) {
            subscribe = HXHttpUtil.getInstance()
                                  .charmRanking(userInfo.user_id, viewed_user_id, mPage)
                                  .map(new Func1<RankListBean, List<FansInfoBean>>() {
                                      @Override
                                      public List<FansInfoBean> call(RankListBean object) {
                                          if (object != null) {
                                              return object.rankList;
                                          }
                                          return null;
                                      }
                                  })
                                  .subscribe(new HttpSubscriber<List<FansInfoBean>>() {

                                      @Override
                                      public void onNext(List<FansInfoBean> list) {
                                          if (list == null) {
                                              onDataSuccess(null);
                                              return;
                                          }
                                          if (!getMore) {
                                              if (null == mfansList) {
                                                  mfansList = new ArrayList<>();
                                              }
                                              if (list.size() <= 3) {
                                                  mfansList = list;
                                                  if (list.size() == 0) {
                                                      haveFoot = true;
                                                  } else {
                                                      haveFoot = false;
                                                  }
                                                  onDataSuccess(null);
                                              } else {
                                                  mfansList = list
                                                          .subList(0, 3);
                                                  onDataSuccess(list
                                                          .subList(3, list
                                                                  .size()));
                                              }
                                          } else {
                                              onDataSuccess(list);
                                          }
                                      }


                                      @Override
                                      public void onError(Throwable e) {
                                          super.onError(e);
                                          if (!getMore) {
                                              haveFoot = false;
                                          } else {
                                              haveFoot = true;
                                          }
                                          onDataSuccess(null);
                                      }
                                  });
        } else if (type == 1) {
            subscribe = HXHttpUtil.getInstance()
                                  .contributionRanking(viewed_user_id, mPage)
                                  .map(new Func1<RankListBean, List<FansInfoBean>>() {
                                      @Override
                                      public List<FansInfoBean> call(RankListBean object) {
                                          if (object != null) {
                                              return object.rankList;
                                          }
                                          return null;
                                      }
                                  })
                                  .subscribe(new HttpSubscriber<List<FansInfoBean>>() {

                                      @Override
                                      public void onNext(List<FansInfoBean> object) {
                                          if (!getMore) {
                                              haveFoot = true;
                                          } else {
                                              haveFoot = false;
                                          }
                                          onDataSuccess(object);
                                      }


                                      @Override
                                      public void onError(Throwable e) {
                                          super.onError(e);
                                          if (!getMore) {
                                              haveFoot = true;
                                          } else {
                                              haveFoot = false;
                                          }
                                          onDataSuccess(null);
                                      }
                                  });
        }
        if (subscribe != null) {
            addSubscription(subscribe);
        }
    }


    @Override
    protected void onDataSuccess(List<FansInfoBean> newData) {
        isLoading = false;
        if (null == mList) {
            mList = new ArrayList<>();
        }
        if (isclearList) {
            mList.clear();
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
        if (null != mPtrFrameLayout) {
            mPtrFrameLayout.refreshComplete();
        }
        hasMore = null != newData &&
                newData.size() >= HXHttpUtil.PAGE_LIMIT - 3;
        if (null == adapter) {
            HXCommonAdapter<FansInfoBean> myAdapter = new HXCommonAdapter<>(mList, getAdapterItemSupplier());
            adapter = new RcvAdapterWrapper(myAdapter, mRecycleView
                    .getLayoutManager());
            mRecycleView.setAdapter(adapter);
            View view = initHeaderView();
            if (null != view) {
                adapter.setHeaderView(view);
            }
        }
        if (null != newData && newData.size() > 0) {
            int positionStart = mList.size();
            mList.addAll(newData);
            if (adapter != null) {
                if (adapter.getFooterView() != null) {
                    adapter.removeFooterView();
                }
                adapter.notifyItemRangeInserted(
                        positionStart + adapter.getHeaderCount(), newData
                                .size());
            }
        } else {
            mfooterView = (BlankHintView) initFooterView();
            if (null != mfooterView) {
                adapter.setFooterView(mfooterView);
            }
        }
    }


    @Override
    public View initHeaderView() {
        if (type == 0 && mfansList != null) {
            if (mfansList.size() != 0) {
                if (mHeaderView == null) {
                    mHeaderView = new BillListHeadView(getActivity(), mfansList);
                }
            }
            return mHeaderView;
        } else {
            return null;
        }
    }


    @Override
    public View initFooterView() {
        if (!haveFoot) {
            return null;
        } else {
            BlankHintView blankHintView = new BlankHintView(getActivity());
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            blankHintView.setLayoutParams(layoutParams);
            blankHintView.setHint1Text(getHint1Text());
            blankHintView.setHint2Text(getHint2Text());
            return blankHintView;
        }
    }


    private List<FansInfoBean> parseArray(JSONStringer object) {
        if (object == null) {
            return null;
        }
        List<FansInfoBean> rankList = null;
        Gson gson = new Gson();
        JSONObject object1 = null;
        try {
            object1 = new JSONObject(object.toString());
            rankList = gson.fromJson(object1
                    .optString("rankList"), new TypeToken<List<FansInfoBean>>() {}
                    .getType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rankList;
    }
}
