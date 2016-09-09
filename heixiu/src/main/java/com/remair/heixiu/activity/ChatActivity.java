package com.remair.heixiu.activity;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.remair.heixiu.DemoConstants;
import com.remair.heixiu.bean.Gift;
import com.remair.heixiu.giftview.GiftSelector;
import com.remair.heixiu.giftview.GiftSelectorPage;
import com.remair.heixiu.giftview.GiftWrapper;
import com.remair.heixiu.HXActivityV4;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.HXJavaNet;
import com.remair.heixiu.utils.HXUtil;
import com.remair.heixiu.R;
import com.remair.heixiu.bean.UserInfo;
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
import com.remair.heixiu.view.LoadingDialog;
import com.remair.heixiu.view.RechargeDialog;
import com.remair.heixiu.view.ShowDialog;
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
import org.json.JSONException;
import org.json.JSONObject;
import studio.archangel.toolkitv2.activities.PickImageActivity;
import studio.archangel.toolkitv2.adapters.CommonRecyclerAdapter;
import studio.archangel.toolkitv2.util.Logger;
import studio.archangel.toolkitv2.util.Util;
import studio.archangel.toolkitv2.util.networking.AngelNetCallBack;
import studio.archangel.toolkitv2.util.text.Notifier;
import studio.archangel.toolkitv2.views.AngelActionBar;
import studio.archangel.toolkitv2.views.AngelMaterialButton;

/**
 * Created by JXHIUUI on 2016/2/27.
 */
public class ChatActivity extends HXActivityV4 implements View.OnClickListener {
    private final static String TAG = ChatActivity.class.getSimpleName();
    private List<List<String>> emojis;
    private ArrayList<View> pageViews;
    private ArrayList<EmojiAdapter> emojiAdapters;
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
    @BindView(R.id.fl_chat_pre) RelativeLayout fl_chat_pre;
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
    @BindView(R.id.act_chat_gift_account) public TextView act_chat_gift_account;
    @BindView(R.id.act_chat_species_account)
    public TextView act_chat_species_account;
    @BindView(R.id.ll_recharge) LinearLayout ll_recharge;
    @BindView(R.id.fl_point) FrameLayout fl_point;
    @BindView(R.id.iv_chat_delete) ImageView iv_chat_delete;
    @BindView(R.id.ll_title) RelativeLayout ll_title;
    private TextView tv_concern;
    private List<MessageInfoDB> listChatEntity;
    private ChatMessageListAdapter chatMsgListAdapter;
    private TIMConversation mConversation;
    private String user_name;
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
    private Queue<JSONObject> sendgiftqueue = new LinkedList<JSONObject>();//发礼物队列
    private int relation;//关注
    private boolean isconcern = false;
    private int loveGradegift;
    private double loveGradeRatiogift;
    private int index = 0;//每次加载数据返回的索引
    private int mTotalPointCount; //符合条件的总数据数量
    private boolean isLoading = false;//是否加载
    private long LIMIT = 20;//每次加载数据的最大限制
    private TIMMessageListener msgListener = new TIMMessageListener() {
        @Override
        public boolean onNewMessages(List<TIMMessage> list) {
            Logger.out("onNewMessagesGet  " + list.size());
            if (isTopActivity()) {
                refreshChat(list);
            }
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
    private Application mContext;


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
                                MessageInfoDao mesageInfo = new MessageInfoDao(ChatActivity.this);
                                mesageInfo.addorupdate(chatEntity, msg
                                        .getMsgId());
                                ConcernInfoDao concernInfoDao = new ConcernInfoDao(ChatActivity.this);
                                UnFollowConcernInfoDao unconcernInfoDao = new UnFollowConcernInfoDao(ChatActivity.this);
                                try {
                                    ConcernInfoDB concernInfoByUId = concernInfoDao
                                            .getConcernInfoByUId(user_id + "");
                                    UnFollowConcernInfoDB unFollowConcernInfoByUId = unconcernInfoDao
                                            .getUnFollowConcernInfoByUId(
                                                    user_id + "");
                                    if (null != concernInfoByUId) {
                                        GetMessageUtil
                                                .savaDatatwo(1, tex, ChatActivity.this,
                                                        user_id + "");
                                    }
                                    if (null != unFollowConcernInfoByUId) {
                                        GetMessageUtil
                                                .savaDatatwo(0, tex, ChatActivity.this,
                                                        user_id + "");
                                    }
                                    if (null == concernInfoByUId &&
                                            null == unFollowConcernInfoByUId) {
                                        GetMessageUtil.showPerInfoCard(
                                                HXApp.getInstance()
                                                     .getUserInfo().user_id +
                                                        "", user_id +
                                                        "", tex, ChatActivity.this, msg);
                                    }
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }

                                //                                GetMessageUtil.showPerInfoCard(HXApp.getInstance().getUserInfo().user_id + "", user_id + "", tex, ChatActivity.this, msg);
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
                                        MessageInfoDao mesageInfo = new MessageInfoDao(ChatActivity.this);
                                        mesageInfo
                                                .addorupdate(chatphotoEntity, chatphotoEntity
                                                        .getUuid());
                                        //                                        showPerInfoCard(HXApp.getInstance().getUserInfo().user_id + "", user_id + "", "[图片]");

                                        //                                        chatMsgListAdapter.notifyDataSetChanged();
                                        //                                        mListView.setVisibility(View.VISIBLE);
                                        //                                        if (mListView.getCount() > 1) {
                                        //                                            mListView.setSelection(mListView.getCount() - 1);
                                        //                                        }
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
                                    MessageInfoDao mesageInfo = new MessageInfoDao(ChatActivity.this);
                                    mesageInfo.addorupdate(chatEntity, msg
                                            .getMsgId());
                                    ConcernInfoDao concernInfoDao = new ConcernInfoDao(ChatActivity.this);
                                    UnFollowConcernInfoDao unconcernInfoDao = new UnFollowConcernInfoDao(ChatActivity.this);
                                    ConcernInfoDB concernInfoByUId = concernInfoDao
                                            .getConcernInfoByUId(user_id + "");
                                    UnFollowConcernInfoDB unFollowConcernInfoByUId = unconcernInfoDao
                                            .getUnFollowConcernInfoByUId(
                                                    user_id + "");
                                    if (null != concernInfoByUId) {
                                        GetMessageUtil
                                                .savaDatatwo(1, "[礼物]", ChatActivity.this,
                                                        user_id + "");
                                    }
                                    if (null != unFollowConcernInfoByUId) {
                                        GetMessageUtil
                                                .savaDatatwo(0, "[礼物]", ChatActivity.this,
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
                    //                    for(TIMImage image : e.getImageList()) {
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
                    MessageInfoDao mesageInfo = new MessageInfoDao(ChatActivity.this);
                    mesageInfo.addorupdate(chatphotoEntity, msg.getMsgId());

                    ConcernInfoDao concernInfoDao = new ConcernInfoDao(ChatActivity.this);
                    UnFollowConcernInfoDao unconcernInfoDao = new UnFollowConcernInfoDao(ChatActivity.this);
                    try {
                        ConcernInfoDB concernInfoByUId = concernInfoDao
                                .getConcernInfoByUId(user_id + "");
                        UnFollowConcernInfoDB unFollowConcernInfoByUId = unconcernInfoDao
                                .getUnFollowConcernInfoByUId(user_id + "");
                        if (null != concernInfoByUId) {
                            GetMessageUtil
                                    .savaDatatwo(1, "[图片]", ChatActivity.this,
                                            user_id + "");
                        }
                        if (null != unFollowConcernInfoByUId) {
                            GetMessageUtil
                                    .savaDatatwo(0, "[图片]", ChatActivity.this,
                                            user_id + "");
                        }
                        if (null == concernInfoByUId &&
                                null == unFollowConcernInfoByUId) {
                            GetMessageUtil.showPerInfoCard(
                                    HXApp.getInstance().getUserInfo().user_id +
                                            "", user_id +
                                            "", "[图片]", ChatActivity.this, list
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
                            Log.d(TAG, "file exist");
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
                                MessageInfoDao mesageInfo = new MessageInfoDao(ChatActivity.this);
                                mesageInfo.addorupdate(chatsoundEntity, msg
                                        .getMsgId());

                                ConcernInfoDao concernInfoDao = new ConcernInfoDao(ChatActivity.this);
                                UnFollowConcernInfoDao unconcernInfoDao = new UnFollowConcernInfoDao(ChatActivity.this);
                                try {
                                    ConcernInfoDB concernInfoByUId = concernInfoDao
                                            .getConcernInfoByUId(user_id + "");
                                    UnFollowConcernInfoDB unFollowConcernInfoByUId = unconcernInfoDao
                                            .getUnFollowConcernInfoByUId(
                                                    user_id + "");
                                    if (null != concernInfoByUId) {
                                        GetMessageUtil
                                                .savaDatatwo(1, "[语音]", ChatActivity.this,
                                                        user_id + "");
                                    }
                                    if (null != unFollowConcernInfoByUId) {
                                        GetMessageUtil
                                                .savaDatatwo(0, "[语音]", ChatActivity.this,
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_chat);
        mContext = getApplication();
        ButterKnife.bind(this);
        SharedPreferenceUtil.setContext(getApplicationContext());
        Util.setupActionBar(this, "快乐的小逗比");
        AngelActionBar aab = this.getAngelActionBar();
        user_id = getIntent().getIntExtra("user_id", 0);
        user_photo = getIntent().getStringExtra("user_photo");
        if (aab != null) {
            user_name = getIntent().getStringExtra("user_name");
            aab.setTitleText(user_name);
            aab.setTextColor(getResources().getColor(R.color.white));
            aab.setRightImage(R.drawable.threedpoint);
            aab.setLeftListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferenceUtil
                            .Remove(Long.parseLong(user_id + "") + "");
                    SharedPreferenceUtil.Remove("oflinemessage");
                    finish();
                }
            });
            aab.setRightListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideMsgIputKeyboard();
                    if (user_id != 0) {
                        showPopupWindow1(fl_chat_pre, user_id);
                    }
                }
            });
        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ll_title
                .getLayoutParams();
        layoutParams.topMargin = Util.getPX(48);
        ll_title.requestLayout();
        relation = getIntent().getIntExtra("relation", 0);
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
        sendBroadcast(intent);
        HXApp.getInstance().unreadCount = 0;
        ll_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HXApp.getInstance().configMessage.ali_recharge_switch.equals("1")) {
                    String mEncrypt = "";
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
                        Intent it = new Intent(mContext, MinediamondActivity.class);
                        startActivity(it);
                        return;
                    }
                    Intent intent = new Intent(mContext, WheelViewMessageActivity.class);
                    if (HXApp.isTest) {
                        intent.putExtra("url", "http://www.imheixiu.com/alipay/?ispost=1");
                    } else {
                        intent.putExtra("url", "http://www-test.imheixiu.com/alipay/?ispost=1");
                    }

                    intent.putExtra("title", "快速充值通道");
                    intent.putExtra("data", mEncrypt);
                    intent.putExtra("carousel_id", DemoConstants.CAROUSEL_ID);
                    startActivity(intent);
                } else {
                    RechargeDialog rechargeDialog = new RechargeDialog();
                    Bundle bundle = new Bundle();
                    bundle.putString("tag", "ChatActivity");
                    rechargeDialog.setArguments(bundle);
                    rechargeDialog
                            .setStyle(DialogFragment.STYLE_NO_TITLE, R.style.add_dialog);
                    rechargeDialog
                            .show(getSupportFragmentManager(), "recharge");
                }
            }
        });
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
        list.add("拍照");
        list.add("礼物");
        GridLayoutManager gm = new GridLayoutManager(mContext, 4);
        recy_type.setLayoutManager(gm);
        ChatGridAdapter adapter = new ChatGridAdapter(mContext, list);
        recy_type.setAdapter(adapter);
        adapter.setRecyclerView(recy_type);
        adapter.setOnItemToClickListener(new CommonRecyclerAdapter.OnItemToClickListener() {
            @Override
            public void onItemClick(View view, Object data) {
                UserInfo userInfo = HXApp.getInstance().getUserInfo();
                if (userInfo == null) {
                    return;
                }
                String string = (String) data;
                if (string.equals(list.get(1))) {
                    Intent intent = new Intent(mContext, PickImageActivity.class);
                    intent.putExtra("mode", PickImageActivity.mode_take_a_photo);
                    try {
                        String path = File.createTempFile("upload_image_" +
                                userInfo.user_id + "_" +
                                System.currentTimeMillis(), ".jpg", getExternalCacheDir())
                                          .getAbsolutePath();
                        intent.putExtra("file_name", path);
                        startActivityForResult(intent, 0x2711);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (string.equals(list.get(0))) {
                    Intent intent = new Intent(mContext, PickImageActivity.class);
                    intent.putExtra("mode", PickImageActivity.mode_select_from_gallery);
                    startActivityForResult(intent, 0x2711);
                } else if (string.equals(list.get(2))) {//礼物
                    chat_gift_all_pad.setVisibility(View.VISIBLE);
                    mGiftPad.setVisibility(View.GONE);
                    isGifeVisible = true;
                }
            }
        });
        mChatEmoji = (ImageView) findViewById(R.id.chat_emoji);
        mEditIn = (AppCompatEditText) findViewById(R.id.chat_msg_input);
        mLinearLayout = (SmileLayout) findViewById(R.id.ll_emojis);
        mListView = (ListView) findViewById(R.id.chat_lv);
        mPBLoadData = (ProgressBar) findViewById(R.id.pb_load_more);
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
                            .hideSoftInputFromWindow(ChatActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);*/
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
        MessageInfoDao mesageInfo = new MessageInfoDao(ChatActivity.this);
        try {
            List<MessageInfoDB> messageInfoDBList = mesageInfo
                    .getMessageInfo(0, LIMIT, user_id + "");
            Collections.reverse(messageInfoDBList);
            listChatEntity.addAll(messageInfoDBList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        chatMsgListAdapter = new ChatMessageListAdapter(ChatActivity.this, listChatEntity, user_photo, pw);
        mListView.setAdapter(chatMsgListAdapter);
        if (mListView.getCount() > 1) {
            mListView.setSelection(mListView.getCount() - 1);
        }

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:// 静止状态
                        if (isLoading) {
                            Notifier.showNormalMsg(getApplicationContext(), "正在加载中...稍后再试...");
                            return;
                        }

                        int postion = mListView.getLastVisiblePosition();// 19
                        int firstVisiblePosition = mListView
                                .getFirstVisiblePosition();
                        int size = listChatEntity.size();// 20;

                        boolean bGetData = false;
                        if (0 == firstVisiblePosition) {//第一页
                            if ((index / LIMIT) > 0) {
                                index -= LIMIT;
                                bGetData = true;
                            }
                        } else if (postion == (size - 1)) {//最后一页
                            if ((index + size) < (postion + LIMIT)) {
                                index += LIMIT;
                                bGetData = true;
                            }
                        }
                        if (bGetData) {
                            isLoading = true;
                            LoadingDialog loadingDialog = new LoadingDialog(ChatActivity.this, R.style.dialog);
                            loadingDialog.show();
                            MessageInfoDao mesageInfo = new MessageInfoDao(ChatActivity.this);
                            try {
                                List<MessageInfoDB> messageInfoDBList = mesageInfo
                                        .getMessageInfo(listChatEntity
                                                .size(), LIMIT, user_id + "");
                                if (messageInfoDBList.size() < 20) {
                                    Notifier.showNormalMsg(getApplicationContext(), "没有更多数据了");
                                }
                                Collections.reverse(messageInfoDBList);
                                //listChatEntity.addAll(messageInfoDBList);
                                listChatEntity.addAll(0, messageInfoDBList);
                                chatMsgListAdapter.notifyDataSetChanged();
                                loadingDialog.todismiss();
                                isLoading = false;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        break;

                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:// 滚动状态
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 手指滑动状态
                        break;
                }
            }


            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub

            }
        });
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
                return false;
            }
        });

        gift_selector
                .setOnGiftSelectedListener(new GiftSelectorPage.OnGiftSelectedListener() {
                    @Override
                    public void onGiftSelected(GiftWrapper gift, int position) {
                        selected_gift = gift.gift;
                        amountTransfer = 0;
                        //                giftcount = gift.count;
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


    private void showPopupWindow(View v) {
        LayoutInflater inflater = LayoutInflater.from(ChatActivity.this);
        View view = inflater.inflate(R.layout.chat_popupwindow, null);
        TextView tv_chat_copy = (TextView) view.findViewById(R.id.tv_chat_copy);
        TextView tv_chat_delete = (TextView) view
                .findViewById(R.id.tv_chat_delete);
        tv_chat_delete.setVisibility(View.GONE);
        tv_chat_copy.setText("粘贴");
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay()
                       .getHeight();//studio.archangel.toolkitv2.util.Utils.getPX(60)
        final PopupWindow pw = new PopupWindow(view, Util.getPX(50), Util
                .getPX(40));
        //        WindowManager.LayoutParams params = ChatActivity.this.getWindow().getAttributes();
        //        params.alpha = 0.7f;

        //        ChatActivity.this.getWindow().setAttributes(params);
        tv_chat_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditIn.setText(CofyUtil.paste(ChatActivity.this));
                pw.dismiss();
            }
        });
        // 点击外部可以被关闭
        pw.setOutsideTouchable(true);
        pw.setBackgroundDrawable(new BitmapDrawable());

        pw.setFocusable(true);// 设置PopupWindow允许获得焦点, 默认情况下popupWindow中的控件不可以获得焦点, 例外: Button, ImageButton..
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            pw.showAsDropDown(v, 0, -Util.getPX(40) - 50, Gravity.BOTTOM);
        } else {
            pw.showAsDropDown(v, 0, -Util.getPX(40) - 50);
        }
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
                                    MessageInfoDao mesageInfo = new MessageInfoDao(ChatActivity.this);
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
                } else {
                    mChatEmoji.setImageResource(R.drawable.chat_emoji);
                    mLinearLayout.setVisibility(View.GONE);
                    showKeyboard();
                    isVisible = true;
                    mChatGife.setVisibility(View.VISIBLE);
                    chat_message_send.setVisibility(View.GONE);
                }

                break;
            case R.id.chat_msg_input:
                //输入框点击，隐藏表情和礼物
                showKeyboard();
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
                    hideMsgIputKeyboard();
                    mGiftPad.setVisibility(View.VISIBLE);//显示
                    mChatGife.setImageResource(R.drawable.chat_soft_input);
                    isGifeVisible = true;
                    mLinearLayout.setVisibility(View.GONE);
                    mChatEmoji.setImageResource(R.drawable.chat_emoji);
                    isVisible = true;
                    mChatGife.setVisibility(View.VISIBLE);
                    chat_message_send.setVisibility(View.GONE);
                } else {
                    mGiftPad.setVisibility(View.GONE);
                    chat_gift_all_pad.setVisibility(View.GONE);
                    mChatGife.setImageResource(R.drawable.add_chat);
                    isGifeVisible = false;
                    showKeyboard();
                }
                break;
            case R.id.act_chat_gift_send://发送礼物
                if (selected_gift == null) {
                    Notifier.showNormalMsg(mContext, "还没有选择礼物哦~");
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
                                    //TODO
                                }
                                if (tv_time == null) {
                                    return;
                                }
                                tv_time.setText("0");
                                amountTransfer = 0;
                                //                                CircleAnimation.stopRotateAnmiation(view_liv_send_button);
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
                                    Log.d(TAG, "recording ret false");
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


    private void sendgiftAlertDialog(int gift_type) {
        String figt_tishiyu="嘿豆";
        if (gift_type != 2) figt_tishiyu = "钻石";
        final ShowDialog showDialog = new ShowDialog(this,
                "当前" + figt_tishiyu + "余额不足，是否需要充值？", "充值", "稍后");

        showDialog.setClicklistener(new ShowDialog.ClickListenerInterface() {
            @Override
            public void doConfirm() {
                showDialog.dismiss();
                if (HXApp.getInstance().configMessage.ali_recharge_switch.equals("1")) {
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
                        Intent it = new Intent(mContext, MinediamondActivity.class);
                        startActivity(it);
                        return;
                    }
                    Intent intent = new Intent(mContext, WheelViewMessageActivity.class);
                    if (HXApp.isTest) {
                        intent.putExtra("url", "http://www.imheixiu.com/alipay/?ispost=1");
                    } else {
                        intent.putExtra("url", "http://www-test.imheixiu.com/alipay/?ispost=1");
                    }

                    intent.putExtra("title", "快速充值通道");
                    intent.putExtra("data", mEncrypt);
                    intent.putExtra("carousel_id", DemoConstants.CAROUSEL_ID);
                    startActivity(intent);
                } else {
                    RechargeDialog rechargeDialog = new RechargeDialog();
                    Bundle bundle = new Bundle();
                    bundle.putString("tag", "ChatActivity");
                    rechargeDialog.setArguments(bundle);

                    rechargeDialog
                            .setStyle(DialogFragment.STYLE_NO_TITLE, R.style.add_dialog);
                    rechargeDialog
                            .show(getSupportFragmentManager(), "recharge");
                }
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
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mEditIn.clearFocus();
        imm.hideSoftInputFromWindow(mEditIn.getWindowToken(), 0);
    }


    private void onSendMsg() {
        if (user_id == 1) {
            mEditIn.setText("");
            return;
        }
        final String msg = mEditIn.getText().toString();
        mEditIn.setText("");
        if (msg.length() > 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        if (msg.length() == 0) {
                            return;
                        }
                        TIMMessage Nmsg = new TIMMessage();
                        final TIMCustomElem ce = new TIMCustomElem();
                        ce.setData((HXUtil.createLiveChatTextMessage(msg).toString()
                                          .getBytes()));
                        TIMTextElem tte = new TIMTextElem();
                        tte.setText(msg);

                        if (Nmsg.addElement(ce) != 0) {
                            return;
                        }
                        Nmsg.addElement(tte);

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
                                            MessageInfoDao mesageInfo = new MessageInfoDao(ChatActivity.this);
                                            mesageInfo
                                                    .addorupdate(chatEntity, timMessage
                                                            .getMsgId());
                                            ConcernInfoDao concernInfoDao = new ConcernInfoDao(ChatActivity.this);
                                            UnFollowConcernInfoDao unconcernInfoDao = new UnFollowConcernInfoDao(ChatActivity.this);
                                            try {
                                                ConcernInfoDB concernInfoByUId = concernInfoDao
                                                        .getConcernInfoByUId(
                                                                user_id + "");
                                                UnFollowConcernInfoDB unFollowConcernInfoByUId = unconcernInfoDao
                                                        .getUnFollowConcernInfoByUId(
                                                                user_id + "");
                                                if (null != concernInfoByUId) {
                                                    GetMessageUtil
                                                            .savaDatatwo(1, tex, ChatActivity.this,
                                                                    user_id + "");
                                                }
                                                if (null !=
                                                        unFollowConcernInfoByUId) {
                                                    GetMessageUtil
                                                            .savaDatatwo(0, tex, ChatActivity.this,
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //                    TIMMessage Nmsg2 = new TIMMessage();
                    //                    TIMTextElem tte=new TIMTextElem();
                    //                    tte.setText(msg);
                    //                    Nmsg2.addElement(tte);
                    //                    mConversation.sendMessage(Nmsg, new TIMValueCallBack<TIMMessage>() {
                    //                        @Override
                    //                        public void onError(int i, String s) {
                    //
                    //                        }
                    //
                    //                        @Override
                    //                        public void onSuccess(TIMMessage timMessage) {
                    //
                    //                        }
                    //                    });
                    //                    getMessage();
                }
            }).start();
        }
    }


    private boolean isTopActivity() {
        boolean isTop = false;
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        if (cn.getClassName().contains(TAG)) {
            isTop = true;
        }
        return isTop;
    }


    private void savaData(int relation, String text, String userInfo) {
        try {
            if (relation == 1) {
                ConcernInfoDao concernInfoDao = new ConcernInfoDao(ChatActivity.this);
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
                UnFollowConcernInfoDao unFollowConcernInfoDao = new UnFollowConcernInfoDao(ChatActivity.this);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x2711 && resultCode == HXApp.result_ok) {
            String file = data.getStringExtra("file");
            onSendPhoto(file);
        }
    }


    //发送图片
    private void onSendPhoto(String file) {
        if (user_id == 1) {
            return;
        }
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
                        MessageInfoDao mesageInfo = new MessageInfoDao(ChatActivity.this);
                        mesageInfo
                                .addorupdate(chatEntity, timMessage.getMsgId());

                        ConcernInfoDao concernInfoDao = new ConcernInfoDao(ChatActivity.this);
                        UnFollowConcernInfoDao unconcernInfoDao = new UnFollowConcernInfoDao(ChatActivity.this);
                        try {
                            ConcernInfoDB concernInfoByUId = concernInfoDao
                                    .getConcernInfoByUId(user_id + "");
                            UnFollowConcernInfoDB unFollowConcernInfoByUId = unconcernInfoDao
                                    .getUnFollowConcernInfoByUId(user_id + "");
                            if (null != concernInfoByUId) {
                                GetMessageUtil
                                        .savaDatatwo(1, "[图片]", ChatActivity.this,
                                                user_id + "");
                            }
                            if (null != unFollowConcernInfoByUId) {
                                GetMessageUtil
                                        .savaDatatwo(0, "[图片]", ChatActivity.this,
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
        if (user_id == 1) {
            return;
        }
        if (path.length() == 0) {
            return;
        }
        //从文件读取数据
        File f = new File(path);
        Log.d(TAG, "file len:" + f.length());
        if (f.length() == 0) {
            Log.e(TAG, "file empty!");
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
                Notifier.showNormalMsg(ChatActivity.this, s);
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
                    MessageInfoDao mesageInfo = new MessageInfoDao(ChatActivity.this);
                    mesageInfo.addorupdate(chatEntity, e.getUuid());

                    ConcernInfoDao concernInfoDao = new ConcernInfoDao(ChatActivity.this);
                    UnFollowConcernInfoDao unconcernInfoDao = new UnFollowConcernInfoDao(ChatActivity.this);
                    try {
                        ConcernInfoDB concernInfoByUId = concernInfoDao
                                .getConcernInfoByUId(user_id + "");
                        UnFollowConcernInfoDB unFollowConcernInfoByUId = unconcernInfoDao
                                .getUnFollowConcernInfoByUId(user_id + "");
                        if (null != concernInfoByUId) {
                            GetMessageUtil
                                    .savaDatatwo(1, "[语音]", ChatActivity.this,
                                            user_id + "");
                        }
                        if (null != unFollowConcernInfoByUId) {
                            GetMessageUtil
                                    .savaDatatwo(0, "[语音]", ChatActivity.this,
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
            mRecorder.setOnErrorListener(new OnErrorListener() {
                @Override
                public void onError(MediaRecorder mr, int what, int extra) {
                    // TODO Auto-generated method stub
                    stopRecording();
                    Toast.makeText(getBaseContext(),
                            "录音发生错误:" + what, Toast.LENGTH_SHORT).show();
                }
            });
            mRecorder.start();
        } catch (IOException e) {
            Log.e(TAG, "start record error" + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            Log.e(TAG, "start record error2" + e.getMessage());
            e.printStackTrace();
        }
    }


    private boolean stopRecording() {

        if (mRecorder != null) {
            mRecorder.setOnErrorListener(null);
            try {
                mRecorder.reset();
            } catch (IllegalStateException e) {
                Log.e(TAG, "stop Record error:" + e.getMessage());
                mRecorder.release();
                mRecorder = null;
                return false;
            } catch (Exception e) {
                Log.e(TAG, "stop Record Exception:" + e.getMessage());
                mRecorder.release();
                mRecorder = null;
                return false;
            }
            mRecorder.release();
            mRecorder = null;
        }
        mPttRecordTime = System.currentTimeMillis() - mPttRecordTime;
        if (mPttRecordTime < 1000) {
            Toast.makeText(this, "录音时间太短!", Toast.LENGTH_SHORT).show();
            return false;
        }
        Log.d(TAG, "time:" + SystemClock.elapsedRealtime());
        mPttRecordTime = mPttRecordTime / 1000;
        return true;
    }


    //发礼物
    private synchronized void onSendGift(int flag) {
        if (user_id == 1) {
            return;
        }
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
                    sendgiftAlertDialog(gift_type);
                    amountTransfer -= giftcount;
                    return;
                }
            } else {
                if (Integer.parseInt(act_chat_species_account.getText()
                                                             .toString()) <
                        gift_price * giftcount) {
                    sendgiftAlertDialog(gift_type);
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
        final String gift_name = jsonObject.optJSONObject("command")
                                           .optJSONObject("giftElem")
                                           .optString("name");
        final int gift_once_count = jsonObject.optJSONObject("command")
                                              .optInt("giftCount");
        int gift_price = jsonObject.optJSONObject("command")
                                   .optJSONObject("giftElem").optInt("price");
        if (gift_type != 2) {
            if (Integer.parseInt(act_chat_gift_account.getText().toString()) <
                    gift_price * gift_once_count) {
                sendgiftAlertDialog(gift_type);
                sendgiftqueue.clear();
                amountTransfer -= giftcount;
                return;
            }
        } else {
            if (Integer
                    .parseInt(act_chat_species_account.getText().toString()) <
                    gift_price * gift_once_count) {
                sendgiftAlertDialog(gift_type);
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
                            sendgiftAlertDialog(gift_type);
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
                                jsonObjectcom.put("charm_value", charm_value);
                                jsonObjectcom.put("winning_rate", rate);
                                jsonObjectcom.put("loveGrade", loveGradegift);
                                jsonObjectcom
                                        .put("loveGradeRatio", loveGradeRatiogift);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            TIMMessage Tim = new TIMMessage();
                            TIMCustomElem elem = new TIMCustomElem();
                            TIMTextElem elemtext = new TIMTextElem();
                            elem.setData(jsonObject.toString().getBytes());
                            elemtext.setText("[礼物]");
                            if (1 == Tim.addElement(elem)) {
                                Notifier.showNormalMsg(mContext, "出错了...");
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
                                                        MessageInfoDao mesageInfo = new MessageInfoDao(ChatActivity.this);
                                                        mesageInfo
                                                                .addorupdate(chatEntity, timMessage
                                                                        .getMsgId());

                                                        ConcernInfoDao concernInfoDao = new ConcernInfoDao(ChatActivity.this);
                                                        UnFollowConcernInfoDao unconcernInfoDao = new UnFollowConcernInfoDao(ChatActivity.this);
                                                        ConcernInfoDB concernInfoByUId = concernInfoDao
                                                                .getConcernInfoByUId(
                                                                        user_id + "");
                                                        UnFollowConcernInfoDB unFollowConcernInfoByUId = unconcernInfoDao
                                                                .getUnFollowConcernInfoByUId(
                                                                        user_id + "");
                                                        if (null != concernInfoByUId) {
                                                            GetMessageUtil
                                                                    .savaDatatwo(1, "[礼物]", ChatActivity.this,
                                                                            user_id +
                                                                                    "");
                                                        }
                                                        if (null !=
                                                                unFollowConcernInfoByUId) {
                                                            GetMessageUtil
                                                                    .savaDatatwo(0, "[礼物]", ChatActivity.this,
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


    private void showPopupWindow1(View v, final int viewed_user_id) {
        LayoutInflater inflater = LayoutInflater.from(ChatActivity.this);
        View view = inflater.inflate(R.layout.show_popu, null);
        TextView tv_blacklist = (TextView) view.findViewById(R.id.tv_blacklist);
        TextView tv_report = (TextView) view.findViewById(R.id.tv_report);
        TextView tv_dimiss = (TextView) view.findViewById(R.id.tv_dimiss);
        tv_concern = (TextView) view.findViewById(R.id.tv_concern);
        TextView tv_line = (TextView) view.findViewById(R.id.tv_line);
        tv_concern.setVisibility(View.VISIBLE);
        tv_line.setVisibility(View.VISIBLE);
        if (relation == 1) {
            tv_concern.setText("取消关注");
        } else if (relation == 0) {
            tv_concern.setText("加关注");
        }
        tv_concern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isconcern) {
                    HashMap<String, Object> parater = new HashMap<String, Object>();
                    parater.put("user_id", HXApp.getInstance()
                                                .getUserInfo().user_id);
                    parater.put("passive_user_id", viewed_user_id);
                    HXJavaNet
                            .post(HXJavaNet.url_concern, parater, new AngelNetCallBack() {
                                @Override
                                public void onSuccess(int ret_code, String ret_data, String msg) {
                                    if (ret_code == 200) {
                                        tv_concern.setText("取消关注");
                                        GetMessageUtil
                                                .chageSavaData(ChatActivity.this,
                                                        viewed_user_id +
                                                                "", true);
                                    }
                                }


                                @Override
                                public void onFailure(String msg) {
                                    tv_concern.setText("+加关注");
                                }
                            });

                    isconcern = true;
                } else {
                    HashMap<String, Object> parater = new HashMap<String, Object>();
                    parater.put("user_id", HXApp.getInstance()
                                                .getUserInfo().user_id);
                    parater.put("passive_user_id", viewed_user_id);
                    HXJavaNet
                            .post(HXJavaNet.url_unconcern, parater, new AngelNetCallBack() {
                                @Override
                                public void onSuccess(int ret_code, String ret_data, String msg) {
                                    if (ret_code == 200) {
                                        tv_concern.setText("+加关注");
                                        GetMessageUtil
                                                .chageSavaData(ChatActivity.this,
                                                        viewed_user_id +
                                                                "", false);
                                    }
                                }


                                @Override
                                public void onFailure(String msg) {
                                    tv_concern.setText("取消关注");
                                }
                            });
                    isconcern = false;
                }
            }
        });
        tv_blacklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideMsgIputKeyboard();
                HashMap<String, Object> parater = new HashMap<String, Object>();
                parater.put("user_id", HXApp.getInstance()
                                            .getUserInfo().user_id);
                parater.put("black_user_id", viewed_user_id);
                HXJavaNet
                        .post(HXJavaNet.url_pull_black, parater, new AngelNetCallBack() {
                            @Override
                            public void onSuccess(int ret_code, String ret_data, String msg) {
                                if (ret_code == 200) {
                                    Notifier.showShortMsg(ChatActivity.this,
                                            "您已拉黑了" + user_name);
                                    ConcernInfoDao concernInfoDao = new ConcernInfoDao(ChatActivity.this);
                                    UnFollowConcernInfoDao unconcernInfoDao = new UnFollowConcernInfoDao(ChatActivity.this);
                                    ConcernInfoDB concernInfoByUId = null;
                                    try {
                                        concernInfoByUId = concernInfoDao
                                                .getConcernInfoByUId(
                                                        viewed_user_id + "");
                                        UnFollowConcernInfoDB unFollowConcernInfoByUId = unconcernInfoDao
                                                .getUnFollowConcernInfoByUId(
                                                        viewed_user_id + "");
                                        if (null != concernInfoByUId) {
                                            concernInfoDao
                                                    .deleteConcernInfoByUId(
                                                            viewed_user_id +
                                                                    "");
                                        }
                                        if (null != unFollowConcernInfoByUId) {
                                            unconcernInfoDao
                                                    .deleteUnFollowConcernInfoByUId(
                                                            viewed_user_id +
                                                                    "");
                                        }
                                        ChatActivity.this.finish();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if (pw != null && pw.isShowing()) {
                                        pw.dismiss();
                                    }
                                }
                            }


                            @Override
                            public void onFailure(String msg) {
                                Notifier.showShortMsg(ChatActivity.this, "操作失败");
                            }
                        });
                Notifier.showShortMsg(ChatActivity.this, "拉黑");
            }
        });
        tv_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notifier.showShortMsg(ChatActivity.this, "您已举报了" + user_name);
                pw.dismiss();
            }
        });
        tv_dimiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pw != null && pw.isShowing()) {
                    pw.dismiss();
                    WindowManager.LayoutParams params = ChatActivity.this
                            .getWindow().getAttributes();
                    params.alpha = 1f;
                    ChatActivity.this.getWindow().setAttributes(params);
                }
            }
        });
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        view.measure(0, 0);
        pw = new PopupWindow(view, width, view.getMeasuredHeight());

        // 点击外部可以被关闭
        //		pw.setOutsideTouchable(true);

        pw.setBackgroundDrawable(new BitmapDrawable());
        backgroundAlpha(0.4f);
        pw.setFocusable(true);// 设置PopupWindow允许获得焦点, 默认情况下popupWindow中的控件不可以获得焦点, 例外: Button, ImageButton..
        pw.showAtLocation(v, Gravity.BOTTOM, 0, 0);
        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1);
            }
        });
    }


    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }


    @Override
    public void finish() {
        super.finish();
        Util.hideInputBoard(mEditIn, getApplication());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        if (msgListener != null) {
            TIMManager.getInstance().removeMessageListener(msgListener);
        }
    }
}
