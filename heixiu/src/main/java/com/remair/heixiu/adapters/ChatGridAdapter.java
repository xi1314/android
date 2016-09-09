package com.remair.heixiu.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.remair.heixiu.R;
import java.util.List;
import studio.archangel.toolkitv2.adapters.CommonRecyclerAdapter;
import studio.archangel.toolkitv2.viewholders.AngelCommonViewHolder;

/**
 * Created by wsk
 * adapter for chat
 */
public class ChatGridAdapter extends CommonRecyclerAdapter {

    public ChatGridAdapter(Context c, List i) {
        super(c, new int[] { R.layout.item_chat_type }, null, i, false);
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
        @BindView(R.id.iv_chat_pic) ImageView iv_chat_pic;
        @BindView(R.id.tv_chat_type) TextView tv_chat_type;


        public ItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }


        @Override
        public void render(final Context context, Object model) {
            String info = (String) model;
            if (info.equals("图片")) {
                iv_chat_pic.setImageResource(R.drawable.chat_pic);
            } else if (info.equals("拍照")) {
                iv_chat_pic.setImageResource(R.drawable.chat_popto);
            } else if (info.equals("礼物")) {
                iv_chat_pic.setImageResource(R.drawable.private_gift);
            }
            tv_chat_type.setText(info);
        }
    }
}
