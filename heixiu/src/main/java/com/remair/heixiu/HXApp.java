package com.remair.heixiu;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;
import com.apkfuns.logutils.LogConfig;
import com.apkfuns.logutils.LogLevel;
import com.apkfuns.logutils.LogUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.remair.heixiu.activity.AvActivity;
import com.remair.heixiu.bean.ConfigBean;
import com.remair.heixiu.bean.UserInfo;
import com.remair.heixiu.controllers.QavsdkControl;
import com.remair.heixiu.log.OkHttpRequestParse;
import com.remair.heixiu.log.OkHttpResponseParse;
import com.remair.heixiu.net.HXHttpUtil;
import com.remair.heixiu.net.HttpSubscriber;
import com.remair.heixiu.sqlite.ConcernInfoDB;
import com.remair.heixiu.sqlite.ConcernInfoDao;
import com.remair.heixiu.sqlite.MessageInfoDB;
import com.remair.heixiu.sqlite.MessageInfoDao;
import com.remair.heixiu.sqlite.UnFollowConcernInfoDB;
import com.remair.heixiu.sqlite.UnFollowConcernInfoDao;
import com.remair.heixiu.utils.AppManagerUtil;
import com.remair.heixiu.utils.GetMessageUtil;
import com.remair.heixiu.utils.SharedPreferenceUtil;
import com.remair.heixiu.utils.Utils;
import com.remair.heixiu.view.ShowDialog;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.TIMCallBack;
import com.tencent.TIMConnListener;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMCustomElem;
import com.tencent.TIMElem;
import com.tencent.TIMElemType;
import com.tencent.TIMImageElem;
import com.tencent.TIMLogLevel;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageListener;
import com.tencent.TIMOfflinePushListener;
import com.tencent.TIMOfflinePushNotification;
import com.tencent.TIMSoundElem;
import com.tencent.TIMUserStatusListener;
import com.tencent.android.tpush.XGNotifaction;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.XGPushNotifactionCallback;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.qalsdk.sdk.MsfSdkUtils;
import com.umeng.analytics.MobclickAgent;
import info.liujun.image.FrescoConfig;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;
import studio.archangel.toolkitv2.util.Logger;
import studio.archangel.toolkitv2.util.Util;
import studio.archangel.toolkitv2.views.AngelActionBar;
import studio.archangel.toolkitv2.views.AngelCheckBox;
import studio.archangel.toolkitv2.views.AngelDivider;
import studio.archangel.toolkitv2.views.AngelRadioButton;

/**
 * Created by Michael
 * application
 */
public class HXApp extends Application {
    public static final String BUCKET_NAME = "heixiu";
    private static final String TAG = "HXApp";
    public static String customcontent;
    public static final int result_ok = Activity.RESULT_OK;
    public static final int result_fail = 1001;
    public static final int result_special = 1002;
    public static final int result_cancel = Activity.RESULT_CANCELED;

    private volatile UserInfo mUserInfo;
    private QavsdkControl mQavsdkControl = null;
    private String roomName = "";
    private String roomCoverPath = "";
    public int animationType;//七夕特效
    public int wining = 500;//中奖倍数500倍弹幕
    public int swith_domain = 0;//切换域名
    public boolean loading_info = false;
    private Stack<Activity> activityStack;
    private MessageInfoDB chatphotoEntity;//图片
    public boolean isStartContext = false;
    public long unreadCount = 0;
    public static int userid;
    public static String user_name;
    public static String user_avatar;
    public static int relation;
    public static boolean isTest = false;//正式服true测试服false
    private static HXApp sInstance;
    public int recharge_appid;
    public ConfigBean configMessage = new ConfigBean();//config配置信息
    public boolean ListOrGrid = false;


    public static HXApp getInstance() {
        return sInstance;
    }


    public HXApp() {sInstance = this;}


    public ShowDialog DialognewPerCard;


    @Override
    public void onCreate() {
        super.onCreate();
        isTest = !BuildConfig.FLAVOR.equals("offline");
        LeakCanary.install(this);
        LogConfig logConfig = LogUtils.getLogConfig();
        if (BuildConfig.LOG_DEBUG) {
            logConfig.configAllowLog(true);
            Logger.setEnable(true);
            //在debug模式的包中开启严格模式，检查代码规范和内存泄露。从logcat中过滤StrictMode的日志可以查看
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll()
                                                                    .penaltyLog()
                                                                    .build());
            MobclickAgent.setDebugMode(true);
            TIMManager.getInstance().setLogPrintEanble(true);
            TIMManager.getInstance().setLogLevel(TIMLogLevel.DEBUG);
        } else {
            logConfig.configAllowLog(false);
            Logger.setEnable(false);
            TIMManager.getInstance().setLogPrintEanble(false);
        }
        logConfig.configTagPrefix(TAG);
        logConfig.configFormatTag("%d{HH:mm:ss:SSS} %t %c{-5}");
        logConfig.configShowBorders(true);
        logConfig.configLevel(LogLevel.TYPE_VERBOSE);
        //noinspection unchecked
        logConfig
                .addParserClass(OkHttpResponseParse.class, OkHttpRequestParse.class);

        //初始化bugly
        CrashReport.initCrashReport(this, String
                .valueOf(DemoConstants.APPID), BuildConfig.LOG_DEBUG);
        lifecycle();

        if (!isMainProcess()) {
            //新启动一个进程会运行Application的onCreate方法
            //所以在这里判断调用这个方法的进程是不是主进程，如果不是主进程就return，防止下面的代码执行两次
            return;
        }

        mQavsdkControl = new QavsdkControl(HXApp.getInstance());
        MobclickAgent
                .setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setCatchUncaughtExceptions(true);
        activityStack = new Stack<>();

        XGPushManager.setNotifactionCallback(new XGPushNotifactionCallback() {
            @Override
            public void handleNotify(XGNotifaction xGNotifaction) {
                // 获取标签、内容、自定义内容
                String title = xGNotifaction.getTitle();
                String content = xGNotifaction.getContent();
                String customContent = xGNotifaction.getCustomContent();
                // 其它的处理
                // 如果还要弹出通知，可直接调用以下代码或自己创建Notifaction，否则，本通知将不会弹出在通知栏中。
                if (lifecycle) {
                    xGNotifaction.doNotify();
                } else {
                    if (getActivity(AvActivity.class) != null) {
                        EventBus.getDefault()
                                .post(content, EventConstants.UPDATE_XGPUSHMANAGER);
                    }
                }
            }
        });

        initFresco();

        Util.setApplicationContext(this);

        setColorTheme();

        if (MsfSdkUtils.isMainProcess(this)) {
            Log.d("MyApplication", "main process");
            TIMManager.getInstance()
                      .setOfflinePushListener(new TIMOfflinePushListener() {
                          @Override
                          public void handleNotification(TIMOfflinePushNotification notification) {
                              Log.d("MyApplication", "recv offline push");
                              Logger.out(
                                      "offlinemsg:" + notification.getTitle() +
                                              ":" + notification.getContent());
                              notification
                                      .doNotify(HXApp.this, R.drawable.icon_laucher);
                          }
                      });
        }
        TIMManager.getInstance().addMessageListener(new TIMMessageListener() {
            @Override
            public boolean onNewMessages(final List<TIMMessage> list) {
                for (int i = 0; i < list.size(); i++) {
                    for (int j = 0; j < list.get(i).getElementCount(); j++) {
                        TIMElem elem = list.get(i).getElement(j);
                        if (list.get(i).getConversation().getType() ==
                                TIMConversationType.Group) {
                            continue;
                        }
                        if (list.get(i).getConversation().getType() ==
                                TIMConversationType.System) {
                            continue;
                        }
                        //获取当前元素的类型
                        TIMElemType elemType = elem.getType();
                        if (elemType == TIMElemType.Text) {
                            continue;
                        }
                        if (elemType == TIMElemType.Custom) {//自定义消息
                            try {
                                String text = new String(((TIMCustomElem) elem)
                                        .getData(), "UTF-8");
                                JSONObject attr = new JSONObject(text);
                                if (attr.optString("text").contains("点亮了")) {
                                    continue;
                                }
                                Iterator itt = attr.keys();
                                while (itt.hasNext()) {
                                    String keyname = itt.next().toString();
                                    if (keyname.equals("text")) {
                                        SharedPreferenceUtil
                                                .setContext(HXApp.this);
                                        String userid = list.get(i).getSender();
                                        TIMConversation conversation = list
                                                .get(i).getConversation();
                                        long unreadnum = conversation
                                                .getUnreadMessageNum();
                                        SharedPreferenceUtil
                                                .putLong(userid, unreadnum);
                                        String uid = attr.optJSONObject("user")
                                                         .getString("user_id");
                                        String tex = attr.optString("text");
                                        MessageInfoDB chatEntity = new MessageInfoDB();
                                        chatEntity.setType(1);
                                        chatEntity.setIssend(1);
                                        chatEntity.setUserid(userid);
                                        chatEntity.setSendstatue(list.get(i)
                                                                     .status());
                                        chatEntity.setMessagetype("text");
                                        chatEntity.setUuid(list.get(i)
                                                               .getMsgId());
                                        chatEntity.setMessage(tex);
                                        chatEntity.setCreatetime(list.get(i)
                                                                     .timestamp());
                                        chatEntity.setTime(0);
                                        //                                        chatEntity.setTimMessage(list.get(i));

                                        //保存数据库
                                        MessageInfoDao mesageInfo = new MessageInfoDao(HXApp.this);
                                        mesageInfo.addorupdate(chatEntity, list
                                                .get(i).getMsgId());

                                        ConcernInfoDao concernInfoDao = new ConcernInfoDao(HXApp.this);
                                        UnFollowConcernInfoDao unconcernInfoDao = new UnFollowConcernInfoDao(HXApp.this);
                                        try {
                                            ConcernInfoDB concernInfoByUId = concernInfoDao
                                                    .getConcernInfoByUId(
                                                            userid + "");
                                            UnFollowConcernInfoDB unFollowConcernInfoByUId = unconcernInfoDao
                                                    .getUnFollowConcernInfoByUId(
                                                            userid + "");
                                            if (null != concernInfoByUId) {
                                                GetMessageUtil
                                                        .savaDatatwo(1, tex, HXApp.this,
                                                                userid + "");
                                            }
                                            if (null !=
                                                    unFollowConcernInfoByUId) {
                                                GetMessageUtil
                                                        .savaDatatwo(0, tex, HXApp.this,
                                                                userid + "");
                                            }
                                            if (null == concernInfoByUId &&
                                                    null ==
                                                            unFollowConcernInfoByUId) {
                                                GetMessageUtil.showPerInfoCard(
                                                        HXApp.getInstance()
                                                             .getUserInfo().user_id +
                                                                "", userid, tex, HXApp.this, list
                                                                .get(i));
                                            }
                                        } catch (Exception e2) {
                                            e2.printStackTrace();
                                        }
                                        if (unreadnum > 0) {
                                            getMessage(list.get(i));
                                        }
                                    } else if (keyname.equals("command")) {
                                        SharedPreferenceUtil
                                                .setContext(HXApp.this);
                                        String userid = list.get(i).getSender();
                                        TIMConversation conversation = list
                                                .get(i).getConversation();
                                        long unreadnum = conversation
                                                .getUnreadMessageNum();
                                        SharedPreferenceUtil
                                                .putLong("1", unreadnum);
                                        JSONObject jsonObject = attr
                                                .optJSONObject("command");
                                        String name = jsonObject
                                                .optString("name");
                                        if (name.equalsIgnoreCase("sys_text_normal")) {
                                            String textMessage = jsonObject
                                                    .optString("text");
                                            MessageInfoDB chatEntity = new MessageInfoDB();
                                            chatEntity.setType(1);
                                            chatEntity.setIssend(1);
                                            chatEntity.setUserid("1");
                                            chatEntity.setSendstatue(list.get(i)
                                                                         .status());
                                            chatEntity.setMessagetype("text");
                                            chatEntity.setUuid(list.get(i)
                                                                   .getMsgId());
                                            chatEntity.setMessage(textMessage);
                                            chatEntity.setCreatetime(list.get(i)
                                                                         .timestamp());
                                            chatEntity.setTime(0);
                                            //                                        chatEntity.setTimMessage(list.get(i));
                                            //保存数据库
                                            MessageInfoDao mesageInfo = new MessageInfoDao(HXApp.this);
                                            mesageInfo
                                                    .addorupdate(chatEntity, list
                                                            .get(i).getMsgId());

                                            ConcernInfoDao concernInfoDao = new ConcernInfoDao(HXApp.this);
                                            ConcernInfoDB concernInfoDB = new ConcernInfoDB();
                                            JSONObject user = new JSONObject();
                                            user.put("user_id", "1");
                                            user.put("user_name", "嘿秀小助手");
                                            concernInfoDB.setUser_id("1");
                                            concernInfoDB.setUserinfo(user
                                                    .toString());
                                            concernInfoDB
                                                    .setLastmessage(textMessage);
                                            concernInfoDB.setHxtype("0");
                                            concernInfoDB.setUnread("");
                                            concernInfoDB.setRelation(1);
                                            concernInfoDB
                                                    .setUpdatetime(list.get(i)
                                                                       .timestamp());
                                            concernInfoDao
                                                    .addorupdate(concernInfoDB, "1");
                                            if (unreadnum > 0) {
                                                getMessage(list.get(i));
                                            }
                                        } else if (name
                                                .equalsIgnoreCase("gift")) {
                                            SharedPreferenceUtil
                                                    .setContext(HXApp.this);
                                            String user_id = list.get(i)
                                                                 .getSender();
                                            TIMConversation conversationG = list
                                                    .get(i).getConversation();
                                            long unreadnumG = conversationG
                                                    .getUnreadMessageNum();
                                            SharedPreferenceUtil
                                                    .putLong(userid, unreadnumG);
                                            MessageInfoDB chatEntity = new MessageInfoDB();
                                            JSONObject attrCommand = attr
                                                    .optJSONObject("command");
                                            chatEntity.setType(1);
                                            if (list.get(i).isSelf()) {
                                                chatEntity.setIssend(0);
                                            } else {
                                                chatEntity.setIssend(1);
                                            }
                                            chatEntity.setUserid(user_id + "");
                                            chatEntity.setSendstatue(list.get(i)
                                                                         .status());
                                            chatEntity.setMessagetype("gift");
                                            chatEntity.setUuid(list.get(i)
                                                                   .getMsgId());
                                            chatEntity.setMessage(attrCommand
                                                    .toString());
                                            chatEntity.setCreatetime(list.get(i)
                                                                         .timestamp());
                                            chatEntity.setTime(0);

                                            //保存数据库
                                            MessageInfoDao mesageInfo = new MessageInfoDao(HXApp.this);
                                            mesageInfo
                                                    .addorupdate(chatEntity, list
                                                            .get(i).getMsgId());

                                            ConcernInfoDao concernInfoDao = new ConcernInfoDao(HXApp.this);
                                            UnFollowConcernInfoDao unconcernInfoDao = new UnFollowConcernInfoDao(HXApp.this);
                                            ConcernInfoDB concernInfoByUId = concernInfoDao
                                                    .getConcernInfoByUId(
                                                            user_id + "");
                                            UnFollowConcernInfoDB unFollowConcernInfoByUId = unconcernInfoDao
                                                    .getUnFollowConcernInfoByUId(
                                                            user_id + "");
                                            if (null != concernInfoByUId) {
                                                GetMessageUtil
                                                        .savaDatatwo(1, "[礼物]", HXApp.this,
                                                                user_id + "");
                                            }
                                            if (null !=
                                                    unFollowConcernInfoByUId) {
                                                GetMessageUtil
                                                        .savaDatatwo(0, "[礼物]", HXApp.this,
                                                                user_id + "");
                                            }
                                            if (null == concernInfoByUId &&
                                                    null ==
                                                            unFollowConcernInfoByUId) {
                                                GetMessageUtil.showPerInfoCard(
                                                        HXApp.getInstance()
                                                             .getUserInfo().user_id +
                                                                "", userid, "[礼物]", HXApp.this, list
                                                                .get(i));
                                            }
                                            if (unreadnum > 0) {
                                                getMessage(list.get(i));
                                            }
                                        } else if (name
                                                .equalsIgnoreCase("sys_text_certificate")) {
                                            String textMessage = jsonObject
                                                    .optString("text");
                                            String textstatus = jsonObject
                                                    .optString("status");
                                            if (textstatus.equals("0")) {

                                            } else if (textstatus.equals("1")) {
                                                DialognewPerCard = new ShowDialog(HXApp
                                                        .getInstance(), textMessage, "查看信息", "立即提现");
                                                DialognewPerCard
                                                        .setClicklistener(new ShowDialog.ClickListenerInterface() {
                                                            @Override
                                                            public void doConfirm() {
                                                            }


                                                            @Override
                                                            public void doCancel() {
                                                            }
                                                        });
                                            } else if (textstatus.equals("2")) {
                                                DialognewPerCard = new ShowDialog(HXApp
                                                        .getInstance(), textMessage, "查看原因", "");
                                                DialognewPerCard
                                                        .setClicklistener(new ShowDialog.ClickListenerInterface() {
                                                            @Override
                                                            public void doConfirm() {
                                                            }


                                                            @Override
                                                            public void doCancel() {
                                                            }
                                                        });
                                            } else if (textstatus.equals("3")) {

                                            }
                                            //                                            getMessage();
                                        } else if (name
                                                .equalsIgnoreCase("sys_text_config")) {//config接口发生变化，重新请求接口
                                            String textMessage = jsonObject
                                                    .optString("text");
                                            String textstatus = jsonObject
                                                    .optString("status");
                                            String app_version_name = BuildConfig.VERSION_NAME;

                                            HXHttpUtil.getInstance()
                                                      .config(app_version_name, 0)
                                                      .subscribe(new HttpSubscriber<ConfigBean>() {
                                                          @Override
                                                          public void onNext(ConfigBean configBean) {
                                                              configMessage = configBean;
                                                          }


                                                          @Override
                                                          public void onError(Throwable e) {
                                                              super.onError(e);
                                                          }
                                                      });
                                        } else if (name
                                                .equalsIgnoreCase("sys_forbid")) {
                                            MessageInfoDB chatEntity = new MessageInfoDB();
                                            chatEntity.setType(1);
                                            chatEntity.setIssend(1);
                                            chatEntity.setUserid("1.1");
                                            chatEntity.setSendstatue(list.get(i)
                                                                         .status());
                                            chatEntity
                                                    .setMessagetype("systemforbit");
                                            chatEntity.setUuid(list.get(i)
                                                                   .getMsgId());
                                            chatEntity.setMessage(jsonObject
                                                    .toString());
                                            chatEntity.setCreatetime(list.get(i)
                                                                         .timestamp());
                                            chatEntity.setTime(0);
                                            //                                        chatEntity.setTimMessage(list.get(i));
                                            //保存数据库
                                            MessageInfoDao mesageInfo = new MessageInfoDao(HXApp.this);
                                            mesageInfo
                                                    .addorupdate(chatEntity, list
                                                            .get(i).getMsgId());

                                            ConcernInfoDao concernInfoDao = new ConcernInfoDao(HXApp.this);
                                            ConcernInfoDB concernInfoDB = new ConcernInfoDB();
                                            JSONObject user = new JSONObject();
                                            user.put("user_id", "1.1");
                                            user.put("user_name", "系统消息");
                                            concernInfoDB.setUser_id("1.1");
                                            concernInfoDB.setUserinfo(user
                                                    .toString());
                                            concernInfoDB
                                                    .setLastmessage(jsonObject
                                                            .toString());
                                            concernInfoDB.setHxtype("0");
                                            concernInfoDB.setUnread("");
                                            concernInfoDB.setRelation(1);
                                            concernInfoDB
                                                    .setUpdatetime(list.get(i)
                                                                       .timestamp());
                                            concernInfoDao
                                                    .addorupdate(concernInfoDB, "1.1");
                                            if (unreadnum > 0) {
                                                getMessage(list.get(i));
                                            }
                                        }
                                    }
                                    //                                    getMessage(list.get(i));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (elemType == TIMElemType.Image) {
                            if (null == chatphotoEntity) {
                                chatphotoEntity = new MessageInfoDB();
                            }
                            TIMImageElem e = (TIMImageElem) elem;
                            String userid = list.get(i).getSender();
                            TIMConversation conversation = list.get(i)
                                                               .getConversation();
                            long unreadnum = conversation.getUnreadMessageNum();
                            SharedPreferenceUtil.putLong(userid, unreadnum);

                            //                            for(TIMImage image : e.getImageList()) {
                            chatphotoEntity.setType(1);
                            chatphotoEntity.setIssend(1);
                            chatphotoEntity.setUserid(userid + "");
                            chatphotoEntity.setSendstatue(list.get(i).status());
                            chatphotoEntity.setMessagetype("photo");
                            chatphotoEntity.setUuid(list.get(i).getMsgId());
                            chatphotoEntity.setMessage(e.getImageList().get(1)
                                                        .getUrl());
                            chatphotoEntity
                                    .setCreatetime(list.get(i).timestamp());
                            chatphotoEntity.setTime(0);
                            //保存数据库
                            MessageInfoDao mesageInfo = new MessageInfoDao(HXApp.this);
                            mesageInfo.addorupdate(chatphotoEntity, list.get(i)
                                                                        .getMsgId());

                            ConcernInfoDao concernInfoDao = new ConcernInfoDao(HXApp.this);
                            UnFollowConcernInfoDao unconcernInfoDao = new UnFollowConcernInfoDao(HXApp.this);
                            try {
                                ConcernInfoDB concernInfoByUId = concernInfoDao
                                        .getConcernInfoByUId(userid + "");
                                UnFollowConcernInfoDB unFollowConcernInfoByUId = unconcernInfoDao
                                        .getUnFollowConcernInfoByUId(
                                                userid + "");
                                if (null != concernInfoByUId) {
                                    GetMessageUtil
                                            .savaDatatwo(1, "[图片]", HXApp.this,
                                                    userid + "");
                                }
                                if (null != unFollowConcernInfoByUId) {
                                    GetMessageUtil
                                            .savaDatatwo(0, "[图片]", HXApp.this,
                                                    userid + "");
                                }
                                if (null == concernInfoByUId &&
                                        null == unFollowConcernInfoByUId) {
                                    GetMessageUtil.showPerInfoCard(
                                            HXApp.getInstance()
                                                 .getUserInfo().user_id +
                                                    "", userid, "[图片]", HXApp.this, list
                                                    .get(i));
                                }
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                            if (unreadnum > 0) {
                                getMessage(list.get(i));
                            }
                        } else if (elemType == TIMElemType.Sound) {
                            final int flag = i;
                            chatphotoEntity = new MessageInfoDB();
                            final TIMSoundElem timSoundElem = (TIMSoundElem) elem;
                            final String userid = list.get(i).getSender();
                            TIMConversation conversation = list.get(i)
                                                               .getConversation();
                            long unreadnum = conversation.getUnreadMessageNum();
                            try {
                                File file = new File("record_evd.mp3");
                                if (file.exists()) {
                                    Log.d(TAG, "file exist");
                                    file.delete();
                                }
                                final File mPttFile = File
                                        .createTempFile("record_evd", ".mp3");
                                timSoundElem.getSoundToFile(mPttFile
                                        .getAbsolutePath(), new TIMCallBack() {
                                    @Override
                                    public void onError(int i, String s) {

                                    }


                                    @Override
                                    public void onSuccess() {
                                        chatphotoEntity.setType(1);
                                        chatphotoEntity.setIssend(1);
                                        chatphotoEntity.setUserid(userid + "");
                                        chatphotoEntity
                                                .setSendstatue(list.get(flag)
                                                                   .status());
                                        chatphotoEntity.setMessagetype("sound");
                                        chatphotoEntity.setUuid(list.get(flag)
                                                                    .getMsgId());
                                        chatphotoEntity.setMessage(mPttFile
                                                .getAbsolutePath());
                                        chatphotoEntity
                                                .setCreatetime(list.get(flag)
                                                                   .timestamp());
                                        chatphotoEntity.setTime(timSoundElem
                                                .getDuration());
                                        //保存数据库
                                        MessageInfoDao mesageInfo = new MessageInfoDao(HXApp.this);
                                        mesageInfo
                                                .addorupdate(chatphotoEntity, list
                                                        .get(flag).getMsgId());
                                        ConcernInfoDao concernInfoDao = new ConcernInfoDao(HXApp.this);
                                        UnFollowConcernInfoDao unconcernInfoDao = new UnFollowConcernInfoDao(HXApp.this);
                                        try {
                                            ConcernInfoDB concernInfoByUId = concernInfoDao
                                                    .getConcernInfoByUId(
                                                            userid + "");
                                            UnFollowConcernInfoDB unFollowConcernInfoByUId = unconcernInfoDao
                                                    .getUnFollowConcernInfoByUId(
                                                            userid + "");
                                            if (null != concernInfoByUId) {
                                                GetMessageUtil
                                                        .savaDatatwo(1, "[语音]", HXApp.this,
                                                                userid + "");
                                            }
                                            if (null !=
                                                    unFollowConcernInfoByUId) {
                                                GetMessageUtil
                                                        .savaDatatwo(0, "[语音]", HXApp.this,
                                                                userid + "");
                                            }
                                            if (null == concernInfoByUId &&
                                                    null ==
                                                            unFollowConcernInfoByUId) {
                                                GetMessageUtil.showPerInfoCard(
                                                        HXApp.getInstance()
                                                             .getUserInfo().user_id +
                                                                "", userid, "[语音]", HXApp.this, list
                                                                .get(flag));
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (unreadnum > 0) {
                                getMessage(list.get(i));
                            }
                        }
                    }
                }

                return false;
            }
        });
        TIMManager.getInstance().setConnectionListener(new TIMConnListener() {

            @Override
            public void onConnected() {

            }


            @Override
            public void onDisconnected(int i, String s) {

            }


            @Override
            public void onWifiNeedAuth(String s) {

            }
        });

        TIMManager.getInstance()
                  .setUserStatusListener(new TIMUserStatusListener() {
                      @Override
                      public void onForceOffline() {

                      }
                  });
    }


    private boolean isMainProcess() {
        String curProcessName = AppManagerUtil
                .getCurProcessName(getApplicationContext());
        String packageName = getPackageName();
        Logger.out(String
                .format("PackageName :%s  --   curProcessName :%s", packageName, curProcessName));
        if (packageName.equals(curProcessName)) {
            return true;
        }
        return false;
    }


    private void initFresco() {
        ImagePipelineConfig.Builder configBuilder = FrescoConfig
                .getConfigBuilder(this, getCacheDir(), HXJavaNet
                        .getOkhttpClient());
        configBuilder
                .setMemoryTrimmableRegistry(new MyMemoryTrimmableRegistry());
        Fresco.initialize(this, configBuilder.build());
    }


    private void getMessage(TIMMessage timMessage) {
        if (null != timMessage) {
            unreadCount++;
            SharedPreferenceUtil.setContext(HXApp.this);
            SharedPreferenceUtil.putLong("oflinemessage", unreadCount);
            if (unreadCount > 0) {
                Intent intent = new Intent();
                intent.setAction("heixiu.offlinemessage");
                intent.putExtra("oflinemessage", unreadCount);
                sendBroadcast(intent);
            }
        }
    }


    /**
     * add Activity 添加Activity到栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }


    /**
     * 获取activity
     */
    public Activity getActivity(Class cls) {
        if (activityStack != null && !activityStack.isEmpty()) {
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    return activity;
                }
            }
        }
        return null;
    }


    /**
     * 结束当前Activity（栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }


    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }


    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }


    public boolean isInTask(Class cls) {
        if (activityStack != null && !activityStack.isEmpty()) {
            for (Activity a : activityStack) {
                return a.getClass().isInstance(cls);
                //if (a instanceof AvActivity) {
                //    return true;
                //}
            }
        }

        return false;
    }


    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            finishAllActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Nullable
    public UserInfo getUserInfo() {
        if (mUserInfo == null) {
            synchronized (this) {
                if (mUserInfo == null) {
                    String string = getSharedPreferences(DemoConstants.LOCAL_DATA, Context.MODE_PRIVATE)
                            .getString(DemoConstants.LOACL_USERINFO, "");
                    if (!TextUtils.isEmpty(string)) {
                        mUserInfo = Utils.jsonToBean(string, UserInfo.class);
                    }
                }
            }
        }
        return mUserInfo;
    }


    public void setUserInfo(UserInfo mSelfUserInfo) {
        SharedPreferences sp = getSharedPreferences(DemoConstants.LOCAL_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (mSelfUserInfo == null) {
            editor.putString(DemoConstants.LOACL_USERINFO, "");
            mUserInfo = null;
        } else {
            if (mUserInfo != null) {
                if (!TextUtils.isEmpty(mUserInfo.tlsSig)) {
                    mSelfUserInfo.tlsSig = mUserInfo.tlsSig;
                }
                if (!TextUtils.isEmpty(mUserInfo.token)) {
                    mSelfUserInfo.token = mUserInfo.token;
                }
            }
            this.mUserInfo = mSelfUserInfo;
            editor.putString(DemoConstants.LOACL_USERINFO, Utils
                    .beanTojson(mUserInfo));
        }
        editor.commit();
    }


    public void onLowMemory() {
        super.onLowMemory();
        Logger.out("WL_DEBUG onLowMemory");
    }


    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Logger.out("WL_DEBUG onTrimMemory" + level);
        if (!isMainProcess()) {
            //新启动一个进程会运行Application的onCreate方法
            //所以在这里判断调用这个方法的进程是不是主进程，如果不是主进程就return，防止下面的代码执行两次
            return;
        }
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.clearMemoryCaches();
    }


    public QavsdkControl getQavsdkControl() {
        return mQavsdkControl;
    }


    public String getRoomName() {
        return roomName;
    }


    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }


    public String getRoomCoverPath() {
        return roomCoverPath;
    }


    public void setRoomCoverPath(String roomCoverPath) {
        this.roomCoverPath = roomCoverPath;
    }


    void setColorTheme() {
        AngelActionBar
                .setDefaultBackgroundResource(R.drawable.shape_main_background);
        AngelActionBar.setDefaultForegroundColor(R.color.text_black);
        AngelActionBar.setDefaultLeftText("");
        AngelActionBar.setDefaultArrowDrawable(R.drawable.white_arrow);
        AngelActionBar.setDefaultArrowColor(R.color.text_white);
        AngelCheckBox.setDefaultColorMain(R.color.hx_main);
        AngelActionBar.setDefaultTextColor(R.color.white);
        AngelRadioButton.setDefaultColorMain(R.color.hx_main);
        AngelDivider.setDefaultColorResource(R.color.divider_normal);
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    public void setAccount(String account) {
        getPreference().edit().putString("account", account).apply();
    }


    public String getAccount() {
        return getPreference().getString("account", "");
    }


    public SharedPreferences getPreference() {
        return getSharedPreferences(TAG, MODE_PRIVATE);
    }


    /**
     * 退出登录
     *
     * @param showMsg 是否显示登录失效的提示信息
     * @param isOnForceOffline 是否im顶下线
     */
    public void goLogin(boolean showMsg, boolean isOnForceOffline) {
        if (getUserInfo() == null) {
            return;
        }
        setUserInfo(null);
        //退出AV那边群
        if ((mQavsdkControl != null) &&
                (mQavsdkControl.getAVContext() != null) &&
                (mQavsdkControl.getAVContext().getAudioCtrl() != null)) {
            mQavsdkControl.getAVContext().getAudioCtrl().stopTRAEService();
            mQavsdkControl.exitRoom();
        }
        TIMManager.getInstance().logout(new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                LogUtils.tag("logout").e("onError code: " + i + " msg: " + s);
            }


            @Override
            public void onSuccess() {
                LogUtils.tag("logout").e("退出成功");
            }
        });
        //关闭所有activity
        EventBus.getDefault()
                .post(EventConstants.ACTIVITY_LOGOUT, EventConstants.ACTIVITY_LOGOUT);
        HXApp.getInstance().finishAllActivity();

        final Intent intent = getPackageManager()
                .getLaunchIntentForPackage(getPackageName());
        intent.setFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EventConstants.ISONFORCEOFFLINE, isOnForceOffline);
        intent.putExtra(EventConstants.LOGIN_EXPIRE, showMsg);
        startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }


    int count = 0;
    boolean lifecycle;


    public void lifecycle() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }


            @Override
            public void onActivityStarted(Activity activity) {
                if (count == 0) {
                    lifecycle = false;
                }
                count++;
            }


            @Override
            public void onActivityResumed(Activity activity) {

            }


            @Override
            public void onActivityPaused(Activity activity) {

            }


            @Override
            public void onActivityStopped(Activity activity) {
                count--;
                if (count == 0) {
                    lifecycle = true;
                }
            }


            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }


            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }
}