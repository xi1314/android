package com.remair.heixiu.giftview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.remair.heixiu.R;
import com.remair.heixiu.utils.HXImageLoader;
import com.remair.heixiu.utils.SharedPreferenceUtil;
import com.remair.heixiu.utils.Utils;
import java.util.List;
import studio.archangel.toolkitv2.adapters.CommonRecyclerAdapter;
import studio.archangel.toolkitv2.viewholders.AngelCommonViewHolder;

/**
 * Created by Michael
 * adapter for custom view for displaying gift page
 */
public class GiftSelectorPageAdapter extends CommonRecyclerAdapter {
    Context context;


    public GiftSelectorPageAdapter(Context c, List i) {
        super(c, new int[] { R.layout.item_gift }, null, i, false);
        this.context = c;
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
        @BindView(R.id.item_gift_image_layout) View v_layout;
        //        @BindView(R.id.item_gift_image)
        //        ImageView iv_image;
        @BindView(R.id.iv_species) ImageView iv_species;
        @BindView(R.id.iv_encourage) ImageView iv_encourage;
        @BindView(R.id.item_gift_image) SimpleDraweeView iv_image;
        @BindView(R.id.item_gift_text) TextView tv_text;
        //        @BindView(R.id.tv_transfer)
        //        TextView tv_transfer;
        @BindView(R.id.tv_envent_count) TextView tv_envent_count;


        public ItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }


        @Override
        public void render(final Context context, Object model) {
            final GiftWrapper o = (GiftWrapper) model;
            if (o.is_selected) {
                if (o.gift.select_animation == 1) {
                    final String gifturll = SharedPreferenceUtil
                            .getString(o.gift.gift_id, "");
                    if (null != gifturll && !"".equals(gifturll)) {
                        if (o.gift.animation == 1) {//动画
                            HXImageLoader
                                    .loadGifImage(iv_image, o.gift.select_animation_image, Utils
                                            .getPX(48), Utils.getPX(48));
                        } else {
                            DraweeController controller = Fresco
                                    .newDraweeControllerBuilder()
                                    .setUri("file://" + gifturll)
                                    .setAutoPlayAnimations(true).build();
                            iv_image.setController(controller);
                        }
                    } else {
                        HXImageLoader
                                .loadGifImage(iv_image, o.gift.select_animation_image, Utils
                                        .getPX(48), Utils.getPX(48));
                    }
                } else {
                    HXImageLoader.loadImage(iv_image, o.gift.image, Utils
                            .getPX(48), Utils.getPX(48));
                }
            } else {
                HXImageLoader
                        .loadImage(iv_image, o.gift.image, Utils.getPX(48), Utils
                                .getPX(48));
            }
            tv_text.setText(o.gift.price + "");
            v_layout.setSelected(o.is_selected);
            if (o.gift.type == 2) {
                iv_encourage.setVisibility(View.VISIBLE);
                //                tv_transfer.setVisibility(View.GONE);
            } else {
                iv_encourage.setVisibility(View.GONE);
                if (o.gift.animation == 1) {
                    //                    tv_transfer.setVisibility(View.GONE);
                    tv_envent_count.setVisibility(View.GONE);
                } else {
                    //                    tv_transfer.setVisibility(View.VISIBLE);
                }
            }
            if (o.gift.type == 0) {//0钻石普通道具
                iv_species.setBackgroundResource(R.drawable.zhuanshi);
            } else if (o.gift.type == 1) {//1钻石豪华道具
                iv_species.setBackgroundResource(R.drawable.zhuanshi);
            } else if (o.gift.type == 2) {//2嘿豆道具
                iv_species.setBackgroundResource(R.drawable.species_small);
            } else {
                iv_species.setBackgroundResource(R.drawable.zhuanshi);
            }
        }
    }
}
