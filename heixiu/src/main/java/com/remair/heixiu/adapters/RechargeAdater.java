package com.remair.heixiu.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.remair.heixiu.R;
import com.remair.heixiu.bean.RechargeBean;
import java.util.List;

/**
 * Created by JXHIUUI on 2016/6/11.
 */
public class RechargeAdater extends RecyclerView.Adapter {
    private Context mContext;
    private int id;
    private List i;
    private RecyclerView owner;
    private OnClickListener listener;
    int type;

    public RechargeAdater(Context c, int layout_id, List i, int type) {
        super();
        this.mContext = c;
        this.id = layout_id;
        this.i = i;
        this.type = type;
    }

    public void setRecycleView(RecyclerView view) {
        this.owner = view;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(id, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    if (owner != null) {
                        listener.onclick(owner.indexOfChild(v));
                    } else {
                        Toast.makeText(mContext, "先设置recyclerview", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "先设置listener", Toast.LENGTH_SHORT).show();
                }
            }
        });
        RechargeBean info = (RechargeBean) i.get(position);

        if (type==0){
            if (position != 0) {
                ((ItemViewHolder) holder).icon_first.setVisibility(View.GONE);
            }
        }else{
            ((ItemViewHolder) holder).icon_first.setVisibility(View.GONE);
        }


        ((ItemViewHolder) holder).tv_count.setText(info.count + "");
        if (info.type == 0) {
            ((ItemViewHolder) holder).tv_money.setText("¥" + info.money);
        } else {
            ((ItemViewHolder) holder).tv_money.setText(info.money + "");
            Drawable diamond = mContext.getResources().getDrawable(R.drawable.zhuanshibgbg);
            diamond.setBounds(0, 0, diamond.getMinimumWidth(), diamond.getMinimumHeight());
            ((ItemViewHolder) holder).tv_money.setCompoundDrawables(null, null, diamond, null);
        }

        if (info.type == 0) {
            if (info.grade == 1) {
                ((ItemViewHolder) holder).iv_diamond.setImageResource(R.drawable.diamond1);
            } else if (info.grade == 2) {
                ((ItemViewHolder) holder).iv_diamond.setImageResource(R.drawable.diamond2);
            } else if (info.grade == 3) {
                ((ItemViewHolder) holder).iv_diamond.setImageResource(R.drawable.diamond3);
            } else if (info.grade == 4) {
                ((ItemViewHolder) holder).iv_diamond.setImageResource(R.drawable.diamond4);
            } else if (info.grade == 5) {
                ((ItemViewHolder) holder).iv_diamond.setImageResource(R.drawable.diamond5);
            } else if (info.grade == 6) {
                ((ItemViewHolder) holder).iv_diamond.setImageResource(R.drawable.diamond6);
            }
        } else {
            if (info.grade == 1) {
                ((ItemViewHolder) holder).iv_diamond.setImageResource(R.drawable.heidou1);
            } else if (info.grade == 2) {
                ((ItemViewHolder) holder).iv_diamond.setImageResource(R.drawable.heidou2);
            } else if (info.grade == 3) {
                ((ItemViewHolder) holder).iv_diamond.setImageResource(R.drawable.heidou3);
            } else if (info.grade == 4) {
                ((ItemViewHolder) holder).iv_diamond.setImageResource(R.drawable.heidou4);
            } else if (info.grade == 5) {
                ((ItemViewHolder) holder).iv_diamond.setImageResource(R.drawable.heidou5);
            } else if (info.grade == 6) {
                ((ItemViewHolder) holder).iv_diamond.setImageResource(R.drawable.heidou6);
            }
        }

    }

    @Override
    public int getItemCount() {
        return i.size();
    }

    public void setOnItemClickListener(OnClickListener listener) {
        if (listener != null) {
            this.listener = listener;
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.chat_img_button)
        TextView tv_count;
        @BindView(R.id.TextView_chat_img_button)
        TextView tv_money;
        @BindView(R.id.chat_img)
        ImageView iv_diamond;
        @BindView(R.id.icon_first)
        ImageView icon_first;

        public ItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public interface OnClickListener {
        void onclick(int position);
    }
}
