package com.remair.heixiu.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import com.remair.heixiu.view.LoadingDialog;
import com.remair.heixiu.view.recycler.item.CommonUserItem;
import java.util.List;
import kale.adapter.item.AdapterItem;
import rx.Subscription;
import rx.functions.Action1;
import studio.archangel.toolkitv2.util.text.Notifier;

/**
 * Created by Michael
 * search page
 */

public class SearchActivity extends RefreshListActivity<FriendInfo> implements View.OnClickListener {

    @BindView(R.id.act_search_input) EditText mActSearchInput;
    @BindView(R.id.cancel) TextView mCancel;
    protected Context mContext;
    private LoadingDialog dialog;
    private int type = 0;
    private String mKeyword;


    @Override
    protected void initData() {
        mContext = getApplicationContext();
        mActSearchInput
                .setOnEditorActionListener(new TextView.OnEditorActionListener() {

                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            mKeyword = mActSearchInput.getText().toString()
                                                      .trim();
                            if (!mKeyword.isEmpty()) {
                                mList.clear();
                                adapter.removeHeaderView();
                                adapter.notifyDataSetChanged();
                                if (dialog == null) {
                                    dialog = new LoadingDialog(SearchActivity.this, R.style.dialog);
                                }
                                dialog.show();
                                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                                        .hideSoftInputFromWindow(mActSearchInput
                                                .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                type = 1;
                                loadData(false);
                            }
                            return true;
                        }
                        return false;
                    }
                });
        RxViewUtil.viewBindClick(mCancel, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(mActSearchInput
                                .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                finish();
            }
        });
        loadData(false);
    }


    @Override
    public View initHeaderView() {
        View view = View.inflate(mActivity, R.layout.search_head, null);
        view.findViewById(R.id.phonefriend).setOnClickListener(this);
        view.findViewById(R.id.weibofriend).setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), RecomActivity.class);
        int current = -1;
        int id = v.getId();
        if (id == R.id.phonefriend) {
            current = 0;
        } else if (id == R.id.weibofriend) {
            current = 1;
        }
        intent.putExtra("current", current);
        startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.act_search;
    }


    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return LayoutManagerUtil.getLinearLayoutManager(this);
    }


    @Override
    protected void requestData(boolean getMore) {
        UserInfo userInfo = HXApp.getInstance().getUserInfo();
        if (null == userInfo) {
            return;
        }
        Subscription subscribe;
        if (type == 0) {
            subscribe = HXHttpUtil.getInstance()
                                  .recommendUserList(userInfo.user_id, mPage)
                                  .subscribe(new HttpSubscriber<List<FriendInfo>>() {
                                      @Override
                                      public void onNext(List<FriendInfo> friendInfos) {
                                          onDataSuccess(friendInfos);
                                      }


                                      @Override
                                      public void onError(Throwable e) {
                                          super.onError(e);
                                          onDataSuccess(null);
                                          Notifier.showShortMsg(mContext, getString(R.string.net_hint));
                                      }
                                  });
            if (subscribe != null) {
                addSubscription(subscribe);
            }
        } else {

            subscribe = HXHttpUtil.getInstance()
                                  .userSearch(userInfo.user_id, mKeyword, mPage)
                                  .subscribe(new HttpSubscriber<List<FriendInfo>>() {
                                      @Override
                                      public void onNext(List<FriendInfo> friendInfos) {
                                          if (dialog != null) {
                                              dialog.todismiss();
                                          }
                                          onDataSuccess(friendInfos);
                                      }


                                      @Override
                                      public void onError(Throwable e) {
                                          super.onError(e);
                                          if (dialog != null) {
                                              dialog.todismiss();
                                          }
                                          onDataSuccess(null);
                                      }
                                  });
            if (subscribe != null) {
                addSubscription(subscribe);
            }
        }
    }


    @Override
    protected Supplier<AdapterItem<FriendInfo>, FriendInfo> getAdapterItemSupplier() {
        return new Supplier<AdapterItem<FriendInfo>, FriendInfo>() {
            @Override
            public AdapterItem<FriendInfo> get(FriendInfo friendInfo) {
                return new CommonUserItem(mActivity, true, null, mOnClickListener);
            }
        };
    }


    @Override
    protected String getHint1Text() {
        if (type == 0) {
            return getString(R.string.search_blank_hint);
        } else {
            return getString(R.string.search_no_hint);
        }
    }


    @Override
    protected String getHint2Text() {
        return null;
    }


    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FriendInfo friendInfo = (FriendInfo) v.getTag();
            Intent intent = new Intent(mActivity, FriendMessageActivity.class);
            intent.putExtra("viewed_user_id", friendInfo.user_id);
            startActivity(intent);
        }
    };
}
