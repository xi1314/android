package com.remair.heixiu.activity.base;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.remair.heixiu.R;
import com.remair.heixiu.common.Supplier;
import com.remair.heixiu.net.HXHttpUtil;
import com.remair.heixiu.pull_rectclerview.widget.RefreshHeader;
import com.remair.heixiu.utils.RxViewUtil;
import com.remair.heixiu.view.BlankHintView;
import com.remair.heixiu.view.recycler.HXCommonAdapter;
import com.remair.heixiu.view.recycler.SimpleOnRcvScrollListener;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import java.util.ArrayList;
import java.util.List;
import kale.adapter.RcvAdapterWrapper;
import kale.adapter.item.AdapterItem;
import rx.functions.Action1;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：LiuJun
 * 创建时间：16/8/26 11:33
 * 修改人：LiuJun
 * 修改时间：16/8/26 11:33
 * 修改备注：
 */
public abstract class RefreshListActivity<T> extends HXBaseActivity {

    @Nullable @BindView(R.id.title_left_image)
    protected ImageButton mTitleLeftImage;
    @Nullable @BindView(R.id.ptrFrameLayout)
    protected PtrFrameLayout mPtrFrameLayout;
    @BindView(R.id.recycleView) protected RecyclerView mRecycleView;

    protected BlankHintView mBlank_hint;

    protected RcvAdapterWrapper adapter;

    protected int mPage;

    protected List<T> mList = new ArrayList<>();

    protected boolean hasMore = true, isclearList = false, isLoading = false;


    /**
     * 获取页面布局文件
     * 需要实现不同的界面效果可以重写此方法
     * 只要将RecyclerView 和PtrFrameLayout控件的ID设置为对应的ID就可以
     *
     * @return layout id
     */
    protected int getLayoutId() {
        return R.layout.activity_refresh_default;
    }


    protected abstract RecyclerView.LayoutManager getLayoutManager();

    protected abstract void requestData(boolean getMore);

    protected abstract Supplier<AdapterItem<T>, T> getAdapterItemSupplier();

    protected abstract String getHint1Text();

    protected abstract String getHint2Text();


    @Override
    protected void initUI() {
        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
            ButterKnife.bind(this);
        } else {
            finish();
        }
        if (mPtrFrameLayout != null) {
            mPtrFrameLayout.setResistance(1.7f);
            mPtrFrameLayout.setRatioOfHeaderHeightToRefresh(1.2f);
            mPtrFrameLayout.setDurationToClose(200);
            mPtrFrameLayout.setDurationToCloseHeader(1000);
            mPtrFrameLayout.setKeepHeaderWhenRefresh(true);
            mPtrFrameLayout.setPullToRefresh(false);
            //ViewPager滑动冲突
            mPtrFrameLayout.disableWhenHorizontalMove(true);

            RefreshHeader mHeaderView = new RefreshHeader(this);
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
        RxViewUtil.viewBindClick(mTitleLeftImage, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                onBackPressed();
            }
        });
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
                adapter.removeFooterView();
                adapter.notifyItemRangeInserted(
                        positionStart + adapter.getHeaderCount(), newData
                                .size());
            }
        } else {
            adapter.setFooterView(initFooterView());
        }
    }


    public View initHeaderView() {
        return null;
    }


    private View initFooterView() {
        if (mBlank_hint == null) {
            //数据为空时的提示背景
            mBlank_hint = new BlankHintView(this);
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT);
      /*      ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);*/
            mBlank_hint.setLayoutParams(layoutParams);
            mBlank_hint.setHint1Text(getHint1Text());
            mBlank_hint.setHint2Text(getHint2Text());
        }
        return mBlank_hint;
    }
}
