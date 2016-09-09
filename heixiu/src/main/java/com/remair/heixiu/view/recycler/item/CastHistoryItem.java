package com.remair.heixiu.view.recycler.item;

import android.widget.TextView;
import butterknife.BindView;
import com.remair.heixiu.R;
import com.remair.heixiu.bean.CaseHistoryInfo;
import com.remair.heixiu.view.recycler.SimpleItem;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：JXH
 * 创建时间：2016/8/31 20:51
 * 修改人：JXH
 * 修改时间：2016/8/31 20:51
 * 修改备注：
 */
public class CastHistoryItem extends SimpleItem<CaseHistoryInfo> {
    @BindView(R.id.tv_count) TextView mTvCount;
    @BindView(R.id.tv_timer) TextView mTvTimer;
    protected int type;


    public CastHistoryItem(int type) {
        this.type = type;
    }


    @Override
    public int getLayoutResId() {
        return R.layout.item_case_history;
    }


    @Override
    public void handleData(CaseHistoryInfo caseHistoryInfo, int i) {
        if (type == 0) {
            mTvCount.setText("兑换钻石:" + caseHistoryInfo.coin);
        } else {
            mTvCount.setText("兑换嘿豆:" + caseHistoryInfo.heidou);
        }
        mTvTimer.setText(caseHistoryInfo.time);
    }
}
