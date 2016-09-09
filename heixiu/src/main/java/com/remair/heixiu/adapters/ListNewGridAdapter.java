package com.remair.heixiu.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.remair.heixiu.R;
import com.remair.heixiu.bean.LiveVideoBean;
import com.remair.heixiu.utils.HXImageLoader;
import com.remair.heixiu.utils.Utils;
import java.util.List;
import studio.archangel.toolkitv2.adapters.CommonRecyclerAdapter;
import studio.archangel.toolkitv2.viewholders.AngelCommonViewHolder;

/**
 * Created by Michael
 * adapter for shows
 */

public class ListNewGridAdapter extends CommonRecyclerAdapter {
    int px;


    public ListNewGridAdapter(Context c, List i) {
        super(c, new int[] { R.layout.item_grid }, null, i, false);
        px = Utils.getPX(130);
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
        @BindView(R.id.item_grid) SimpleDraweeView item_grid;
        @BindView(R.id.tv_nickname) TextView tv_nickname;
        @BindView(R.id.tv_noauditing) TextView tv_noauditing;


        public ItemViewHolder(View v) {
            super(v);
            item_grid = (SimpleDraweeView) v.findViewById(R.id.item_grid);
            tv_nickname = (TextView) v.findViewById(R.id.tv_nickname);
            tv_noauditing = (TextView) v.findViewById(R.id.tv_noauditing);
        }


        @Override
        public void render(final Context context, Object model) {
            final LiveVideoBean info = (LiveVideoBean) model;
            tv_nickname.setText(info.nickname);
            if (info.state == 3) {
                tv_noauditing.setVisibility(View.VISIBLE);
            } else {
                tv_noauditing.setVisibility(View.GONE);
            }
            HXImageLoader.loadImage(item_grid, info.photo, px, px);
        }
    }
}
