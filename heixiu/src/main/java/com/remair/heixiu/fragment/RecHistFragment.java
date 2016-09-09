package com.remair.heixiu.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.CaseHistryActivity;
import com.remair.heixiu.bean.CaseHistoryInfo;
import com.remair.heixiu.common.Supplier;
import com.remair.heixiu.fragment.base.RefreshListFragment;
import com.remair.heixiu.net.HXHttpUtil;
import com.remair.heixiu.net.HttpSubscriber;
import com.remair.heixiu.utils.LayoutManagerUtil;
import com.remair.heixiu.view.recycler.item.CastHistoryItem;
import java.util.List;
import kale.adapter.item.AdapterItem;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：JXH
 * 创建时间：2016/6/24 21:02
 * 修改人：JXH
 * 修改时间：2016/6/24 21:02
 * 修改备注：
 */
public class RecHistFragment extends RefreshListFragment<CaseHistoryInfo> {

    private int type;
    private int fromType;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View getRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frag_heidou, null);
    }


    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return LayoutManagerUtil.getLinearLayoutManager(getActivity());
    }


    @Override
    protected Supplier<AdapterItem<CaseHistoryInfo>, CaseHistoryInfo> getAdapterItemSupplier() {
        return new Supplier<AdapterItem<CaseHistoryInfo>, CaseHistoryInfo>() {
            @Override
            public AdapterItem<CaseHistoryInfo> get(CaseHistoryInfo caseHistoryInfo) {
                if (fromType == 0 || fromType == 1) {
                    return new CastHistoryItem(fromType);
                } else {
                    return new CastHistoryItem(type);
                }
            }
        };
    }


    @Override
    public void initData() {
        Bundle arguments = getArguments();
        CaseHistryActivity activity = (CaseHistryActivity) getActivity();
        fromType = activity.type;
        if (arguments != null) {
            type = (int) arguments.get("type");
        }
        loadData(false);
    }


    @Override
    protected void requestData(boolean getMore) {
        int user_id = HXApp.getInstance().getUserInfo().user_id;
        if (fromType == 0 || (fromType == 2 && type == 0)) {
            HXHttpUtil.getInstance()
                      .rechargeHistory(user_id, fromType == 2 ? 1 : type)
                      .subscribe(new HttpSubscriber<List<CaseHistoryInfo>>() {
                          @Override
                          public void onNext(List<CaseHistoryInfo> caseHistoryInfos) {
                              onDataSuccess(caseHistoryInfos);
                              hasMore = false;
                          }
                      });
        } else if (fromType == 1 || (fromType == 2 && type == 1)) {
            HXHttpUtil.getInstance().heidouChargeHistory(user_id, type)
                      .subscribe(new HttpSubscriber<List<CaseHistoryInfo>>() {
                          @Override
                          public void onNext(List<CaseHistoryInfo> caseHistoryInfos) {
                              onDataSuccess(caseHistoryInfos);
                              hasMore = false;
                          }
                      });
        }
    }


    @Override
    protected String getHint2Text() {
        if (fromType == 0 || fromType == 2) {
            if (type == 0) {
                return getString(R.string.rehis_heidou1);
            } else {
                return getString(R.string.rehis_heidou1);
            }
        } else if (fromType == 1) {
            if (type == 0) {
                return getString(R.string.rehis_heidou2);
            } else {
                return getString(R.string.rehis_heidou2);
            }
        }
        return null;
    }


    @Override
    protected String getHint1Text() {
        if (fromType == 0 || fromType == 2) {
            if (type == 0) {
                return getString(R.string.rehis_charge_hint);
            } else {
                return getString(R.string.rehis_exchange_hit);
            }
        } else if (fromType == 1) {
            if (type == 0) {
                return getString(R.string.rehis_charge_hint1);
            } else {
                return getString(R.string.rehis_exchange_hit1);
            }
        }
        return null;
    }
}
