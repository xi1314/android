package com.remair.heixiu.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.remair.heixiu.R;
import com.remair.heixiu.bean.AttentionBean;
import com.remair.heixiu.utils.HXImageLoader;
import com.remair.heixiu.utils.Utils;
import com.remair.heixiu.utils.Xtgrade;
import java.util.List;
import studio.archangel.toolkitv2.adapters.CommonRecyclerAdapter;
import studio.archangel.toolkitv2.viewholders.AngelCommonViewHolder;

/**
 *
 * 项目名称：Android
 * 类描述：
 * 创建人：JXH
 * 创建时间：2016/3/4 14:22
 * 修改人：JXH
 * 修改时间：2016/3/4 14:22
 * 修改备注：
 * @version
 *
 */
public class BlackListAdapter extends CommonRecyclerAdapter {
    private final int px;


    public BlackListAdapter(Context c, List i) {
        super(c, new int[] { R.layout.item_black }, null, i, false);
        px = Utils.getPX(42);
    }


    @Override
    protected RecyclerView.ViewHolder onCreateHeaderVH(View v) {
        return null;
    }


    @Override
    protected RecyclerView.ViewHolder onCreateItemVH(View v) {
        return new AttentionViewHolder(v);
    }


    public class AttentionViewHolder extends AngelCommonViewHolder {
        @BindView(R.id.chat_img) SimpleDraweeView chat_img;//头像
        @BindView(R.id.chat_name) TextView chat_name;//名称
        @BindView(R.id.chat_sex) ImageView chat_sex;//性别
        @BindView(R.id.tv_grade) TextView tv_grade;//等级
        @BindView(R.id.chat_msg) TextView chat_msg;//签名
        @BindView(R.id.iv_grade) SimpleDraweeView iv_grade;


        public AttentionViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }


        @Override
        public void render(final Context context, Object model) {
            final AttentionBean bean = (AttentionBean) model;

            HXImageLoader.loadImage(chat_img, bean.photo, px, px);
            chat_name.setText(bean.nickname);
            chat_msg.setText(bean.signature);
            if (bean.gender == 0) {
                chat_sex.setImageResource(R.drawable.sex_man);
            } else if (bean.gender == 1) {
                chat_sex.setImageResource(R.drawable.sex_woman);
            }
            Xtgrade.mXtgrade(bean.grade, iv_grade, tv_grade);
        }
    }
}
