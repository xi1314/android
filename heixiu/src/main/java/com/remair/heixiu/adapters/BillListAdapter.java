package com.remair.heixiu.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.facebook.drawee.view.SimpleDraweeView;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.FriendMessageActivity;
import com.remair.heixiu.bean.FansInfoBean;
import com.remair.heixiu.utils.HXImageLoader;
import com.remair.heixiu.utils.Utils;
import com.remair.heixiu.utils.Xtgrade;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by JXHIUUI on 2016/7/19.
 */
public class BillListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int HEAD = 0;
    public static final int ITEM = 1;

    private Context context;
    private ArrayList<FansInfoBean> list;
    private int type;
    private int px;
    private int px1;
    private final int px2;


    public BillListAdapter(Context context, ArrayList<FansInfoBean> list, int type) {
        this.context = context;
        this.list = list;
        this.type = type;
        px = Utils.getPX(118);
        px1 = Utils.getPX(70);
        px2 = Utils.getPX(42);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEAD) {
            View inflate = View.inflate(context, R.layout.part_bill_list, null);
            BillListHeadHolder billListHeadHolder = new BillListHeadHolder(inflate, context);
            return billListHeadHolder;
        } else if (viewType == ITEM) {
            View inflate = View.inflate(context, R.layout.item_bill_list, null);
            BillListViewHolder billListViewHolder = new BillListViewHolder(inflate, context);
            return billListViewHolder;
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (position == 0) {
            if (type == 0) {
                int size = 0;
                if (list.size() > 3) {
                    size = 3;
                } else {
                    size = list.size();
                }
                for (int i = 0; i < size; i++) {
                    FansInfoBean fansInfoBean = list.get(i);
                    if (i == 0) {

                        HXImageLoader
                                .loadImage(((BillListHeadHolder) holder).photoOne, fansInfoBean.photo, px, px);
                        ((BillListHeadHolder) holder).valueOne.setText(Xtgrade
                                .moneynumber(
                                        fansInfoBean.charm_value_sent + ""));
                        ((BillListHeadHolder) holder).partOne
                                .setVisibility(View.VISIBLE);
                        ((BillListHeadHolder) holder).partOne
                                .setTag(fansInfoBean);
                    } else {
                        if (i == 1) {
                            HXImageLoader
                                    .loadImage(((BillListHeadHolder) holder).photoTwo, fansInfoBean.photo, px1, px1);
                            ((BillListHeadHolder) holder).valueTwo
                                    .setText(Xtgrade.moneynumber(
                                            fansInfoBean.charm_value_sent +
                                                    ""));
                            ((BillListHeadHolder) holder).partTwo
                                    .setVisibility(View.VISIBLE);
                            ((BillListHeadHolder) holder).partTwo
                                    .setTag(fansInfoBean);
                        } else if (i == 2) {
                            HXImageLoader
                                    .loadImage(((BillListHeadHolder) holder).photoThree, fansInfoBean.photo, px1, px1);
                            ((BillListHeadHolder) holder).valueThree
                                    .setText(Xtgrade.moneynumber(
                                            fansInfoBean.charm_value_sent +
                                                    ""));
                            ((BillListHeadHolder) holder).partThree
                                    .setVisibility(View.VISIBLE);
                            ((BillListHeadHolder) holder).partThree
                                    .setTag(fansInfoBean);
                        }
                    }
                }
            } else {
                FansInfoBean fansInfoBean = list.get(position);
                ((BillListViewHolder) holder).tvBillGrade
                        .setVisibility(View.GONE);
                ((BillListViewHolder) holder).ivBillGrade
                        .setVisibility(View.VISIBLE);
                ((BillListViewHolder) holder).ivBillGrade
                        .setImageResource(R.drawable.bill_list_jg);
                HXImageLoader
                        .loadImage(((BillListViewHolder) holder).chatImg, fansInfoBean.photo, px2, px2);
                ((BillListViewHolder) holder).chatName
                        .setText(fansInfoBean.nickname);
                if (fansInfoBean.gender == 0) {
                    ((BillListViewHolder) holder).chatSex
                            .setImageResource(R.drawable.sex_man);
                } else {
                    ((BillListViewHolder) holder).chatSex
                            .setImageResource(R.drawable.sex_woman);
                }
                Xtgrade.mXtgrade(fansInfoBean.grade, ((BillListViewHolder) holder).ivGrade, ((BillListViewHolder) holder).tvGrade);
                ((BillListViewHolder) holder).chatMsg
                        .setText(fansInfoBean.signature);
                ((BillListViewHolder) holder).tvValue.setText(Xtgrade
                        .moneynumber(fansInfoBean.charm_value_sent + ""));
                ((BillListViewHolder) holder).ll_item.setTag(fansInfoBean);
            }
        } else {
            FansInfoBean fansInfoBean;
            if (type == 0) {
                if (position + 2 >= list.size()) {
                    return;
                }
                fansInfoBean = list.get(position + 2);
            } else {
                fansInfoBean = list.get(position);
            }

            if (type == 1) {
                ((BillListViewHolder) holder).ivBillGrade
                        .setVisibility(View.GONE);
                ((BillListViewHolder) holder).tvBillGrade
                        .setVisibility(View.VISIBLE);
                if (position == 1) {
                    ((BillListViewHolder) holder).tvBillGrade
                            .setVisibility(View.GONE);
                    ((BillListViewHolder) holder).ivBillGrade
                            .setVisibility(View.VISIBLE);
                    ((BillListViewHolder) holder).ivBillGrade
                            .setImageResource(R.drawable.bill_list_yg);
                } else if (position == 2) {
                    ((BillListViewHolder) holder).ivBillGrade
                            .setVisibility(View.VISIBLE);
                    ((BillListViewHolder) holder).tvBillGrade
                            .setVisibility(View.GONE);
                    ((BillListViewHolder) holder).ivBillGrade
                            .setImageResource(R.drawable.bill_list_tg);
                }
            }
            if (type == 0) {
                ((BillListViewHolder) holder).tvBillGrade
                        .setText(position + 3 + "");
            } else {
                ((BillListViewHolder) holder).tvBillGrade
                        .setText(position + 1 + "");
            }
            HXImageLoader
                    .loadImage(((BillListViewHolder) holder).chatImg, fansInfoBean.photo, px2, px2);
            ((BillListViewHolder) holder).chatName
                    .setText(fansInfoBean.nickname);
            if (fansInfoBean.gender == 0) {
                ((BillListViewHolder) holder).chatSex
                        .setImageResource(R.drawable.sex_man);
            } else {
                ((BillListViewHolder) holder).chatSex
                        .setImageResource(R.drawable.sex_woman);
            }
            Xtgrade.mXtgrade(fansInfoBean.grade, ((BillListViewHolder) holder).ivGrade, ((BillListViewHolder) holder).tvGrade);
            ((BillListViewHolder) holder).chatMsg
                    .setText(fansInfoBean.signature);
            ((BillListViewHolder) holder).tvValue.setText(Xtgrade
                    .moneynumber(fansInfoBean.charm_value_sent + ""));
            ((BillListViewHolder) holder).ll_item.setTag(fansInfoBean);
        }
    }


    @Override
    public int getItemCount() {
        if (type == 0) {
            int size = list.size();
            if (size == 1 || size == 2 || size == 3) {
                return 1;
            } else {
                return size - 2;
            }
        } else {
            return list.size();
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (type == 0) {
            if (position == 0) {
                return HEAD;
            } else {
                return ITEM;
            }
        } else {
            return ITEM;
        }
    }


    public static class BillListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_bill_grade) TextView tvBillGrade;
        @BindView(R.id.chat_img) SimpleDraweeView chatImg;
        @BindView(R.id.chat_name) TextView chatName;
        @BindView(R.id.chat_sex) ImageView chatSex;
        @BindView(R.id.iv_grade) SimpleDraweeView ivGrade;
        @BindView(R.id.tv_grade) TextView tvGrade;
        @BindView(R.id.chat_msg) TextView chatMsg;
        @BindView(R.id.tv_value) TextView tvValue;
        @BindView(R.id.ll_item) LinearLayout ll_item;
        @BindView(R.id.iv_bill_grade) ImageView ivBillGrade;
        Context context;


        public BillListViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            ButterKnife.bind(this, itemView);
        }


        @OnClick({ R.id.ll_item })
        public void onClick(View view) {
            FansInfoBean tag = (FansInfoBean) view.getTag();
            Intent intent = new Intent(context, FriendMessageActivity.class);
            intent.putExtra("viewed_user_id", tag.user_id);
            context.startActivity(intent);
        }
    }

    public static class BillListHeadHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.photo_two) SimpleDraweeView photoTwo;
        @BindView(R.id.value_two) TextView valueTwo;
        @BindView(R.id.part_two) LinearLayout partTwo;
        @BindView(R.id.photo_one) public SimpleDraweeView photoOne;
        @BindView(R.id.value_one) TextView valueOne;
        @BindView(R.id.part_one) LinearLayout partOne;
        @BindView(R.id.photo_three) SimpleDraweeView photoThree;
        @BindView(R.id.value_three) TextView valueThree;
        @BindView(R.id.part_three) LinearLayout partThree;

        Context context;


        public BillListHeadHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            ButterKnife.bind(this, itemView);
        }


        @OnClick({ R.id.part_two, R.id.part_one, R.id.part_three })
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.part_two:
                case R.id.part_one:
                case R.id.part_three:
                    FansInfoBean tag = (FansInfoBean) view.getTag();
                    Intent intent = new Intent(context, FriendMessageActivity.class);
                    intent.putExtra("viewed_user_id", tag.user_id);
                    context.startActivity(intent);
                    break;
            }
        }
    }


    public String toString(int value) {
        if (value > 10000) {
            double newValue = value / 10000;
            DecimalFormat decimalFormat = new DecimalFormat("####0.0");
            return decimalFormat.format(newValue);
        } else {
            return value + "";
        }
    }
}
