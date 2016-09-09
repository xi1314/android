package com.remair.heixiu.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.remair.heixiu.HXJavaNet;
import com.remair.heixiu.R;
import com.remair.heixiu.bean.CaseHistoryInfo;
import java.util.List;
import studio.archangel.toolkitv2.adapters.CommonRecyclerAdapter;
import studio.archangel.toolkitv2.viewholders.AngelCommonViewHolder;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：JXH
 * 创建时间：16/4/7 14:23
 * 修改人：JXH
 * 修改时间：16/4/7 14:23
 * 修改备注：
 */
public class CaseHistoryAdapter extends CommonRecyclerAdapter {
    private String url;


    public CaseHistoryAdapter(Context context, List<CaseHistoryInfo> mList, String url) {
        super(context, new int[] {
                R.layout.item_case_history }, null, mList, false);
        this.url = url;
    }


    @Override
    protected RecyclerView.ViewHolder onCreateHeaderVH(View v) {
        return null;
    }


    @Override
    protected RecyclerView.ViewHolder onCreateItemVH(View v) {
        return new ItemViewHolder(v);
    }


    public class ItemViewHolder extends AngelCommonViewHolder {
        @BindView(R.id.tv_count) TextView tv_count;
        @BindView(R.id.tv_timer) TextView tv_timer;


        public ItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }


        @Override
        public void render(Context context, Object model) {
            CaseHistoryInfo caseInfo = (CaseHistoryInfo) model;
            if (url.equals(HXJavaNet.url_rechargehistory)) {
                tv_count.setText("兑换钻石:" + caseInfo.coin);
            } else {
                tv_count.setText("兑换嘿豆:" + caseInfo.heidou);
            }
            tv_timer.setText(caseInfo.time);
        }
    }
}
