package com.remair.heixiu.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.remair.heixiu.R;
import com.remair.heixiu.utils.HXImageLoader;
import com.remair.heixiu.utils.Utils;
import java.util.Vector;
import org.json.JSONObject;

/**
 * Created by Michael
 * adapter for users in live
 */
public class LiveMemberAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HERDER = 0;
    private static final int TYPE_NUMBERONE = 1;
    private static final int TYPE_NUMBERTWO = 2;
    private static final int TYPE_NUMBERTHREE = 3;
    private final int mPx;

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


    private RecyclerView mRecyclervire;
    private Vector<JSONObject> list;
    private Activity activity;


    public LiveMemberAdapter(RecyclerView mRecyclervire, Vector list, Activity activity) {

        this.mRecyclervire = mRecyclervire;
        this.activity = activity;
        this.list = list;
        mPx = Utils.getPX(26);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.item_member, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setTag(list.get(position));
        if (toClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toClickListener.onItemClick(holder.itemView, holder.itemView
                            .getTag());
                }
            });
        }

        if (position == TYPE_HERDER) {
            ((ItemViewHolder) holder).item_member_bg
                    .setVisibility(View.VISIBLE);
            ((ItemViewHolder) holder).item_member_bg
                    .setImageResource(R.drawable.huangguan_bg);
        } else if (position == TYPE_NUMBERONE) {

            ((ItemViewHolder) holder).item_member_bg
                    .setVisibility(View.VISIBLE);
            ((ItemViewHolder) holder).item_member_bg
                    .setImageResource(R.drawable.yinguan_bg);
        } else if (position == TYPE_NUMBERTWO) {

            ((ItemViewHolder) holder).item_member_bg
                    .setVisibility(View.VISIBLE);
            ((ItemViewHolder) holder).item_member_bg
                    .setImageResource(R.drawable.tongguan_bg);
        } else {
            ((ItemViewHolder) holder).item_member_bg
                    .setVisibility(View.INVISIBLE);
        }

        HXImageLoader.loadImage(((ItemViewHolder) holder).iv_avatar, list
                .get(position).optString("user_avatar"), mPx, mPx);
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_member_avatar) SimpleDraweeView iv_avatar;
        @BindView(R.id.item_member_bg) ImageView item_member_bg;


        public void settag(String s) {
            iv_avatar.setTag(s);
        }


        public ItemViewHolder(View v) {
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
        return list.size();
    }


    public boolean add(JSONObject jsonObject, boolean sort) {
        if (!sort) {
            if (isContains(jsonObject) == -1) {
                if (list.size() > 20) {
                    return true;
                }
                list.add(jsonObject);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
                return true;
            } else {
                return false;
            }
        } else {
            if (isContains(jsonObject) == -1) {
                if (list.size() == 0) {
                    list.add(jsonObject);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            notifyDataSetChanged();
                        }
                    });

                    return true;
                }
                if (Integer.valueOf(jsonObject.optInt("send_out_vca") + "") ==
                        0) {
                    if (list.size() >= 20) {
                        return true;
                    }
                }

                if (Integer.valueOf(list.get(0).optInt("send_out_vca") + "") <
                        Integer.valueOf(
                                jsonObject.optInt("send_out_vca") + "")) {
                    list.add(0, jsonObject);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            notifyDataSetChanged();
                            mRecyclervire.smoothScrollToPosition(0);
                        }
                    });

                    return true;
                }
                if (Integer.valueOf(
                        list.get(list.size() - 1).optInt("send_out_vca") + "") >
                        Integer.valueOf(
                                jsonObject.optInt("send_out_vca") + "")) {
                    if (list.size() < 20) {
                        list.add(jsonObject);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                notifyDataSetChanged();
                            }
                        });
                        return true;
                    }
                }
                for (int k = 0; k < 20; k++) {
                    if (Integer
                            .valueOf(list.get(k).optInt("send_out_vca") + "") <=
                            Integer.valueOf(
                                    jsonObject.optInt("send_out_vca") + "")) {
                        list.add(k, jsonObject);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                notifyDataSetChanged();
                            }
                        });
                        return true;
                    }
                }
                return false;
            } else {
                return false;
            }
        }
    }


    public boolean remove(JSONObject jsonObject) {
        int contains = isContains(jsonObject);
        if (contains != -1) {
            if (contains == list.size()) {
                return true;
            }
            list.remove(contains);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            });

            return true;
        } else if (getItemCount() > 20) {
            return true;
        } else {
            return false;
        }
    }


    public int isContains(JSONObject jsonObject) {
        String user_id = jsonObject.optString("user_id");
        for (int i = 0; i < getItemCount(); i++) {
            if (user_id.equals(list.get(i).optString("user_id"))) {
                return i;
            }
        }
        return -1;//不包含
    }
}
