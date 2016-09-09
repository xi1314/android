package com.remair.heixiu.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.R;
import com.remair.heixiu.adapters.PriLetterAdapter;
import com.remair.heixiu.adapters.RechargePagerAdapter;
import com.remair.heixiu.adapters.UnfollowInfoAdapter;
import com.remair.heixiu.sqlite.ConcernInfoDB;
import com.remair.heixiu.sqlite.ConcernInfoDao;
import com.remair.heixiu.sqlite.UnFollowConcernInfoDB;
import com.remair.heixiu.sqlite.UnFollowConcernInfoDao;
import com.remair.heixiu.utils.SharedPreferenceUtil;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import studio.archangel.toolkitv2.adapters.CommonRecyclerAdapter;
import studio.archangel.toolkitv2.util.Util;
import studio.archangel.toolkitv2.util.ui.SelectorProvider;

/**
 * Created by wsk on 2016/6/8.
 */
public class PrivateMessageDialog extends DialogFragment {
    private RecyclerView rv_follow, rv_unfollow;
    private List<ConcernInfoDB> concerInfoList;
    private List<UnFollowConcernInfoDB> unFollowList;
    private PriLetterAdapter adapter;
    private UnfollowInfoAdapter unfollowInfoAdapter;
    private WindowManager wm;
    private ImageView ib_back;
    private String anchor_id;//主播id
    private String anchor_name;//主播name
    private String anchor_avatar;//主播头像
    private int relation;//是否关注


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = View
                .inflate(getContext(), R.layout.activity_priletters, null);
        SmartTabLayout tab = (SmartTabLayout) inflate
                .findViewById(R.id.activity_priletter_pager_tab);
        final ViewPager pager = (ViewPager) inflate
                .findViewById(R.id.activity_priletter_pager);
        ib_back = (ImageView) inflate.findViewById(R.id.ib_back);
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        ArrayList<View> views = new ArrayList<>();
        String[] strings = new String[] { "已关注", "未关注" };
        View followView = inflater.inflate(R.layout.actvity_priletter, null);
        View unfollowView = inflater.inflate(R.layout.actvity_priletter, null);

        rv_follow = (RecyclerView) followView
                .findViewById(R.id.act_friend_list);
        rv_unfollow = (RecyclerView) unfollowView
                .findViewById(R.id.act_friend_list);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        LinearLayoutManager lm2 = new LinearLayoutManager(getActivity());
        rv_follow.setLayoutManager(lm);
        rv_unfollow.setLayoutManager(lm2);
        rv_follow.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = studio.archangel.toolkitv2.util.Util
                        .getPX((float) 0.5);
            }
        });
        rv_unfollow.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = studio.archangel.toolkitv2.util.Util
                        .getPX((float) 0.5);
            }
        });
        Bundle bundle = getArguments();
        if (null != bundle) {
            relation = bundle.getInt("relation");
            anchor_id = bundle.getString("anchor_id");
            anchor_name = bundle.getString("anchor_name");
            anchor_avatar = bundle.getString("anchor_avatar");
        }

        try {
            ConcernInfoDao concerInfo = new ConcernInfoDao(getActivity());
            concerInfoList = concerInfo.getConcernInfoAll();
            ConcernInfoDB concernInfoDB = concerInfo
                    .getConcernInfoByUId(anchor_id);
            if (null == concernInfoDB) {
                if (HXApp.getInstance().getUserInfo().isCreater) {

                } else {
                    concernInfoDB = new ConcernInfoDB();
                    JSONObject user = new JSONObject();
                    user.put("user_id", anchor_id);
                    user.put("user_name", anchor_name);
                    user.put("user_avatar", anchor_avatar);
                    concernInfoDB.setRelation(relation);
                    concernInfoDB.setUser_id(anchor_id);
                    concernInfoDB.setHxtype("-1");
                    concernInfoDB.setUserinfo(user.toString());
                    if (relation == 1) {
                        concerInfoList.add(concernInfoDB);
                    }
                }
            }
            Collections.reverse(concerInfoList);
            if (concerInfoList.size() > 0) {
                adapter = new PriLetterAdapter(getActivity(), concerInfoList, getChildFragmentManager());
                rv_follow.setAdapter(adapter);
                adapter.setOnItemToClickListener(new CommonRecyclerAdapter.OnItemToClickListener() {
                    @Override
                    public void onItemClick(View view, Object data) {
                        try {
                            ConcernInfoDB chatInfo = (ConcernInfoDB) data;
                            String userInfo = chatInfo.getUserinfo();
                            RelativeLayout relativeLayout = (RelativeLayout) view
                                    .findViewById(R.id.chat_msg_tip);
                            relativeLayout.setVisibility(View.GONE);
                            SharedPreferenceUtil.Remove(Long
                                    .parseLong(chatInfo.getUser_id()) + "");
                            JSONObject jsonObject = new JSONObject(userInfo);
                            HXApp.getInstance().userid = Integer
                                    .parseInt(chatInfo.getUser_id());
                            HXApp.getInstance().user_name = jsonObject
                                    .optString("user_name");
                            HXApp.getInstance().user_avatar = jsonObject
                                    .optString("user_avatar");
                            HXApp.getInstance().relation = chatInfo
                                    .getRelation();
                            ChatMessageDialog chatMessageDialog = new ChatMessageDialog();
                            chatMessageDialog
                                    .setStyle(DialogFragment.STYLE_NO_TITLE, R.style.add_dialog);
                            chatMessageDialog
                                    .show(getChildFragmentManager(), "private");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                adapter.setOnItemLongClickListener(new CommonRecyclerAdapter.OnItemLongClickListener() {
                    @Override
                    public void OnItemLongClick(int position, View view, Object data) {
                        showPopupWindow(view, data, 1);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            UnFollowConcernInfoDao unFollowInfo = new UnFollowConcernInfoDao(getActivity());
            unFollowList = unFollowInfo.getConcernInfoAll();
            UnFollowConcernInfoDB unFollowConcernInfoByUId = unFollowInfo
                    .getUnFollowConcernInfoByUId(anchor_id);
            if (null == unFollowConcernInfoByUId) {
                if (HXApp.getInstance().getUserInfo().isCreater) {

                } else {
                    unFollowConcernInfoByUId = new UnFollowConcernInfoDB();
                    JSONObject user = new JSONObject();
                    user.put("user_id", anchor_id);
                    user.put("user_name", anchor_name);
                    user.put("user_avatar", anchor_avatar);
                    unFollowConcernInfoByUId.setRelation(relation);
                    unFollowConcernInfoByUId.setUser_id(anchor_id);
                    unFollowConcernInfoByUId.setHxtype("-1");
                    unFollowConcernInfoByUId.setUserinfo(user.toString());
                    if (relation == 0) {
                        unFollowList.add(unFollowConcernInfoByUId);
                    }
                }
            }
            Collections.reverse(unFollowList);
            if (unFollowList.size() > 0) {
                unfollowInfoAdapter = new UnfollowInfoAdapter(getActivity(), unFollowList, getChildFragmentManager());
                rv_unfollow.setAdapter(unfollowInfoAdapter);
                unfollowInfoAdapter
                        .setOnItemToClickListener(new CommonRecyclerAdapter.OnItemToClickListener() {
                            @Override
                            public void onItemClick(View view, Object data) {
                                try {
                                    UnFollowConcernInfoDB chatInfo = (UnFollowConcernInfoDB) data;
                                    String userInfo = chatInfo.getUserinfo();
                                    RelativeLayout relativeLayout = (RelativeLayout) view
                                            .findViewById(R.id.chat_msg_tip);
                                    relativeLayout.setVisibility(View.GONE);
                                    SharedPreferenceUtil.Remove(Long
                                            .parseLong(chatInfo.getUser_id()) +
                                            "");
                                    JSONObject jsonObject = new JSONObject(userInfo);

                                    HXApp.getInstance().userid = Integer
                                            .parseInt(chatInfo.getUser_id());
                                    HXApp.getInstance().user_name = jsonObject
                                            .optString("user_name");
                                    HXApp.getInstance().user_avatar = jsonObject
                                            .optString("user_avatar");
                                    HXApp.getInstance().relation = chatInfo
                                            .getRelation();
                                    ChatMessageDialog chatMessageDialog = new ChatMessageDialog();
                                    chatMessageDialog
                                            .setStyle(DialogFragment.STYLE_NO_TITLE, R.style.add_dialog);
                                    chatMessageDialog
                                            .show(getChildFragmentManager(), "private");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                unfollowInfoAdapter
                        .setOnItemLongClickListener(new CommonRecyclerAdapter.OnItemLongClickListener() {
                            @Override
                            public void OnItemLongClick(int position, View view, Object data) {
                                showPopupWindow(view, data, 2);
                            }
                        });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        views.add(rv_follow);
        views.add(rv_unfollow);
        RechargePagerAdapter rechargePagerAdapter = new RechargePagerAdapter(views, strings);
        pager.setAdapter(rechargePagerAdapter);
        tab.setDefaultTabTextColor(SelectorProvider
                .getColorStateList(getContext().getResources()
                                               .getColor(R.color.white), getContext()
                        .getResources().getColor(R.color.gray_message)));
        tab.setViewPager(pager);
        wm = (WindowManager) getActivity()
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        getDialog().getWindow()
                   .setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
        //        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        Display display = getActivity().getWindowManager()
                                       .getDefaultDisplay(); // 为获取屏幕宽、高
        lp.width = (int) (display.getWidth()); // 宽度设置为屏幕的0.95
        lp.height = (int) (display.getHeight() * 0.4); // 高度设置为屏幕的0.6
        getDialog().getWindow().setAttributes(lp);
        return inflate;
    }


    @Override
    public void onResume() {
        Display display = getActivity().getWindowManager()
                                       .getDefaultDisplay(); // 为获取屏幕宽、高
        int width = (int) (display.getWidth());
        int height = (int) (display.getHeight());
        getDialog().getWindow().setLayout(width, (height / 2));
        super.onResume();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void showPopupWindow(View v, final Object data, final int flag) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.chat_popupwindow, null);
        TextView tv_chat_copy = (TextView) view.findViewById(R.id.tv_chat_copy);
        TextView tv_chat_delete = (TextView) view
                .findViewById(R.id.tv_chat_delete);
        tv_chat_copy.setText("取消");
        WindowManager wm = (WindowManager) getActivity()
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay()
                       .getHeight();//studio.archangel.toolkitv2.util.Utils.getPX(60)
        final PopupWindow pw = new PopupWindow(view, Util.getPX(100), Util
                .getPX(40));

        tv_chat_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw.dismiss();
            }
        });
        tv_chat_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == 1) {
                    ConcernInfoDB chatInfo = (ConcernInfoDB) data;
                    concerInfoList.remove(chatInfo);
                    adapter.notifyDataSetChanged();
                    try {
                        ConcernInfoDao concernInfoDao = new ConcernInfoDao(getActivity());
                        concernInfoDao
                                .deleteConcernInfoByUId(chatInfo.getUser_id());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else if (flag == 2) {
                    UnFollowConcernInfoDB unFollowConcernInfoDB = (UnFollowConcernInfoDB) data;
                    unFollowList.remove(unFollowConcernInfoDB);
                    unfollowInfoAdapter.notifyDataSetChanged();
                    try {
                        UnFollowConcernInfoDao unFollowConcernInfoDao = new UnFollowConcernInfoDao(getActivity());
                        unFollowConcernInfoDao
                                .deleteUnFollowConcernInfoByUId(unFollowConcernInfoDB
                                        .getUser_id());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                pw.dismiss();
            }
        });
        // 点击外部可以被关闭
        pw.setOutsideTouchable(true);
        pw.setBackgroundDrawable(new BitmapDrawable());

        pw.setFocusable(true);// 设置PopupWindow允许获得焦点, 默认情况下popupWindow中的控件不可以获得焦点, 例外: Button, ImageButton..
        pw.showAsDropDown(v, (width / 2 - Util.getPX(50)), -Util
                .getPX(90), Gravity.CENTER_HORIZONTAL);
    }
}
