package com.remair.heixiu.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.ChatActivity;
import com.remair.heixiu.bean.PrivateMessage;
import com.remair.heixiu.common.Supplier;
import com.remair.heixiu.fragment.base.RefreshListFragment;
import com.remair.heixiu.sqlite.ConcernInfoDB;
import com.remair.heixiu.sqlite.ConcernInfoDao;
import com.remair.heixiu.sqlite.MessageInfoDB;
import com.remair.heixiu.sqlite.MessageInfoDao;
import com.remair.heixiu.sqlite.UnFollowConcernInfoDB;
import com.remair.heixiu.sqlite.UnFollowConcernInfoDao;
import com.remair.heixiu.utils.LayoutManagerUtil;
import com.remair.heixiu.utils.SharedPreferenceUtil;
import com.remair.heixiu.view.recycler.item.FollowInfoItem;
import com.remair.heixiu.view.recycler.item.UnFollowInfoItem;
import java.util.ArrayList;
import java.util.List;
import kale.adapter.item.AdapterItem;
import org.json.JSONException;
import org.json.JSONObject;
import studio.archangel.toolkitv2.util.Util;

/**
 * Created by wsk on 16/2/27.
 */
public class ShowFriendListFragment extends RefreshListFragment<PrivateMessage> {
    private int type;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("heixiu.offlinemessage")) {
                if (getActivity() == null) {
                    return;
                }
                requestData(true);
            }
        }
    };


    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return LayoutManagerUtil.getLinearLayoutManager(getContext());
    }


    @Override
    protected void requestData(boolean getMore) {
        getData();
    }


    @Override
    protected Supplier<AdapterItem<PrivateMessage>, PrivateMessage> getAdapterItemSupplier() {
        return new Supplier<AdapterItem<PrivateMessage>, PrivateMessage>() {
            @Override
            public AdapterItem<PrivateMessage> get(PrivateMessage privateMessage) {
                if (privateMessage instanceof ConcernInfoDB) {
                    return new FollowInfoItem(getContext(), mOnClickListener);
                } else if (privateMessage instanceof UnFollowConcernInfoDB) {
                    return new UnFollowInfoItem(getContext(), mOnClickListener);
                } else {
                    return null;
                }
            }
        };
    }


    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Object object = view.getTag();
            if (object.getClass().isAssignableFrom(ConcernInfoDB.class)) {
                try {
                    ConcernInfoDB chatInfo = (ConcernInfoDB) object;
                    String userInfo = chatInfo.getUserinfo();
                    RelativeLayout relativeLayout = (RelativeLayout) view
                            .findViewById(R.id.chat_msg_tip);
                    relativeLayout.setVisibility(View.GONE);
                    SharedPreferenceUtil
                            .Remove(Long.parseLong(chatInfo.getUser_id()) + "");
                    JSONObject jsonObject = new JSONObject(userInfo);
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra("user_id", Integer
                            .parseInt(chatInfo.getUser_id()));
                    intent.putExtra("user_name", jsonObject
                            .optString("user_name"));
                    intent.putExtra("user_photo", jsonObject
                            .optString("user_avatar"));
                    intent.putExtra("relation", chatInfo.getRelation());
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (object.getClass()
                             .isAssignableFrom(UnFollowConcernInfoDB.class)) {
                try {
                    UnFollowConcernInfoDB chatInfo = (UnFollowConcernInfoDB) object;
                    String userInfo = chatInfo.getUserinfo();
                    RelativeLayout relativeLayout = (RelativeLayout) view
                            .findViewById(R.id.chat_msg_tip);
                    relativeLayout.setVisibility(View.GONE);
                    SharedPreferenceUtil
                            .Remove(Long.parseLong(chatInfo.getUser_id()) + "");
                    JSONObject jsonObject = new JSONObject(userInfo);
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    int i = Integer.parseInt(chatInfo.getUser_id());
                    intent.putExtra("user_id", i);
                    intent.putExtra("user_name", jsonObject
                            .optString("user_name"));
                    intent.putExtra("user_photo", jsonObject
                            .optString("user_avatar"));
                    intent.putExtra("relation", chatInfo.getRelation());
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    View.OnLongClickListener mLongClickListener = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(View view) {
            Object object=view.getTag();
            if (object.getClass().isAssignableFrom(ConcernInfoDB.class)) {
                showPopupWindow(view, object, 1);
            }else if (object.getClass()
                            .isAssignableFrom(UnFollowConcernInfoDB.class)){
                showPopupWindow(view, object, 2);
            }
            return false;
        }
    };


    @Override
    public void initData() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("heixiu.offlinemessage");
        getActivity().registerReceiver(broadcastReceiver, intentFilter);
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getInt("type");
        }
        if (mPtrFrameLayout != null) {
            mPtrFrameLayout.autoRefresh();
        }
    }


    @Override
    protected String getHint2Text() {
        return getString(R.string.private_messagetwo);
    }


    @Override
    protected String getHint1Text() {
        return getString(R.string.private_message);
    }


    private void getData() {
        if (getActivity() == null) {
            return;
        }
        if (type == 0) {
            try {
                ConcernInfoDao concerInfo = new ConcernInfoDao(getActivity());
                List<ConcernInfoDB> concerInfoList = concerInfo
                        .getConcernInfoAll();
                //Collections.reverse(concerInfoList);
                List<PrivateMessage> privateMessageList= new ArrayList<>();
                if (concerInfoList.size() > 0) {
                    privateMessageList.addAll(concerInfoList);
                    onDataSuccess(privateMessageList);
                } else {
                    onDataSuccess(null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (type == 1) {
            try {
                UnFollowConcernInfoDao unFollowInfo = new UnFollowConcernInfoDao(getActivity());
                List<UnFollowConcernInfoDB> unFollow = unFollowInfo
                        .getConcernInfoAll();
                List<PrivateMessage> privateMessageList= new ArrayList<>();
                //Collections.reverse(unFollow);
                if (unFollow.size() > 0) {
                    privateMessageList.addAll(unFollow);
                    onDataSuccess(privateMessageList);
                } else {
                    onDataSuccess(null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        getData();
    }


    @Override
    public void onStop() {
        super.onStop();
    }


    private void showPopupWindow(final View v, final Object data, final int flag) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.chat_popupwindow, null);
        TextView tv_chat_copy = (TextView) view.findViewById(R.id.tv_chat_copy);
        TextView tv_chat_delete = (TextView) view
                .findViewById(R.id.tv_chat_delete);
        tv_chat_copy.setText(getString(R.string.cancel));
        WindowManager wm = (WindowManager) getActivity()
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
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
                    try {
                        ConcernInfoDao concernInfoDao = new ConcernInfoDao(getActivity());
                        concernInfoDao
                                .deleteConcernInfoByUId(chatInfo.getUser_id());
                        MessageInfoDao messageInfoDao = new MessageInfoDao(getActivity());
                        List<MessageInfoDB> messageInfoDBList = messageInfoDao
                                .getMessageInfoByUId(chatInfo.getUser_id());
                        for (int i = 0; i < messageInfoDBList.size(); i++) {
                            messageInfoDao
                                    .deleteMessage(messageInfoDBList.get(i)
                                                                    .getUuid());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (flag == 2) {
                    UnFollowConcernInfoDB unFollowConcernInfoDB = (UnFollowConcernInfoDB) data;
                    try {
                        UnFollowConcernInfoDao unFollowConcernInfoDao = new UnFollowConcernInfoDao(getActivity());
                        unFollowConcernInfoDao
                                .deleteUnFollowConcernInfoByUId(unFollowConcernInfoDB
                                        .getUser_id());
                        MessageInfoDao messageInfoDao = new MessageInfoDao(getActivity());
                        List<MessageInfoDB> messageInfoDBList = messageInfoDao
                                .getMessageInfoByUId(unFollowConcernInfoDB
                                        .getUser_id());
                        for (int i = 0; i < messageInfoDBList.size(); i++) {
                            messageInfoDao
                                    .deleteMessage(messageInfoDBList.get(i)
                                                                    .getUuid());
                        }
                    } catch (Exception e) {
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
        pw.showAsDropDown(v, (width / 2 - Util.getPX(50)), -Util.getPX(90));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
    }
}
