package com.remair.heixiu.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.HXJavaNet;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.FriendMessageActivity;
import com.remair.heixiu.adapters.UsercpAdapter;
import com.remair.heixiu.bean.CurrUser;
import com.remair.heixiu.bean.RankList;
import com.remair.heixiu.bean.UsercpBean;
import com.remair.heixiu.pull_rectclerview.RefreshRecyclerView;
import com.remair.heixiu.pull_rectclerview.listener.OnBothRefreshListener;
import com.remair.heixiu.pull_rectclerview.manager.RecyclerMode;
import com.remair.heixiu.pull_rectclerview.manager.RecyclerViewManager;
import com.remair.heixiu.utils.HXImageLoader;
import com.remair.heixiu.utils.Utils;
import com.remair.heixiu.utils.Xtgrade;
import java.util.ArrayList;
import java.util.HashMap;
import studio.archangel.toolkitv2.util.Util;
import studio.archangel.toolkitv2.util.networking.AngelNetCallBack;

/**
 * Created by wsk on 16/4/5.
 */
public class UsercpDialog extends Dialog implements View.OnClickListener {
    @BindView(R.id.usercp_list) RefreshRecyclerView usercp_list;
    @BindView(R.id.fl_fragme) FrameLayout fl_fragme;
    @BindView(R.id.wozhidaole) RelativeLayout wozhidaole;

    @BindView(R.id.wozhidaole1) RelativeLayout wozhidaole1;

    @BindView(R.id.b_close) ImageView b_close;

    ArrayList<RankList> rankLists;
    UsercpAdapter adapter;
    String usercp;
    private String viewed_user_id;
    private Context context;
    private int page = 1;


    public UsercpDialog(Context context) {
        super(context);
    }


    public UsercpDialog(Context context, String usercp, String viewed_user_id) {
        super(context, R.style.dialog3);
        this.context = context;
        this.usercp = usercp;
        this.viewed_user_id = viewed_user_id;
        init();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    SimpleDraweeView cunzaigan_baoye111;
    SimpleDraweeView cunzaigan_baoyehehe;
    TextView cunzaigan_baoyeTextView111;
    TextView meilizhi_textviewcunzaiganddd;
    TextView txt_rank2;
    TextView meilizhi_textviewcunzaigan;


    private void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.usercp_activity, null);
        setContentView(view);
        ButterKnife.bind(this);

        cunzaigan_baoye111 = (SimpleDraweeView) view
                .findViewById(R.id.cunzaigan_baoye111);
        cunzaigan_baoyeTextView111 = (TextView) view
                .findViewById(R.id.cunzaigan_baoyeTextView111);
        meilizhi_textviewcunzaigan = (TextView) view
                .findViewById(R.id.meilizhi_textviewcunzaigan);

        //我
        cunzaigan_baoyehehe = (SimpleDraweeView) view
                .findViewById(R.id.cunzaigan_baoyehehe);

        txt_rank2 = (TextView) view.findViewById(R.id.txt_rank2);
        meilizhi_textviewcunzaiganddd = (TextView) view
                .findViewById(R.id.meilizhi_textviewcunzaiganddd);

        if (viewed_user_id
                .equals(HXApp.getInstance().getUserInfo().user_id + "")) {
            wozhidaole1.setVisibility(View.VISIBLE);
            wozhidaole.setVisibility(View.GONE);
            wozhidaole1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UsercpDialog.this.dismiss();
                }
            });
        }
        rankLists = new ArrayList<>();
        adapter = new UsercpAdapter(context, rankLists, usercp);
        LinearLayoutManager lm = new LinearLayoutManager(context);
        usercp_list.setLayoutManager(lm);
        usercp_list.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = Util.getPX((float) 0.5);
            }
        });
        usercp_list.setAdapter(adapter);
        RecyclerViewManager.with(adapter, lm).setMode(RecyclerMode.BOTH)
                           .setOnBothRefreshListener(new OnBothRefreshListener() {
                               @Override
                               public void onPullDown() {
                                   page = 1;
                                   initdata();
                               }


                               @Override
                               public void onLoadMore() {
                                   page++;
                                   initdata();
                               }
                           })
                           .into(usercp_list, context.getApplicationContext());

        b_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UsercpDialog.this.dismiss();
            }
        });
        initdata();
    }


    private void initdata() {
        String viewed_user_id = this.viewed_user_id;
        HashMap<String, Object> pare = new HashMap<>();
        pare.put("user_id", HXApp.getInstance().getUserInfo().user_id);
        pare.put("viewed_user_id", viewed_user_id);
        pare.put("page", page);
        pare.put("limit", 20);
        HXJavaNet
                .post(HXJavaNet.url_charmRanking, pare, new AngelNetCallBack() {
                    @Override
                    public void onSuccess(int ret_code, String ret_data, String msg) {
                        usercp_list.onRefreshCompleted();
                        if (ret_code == 200) {
                            try {
                                UsercpBean usercpBeans = Utils
                                        .jsonToBean(ret_data, UsercpBean.class);
                                if (null != usercpBeans) {
                                    final ArrayList<RankList> rankList = usercpBeans.rankList;
                                    if (page == 1) {
                                        rankLists.clear();
                                        rankLists.addAll(rankList);
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        int size = rankLists.size();
                                        rankLists.addAll(rankList);
                                        adapter.notifyItemRangeChanged(
                                                size - 1, rankLists.size());
                                    }
                                    adapter.notifyDataSetChanged();
                                    CurrUser curruser = usercpBeans.currUser;
                                    int px = Util.getPX(64);
                                    HXImageLoader
                                            .loadImage(cunzaigan_baoye111, rankLists
                                                    .get(0).photo, px, px);
                                    cunzaigan_baoye111
                                            .setOnClickListener(new View.OnClickListener() {

                                                @Override
                                                public void onClick(View v) {

                                                    Intent intent = new Intent(context, FriendMessageActivity.class);
                                                    intent.putExtra("viewed_user_id", rankLists
                                                            .get(0).user_id);
                                                    context.startActivity(intent);
                                                }
                                            });

                                    cunzaigan_baoyeTextView111
                                            .setText(rankLists.get(0).nickname);
                                    meilizhi_textviewcunzaigan.setText(Xtgrade
                                            .moneynumber(rankLists
                                                    .get(0).charm_value_sent +
                                                    ""));
                                    if (curruser != null) {
                                        showPopupWindow(usercp_list, curruser);
                                    }
                                    adapter.setOnItemClickListener(new UsercpAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(int position, View view, Object data) {

                                            RankList rankList = (RankList) data;
                                            Intent intent = new Intent(context, FriendMessageActivity.class);
                                            intent.putExtra("viewed_user_id", rankList.user_id);
                                            context.startActivity(intent);
                                        }


                                        @Override
                                        public void OnItemLongClick(int position, View view) {
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }


                    @Override
                    public void onFailure(String msg) {
                        //act_usercp.setRefreshing(false);
                    }
                });
    }


    private void showPopupWindow(View v, CurrUser curruser) {
        int px = Util.getPX(40);
        HXImageLoader.loadImage(cunzaigan_baoyehehe, curruser.photo, px, px);
        txt_rank2.setText("" + (curruser.position));

        meilizhi_textviewcunzaiganddd
                .setText(Xtgrade.moneynumber(curruser.charm_value_sent + ""));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_deleteto:
                fl_fragme.setVisibility(View.GONE);
                break;
        }
    }
}
