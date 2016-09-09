package com.remair.heixiu.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.HXJavaNet;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.AvActivity;
import com.remair.heixiu.activity.ChatActivity;
import com.remair.heixiu.activity.PayPrepareActivity;
import com.remair.heixiu.adapters.RechargeAdater;
import com.remair.heixiu.adapters.RechargePagerAdapter;
import com.remair.heixiu.bean.RechargeBean;
import com.remair.heixiu.bean.UserInfo;
import com.remair.heixiu.utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import studio.archangel.toolkitv2.util.networking.AngelNetCallBack;
import studio.archangel.toolkitv2.util.text.Notifier;
import studio.archangel.toolkitv2.util.ui.SelectorProvider;

/**
 * Created by JXHIUUI on 2016/6/8.
 */
public class RechargeDialog extends DialogFragment {
    private String assActivity;
    public TextView diamond, sediamond;
    private RecyclerView rv_diamond, rv_heidou;
    private ArrayList<RechargeBean> diamondList, heidouList;
    private int isRecharged = 0;
    private HashMap<String, Object> params;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        UserInfo userInfo = HXApp.getInstance().getUserInfo();
        if (userInfo == null) {
            dismiss();
            return;
        }
        super.onCreate(savedInstanceState);
        isRecharged = userInfo.is_recharged;
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.assActivity = arguments.getString("tag");
        } else {
            Notifier.showShortMsg(getContext(), "参数错误");
            return;
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = View
                .inflate(getContext(), R.layout.dialog_recharge, null);
        final UserInfo userInfo = HXApp.getInstance().getUserInfo();
        if (userInfo == null) {
            dismiss();
            return inflate;
        }
        SmartTabLayout tab = (SmartTabLayout) inflate
                .findViewById(R.id.dialog_recharge_tab);
        final ViewPager pager = (ViewPager) inflate
                .findViewById(R.id.dialog_pager);

        ArrayList<View> views = new ArrayList<>();
        String[] strings = new String[] { "钻石", "嘿豆" };
        View seDiamondView = inflater
                .inflate(R.layout.fragment_sediamond_recharge, null);
        View diamondView = inflater
                .inflate(R.layout.fragment_diamond_recharge, null);
        sediamond = (TextView) seDiamondView.findViewById(R.id.sediamond);
        diamond = (TextView) diamondView.findViewById(R.id.diamond);
        rv_diamond = (RecyclerView) diamondView.findViewById(R.id.rv_diamond);
        rv_heidou = (RecyclerView) seDiamondView.findViewById(R.id.rv_heidou);
        diamondList = new ArrayList<>();
        heidouList = new ArrayList<>();
        RechargeBean diamond1 = new RechargeBean(1, 20, 0, 1);
        RechargeBean diamond2 = new RechargeBean(6, 60, 0, 2);
        RechargeBean diamond3 = new RechargeBean(30, 300, 0, 3);
        RechargeBean diamond4 = new RechargeBean(98, 1000, 0, 4);
        RechargeBean diamond5 = new RechargeBean(298, 3100, 0, 5);
        RechargeBean diamond6 = new RechargeBean(588, 6140, 0, 6);
        diamondList.add(diamond1);
        diamondList.add(diamond2);
        diamondList.add(diamond3);
        diamondList.add(diamond4);
        diamondList.add(diamond5);
        diamondList.add(diamond6);
        RechargeBean heidou1 = new RechargeBean(10, 1000, 1, 1);
        RechargeBean heidou2 = new RechargeBean(60, 6000, 1, 2);
        RechargeBean heidou3 = new RechargeBean(300, 30000, 1, 3);
        RechargeBean heidou4 = new RechargeBean(980, 98000, 1, 4);
        RechargeBean heidou5 = new RechargeBean(2980, 298000, 1, 5);
        RechargeBean heidou6 = new RechargeBean(5880, 588000, 1, 6);
        heidouList.add(heidou1);
        heidouList.add(heidou2);
        heidouList.add(heidou3);
        heidouList.add(heidou4);
        heidouList.add(heidou5);
        heidouList.add(heidou6);
        GridLayoutManager diamondManager = new GridLayoutManager(getContext(), 3);
        GridLayoutManager seDiaManager = new GridLayoutManager(getContext(), 3);
        RechargeAdater diamondAdapter = new RechargeAdater(getContext(), R.layout.item_recharge, diamondList, 0);
        RechargeAdater seDiamondAdapter = new RechargeAdater(getContext(), R.layout.item_recharge, heidouList, 1);

        rv_diamond.setLayoutManager(diamondManager);
        rv_heidou.setLayoutManager(seDiaManager);
        diamondAdapter.setRecycleView(rv_diamond);
        seDiamondAdapter.setRecycleView(rv_heidou);
        rv_diamond.setAdapter(diamondAdapter);
        rv_heidou.setAdapter(seDiamondAdapter);
        diamondAdapter
                .setOnItemClickListener(new RechargeAdater.OnClickListener() {
                    @Override
                    public void onclick(int position) {
                        RechargeBean rechargeBean = diamondList.get(position);
                        if (position == 0) {
                            if (isRecharged == 1) {
                                Toast.makeText(getContext(), "您已使用过首冲礼包", Toast.LENGTH_SHORT)
                                     .show();
                                return;
                            }
                        }
                        int money = rechargeBean.money;
                        Intent it = new Intent(getContext()
                                .getApplicationContext(), PayPrepareActivity.class);
                        it.putExtra("type", 0);
                        it.putExtra("method", "wechat");
                        it.putExtra("recharge_amount", money);
                        startActivityForResult(it, 0x2713);
                    }
                });

        seDiamondAdapter
                .setOnItemClickListener(new RechargeAdater.OnClickListener() {
                    @Override
                    public void onclick(int position) {
                        RechargeBean rechargeBean = heidouList.get(position);
                        final int money = rechargeBean.money;
                        int i = Integer
                                .parseInt(diamond.getText().toString().trim());
                        if (i >= money) {
                            final ShowDialog showDialog = new ShowDialog(getActivity(),
                                    "将消耗" + money + "钻石", "充值", "取消");
                            showDialog
                                    .setClicklistener(new ShowDialog.ClickListenerInterface() {
                                        @Override
                                        public void doConfirm() {
                                            showDialog.dismiss();
                                            HashMap<String, Object> params = new HashMap<>();
                                            params.put("user_id", userInfo.user_id);
                                            params.put("charm_diamond", money);
                                            params.put("type", 0);
                                            HXJavaNet
                                                    .post(HXJavaNet.url_newExchange, params, new AngelNetCallBack() {
                                                        @Override
                                                        public void onSuccess(int ret_code, String ret_data, String msg) {
                                                            if (ret_code ==
                                                                    200) {
                                                                try {
                                                                    JSONObject jsonObject = new JSONObject(ret_data);
                                                                    int heidou_amount = jsonObject
                                                                            .optInt("heidou_amount");
                                                                    int charm_diamond = jsonObject
                                                                            .optInt("charm_diamond_valid");
                                                                    diamond.setText(
                                                                            charm_diamond +
                                                                                    "");
                                                                    sediamond
                                                                            .setText(
                                                                                    heidou_amount +
                                                                                            "");
                                                                    if (AvActivity.class
                                                                            .getSimpleName()
                                                                            .equals(assActivity)) {
                                                                        AvActivity activity = (AvActivity) getActivity();
                                                                        if (activity !=
                                                                                null) {
                                                                            activity.tv_account
                                                                                    .setText(diamond
                                                                                            .getText());
                                                                            activity.tv_species_account
                                                                                    .setText(sediamond
                                                                                            .getText());
                                                                        }
                                                                    } else if (ChatActivity.class
                                                                            .getSimpleName()
                                                                            .equals(assActivity)) {
                                                                        ChatActivity activity = (ChatActivity) getActivity();
                                                                        if (activity !=
                                                                                null) {
                                                                            activity.act_chat_gift_account
                                                                                    .setText(diamond
                                                                                            .getText());
                                                                            activity.act_chat_species_account
                                                                                    .setText(sediamond
                                                                                            .getText());
                                                                        }
                                                                    }
                                                                    Notifier.showShortMsg(getContext()
                                                                            .getApplicationContext(), msg);
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            } else {
                                                                Notifier.showShortMsg(getContext()
                                                                        .getApplicationContext(), "兑换失败，请重试");
                                                            }
                                                        }


                                                        @Override
                                                        public void onFailure(String msg) {
                                                            Notifier.showShortMsg(getContext()
                                                                    .getApplicationContext(), "兑换失败，请重试");
                                                        }
                                                    });
                                        }


                                        @Override
                                        public void doCancel() {
                                            showDialog.dismiss();
                                        }
                                    });
                            showDialog.show();
                        } else {
                            final ShowDialog showDialog = new ShowDialog(getActivity(), "您的钻石余额不足", "好的", "关闭");
                            showDialog.show();
                            showDialog
                                    .setClicklistener(new ShowDialog.ClickListenerInterface() {
                                        @Override
                                        public void doConfirm() {
                                            showDialog.dismiss();
                                        }


                                        @Override
                                        public void doCancel() {

                                        }
                                    });
                            showDialog.setCancelGone();
                        }
                    }
                });
        views.add(diamondView);
        views.add(seDiamondView);
        RechargePagerAdapter rechargePagerAdapter = new RechargePagerAdapter(views, strings);
        pager.setAdapter(rechargePagerAdapter);
        tab.setDefaultTabTextColor(SelectorProvider
                .getColorStateList(getContext().getResources()
                                               .getColor(R.color.white), getContext()
                        .getResources().getColor(R.color.gray_message)));
        tab.setViewPager(pager);

        params = new HashMap<>();
        params.put("user_id", userInfo.user_id);
        params.put("viewed_user_id", userInfo.user_id);
        getUserInfo(params);
        return inflate;
    }


    private void getUserInfo(HashMap<String, Object> params) {
        HXJavaNet
                .post(HXJavaNet.url_getuserinfo, params, new AngelNetCallBack() {
                    @Override
                    public void onSuccess(int ret_code, String ret_data, String msg) {
                        if (ret_code == 200) {
                            try {
                                JSONObject jsonObject = new JSONObject(ret_data);
                                int virtual_currency_amount = jsonObject
                                        .optInt("virtual_currency_amount");
                                int heidou_amount = jsonObject
                                        .optInt("heidou_amount");
                                sediamond.setText(heidou_amount + "");
                                diamond.setText(virtual_currency_amount + "");
                                if (AvActivity.class.getSimpleName()
                                                    .equals(assActivity)) {
                                    AvActivity activity = (AvActivity) getActivity();
                                    if (activity != null) {
                                        activity.tv_account
                                                .setText(diamond.getText());
                                        activity.tv_species_account
                                                .setText(sediamond.getText());
                                    }
                                } else if (ChatActivity.class.getSimpleName()
                                                             .equals(assActivity)) {
                                    ChatActivity activity = (ChatActivity) getActivity();
                                    if (activity != null) {
                                        activity.act_chat_gift_account
                                                .setText(diamond.getText());
                                        activity.act_chat_species_account
                                                .setText(sediamond.getText());
                                    }
                                }
                                isRecharged = jsonObject.optInt("is_recharged");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }


                    @Override
                    public void onFailure(String msg) {

                    }
                });
    }


    @Override
    public void onResume() {
        getDialog().getWindow()
                   .setLayout(WindowManager.LayoutParams.WRAP_CONTENT, Utils
                           .getPX(400));
        super.onResume();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x2713 && resultCode == HXApp.result_ok) {
            getUserInfo(params);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
