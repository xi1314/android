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
import android.widget.TextView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.remair.heixiu.R;
import com.remair.heixiu.adapters.LiveSettingAdapter;
import com.remair.heixiu.adapters.RechargePagerAdapter;
import com.remair.heixiu.bean.UserInfo;
import com.remair.heixiu.sqlite.ConcernInfoDB;
import java.util.ArrayList;
import java.util.List;
import studio.archangel.toolkitv2.util.Util;
import studio.archangel.toolkitv2.util.ui.SelectorProvider;

/**
 * Created by wsk on 2016/6/8.
 */
public class LiveSettingDialog extends DialogFragment {
    private RecyclerView rv_perpetual, rv_temporary;
    private LiveSettingAdapter adapter, temporaryAdapter;
    private WindowManager wm;
    private ImageView ib_back;
    private String anchor_id;//主播id
    private String anchor_name;//主播name
    private String anchor_avatar;//主播头像
    private int relation;//是否关注
    private List<UserInfo> listManage;
    private List<UserInfo> temporaryManage;


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
        String[] strings = new String[] { getContext()
                .getResources().getString(R.string.permanent_manager),
                getContext()
                        .getResources().getString(R.string.temporary_manager) };
        View perpetualView = inflater.inflate(R.layout.actvity_priletter, null);
        View temporaryView = inflater.inflate(R.layout.actvity_priletter, null);

        rv_perpetual = (RecyclerView) perpetualView
                .findViewById(R.id.act_friend_list);
        rv_temporary = (RecyclerView) temporaryView
                .findViewById(R.id.act_friend_list);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        LinearLayoutManager lm2 = new LinearLayoutManager(getActivity());
        rv_perpetual.setLayoutManager(lm);
        rv_temporary.setLayoutManager(lm2);
        rv_perpetual.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = Util.getPX((float) 0.5);
            }
        });
        rv_temporary.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = Util.getPX((float) 0.5);
            }
        });
        Bundle bundle = getArguments();

        try {

            adapter = new LiveSettingAdapter(getActivity(), rv_perpetual, listManage, 0);
            rv_perpetual.setAdapter(adapter);
            //ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new
            //        ItemTouchHelper(ItemTouchHelper.Callback));
            adapter.setOnItemClickListener(new LiveSettingAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, View view, Object data) {

                }


                @Override
                public void OnItemLongClick(int position, View view, Object data) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            temporaryAdapter = new LiveSettingAdapter(getActivity(), rv_temporary, temporaryManage, 1);
            rv_temporary.setAdapter(temporaryAdapter);
            temporaryAdapter
                    .setOnItemClickListener(new LiveSettingAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position, View view, Object data) {

                        }


                        @Override
                        public void OnItemLongClick(int position, View view, Object data) {
                            showPopupWindow(view, data, 2);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

        views.add(rv_perpetual);
        views.add(rv_temporary);
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
                       .getHeight();//studio.archangel.toolkitv2.util.Util.getPX(60)
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
                    listManage.remove(chatInfo);
                    adapter.notifyDataSetChanged();
                } else if (flag == 2) {
                    UserInfo userInfoDB = (UserInfo) data;
                    temporaryManage.remove(userInfoDB);
                    temporaryAdapter.notifyDataSetChanged();
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
