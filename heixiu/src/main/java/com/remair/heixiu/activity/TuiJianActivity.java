package com.remair.heixiu.activity;

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
import com.remair.heixiu.view.PanningView;
import com.remair.heixiu.view.recycler.item.RecommendationItem;
import in.srain.cube.views.ptr.PtrFrameLayout;
import java.util.ArrayList;
import java.util.List;
import kale.adapter.item.AdapterItem;
import rx.Subscription;
import studio.archangel.toolkitv2.views.AngelMaterialButton;

public class TuiJianActivity extends RefreshListActivity<FriendInfo> {

    @BindView(R.id.Bt_button) AngelMaterialButton bt;
    @BindView(R.id.recycleView) RecyclerView recycleView;
    @BindView(R.id.ptrFrameLayout) PtrFrameLayout ptrFrameLayout;
    @BindView(R.id.act_search_hint2) TextView tv_hint1;
    @BindView(R.id.iv_go) TextView iv_go;
    @BindView(R.id.pan_view) PanningView pan_view;
    ArrayList<FriendInfo> shows1;

    int user_idd;
    private boolean isselect=true;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_tui_jian;
    }


    @Override
    protected void initData() {
        iv_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TuiJianActivity.this.finish();
                if (null != pan_view) {
                    pan_view.stopPanning();
                    pan_view = null;
                }
            }
        });

        try {
            user_idd = getIntent().getIntExtra("user_id", -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        pan_view.setImageResource(R.drawable.bg1);
        pan_view.startPanning();
        indata();
        if (null!=ptrFrameLayout){
            ptrFrameLayout.autoRefresh();
        }

    }

    boolean b1, b2, b3, b4, b5, b6, b7, b8, b9;
    ArrayList list = new ArrayList();
    String s;


    private void indata() {
        shows1 = new ArrayList<>();

        if ( user_idd == -1) {
            TuiJianActivity.this.finish();
            return;
        }
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0;i<shows1.size();i++){
                    if (i<shows1.size()-1){
                        s+=shows1.get(i).user_id+",";
                    }else if (i==shows1.size()-1){
                        s+=shows1.get(i).user_id;
                    }
                }
                //if (!b1) {
                //
                //    if (shows1.size() > 1) {
                //        int i = shows1.get(0).user_id;
                //        list.add(i);
                //        s = i + "";
                //    }
                //}
                //if (!b2) {
                //    if (shows1.size() > 2) {
                //        int i = shows1.get(1).user_id;
                //        list.add(i);
                //        s = s + "," + i;
                //    }
                //}
                //if (!b3) {
                //    if (shows1.size() > 3) {
                //        int i = shows1.get(2).user_id;
                //        list.add(i);
                //        s = s + "," + i;
                //    }
                //}
                //if (!b4) {
                //    if (shows1.size() > 4) {
                //        int i = shows1.get(3).user_id;
                //        list.add(i);
                //        s = s + "," + i;
                //    }
                //}
                //if (!b5) {
                //    if (shows1.size() > 5) {
                //        int i = shows1.get(4).user_id;
                //        list.add(i);
                //        s = s + "," + i;
                //    }
                //}
                //if (!b6) {
                //    if (shows1.size() > 6) {
                //        int i = shows1.get(5).user_id;
                //        list.add(i);
                //        s = s + "," + i;
                //    }
                //}
                //if (!b7) {
                //    if (shows1.size() > 7) {
                //        int i = shows1.get(6).user_id;
                //        list.add(i);
                //        s = s + "," + i;
                //    }
                //}
                //if (!b8) {
                //    if (shows1.size() > 8) {
                //        int i = shows1.get(7).user_id;
                //        list.add(i);
                //        s = s + "," + i;
                //    }
                //}
                //if (!b9) {
                //    if (shows1.size() > 9) {
                //        int i = shows1.get(8).user_id;
                //        list.add(i);
                //        s = s + "," + i;
                //    }
                //}
                HXHttpUtil.getInstance().concernBatch(user_idd, s)
                          .subscribe(new HttpSubscriber<Object>() {
                              @Override
                              public void onNext(Object o) {
                                  TuiJianActivity.this.finish();
                                  if (null != pan_view) {
                                      pan_view.stopPanning();
                                      pan_view = null;
                                  }
                              }


                              @Override
                              public void onError(Throwable e) {
                                  super.onError(e);
                                  TuiJianActivity.this.finish();
                              }
                          });

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != pan_view) {
            pan_view.stopPanning();
            pan_view = null;
        }
    }


    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return LayoutManagerUtil.getGridLayoutManager(mActivity, 3);
    }


    @Override
    protected void requestData(boolean getMore) {
        Subscription subscription = HXHttpUtil.getInstance()
                                              .recommendUserList(user_idd, 1)
                                              .subscribe(new HttpSubscriber<List<FriendInfo>>() {
                                                  @Override
                                                  public void onNext(List<FriendInfo> friendInfos) {
                                                      shows1.clear();
                                                      onDataSuccess(friendInfos);
                                                      shows1.addAll(friendInfos);
                                                  }


                                                  @Override
                                                  public void onError(Throwable e) {
                                                      super.onError(e);
                                                      onDataSuccess(null);
                                                  }
                                              });

        addSubscription(subscription);
    }


    @Override
    protected Supplier<AdapterItem<FriendInfo>, FriendInfo> getAdapterItemSupplier() {
        return new Supplier<AdapterItem<FriendInfo>, FriendInfo>() {
            @Override
            public AdapterItem<FriendInfo> get(FriendInfo friendInfo) {
                return new RecommendationItem(mItemOnClickListener);
            }
        };
    }
    View.OnClickListener mItemOnClickListener=new  View.OnClickListener(){
        @Override
        public void onClick(View view) {
            if (isselect){
               shows1.remove(view.getTag());
                isselect=false;
            }else{
                shows1.add((FriendInfo)view.getTag());
                isselect=true;
            }
        }
    };


    @Override
    protected String getHint1Text() {
        return getString(R.string.new_hint1);
    }


    @Override
    protected String getHint2Text() {
        return null;
    }
}
