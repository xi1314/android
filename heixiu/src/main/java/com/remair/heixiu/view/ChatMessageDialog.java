package com.remair.heixiu.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.remair.heixiu.DemoConstants;
import com.remair.heixiu.activity.WheelViewMessageActivity;
import com.remair.heixiu.bean.Gift;
import com.remair.heixiu.giftview.GiftSelector;
import com.remair.heixiu.giftview.GiftSelectorPage;
import com.remair.heixiu.giftview.GiftWrapper;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.HXJavaNet;
import com.remair.heixiu.utils.HXUtil;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.MinediamondActivity;
import com.remair.heixiu.adapters.ChatGridAdapter;
import com.remair.heixiu.adapters.ChatMessageListAdapter;
import com.remair.heixiu.adapters.EmojiAdapter;
import com.remair.heixiu.emoji.SmileLayout;
import com.remair.heixiu.giftview.CircleAnimation;
import com.remair.heixiu.sqlite.ConcernInfoDB;
import com.remair.heixiu.sqlite.ConcernInfoDao;
import com.remair.heixiu.sqlite.MessageInfoDB;
import com.remair.heixiu.sqlite.MessageInfoDao;
import com.remair.heixiu.sqlite.UnFollowConcernInfoDB;
import com.remair.heixiu.sqlite.UnFollowConcernInfoDao;
import com.remair.heixiu.utils.CofyUtil;
import com.remair.heixiu.utils.GetMessageUtil;
import com.remair.heixiu.utils.RSAUtils;
import com.remair.heixiu.utils.SharedPreferenceUtil;
import com.tencent.TIMCallBack;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMCustomElem;
import com.tencent.TIMElem;
import com.tencent.TIMElemType;
import com.tencent.TIMImageElem;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageListener;
import com.tencent.TIMMessageStatus;
import com.tencent.TIMSoundElem;
import com.tencent.TIMTextElem;
import com.tencent.TIMValueCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.archangel.toolkitv2.activities.PickImageActivity;
import studio.archangel.toolkitv2.adapters.CommonRecyclerAdapter;
import studio.archangel.toolkitv2.util.Logger;
import studio.archangel.toolkitv2.util.Util;
import studio.archangel.toolkitv2.util.networking.AngelNetCallBack;
import studio.archangel.toolkitv2.util.text.Notifier;
import studio.archangel.toolkitv2.views.AngelMaterialButton;

/**
 * Created by wsk on 2016/6/8.
 */
public class ChatMessageDialog extends DialogFragment implements View.OnClickListener {
    private List<List<String>> emojis;
    private ArrayList<View> pageViews;
    private ArrayList<EmojiAdapter> emojiAdapters;
    private int current = 0;
    private AppCompatEditText mEditIn;
    private ImageView mChatEmoji;
    private ListView mListView;
    private SmileLayout mLinearLayout;
    private boolean isVisible = true;//表情判断
    private boolean isGifeVisible = false;
    private boolean isSoundVisible = false;
    private boolean isSoundcancel = true;
    @BindView(R.id.chat_gift) ImageView mChatGife;

    @BindView(R.id.chat_gift_pad) View mGiftPad;
    private Gift selected_gift = null;//选中的礼物
    @BindView(R.id.chat_message_send) TextView chat_message_send;
    @BindView(R.id.rl_chat_type) RelativeLayout rl_chat_type;
    @BindView(R.id.recy_type) RecyclerView recy_type;
    @BindView(R.id.chat_speed) ImageView chat_speed;
    @BindView(R.id.tv_down) TextView tv_down;
    @BindView(R.id.fl_chat) FrameLayout fl_chat;
    @BindView(R.id.iv_chat_sount) ImageView iv_chat_sount;
    @BindView(R.id.chat_gift_all_pad) View chat_gift_all_pad;
    @BindView(R.id.act_chat_gift_send) AngelMaterialButton act_chat_gift_send;
    @BindView(R.id.act_chat_gift_selector) GiftSelector gift_selector;
    @BindView(R.id.hint_message) TextView hint_message;
    @BindView(R.id.fl_background) FrameLayout fl_background;
    @BindView(R.id.ll_give) LinearLayout ll_give;
    @BindView(R.id.ll_give2) LinearLayout ll_give2;
    @BindView(R.id.ll_give3) LinearLayout ll_give3;
    @BindView(R.id.ll_give4) LinearLayout ll_give4;
    @BindView(R.id.tv_count1) TextView tv_count1;
    @BindView(R.id.tv_count2) TextView tv_count2;
    @BindView(R.id.tv_count3) TextView tv_count3;
    @BindView(R.id.tv_count4) TextView tv_count4;
    @BindView(R.id.view_liv_send_button) FrameLayout view_liv_send_button;
    @BindView(R.id.tv_time) TextView tv_time;
    @BindView(R.id.act_chat_gift_account) TextView act_chat_gift_account;
    @BindView(R.id.act_chat_species_account) TextView act_chat_species_account;
    @BindView(R.id.ll_recharge) LinearLayout ll_recharge;
    @BindView(R.id.fl_point) FrameLayout fl_point;
    @BindView(R.id.iv_chat_delete) ImageView iv_chat_delete;
    @BindView(R.id.tv_name) TextView tv_name;
    @BindView(R.id.ib_back) ImageView ib_back;
    @BindView(R.id.rl_lan) RelativeLayout rl_lan;
    private List<MessageInfoDB> listChatEntity;
    private ChatMessageListAdapter chatMsgListAdapter;
    private TIMConversation mConversation;
    private String user_photo;
    private int user_id;

    private final int MAX_PAGE_NUM = 20;
    private int mLoadMsgNum = MAX_PAGE_NUM;
    private ProgressBar mPBLoadData;
    private boolean mIsLoading = false;
    private boolean mBMore = true;
    private boolean mBNerverLoadMore = true;

    private static final String UNREAD = "0";//未读
    private JSONObject user;
    private int relation_type;
    private TIMMessage lastMessage;
    PopupWindow pw;
    private String strType[] = new String[] { "图片", "拍照" };
    private List<String> list;
    private MessageInfoDB chatphotoEntity;//图片
    private MessageInfoDB chatsoundEntity;//语音
    private int mPicLevel = 1;
    private AnimationDrawable animationDrawable;
    private MediaRecorder mRecorder;
    private File mPttFile;
    private long mPttRecordTime;
    double y;
    private int giftcount;
    private CountDownTimer sendgifttimer;
    private int amountTransfer = 0;//连送礼物数
    private JSONObject sendgiftattr;
    private View inflate;
    String user_name;
    private Queue<JSONObject> sendgiftqueue = new LinkedList<JSONObject>();//发礼物队列
    private int loveGradegift;
    private double loveGradeRatiogift;
    private int width;
    private int height;

    private TIMMessageListener msgListener = new TIMMessageListener() {
        @Override
        public boolean onNewMessages(List<TIMMessage> list) {
            refreshChat(list);
            //                getMessage();
            return false;
        }
    };
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 11:
                    mPBLoadData.setVisibility(View.GONE);
                    break;
                case 12:
                    chatMsgListAdapter.notifyDataSetChanged();
                    mListView.setVisibility(View.VISIBLE);
                    if (mListView.getCount() > 1) {
                        if (mIsLoading) {
                            mListView.setSelection(0);
                        } else {
                            mListView.setSelection(mListView.getCount() - 1);
                        }
                    }
                    mPBLoadData.setVisibility(View.GONE);
                    break;
            }
        }
    };


    private void refreshChat(List<TIMMessage> list) {
        for (int i = 0; i < list.size(); i++) {
            final TIMMessage msg = list.get(i);
            msg.getConversation().setReadMessage(msg);
            for (int j = 0; j < msg.getElementCount(); j++) {
                if (msg.getElement(j) == null) {
                    continue;
                }
                if (msg.status() == TIMMessageStatus.HasDeleted) {
                    continue;
                }
                if (list.get(i).getConversation().getType() ==
                        TIMConversationType.Group) {
                    continue;
                }
                TIMElem elem = msg.getElement(j);
                TIMElemType elemType = elem.getType();
                String sebder = msg.getSender();
                if (!sebder.equals(user_id + "")) {
                    continue;
                }
                if (elemType == TIMElemType.Custom) {
                    try {
                        String text = new String(((TIMCustomElem) elem)
                                .getData(), "UTF-8");
                        JSONObject attr = new JSONObject(text);
                        Iterator itt = attr.keys();
                        while (itt.hasNext()) {
                            String keyname = itt.next().toString();
                            if (keyname.equals("text")) {
                                String uid = attr.optJSONObject("user")
                                                 .getString("user_id");
                                if (Integer.parseInt(uid) != user_id) {
                                    continue;
                                }
                                String tex = attr.optString("text");
                                MessageInfoDB chatEntity = new MessageInfoDB();
                                chatEntity.setType(1);
                                if (msg.isSelf()) {
                                    chatEntity.setIssend(0);
                                } else {
                                    chatEntity.setIssend(1);
                                }
                                chatEntity.setUserid(user_id + "");
                                chatEntity.setSendstatue(msg.status());
                                chatEntity.setMessagetype("text");
                                chatEntity.setUuid(msg.getMsgId());
                                chatEntity.setMessage(tex);
                                chatEntity.setCreatetime(msg.timestamp());
                                chatEntity.setTime(0);
                                listChatEntity.add(chatEntity);
                                //保存数据库
                                MessageInfoDao mesageInfo = new MessageInfoDao(getActivity());
                                mesageInfo.addorupdate(chatEntity, msg
                                        .getMsgId());
                                ConcernInfoDao concernInfoDao = new ConcernInfoDao(getActivity());
                                UnFollowConcernInfoDao unconcernInfoDao = new UnFollowConcernInfoDao(getActivity());
                                try {
                                    ConcernInfoDB concernInfoByUId = concernInfoDao
                                            .getConcernInfoByUId(user_id + "");
                                    UnFollowConcernInfoDB unFollowConcernInfoByUId = unconcernInfoDao
                                            .getUnFollowConcernInfoByUId(
                                                    user_id + "");
                                    if (null != concernInfoByUId) {
                                        GetMessageUtil
                                                .savaDatatwo(1, tex, getActivity(),
                                                        user_id + "");
                                    }
                                    if (null != unFollowConcernInfoByUId) {
                                        GetMessageUtil
                                                .savaDatatwo(0, tex, getActivity(),
                                                        user_id + "");
                                    }
                                    if (null == concernInfoByUId &&
                                            null == unFollowConcernInfoByUId) {
                                        GetMessageUtil.showPerInfoCard(
                                                HXApp.getInstance()
                                                     .getUserInfo().user_id +
                                                        "", user_id +
                                                        "", tex, getActivity(), msg);
                                    }
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            } else if (keyname.equals("command")) {
                                JSONObject jsonObject = attr
                                        .optJSONObject("command");
                                String name = jsonObject.optString("name");
                                if (name.equalsIgnoreCase("photo")) {
                                    if (null == chatphotoEntity) {
                                        chatphotoEntity = new MessageInfoDB();
                                    } else {
                                        String uid = attr.optJSONObject("user")
                                                         .getString("user_id");
                                        if (Integer.parseInt(uid) != user_id) {
                                            continue;
                                        }
                                        chatphotoEntity.setUserid(user_id + "");
                                        //保存数据库
                                        MessageInfoDao mesageInfo = new MessageInfoDao(getActivity());
                                        mesageInfo
                                                .addorupdate(chatphotoEntity, chatphotoEntity
                                                        .getUuid());
                                    }
                                } else if (name.equalsIgnoreCase("sound")) {

                                } else if (name.equalsIgnoreCase("gift")) {
                                    MessageInfoDB chatEntity = new MessageInfoDB();
                                    JSONObject attrCommand = attr
                                            .optJSONObject("command");
                                    chatEntity.setType(1);
                                    if (msg.isSelf()) {
                                        chatEntity.setIssend(0);
                                    } else {
                                        chatEntity.setIssend(1);
                                    }
                                    chatEntity.setUserid(user_id + "");
                                    chatEntity.setSendstatue(msg.status());
                                    chatEntity.setMessagetype("gift");
                                    chatEntity.setUuid(msg.getMsgId());
                                    chatEntity
                                            .setMessage(attrCommand.toString());
                                    chatEntity.setCreatetime(msg.timestamp());
                                    chatEntity.setTime(0);

                                    listChatEntity.add(chatEntity);
                                    //保存数据库
                                    MessageInfoDao mesageInfo = new MessageInfoDao(getActivity());
                                    mesageInfo.addorupdate(chatEntity, msg
                                            .getMsgId());

                                    ConcernInfoDao concernInfoDao = new ConcernInfoDao(getActivity());
                                    UnFollowConcernInfoDao unconcernInfoDao = new UnFollowConcernInfoDao(getActivity());
                                    ConcernInfoDB concernInfoByUId = concernInfoDao
                                            .getConcernInfoByUId(user_id + "");
                                    UnFollowConcernInfoDB unFollowConcernInfoByUId = unconcernInfoDao
                                            .getUnFollowConcernInfoByUId(
                                                    user_id + "");
                                    if (null != concernInfoByUId) {
                                        GetMessageUtil
                                                .savaDatatwo(1, "[礼物]", getActivity(),
                                                        user_id + "");
                                    }
                                    if (null != unFollowConcernInfoByUId) {
                                        GetMessageUtil
                                                .savaDatatwo(0, "[礼物]", getActivity(),
                                                        user_id + "");
                                    }
                                    if (null == concernInfoByUId &&
                                            null == unFollowConcernInfoByUId) {
                                        showPerInfoCard(HXApp.getInstance()
                                                             .getUserInfo().user_id +
                                                "", user_id + "", "[礼物]");
                                    }
                                    chatMsgListAdapter.notifyDataSetChanged();
                                    mListView.setVisibility(View.VISIBLE);
                                    if (mListView.getCount() > 1) {
                                        mListView.setSelection(
                                                mListView.getCount() - 1);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (elemType == TIMElemType.Image) {
                    if (null == chatphotoEntity) {
                        chatphotoEntity = new MessageInfoDB();
                    }
                    TIMImageElem e = (TIMImageElem) elem;
                    chatphotoEntity.setType(1);
                    if (msg.isSelf()) {
                        chatphotoEntity.setIssend(0);
                    } else {
                        chatphotoEntity.setIssend(1);
                    }
                    chatphotoEntity.setUserid(user_id + "");
                    chatphotoEntity.setSendstatue(msg.status());
                    chatphotoEntity.setMessagetype("photo");
                    chatphotoEntity.setUuid(msg.getMsgId());
                    chatphotoEntity
                            .setMessage(e.getImageList().get(1).getUrl());
                    chatphotoEntity.setCreatetime(msg.timestamp());
                    chatphotoEntity.setTime(0);

                    listChatEntity.add(chatphotoEntity);
                    //保存数据库
                    MessageInfoDao mesageInfo = new MessageInfoDao(getActivity());
                    mesageInfo.addorupdate(chatphotoEntity, msg.getMsgId());

                    ConcernInfoDao concernInfoDao = new ConcernInfoDao(getActivity());
                    UnFollowConcernInfoDao unconcernInfoDao = new UnFollowConcernInfoDao(getActivity());
                    try {
                        ConcernInfoDB concernInfoByUId = concernInfoDao
                                .getConcernInfoByUId(user_id + "");
                        UnFollowConcernInfoDB unFollowConcernInfoByUId = unconcernInfoDao
                                .getUnFollowConcernInfoByUId(user_id + "");
                        if (null != concernInfoByUId) {
                            GetMessageUtil.savaDatatwo(1, "[图片]", getActivity(),
                                    user_id + "");
                        }
                        if (null != unFollowConcernInfoByUId) {
                            GetMessageUtil.savaDatatwo(0, "[图片]", getActivity(),
                                    user_id + "");
                        }
                        if (null == concernInfoByUId &&
                                null == unFollowConcernInfoByUId) {
                            GetMessageUtil.showPerInfoCard(
                                    HXApp.getInstance().getUserInfo().user_id +
                                            "",
                                    user_id + "", "[图片]", getActivity(), list
                                            .get(i));
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                } else if (elemType == TIMElemType.Sound) {
                    chatsoundEntity = new MessageInfoDB();
                    final TIMSoundElem timSoundElem = (TIMSoundElem) elem;
                    try {
                        File file = new File("record_evd.mp3");
                        if (file.exists()) {
                            file.delete();
                        }
                        mPttFile = File.createTempFile("record_evd", ".mp3");
                        timSoundElem.getSoundToFile(mPttFile
                                .getAbsolutePath(), new TIMCallBack() {
                            @Override
                            public void onError(int i, String s) {

                            }


                            @Override
                            public void onSuccess() {
                                chatsoundEntity.setType(1);
                                if (msg.isSelf()) {
                                    chatsoundEntity.setIssend(0);
                                } else {
                                    chatsoundEntity.setIssend(1);
                                }
                                chatsoundEntity.setUserid(user_id + "");
                                chatsoundEntity.setSendstatue(msg.status());
                                chatsoundEntity.setMessagetype("sound");
                                chatsoundEntity.setUuid(msg.getMsgId());
                                chatsoundEntity
                                        .setMessage(mPttFile.getAbsolutePath());
                                chatsoundEntity.setCreatetime(msg.timestamp());
                                chatsoundEntity
                                        .setTime(timSoundElem.getDuration());

                                listChatEntity.add(chatsoundEntity);
                                //保存数据库
                                MessageInfoDao mesageInfo = new MessageInfoDao(getActivity());
                                mesageInfo.addorupdate(chatsoundEntity, msg
                                        .getMsgId());

                                ConcernInfoDao concernInfoDao = new ConcernInfoDao(getActivity());
                                UnFollowConcernInfoDao unconcernInfoDao = new UnFollowConcernInfoDao(getActivity());
                                try {
                                    ConcernInfoDB concernInfoByUId = concernInfoDao
                                            .getConcernInfoByUId(user_id + "");
                                    UnFollowConcernInfoDB unFollowConcernInfoByUId = unconcernInfoDao
                                            .getUnFollowConcernInfoByUId(
                                                    user_id + "");
                                    if (null != concernInfoByUId) {
                                        GetMessageUtil
                                                .savaDatatwo(1, "[语音]", getActivity(),
                                                        user_id + "");
                                    }
                                    if (null != unFollowConcernInfoByUId) {
                                        GetMessageUtil
                                                .savaDatatwo(0, "[语音]", getActivity(),
                                                        user_id + "");
                                    }
                                    if (null == concernInfoByUId &&
                                            null == unFollowConcernInfoByUId) {
                                        showPerInfoCard(HXApp.getInstance()
                                                             .getUserInfo().user_id +
                                                "", user_id + "", "[语音]");
                                    }
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }

                                handler.sendEmptyMessage(12);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        chatMsgListAdapter.notifyDataSetChanged();
        mListView.setVisibility(View.VISIBLE);
        if (mListView.getCount() > 1) {
            if (mIsLoading) {
                mListView.setSelection(0);
            } else {
                mListView.setSelection(mListView.getCount() - 1);
            }
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflate = View.inflate(getContext(), R.layout.act_chat, null);
        ButterKnife.bind(this, inflate);
        SharedPreferenceUtil.setContext(getActivity());
        rl_lan.setVisibility(View.VISIBLE);

        user_name = HXApp.getInstance().user_name;
        tv_name.setText(user_name);
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        user_id = HXApp.getInstance().userid;
        user_photo = HXApp.getInstance().user_avatar;
        int relation = HXApp.getInstance().relation;
        initdata(user_id + "");
        initview();

        if (relation == 1) {
            fl_point.setVisibility(View.GONE);
        } else {
            fl_point.setVisibility(View.VISIBLE);
        }
        iv_chat_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fl_point.setVisibility(View.GONE);
            }
        });

        Intent intent = new Intent();
        intent.setAction("heixiu.offlinemessage");
        intent.putExtra("oflinemessage", 0L);
        getActivity().sendBroadcast(intent);
        HXApp.getInstance().unreadCount = 0;
        ll_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HXApp.getInstance().configMessage.ali_recharge_switch
                        .equals("1")) {
                    String mEncrypt = null;
                    try {
                        String source =
                                HXApp.getInstance().getUserInfo().identity +
                                        "," +
                                        HXApp.getInstance()
                                             .getUserInfo().user_id;
                        mEncrypt = RSAUtils.encrypt(source);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (mEncrypt == null) {
                        Intent it = new Intent(getActivity(), MinediamondActivity.class);
                        startActivity(it);
                        return;
                    }
                    Intent intent = new Intent(getActivity(), WheelViewMessageActivity.class);
                    if (HXApp.isTest) {
                        intent.putExtra("url", "http://www.imheixiu.com/alipay/?ispost=1");
                    } else {
                        intent.putExtra("url", "http://www-test" +
                                ".imheixiu.com/alipay/?ispost=1");
                    }

                    intent.putExtra("title", "快速充值通道");
                    intent.putExtra("data", mEncrypt);
                    intent.putExtra("carousel_id", DemoConstants.CAROUSEL_ID);
                    startActivity(intent);
                } else {
                    RechargeDialog rechargeDialog = new RechargeDialog();
                    Bundle bundle = new Bundle();
                    bundle.putString("tag", "AvActivity");
                    rechargeDialog.setArguments(bundle);
                    rechargeDialog
                            .setStyle(DialogFragment.STYLE_NO_TITLE, R.style.add_dialog);
                    rechargeDialog.show(getFragmentManager(), "recharge");
                }
            }
        });

        getDialog().getWindow()
                   .setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
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
        width = (int) (display.getWidth());
        height = (int) (display.getHeight());
        getDialog().getWindow().setLayout(width, (height / 2));
        super.onResume();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void initdata(String mFriendName) {
        mConversation = TIMManager.getInstance()
                                  .getConversation(TIMConversationType.C2C, mFriendName);
        TIMManager.getInstance().addMessageListener(msgListener);
        mConversation.setReadMessage();
    }


    private void initview() {
        chat_gift_all_pad.setVisibility(View.GONE);
        list = new ArrayList<>();
        list.add("图片");
        //    list.add("拍照");
        list.add("礼物");
        GridLayoutManager gm = new GridLayoutManager(getActivity(), 4);
        recy_type.setLayoutManager(gm);
        ChatGridAdapter adapter = new ChatGridAdapter(getActivity(), list);
        recy_type.setAdapter(adapter);
        adapter.setRecyclerView(recy_type);
        adapter.setOnItemToClickListener(new CommonRecyclerAdapter.OnItemToClickListener() {
            @Override
            public void onItemClick(View view, Object data) {
                String string = (String) data;
                if (string.equals(list.get(0))) {
                    Intent intent = new Intent(getActivity(), PickImageActivity.class);
                    intent.putExtra("mode", PickImageActivity.mode_select_from_gallery);
                    startActivityForResult(intent, 0x2711);
                } else if (string.equals(list.get(1))) {//礼物
                    chat_gift_all_pad.setVisibility(View.VISIBLE);
                    mGiftPad.setVisibility(View.GONE);
                    isGifeVisible = true;
                    getDialog().getWindow().setLayout(width, (height / 5) * 4);
                }
            }
        });
        mChatEmoji = (ImageView) inflate.findViewById(R.id.chat_emoji);
        mEditIn = (AppCompatEditText) inflate.findViewById(R.id.chat_msg_input);
        mLinearLayout = (SmileLayout) inflate.findViewById(R.id.ll_emojis);
        mListView = (ListView) inflate.findViewById(R.id.chat_lv);
        mPBLoadData = (ProgressBar) inflate.findViewById(R.id.pb_load_more);
        mChatGife.setOnClickListener(this);
        mChatEmoji.setOnClickListener(this);
        mEditIn.setOnClickListener(this);
        chat_message_send.setOnClickListener(this);
        chat_speed.setOnClickListener(this);
        act_chat_gift_send.setOnClickListener(this);
        ll_give.setOnClickListener(this);
        ll_give2.setOnClickListener(this);
        ll_give3.setOnClickListener(this);
        ll_give4.setOnClickListener(this);
        view_liv_send_button.setOnClickListener(this);
        listChatEntity = new ArrayList<>();
        mEditIn.setTextColor(getResources().getColor(R.color.skin_input));
        mEditIn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }


            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }


            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    chat_message_send.setVisibility(View.VISIBLE);
                    mChatGife.setVisibility(View.GONE);
                } else {
                    chat_message_send.setVisibility(View.GONE);
                    mChatGife.setVisibility(View.VISIBLE);
                }
            }
        });

        mEditIn.setOnFocusChangeListener(new View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    showKeyboard();
                    getDialog().getWindow().setLayout(width, (height / 2));
                    mLinearLayout.setVisibility(View.GONE);
                    mChatEmoji.setImageResource(R.drawable.chat_emoji);

                    if (isGifeVisible) {
                        mGiftPad.setVisibility(View.GONE);
                        mChatGife.setImageResource(R.drawable.add_chat);
                    }
                    if (chat_gift_all_pad.getVisibility() == View.VISIBLE) {
                        chat_gift_all_pad.setVisibility(View.GONE);
                        isGifeVisible = false;
                    }
                } else {
                    // 此处为失去焦点时的处理内容
                    hideMsgIputKeyboard();
                }
            }
        });

        InputMethodManager inputManager = (InputMethodManager) mEditIn
                .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(mEditIn, 0);

        mEditIn.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    // 先隐藏键盘
    /*                ((InputMethodManager) mEditIn.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);*/
                    if (!("").equals(mEditIn.getText().toString())) {
                        onSendMsg();
                        mEditIn.setText("");
                    }
                    return true;
                }
                return false;
            }
        });
        mEditIn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showPopupWindow(view);
                return true;
            }
        });

        mLinearLayout.init(mEditIn);
        listChatEntity = new ArrayList<MessageInfoDB>();
        //        getMessage();
        MessageInfoDao mesageInfo = new MessageInfoDao(getActivity());
        try {
            List<MessageInfoDB> messageInfoDBList = mesageInfo
                    .getMessageInfoByUId(user_id + "");
            listChatEntity.addAll(messageInfoDBList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        chatMsgListAdapter = new ChatMessageListAdapter(getActivity(), listChatEntity, user_photo, pw);
        mListView.setAdapter(chatMsgListAdapter);
        if (mListView.getCount() > 1) {
            mListView.setSelection(mListView.getCount() - 1);
        }

        //        mListView.setOnScrollListener(new AbsListView.OnScrollListener(){
        //
        //            @Override
        //            public void onScrollStateChanged(AbsListView view, int scrollState) {
        //                // TODO Auto-generated method stub
        //                switch (scrollState) {
        //                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
        ////                        if (view.getFirstVisiblePosition() == 0 && !mIsLoading && mBMore) {
        ////                            mPBLoadData.setVisibility(View.VISIBLE);
        ////                            mBNerverLoadMore = false;
        ////                            mIsLoading =true;
        ////                            mLoadMsgNum += MAX_PAGE_NUM;
        //////                            getMessage();
        ////
        //////						mIsLoading = false;
        ////
        ////                        }
        //                        break;
        //                }
        //            }
        //
        //            @Override
        //            public void onScroll(AbsListView view, int firstVisibleItem,
        //                                 int visibleItemCount, int totalItemCount) {
        //                // TODO Auto-generated method stub
        //
        //            }
        //
        //        });
        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mListView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        mGiftPad.setVisibility(View.GONE);
                        mChatGife.setVisibility(View.VISIBLE);
                        mLinearLayout.setVisibility(View.GONE);
                        chat_message_send.setVisibility(View.GONE);
                        mChatEmoji.setImageResource(R.drawable.chat_emoji);
                        mChatGife.setImageResource(R.drawable.add_chat);
                        chat_gift_all_pad.setVisibility(View.GONE);
                        hideMsgIputKeyboard();
                        getDialog().getWindow().setLayout(width, (height / 2));
                        return false;
                    }
                });
                return false;
            }
        });

        gift_selector
                .setOnGiftSelectedListener(new GiftSelectorPage.OnGiftSelectedListener() {
                    @Override
                    public void onGiftSelected(GiftWrapper gift, int position) {
                        selected_gift = gift.gift;
                        amountTransfer = 0;
                        if (gift.gift.type == 2) {
                            hint_message.setText("主播可得" + gift.gift.price / 5 +
                                    "个嘿豆。送出后有机会获得500倍大奖");
                        } else {
                            hint_message.setText(
                                    "主播可获得" + gift.gift.price * 10 + "存在感哟");
                        }
                        if (null != sendgifttimer) {
                            sendgifttimer.cancel();
                            amountTransfer = 0;
                            giftcount = 0;
                            view_liv_send_button.setVisibility(View.GONE);
                            fl_background.setVisibility(View.GONE);
                            ll_give.setVisibility(View.GONE);
                            ll_give2.setVisibility(View.GONE);
                            ll_give3.setVisibility(View.GONE);
                            ll_give4.setVisibility(View.GONE);
                            act_chat_gift_send.setVisibility(View.VISIBLE);
                        }
                    }
                });

        act_chat_gift_account.setText(
                HXApp.getInstance().getUserInfo().virtual_currency_amount + "");
        act_chat_species_account
                .setText(HXApp.getInstance().getUserInfo().heidou_amount + "");
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void showPopupWindow(View v) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.chat_popupwindow, null);
        TextView tv_chat_copy = (TextView) view.findViewById(R.id.tv_chat_copy);
        TextView tv_chat_delete = (TextView) view
                .findViewById(R.id.tv_chat_delete);
        tv_chat_delete.setVisibility(View.GONE);
        tv_chat_copy.setText("粘贴");
        WindowManager wm = (WindowManager) getActivity()
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay()
                       .getHeight();//studio.archangel.toolkitv2.util.Utils.getPX(60)
        final PopupWindow pw = new PopupWindow(view, Util.getPX(50), Util
                .getPX(40));
        //        WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
        //        params.alpha = 0.7f;

        //        getActivity().getWindow().setAttributes(params);
        tv_chat_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditIn.setText(CofyUtil.paste(getActivity()));
                pw.dismiss();
            }
        });
        // 点击外部可以被关闭
        pw.setOutsideTouchable(true);
        pw.setBackgroundDrawable(new BitmapDrawable());

        pw.setFocusable(true);// 设置PopupWindow允许获得焦点, 默认情况下popupWindow中的控件不可以获得焦点, 例外: Button, ImageButton..
        pw.showAsDropDown(v, 0, -Util.getPX(40) - 50, Gravity.BOTTOM);
        //        pw.showAtLocation(v, Gravity.TOP, 0, -20);
    }


    private void getMessage() {
        if (mConversation == null) {
            return;
        }
        mConversation
                .getMessage(MAX_PAGE_NUM, lastMessage, new TIMValueCallBack<List<TIMMessage>>() {
                    @Override
                    public void onError(int code, String desc) {
                        mPBLoadData.setVisibility(View.GONE);
                        mIsLoading = false;
                    }


                    @Override
                    public void onSuccess(List<TIMMessage> msgs) {

                        final List<TIMMessage> tmpMsgs = msgs;
                        if (msgs.size() > 0) {
                            mConversation
                                    .setReadMessage(msgs.get(msgs.size() - 1));
                        }
                        if (!mBNerverLoadMore && (msgs.size() < mLoadMsgNum)) {
                            listChatEntity.clear();
                        }
                        for (int i = 0; i < tmpMsgs.size(); i++) {
                            TIMMessage msg = tmpMsgs.get(i);
                            lastMessage = msg;
                            mConversation.setReadMessage(lastMessage);
                            for (int j = 0; j < msg.getElementCount(); j++) {
                                if (msg.getElement(j) == null) {
                                    continue;
                                }
                                if (msg.status() ==
                                        TIMMessageStatus.HasDeleted) {
                                    continue;
                                }
                                TIMElem elem = msg.getElement(j);
                                try {
                                    String text = new String(((TIMCustomElem) elem)
                                            .getData(), "UTF-8");
                                    JSONObject attr = new JSONObject(text);
                                    String uid = attr.optJSONObject("user")
                                                     .getString("user_id");
                                    String tex = attr.optString("text");
                                    MessageInfoDB chatEntity = new MessageInfoDB();
                                    chatEntity.setType(1);
                                    if (msg.isSelf()) {
                                        chatEntity.setIssend(0);
                                    } else {
                                        chatEntity.setIssend(1);
                                    }

                                    chatEntity.setUserid(user_id + "");
                                    chatEntity.setSendstatue(msg.status());
                                    chatEntity.setMessagetype("text");
                                    chatEntity.setUuid(msg.getMsgId());
                                    chatEntity.setMessage(tex);
                                    chatEntity.setCreatetime(msg.timestamp());

                                    listChatEntity.add(chatEntity);
                                    //保存数据库
                                    MessageInfoDao mesageInfo = new MessageInfoDao(getActivity());
                                    mesageInfo.addorupdate(chatEntity, msg
                                            .getMsgId());
                                    if (user == null) {
                                        showPerInfoCard(HXApp.getInstance()
                                                             .getUserInfo().user_id +
                                                "", user_id + "", tex);
                                    } else {
                                        savaData(relation_type, tex, user
                                                .toString());
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        Collections.reverse(listChatEntity);
                        chatMsgListAdapter.notifyDataSetChanged();
                        mListView.setVisibility(View.VISIBLE);
                        if (mListView.getCount() > 1) {
                            if (mIsLoading) {
                                mListView.setSelection(0);
                            } else {
                                mListView
                                        .setSelection(mListView.getCount() - 1);
                            }
                        }
                        mIsLoading = false;
                    }
                });

        handler.sendEmptyMessage(11);
    }


    boolean mboolean;


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.chat_emoji:
                if (isVisible) {
                    hideMsgIputKeyboard();
                    mChatEmoji.setImageResource(R.drawable.chat_soft_input);
                    mLinearLayout.setVisibility(View.VISIBLE);
                    chat_message_send.setVisibility(View.VISIBLE);
                    mChatGife.setVisibility(View.GONE);
                    isVisible = false;
                    mGiftPad.setVisibility(View.GONE);
                    chat_gift_all_pad.setVisibility(View.GONE);
                    mChatGife.setImageResource(R.drawable.add_chat);
                    isGifeVisible = false;
                    getDialog().getWindow().setLayout(width, (height / 5) * 4);
                } else {
                    mChatEmoji.setImageResource(R.drawable.chat_emoji);
                    mLinearLayout.setVisibility(View.GONE);
                    showKeyboard();
                    isVisible = true;
                    mChatGife.setVisibility(View.VISIBLE);
                    chat_message_send.setVisibility(View.GONE);
                    getDialog().getWindow().setLayout(width, (height / 2));
                }

                break;
            case R.id.chat_msg_input:
                //输入框点击，隐藏表情和礼物
                showKeyboard();
                getDialog().getWindow().setLayout(width, (height / 2));
                mLinearLayout.setVisibility(View.GONE);
                mChatEmoji.setImageResource(R.drawable.chat_emoji);

                if (isGifeVisible) {
                    mGiftPad.setVisibility(View.GONE);
                    mChatGife.setImageResource(R.drawable.add_chat);
                }
                if (chat_gift_all_pad.getVisibility() == View.VISIBLE) {
                    chat_gift_all_pad.setVisibility(View.GONE);
                    isGifeVisible = false;
                }
                break;
            case R.id.chat_gift:
                //礼物栏不可见
                if (!isGifeVisible) {
                    mGiftPad.setVisibility(View.VISIBLE);//显示
                    mChatGife.setImageResource(R.drawable.chat_soft_input);
                    isGifeVisible = true;
                    mLinearLayout.setVisibility(View.GONE);
                    mChatEmoji.setImageResource(R.drawable.chat_emoji);
                    isVisible = true;
                    mChatGife.setVisibility(View.VISIBLE);
                    chat_message_send.setVisibility(View.GONE);
                    hideMsgIputKeyboard();
                    getDialog().getWindow().setLayout(width, (height / 5) * 4);
                } else {
                    mGiftPad.setVisibility(View.GONE);
                    chat_gift_all_pad.setVisibility(View.GONE);
                    mChatGife.setImageResource(R.drawable.add_chat);
                    isGifeVisible = false;
                    showKeyboard();
                    getDialog().getWindow().setLayout(width, (height / 2));
                }
                break;
            case R.id.act_chat_gift_send://发送礼物
                if (selected_gift == null) {
                    Notifier.showNormalMsg(getActivity(), "还没有选择礼物哦~");
                    return;
                }
                try {

                    giftcount = 1;
                    amountTransfer++;
                    sendgiftattr = HXUtil
                            .createPrivateChatGiftMessage(selected_gift, giftcount, amountTransfer, 0);
                    int isGif = sendgiftattr.getJSONObject("command")
                                            .getJSONObject("giftElem")
                                            .optInt("animation");
                    if (isGif == 0) {
                        view_liv_send_button.setVisibility(View.VISIBLE);
                        fl_background.setVisibility(View.VISIBLE);
                        ll_give.setVisibility(View.VISIBLE);
                        ll_give2.setVisibility(View.VISIBLE);
                        ll_give3.setVisibility(View.VISIBLE);
                        ll_give4.setVisibility(View.VISIBLE);
                        CircleAnimation
                                .startScaleAnimationtwo(view_liv_send_button);
                        act_chat_gift_send.setVisibility(View.GONE);
                        sendgifttimer = new CountDownTimer(3000, 150) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                long mill = millisUntilFinished / 150;
                                if (null == tv_time) {
                                    return;
                                }
                                tv_time.setText(mill + "");
                            }


                            @Override
                            public void onFinish() {
                                if (amountTransfer > 0) {
                                }
                                if (tv_time == null) {
                                    return;
                                }
                                tv_time.setText("0");
                                amountTransfer = 0;
                                act_chat_gift_send.setVisibility(View.VISIBLE);
                                view_liv_send_button.setVisibility(View.GONE);
                                fl_background.setVisibility(View.GONE);
                                ll_give.setVisibility(View.GONE);
                                ll_give2.setVisibility(View.GONE);
                                ll_give3.setVisibility(View.GONE);
                                ll_give4.setVisibility(View.GONE);
                            }
                        };
                        sendgifttimer.start();
                        onSendGift(1);
                    } else {
                        onSendGift(1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.chat_message_send://发送消息
                onSendMsg();
                break;
            case R.id.chat_speed://语音
                if (!isSoundVisible) {
                    y = 0f;
                    chat_speed.setImageResource(R.drawable.chat_soft_input);
                    mEditIn.setVisibility(View.GONE);
                    tv_down.setVisibility(View.VISIBLE);
                    tv_down.setText("按住说话");
                    tv_down.setOnClickListener(this);
                    tv_down.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            if (motionEvent.getAction() ==
                                    MotionEvent.ACTION_DOWN) {//开始录音
                                y = motionEvent.getY();
                                tv_down.setText("松开结束");
                                fl_chat.setVisibility(View.VISIBLE);
                                if (!mboolean) {
                                    mboolean = true;
                                    startRecording();
                                }
                                iv_chat_sount
                                        .setImageResource(R.drawable.anim_chat);
                                animationDrawable = (AnimationDrawable) iv_chat_sount
                                        .getDrawable();
                                animationDrawable.start();
                            } else if (motionEvent.getAction() ==
                                    MotionEvent.ACTION_UP) {//发送
                                tv_down.setText("按住说话");
                                fl_chat.setVisibility(View.GONE);
                                animationDrawable.stop();
                                mboolean = false;
                                if (stopRecording() == false) {
                                    return true;
                                }
                                if (isSoundcancel) {
                                    onSendSound(mPttFile.getAbsolutePath());
                                } else {
                                    isSoundcancel = true;
                                }
                            } else if (motionEvent.getAction() ==
                                    MotionEvent.ACTION_MOVE) {//取消的发送
                                if (Math.abs(motionEvent.getY() - y) > 50f) {
                                    isSoundcancel = false;
                                }
                            }
                            return true;
                        }
                    });
                    isSoundVisible = true;
                } else {
                    chat_speed.setImageResource(R.drawable.chat_speech);
                    mEditIn.setVisibility(View.VISIBLE);
                    tv_down.setVisibility(View.GONE);
                    isSoundVisible = false;
                }

                break;
            case R.id.view_liv_send_button://连发礼物
                giftcount = 1;
                amountTransfer++;
                sendgifttimer.cancel();
                sendgifttimer.start();
                Animation scaleAnimation = new ScaleAnimation(1.2f, 1f, 1.2f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                //设置动画时间
                scaleAnimation.setDuration(200);
                scaleAnimation.setRepeatCount(0);
                scaleAnimation.setInterpolator(new DecelerateInterpolator());
                view_liv_send_button.startAnimation(scaleAnimation);
                onSendGift(1);
                break;
            case R.id.ll_give://连发礼物1
                giftcount = Integer.parseInt(tv_count1.getText().toString());
                amountTransfer += Integer
                        .parseInt(tv_count1.getText().toString());
                sendgifttimer.cancel();
                sendgifttimer.start();
                Animation scaleAnimation1 = new ScaleAnimation(1.2f, 1f, 1.2f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                //设置动画时间
                scaleAnimation1.setDuration(200);
                scaleAnimation1.setRepeatCount(0);
                scaleAnimation1.setInterpolator(new DecelerateInterpolator());
                ll_give.startAnimation(scaleAnimation1);
                onSendGift(1);
                break;
            case R.id.ll_give2://连发礼物9
                giftcount = Integer.parseInt(tv_count2.getText().toString());
                amountTransfer += Integer
                        .parseInt(tv_count2.getText().toString());
                sendgifttimer.cancel();
                sendgifttimer.start();
                Animation scaleAnimation2 = new ScaleAnimation(1.2f, 1f, 1.2f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                //设置动画时间
                scaleAnimation2.setDuration(200);
                scaleAnimation2.setRepeatCount(0);
                scaleAnimation2.setInterpolator(new DecelerateInterpolator());
                ll_give2.startAnimation(scaleAnimation2);
                onSendGift(1);
                break;
            case R.id.ll_give3://连发礼物99
                giftcount = Integer.parseInt(tv_count3.getText().toString());
                //                amountTransfer += Integer.parseInt(tv_count3.getText().toString());
                sendgifttimer.cancel();
                sendgifttimer.start();
                Animation scaleAnimation3 = new ScaleAnimation(1.2f, 1f, 1.2f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                //设置动画时间
                scaleAnimation3.setDuration(200);
                scaleAnimation3.setRepeatCount(0);
                scaleAnimation3.setInterpolator(new DecelerateInterpolator());
                ll_give3.startAnimation(scaleAnimation3);
                onSendGift(1);
                break;
            case R.id.ll_give4://连发礼物999
                giftcount = Integer.parseInt(tv_count4.getText().toString());
                amountTransfer += Integer
                        .parseInt(tv_count4.getText().toString());
                sendgifttimer.cancel();
                sendgifttimer.start();
                Animation scaleAnimation4 = new ScaleAnimation(1.2f, 1f, 1.2f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                //设置动画时间
                scaleAnimation4.setDuration(200);
                scaleAnimation4.setRepeatCount(0);
                scaleAnimation4.setInterpolator(new DecelerateInterpolator());
                ll_give4.startAnimation(scaleAnimation4);
                onSendGift(1);
                break;
        }
    }


    private void sendgiftAlertDialog() {
        final ShowDialog showDialog = new ShowDialog(getActivity(), "当前余额不足", "确定", "");
        showDialog.setClicklistener(new ShowDialog.ClickListenerInterface() {
            @Override
            public void doConfirm() {
                showDialog.dismiss();
            }


            @Override
            public void doCancel() {
                showDialog.dismiss();
            }
        });
        showDialog.show();
    }


    //显示软键盘
    public void showKeyboard() {
        mEditIn.setFocusable(true);
        mEditIn.setFocusableInTouchMode(true);
        mEditIn.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) mEditIn
                .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(mEditIn, 0);
    }


    public void hideMsgIputKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        mEditIn.clearFocus();
        imm.hideSoftInputFromWindow(mEditIn.getWindowToken(), 0);
    }


    private void onSendMsg() {
        final String msg = mEditIn.getText().toString();
        mEditIn.setText("");
        if (msg.length() > 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (msg.length() == 0) {
                        return;
                    }
                    TIMMessage Nmsg = new TIMMessage();
                    final TIMCustomElem ce = new TIMCustomElem();
                    ce.setData((HXUtil.createLiveChatTextMessage(msg).toString()
                                      .getBytes()));
                    if (Nmsg.addElement(ce) != 0) {
                        return;
                    }
                    mConversation
                            .sendMessage(Nmsg, new TIMValueCallBack<TIMMessage>() {
                                @Override
                                public void onError(int i, String s) {
                                    //                            getMessage();
                                }


                                @Override
                                public void onSuccess(TIMMessage timMessage) {
                                    //                            getMessage();
                                    TIMElem elem = timMessage.getElement(0);
                                    try {
                                        JSONObject attr = new JSONObject(new String(((TIMCustomElem) elem)
                                                .getData(), "utf-8"));
                                        String tex = attr.optString("text");
                                        MessageInfoDB chatEntity = new MessageInfoDB();
                                        chatEntity.setType(1);
                                        if (timMessage.isSelf()) {
                                            chatEntity.setIssend(0);
                                        } else {
                                            chatEntity.setIssend(1);
                                        }
                                        chatEntity.setUserid(user_id + "");
                                        chatEntity.setSendstatue(timMessage
                                                .status());
                                        chatEntity.setMessagetype("text");
                                        chatEntity
                                                .setUuid(timMessage.getMsgId());
                                        chatEntity.setMessage(tex);
                                        chatEntity.setCreatetime(timMessage
                                                .timestamp());
                                        chatEntity.setTime(0);
                                        listChatEntity.add(chatEntity);
                                        //保存数据库
                                        MessageInfoDao mesageInfo = new MessageInfoDao(getActivity());
                                        mesageInfo
                                                .addorupdate(chatEntity, timMessage
                                                        .getMsgId());
                                        ConcernInfoDao concernInfoDao = new ConcernInfoDao(getActivity());
                                        UnFollowConcernInfoDao unconcernInfoDao = new UnFollowConcernInfoDao(getActivity());
                                        try {
                                            ConcernInfoDB concernInfoByUId = concernInfoDao
                                                    .getConcernInfoByUId(
                                                            user_id + "");
                                            UnFollowConcernInfoDB unFollowConcernInfoByUId = unconcernInfoDao
                                                    .getUnFollowConcernInfoByUId(
                                                            user_id + "");
                                            if (null != concernInfoByUId) {
                                                GetMessageUtil
                                                        .savaDatatwo(1, tex, getActivity(),
                                                                user_id + "");
                                            }
                                            if (null !=
                                                    unFollowConcernInfoByUId) {
                                                GetMessageUtil
                                                        .savaDatatwo(0, tex, getActivity(),
                                                                user_id + "");
                                            }
                                            if (null == concernInfoByUId &&
                                                    null ==
                                                            unFollowConcernInfoByUId) {
                                                showPerInfoCard(
                                                        HXApp.getInstance()
                                                             .getUserInfo().user_id +
                                                                "",
                                                        user_id + "", tex);
                                            }
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                        }
                                        chatMsgListAdapter
                                                .notifyDataSetChanged();
                                        mListView.setVisibility(View.VISIBLE);
                                        if (mListView.getCount() > 1) {
                                            //                                    if(mIsLoading){
                                            //                                        mListView.setSelection(0);
                                            //                                    }else{
                                            mListView.setSelection(
                                                    mListView.getCount() - 1);
                                            //                                    }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                    //                    getMessage();
                }
            }).start();
        }
    }


    private void savaData(int relation, String text, String userInfo) {
        try {
            if (relation == 1) {
                ConcernInfoDao concernInfoDao = new ConcernInfoDao(getActivity());
                ConcernInfoDB concernInfoDB = new ConcernInfoDB();
                concernInfoDB.setUser_id(user_id + "");
                concernInfoDB.setUserinfo(userInfo);
                concernInfoDB.setLastmessage(text);
                concernInfoDB.setHxtype("");
                concernInfoDB.setUnread("");
                concernInfoDB.setRelation(relation);
                concernInfoDB.setUpdatetime(System.currentTimeMillis());

                concernInfoDao.addorupdate(concernInfoDB, user_id + "");
            } else {
                UnFollowConcernInfoDao unFollowConcernInfoDao = new UnFollowConcernInfoDao(getActivity());
                UnFollowConcernInfoDB unFollowConcernInfoDB = new UnFollowConcernInfoDB();
                unFollowConcernInfoDB.setUser_id(user_id + "");
                unFollowConcernInfoDB.setUserinfo(userInfo);
                unFollowConcernInfoDB.setLastmessage(text);
                unFollowConcernInfoDB.setHxtype("");
                unFollowConcernInfoDB.setUnread("");
                unFollowConcernInfoDB.setRelation(relation);
                unFollowConcernInfoDB.setUpdatetime(System.currentTimeMillis());

                unFollowConcernInfoDao
                        .addorupdate(unFollowConcernInfoDB, user_id + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //显示个人信息卡片
    private void showPerInfoCard(String user_id, String viewed_user_id, final String tex) {
        HashMap<String, Object> para = new HashMap<>();
        para.put("user_id", user_id);
        para.put("viewed_user_id", viewed_user_id);

        HXJavaNet.post(HXJavaNet.url_getuserinfo, para, new AngelNetCallBack() {
            @Override
            public void onSuccess(int code, String data, String msg) {
                if (code == 200) {
                    try {
                        JSONObject jo = new JSONObject(data);
                        user = new JSONObject();
                        relation_type = jo.optInt("relation_type");
                        user.put("user_id", jo.optString("user_id"));
                        user.put("user_name", jo.optString("nickname"));
                        user.put("user_avatar", jo.optString("photo"));
                        user.put("address", jo.optString("address"));
                        user.put("ticket_amount", jo
                                .optString("ticket_amount"));
                        user.put("fans_amount", jo.optInt("fans_amount"));
                        user.put("relation_type", jo.optInt("relation_type"));
                        user.put("grade", jo.optString("grade"));
                        user.put("gender", jo.optString("gender"));
                        user.put("send_out_vca", jo.optString("send_out_vca"));

                        savaData(relation_type, tex, user.toString());
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x2711 && resultCode == HXApp.result_ok) {
            String file = data.getStringExtra("file");
            onSendPhoto(file);
        }
    }


    //发送图片
    private void onSendPhoto(String file) {
        TIMMessage Nmsg = new TIMMessage();
        TIMImageElem elem = new TIMImageElem();
        elem.setPath(file);
        elem.setLevel(mPicLevel);
        //            final TIMCustomElem ce = new TIMCustomElem();
        //            ce.setData((HXUtil.createPhotoMessage(timImage).toString().getBytes()));
        Nmsg.addElement(elem);
        //            if (Nmsg.addElement(ce) != 0) {
        //                return;
        //            }
        mConversation.sendMessage(Nmsg, new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {

            }


            @Override
            public void onSuccess(TIMMessage timMessage) {
                MessageInfoDB chatEntity = new MessageInfoDB();
                for (int i = 0; i < timMessage.getElementCount(); ++i) {
                    TIMElem element = timMessage.getElement(0);
                    TIMElemType elemType = element.getType();
                    if (elemType == TIMElemType.Image) {
                        TIMImageElem e = (TIMImageElem) element;
                        //                            for(TIMImage image : e.getImageList()) {
                        chatEntity.setType(1);
                        if (timMessage.isSelf()) {
                            chatEntity.setIssend(0);
                        } else {
                            chatEntity.setIssend(1);
                        }
                        chatEntity.setUserid(user_id + "");
                        chatEntity.setSendstatue(timMessage.status());
                        chatEntity.setMessagetype("photo");
                        chatEntity.setUuid(timMessage.getMsgId());
                        chatEntity.setMessage(e.getImageList().get(1).getUrl());
                        chatEntity.setCreatetime(timMessage.timestamp());
                        chatEntity.setTime(0);

                        listChatEntity.add(chatEntity);
                        //保存数据库
                        MessageInfoDao mesageInfo = new MessageInfoDao(getActivity());
                        mesageInfo
                                .addorupdate(chatEntity, timMessage.getMsgId());

                        ConcernInfoDao concernInfoDao = new ConcernInfoDao(getActivity());
                        UnFollowConcernInfoDao unconcernInfoDao = new UnFollowConcernInfoDao(getActivity());
                        try {
                            ConcernInfoDB concernInfoByUId = concernInfoDao
                                    .getConcernInfoByUId(user_id + "");
                            UnFollowConcernInfoDB unFollowConcernInfoByUId = unconcernInfoDao
                                    .getUnFollowConcernInfoByUId(user_id + "");
                            if (null != concernInfoByUId) {
                                GetMessageUtil
                                        .savaDatatwo(1, "[图片]", getActivity(),
                                                user_id + "");
                            }
                            if (null != unFollowConcernInfoByUId) {
                                GetMessageUtil
                                        .savaDatatwo(0, "[图片]", getActivity(),
                                                user_id + "");
                            }
                            if (null == concernInfoByUId &&
                                    null == unFollowConcernInfoByUId) {
                                showPerInfoCard(HXApp.getInstance()
                                                     .getUserInfo().user_id +
                                        "", user_id + "", "[图片]");
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        chatMsgListAdapter.notifyDataSetChanged();
                        mListView.setVisibility(View.VISIBLE);
                        if (mListView.getCount() > 1) {
                            mListView.setSelection(mListView.getCount() - 1);
                        }
                        TIMMessage Nmsgphoto = new TIMMessage();
                        final TIMCustomElem ce = new TIMCustomElem();
                        ce.setData((HXUtil.createPhotoMessage(e.getImageList()
                                                               .get(1))
                                          .toString().getBytes()));
                        if (Nmsgphoto.addElement(ce) != 0) {
                            return;
                        }
                        mConversation
                                .sendMessage(Nmsgphoto, new TIMValueCallBack<TIMMessage>() {
                                    @Override
                                    public void onError(int i, String s) {

                                    }


                                    @Override
                                    public void onSuccess(TIMMessage timMessage) {

                                    }
                                });
                        //                            }
                    }
                }
            }
        });
    }


    private void onSendSound(final String path) {
        if (path.length() == 0) {
            return;
        }
        //从文件读取数据
        File f = new File(path);
        if (f.length() == 0) {
            return;
        }
        byte[] fileData = new byte[(int) f.length()];
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(new FileInputStream(f));
            dis.readFully(fileData);
            dis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        TIMMessage Nmsg = new TIMMessage();
        TIMSoundElem elem = new TIMSoundElem();
        elem.setData(fileData);
        elem.setPath(path);
        elem.setDuration(mPttRecordTime);
        final TIMCustomElem ce = new TIMCustomElem();
        ce.setData((HXUtil.createsoundMessage(mPttRecordTime).toString()
                          .getBytes()));
        if (0 != Nmsg.addElement(elem)) {
            return;
        }
        Nmsg.addElement(ce);
        mConversation.sendMessage(Nmsg, new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {
                Notifier.showNormalMsg(getActivity(), s);
            }


            @Override
            public void onSuccess(TIMMessage timMessage) {
                MessageInfoDB chatEntity = new MessageInfoDB();
                //                for(int i = 0; i < timMessage.getElementCount(); ++i) {
                TIMElem element = timMessage.getElement(0);
                TIMElemType elemType = element.getType();
                if (elemType == TIMElemType.Sound) {
                    TIMSoundElem e = (TIMSoundElem) element;
                    //                            for(TIMImage image : e.getImageList()) {
                    chatEntity.setType(1);
                    if (timMessage.isSelf()) {
                        chatEntity.setIssend(0);
                    } else {
                        chatEntity.setIssend(1);
                    }
                    chatEntity.setUserid(user_id + "");
                    chatEntity.setSendstatue(timMessage.status());
                    chatEntity.setMessagetype("sound");
                    chatEntity.setUuid(e.getUuid());
                    chatEntity.setMessage(path);
                    chatEntity.setCreatetime(timMessage.timestamp());
                    chatEntity.setTime(mPttRecordTime);

                    listChatEntity.add(chatEntity);
                    //保存数据库
                    MessageInfoDao mesageInfo = new MessageInfoDao(getActivity());
                    mesageInfo.addorupdate(chatEntity, e.getUuid());

                    ConcernInfoDao concernInfoDao = new ConcernInfoDao(getActivity());
                    UnFollowConcernInfoDao unconcernInfoDao = new UnFollowConcernInfoDao(getActivity());
                    try {
                        ConcernInfoDB concernInfoByUId = concernInfoDao
                                .getConcernInfoByUId(user_id + "");
                        UnFollowConcernInfoDB unFollowConcernInfoByUId = unconcernInfoDao
                                .getUnFollowConcernInfoByUId(user_id + "");
                        if (null != concernInfoByUId) {
                            GetMessageUtil.savaDatatwo(1, "[语音]", getActivity(),
                                    user_id + "");
                        }
                        if (null != unFollowConcernInfoByUId) {
                            GetMessageUtil.savaDatatwo(0, "[语音]", getActivity(),
                                    user_id + "");
                        }
                        if (null == concernInfoByUId &&
                                null == unFollowConcernInfoByUId) {
                            showPerInfoCard(
                                    HXApp.getInstance().getUserInfo().user_id +
                                            "", user_id + "", "[语音]");
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    chatMsgListAdapter.notifyDataSetChanged();
                    mListView.setVisibility(View.VISIBLE);
                    if (mListView.getCount() > 1) {
                        mListView.setSelection(mListView.getCount() - 1);
                    }
                }
            }
        });
    }


    private void startRecording() {
        try {

            File file = new File("record_tmp.mp3");
            if (file.exists()) {
                file.delete();
            }
            mPttFile = File.createTempFile("record_tmp", ".mp3");
            if (mRecorder == null) {
                mRecorder = new MediaRecorder();
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                mRecorder.setOutputFile(mPttFile.getAbsolutePath());
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                mRecorder.setPreviewDisplay(null);
                mRecorder.prepare();
            }
            mPttRecordTime = System.currentTimeMillis();
            mRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
                @Override
                public void onError(MediaRecorder mr, int what, int extra) {
                    // TODO Auto-generated method stub
                    stopRecording();
                    Toast.makeText(getActivity(),
                            "录音发生错误:" + what, Toast.LENGTH_SHORT).show();
                }
            });
            mRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean stopRecording() {

        if (mRecorder != null) {
            mRecorder.setOnErrorListener(null);
            try {
                mRecorder.reset();
            } catch (IllegalStateException e) {
                mRecorder.release();
                mRecorder = null;
                return false;
            } catch (Exception e) {
                mRecorder.release();
                mRecorder = null;
                return false;
            }
            mRecorder.release();
            mRecorder = null;
        }
        mPttRecordTime = System.currentTimeMillis() - mPttRecordTime;
        if (mPttRecordTime < 1000) {
            Toast.makeText(getActivity(), "录音时间太短!", Toast.LENGTH_SHORT).show();
            return false;
        }
        mPttRecordTime = mPttRecordTime / 1000;
        return true;
    }


    //发礼物
    private synchronized void onSendGift(int flag) {
        try {
            if (amountTransfer == 0) {
                amountTransfer = giftcount;
            } else {
                amountTransfer += giftcount;
            }
            int gift_price = sendgiftattr.optJSONObject("command")
                                         .optJSONObject("giftElem")
                                         .optInt("price");
            int gift_type = sendgiftattr.optJSONObject("command")
                                        .optJSONObject("giftElem")
                                        .optInt("type");
            if (gift_type != 2) {
                if (Integer
                        .parseInt(act_chat_gift_account.getText().toString()) <
                        gift_price * giftcount) {
                    sendgiftAlertDialog();
                    amountTransfer -= giftcount;
                    return;
                }
            } else {
                if (Integer.parseInt(act_chat_species_account.getText()
                                                             .toString()) <
                        gift_price * giftcount) {
                    sendgiftAlertDialog();
                    amountTransfer -= giftcount;
                    return;
                }
            }
            if (sendgiftqueue.size() == 0) {
                if (flag == 1) {
                    sendgiftqueue.offer(HXUtil
                            .createPrivateChatGiftMessage(selected_gift, giftcount, amountTransfer, 0));
                } else {
                    sendgiftattr.optJSONObject("command")
                                .put("giftSearilNum", amountTransfer);
                    sendgiftattr.optJSONObject("command")
                                .put("giftCount", giftcount);
                    sendgiftqueue.offer(sendgiftattr);
                }
                sendgiftQueue();
            } else {
                if (flag == 1) {
                    sendgiftqueue.offer(HXUtil
                            .createPrivateChatGiftMessage(selected_gift, giftcount, amountTransfer, 0));
                } else {
                    sendgiftattr.optJSONObject("command")
                                .put("giftSearilNum", amountTransfer);
                    sendgiftattr.optJSONObject("command")
                                .put("giftCount", giftcount);
                    sendgiftqueue.offer(sendgiftattr);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void sendgiftQueue() {
        final JSONObject jsonObject = sendgiftqueue.peek();
        final String gift_id = jsonObject.optJSONObject("command")
                                         .optJSONObject("giftElem")
                                         .optString("gift_id");
        final int gift_type = jsonObject.optJSONObject("command")
                                        .optJSONObject("giftElem")
                                        .optInt("type");
        final int gift_once_count = jsonObject.optJSONObject("command")
                                              .optInt("giftCount");
        int gift_price = jsonObject.optJSONObject("command")
                                   .optJSONObject("giftElem").optInt("price");
        if (gift_type != 2) {
            if (Integer.parseInt(act_chat_gift_account.getText().toString()) <
                    gift_price * gift_once_count) {
                sendgiftAlertDialog();
                sendgiftqueue.clear();
                amountTransfer -= giftcount;
                return;
            }
        } else {
            if (Integer
                    .parseInt(act_chat_species_account.getText().toString()) <
                    gift_price * gift_once_count) {
                sendgiftAlertDialog();
                sendgiftqueue.clear();
                amountTransfer -= giftcount;
                return;
            }
        }
        HashMap<String, Object> per = new HashMap<String, Object>();
        per.put("user_id", HXApp.getInstance().getUserInfo().user_id);
        per.put("receiver_id", user_id);
        per.put("amount", gift_once_count);
        per.put("gift_id", gift_id);
        HXJavaNet
                .post(HXJavaNet.url_give_user_gift, per, new AngelNetCallBack() {
                    @Override
                    public void onSuccess(int ret_code, String ret_data, String msg) {
                        if (ret_code == 406) {
                            sendgiftAlertDialog();
                            sendgiftqueue.clear();
                        } else if (ret_code == 200) {
                            int charm_value = 0;
                            int rate = 0;
                            try {
                                if (gift_id.equals(DemoConstants.AIRCRAFT)) {
                                    HashMap<String, Object> per = new HashMap<String, Object>();
                                    per.put("user_id", HXApp.getInstance()
                                                            .getUserInfo().user_id);
                                    per.put("receiver_id", user_id);
                                    per.put("amount", gift_once_count);
                                    per.put("gift_id", gift_id);
                                    HXJavaNet
                                            .post(HXJavaNet.url_send_gift_msg, per, new AngelNetCallBack() {
                                                @Override
                                                public void onSuccess(int ret_code, String ret_data, String msg) {
                                                }


                                                @Override
                                                public void onFailure(String msg) {
                                                }
                                            });
                                }
                                JSONObject json = new JSONObject(ret_data);
                                charm_value = json.getInt("charm_value");
                                int diamond_amount = json
                                        .getInt("diamond_amount");
                                loveGradegift = json.optInt("loveGrade");
                                loveGradeRatiogift = json
                                        .optInt("loveGradeRatio");
                                if (gift_type != 2) {
                                    act_chat_gift_account
                                            .setText(diamond_amount + "");
                                } else {//黑豆
                                    act_chat_species_account
                                            .setText(diamond_amount + "");
                                }
                                rate = json.getInt("rate");//黑豆获奖
                                int grade = json.getInt("grade");//等级
                                HXApp.getInstance().getUserInfo().grade = grade;
                                sendgiftqueue.poll();
                                if (sendgiftqueue.size() > 0) {
                                    sendgiftQueue();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            JSONObject jsonObjectcom = jsonObject
                                    .optJSONObject("command");
                            try {
                                jsonObjectcom.put("giftCount", gift_once_count);
                                jsonObjectcom.put("giftReward", rate);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            TIMMessage Tim = new TIMMessage();
                            TIMCustomElem elem = new TIMCustomElem();
                            TIMTextElem elemtext = new TIMTextElem();
                            elem.setData(jsonObject.toString().getBytes());
                            elem.setDesc(UNREAD);
                            if (1 == Tim.addElement(elem)) {
                                Notifier.showNormalMsg(getActivity(), "出错了...");
                                return;
                            }
                            Tim.addElement(elemtext);
                            mConversation
                                    .sendMessage(Tim, new TIMValueCallBack<TIMMessage>() {
                                                @Override
                                                public void onError(int i, String s) {
                                                    Logger.out("send gift error" + i +
                                                            ": " + s);
                                                }


                                                @Override
                                                public void onSuccess(TIMMessage timMessage) {

                                                    try {
                                                        MessageInfoDB chatEntity = new MessageInfoDB();
                                                        TIMElem elem = timMessage
                                                                .getElement(0);
                                                        JSONObject attr = new JSONObject(new String(((TIMCustomElem) elem)
                                                                .getData(), "utf-8"));
                                                        JSONObject attrCommand = attr
                                                                .optJSONObject("command");
                                                        chatEntity.setType(1);
                                                        if (timMessage.isSelf()) {
                                                            chatEntity.setIssend(0);
                                                        } else {
                                                            chatEntity.setIssend(1);
                                                        }
                                                        chatEntity.setUserid(
                                                                user_id + "");
                                                        chatEntity
                                                                .setSendstatue(timMessage
                                                                        .status());
                                                        chatEntity
                                                                .setMessagetype("gift");
                                                        chatEntity.setUuid(timMessage
                                                                .getMsgId());
                                                        chatEntity
                                                                .setMessage(attrCommand
                                                                        .toString());
                                                        chatEntity
                                                                .setCreatetime(timMessage
                                                                        .timestamp());
                                                        chatEntity.setTime(0);

                                                        listChatEntity.add(chatEntity);
                                                        //保存数据库
                                                        MessageInfoDao mesageInfo = new MessageInfoDao(getActivity());
                                                        mesageInfo
                                                                .addorupdate(chatEntity, timMessage
                                                                        .getMsgId());

                                                        ConcernInfoDao concernInfoDao = new ConcernInfoDao(getActivity());
                                                        UnFollowConcernInfoDao unconcernInfoDao = new UnFollowConcernInfoDao(getActivity());
                                                        ConcernInfoDB concernInfoByUId = concernInfoDao
                                                                .getConcernInfoByUId(
                                                                        user_id + "");
                                                        UnFollowConcernInfoDB unFollowConcernInfoByUId = unconcernInfoDao
                                                                .getUnFollowConcernInfoByUId(
                                                                        user_id + "");
                                                        if (null != concernInfoByUId) {
                                                            GetMessageUtil
                                                                    .savaDatatwo(1, "[礼物]", getActivity(),
                                                                            user_id +
                                                                                    "");
                                                        }
                                                        if (null !=
                                                                unFollowConcernInfoByUId) {
                                                            GetMessageUtil
                                                                    .savaDatatwo(0, "[礼物]", getActivity(),
                                                                            user_id +
                                                                                    "");
                                                        }
                                                        if (null == concernInfoByUId &&
                                                                null ==
                                                                        unFollowConcernInfoByUId) {
                                                            showPerInfoCard(
                                                                    HXApp.getInstance()
                                                                         .getUserInfo().user_id +
                                                                            "",
                                                                    user_id +
                                                                            "", "[礼物]");
                                                        }
                                                        chatMsgListAdapter
                                                                .notifyDataSetChanged();
                                                        mListView
                                                                .setVisibility(View.VISIBLE);
                                                        if (mListView.getCount() > 1) {
                                                            mListView.setSelection(
                                                                    mListView
                                                                            .getCount() -
                                                                            1);
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }

                                    );
                        } else if (ret_code == 0) {
                            sendgiftqueue.clear();
                        }
                    }


                    @Override
                    public void onFailure(String msg) {
                        if (sendgiftqueue != null) {
                            sendgiftqueue.poll();
                        }
                        if (sendgiftqueue.size() > 0) {
                            sendgiftQueue();
                        }
                    }
                });
    }
}
