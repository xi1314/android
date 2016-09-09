package com.remair.heixiu.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.HXJavaNet;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.WheelViewMessageActivity;
import com.remair.heixiu.bean.LiveVideoBean;
import com.remair.heixiu.bean.UserInfo;
import com.remair.heixiu.bean.WhellBean;
import com.remair.heixiu.pull_rectclerview.RefreshRecyclerView;
import com.remair.heixiu.utils.HXImageLoader;
import com.remair.heixiu.utils.RSAUtils;
import com.remair.heixiu.utils.Utils;
import com.remair.heixiu.view.Kanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import studio.archangel.toolkitv2.util.Logger;
import studio.archangel.toolkitv2.util.networking.AngelNetCallBack;

/**
 * Created by Michael
 * adapter for shows
 */
public class ShowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HERDER = 0;
    private static final int TYPE_NUMBERONE = 1;
    private static final int TYPE_NUMBERTWO = 2;
    private static final int TYPE_NUMBERTHREE = 3;
    private final int px34;
    ArrayList<WhellBean> whellBeans = new ArrayList<>();
    private boolean First = true;
    private String encrypt;
    private final int mWidth;

    public interface OnItemClickListener {

        void onItemClick(int position);
    }

    public interface OnItemToClickListener {

        void onItemClick(View view, Object data);
    }

    OnItemClickListener listener;
    OnItemToClickListener toClickListener;


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public void setOnItemToClickListener(OnItemToClickListener listener) {

        this.toClickListener = listener;
    }


    private Context context;
    private ArrayList<LiveVideoBean> list;
    WindowManager wm;


    public ShowAdapter(Context context, RefreshRecyclerView mRecyclervire, ArrayList<LiveVideoBean> list) {
        this.context = context;
        this.list = list;
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        mWidth = displayMetrics.widthPixels;
        px34 = Utils.getPX(34);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HERDER) {

            View view = LayoutInflater.from(parent.getContext())
                                      .inflate(R.layout.item_showtwo, parent, false);
            ItemViewHolderone itemViewHolder = new ItemViewHolderone(view);
            return itemViewHolder;
        } else {

            View view = LayoutInflater.from(parent.getContext())
                                      .inflate(R.layout.item_show, parent, false);
            ItemViewHolder itemViewHolder = new ItemViewHolder(view);
            return itemViewHolder;
        }
    }


    ;


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (toClickListener != null) {
            if (0 == position) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onItemClick(position);
                        }
                    }
                });
            } else {
                holder.itemView.setTag(list.get(position - 1));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toClickListener
                                .onItemClick(holder.itemView, holder.itemView
                                        .getTag());
                    }
                });
            }
        }
        if (position == TYPE_HERDER) {
            //轮播图
            // if (whellBeans.size() == 0) {
            synchronized (this) {
                if (First) {
                    whellBeans.clear();
                    First = false;
                    initdata(holder);
                }
            }
        } else {

            final LiveVideoBean info =  list.get(position - 1);

            HXImageLoader
                    .loadImage(((ItemViewHolder) holder).iv_avatar, info.photo, px34, px34);
            HXImageLoader
                    .loadImage(((ItemViewHolder) holder).iv_back, info.cover_image, mWidth, mWidth);
            int viewing_num = info.viewing_num;
            if (viewing_num < 0) {
                viewing_num = 0;
            }
            ((ItemViewHolder) holder).tv_amount.setText(viewing_num + "人正在观看");
            ((ItemViewHolder) holder).tv_name.setText(info.nickname);
            ((ItemViewHolder) holder).tv_location.setText(info.address);
            if (info.title.isEmpty()) {
                ((ItemViewHolder) holder).item_show_des
                        .setVisibility(View.GONE);
            } else {
                ((ItemViewHolder) holder).item_show_des.setText(info.title);
            }
        }
    }


    private void initdata(final RecyclerView.ViewHolder holder) {
        final UserInfo userInfo = HXApp.getInstance().getUserInfo();
        if (userInfo == null) {
            return;
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("user_id", userInfo.user_id);

        HXJavaNet
                .post(HXJavaNet.url_carousel_list, params, new AngelNetCallBack() {
                    @Override
                    public void onSuccess(int ret_code, String ret_data, String msg) {
                        if (ret_code == 200) {
                            try {

                                if ("".equals(ret_data)) {
                                    ((ItemViewHolderone) holder).network_indicate_view
                                            .setVisibility(View.GONE);
                                } else {
                                    JSONArray jsonArray = new JSONArray(ret_data);
                                    List<String> urlList = new ArrayList<String>();

                                    for (int i = 0; i < jsonArray.length();
                                         i++) {
                                        WhellBean whellBean = Utils
                                                .jsonToBean(jsonArray.get(i)
                                                                     .toString(), WhellBean.class);
                                        whellBeans.add(whellBean);
                                        urlList.add(whellBean.image);
                                    }

                                    ((ItemViewHolderone) holder).network_indicate_view
                                            .setImagesUrl(urlList);

                                    ((ItemViewHolderone) holder).network_indicate_view
                                            .setOnItemClickListener(new Kanner.OnItemClickListener() {

                                                @Override
                                                public void OnItemClick(View view, int position) {
                                                    if (null != whellBeans &&
                                                            whellBeans.size() >
                                                                    0) {
                                                        try {
                                                            String source =
                                                                    userInfo.identity +
                                                                            "," +
                                                                            userInfo.user_id;
                                                            encrypt = RSAUtils
                                                                    .encrypt(source);
                                                            Logger.out(encrypt);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                        Intent intent = new Intent(context, WheelViewMessageActivity.class);
                                                        WhellBean whellBean = whellBeans
                                                                .get(position);
                                                        if (null != whellBean) {
                                                            intent.putExtra("url", whellBean.url);
                                                            intent.putExtra("title", whellBean.title);
                                                            intent.putExtra("carousel_id", whellBean.carousel_id);
                                                            if (encrypt !=
                                                                    null) {
                                                                intent.putExtra("data", encrypt);
                                                            }
                                                            context.startActivity(intent);
                                                        }
                                                    }
                                                }
                                            });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ((ItemViewHolderone) holder).network_indicate_view
                                    .setVisibility(View.GONE);
                        }
                    }


                    @Override
                    public void onFailure(String msg) {
                        ((ItemViewHolderone) holder).network_indicate_view
                                .setVisibility(View.GONE);
                    }
                });
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_show_back) SimpleDraweeView iv_back;
        @BindView(R.id.item_show_amount) TextView tv_amount;
        @BindView(R.id.item_show_avatar) SimpleDraweeView iv_avatar;
        @BindView(R.id.item_show_name) TextView tv_name;
        @BindView(R.id.item_show_location) TextView tv_location;
        @BindView(R.id.item_show_des) TextView item_show_des;


        public ItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public class ItemViewHolderone extends RecyclerView.ViewHolder {
        @BindView(R.id.network_indicate_view) Kanner network_indicate_view;


        public ItemViewHolderone(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HERDER;
        } else if (position == 1) {
            return TYPE_NUMBERONE;
        } else if (position == 2) {
            return TYPE_NUMBERTWO;
        } else {
            return TYPE_NUMBERTHREE;
        }
    }


    @Override
    public int getItemCount() {
        return list.size() + 1;
    }
}
