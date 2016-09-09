package com.remair.heixiu.fragment.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.remair.heixiu.R;
import com.remair.heixiu.common.Supplier;
import com.remair.heixiu.net.HXHttpUtil;
import com.remair.heixiu.pull_rectclerview.widget.RefreshHeader;
import com.remair.heixiu.view.BlankHintView;
import com.remair.heixiu.view.recycler.HXCommonAdapter;
import com.remair.heixiu.view.recycler.SimpleOnRcvScrollListener;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import java.util.ArrayList;
import java.util.List;
import kale.adapter.RcvAdapterWrapper;
import kale.adapter.item.AdapterItem;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：LiuJun
 * 创建时间：16/8/27 15:21
 * 修改人：LiuJun
 * 修改时间：16/8/27 15:21
 * 修改备注：
 */
public abstract class RefreshListFragment<T> extends HXBaseFragment {

    @Nullable @BindView(R.id.ptrFrameLayout)
    protected PtrFrameLayout mPtrFrameLayout;

    @BindView(R.id.recycleView) protected RecyclerView mRecycleView;

    protected RcvAdapterWrapper adapter;

    protected int mPage;

    protected List<T> mList;

    protected boolean hasMore = true, isclearList = false, isLoading = false;

    Unbinder unbinder;

    protected BlankHintView mfooterView;


    protected abstract RecyclerView.LayoutManager getLayoutManager();

    protected abstract void requestData(boolean getMore);

    protected abstract Supplier<AdapterItem<T>, T> getAdapterItemSupplier();


    @Override
    public View getRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater
                .inflate(R.layout.fragment_refresh_default, container, false);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = getRootView(inflater, container);
        unbinder = ButterKnife.bind(this, rootView);
        if (mPtrFrameLayout != null) {
            mPtrFrameLayout.setResistance(1.7f);
            mPtrFrameLayout.setRatioOfHeaderHeightToRefresh(1.2f);
            mPtrFrameLayout.setDurationToClose(200);
            mPtrFrameLayout.setDurationToCloseHeader(1000);
            mPtrFrameLayout.setKeepHeaderWhenRefresh(true);
            mPtrFrameLayout.setPullToRefresh(false);
            //ViewPager滑动冲突
            mPtrFrameLayout.disableWhenHorizontalMove(true);

            RefreshHeader mHeaderView = new RefreshHeader(getActivity());
            mPtrFrameLayout.setHeaderView(mHeaderView);
            mPtrFrameLayout.addPtrUIHandler(mHeaderView);
            mPtrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    hasMore = true;
                    loadData(false);
                }
            });
        }
        mRecycleView.addOnScrollListener(new SimpleOnRcvScrollListener() {

            @Override
            public void onBottom() {
                if (hasMore) {
                    loadData(true);
                }
            }
        });
        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        mRecycleView.setLayoutManager(layoutManager);
        return rootView;
    }


    protected void loadData(boolean getMore) {
        if (isLoading) {
            return;
        }
        isLoading = true;
        isclearList = !getMore;
        if (getMore) {
            mPage = mList.size() / HXHttpUtil.PAGE_LIMIT;
            if (mList.size() % HXHttpUtil.PAGE_LIMIT != 0) {
                mPage += 1;
            }
            mPage += 1;
        } else {
            mPage = 1;
        }
        requestData(getMore);
    }


    /**
     * requestData方法请求数据成功后将数据集传入本方法进行刷新列表
     */
    protected void onDataSuccess(List<T> newData) {
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
        hasMore = null != newData && newData.size() >= HXHttpUtil.PAGE_LIMIT;
        if (null == adapter) {
            HXCommonAdapter<T> myAdapter = new HXCommonAdapter<>(mList, getAdapterItemSupplier());
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
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }


    public View initHeaderView() {
        return null;
    }


    public View initFooterView() {
        if (mfooterView == null) {
            //数据为空时的提示背景
            mfooterView = new BlankHintView(getActivity());
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT);
            mfooterView.setLayoutParams(layoutParams);
            mfooterView.setHint1Text(getHint1Text());
            mfooterView.setHint2Text(getHint2Text());
        }
        return mfooterView;
    }


    protected abstract String getHint2Text();

    protected abstract String getHint1Text();


}
