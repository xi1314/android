package com.remair.heixiu.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.remair.heixiu.R;
import com.remair.heixiu.bean.WithdrawRecord;
import java.util.List;
import studio.archangel.toolkitv2.adapters.CommonRecyclerAdapter;
import studio.archangel.toolkitv2.viewholders.AngelCommonViewHolder;

/**
 * Created by Michael
 * adapter for display withdraw records
 */
public class WithdrawRecordAdapter extends CommonRecyclerAdapter {

    public WithdrawRecordAdapter(Context c, List i) {
        super(c, new int[]{R.layout.item_withdraw_record}, null, i, false);
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
        @BindView(R.id.item_withdraw_record_title)
        TextView tv_title;
        @BindView(R.id.item_withdraw_record_date)
        TextView tv_date;
        @BindView(R.id.item_withdraw_record_status)
        TextView tv_status;

        public ItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        @Override
        public void render(final Context context, Object model) {
            final WithdrawRecord record = (WithdrawRecord) model;
            tv_title.setText(record.title);
            tv_date.setText(record.date);
            tv_status.setText(record.status);
        }
    }
}
