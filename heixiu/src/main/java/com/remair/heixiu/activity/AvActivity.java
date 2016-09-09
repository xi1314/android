package com.remair.heixiu.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.Process;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import com.badoo.mobile.util.WeakHandler;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.remair.heixiu.BuildConfig;
import com.remair.heixiu.DemoConstants;
import com.remair.heixiu.EventConstants;
import com.remair.heixiu.HXActivityV4;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.HXJavaNet;
import com.remair.heixiu.R;
import com.remair.heixiu.adapters.LiveChatMessageAdapter;
import com.remair.heixiu.adapters.LiveMemberAdapter;
import com.remair.heixiu.bean.Gift;
import com.remair.heixiu.bean.MemberInfo;
import com.remair.heixiu.bean.NewPerMesInfo;
import com.remair.heixiu.bean.UserInfo;
import com.remair.heixiu.capricorn.RotateAndTranslateAnimation;
import com.remair.heixiu.controllers.AVUIControl;
import com.remair.heixiu.controllers.QavsdkControl;
import com.remair.heixiu.giftview.CircleAnimation;
import com.remair.heixiu.giftview.GetGiftThree;
import com.remair.heixiu.giftview.GiftSelector;
import com.remair.heixiu.giftview.GiftSelectorPage;
import com.remair.heixiu.giftview.GiftWrapper;
import com.remair.heixiu.giftview.StrokeTextView;
import com.remair.heixiu.heartlayout.HeartLayout;
import com.remair.heixiu.utils.GetMessageUtil;
import com.remair.heixiu.utils.HXImageLoader;
import com.remair.heixiu.utils.HXUtil;
import com.remair.heixiu.utils.ImageUtils;
import com.remair.heixiu.utils.RSAUtils;
import com.remair.heixiu.utils.RxViewUtil;
import com.remair.heixiu.utils.SharedPreferenceUtil;
import com.remair.heixiu.utils.SoftKeyboardUtil;
import com.remair.heixiu.utils.Utils;
import com.remair.heixiu.utils.Xtgrade;
import com.remair.heixiu.utils.showPLPupwindows;
import com.remair.heixiu.view.AircraftAnimationView;
import com.remair.heixiu.view.AnimationListener;
import com.remair.heixiu.view.BarrageView;
import com.remair.heixiu.view.CarAnimatorView;
import com.remair.heixiu.view.ChatMessageDialog;
import com.remair.heixiu.view.CircleProgressBar;
import com.remair.heixiu.view.ClosureDialog;
import com.remair.heixiu.view.EnterView;
import com.remair.heixiu.view.FlowerAnimationView;
import com.remair.heixiu.view.FlyAnimatorView;
import com.remair.heixiu.view.LiveSettingDialog;
import com.remair.heixiu.view.LiveShareView;
import com.remair.heixiu.view.LoadingDialog;
import com.remair.heixiu.view.MemberLinearLayoutManager;
import com.remair.heixiu.view.PrivateMessageDialog;
import com.remair.heixiu.view.RechargeDialog;
import com.remair.heixiu.view.RedcarAnimatorView;
import com.remair.heixiu.view.ShowDialog;
import com.remair.heixiu.view.UsercpDialog;
import com.remair.heixiu.view.WrapContentLinearLayoutManager;
import com.tencent.TIMCallBack;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMCustomElem;
import com.tencent.TIMElem;
import com.tencent.TIMElemType;
import com.tencent.TIMGroupManager;
import com.tencent.TIMGroupMemberRoleType;
import com.tencent.TIMGroupSystemElem;
import com.tencent.TIMGroupSystemElemType;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageListener;
import com.tencent.TIMTextElem;
import com.tencent.TIMValueCallBack;
import com.tencent.av.TIMAvManager;
import com.tencent.av.opengl.ui.GLRootView;
import com.tencent.av.sdk.AVAudioCtrl;
import com.tencent.av.sdk.AVConstants;
import com.tencent.av.sdk.AVContext;
import com.tencent.av.sdk.AVEndpoint;
import com.tencent.av.sdk.AVError;
import com.tencent.av.sdk.AVRoom;
import com.tencent.av.sdk.AVRoomMulti;
import com.tencent.av.sdk.AVVideoCtrl;
import com.tencent.av.sdk.AVView;
import com.tencent.av.utils.PhoneStatusTools;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import rx.functions.Action1;
import studio.archangel.toolkitv2.activities.PickImageActivity;
import studio.archangel.toolkitv2.adapters.CommonRecyclerAdapter;
import studio.archangel.toolkitv2.interfaces.AngelNetProgressCallBack;
import studio.archangel.toolkitv2.util.Logger;
import studio.archangel.toolkitv2.util.networking.AngelNetCallBack;
import studio.archangel.toolkitv2.util.text.Notifier;
import studio.archangel.toolkitv2.views.AngelMaterialButton;
import studio.archangel.toolkitv2.views.AngelMenuDialog;

/**
 * 直播界面
 */

public class AvActivity extends HXActivityV4 implements OnClickListener {
    public boolean isEnterroominterface;
    //触摸事件的距离   private int getRawY;
    public boolean tanmuboolean = false;
    public static int[] mtextColor = new int[] { Color.BLUE, Color.RED,
            Color.YELLOW, Color.WHITE, Color.MAGENTA, Color.CYAN };
    int heartbeat_interval = 5000;
    //静音
    boolean audio_jingyin;
    @BindView(R.id.fb_cover_bg) SimpleDraweeView mFbCoverBg;
    @BindView(R.id.iv_cover_bg) FrameLayout mIvCoverBg;
    @BindView(R.id.meilizhi_textview) TextView mMeilizhiTextview;
    @BindView(R.id.work_status_qav_topleft) TextView mWorkStatusQavTopleft;
    @BindView(R.id.work_status_qav_topright) TextView mWorkStatusQavTopright;
    @BindView(R.id.ll_usercp1) LinearLayout mLlUsercp1;
    @BindView(R.id.tanmu_container1) RelativeLayout mTanmuContainer1;
    @BindView(R.id.iv_in_recharge) ImageView mIvInRecharge;
    @BindView(R.id.tv_give1) TextView mTvGive1;
    @BindView(R.id.tv_give2) TextView mTvGive2;
    @BindView(R.id.tv_give3) TextView mTvGive3;
    @BindView(R.id.tv_give4) TextView mTvGive4;
    @BindView(R.id.ll_species) LinearLayout mLlSpecies;
    @BindView(R.id.av_activity) RelativeLayout mAvActivity;
    @BindView(R.id.act_live_setting) RelativeLayout act_live_setting;
    //魅力值
    private int charm_value = 0;
    //心跳包
    private Timer mtimer;
    //camera 闪光灯开关判断
    private boolean cameraTrue;
    GestureDetectorCompat gestureDetector;
    private long sytem_datei = 0;
    private static final String TAG = "AvActivity";
    private static final String UNREAD = "0";
    private static final int PRIASE_MSG = 1;
    private static final int MEMBER_ENTER_MSG = 2;
    private static final int MEMBER_EXIT_MSG = 3;
    private static final int VIDEOCHAT_INVITE = 4;
    private static final int YES_I_JOIN = 5;
    private static final int NO_I_REFUSE = 6;
    private static final int MUTEVOICE = 7;
    private static final int UNMUTEVOICE = 8;
    private static final int MUTEVIDEO = 9;
    private static final int UNMUTEVIDEO = 10;
    private static final int CLOSEVIDEOSEND = 11;
    private static final int DIALOG_INIT = 0;
    private static final int DIALOG_AT_ON_CAMERA = DIALOG_INIT + 1;
    private static final int DIALOG_ON_CAMERA_FAILED = DIALOG_AT_ON_CAMERA + 1;
    private static final int DIALOG_AT_OFF_CAMERA = DIALOG_ON_CAMERA_FAILED + 1;
    private static final int DIALOG_OFF_CAMERA_FAILED = DIALOG_AT_OFF_CAMERA +
            1;
    private static final int DIALOG_AT_SWITCH_FRONT_CAMERA =
            DIALOG_OFF_CAMERA_FAILED + 1;
    private static final int DIALOG_SWITCH_FRONT_CAMERA_FAILED =
            DIALOG_AT_SWITCH_FRONT_CAMERA + 1;
    private static final int DIALOG_AT_SWITCH_BACK_CAMERA =
            DIALOG_SWITCH_FRONT_CAMERA_FAILED + 1;
    private static final int DIALOG_SWITCH_BACK_CAMERA_FAILED =
            DIALOG_AT_SWITCH_BACK_CAMERA + 1;
    private static final int DIALOG_DESTROY = DIALOG_SWITCH_BACK_CAMERA_FAILED +
            1;
    public static final int ERROR_MESSAGE_TOO_LONG = 0x1;
    public static final int ERROR_ACCOUNT_NOT_EXIT = ERROR_MESSAGE_TOO_LONG + 1;
    private static final int REFRESH_dialog = 99955;
    private static final int REFRESH_CHAT = 0x100;
    private static final int REFRESH_MESSAGE = 124;
    private static final int UPDAT_WALL_TIME_TIMER_TASK = REFRESH_CHAT + 1;
    private static final int REMOVE_CHAT_ITEM_TIMER_TASK =
            UPDAT_WALL_TIME_TIMER_TASK + 1;
    private static final int UPDAT_MEMBER = REMOVE_CHAT_ITEM_TIMER_TASK + 1;
    private static final int MEMBER_EXIT_COMPLETE = UPDAT_MEMBER + 1;
    private static final int CLOSE_VIDEO = MEMBER_EXIT_COMPLETE + 1;
    private static final int START_RECORD = CLOSE_VIDEO + 1;
    private static final int IM_HOST_LEAVE = START_RECORD + 1;
    private static final int GET_ROOM_INFO = IM_HOST_LEAVE + 1;
    private static final int REFRESH_PRAISE = GET_ROOM_INFO + 1;
    private static final int chatmessage = 250;
    private static final int chatscoll = 251;
    private static final String[] SDKtype = { "普通开发SDK业务", "普通物联网摄像头SDK业务",
            "滨海摄像头SDK业务" };
    private static final int MAX_REQUEST_VIEW_COUNT = 3;//当前最大支持请求画面个数
    private static boolean LEVAE_MODE = false;
    private static boolean hasPullMemberList = false;
    private final int MAX_PAGE_NUM = 6;
    private final int delay = 301;//时间纪录
    private final int delaytwo = 302;//时间纪录
    private final int delayGIF = 303;//GIF
    ArrayList<JSONObject> messages;
    Vector<JSONObject> members;
    ArrayList<JSONObject> messages2, messagesgift;
    Map<String, List<String>> giftMap = new HashMap<>();//存礼物的map
    LiveMemberAdapter adapter_member;
    OrientationEventListener mOrientationEventListener = null;
    int mRotationAngle = 0;
    ArrayList<MemberInfo> mMemberList, mVideoMemberList, mNormalMemberList;
    @BindView(R.id.act_live_message) View b_message;
    @BindView(R.id.hot_list) TextView mHostList;
    @BindView(R.id.headLayout) View mHeadLayout;
    @BindView(R.id.act_live_music) View b_music;
    @BindView(R.id.act_live_music1) View b_music1;
    @BindView(R.id.act_live_music2) View b_music2;
    @BindView(R.id.act_live_music6) View b_music6;
    @BindView(R.id.act_live_music3) View b_music3;
    @BindView(R.id.act_live_music4) View b_music4;
    @BindView(R.id.act_live_music7) View b_music7;
    @BindView(R.id.act_live_music8) View b_music8;
    @BindView(R.id.act_live_private_message) View act_live_private_message;
    @BindView(R.id.act_live_fenxiang) View b_fenxiang;
    @BindView(R.id.act_live_function) View v_function;
    @BindView(R.id.act_live_gift) View b_gift;
    @BindView(R.id.act_live_camera) View b_camera;
    @BindView(R.id.act_live_flash) ImageView b_flash;
    @BindView(R.id.act_live_beautify) ImageView b_beautify;
    @BindView(R.id.act_live_function_button) View b_function;
    @BindView(R.id.act_live_hangup) View amr_hangup;
    @BindView(R.id.act_live_rv_chat) RecyclerView list_chat;
    //禮物的佈局
    @BindView(R.id.act_live_rv_chat2) RecyclerView list_chat2;
    @BindView(R.id.act_live_rv_member) RecyclerView list_member;
    @BindView(R.id.act_live_gift_pad) View v_gift_pad;
    @BindView(R.id.act_live_gift_send) AngelMaterialButton amb_gift_send;
    @BindView(R.id.act_live_gift_account) public TextView tv_account;
    @BindView(R.id.act_live_species_account)
    public TextView tv_species_account;//二币
    @BindView(R.id.act_live_gift_selector) GiftSelector gift_selector;
    @BindView(R.id.view_liv_send_button) FrameLayout view_liv_send_button;
    @BindView(R.id.tv_sendlian) TextView tv_sendlian;
    @BindView(R.id.tv_time) TextView tv_time;
    @BindView(R.id.msg_enter) TextView msg_enter;//进入房间的消息通知
    @BindView(R.id.av_screen_layout) RelativeLayout av_screen_layout;
    @BindView(R.id.act_live_rv_gift) RecyclerView act_live_rv_gift;
    @BindView(R.id.perso_num) TextView perso_num;
    @BindView(R.id.av_video_glview) GLRootView av_video_glview;
    @BindView(R.id.ll_recharge) LinearLayout ll_recharge;
    @BindView(R.id.view_one) RelativeLayout view_one;
    @BindView(R.id.view_two) RelativeLayout view_two;
    @BindView(R.id.view_three) RelativeLayout view_three;
    @BindView(R.id.view_four) RelativeLayout view_four;
    @BindView(R.id.giftbig_fl) SimpleDraweeView giftbig_fl;

    @BindView(R.id.ll_usercp) RelativeLayout ll_usercp;

    @BindView(R.id.relativelayout) RelativeLayout relativelayout;
    //需要移动的布局
    @BindView(R.id.parent_rela) RelativeLayout parent_rela;
    @BindView(R.id.qav_top_bar) RelativeLayout qav_top_bar;
    @BindView(R.id.qav_bottom_bar) FrameLayout qav_bottom_bar;
    @BindView(R.id.zan) HeartLayout zan;
    @BindView(R.id.tv_isconcern) ImageView tv_isconcern;
    @BindView(R.id.frag_progress) RelativeLayout frag_progress;
    @BindView(R.id.circleProgressbar) CircleProgressBar circleProgressbar;
    @BindView(R.id.tv_species) TextView tv_species;
    @BindView(R.id.iv_species_sma) ImageView iv_species_sma;
    @BindView(R.id.tv_lianchengcount) StrokeTextView tv_lianchengcount;
    @BindView(R.id.qav_timer) TextView mClockTextView;
    @BindView(R.id.qav_awards)//中奖动画
            View qav_awards;
    @BindView(R.id.iv_konfetti) ImageView iv_konfetti;
    @BindView(R.id.iv_light) ImageView iv_light;
    @BindView(R.id.iv_bat) ImageView iv_bat;
    @BindView(R.id.tv_times) TextView tv_times;
    @BindView(R.id.fl_background) FrameLayout fl_background;
    @BindView(R.id.ll_give) LinearLayout ll_give;
    @BindView(R.id.ll_give2) LinearLayout ll_give2;
    @BindView(R.id.ll_give3) LinearLayout ll_give3;
    @BindView(R.id.ll_give4) LinearLayout ll_give4;
    @BindView(R.id.tv_count1) TextView tv_count1;
    @BindView(R.id.tv_count2) TextView tv_count2;
    @BindView(R.id.tv_count3) TextView tv_count3;
    @BindView(R.id.tv_count4) TextView tv_count4;
    @BindView(R.id.live_gift) SimpleDraweeView live_gift;
    @BindView(R.id.hint_message) TextView hint_message;
    @BindView(R.id.tv_id) TextView tv_id;
    @BindView(R.id.chat_msg_remind) RelativeLayout chat_msg_remind;
    @BindView(R.id.chat_msg_count) TextView chat_msg_count;
    @BindView(R.id.beauty_progress) SeekBar beautyProgress;
    @BindView(R.id.seekbar_close) ImageView seekBarClose;
    @BindView(R.id.beauty_part) LinearLayout beautyPart;
    //主播提示分享
    @BindView(R.id.iv_tips_creater) ImageView tipsCreater;
    //观众提示分享
    @BindView(R.id.iv_tips_viewer) ImageView tipsViewer;
    @BindView(R.id.ll_global_gift) FrameLayout ll_global_gift;
    @BindView(R.id.host_gift) SimpleDraweeView host_gift;
    @BindView(R.id.send_person) TextView send_person;
    @BindView(R.id.rl_global_gift) LinearLayout rl_global_gift;
    @BindView(R.id.act_live_tmp2) LinearLayout act_live_tmp2;
    @BindView(R.id.msg_enterrome) RelativeLayout msg_enterrome;
    @BindView(R.id.host_head) SimpleDraweeView hostHead;
    @BindView(R.id.share_rl) RelativeLayout share_rl;

    private int isScret = 0;//是否是加密直播
    private String mPwd;
    private int like_count = 0;
    private int perso_num_count = 0;
    private List mlistadministart = new ArrayList();
    boolean func_flash_active = false;
    TIMAvManager.RecordParam mRecordParam;
    TIMAvManager.StreamParam mStreamParam;
    private boolean mIsPaused = false;
    private boolean mIsSuccess = true;
    private boolean mpush = false;
    private boolean mRecord = false;
    private int mOnOffCameraErrorCode = AVError.AV_OK;
    private int mSwitchCameraErrorCode = AVError.AV_OK;
    private ProgressDialog mDialogInit = null;
    private ProgressDialog mDialogAtOnCamera = null;
    private ProgressDialog mDialogAtOffCamera = null;
    private ProgressDialog mDialogAtSwitchFrontCamera = null;
    private ProgressDialog mDialogAtSwitchBackCamera = null;
    private ProgressDialog mDialogAtDestroy = null;
    private String videoRecordId = "";
    private AppCompatEditText mEditTextInputMsg;

    private TIMConversation mConversation;
    private TIMConversation mSystemConversation, testConversation;
    //adapter_chat2 禮物的adapter
    private LiveChatMessageAdapter adapter_chat, adapter_chat2, adapter_gift;
    private int mLoadMsgNum = MAX_PAGE_NUM;
    private boolean bNeverLoadMore = true;
    private boolean bMore = true;
    private boolean mIsLoading = false;
    private QavsdkControl mQavsdkControl;
    private String mRecvIdentifier = "";
    public String mHostIdentifier = "";
    private PowerManager.WakeLock wakeLock;
    private Context ctx;
    private Context mContext;
    private UserInfo mSelfUserInfo;
    private int roomNum;
    private String groupId;
    private int StreamType = 2;
    private int StreamTypeCode = DemoConstants.HLS;
    private HXApp mHXApp;
    private int groupForPush;
    private long second = 0;
    private long time;
    private int mMemberVideoCount = 0;
    private AVView mRequestViewList[] = null;
    private String mRequestIdentifierList[] = null;
    private int stop = 1;
    private long streamChannelID;
    private String channelID = "";
    private String selectIdentier = "";

    private MemberInfo hostMember = new MemberInfo();
    private Boolean OpenVoice = false;
    private Boolean OpenVideo = false;
    private int mMaskViewCount = 0;
    private long startTime = 0;
    private long lonone;
    private int gift_price = 0;
    private long secondtime = 4000;//礼物显示几秒
    private long GIFtime = 5000;//动礼物显示几秒
    private boolean issend = true;//是否发送服务器
    private int isshow = 1;//第一次点赞显示上线
    private int amountTransfer = 0;//连送礼物数量
    private CountDownTimer sendgifttimer;//发送礼物计时
    private JSONObject sendgiftattr;
    private Queue<JSONObject> sendgiftqueue = new LinkedList<JSONObject>();//发礼物队列
    private LinkedList<JSONObject> queue = new LinkedList<JSONObject>();//小于10的队列
    private LinkedList<JSONObject> queuebig = new LinkedList<JSONObject>();//大于10的队列
    private LinkedList<JSONObject> localqueue = new LinkedList<JSONObject>();
    private Queue<String> danmakuQueue = new LinkedList<String>();
    private Queue<Integer> heidouWinQueue = new LinkedList<Integer>();
    private LinkedList<JSONObject> localendterRoom = new LinkedList<JSONObject>();//进入房队列
    private final int sendGift = 304;
    private boolean isback = false;//主播切入后台再进入标示
    private PopupWindow popupWindow;
    private GetGiftThree getGift;
    private String[] strTipsarray = new String[] { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9" };
    private int strTipsarrayi;
    private AVContext avContext;
    private String userid = "";
    private String giftid = "";
    private String sifegiftid = "";
    private long mill;//连发礼物时间
    private boolean isshowlianfa = false;
    private int spitCount = 0;//瓶子吐赞计数
    private String gift_image;//选中礼物的图片
    private NewPerMesInfo messageInfo;
    private int chatmessagecount = 0;
    private long chatmessagec = 0;
    int danmuFlag = 0;//弹幕标记位置0是第一行，1...

    //判断dialog宽高的值
    private HashMap<String, Integer> viewIndex = new HashMap<String, Integer>();
    //正在发起请求的id
    private Gift selected_gift = null;
    private int giftcount = 1;
    //吧百分比去了；
    private int loveGradegift;
    private double loveGradeRatiogift;
    private boolean isroll = false;//聊天区域控制刷新
    private boolean isenterMessage = true;
    private LoadingDialog loadingDialog;
    private BarrageView barrageView;//弹幕
    Thread thread, threadzan, threadgif, threadbig;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                    if (queue.size() > 0) {
                        if (null == getGift) {
                            getGift = new GetGiftThree(mContext, mHandler, mMeilizhiTextview, giftbig_fl, list_chat, list_chat2, act_live_rv_gift, messages2, messages, messagesgift, adapter_chat, adapter_chat2, adapter_gift, giftMap, queuebig);
                        }
                        getGift.addMessageToList1(queue.peek(), queuebig);
                        queue.poll();
                    } else if (queue.size() == 0) {
                        if (queuebig.size() == 0) {
                            giftMap.clear();
                        }
                    }
                }
            });
            mHandler.postDelayed(runnable, 300);
        }
    };
    Runnable runnablebig = new Runnable() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                    if (queuebig.size() > 0) {
                        if (null == getGift) {
                            getGift = new GetGiftThree(mContext, mHandler, mMeilizhiTextview, giftbig_fl, list_chat, list_chat2, act_live_rv_gift, messages2, messages, messagesgift, adapter_chat, adapter_chat2, adapter_gift, giftMap, queuebig);
                        }
                        getGift.addMessageToList1(queuebig.peek(), queuebig);
                        queuebig.poll();
                    } else if (queuebig.size() == 0) {
                        if (queue.size() == 0) {
                            giftMap.clear();
                        }
                    }
                }
            });
            mHandler.postDelayed(runnablebig, 300);
        }
    };
    Runnable runnablegif = new Runnable() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                    if (danmakuQueue.size() > 0) {
                        if (null == barrageView) {
                            barrageView = new BarrageView(getApplication(), danmakuQueue, mTanmuContainer1, view_one, view_two, view_three, view_four);
                            mTanmuContainer1.addView(barrageView);
                        }
                        barrageView.sendTanmu(danmakuQueue.poll());
                    }
                    if (heidouWinQueue.size() > 0) {
                        heidouWining(heidouWinQueue.poll());
                    }

                    long count = SharedPreferenceUtil
                            .getLong("oflinemessage", 0);
                    if (count > 0) {
                        chat_msg_remind.setVisibility(View.VISIBLE);
                        chat_msg_count.setText(count + "");
                    } else {
                        chat_msg_remind.setVisibility(View.GONE);
                    }
                }
            });
            mHandler.postDelayed(runnablegif, 3000);
        }
    };
    Runnable runnablezan = new Runnable() {
        @Override
        public void run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            if (like_count > 0) {

                HashMap<String, Object> pare = new HashMap<String, Object>();
                pare.put("room_num", roomNum);
                pare.put("user_id", mSelfUserInfo.user_id);
                pare.put("praise_num", like_count);
                HXJavaNet
                        .post(HXJavaNet.url_reportPraise, pare, new NetCallBack(AvActivity.this));
            }
            mHandler.postDelayed(runnablezan, 60000 * 5);
        }
    };

    ExecutorService fix = Executors
            .newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private TIMMessageListener msgListener = new TIMMessageListener() {
        @Override
        public boolean onNewMessages(final List<TIMMessage> list) {
            Logger.out("onNewMessagesGet  " + list.size());
            if (isTopActivity()) {
                //解析TIM推送消息
                if (groupId != null) {
                    if (fix != null) {
                        fix.execute(new Runnable() {
                            @Override
                            public void run() {
                                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                                refreshChat2(list);
                            }
                        });
                    }
                }
            }
            return false;
        }
    };

    private WeakHandler mHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case chatmessage://聊天信息区域刷新
                    chatmessagecount++;
                    long thistime = System.currentTimeMillis();
                    if (thistime - chatmessagec > 1000) {
                        if (!isroll) {
                            adapter_chat
                                    .notifyItemRangeInserted(0, chatmessagecount);
                            list_chat.smoothScrollToPosition(0);
                            chatmessagecount = 0;
                            chatmessagec = thistime;
                        } else {

                        }
                    }
                    break;
                case chatscoll://聊天信息
                    isroll = false;
                    break;
                case REFRESH_PRAISE:
                    break;
                case IM_HOST_LEAVE:
                    onCloseVideo(false, true);
                    liveCloseInfo(true);
                    break;

                case ERROR_MESSAGE_TOO_LONG:
                    Toast.makeText(getBaseContext(), "消息太长，发送失败", Toast.LENGTH_SHORT)
                         .show();
                    break;
                case ERROR_ACCOUNT_NOT_EXIT:
                    Toast.makeText(getBaseContext(), "对方账号不存在或未登录过！", Toast.LENGTH_SHORT)
                         .show();
                    break;

                case UPDAT_WALL_TIME_TIMER_TASK:
                    //                    updateWallTime();
                    break;
                case REMOVE_CHAT_ITEM_TIMER_TASK:
                    removeChatItem();
                    break;
                case UPDAT_MEMBER:
                    updateMemberView();
                    //                    mChatMsgListAdapter.refresh(hostMember);
                    break;
                //                case REFRESH_HOST_INFO
                //                    break;
                case MEMBER_EXIT_COMPLETE://退出房间
                    sendCloseMsg();
                    break;
                case CLOSE_VIDEO:
                    onCloseVideo(false, false);
                    break;
                case START_RECORD:
                    startRecord();
                    break;
                case GET_ROOM_INFO:
                    getMemberInfo(mSelfUserInfo.user_id, roomNum, 0);
                    break;
                case REFRESH_CHAT://点赞
                    onSendPraise(false);
                    break;
                case REFRESH_MESSAGE://文本消息
                    addMessageToListtwo((TIMMessage) msg.obj);
                    break;

                case REFRESH_dialog:
                    if (mIvCoverBg.getVisibility() == View.VISIBLE) {
                        mIvCoverBg.setVisibility(View.GONE);
                        av_screen_layout.removeView(mIvCoverBg);
                    }
                    break;
                case 5555:
                    try {
                        //发送本机信息
                        addmessageimbenji("2", (String) msg.obj);
                    } catch (Exception e) {
                    }
                    break;
                case 6666:
                    addmessageimbenji("2", mSelfUserInfo.user_id + "");
                    mHandler.sendEmptyMessageDelayed(6666, 5000);
                    break;
                case delay://条目一
                    remove(0);
                    break;
                case delaytwo://条目二
                    removeone(0);
                    break;
                case delayGIF:
                    localqueue.poll();
                    String url = (String) msg.obj;
                    giftbig_fl.setVisibility(View.GONE);
                    ImagePipeline imagePipeline = Fresco.getImagePipeline();
                    Uri uri = Uri.parse(url);
                    imagePipeline.evictFromMemoryCache(uri);
                    imagePipeline.evictFromDiskCache(uri);
                    imagePipeline.evictFromCache(uri);
                    GifAnimation();
                    break;
                case sendGift:
                    //                    addMessageToList1(queue);
                    break;
                case 100:
                    msg_enter.setVisibility(View.INVISIBLE);
                    break;
                case 305:
                    heidouWinQueue.offer(msg.arg1);
                    break;
                case 306:
                    if (iv_konfetti.getVisibility() == View.VISIBLE) {
                        qav_awards.clearAnimation();
                        qav_awards.setVisibility(View.GONE);
                        iv_konfetti.clearAnimation();
                        iv_konfetti.setVisibility(View.GONE);
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
    });
    private int shareType = -1;
    boolean shareWechatComon = false, shareQqSpace = false, shareSina = false;
    PlatformActionListener platformActionListener;
    private TimerTask mTask;


    private void heidouWining(int count) {
        Message message = new Message();
        message.arg1 = count;
        message.what = 306;
        if (count >= HXApp.getInstance().wining) {
            qav_awards.setVisibility(View.VISIBLE);
            iv_konfetti.setVisibility(View.VISIBLE);
            tv_times.setText(count + "");
            WinningEffects();
        }
        lonone = System.currentTimeMillis();
        if (startTime != 0) {
            if ((lonone - startTime) <= 4000) {
                mHandler.removeMessages(306);
            }
        }
        mHandler.sendMessageDelayed(message, 4000);
        startTime = lonone;
    }


    private boolean enableSclide = true;


    //发送本机信息
    private void addmessageimbenji(String type, String user_id) {
        String strTips = strTipsarray[0] + "\r\n" + strTipsarray[1] + "\r\n" +
                strTipsarray[2] + "\r\n" + strTipsarray[3] + "\r\n" +
                strTipsarray[4] + "\r\n" + strTipsarray[5] + "\r\n" +
                strTipsarray[6] + "\r\n" + strTipsarray[7] + "\r\n" +
                strTipsarray[8] + "\r\n" + strTipsarray[9];
        //   strTips = praseString(strTips);
        TIMMessage Tim = new TIMMessage();
        TIMCustomElem elem = new TIMCustomElem();
        elem.setData(HXUtil.addmessageimbenji(strTips, type, user_id).toString()
                           .getBytes());
        elem.setDesc(UNREAD);
        if (1 == Tim.addElement(elem)) {
            Notifier.showNormalMsg(mContext, "出错了...");
            return;
        }
        mConversation.sendMessage(Tim, new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {
                Logger.out("send message error" + i + ": " + s);
            }


            @Override
            public void onSuccess(TIMMessage timMessage) {

            }
        });
    }


    private BroadcastReceiver connectionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo mobileInfo = connectMgr
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mobileInfo == null) {
                return;
            }
            NetworkInfo wifiInfo = connectMgr
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifiInfo == null) {
                return;
            }
            Logger.out("WL_DEBUG netinfo mobile = " + mobileInfo.isConnected() +
                    ", wifi = " + wifiInfo.isConnected());
            int netType = Utils.getNetWorkType(mContext);
            Logger.out(
                    "WL_DEBUG connectionReceiver getNetWorkType = " + netType);
            if (mQavsdkControl != null) {
                mQavsdkControl.setNetType(netType);
            }
            String s = "wifi";
            switch (netType) {
                case 0:
                    s = "网络断开";
                    break;
                case 1:
                    s = "网络连接";
                    break;
                case 2:
                    s = "WIFI网络";
                    break;
                case 3:
                    s = "3G网络";
                    break;
                case 4:
                    s = "2G网络";
                    break;
                case 5:
                    s = "4G网络";
                    break;
            }
            Notifier.showShortMsg(mContext, s);

            if (!mobileInfo.isConnected() && !wifiInfo.isConnected()) {
                Logger.out("WL_DEBUG connectionReceiver no network = ");
            }
        }
    };
    private boolean currentCameraIsFront = true;
    private String PushfinalUrl;//.
    /**
     * 邀请Dialog
     */

    private Dialog inviteDialog;
    private Dialog dialog;

    private boolean isFirst = true;
    private int heart_user_id;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Logger.out("onReceive action = " + action);

            if (!mSelfUserInfo.isCreater) {//非主播进入
                if (action.equals(Utils.ACTION_ROOM_CREATE_COMPLETE)) {
                    Logger.out("liveAcitivity onReceive ACTION_ROOM_CREATE_COMPLETE");
                    int mCreateRoomErrorCode = intent
                            .getIntExtra(Utils.EXTRA_AV_ERROR_RESULT, AVError.AV_OK);
                    if (mCreateRoomErrorCode == AVError.AV_OK) {
                        if (avContext != null) {
                            mQavsdkControl
                                    .onCreate(getApplicationContext(), findViewById(android.R.id.content));
                        } else {
                            finish();
                            return;
                        }
                    } else {
                        Notifier.showShortMsg(mContext, "网络有点问题，请重试");
                    }
                }
                Logger.out(
                        "WL_DEBUG ANR  onReceive action = " + action + " Out");
            } else {//主播进入
                if (action.equals(Utils.ACTION_ROOM_CREATE_COMPLETE)) {
                    //加入房间的回调
                    Logger.out("create room complete");
                    int mCreateRoomErrorCode = intent
                            .getIntExtra(Utils.EXTRA_AV_ERROR_RESULT, AVError.AV_OK);

                    if (mCreateRoomErrorCode == AVError.AV_OK) {
                        Logger.out("createGroup");
                        ArrayList<String> list = new ArrayList<String>();
                        list.add(mSelfUserInfo.nickname);
                        TIMGroupManager.getInstance()
                                       .createGroup("ChatRoom", list, roomNum +
                                               "", new TIMValueCallBack<String>() {
                                           @Override
                                           public void onError(int i, String s) {
                                               if (dialog != null) {
                                                   dialog.dismiss();
                                               }
                                               Notifier.showNormalMsg(getApplicationContext(), "创建房间失败，请重试");
                                           }


                                           @Override
                                           public void onSuccess(String s) {
                                               groupId = s;
                                               if (loadingDialog != null) {
                                                   loadingDialog.todismiss();
                                               }
                                               //sendBroadcast(new Intent(Utils.ACTION_CREATE_GROUP_ID_COMPLETE));
                                               if (avContext != null) {
                                                   mQavsdkControl
                                                           .onCreate((HXApp) getApplication(), findViewById(android.R.id.content));
                                               } else {
                                                   finish();
                                                   return;
                                               }
                                           }
                                       });
                    } else {
                        if (mCreateRoomErrorCode ==
                                AVError.AV_ERR_SERVER_NO_PERMISSION) {
                            mQavsdkControl
                                    .enterRoomCreater(roomNum, "anchor2", false, AVRoom.VIDEO_RECV_MODE_SEMI_AUTO_RECV_CAMERA_VIDEO);
                        }
                    }
                }
            }

            if (action.equals(Utils.ACTION_SURFACE_CREATED)) {
                Logger.out("JXH:ACTION_SURFACE_CREATED");
                locateCameraPreview();
                wakeLock.acquire();
                if (mSelfUserInfo.isCreater) {
                    initTIMGroup();
                    mIsSuccess = true;
                    mQavsdkControl.toggleEnableCamera();
                    boolean isEnable = mQavsdkControl.getIsEnableCamera();
                    refreshCameraUI();
                    if (mOnOffCameraErrorCode != AVError.AV_OK) {
                        showDialog(isEnable
                                   ? DIALOG_OFF_CAMERA_FAILED
                                   : DIALOG_ON_CAMERA_FAILED);
                        mQavsdkControl.setIsInOnOffCamera(false);
                        refreshCameraUI();
                    }
                    //                    mHandler.sendEmptyMessageDelayed(GET_ROOM_INFO, 0);
                    mQavsdkControl.setRequestCount(0);
                    //上报主播心跳
                    //                    mHeartClickTimer.schedule(mHeartClickTask, 1000, 10000);
                } else {
                    mIsSuccess = true;
                    //IMSDk 加入聊天室
                    joinGroup();
                    initTIMGroup();
                    boolean isEnable = mQavsdkControl.getIsEnableCamera();
                    if (isEnable) {
                        mQavsdkControl.toggleEnableCamera();
                    }
                }
            } else if (action.equals(Utils.ACTION_VIDEO_CLOSE)) {
                String identifier = intent
                        .getStringExtra(Utils.EXTRA_IDENTIFIER);
                if (!TextUtils.isEmpty(mRecvIdentifier)) {
                    mQavsdkControl.setRemoteHasVideo(false, mRecvIdentifier, 0);
                }
                mRecvIdentifier = identifier;
            } else if (action.equals(Utils.ACTION_VIDEO_SHOW)) {
                //  int i = mQavsdkControl.getAVVideoControl().enableExternalCapture(true);
                //        Logger.out("JXH:ACTION_VIDEO_SHOW " + i);
                //成员模式加入视 频聊天室
                String identifier = intent
                        .getStringExtra(Utils.EXTRA_IDENTIFIER);
                mRecvIdentifier = identifier;
                mQavsdkControl.setRemoteHasVideo(true, mRecvIdentifier, 0);

                //    Utils.switchWaitingDialog(ctx, mDialogInit, DIALOG_INIT, false);
            } else if (action
                    .equals(Utils.ACTION_SEMI_AUTO_RECV_CAMERA_VIDEO)) {//自动接收视频
                //                   if (!mSelfUserInfo.isCreater){
                //                    String[] strings = intent.getStringArrayExtra(Utils.ACTION_SEMI_AUTO_RECV_CAMERA_VIDEO);
                //                    Logger.out("JXH:ACTION_SEMI_AUTO_RECV_CAMERA_VIDEO " + strings.toString());
                //                    hostRequestView(mHostIdentifier);
                //                    isFirst = false;
                //                }
            } else if (action.equals(Utils.ACTION_ENABLE_CAMERA_COMPLETE)) {
                int intExtra = intent
                        .getIntExtra(Utils.EXTRA_AV_ERROR_RESULT, -1);
                boolean booleanExtra = intent
                        .getBooleanExtra(Utils.EXTRA_IS_ENABLE, false);
                //                mQavsdkControl.getAVVideoControl().enableCustomerRendMode(remoteVideoPreviewCallback);
                Logger.out("onClick ACTION_ENABLE_CAMERA_COMPLETE   " +
                        " status " + mQavsdkControl.getIsEnableCamera());
                Logger.out("wskcreate 接受到了广播" +
                        mQavsdkControl.getIsEnableCamera());
                if (mSelfUserInfo.isCreater == true) {
                    refreshCameraUI();
                    mOnOffCameraErrorCode = intent
                            .getIntExtra(Utils.EXTRA_AV_ERROR_RESULT, AVError.AV_OK);
                    boolean isEnable = intent
                            .getBooleanExtra(Utils.EXTRA_IS_ENABLE, false);
                    if (mOnOffCameraErrorCode == AVError.AV_OK) {
                        if (!mIsPaused) {
                            Logger.out(
                                    "ACTION_ENABLE_CAMERA_COMPLETE mHostIdentifier " +
                                            mHostIdentifier);
                            mQavsdkControl.setSelfId(mHostIdentifier);
                            mQavsdkControl
                                    .setLocalHasVideo(isEnable, mHostIdentifier);
                        }
                    }
                    //使用美颜功能
                    boolean enableBeauty = mQavsdkControl.getAVContext()
                                                         .getVideoCtrl()
                                                         .isEnableBeauty();
                    if (enableBeauty) {
                        mQavsdkControl.getAVContext().getVideoCtrl()
                                      .inputBeautyParam(0f);
                    }

                    if (currentCameraIsFront == false) {
                        Logger.out(
                                " onSwitchCamera!!ACTION_ENABLE_CAMERA_COMPLETE and lastTime is backCamera :  " +
                                        mQavsdkControl.getIsInOnOffCamera());
                        onSwitchCamera();
                    }
                    setRecordParam();//开启录制
                    PushStart();//开启旁路直播推送
                }
            } else if (action
                    .equals(Utils.ACTION_SWITCH_CAMERA_COMPLETE)) {//打开摄像头
                Logger.out(" onSwitchCamera!! ACTION_SWITCH_CAMERA_COMPLETE  " +
                        mQavsdkControl.getIsInOnOffCamera());
                refreshCameraUI();
                mSwitchCameraErrorCode = intent
                        .getIntExtra(Utils.EXTRA_AV_ERROR_RESULT, AVError.AV_OK);
                boolean isFront = intent
                        .getBooleanExtra(Utils.EXTRA_IS_FRONT, false);
                if (mSwitchCameraErrorCode != AVError.AV_OK) {
                    showDialog(isFront
                               ? DIALOG_SWITCH_FRONT_CAMERA_FAILED
                               : DIALOG_SWITCH_BACK_CAMERA_FAILED);
                } else {
                    currentCameraIsFront = mQavsdkControl.getIsFrontCamera();
                    if (isFront) {
                        AVUIControl.view.setMirror(true);
                    } else {
                        AVUIControl.view.setMirror(false);
                    }
                    Logger.out("onSwitchCamera  " + currentCameraIsFront);
                }
            } else if (action.equals(Utils.ACTION_MEMBER_CHANGE)) {
                Logger.out("JXH:ACTION_MEMBER_CHANGE");
                if (!mSelfUserInfo.isCreater) {
                    // if (isFirst)
                    hostRequestView(mHostIdentifier);
                }
            } else if (action
                    .equals(Utils.ACTION_INSERT_ROOM_TO_SERVER_COMPLETE)) {
                mHandler.sendEmptyMessageDelayed(GET_ROOM_INFO, 0);
            } else if (action.equals(Utils.ACTION_INVITE_MEMBER_VIDEOCHAT)) {
                //发起邀请消息
                selectIdentier = intent.getStringExtra(Utils.EXTRA_IDENTIFIER);
                Logger.out(
                        "onReceive inviteVC selectIdentier " + selectIdentier);

                if (viewIndex != null) {
                    String id;
                    if (selectIdentier.startsWith("86-")) {
                        id = selectIdentier.substring(3);
                    } else {
                        id = selectIdentier;
                    }
                    if (viewIndex.containsKey(id)) {
                        Toast.makeText(AvActivity.this, "you can't allowed to invite the same people", Toast.LENGTH_SHORT)
                             .show();
                        return;
                    }
                }
                //开始邀请信息
                sendVCInvitation(selectIdentier);
            } else if (action.equals(Utils.ACTION_MEMBER_VIDEO_SHOW)) {
                String identifier = intent
                        .getStringExtra(Utils.EXTRA_IDENTIFIER);
                mRecvIdentifier = identifier;
                //不在这个位置
                //                int viewindex = viewIndex.get(identifier.substring(3));
                //第一个位置
                int locactionIndex = mQavsdkControl.getSmallVideoView();
                mMemberVideoCount = locactionIndex;
                Logger.out("JXH: ACTION_MEMBER_VIDEO_SHOW  id " + identifier +
                        " viewindex " + locactionIndex);
                mQavsdkControl
                        .setRemoteHasVideo(true, mRecvIdentifier, locactionIndex);
            } else if (action.equals(Utils.ACTION_SHOW_VIDEO_MEMBER_INFO)) {
                String identifier = intent
                        .getStringExtra(Utils.EXTRA_IDENTIFIER);
            } else if (action.equals(Utils.ACTION_CLOSE_MEMBER_VIDEOCHAT)) {
                String identifier = intent
                        .getStringExtra(Utils.EXTRA_IDENTIFIER);
                closeVideoMemberByHost(identifier);
            } else if (action.equals(Utils.ACTION_CLOSE_ROOM_COMPLETE)) {
                Logger.out("JXH:ACTION_CLOSE_ROOM_COMPLETE");
                if (isFinishing()) {
                    return;
                } else {
                    closeActivity();
                }
            } else if (action.equals(Utils.ACTION_EXTRA_STOPRECORDD)) {
                Logger.out("JXH:ACTION_EXTRA_STOPRECORDD");
                boolean mTrue = intent.getBooleanExtra("true", false);
                onCloseVideo(mTrue, false);
            } else if (action.equals(Utils.ACTION_HAVE_REMOTEVIDEO)) {
                if (mIvCoverBg.getVisibility() == View.VISIBLE) {
                    mIvCoverBg.setVisibility(View.GONE);
                    av_screen_layout.removeView(mIvCoverBg);
                }
            }
        }
    };


    private void closeActivity() {
        if (mQavsdkControl != null) {
            mQavsdkControl.onDestroy();
        }
        destroyTIM();
        if (wakeLock.isHeld()) {
            wakeLock.release();
        }
        if (mSelfUserInfo.isCreater == true) {
            setResult(Utils.SHOW_RESULT_CODE);
            //   Utils.switchWaitingDialog(ctx, mDialogAtDestroy, DIALOG_DESTROY, true);
        } else {
            setResult(Utils.VIEW_RESULT_CODE);
        }

        // 注销广播
        if (mBroadcastReceiver != null) {
            getApplicationContext().unregisterReceiver(mBroadcastReceiver);
            mBroadcastReceiver = null;
        }
        if (connectionReceiver != null) {
            getApplicationContext().unregisterReceiver(connectionReceiver);
            connectionReceiver = null;
            connectionReceiver = null;
        }

        members = null;
        if (mtimer != null) {
            mtimer.cancel();
            mtimer = null;
        }
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }

        if (lookPositiontimer != null) {
            lookPositiontimer.cancel();
            lookPositiontimer = null;
        }

        mMaskViewCount = 0;
        finish();
        Logger.out("AV+ closeActivity");
    }


    private long missTime = 0;//进入房间通知显示的时间
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //心跳次数
    private int heartbeat_intervaltime;
    //网络状况上报次数；
    private int mTypeWangtime;
    //分享提示计时
    private int shareTipsTime = 0;
    //发im提示网络情况
    private int ImupTypeWangtime;
    //获取状态的判断
    private boolean booleanextviewTextView1;
    int left;
    float x_length;
    boolean isRight = false;
    int width;
    int height;
    boolean isScroll = false;
    private AVEndpoint.RequestViewListCompleteCallback mRequestViewListCompleteCallback = new AVEndpoint.RequestViewListCompleteCallback() {

        protected void OnComplete(String[] var1, AVView[] var2, int var3, int var4) {
            Log.d("SdkJni", "RequestViewListCompleteCallback.OnComplete");
        }
    };

    AVVideoCtrl.RemoteVideoPreviewCallback remoteVideoPreviewCallback = new AVVideoCtrl.RemoteVideoPreviewCallback() {
        public void onFrameReceive(AVVideoCtrl.VideoFrame videoFrame) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
        }
    };
    WrapContentLinearLayoutManager wrapContentLinearLayoutManager;
    String mhostzhuidentity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.av_activity);
        ctx = this;
        mContext = getApplicationContext();
        EventBus.getDefault().register(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
        ButterKnife.bind(this);
        mHXApp = (HXApp) getApplication();
        mSelfUserInfo = mHXApp.getUserInfo();
        if (!mSelfUserInfo.isCreater) {
            mIvCoverBg.setVisibility(View.VISIBLE);
            HXImageLoader.loadBlurImage(mFbCoverBg, getIntent()
                    .getStringExtra("photo"), width, height);
        } else {
            mWorkStatusQavTopright.setVisibility(View.VISIBLE);
        }
        ShareSDK.initSDK(mContext);
        mQavsdkControl = mHXApp.getQavsdkControl();
        avContext = mQavsdkControl.getAVContext();
        SharedPreferenceUtil.setContext(mContext);
        qav_awards.setVisibility(View.GONE);
        iv_konfetti.setVisibility(View.GONE);

        registerBroadcastReceiver();
        roomNum = getIntent().getExtras().getInt(Utils.EXTRA_ROOM_NUM);
        int netType = Utils.getNetWorkType(mContext);
        if (netType != AVConstants.NETTYPE_NONE) {
            mQavsdkControl.setNetType(netType);
        }
        int px = Utils.getPX(38);
        if (mSelfUserInfo.isCreater) {
            if (Utils.isNetworkAvailable(mContext)) {
                if (roomNum != 0) {
                    mQavsdkControl
                            .enterRoomCreater(roomNum, "anchor2", false, AVRoom.VIDEO_RECV_MODE_SEMI_AUTO_RECV_CAMERA_VIDEO);
                }
            } else {
                Notifier.showNormalMsg(mContext, getString(R.string.notify_no_network));
            }
            HXImageLoader.loadImage(hostHead, mSelfUserInfo.photo, px, px);
            tv_id.setText("嘿秀ID:" + mSelfUserInfo.identity);
            perso_num.setText(0 + "观众");
            tv_isconcern.setVisibility(View.GONE);
            loadingDialog = new LoadingDialog(AvActivity.this, R.style.dialog);
            loadingDialog.show();
        } else {
            OpenLive();
            HXImageLoader.loadImage(hostHead, getIntent()
                    .getStringExtra("photo"), px, px);
        }

        //不熄屏
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "TAG");

        mQavsdkControl.setRequestCount(0);
        mMemberList = mQavsdkControl.getMemberList();
        mNormalMemberList = copyToNormalMember();
        mVideoMemberList = new ArrayList<MemberInfo>();
        //parent_rela的左边值
        left = parent_rela.getLeft();
        av_screen_layout.setDrawingCacheEnabled(true);

        mRequestIdentifierList = new String[MAX_REQUEST_VIEW_COUNT];
        mRequestViewList = new AVView[MAX_REQUEST_VIEW_COUNT];
        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideGifPad();
            }
        };
        mHeadLayout.setOnClickListener(onClickListener);
        //初始化控件
        initgoup();
        initView();
        if (!mSelfUserInfo.isCreater) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("viewed_user_id", mHostIdentifier);
            params.put("user_id", mSelfUserInfo.user_id);
            HXJavaNet
                    .post(HXJavaNet.url_user_infos, params, new AngelNetCallBack() {
                        @Override
                        public void onSuccess(final int status, final String json, final String readable_msg) {
                            messageInfo = Utils
                                    .jsonToBean(json, NewPerMesInfo.class);
                            if (null != messageInfo) {
                                mMeilizhiTextview.setText(
                                        messageInfo.ticket_amount + "");
                                int relation_type = messageInfo.relation_type;
                                if (relation_type == 1) {
                                    tv_isconcern.setVisibility(View.GONE);
                                }
                            }
                        }


                        @Override
                        public void onFailure(String msg) {
                            Logger.err(msg);
                        }
                    });
            mhostzhuidentity = getIntent().getExtras().getString("identity");

            tv_id.setText("嘿秀ID:" + mhostzhuidentity);
        } else {
            share_rl.setVisibility(View.VISIBLE);
            final LiveShareView liveShareView = new LiveShareView(AvActivity
                    .this, roomNum, mQavsdkControl, mMeilizhiTextview, heart_user_id);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            share_rl.addView(liveShareView, layoutParams);
            liveShareView
                    .setClicklistener(new LiveShareView.ClickListenerInterface() {
                        @Override
                        public void doShare() {
                            //私密直播
                            if (isScret == 1) {
                                mPwd = liveShareView.mEtPwd.getSecurityString();
                                if (mPwd.length() < 4) {
                                    Notifier.showShortMsg(mContext, "请输入四位数字密码");
                                    return;
                                }
                            }
                            if (!Utils.isNetworkAvailable(mContext)) {
                                Notifier.showNormalMsg(mContext, getString(R.string.notify_no_network));
                                return;
                            }
                            String title = liveShareView.et_name.getText()
                                                                .toString();
                            HashMap<String, Object> para = new HashMap<>();
                            HXApp.getInstance().setRoomName(title);
                            para.put("user_id", mSelfUserInfo.user_id);
                            para.put("title", title);
                            para.put("group_id", groupId);
                            if (!"".equals(mSelfUserInfo.high_photo)) {
                                para.put("cover_image", mSelfUserInfo.high_photo);
                            } else {
                                para.put("cover_image", mSelfUserInfo.photo);
                            }
                            para.put("address", mSelfUserInfo.address);
                            para.put("room_num", roomNum);
                            para.put("type", isScret);
                            if (isScret == 1) {
                                para.put("password", Integer.valueOf(mPwd));
                            }
                            // _live_create直接就是根目录
                            HXJavaNet
                                    .post(HXJavaNet.url_live_create_room, para, new AngelNetCallBack() {
                                        //进去直播界面
                                        @Override
                                        public void onSuccess(int ret_code, String ret_data, String msg) {
                                            if (dialog != null) {
                                                dialog.dismiss();
                                            }
                                            if (ret_code == 200) {
                                                try {
                                                    JSONObject jo = new JSONObject(ret_data);
                                                    mMeilizhiTextview.setText(
                                                            jo.getInt("charm_value") +
                                                                    "");
                                                    heart_user_id = jo
                                                            .optInt("heart_user_id");
                                                    liveShareView
                                                            .share();//直播前分享
                                                    share_rl.setVisibility(View.GONE);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            } else {
                                                Notifier.showShortMsg(mContext, "网络异常,请重试");
                                                if (mQavsdkControl != null) {
                                                    mQavsdkControl.exitRoom();
                                                }
                                            }
                                        }


                                        @Override
                                        public void onFailure(String msg) {
                                            if (dialog != null) {
                                                dialog.dismiss();
                                            }
                                            Notifier.showShortMsg(mContext,
                                                    "网络异常," + "请重试");
                                            if (mQavsdkControl != null) {
                                                mQavsdkControl.exitRoom();
                                            }
                                        }
                                    });
                        }
                    });
        }
        mLlUsercp1.setVisibility(View.GONE);

        gestureDetector = new GestureDetectorCompat(ctx, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                Logger.out("gestureDetector:" + "onDown   ");
                int x = (int) e.getRawX();
                int y = (int) e.getRawY();
                boolean a = inView(x, y, qav_top_bar);
                Boolean b = inView(0, height -
                        studio.archangel.toolkitv2.util.Util
                                .getPX(200), width, height, x, y);
                if (a | b) {
                    enableSclide = false;
                }
                isScroll = false;
                return false;
            }


            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

                Logger.out("gestureDetector:" + "onScroll   " + distanceX);
                if (!enableSclide) {
                    return false;
                }
                if (Math.abs(distanceX / distanceY) >= 3) {
                    float x = qav_top_bar.getX() - distanceX;
                    if (x > width) {
                        x = width;
                    } else if (x < 0) {
                        x = 0;
                    }
                    if (isRight && qav_top_bar.getX() == width) {

                        if (distanceX >= 0) {
                            qav_top_bar.setX(x);
                            qav_bottom_bar.setX(x);
                            zan.setX(zan.getX() - distanceX);
                            x_length += distanceX;
                        }
                    } else if (!isRight && qav_top_bar.getX() == 0) {

                        if (distanceX <= 0) {
                            qav_top_bar.setX(x);
                            qav_bottom_bar.setX(x);
                            zan.setX(zan.getX() - distanceX);
                            x_length += distanceX;
                        }
                    } else {
                        qav_top_bar.setX(x);
                        qav_bottom_bar.setX(x);
                        zan.setX(zan.getX() - distanceX);
                        x_length += distanceX;
                    }

                    isScroll = true;
                }
                return false;
            }


            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Logger.out("gestureDetector:" + "onSingleTapUp   ");

                //isScroll = false;
                return false;
            }


            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                Logger.out("gestureDetector:" + "onFling   " + velocityX);
                return false;
            }
        }

        );
        getUserAccount();
        getMemberInfo(mSelfUserInfo.user_id, roomNum, 0);

        try {
            heartbeat_interval = HXApp.getInstance().configMessage.heartbeat_interval *
                    1000;
            if (heartbeat_interval < 4000) {
                heartbeat_interval = 5000;
            }
        } catch (Exception e) {
        }

        //关注主播排名
        if (mSelfUserInfo.isCreater && mSelfUserInfo.type != 0) {
            zhubolookPosition();
        }

        mtimer = new Timer();
        //没有分享过
        mTask = new TimerTask() {
            @Override
            public void run() {
                if (roomNum == 0 || heart_user_id == 0) {
                    return;
                }
                second++;
                shareTipsTime++;
                mTypeWangtime++;
                heartbeat_intervaltime++;
                ImupTypeWangtime++;

                if (shareTipsTime == 1 * 60 || shareTipsTime == 10 * 60) {
                    if (shareType == -1) {
                        //没有分享过
                        if (mSelfUserInfo.isCreater) {
                            tipsCreater.post(new Runnable() {
                                @Override
                                public void run() {
                                    tipsCreater.setVisibility(View.VISIBLE);
                                }
                            });
                        } else {
                            tipsViewer.post(new Runnable() {
                                @Override
                                public void run() {
                                    tipsViewer.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }
                }
                if (mTypeWangtime >= 30) {
                    mTypeWangtime = 0;
                }
                if (ImupTypeWangtime >= 5) {
                    ImupTypeWangtime = 0;

                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (mQavsdkControl.getRoom() == (null) ||
                                    mQavsdkControl.getRoom().getQualityTips() ==
                                            null) {
                                return;
                            }
                            String strTips = mQavsdkControl.getRoom()
                                                           .getQualityTips();
                            if (strTips.length() < 4) {
                                return;
                            }

                            strTips = strTips
                                    .substring(strTips.indexOf("RTT=") + 4);
                            strTips = strTips
                                    .substring(0, strTips.indexOf("\r"));
                            if (strTipsarrayi < 9) {
                                strTipsarray[strTipsarrayi] = strTips;
                                strTipsarrayi++;
                            } else {
                                strTipsarray[strTipsarrayi] = "版本：" +
                                        BuildConfig.VERSION_NAME +
                                        "手机型号" + Build.MODEL +
                                        mQavsdkControl.getRoom()
                                                      .getQualityTips() +
                                        mQavsdkControl.getRoom()
                                                      .getQualityParam() +
                                        "版本：" +
                                        BuildConfig.VERSION_NAME;
                                strTipsarrayi = 0;
                            }
                            if (!TextUtils.isEmpty(strTips)) {
                                if (Integer.parseInt(strTips.trim()) > 1000) {
                                    mLlUsercp1.setVisibility(View.VISIBLE);
                                    mWorkStatusQavTopleft
                                            .setVisibility(View.VISIBLE);
                                    mWorkStatusQavTopleft.setText("当前网络不佳");
                                } else {
                                    mLlUsercp1.setVisibility(View.GONE);
                                }
                            } else {
                                mLlUsercp1.setVisibility(View.GONE);
                            }
                        }
                    });
                }

                if (heartbeat_interval <= heartbeat_intervaltime * 1000) {
                    heartbeat_intervaltime = 0;
                    if (!mSelfUserInfo.isCreater) {
                        if (HXApp.getInstance().configMessage.heart_gz_time < 5) {
                            return;
                        } else {
                            heartbeat();
                        }
                    } else {
                        HashMap<String, Object> jo = new HashMap<String, Object>();
                        jo.put("room_num", roomNum);
                        jo.put("live_user_id", heart_user_id);
                        jo.put("user_id", mSelfUserInfo.user_id);
                        HXJavaNet.post(HXJavaNet.url_heartbeat, HXApp
                                .getInstance().configMessage.time_out, jo, new NetCallBack
                                (AvActivity.this) {
                            @Override
                            public void onSuccess(int ret_code, String ret_data, String msg) {

                                if (ret_code == 503) {
                                    Notifier.showShortMsg(mContext, "网络不稳定请重新连接");
                                    onCloseVideo(false, false);
                                }
                            }


                            @Override
                            public void onFailure(String msg) {
                                super.onFailure(msg);
                            }
                        });
                    }
                }
            }
        };
        mtimer.schedule(mTask, 0, 1000);
        thread = new Thread(runnable);
        thread.start();
        threadbig = new Thread(runnablebig);
        threadbig.start();
        threadgif = new Thread(runnablegif);
        threadgif.start();
        //主播上传赞数
        if (mSelfUserInfo.isCreater) {
            threadzan = new Thread(runnablezan);
            threadzan.start();
        }
        //initdanmu2();
        TIMElemTypeJInfang();
    }


    Timer lookPositiontimer;


    private void zhubolookPosition() {
        lookPositiontimer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                HashMap<String, Object> jo = new HashMap<String, Object>();
                jo.put("user_id", mSelfUserInfo.user_id);

                HXJavaNet
                        .post(HXJavaNet.url_lookPosition, jo, new NetCallBack(AvActivity.this) {
                            @Override
                            public void onSuccess(int ret_code, String ret_data, String msg) {
                                super.onSuccess(ret_code, ret_data, msg);
                                if (ret_code == 200) {
                                    try {
                                        if (ret_data.equals("")) {
                                            return;
                                        }

                                        JSONObject json = new JSONObject(ret_data);
                                        int charm_value = json
                                                .optInt("position");
                                        if (charm_value != 0) {
                                            mHostList
                                                    .setVisibility(View.VISIBLE);
                                            mHostList.setText(
                                                    "当前热榜排行第" + charm_value);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                ;
                            }


                            @Override
                            public void onFailure(String msg) {
                                super.onFailure(msg);
                            }
                        });
            }
        };
        lookPositiontimer.schedule(task, 3000, 60000);
    }


    public void WinningEffects() {

        final ScaleAnimation animation = new ScaleAnimation(0.0f, 1.4f, 0.0f, 1.4f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(1000);
        qav_awards.setAnimation(animation);
        final RotateAnimation rotateAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(4000);
        iv_light.setAnimation(rotateAnimation);
        final ScaleAnimation animationkon = new ScaleAnimation(0.0f, 1.4f, 0.0f, 1.4f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animationkon.setStartOffset(1000);
        animationkon.setDuration(1000);
        iv_konfetti.setAnimation(animationkon);
    }


    //非主播打开房间
    private void OpenLive() {
        if (Utils.isNetworkAvailable(mContext)) {
            if (roomNum != 0) {
                int nums = roomNum;
                //进房必须调，退出要调stop
                if ((mQavsdkControl != null) &&
                        (mQavsdkControl.getAVContext() != null) &&
                        (mQavsdkControl.getAVContext().getAudioCtrl() !=
                                 null)) {
                    mQavsdkControl.getAVContext().getAudioCtrl()
                                  .startTRAEService();
                } else {
                    Notifier.showShortMsg(mContext, "请重启应用");
                    return;
                }
                mQavsdkControl
                        .enterRoomViewer(nums, "user2", true, AVRoom.VIDEO_RECV_MODE_SEMI_AUTO_RECV_CAMERA_VIDEO);
            }
        } else {
            Notifier.showNormalMsg(mContext, getString(R.string.notify_no_network));
        }
    }


    private void heartbeat() {

        HashMap<String, Object> jo = new HashMap<String, Object>();
        jo.put("room_num", roomNum);

        jo.put("live_user_id", heart_user_id);
        jo.put("user_id", mSelfUserInfo.user_id);

        HXJavaNet.post(HXJavaNet.url_heartbeat, HXApp
                .getInstance().configMessage.time_out, jo, new NetCallBack(this) {
            @Override
            public void onSuccess(int ret_code, String ret_data, String msg) {
                super.onSuccess(ret_code, ret_data, msg);
                if (ret_code != 200) {
                    heartbeat();
                }
            }


            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                heartbeat();
            }
        });
    }


    private void initgoup() {
        messages = new ArrayList<>();
        members = new Vector<>();
        messages2 = new ArrayList<>();
        messagesgift = new ArrayList<>();
        adapter_chat2 = new LiveChatMessageAdapter(mContext, messages2);
        adapter_gift = new LiveChatMessageAdapter(mContext, messagesgift);
        adapter_chat = new LiveChatMessageAdapter(mContext, messages);
        list_chat.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = studio.archangel.toolkitv2.util.Util.getPX(2);
            }
        });
        //初始化其他人头像
        adapter_member = new LiveMemberAdapter(list_member, members, this);
        //禮物
        list_chat2.setAdapter(adapter_chat2);//条目二
        act_live_rv_gift.setAdapter(adapter_gift);//条目一

        list_chat2.scheduleLayoutAnimation();
        list_chat.setAdapter(adapter_chat);

        adapter_chat
                .setOnItemToClickListener(new CommonRecyclerAdapter.OnItemToClickListener() {
                    @Override
                    public void onItemClick(View view, Object data) {
                        JSONObject jo = ((JSONObject) data)
                                .optJSONObject("user");
                        //   if(jo.get("user_name"))

                        if ("系统消息".equals(jo.optString("user_name"))) {
                            return;
                        }
                        newPerCard(jo, false);
                    }
                });

        adapter_chat
                .setOnItemLongClickListener(new CommonRecyclerAdapter.OnItemLongClickListener() {
                    @Override
                    public void OnItemLongClick(int position, View view, Object data) {
                        JSONObject jo = ((JSONObject) data)
                                .optJSONObject("user");
                        if (null != popupWindow && popupWindow.isShowing()) {
                            try {
                                mEditTextInputMsg.setText(
                                        "@" + jo.getString("user_name"));
                                mEditTextInputMsg.setSelection(mEditTextInputMsg
                                        .getText().length());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (mSelfUserInfo.forbid == 2) {
                                Notifier.showShortMsg(mContext, "你已经被全局禁言");
                            } else {
                                showsoftKeyboard();
                                try {
                                    mEditTextInputMsg.setText(
                                            "@" + jo.getString("user_name"));
                                    mEditTextInputMsg
                                            .setSelection(mEditTextInputMsg
                                                    .getText().length());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
        //设置其他人头像的点击事件
        list_member.setAdapter(adapter_member);
        adapter_member
                .setOnItemToClickListener(new LiveMemberAdapter.OnItemToClickListener() {

                    @Override
                    public void onItemClick(View view, Object data) {
                        JSONObject jo = (JSONObject) data;
                        String user_id = null;
                        try {
                            user_id = jo.getString("user_id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        newPerCard(jo, false);
                        //showPerInfoCard(mSelfUserInfo.user_id + "", user_id);
                    }
                });
    }


    private void showsoftKeyboard() {
        final View view = View.inflate(ctx, R.layout.inputlayout, null);
        final TextView send = (TextView) view
                .findViewById(R.id.act_live_input_send);
        mEditTextInputMsg = (AppCompatEditText) view
                .findViewById(R.id.act_live_input_text);

     /*   InputMethodManager inputManager = (InputMethodManager) mEditTextInputMsg.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(mEditTextInputMsg, 0);*/

        tanmubutton = (ImageView) view.findViewById(R.id.tanmubutton);
        initdanmu();
        mEditTextInputMsg
                .setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            onSendMsg();
                            return true;
                        }
                        return false;
                    }
                });

        send.setOnClickListener(AvActivity.this);
        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                tanmuboolean = false;
                getWindow()
                        .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources()
                .getDrawable(R.drawable.shape_button_white));
        //监听键盘弹起
        SoftKeyboardUtil
                .observeSoftKeyboard(AvActivity.this, new SoftKeyBoardListener(this) {
                    @Override
                    public void onSoftKeyBoardChange(int softKeybardHeight, boolean visible) {
                        super.onSoftKeyBoardChange(softKeybardHeight, visible);
                        AvActivity avActivity = this.avActivity.get();
                        if (avActivity != null) {
                            //float y = avActivity.relativelayout.getY();
                            avActivity.relativelayout.setY(-softKeybardHeight);
                        }
                    }
                });

        // 设置好参数之后再show
        int left = b_message.getLeft();
        int bottom = b_message.getBottom();
        popupWindow
                .showAtLocation(b_message, Gravity.CENTER_HORIZONTAL, left, bottom);

        mEditTextInputMsg.setFocusable(true);
        mEditTextInputMsg.setFocusableInTouchMode(true);
        mEditTextInputMsg.requestFocus();

        InputMethodManager inputManager = (InputMethodManager) getApplicationContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    private static class SoftKeyBoardListener implements SoftKeyboardUtil.OnSoftKeyboardChangeListener {
        public WeakReference<AvActivity> avActivity;


        public SoftKeyBoardListener(AvActivity avActivity) {
            this.avActivity = new WeakReference<AvActivity>(avActivity);
        }


        @Override
        public void onSoftKeyBoardChange(int softKeybardHeight, boolean visible) {

        }
    }


    @Override
    protected void onStart() {

        super.onStart();
        if (mSelfUserInfo.isCreater && isback) {
            TIMMessage Tim = new TIMMessage();
            TIMCustomElem elem = new TIMCustomElem();
            elem.setData(HXUtil.outLiveChatRoomMessage(false).toString()
                               .getBytes());
            elem.setDesc(UNREAD);
            if (1 == Tim.addElement(elem)) {
                Notifier.showNormalMsg(mContext, "出错了...");
                return;
            }
            mConversation.sendMessage(Tim, new TIMValueCallBack<TIMMessage>() {
                @Override
                public void onError(int i, String s) {

                }


                @Override
                public void onSuccess(TIMMessage timMessage) {
                    isback = false;
                    addMessageToList(timMessage);
                }
            });
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mIsPaused = false;
        LEVAE_MODE = false;
        mQavsdkControl.onResume();
        if (mOnOffCameraErrorCode != AVError.AV_OK) {
            showDialog(DIALOG_ON_CAMERA_FAILED);
        }
        //  startOrientationListener();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mIsPaused = true;
        mQavsdkControl.onPause();
        Logger.out("onPause switchCamera!! ");
        refreshCameraUI();
        if (mOnOffCameraErrorCode != AVError.AV_OK) {
            showDialog(DIALOG_OFF_CAMERA_FAILED);
        }
        //        stopOrientationListener();
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mSelfUserInfo.isCreater) {
            isback = true;
            TIMMessage Tim = new TIMMessage();
            TIMCustomElem elem = new TIMCustomElem();
            elem.setData(HXUtil.outLiveChatRoomMessage(true).toString()
                               .getBytes());
            elem.setDesc(UNREAD);
            if (1 == Tim.addElement(elem)) {
                Notifier.showNormalMsg(mContext, "出错了...");
                return;
            }
            if (null != mConversation) {
                mConversation
                        .sendMessage(Tim, new TIMValueCallBack<TIMMessage>() {
                            @Override
                            public void onError(int i, String s) {
                                Logger.out("send message error" + i + ": " + s);
                            }


                            @Override
                            public void onSuccess(TIMMessage timMessage) {
                                addMessageToList(timMessage);
                            }
                        });
            }
        }
    }


    @Subscriber(tag = EventConstants.UPDATE_XGPUSHMANAGER)
    public void onEvente(String s) {
        if (!mSelfUserInfo.isCreater) {
            LayoutInflater inflater = LayoutInflater.from(AvActivity.this);
            View view = inflater.inflate(R.layout.top_popwindows, null);
            TextView dialog_title = (TextView) view
                    .findViewById(R.id.dialog_title);
            dialog_title.setText("" + s);
            final PopupWindow popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            popupWindow.setBackgroundDrawable(getResources()
                    .getDrawable(R.drawable.background_transparent));
            popupWindow.showAtLocation(av_video_glview, Gravity.TOP, 0, 0);

            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                }
            }, 2000);
        }
    }


    @Override
    protected void onDestroy() {
        ShareSDK.stopSDK(mContext);
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (fix != null) {
            fix.shutdownNow();
            fix = null;
        }
        if (sendgifttimer != null) {
            sendgifttimer.cancel();
            sendgifttimer = null;
        }
        if (msgListener != null) {
            TIMManager.getInstance().removeMessageListener(msgListener);
        }
    }


    private void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Utils.ACTION_SURFACE_CREATED);
        intentFilter.addAction(Utils.ACTION_VIDEO_SHOW);
        intentFilter.addAction(Utils.ACTION_SEMI_AUTO_RECV_CAMERA_VIDEO);
        intentFilter.addAction(Utils.ACTION_MEMBER_VIDEO_SHOW);
        intentFilter.addAction(Utils.ACTION_VIDEO_CLOSE);
        intentFilter.addAction(Utils.ACTION_ENABLE_CAMERA_COMPLETE);
        intentFilter.addAction(Utils.ACTION_SWITCH_CAMERA_COMPLETE);
        intentFilter.addAction(Utils.ACTION_INSERT_ROOM_TO_SERVER_COMPLETE);
        intentFilter.addAction(Utils.ACTION_INVITE_MEMBER_VIDEOCHAT);
        intentFilter.addAction(Utils.ACTION_MEMBER_CHANGE);
        intentFilter.addAction(Utils.ACTION_SHOW_VIDEO_MEMBER_INFO);
        intentFilter.addAction(Utils.ACTION_CLOSE_MEMBER_VIDEOCHAT);
        intentFilter.addAction(Utils.ACTION_CLOSE_ROOM_COMPLETE);
        intentFilter.addAction(Utils.ACTION_ROOM_CREATE_COMPLETE);//创建房间
        intentFilter.addAction(Utils.ACTION_HAVE_REMOTEVIDEO);

        intentFilter.addAction(Utils.ACTION_EXTRA_STOPRECORDD);
        getApplicationContext()
                .registerReceiver(mBroadcastReceiver, intentFilter);
        IntentFilter netIntentFilter = new IntentFilter();
        netIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        getApplicationContext()
                .registerReceiver(connectionReceiver, netIntentFilter);
    }


    private void initView() {
        if (mSelfUserInfo.isCreater) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("viewed_user_id", mSelfUserInfo.user_id);
            params.put("user_id", mSelfUserInfo.user_id);
            HXJavaNet
                    .post(HXJavaNet.url_user_infos, params, new AngelNetCallBack() {
                        @Override
                        public void onSuccess(final int status, final String json, final String readable_msg) {
                            messageInfo = Utils
                                    .jsonToBean(json, NewPerMesInfo.class);
                            if (null != messageInfo) {
                                mMeilizhiTextview.setText(
                                        messageInfo.ticket_amount + "");
                                int relation_type = messageInfo.relation_type;
                                if (relation_type == 1) {
                                    tv_isconcern.setVisibility(View.GONE);
                                }
                            }
                        }


                        @Override
                        public void onFailure(String msg) {
                            Logger.err(msg);
                        }
                    });
        }

        mWorkStatusQavTopright.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!booleanextviewTextView1) {
                    addmessageimbenji("2", mSelfUserInfo.user_id + "");
                    mHandler.sendEmptyMessageDelayed(6666, 5000);
                } else {
                    mWorkStatusQavTopleft.setText("");
                    if (mHandler != null) {
                        mHandler.removeMessages(6666);
                    }
                }
                booleanextviewTextView1 = !booleanextviewTextView1;
            }
        });
        if (mSelfUserInfo.isCreater) {
            mClockTextView.setText(mSelfUserInfo.nickname);
            mHostIdentifier = mSelfUserInfo.user_id + "";
            v_function.setVisibility(View.VISIBLE);
            //act_live_setting.setVisibility(View.VISIBLE);
            b_gift.setVisibility(View.GONE);
            b_beautify.setVisibility(View.VISIBLE);
            mWorkStatusQavTopright.setVisibility(View.VISIBLE);
        } else {
            mHostIdentifier =
                    getIntent().getIntExtra(Utils.EXTRA_SELF_IDENTIFIER, -1) +
                            "";
            String nickname = getIntent().getExtras().getString("nickname");
            mClockTextView.setText(nickname);
            charm_value = getIntent().getExtras().getInt("charm_value");
            mRecvIdentifier = "" + roomNum;
            groupId = getIntent().getExtras().getString(Utils.EXTRA_GROUP_ID);
            b_fenxiang.setVisibility(View.VISIBLE);
            v_function.setVisibility(View.GONE);
            b_beautify.setVisibility(View.GONE);
            b_gift.setVisibility(View.VISIBLE);
            //act_live_setting.setVisibility(View.GONE);
        }
        groupForPush = roomNum;
        if (mSelfUserInfo.Env == Utils.ENV_TEST) {
            groupForPush = 14010;
        }
        b_music.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //AVAudioCtrl avAudioCtrl2 = mQavsdkControl.getAVContext()
                //                                         .getAudioCtrl();
                if (!audio_jingyin) {
                    mQavsdkControl.registAudioDataCallback1();
                    Notifier.showShortMsg(mContext, "音乐模式开启");
                } else {
                    mQavsdkControl.unregistAudioDataCallback1();
                    Notifier.showShortMsg(mContext, "音乐模式关闭");
                }
                audio_jingyin = !audio_jingyin;
            }
        });
        act_live_setting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                LiveSettingDialog liveSettingDialog = new LiveSettingDialog();
                liveSettingDialog
                        .setStyle(DialogFragment.STYLE_NO_TITLE, R.style.add_dialog);
                Bundle bundle = new Bundle();
                //if (null != messageInfo) {
                //    bundle.putInt("relation", messageInfo.relation_type);
                //    bundle.putString("anchor_id", mHostIdentifier);
                //    bundle.putString("anchor_name", messageInfo.nickname);
                //    bundle.putString("anchor_avatar", messageInfo.photo);
                //}
                //liveSettingDialog.setArguments(bundle);
                liveSettingDialog.show(getSupportFragmentManager(), "private");
            }
        });
        if (mSelfUserInfo.type == 2 && BuildConfig.IS_ADMIN) {
            b_music1.setVisibility(View.VISIBLE);
            b_music2.setVisibility(View.VISIBLE);
            b_music3.setVisibility(View.VISIBLE);
            b_music4.setVisibility(View.VISIBLE);
            b_music7.setVisibility(View.VISIBLE);
            b_music8.setVisibility(View.VISIBLE);
            b_music1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater inflater = LayoutInflater
                            .from(AvActivity.this);
                    View view = inflater.inflate(R.layout.forbiduser, null);
                    RadioGroup group = (RadioGroup) view
                            .findViewById(R.id.radioGroup);
                    group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            chechedidd = checkedId;
                        }
                    });
                    Button queding = (Button) view.findViewById(R.id.queding);
                    Button quxiao = (Button) view.findViewById(R.id.quxiao);
                    final Dialog jinyanShowDialog = new AlertDialog.Builder(AvActivity.this, R.style.dialog1)
                            .create();

                    jinyanShowDialog.show();
                    jinyanShowDialog.setCanceledOnTouchOutside(true);
                    jinyanShowDialog.setContentView(view);

                    quxiao.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            jinyanShowDialog.dismiss();
                        }
                    });

                    queding.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AngelMenuDialog dialog = new AngelMenuDialog(getSelf(), "上传截图", new String[] {
                                    "从相册(直接传不兼容所有手机)",
                                    "从相册选择(需剪切兼容所有手机)" }, R.layout.item_menudialog, R.id.item_menu_tv, new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if (position == 0) {

                                        Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
                                        getAlbum.setType("image/*");
                                        try {
                                            startActivityForResult(getAlbum, 161);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Notifier.showLongMsg(mContext, "调用图库失败");
                                        }
                                    } else if (position == 1) {
                                        Intent intent = new Intent(mContext, PickImageActivity.class);
                                        intent.putExtra("mode", PickImageActivity.mode_select_from_gallery);
                                        startActivityForResult(intent, 0x2711);
                                    }
                                }
                            });
                            dialog.show();
                        }
                    });
                }
            });
            b_music2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ShowDialog jinyanShowDialog = new ShowDialog(getSelf(), "关房", "确定", "取消");
                    jinyanShowDialog.show();
                    jinyanShowDialog
                            .setClicklistener(new ShowDialog.ClickListenerInterface() {
                                @Override
                                public void doConfirm() {
                                    jinyanShowDialog.dismiss();
                                    HashMap<String, Object> params = new HashMap<>();
                                    params.put("room_num", roomNum);
                                    params.put("user_id", mHostIdentifier);
                                    HXJavaNet
                                            .post(HXJavaNet.url_liveClose, params, new NetCallBack(AvActivity.this) {
                                                @Override
                                                public void onSuccess(int ret_code, String ret_data, String msg) {
                                                    if (ret_code == 200) {
                                                        Notifier.showShortMsg(mContext, "关房成功");
                                                    }
                                                }


                                                @Override
                                                public void onFailure(String msg) {
                                                    Logger.err(msg);
                                                }
                                            });
                                }


                                @Override
                                public void doCancel() {
                                    jinyanShowDialog.dismiss();
                                }
                            });
                }
            });

            b_music6.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    addmessageimbenji("2", mSelfUserInfo.user_id + "");
                }
            });
            b_music3.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ShowDialog jinyanShowDialog = new ShowDialog(getSelf(), "隐藏房间", "隐藏", "取消");
                    jinyanShowDialog.show();
                    jinyanShowDialog
                            .setClicklistener(new ShowDialog.ClickListenerInterface() {
                                @Override
                                public void doConfirm() {

                                    HashMap<String, Object> jo = new HashMap<String, Object>();
                                    jo.put("room_num", roomNum);
                                    jo.put("user_id", mHostIdentifier);
                                    HXJavaNet
                                            .post(HXJavaNet.url_hideLiveInfo, jo, new NetCallBack(AvActivity.this) {
                                                @Override
                                                public void onSuccess(int ret_code, String ret_data, String msg) {
                                                    if (ret_code == 200) {
                                                        Notifier.showShortMsg(mContext, "隐藏房间成功");
                                                    }
                                                }


                                                @Override
                                                public void onFailure(String msg) {

                                                }
                                            });
                                    jinyanShowDialog.dismiss();
                                }


                                @Override
                                public void doCancel() {
                                    jinyanShowDialog.dismiss();
                                }
                            });
                }
            });
            b_music4.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ShowDialog jinyanShowDialog = new ShowDialog(getSelf(), "审核通过显示房间", "通过", "取消");
                    jinyanShowDialog.show();
                    jinyanShowDialog
                            .setClicklistener(new ShowDialog.ClickListenerInterface() {
                                @Override
                                public void doConfirm() {

                                    HashMap<String, Object> jo = new HashMap<>();
                                    jo.put("room_num", roomNum);
                                    jo.put("user_id", mHostIdentifier);
                                    HXJavaNet
                                            .post(HXJavaNet.url_showRoom, jo, new NetCallBack(AvActivity.this) {
                                                @Override
                                                public void onSuccess(int ret_code, String ret_data, String msg) {
                                                    if (ret_code == 200) {
                                                        Notifier.showShortMsg(mContext, "审核通过显示房间");
                                                    }
                                                }


                                                @Override
                                                public void onFailure(String msg) {

                                                }
                                            });
                                    jinyanShowDialog.dismiss();
                                }


                                @Override
                                public void doCancel() {
                                    jinyanShowDialog.dismiss();
                                }
                            });
                }
            });
            b_music7.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ShowDialog jinyanShowDialog = new ShowDialog(getSelf(), "更换主播头像", "确定", "取消");
                    jinyanShowDialog.show();
                    jinyanShowDialog
                            .setClicklistener(new ShowDialog.ClickListenerInterface() {
                                @Override
                                public void doConfirm() {

                                    HashMap<String, Object> jo = new HashMap<>();
                                    jo.put("user_id", mHostIdentifier);
                                    HXJavaNet
                                            .post(HXJavaNet.url_changePhoto, jo, new NetCallBack(AvActivity.this) {
                                                @Override
                                                public void onSuccess(int ret_code, String ret_data, String msg) {
                                                    if (ret_code == 200) {
                                                        Notifier.showShortMsg(mContext, "更换主播头像成功");
                                                    }
                                                }


                                                @Override
                                                public void onFailure(String msg) {

                                                }
                                            });
                                    jinyanShowDialog.dismiss();
                                }


                                @Override
                                public void doCancel() {
                                    jinyanShowDialog.dismiss();
                                }
                            });
                }
            });
            RxViewUtil.viewBindClick(b_music8, new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
                    FragmentManager fm = getFragmentManager();
                    Fragment closureDialog = fm
                            .findFragmentByTag("ClosureDialog");
                    if (closureDialog != null) {
                        fm.beginTransaction().remove(closureDialog);
                    }
                    ClosureDialog dialog = new ClosureDialog();
                    Bundle bundle = new Bundle();
                    bundle.putInt("handle_id", mSelfUserInfo.user_id);
                    bundle.putString("handle_identity", mSelfUserInfo.identity);
                    bundle.putInt("user_id", Integer.parseInt(mHostIdentifier));
                    bundle.putString("forbid_identity", mRecvIdentifier);
                    bundle.putString("nickname", mClockTextView.getText()
                                                               .toString());
                    bundle.putInt("room_num", roomNum);
                    bundle.putString("url", "");
                    dialog.setArguments(bundle);
                    dialog.show(getFragmentManager(), "ClosureDialog");
                }
            });
        }
        act_live_private_message.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                PrivateMessageDialog privateMessageDialog = new PrivateMessageDialog();
                privateMessageDialog
                        .setStyle(DialogFragment.STYLE_NO_TITLE, R.style.add_dialog);
                Bundle bundle = new Bundle();
                if (null != messageInfo) {
                    bundle.putInt("relation", messageInfo.relation_type);
                    bundle.putString("anchor_id", mHostIdentifier);
                    bundle.putString("anchor_name", messageInfo.nickname);
                    bundle.putString("anchor_avatar", messageInfo.photo);
                }
                privateMessageDialog.setArguments(bundle);
                privateMessageDialog
                        .show(getSupportFragmentManager(), "private");

                SharedPreferenceUtil.putLong("oflinemessage", 0);
                chat_msg_remind.setVisibility(View.GONE);
            }
        });

        //启动个人资料卡片
        hostHead.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showPerInfoCard(
                        mSelfUserInfo.user_id + "", mHostIdentifier + "", true);
            }
        });
        final WrapContentLinearLayoutManager lm_chat = new WrapContentLinearLayoutManager(mContext);
        lm_chat.setReverseLayout(true);
        final LinearLayoutManager lm_chat2 = new LinearLayoutManager(mContext);
        lm_chat2.setReverseLayout(true);
        list_chat2.setLayoutManager(lm_chat2);
        list_chat2.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = studio.archangel.toolkitv2.util.Util.getPX(4);
            }
        });
        final WrapContentLinearLayoutManager lm_chatgift = new WrapContentLinearLayoutManager(mContext);
        lm_chatgift.setReverseLayout(true);
        act_live_rv_gift.setLayoutManager(lm_chatgift);
        act_live_rv_gift.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = studio.archangel.toolkitv2.util.Util.getPX(4);
            }
        });

        MemberLinearLayoutManager memberManager = new MemberLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, true);
        list_chat.setLayoutManager(memberManager);
        list_chat.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = studio.archangel.toolkitv2.util.Util
                        .getPX(0.3f);
            }
        });
        final LinearLayoutManager lm_member = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        wrapContentLinearLayoutManager = new WrapContentLinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        list_member.setLayoutManager(wrapContentLinearLayoutManager);

        list_member.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.right = studio.archangel.toolkitv2.util.Util.getPX(7);
            }
        });

        //自动加载头像
        list_member.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastVisiblePosition = wrapContentLinearLayoutManager
                            .findLastVisibleItemPosition();
                    if (lastVisiblePosition >=
                            wrapContentLinearLayoutManager.getItemCount() - 1) {
                        getMemberInfo(mSelfUserInfo.user_id, roomNum, members
                                .size());
                    }
                }
            }
        });

        b_message.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelfUserInfo.forbid == 2) {
                    Notifier.showShortMsg(mContext, "你已经被全局禁言");
                } else {
                    showsoftKeyboard();
                }
            }
        });

        b_camera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onSwitchCamera();
            }
        });
        amr_hangup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mSelfUserInfo.isCreater) {
                    memberCloseAlertDialog();
                } else {
                    hostCloseAlertDialog();
                }
            }
        });

        b_fenxiang.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = LayoutInflater.from(AvActivity.this);
                View view = inflater.inflate(R.layout.show_fengxiangbg, null);
                View qqView = view.findViewById(R.id.shard_qq);
                View weChatView = view.findViewById(R.id.shard_wechat);
                View weiboView = view.findViewById(R.id.shard_weibo);
                View friendsterView = view.findViewById(R.id.ll_friendster);
                View qqSpaceView = view.findViewById(R.id.ll_qq_space);

                View qqView1 = view.findViewById(R.id.shard_qq1);
                View weChatView1 = view.findViewById(R.id.shard_wechat1);
                View weiboView1 = view.findViewById(R.id.shard_weibo1);
                View friendsterView1 = view.findViewById(R.id.ll_friendster1);
                View qqSpaceView1 = view.findViewById(R.id.ll_qq_space1);

                final PopupWindow popupWindow = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
                popupWindow.setTouchable(true);
                popupWindow.setTouchInterceptor(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                        // 这里如果返回true的话，touch事件将被拦截
                        // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                    }
                });
                view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                int popupWidth = view.getMeasuredWidth();
                int popupHeight = view.getMeasuredHeight();
                int[] location = new int[2];
                // 允许点击外部消失
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);
                popupWindow.setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.background_transparent));
                popupWindow.setAnimationStyle(R.style.popupWindow2);
                popupWindow
                        .setHeight(getKjwidth(R.dimen.show_gerenziliaobgb_height));
                popupWindow
                        .setWidth(getKjwidth(R.dimen.show_gerenziliaobgb_width));
                // 获得位置
                b_fenxiang.getLocationOnScreen(location);
                RotateAndTranslateAnimation ra = new RotateAndTranslateAnimation(-50, 0, 1000, 0, 0f, 10f);

                ra.setInterpolator(new DecelerateInterpolator((float) 0.5));
                //  ra.setInterpolator(new CycleInterpolator((float) 0.3));
                ra.setDuration(1000);
                //     weiboView.setAnimation(ra);

                //new BounceInterpolator()

                weiboView.startAnimation(ra);

                RotateAndTranslateAnimation ra1 = new RotateAndTranslateAnimation(-100, 0, 1000, 0, 0f, 10f);

                ra1.setInterpolator(new DecelerateInterpolator((float) 0.5));
                //  ra.setInterpolator(new CycleInterpolator((float) 0.3));
                ra1.setDuration(1000);
                ra1.setStartOffset(100);

                friendsterView.startAnimation(ra1);

                RotateAndTranslateAnimation ra2 = new RotateAndTranslateAnimation(-200, 0, 1000, 0, 0f, 10f);

                ra2.setInterpolator(new DecelerateInterpolator((float) 0.5));
                //  ra.setInterpolator(new CycleInterpolator((float) 0.3));
                ra2.setDuration(1000);
                ra2.setStartOffset(200);
                weChatView.startAnimation(ra2);

                RotateAndTranslateAnimation ra3 = new RotateAndTranslateAnimation(-200, 0, 1000, 0, 0f, 10f);
                ra3.setInterpolator(new DecelerateInterpolator((float) 0.5));
                //  ra.setInterpolator(new CycleInterpolator((float) 0.3));
                ra3.setDuration(1000);
                ra3.setStartOffset(400);
                qqView.startAnimation(ra3);

                RotateAndTranslateAnimation ra4 = new RotateAndTranslateAnimation(-200, 0, 1000, 0, 0f, 10f);
                ra4.setInterpolator(new DecelerateInterpolator((float) 0.5));
                //  ra.setInterpolator(new CycleInterpolator((float) 0.3));
                ra4.setDuration(1000);
                ra4.setStartOffset(600);
                qqSpaceView.startAnimation(ra4);
                popupWindow.showAtLocation(b_fenxiang, Gravity.NO_GRAVITY,
                        (location[0] + v.getWidth() / 2) - popupWidth / 2,
                        location[1] - popupHeight);

                platformActionListener = new PlatformActionListener() {
                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                        // 0分享到朋友圈，1分享到空间 2分享到微博 3分享到微信好友 4分享到qq好友
                        switch (shareType) {
                            case 0:
                                if (!shareWechatComon) {
                                    shareWechatComon = true;
                                    sendShareType();
                                }
                                String desWechatCommon = HXApp.getInstance()
                                                              .getUserInfo().nickname +
                                        " 分享了这个直播到微信朋友圈";
                                sendShareMsg(desWechatCommon, "share");
                                break;
                            case 1:
                                if (!shareQqSpace) {
                                    sendShareType();
                                    shareQqSpace = true;
                                }
                                String desQqSpace = HXApp.getInstance()
                                                         .getUserInfo().nickname +
                                        " 分享了这个直播到QQ空间";
                                sendShareMsg(desQqSpace, "share");
                                break;
                            case 2:
                                if (!shareSina) {
                                    shareSina = true;
                                    sendShareType();
                                }

                                String desSina = HXApp.getInstance()
                                                      .getUserInfo().nickname +
                                        " 分享了这个直播到新浪微博";
                                sendShareMsg(desSina, "share");
                                break;
                            case 3:
                                sendShareType();
                                String desWeChat = HXApp.getInstance()
                                                        .getUserInfo().nickname +
                                        " 分享了这个直播到微信";
                                sendShareMsg(desWeChat, "share");
                                break;
                            case 4:
                                sendShareType();
                                String desQq = HXApp.getInstance()
                                                    .getUserInfo().nickname +
                                        " 分享了这个直播到QQ";
                                sendShareMsg(desQq, "share");
                                break;
                        }
                    }


                    @Override
                    public void onError(Platform platform, int i, Throwable throwable) {

                    }


                    @Override
                    public void onCancel(Platform platform, int i) {

                    }
                };

                Bundle extras = getIntent().getExtras();
                final String identity = extras.getString("identity");
                final String nickname = extras.getString("nickname");
                weiboView1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shareType = 2;
                        SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
                        sp.setTitle(nickname + "(" + identity + ")" + "正在直播");
                        sp.setText(
                                "不在别处仰望，只在嘿秀欢畅！" + nickname + "正在直播，火速围观...");
                        sp.setImageUrl(HXApp.getInstance().getRoomCoverPath());
                        sp.setTitleUrl(getShareUrl(mHostIdentifier, roomNum, "SinaWeibo"));
                        Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                        weibo.share(sp);
                        weibo.setPlatformActionListener(platformActionListener);
                        popupWindow.dismiss();
                    }
                });
                weChatView1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shareType = 3;
                        Wechat.ShareParams wechat = new Wechat.ShareParams();
                        wechat.setTitle(
                                nickname + "(" + identity + ")" + "正在直播");
                        wechat.setText(
                                "不在别处仰望，只在嘿秀欢畅！" + nickname + "正在直播，火速围观...");
                        wechat.setImageUrl(HXApp.getInstance()
                                                .getRoomCoverPath());
                        wechat.setUrl(getShareUrl(mHostIdentifier, roomNum, "WechatSession"));
                        wechat.setShareType(Platform.SHARE_WEBPAGE);
                        Platform weixin = ShareSDK.getPlatform(Wechat.NAME);
                        weixin.share(wechat);
                        weixin.setPlatformActionListener(platformActionListener);
                        popupWindow.dismiss();
                    }
                });
                qqView1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shareType = 4;
                        QQ.ShareParams sp = new QQ.ShareParams();
                        sp.setTitle(nickname + "(" + identity + ")" + "正在直播");
                        sp.setTitleUrl(getShareUrl(mHostIdentifier, roomNum, "QQFriend"));
                        sp.setText(
                                "不在别处仰望，只在嘿秀欢畅！" + nickname + "正在直播，火速围观...");
                        sp.setImageUrl(HXApp.getInstance().getRoomCoverPath());
                        Platform qq = ShareSDK.getPlatform(QQ.NAME);
                        // 执行图文分享
                        qq.share(sp);
                        qq.setPlatformActionListener(platformActionListener);
                        popupWindow.dismiss();
                    }
                });
                friendsterView1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {//朋友圈
                        shareType = 0;
                        WechatMoments.ShareParams emmom = new WechatMoments.ShareParams();
                        emmom.setTitle(
                                nickname + "(" + identity + ")" + "正在直播");
                        emmom.setUrl(getShareUrl(mHostIdentifier, roomNum, "WechatTimeline"));
                        emmom.setText(
                                "不在别处仰望，只在嘿秀欢畅！" + nickname + "正在直播，火速围观...");
                        emmom.setImageUrl(HXApp.getInstance()
                                               .getRoomCoverPath());
                        emmom.setShareType(Platform.SHARE_WEBPAGE);
                        Platform qq = ShareSDK.getPlatform(WechatMoments.NAME);
                        // 执行图文分享
                        qq.share(emmom);
                        qq.setPlatformActionListener(platformActionListener);
                        popupWindow.dismiss();
                    }
                });
                qqSpaceView1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {//QQSpace
                        shareType = 1;
                        QZone.ShareParams qZone = new QZone.ShareParams();
                        qZone.setTitle(
                                nickname + "(" + identity + ")" + "正在直播");
                        qZone.setTitleUrl(getShareUrl(mHostIdentifier, roomNum, "QZone"));
                        qZone.setText(
                                "不在别处仰望，只在嘿秀欢畅！" + nickname + "正在直播，火速围观...");
                        qZone.setImageUrl(HXApp.getInstance()
                                               .getRoomCoverPath());
                        qZone.setShareType(Platform.SHARE_WEBPAGE);
                        Platform qq = ShareSDK.getPlatform(QZone.NAME);
                        // 执行图文分享
                        qq.share(qZone);
                        qq.setPlatformActionListener(platformActionListener);
                        popupWindow.dismiss();
                    }
                });
            }
        });
        b_gift.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v_gift_pad.getVisibility() == View.GONE) {
                    hideMsgIputKeyboard();
                    act_live_private_message.setVisibility(View.GONE);
                    v_gift_pad.setVisibility(View.VISIBLE);
                    amb_gift_send.setVisibility(View.VISIBLE);
                    b_message.setVisibility(View.INVISIBLE);
                    b_gift.setVisibility(View.INVISIBLE);
                    b_fenxiang.setVisibility(View.INVISIBLE);
                    list_chat.setVisibility(View.INVISIBLE);
                    if (frag_progress.getVisibility() == View.VISIBLE) {
                        if (null != sendgifttimer) {
                            sendgifttimer.cancel();
                        }
                        frag_progress.setVisibility(View.GONE);
                        amountTransfer = 0;
                        mill = 0;
                    }
                }
            }
        });
        b_function.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b_flash.getVisibility() == View.VISIBLE) {
                    b_flash.setVisibility(View.GONE);
                    b_camera.setVisibility(View.GONE);
                } else {
                    b_flash.setVisibility(View.VISIBLE);
                    b_camera.setVisibility(View.VISIBLE);
                }
            }
        });
        b_flash.setOnClickListener(new OnClickListener() {
            //闪光灯
            @Override
            public void onClick(View v) {
                func_flash_active = !func_flash_active;
                b_flash.setImageResource(func_flash_active
                                         ? R.drawable.icon_flash_off
                                         : R.drawable.icon_flash_on);

                AVVideoCtrl avvideoctrl = mQavsdkControl.getAVContext()
                                                        .getVideoCtrl();
                Camera.Parameters cameraPara = (Camera.Parameters) avvideoctrl
                        .getCameraPara();
                if (cameraTrue) {
                    cameraPara.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                } else {
                    cameraPara.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                }
                cameraTrue = !cameraTrue;
                avvideoctrl.setCameraPara(cameraPara);
            }
        });

        b_beautify.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                b_message.setVisibility(View.INVISIBLE);
                b_fenxiang.setVisibility(View.INVISIBLE);
                act_live_private_message.setVisibility(View.INVISIBLE);
                v_function.setVisibility(View.INVISIBLE);
                beautyPart.setVisibility(View.VISIBLE);
                b_beautify.setVisibility(View.GONE);
                //使用美颜功能
                boolean enableBeauty = AVVideoCtrl.isEnableBeauty();
                if (enableBeauty) {
                    beautyProgress
                            .setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                @Override
                                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                    mQavsdkControl.getAVContext().getVideoCtrl()
                                                  .inputBeautyParam(
                                                          9.0f * progress /
                                                                  100.0f);
                                }


                                @Override
                                public void onStartTrackingTouch(SeekBar seekBar) {

                                }


                                @Override
                                public void onStopTrackingTouch(SeekBar seekBar) {

                                }
                            });

                    seekBarClose.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            b_beautify.setVisibility(View.VISIBLE);
                            beautyPart.setVisibility(View.GONE);
                            b_message.setVisibility(View.VISIBLE);
                            b_fenxiang.setVisibility(View.VISIBLE);
                            act_live_private_message
                                    .setVisibility(View.VISIBLE);
                            v_function.setVisibility(View.VISIBLE);
                            //act_live_setting.setVisibility(View.VISIBLE);
                        }
                    });
                } else {
                    Notifier.showShortMsg(mContext, "您的手机不支持美颜");
                }
            }
        });
        gift_selector
                .setOnGiftSelectedListener(new GiftSelectorPage.OnGiftSelectedListener() {
                    @Override
                    public void onGiftSelected(GiftWrapper gift, int position) {
                        selected_gift = gift.gift;
                        //                giftcount = gift.count;
                        gift_image = gift.gift.select_animation_image;
                        if (gift.gift.type == 2) {
                            iv_species_sma
                                    .setImageResource(R.drawable.species_small);
                            hint_message.setText("主播可得" + gift.gift.price / 5 +
                                    "个嘿豆。送出后有机会获得500倍大奖");
                        } else {

                            iv_species_sma
                                    .setImageResource(R.drawable.zhuanshi);
                            int giftid = Integer.parseInt(gift.gift.gift_id);
                            if (giftid >= 150 && giftid < 200) {
                                hint_message.setText("主播不获得存在感");
                            } else {
                                hint_message.setText(
                                        "主播可获得" + gift.gift.price * 10 +
                                                "存在感哟");
                            }
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
                            amb_gift_send.setVisibility(View.VISIBLE);
                            //                    if(null!=view_liv_send_button.getAnimation()){
                            //                        view_liv_send_button.getAnimation().cancel();
                            //                    }
                        }
                    }
                });
        tv_account.setText(mSelfUserInfo.virtual_currency_amount + "");
        tv_species_account.setText(mSelfUserInfo.heidou_amount + "");
        view_liv_send_button.setOnClickListener(this);
        ll_give.setOnClickListener(this);
        ll_give2.setOnClickListener(this);
        ll_give3.setOnClickListener(this);
        ll_give4.setOnClickListener(this);
        //发送礼物
        amb_gift_send.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected_gift == null) {
                    Notifier.showNormalMsg(mContext, "还没有选择礼物哦~");
                    return;
                }
                final String gift_id = "";
                giftcount = 1;
                sendgiftattr = HXUtil
                        .createLiveChatGiftMessage(selected_gift, Integer
                                .parseInt(mMeilizhiTextview.getText()
                                                           .toString()), amountTransfer, giftcount, 0, 0, 0.0);
                int isGif = sendgiftattr.optJSONObject("command")
                                        .optJSONObject("gift").optInt("isGif");
                String gifImage = sendgiftattr.optJSONObject("command")
                                              .optJSONObject("gift")
                                              .optString("gif_image");

                if (isGif == 0) {
                    view_liv_send_button.setVisibility(View.VISIBLE);
                    fl_background.setVisibility(View.VISIBLE);
                    ll_give.setVisibility(View.VISIBLE);
                    ll_give2.setVisibility(View.VISIBLE);
                    ll_give3.setVisibility(View.VISIBLE);
                    ll_give4.setVisibility(View.VISIBLE);
                    CircleAnimation.startScaleAnimation(view_liv_send_button);
                    amb_gift_send.setVisibility(View.GONE);
                    isshowlianfa = true;
                    sendgifttimer = new CountDownTimer(20000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            mill = millisUntilFinished / 1000;
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
                            if (frag_progress.getVisibility() == View.GONE) {
                                amountTransfer = 0;
                                mill = 0;
                            }
                            amb_gift_send.setVisibility(View.VISIBLE);
                            view_liv_send_button.setVisibility(View.GONE);
                            fl_background.setVisibility(View.GONE);
                            ll_give.setVisibility(View.GONE);
                            ll_give2.setVisibility(View.GONE);
                            ll_give3.setVisibility(View.GONE);
                            ll_give4.setVisibility(View.GONE);
                        }
                    };
                    sendgifttimer.start();
                }
                sendgift(1);
            }
        });
        AVAudioCtrl audioCtrl = mQavsdkControl.getAVContext().getAudioCtrl();
        if (audioCtrl != null) {
            if (mSelfUserInfo.isCreater) {
                audioCtrl.enableMic(true);
            } else {
                if (mQavsdkControl.getAVContext() == null) {
                    finish();
                    return;
                }
                audioCtrl.enableMic(false);
            }
        }

        //默认不显示键盘
        getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ll_usercp.setOnClickListener(this);
        ll_recharge.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    rechargeDialog
                            .show(getSupportFragmentManager(), "recharge");
                }
            }
        });

        tv_isconcern.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, Object> para = new HashMap<>();
                para.put("user_id", mSelfUserInfo.user_id + "");
                para.put("passive_user_id", mHostIdentifier);
                HXJavaNet
                        .post(HXJavaNet.url_concern, para, new NetCallBack(AvActivity.this) {
                            @Override
                            public void onSuccess(int ret_code, String ret_data, String msg) {
                                super.onSuccess(ret_code, ret_data, msg);
                                if (ret_code == 200) {
                                    sendShareMsg(HXApp.getInstance()
                                                      .getUserInfo().nickname +
                                            " 关注了" +
                                            getIntent().getExtras()
                                                       .getString("nickname"), "attention");
                                    GetMessageUtil
                                            .chageSavaData(AvActivity.this, mHostIdentifier, true);
                                    messageInfo.relation_type = 1;
                                }
                            }
                        });
                tv_isconcern.setVisibility(View.GONE);
            }
        });

        int px = Utils.getPX(30);
        HXImageLoader
                .loadGifFromLocal(host_gift, R.drawable.suona_gift, px, px);
    }


    //管理员版本选择chechedidd
    int chechedidd = 1;


    //分享后发送分享类型给后台
    public void sendShareType() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("room_num", roomNum);
        params.put("shareType", shareType);
        HXJavaNet.post(HXJavaNet.url_shareRoom, params, null);
    }


    //连发礼物，当连发没结束隐藏礼物键盘时，还可以连发
    private void BurstsGift() {
        if (isshowlianfa) {
            if (mill >= 1) {
                int isgif = sendgiftattr.optJSONObject("command")
                                        .optJSONObject("gift").optInt("isGif");
                if (isgif == 1) {
                    return;
                }
                frag_progress.setVisibility(View.VISIBLE);
                isshowlianfa = false;
            } else {
                frag_progress.setVisibility(View.GONE);
                return;
            }
            String url = sendgiftattr.optJSONObject("command")
                                     .optJSONObject("gift")
                                     .optString("gift_image");
            int price = sendgiftattr.optJSONObject("command")
                                    .optJSONObject("gift").optInt("gift_price");
            HXImageLoader.loadGifImage(live_gift, url, Utils.getPX(45), Utils
                    .getPX(45));
            if (giftcount == 0) {
                giftcount = 1;
            }
            tv_lianchengcount.setText("x" + giftcount);
            tv_species.setText(price + "");
            frag_progress.setOnClickListener(this);
            sendgifttimer = new CountDownTimer(20000, 200) {
                @Override
                public void onTick(long millisUntilFinished) {
                    long ll = millisUntilFinished / 200;
                    int count = new Long(ll).intValue();
                    //                tv_time.setText(mill + "");
                    circleProgressbar.setProgress(count);
                }


                @Override
                public void onFinish() {
                    //                tv_time.setText("0");
                    amountTransfer = 0;
                    mill = 0;
                    frag_progress.setVisibility(View.GONE);
                }
            };
            sendgifttimer.start();
        }
    }


    //删除消息
    public void messagedel() {
        if (messages.size() > 200) {
            messages.remove(200);
            messages.remove(199);
            messages.remove(198);
        }
    }


    private String getShareUrl(String user_id, int roomNum, String type) {
        String url = null;
        url = "http://www.imheixiu.com/live/live.php?" + "user_id=" + user_id +
                "&room_id=" + roomNum + "&platform_type=" + type;
        //下面的是测试的
        //        url = "http://www-test.imheixiu.com/live/live.php?" + "user_id=" + user_id + "&room_id=" + roomNum + "&channel_id=" + channel_id + "&platform_type=" + type;
        Logger.out("url:  " + url);
        return url;
    }


    //发送分享信息
    public void sendShareMsg(String des, String name) {
        messagedel();
        JSONObject shareMessage = HXUtil.createLiveChatBasicMessage();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", name);
            jsonObject.put("des", des);
            shareMessage.put("command", jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TIMMessage Tim = new TIMMessage();
        TIMCustomElem elem = new TIMCustomElem();
        elem.setData(shareMessage.toString().getBytes());
        elem.setDesc(UNREAD);

        if (1 == Tim.addElement(elem)) {
            Notifier.showNormalMsg(mContext, "出错了...");
            return;
        }
        if (null == mConversation) {
            return;
        }
        mConversation.sendMessage(Tim, new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {
                Logger.out("send gift error" + i + ": " + s);
            }


            @Override
            public void onSuccess(TIMMessage timMessage) {

            }
        });
        try {
            shareMessage.put("type", 2);
            shareMessage.put("text", des);
            shareMessage.remove("command");
            messages.add(0, shareMessage);
            adapter_chat.notifyItemInserted(0);
            list_chat.smoothScrollToPosition(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Tim = null;
        elem = null;
    }


    //显示个人信息卡片
    private void showPerInfoCard(String user_id, String viewed_user_id, final boolean flag) {
        HashMap<String, Object> para = new HashMap<>();
        para.put("user_id", user_id);
        para.put("viewed_user_id", viewed_user_id);
        // TODO: 2016/5/17 请求网络可以优化的地方
        NetCallBack netCallBack = new NetCallBack(this) {
            @Override
            public void onSuccess(int ret_code, String ret_data, String msg) {
                super.onSuccess(ret_code, ret_data, msg);
                if (act.get() == null) {
                    return;
                }
                if (ret_code == 200) {
                    try {
                        JSONObject jo = new JSONObject(ret_data);
                        JSONObject user = new JSONObject();
                        user.put("user_id", jo.optString("user_id"));
                        user.put("identity", jo.optString("identity"));
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
                        user.put("send_heidou_amount", jo
                                .optInt("send_heidou_amount"));

                        if (flag) {
                            if (act.get().mSelfUserInfo.isCreater) {
                                act.get().newPerCard(user, true);
                            } else {
                                act.get().newPerCard(user, false);
                            }
                        } else {
                            //                            Xtgrade.mGrade(Integer.parseInt(jo.optString("grade")), iv_host_gard);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Notifier.showLongMsg(mContext, "请重试");
                }
            }


            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                Notifier.showLongMsg(mContext, "请重试");
            }
        };
        WeakReference<NetCallBack> netCallBackWeakReference = new WeakReference<>(netCallBack);
        HXJavaNet.post(HXJavaNet.url_getuserinfo, para, netCallBackWeakReference
                .get());
    }


    public Dialog DialognewPerCard;

    private boolean guazhufist;


    private void newPerCard(final JSONObject jo, boolean mbhost) {
        if (isFinishing()) {
            return;
        }
        String mphoto = "123";
        String nickname = "快乐的小逗比";
        String address = "无信息";
        String geren_guanzhu = "0";
        String fans_amount = "0";
        String send_out_vca = "0";
        String ticket_amount = "0";
        int gender, grade;
        String identity = "0";
        String relation_type = "0";
        final String user_id;
        final int relation;
        identity = jo.optString("identity");
        nickname = jo.optString("user_name");
        mphoto = jo.optString("user_avatar");
        user_id = jo.optString("user_id");
        address = jo.optString("address");
        geren_guanzhu = jo.optInt("send_heidou_amount") + "";//关注
        fans_amount = jo.optInt("fans_amount") + "";//粉丝
        send_out_vca = jo.optInt("send_out_vca") + "";//送出的货币
        ticket_amount = jo.optInt("ticket_amount") + "";
        relation_type = jo.optInt("relation_type") + "";
        gender = jo.optInt("gender");
        grade = jo.optInt("grade");
        relation = jo.optInt("relation_type");
        HXApp.getInstance().relation = relation;
        LayoutInflater inflater = LayoutInflater.from(AvActivity.this);
        View view = inflater.inflate(R.layout.show_gerenziliao, null);
        ImageView image_geder = (ImageView) view
                .findViewById(R.id.Image_gender);
        if (gender == 0) {
            image_geder.setImageResource(R.drawable.sex_man);
        } else {
            image_geder.setImageResource(R.drawable.sex_woman);
        }
        //等级
        SimpleDraweeView iv_grade1 = (SimpleDraweeView) view
                .findViewById(R.id.iv_grade1);
        TextView tv_grade1 = (TextView) view.findViewById(R.id.tv_grade1);
        Xtgrade.mXtgrade(grade, iv_grade1, tv_grade1);

        TextView geren_dizhi = (TextView) view.findViewById(R.id.geren_dizhi);
        geren_dizhi.setText(address);
        TextView password_text1 = (TextView) view
                .findViewById(R.id.tv_nickname);
        password_text1.setText(nickname);
        TextView geren_fuanzhu1 = (TextView) view
                .findViewById(R.id.geren_fuanzhu);
        geren_fuanzhu1.setText("" + Xtgrade.moneynumber(geren_guanzhu + ""));
        TextView geren_fensi = (TextView) view.findViewById(R.id.geren_fensi);
        geren_fensi.setText("" + Xtgrade.moneynumber(fans_amount + ""));
        TextView geren_songchu = (TextView) view
                .findViewById(R.id.geren_songchu);
        geren_songchu.setText("" + Xtgrade.moneynumber(send_out_vca + ""));
        geren_songchu.setText("" + Xtgrade.moneynumber(send_out_vca + ""));
        TextView geren_meilizhi = (TextView) view
                .findViewById(R.id.geren_meilizhi);
        geren_meilizhi.setText("" + Xtgrade.moneynumber(ticket_amount + ""));

        //设置头像
        SimpleDraweeView ImageView_top = (SimpleDraweeView) view
                .findViewById(R.id.ImageView_top_head);
        ImageView manager_mark = (ImageView) view
                .findViewById(R.id.manager_mark);
        HXImageLoader.loadImage(ImageView_top, mphoto, Utils.getPX(106), Utils
                .getPX(106));
        int px1 = Utils.getPX(106);
        HXImageLoader.loadImage(ImageView_top, mphoto, px1, px1);
        //id
        TextView identity_dizhi1 = (TextView) view
                .findViewById(R.id.identity_dizhi1);
        if (identity.equals("") || null == identity) {
            identity_dizhi1.setVisibility(View.GONE);
        }
        identity_dizhi1.setText("ID:" + identity);

        //关注
        final TextView tiaozhuan_gerenguanzhu = (TextView) view
                .findViewById(R.id.tiaozhuan_gerenguanzhu);
        final TextView tiaozhuan_gerenzhuye = (TextView) view
                .findViewById(R.id.tiaozhuan_gerenzhuye);

        if (DialognewPerCard != null) {
            //DialognewPerCard.dismiss();
        } else {
            DialognewPerCard = new AlertDialog.Builder(AvActivity.this, R.style.dialog1)
                    .create();
        }
        //     DialognewPerCard.getWindow().setWindowAnimations(R.style.popupWindow);  //添加动画

        DialognewPerCard.show();

        DialognewPerCard.setCanceledOnTouchOutside(true);

        if (isFinishing()) {
            return;
        }
        DialognewPerCard.show();
        DialognewPerCard.setContentView(view);

        //设置dialo宽高
        DialognewPerCard.getWindow()
                        .setLayout(getKjwidth(R.dimen.show_gerenziliao_width), getKjwidth(R.dimen.show_gerenziliao_height));

        DialognewPerCard
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (tiaozhuan_gerenguanzhu.getText().equals("已关注")) {

                            if (jo.optInt("relation_type") == 0) {
                                HashMap<String, Object> para = new HashMap<>();
                                para.put("user_id", mSelfUserInfo.user_id + "");
                                para.put("passive_user_id", user_id + "");

                                HXJavaNet
                                        .post(HXJavaNet.url_concern, para, new NetCallBack(AvActivity.this) {
                                            @Override
                                            public void onSuccess(int ret_code, String ret_data, String msg) {
                                                super.onSuccess(ret_code, ret_data, msg);
                                                if (ret_code == 200) {
                                                    try {
                                                        jo.putOpt("relation_type", "1");
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    GetMessageUtil
                                                            .chageSavaData(AvActivity.this,
                                                                    user_id +
                                                                            "", true);

                                                    tiaozhuan_gerenguanzhu
                                                            .setText("已关注");
                                                    HXApp.getInstance().relation = 1;
                                                } else {
                                                    Notifier.showShortMsg(mContext, "关注失败，请重试");
                                                }
                                            }


                                            @Override
                                            public void onFailure(String msg) {
                                                Notifier.showShortMsg(mContext, "关注失败，请重试");
                                            }
                                        });
                            } else {
                                return;
                            }
                        } else {
                            if (jo.optInt("relation_type") == 1) {
                                HashMap<String, Object> para = new HashMap<>();
                                para.put("user_id", mSelfUserInfo.user_id + "");
                                para.put("passive_user_id", user_id + "");
                                HXJavaNet
                                        .post(HXJavaNet.url_unconcern, para, new NetCallBack(AvActivity.this) {
                                            @Override
                                            public void onSuccess(int ret_code, String ret_data, String msg) {
                                                super.onSuccess(ret_code, ret_data, msg);
                                                if (ret_code == 200) {
                                                    GetMessageUtil
                                                            .chageSavaData(AvActivity.this,
                                                                    user_id +
                                                                            "", false);
                                                    HXApp.getInstance().relation = 0;
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });
        //设置关闭
        ImageView password_text = (ImageView) view
                .findViewById(R.id.image_finsh_log);
        password_text.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                DialognewPerCard.dismiss();
            }
        });

        //头像背景
        final SimpleDraweeView mrelativeLayout = (SimpleDraweeView) view
                .findViewById(R.id.xcroundrectimageview1);

        RoundingParams roundingParams = new RoundingParams();
        int px = Utils.getPX(14);
        roundingParams.setCornersRadii(px, px, 0f, 0f);
        mrelativeLayout.getHierarchy().setRoundingParams(roundingParams);

        HXImageLoader.loadBlurImage(mrelativeLayout, jo
                .optString("user_avatar"), Utils.getPX(280), Utils.getPX(144));

        final View tiaozhuan_gerenzhuyeview = (View) view
                .findViewById(R.id.tiaozhuan_gerenguanzhuview);
        final View tiaozhuan_siliao = view.findViewById(R.id.tiaozhuan_siliao);
        final View tiaozhuan_gerenzhuyeview1 = (View) view
                .findViewById(R.id.tiaozhuan_gerenguanzhuview1);

        if (relation_type.equals("1")) {
            tiaozhuan_gerenguanzhu.setText("已关注");
        }
        final String finalNickname = nickname;
        final String finamphoto = mphoto;

        //禁言
        ImageView modifyGroupSetSilence = (ImageView) view
                .findViewById(R.id.modifyGroupSetSilence);

        //判断是不是主播自己点的关注；

        if (user_id.equals(mSelfUserInfo.user_id + "") &&
                !mSelfUserInfo.isCreater) {
            mbhost = true;
        }

        if (!mbhost) {
            tiaozhuan_gerenguanzhu.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tiaozhuan_gerenguanzhu.getText().equals("已关注")) {
                        tiaozhuan_gerenguanzhu.setText("关注");
                    } else {

                        tiaozhuan_gerenguanzhu.setText("已关注");

                        if (!guazhufist) {
                            if (user_id.equals(mHostIdentifier)) {
                                sendShareMsg(HXApp.getInstance()
                                                  .getUserInfo().nickname +
                                        " 关注了" +
                                        finalNickname, "attention");
                                guazhufist = true;
                            }
                        }
                    }
                }
            });
        } else {
            modifyGroupSetSilence.setVisibility(View.GONE);
            tiaozhuan_gerenzhuyeview.setVisibility(View.GONE);
            tiaozhuan_gerenguanzhu.setVisibility(View.GONE);
            tiaozhuan_gerenzhuyeview1.setVisibility(View.GONE);
            tiaozhuan_siliao.setVisibility(View.GONE);
        }

        //设置权限
        modifyGroupSetSilence.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                DialognewPerCard.dismiss();
                pw = new showPLPupwindows(ctx, v);
                if (user_id != null) {
                    if (mSelfUserInfo.isCreater) {
                        pw.showPopupWindow1(user_id, finalNickname, groupId, mConversation, mHandler);
                    } else if (mlistadministart != null && mlistadministart
                            .contains(mSelfUserInfo.user_id + "")) {
                        pw.showPopupWindow1(user_id, finalNickname, groupId, mConversation, mHandler);
                    } else {
                        pw.showPopupWindow1(user_id, finalNickname, mConversation, mHandler);
                    }
                }
            }
        });

        tiaozhuan_siliao.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DialognewPerCard.dismiss();
                HXApp.getInstance().userid = Integer.parseInt(user_id);
                HXApp.getInstance().user_name = finalNickname;
                HXApp.getInstance().user_avatar = finamphoto;
                ChatMessageDialog chatMessageDialog = new ChatMessageDialog();
                chatMessageDialog
                        .setStyle(DialogFragment.STYLE_NO_TITLE, R.style.add_dialog);
                chatMessageDialog.show(getSupportFragmentManager(), "private");
            }
        });

        tiaozhuan_gerenzhuye.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DialognewPerCard.dismiss();
                Intent intent = new Intent(ctx, FriendMessageActivity.class);
                intent.putExtra("viewed_user_id", Integer.parseInt(user_id));
                startActivity(intent);
            }
        });
    }


    //获取账户钱信息
    void getUserAccount() {
        int user_id = mSelfUserInfo.user_id;
        if (user_id == 0) {
            return;
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("viewed_user_id", user_id);
        params.put("user_id", user_id);
        HXJavaNet.post(HXJavaNet.url_user_infos, params, new NetCallBack(this) {
            @Override
            public void onSuccess(int ret_code, String ret_data, String msg) {
                super.onSuccess(ret_code, ret_data, msg);
                try {
                    JSONObject jo = new JSONObject(ret_data);
                    int current = jo.optInt("virtual_currency_amount");
                    tv_account.setText(current + "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    // private BaseItemAnimator mAnimator;
    private void initTIMGroup() {
        Logger.out("initTIMGroup groupId" + groupId);
        if (groupId != null) {
            mConversation = TIMManager.getInstance()
                                      .getConversation(TIMConversationType.Group, groupId);
            //发消息通知大家 自己上线了

            if (!mSelfUserInfo.isCreater) {
                HashMap<String, Object> jo = new HashMap<>();
                jo.put("room_num", roomNum);
                jo.put("user_id", mSelfUserInfo.user_id);
                NetCallBack netCallBack = new NetCallBack(this) {

                    @Override
                    public void onSuccess(int ret_code, String ret_data, String msg) {
                        super.onSuccess(ret_code, ret_data, msg);
                        if (act.get() == null) {
                            return;
                        }
                        if (ret_code == 200) {
                            try {
                                isEnterroominterface = true;
                                JSONObject jo = new JSONObject(ret_data);
                                String channel_id = jo.optString("channel_id");
                                if (channel_id != null) {
                                    channelID = channel_id;
                                }
                                like_count = jo.optInt("live_praise_num");
                                perso_num_count = jo.optInt("live_viewing_num");
                                perso_num.setText(perso_num_count + "观众");
                                groupId = jo.optString("live_group_id");
                                onMemberEnter();
                                mMeilizhiTextview
                                        .setText(jo.getInt("charm_value") + "");
                                if (jo.optInt("heart_user_id") == 0) {
                                    act.get().heart_user_id = 0;
                                } else {
                                    act.get().heart_user_id = jo
                                            .optInt("heart_user_id");
                                }
                                act.get().mHostIdentifier =
                                        jo.getInt("live_user_id") + "";
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                WeakReference<NetCallBack> netCallBackWeakReference = new WeakReference<>(netCallBack);
                HXJavaNet
                        .post(HXJavaNet.url_enter_room, jo, netCallBackWeakReference
                                .get());
            }

            Logger.out("initTIMGroup mConversation" + mConversation);
        } else {
            finish();
            return;
        }
        mSystemConversation = TIMManager.getInstance()
                                        .getConversation(TIMConversationType.System, "");

        TIMManager.getInstance().addMessageListener(msgListener);

        list_chat2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (!mSelfUserInfo.isCreater) {
                        onSendPraise(issend);//点赞
                    }
                }
                hideGifPad();
                return false;
            }
        });
        act_live_rv_gift.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (!mSelfUserInfo.isCreater) {
                        onSendPraise(issend);//点赞
                    }
                }
                hideGifPad();
                return false;
            }
        });
        list_chat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    isroll = true;
                    if (!mSelfUserInfo.isCreater) {
                        onSendPraise(issend);//点赞
                    }
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    isroll = true;
                } else if (event.getAction() == MotionEvent.ACTION_SCROLL) {
                    isroll = true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    mHandler.sendEmptyMessageDelayed(chatscoll, 6000);
                }

                hideGifPad();
                return false;
            }
        });
        list_chat2.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (((LinearLayoutManager) view.getLayoutManager())
                                .findFirstVisibleItemPosition() == 0 &&
                                !mIsLoading && bMore) {
                            bNeverLoadMore = false;
                            mIsLoading = true;
                            mLoadMsgNum += MAX_PAGE_NUM;
                        }
                        break;
                }
            }


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            }
        });
        list_chat.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (((LinearLayoutManager) view.getLayoutManager())
                                .findFirstVisibleItemPosition() == 0 &&
                                !mIsLoading && bMore) {
                            bNeverLoadMore = false;
                            mIsLoading = true;
                            mLoadMsgNum += MAX_PAGE_NUM;
                            //							getMessage();
                        }
                        break;
                }
            }


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            }
        });

        time = System.currentTimeMillis() / 1000;
    }


    private void hideGifPad() {
        hideMsgIputKeyboard();
        v_gift_pad.setVisibility(View.GONE);
        view_liv_send_button.setVisibility(View.GONE);
        fl_background.setVisibility(View.GONE);
        ll_give.setVisibility(View.GONE);
        ll_give2.setVisibility(View.GONE);
        ll_give3.setVisibility(View.GONE);
        ll_give4.setVisibility(View.GONE);
        b_message.setVisibility(View.VISIBLE);
        list_chat.setVisibility(View.VISIBLE);
        act_live_private_message.setVisibility(View.VISIBLE);

        if (!mSelfUserInfo.isCreater) {
            b_fenxiang.setVisibility(View.VISIBLE);
            b_gift.setVisibility(View.VISIBLE);
        }

        amr_hangup.setVisibility(View.VISIBLE);
        BurstsGift();
    }


    private void destroyTIM() {
        Logger.out("WL_DEBUG onDestroy");
        TIMManager.getInstance().removeMessageListener(msgListener);
        if (groupId != null && mIsSuccess) {
            if (mSelfUserInfo.isCreater) {
                TIMGroupManager.getInstance()
                               .deleteGroup(groupId, new TIMCallBack() {
                                   @Override
                                   public void onError(int i, String s) {
                                       Logger.out(
                                               "quit group error " + i + " " +
                                                       s);
                                   }


                                   @Override
                                   public void onSuccess() {
                                       Logger.out("delete group success");
                                   }
                               });
            } else {
                TIMGroupManager.getInstance()
                               .quitGroup(groupId, new TIMCallBack() {
                                   @Override
                                   public void onError(int i, String s) {
                                       Logger.out(
                                               "quit group error " + i + " " +
                                                       s);
                                   }


                                   @Override
                                   public void onSuccess() {
                                       Logger.out("quit group success");
                                   }
                               });
            }
            TIMManager.getInstance()
                      .deleteConversation(TIMConversationType.Group, groupId);
        }
        groupId = null;
    }


    private void joinGroup() {
        TIMGroupManager.getInstance().applyJoinGroup(groupId,
                "申请加入" + groupId, new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        Log.e("TTTTT", "加群失败" + "i" + i + "s" + s);
                        TIMGroupManager.getInstance()
                                       .quitGroup(groupId, new TIMCallBack() {
                                           @Override
                                           public void onError(int i, String s) {
                                               //Notifier.showShortMsg
                                               //            (getApplication(), "直播已经结束了");
                                           }


                                           @Override
                                           public void onSuccess() {
                                               joinGroup();
                                           }
                                       });
                    }


                    @Override
                    public void onSuccess() {
                        Logger.out("applyJpoinGroup success");
                    }
                });
    }


    private Long onrevoPiraise = 0L;


    private synchronized void refreshChat2(List<TIMMessage> msg) {
        Logger.out("refreshChat 0000 " + msg);
        if (msg == null) {
            return;
        }
        messagedel();
        if (msg.size() > 0) {
            mConversation.setReadMessage(msg.get(0));
            Logger.out("refreshChat readMessage " + msg.get(0).timestamp());
        }
        if (!bNeverLoadMore && (msg.size() < mLoadMsgNum)) {
            bMore = false;
        }
        for (int i = msg.size() - 1; i >= 0; i--) {
            TIMMessage currMsg = msg.get(i);
            Logger.out(currMsg);
            for (int j = 0; j < currMsg.getElementCount(); j++) {
                if (currMsg.getElement(j) == null) {
                    continue;
                }
                final TIMElem elem = currMsg.getElement(j);
                TIMElemType type = elem.getType();
                Logger.out("refreshChat2 type " + type);
                //系统消息
                if (type == TIMElemType.GroupSystem) {
                    if (!groupId
                            .equals(((TIMGroupSystemElem) elem).getGroupId() +
                                    "")) {
                        continue;
                    }
                    if (TIMGroupSystemElemType.TIM_GROUP_SYSTEM_DELETE_GROUP_TYPE ==
                            ((TIMGroupSystemElem) elem).getSubtype()) {
                        mHandler.sendEmptyMessage(IM_HOST_LEAVE);
                        LEVAE_MODE = true;
                    } else {
                        byte[] b = ((TIMGroupSystemElem) elem).getUserData();
                        try {
                            String s = new String(b, "UTF-8");
                            final JSONObject attr = new JSONObject();
                            final JSONObject user = new JSONObject();
                            final JSONObject jo = new JSONObject(s);
                            if (!jo.optString("operation", "0x000")
                                   .equals("0x000") && "creater_leave"
                                    .equals(jo.getString("operation"))) {
                                try {
                                    user.put("user_id", "123");
                                    user.put("user_name", "");
                                    user.put("user_is_vip", 1);
                                    attr.put("user", user);
                                    attr.put("type", 2);
                                    attr.put("text", jo.getString("msg"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                messagedel();
                                list_chat.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        messages.add(0, attr);
                                        mHandler.sendEmptyMessage(chatmessage);
                                        onCloseVideo(false, true);
                                        liveCloseInfo(false);
                                    }
                                });
                                return;
                            }
                            //operation是异常消息处理
                            if (!jo.optString("operation", "0x000")
                                   .equals("0x000") && "normal".equals(jo
                                    .getString("operation"))) {
                                if (heart_user_id != jo.optInt("liveUserld")) {
                                    return;
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        perso_num_count--;
                                        if (null == members) {
                                            return;
                                        }
                                        boolean remove = adapter_member
                                                .remove(jo);
                                        if (remove) {
                                            perso_num_count--;
                                        }
                                    }
                                });

                                return;
                            }
                            if ("room_enter".equals(jo.getJSONObject("command")
                                                      .getString("name"))) {
                                JSONObject joo = jo.optJSONObject("user");
                                final boolean addmem = adapter_member
                                        .add(joo, false);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        perso_num_count++;
                                        perso_num.setText(
                                                perso_num_count + "观众");
                                        if (members == null) {
                                            return;
                                        }
                                        msg_enter.setVisibility(View.VISIBLE);
                                        String des = jo.optJSONObject("command")
                                                       .optString("des");
                                        msg_enter.setText(des);
                                        long timeMillis = System
                                                .currentTimeMillis();
                                        mHandler.sendEmptyMessageDelayed(100, 2000);
                                        if (missTime == 0) {
                                        } else {
                                            if (timeMillis - missTime < 2100) {
                                                mHandler.removeMessages(100);
                                                mHandler.sendEmptyMessageDelayed(100, 2000);
                                            }
                                        }
                                        missTime = timeMillis;
                                    }
                                });
                                return;
                            }
                            if ("like".equals(jo.getJSONObject("command")
                                                .getString("name"))) {
                                try {
                                    user.put("user_id", jo.getJSONObject("user")
                                                          .getString("user_id"));
                                    user.put("user_avatar", jo
                                            .getJSONObject("user")
                                            .getString("user_avatar"));
                                    user.put("user_name", jo
                                            .getJSONObject("user")
                                            .getString("user_name"));
                                    user.put("grade", jo.getJSONObject("user")
                                                        .getString("grade"));
                                    user.put("user_is_vip", jo
                                            .getJSONObject("user")
                                            .getString("user_is_vip"));
                                    attr.put("user", user);
                                    attr.put("text", "点亮了❤️");
                                    attr.put("type", 0);
                                    messagedel();

                                    list_chat.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            messages.add(0, attr);
                                            mHandler.sendEmptyMessage(chatmessage);
                                            ++like_count;
                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                return;
                            }
                            try {
                                user.put("user_id", "123");
                                user.put("user_name", "");
                                user.put("user_is_vip", 1);
                                attr.put("user", user);
                                attr.put("text", s);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            list_chat.post(new Runnable() {
                                @Override
                                public void run() {
                                    messages.add(0, attr);
                                    mHandler.sendEmptyMessage(chatmessage);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }


                //其他群消息过滤
                if (null == currMsg.getConversation() || "".equals(groupId) ||
                        null == groupId) {
                    return;
                }
                if (!groupId.equals(currMsg.getConversation().getPeer())) {
                    continue;
                }
                //自定义
                if (type == TIMElemType.Custom) {
                    String text = null;
                    try {
                        text = new String(((TIMCustomElem) elem)
                                .getData(), "UTF-8");
                        Logger.out("custom 1data" + text);
                        final JSONObject attr = new JSONObject(text);
                        Logger.out("attr:" + attr.toString(4));
                        final JSONObject user = attr.optJSONObject("user");
                        if (attr.has("command")) {
                            final JSONObject command = attr
                                    .optJSONObject("command");
                            String action = command.optString("name");
                            if (action.isEmpty()) {
                                return;
                            }
                            if (action.equalsIgnoreCase("gift_send")) {//礼物
                                String newgift_id = attr
                                        .getJSONObject("command")
                                        .getJSONObject("gift")
                                        .getString("gift_id");
                                String newuser_id = attr.getJSONObject("user")
                                                        .getString("user_id");
                                int isGif = attr.getJSONObject("command")
                                                .getJSONObject("gift")
                                                .getInt("isGif");
                                int giftPrice = attr.getJSONObject("command")
                                                    .getJSONObject("gift")
                                                    .getInt("gift_price");
                                String giftImage = attr.getJSONObject("command")
                                                       .getJSONObject("gift")
                                                       .getString("gif_image");
                                if (isGif == 1 && !giftImage.isEmpty()) {
                                    localqueue.offer(attr);
                                    if (localqueue.size() == 1) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                GifAnimation();
                                            }
                                        });
                                    }
                                } else {
                                    if (giftPrice < 100) {
                                        if (sifegiftid.equals(newgift_id)) {
                                            queuebig.offer(attr);
                                            sifegiftid = newgift_id;
                                        } else {
                                            queue.offer(attr);
                                            sifegiftid = newgift_id;
                                        }
                                    } else {
                                        queuebig.offer(attr);
                                    }
                                }
                            } else if (action.equalsIgnoreCase("silence")) {
                                final JSONObject jsonObject = new JSONObject();
                                jsonObject.put("user", user);
                                jsonObject.put("type", 2);
                                jsonObject
                                        .put("text", command.optString("des"));
                                if ((mSelfUserInfo.user_id + "")
                                        .equals(command.optString("user_id"))) {
                                    if ("2".equals(command.optString("type"))) {

                                        mSelfUserInfo.forbid = 2;
                                        mHXApp.setUserInfo(mSelfUserInfo);
                                        TIMManager.getInstance().logout();
                                        //关闭房间
                                        onCloseVideo(false, false);

                                    }
                                }

                                list_chat.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        messages.add(0, jsonObject);
                                        mHandler.sendEmptyMessage(chatmessage);
                                    }
                                });
                                command.optString("");
                            } else if (action.equalsIgnoreCase("editManager")) {
                                final JSONObject jsonObject = new JSONObject();
                                jsonObject.put("user", user);
                                jsonObject.put("type", 2);
                                jsonObject.put("text", command
                                        .optString("message"));
                                list_chat.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (command.optString("type")
                                                   .equals("2")) {

                                        } else {
                                            messages.add(0, jsonObject);
                                            mHandler.sendEmptyMessage(chatmessage);
                                        }
                                    }
                                });

                                if (command.optString("des").equals("admin")) {
                                    mlistadministart
                                            .add(command.optString("user_id"));
                                } else {

                                    if (mlistadministart.contains(command
                                            .optString("user_id"))) {
                                        mlistadministart.remove(command
                                                .optString("user_id"));
                                    }
                                }
                            } else if (action
                                    .equalsIgnoreCase("network_status")) {

                                if (command.optString("type").equals("0")) {

                                } else if (command.optString("type")
                                                  .equals("1")) {

                                } else if (command.optString("type")
                                                  .equals("2")) {
                                    if (mSelfUserInfo.isCreater) {
                                        String userid = user
                                                .optString("user_id");
                                        Message m = new Message();
                                        m.what = 5555;
                                        m.obj = userid + "";
                                        mHandler.sendMessage(m);
                                        return;
                                    } else {
                                        if (command.optString("user_id")
                                                   .equals(mSelfUserInfo.user_id +
                                                           "")) {
                                            final String desc = command
                                                    .optString("desc");
                                            mWorkStatusQavTopleft
                                                    .post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            mWorkStatusQavTopleft
                                                                    .setText(desc);
                                                        }
                                                    });
                                        }
                                        return;
                                    }
                                } else {
                                    return;
                                }
                            } else if (action.equalsIgnoreCase("share")) {//分享信息
                                final JSONObject jsonObject = new JSONObject();
                                jsonObject.put("user", user);
                                jsonObject.put("type", 2);
                                jsonObject
                                        .put("text", command.optString("des"));
                                list_chat.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        messages.add(0, jsonObject);
                                        mHandler.sendEmptyMessage(chatmessage);
                                    }
                                });
                            } else if (action.equalsIgnoreCase("attention")) {
                                final JSONObject jsonObject = new JSONObject();
                                jsonObject.put("user", user);
                                jsonObject.put("type", 2);
                                jsonObject
                                        .put("text", command.optString("des"));
                                list_chat.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        messages.add(0, jsonObject);
                                        mHandler.sendEmptyMessage(chatmessage);
                                    }
                                });
                            } else if (action
                                    .equalsIgnoreCase("trumpet_send")) {
                                //添加弹幕消息
                                danmakuQueue.offer(text);
                                attr.put("text", command.optString("des"));
                                list_chat.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        messages.add(0, attr);
                                        mHandler.sendEmptyMessage(chatmessage);
                                    }
                                });
                            } else if (action.equalsIgnoreCase("like")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        like_count++;
                                        if ((System.currentTimeMillis() -
                                                     onrevoPiraise) > 500) {
                                            zan.addFavor();
                                            spitCount++;
                                            if (spitCount * 10 > 90) {
                                                spitCount = 0;
                                                zan.addFavorbig();
                                            }
                                            onrevoPiraise = System
                                                    .currentTimeMillis();
                                        }
                                    }
                                });
                            } else if (action.equalsIgnoreCase("room_enter")) {
                                if (members == null) {
                                    return;
                                }

                                if (user.optInt("type") != 2) {
                                    final boolean add = adapter_member
                                            .add(user, true);
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (command
                                                .optInt("WatchingUserCount") ==
                                                0) {
                                            perso_num_count++;
                                        } else {
                                            perso_num_count = command
                                                    .optInt("WatchingUserCount");
                                        }
                                        perso_num.setText(
                                                perso_num_count + "观众");
                                        if (mSelfUserInfo.isCreater) {
                                            int usertype = user.optInt("type");
                                            if (usertype == 2) {
                                                TIMGroupManager.getInstance()
                                                               .modifyGroupMemberInfoSetRole(
                                                                       groupId +
                                                                               "",
                                                                       user.optInt("user_id") +
                                                                               "", TIMGroupMemberRoleType.Admin, new TIMCallBack() {
                                                                           @Override
                                                                           public void onError(int i, String s) {
                                                                           }


                                                                           @Override
                                                                           public void onSuccess() {
                                                                               TIMMessage Nmsg = new TIMMessage();
                                                                               TIMCustomElem ce = new TIMCustomElem();
                                                                               ce.setData((HXUtil.createadminChatsilenceMessage(
                                                                                       user.optString("user_name") +
                                                                                               "已被提升为管理员",
                                                                                       user.optInt("user_id") +
                                                                                               "", "2")
                                                                                                 .toString()
                                                                                                 .getBytes()));
                                                                               if (Nmsg.addElement(ce) !=
                                                                                       0) {
                                                                                   return;
                                                                               }
                                                                               mConversation
                                                                                       .sendMessage(Nmsg, new TIMValueCallBack<TIMMessage>() {
                                                                                           @Override
                                                                                           public void onError(int i, String s) {
                                                                                               if (i ==
                                                                                                       85) {
                                                                                                   handler.sendEmptyMessage(AvActivity.ERROR_MESSAGE_TOO_LONG);
                                                                                               } else if (
                                                                                                       i ==
                                                                                                               6011) {
                                                                                                   handler.sendEmptyMessage(AvActivity.ERROR_ACCOUNT_NOT_EXIT);
                                                                                               }
                                                                                           }


                                                                                           @Override
                                                                                           public void onSuccess(TIMMessage timMessage) {

                                                                                           }
                                                                                       });
                                                                           }
                                                                       });
                                            }
                                        }

                                        if (user.optInt("type") == 2) {
                                            //管理员谨防不现实谨防消息


                                            return;
                                        }
                                        final String des = command
                                                .optString("des");
                                        int grade = user.optInt("grade");
                                        try {
                                            JSONObject jsonObject = new JSONObject();
                                            jsonObject.put("grade", grade);
                                            jsonObject
                                                    .put("enter_message", des);
                                            //判断是否有活动
                                            if (command
                                                    .optInt("animationType") ==
                                                    1) {
                                                //显示进房通知
                                                if (localendterRoom.size() <=
                                                        1) {
                                                    localendterRoom
                                                            .offer(jsonObject);
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            //
                                                        }
                                                    });
                                                } else {
                                                    localendterRoom
                                                            .offer(jsonObject);
                                                }
                                            } else {
                                                if (grade >
                                                        HXApp.getInstance()
                                                                .configMessage.enter_room_grade_limit) {
                                                    if (grade >= 10) {
                                                        isenterMessage = false;
                                                        if (localendterRoom
                                                                .size() <= 1) {
                                                            localendterRoom
                                                                    .offer(jsonObject);
                                                            enterRoomAnimation();
                                                        } else {
                                                            localendterRoom
                                                                    .offer(jsonObject);
                                                        }
                                                    } else {
                                                        if (isenterMessage) {
                                                            if (b_gift
                                                                    .getVisibility() ==
                                                                    View.GONE) {
                                                                msg_enter
                                                                        .setVisibility(View.VISIBLE);
                                                                msg_enter
                                                                        .setText(des);
                                                                long timeMillis = System
                                                                        .currentTimeMillis();
                                                                mHandler.sendEmptyMessageDelayed(100, 2000);
                                                                if (missTime ==
                                                                        0) {
                                                                    //mHandler.sendEmptyMessageDelayed(100, 2000);
                                                                } else {
                                                                    if (timeMillis -
                                                                            missTime <
                                                                            2100) {
                                                                        mHandler.removeMessages(100);
                                                                        mHandler.sendEmptyMessageDelayed(100, 2000);
                                                                    }
                                                                }
                                                                missTime = timeMillis;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            } else if (action
                                    .equalsIgnoreCase("room_exit")) {//用户退出直播间，主播界面刷新界面
                                if (null == members) {
                                    return;
                                }
                                boolean remove = adapter_member.remove(user);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (perso_num_count == 0) {
                                            return;
                                        }
                                        --perso_num_count;
                                        perso_num.setText(
                                                perso_num_count + "观众");
                                    }
                                });
                            } else if (action
                                    .equalsIgnoreCase("author_state")) {//直播重新进入
                                final JSONObject attrr = new JSONObject();
                                attrr.put("user", attr.getJSONObject("user"));
                                attrr.put("text", attr.getJSONObject("command")
                                                      .getString("des"));
                                attrr.put("type", 5);
                                list_chat.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        messages.add(0, attrr);
                                        mHandler.sendEmptyMessage(chatmessage);
                                    }
                                });
                            } else if (action
                                    .equalsIgnoreCase("winning")) {//中奖消息
                                final JSONObject attrr = new JSONObject();
                                attrr.put("user", attr.getJSONObject("user"));
                                attrr.put("text", attr.getJSONObject("command")
                                                      .getString("des"));
                                attrr.put("type", 4);
                                list_chat.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        messages.add(0, attrr);
                                        mHandler.sendEmptyMessage(chatmessage);
                                    }
                                });
                            } else if (action
                                    .equalsIgnoreCase("global_message")) {//全站特效
                                JSONObject user_gm = attr
                                        .optJSONObject("command")
                                        .optJSONObject("user");
                                final String username = user_gm
                                        .optString("user_name");
                                final String user_identity = user_gm
                                        .optString("user_identity");
                                JSONObject anchor = attr
                                        .optJSONObject("command")
                                        .optJSONObject("anchor");
                                final String anchor_name = anchor
                                        .optString("anchor_name");
                                final String anchor_identity = anchor
                                        .optString("anchor_identity");
                                JSONObject gift = attr.optJSONObject("command")
                                                      .optJSONObject("gift");
                                final String gift_name = gift
                                        .optString("gift_name");
                                final String gift_unit_name = gift
                                        .optString("gift_unit_name");
                                String globalmessage = username + "(" +
                                        user_identity + ")" + "送给 " +
                                        anchor_name + "(" + anchor_identity +
                                        ") " + "1" + gift_unit_name + gift_name;
                                int usercolor = globalmessage.indexOf(")") + 1;
                                int givecolor = globalmessage.indexOf(" ") + 1;
                                int anchorcolor =
                                        globalmessage.lastIndexOf(")") + 1;

                                final SpannableString spanString = new SpannableString(globalmessage);
                                ForegroundColorSpan span = new ForegroundColorSpan(Color
                                        .parseColor("#ab4100"));
                                spanString
                                        .setSpan(span, 0, usercolor, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                spanString.setSpan(span, givecolor +
                                        1, anchorcolor, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                ForegroundColorSpan span1 = new ForegroundColorSpan(Color
                                        .parseColor("#f16900"));
                                spanString.setSpan(span1, usercolor +
                                        1, givecolor, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                spanString.setSpan(span1,
                                        anchorcolor + 1, spanString
                                                .length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ll_global_gift
                                                .setVisibility(View.VISIBLE);
                                        rl_global_gift
                                                .setOnTouchListener(new View.OnTouchListener() {
                                                    @Override
                                                    public boolean onTouch(View v, MotionEvent event) {
                                                        return true;
                                                    }
                                                });
                                        send_person.setText(spanString);
                                        rl_global_gift.measure(0, 0);
                                        final ObjectAnimator animator = ObjectAnimator
                                                .ofFloat(rl_global_gift, "x", width, -rl_global_gift
                                                        .getMeasuredWidth());
                                        animator.setDuration(20000);
                                        animator.addListener(new AnimationListener() {
                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                ll_global_gift
                                                        .setVisibility(View.GONE);
                                                animator.cancel();
                                            }
                                        });
                                        animator.start();
                                    }
                                });
                            }
                        } else {
                            list_chat.post(new Runnable() {
                                @Override
                                public void run() {
                                    messages.add(0, attr);
                                    mHandler.sendEmptyMessage(chatmessage);
                                }
                            });
                        }
                    } catch (JSONException | UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        mIsLoading = false;
    }


    //进房特效
    private void enterRoomAnimation() {
        if (localendterRoom.size() == 0) {
            return;
        }
        JSONObject peek = localendterRoom.peek();
        if (null == peek) {
            return;
        }
        int grade = peek.optInt("grade");
        String enter_message = peek.optString("enter_message");
        isenterMessage = false;
        msg_enterrome.setVisibility(View.VISIBLE);
        EnterView enterView = new EnterView(AvActivity.this, width, grade, enter_message);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        msg_enterrome.addView(enterView, layoutParams);
        enterView.addAnimatorLister(new AnimationListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                msg_enterrome.setVisibility(View.GONE);
                localendterRoom.poll();
                isenterMessage = true;
                enterRoomAnimation();
            }
        });
    }


    public void GifAnimation() {
        if (localqueue.size() == 0) {
            return;
        }
        JSONObject peek = localqueue.peek();
        if (peek == null) {
            return;
        }
        JSONObject command = peek.optJSONObject("command");
        JSONObject gift = command.optJSONObject("gift");
        String gift_id = gift.optString("gift_id");

        if (gift_id.equals(DemoConstants.GREEN_CAR)) {//汽车
            final CarAnimatorView carAnimatorView = new CarAnimatorView(mContext, width, height);
            carAnimatorView.addAnimatorLister(new AnimationListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    av_screen_layout.removeView(carAnimatorView);
                    localqueue.poll();
                    GifAnimation();
                }
            });
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            av_screen_layout.addView(carAnimatorView, layoutParams);
            carAnimatorView.startAnimator();
            JSONObject attrto = new JSONObject();
            try {
                attrto.put("user", peek.optJSONObject("user"));
                String gift_count_unit = peek.getJSONObject("command")
                                             .getJSONObject("gift")
                                             .getString("gift_count_unit");
                int giftPrice = peek.getJSONObject("command")
                                    .getJSONObject("gift").getInt("gift_price");
                String name = peek.getJSONObject("command")
                                  .getJSONObject("gift").getString("gift_name");
                attrto.put("text", "送了" + 1 + gift_count_unit + name);
                attrto.put("type", 1);//礼物消息
                mMeilizhiTextview.setText(
                        Integer.parseInt(mMeilizhiTextview.getText()
                                                          .toString()) +
                                giftPrice * 10 + "");
                messages.add(0, attrto);
                adapter_chat.notifyItemInserted(0);
                list_chat.smoothScrollToPosition(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (gift_id.equals(DemoConstants.FLY_BIG)) {//飞机
            final FlyAnimatorView flyAnimatorView = new FlyAnimatorView(getApplicationContext(), width, height);
            flyAnimatorView.addAnimatorLister(new AnimationListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    av_screen_layout.removeView(flyAnimatorView);
                    localqueue.poll();
                    GifAnimation();
                }
            });
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            layoutParams.bottomMargin = studio.archangel.toolkitv2.util.Util
                    .getPX(60);
            av_screen_layout.addView(flyAnimatorView, layoutParams);
            flyAnimatorView.startAnimator();
            JSONObject attrto = new JSONObject();
            try {
                attrto.put("user", peek.getJSONObject("user"));
                String gift_count_unit = peek.getJSONObject("command")
                                             .getJSONObject("gift")
                                             .getString("gift_count_unit");
                String name = peek.getJSONObject("command")
                                  .getJSONObject("gift").getString("gift_name");
                attrto.put("text", "送了" + 1 + gift_count_unit + name);
                attrto.put("type", 1);//礼物消息
                int giftPrice = peek.getJSONObject("command")
                                    .getJSONObject("gift").getInt("gift_price");
                mMeilizhiTextview.setText(
                        Integer.parseInt(mMeilizhiTextview.getText()
                                                          .toString()) +
                                giftPrice * 10 + "");
                messages.add(0, attrto);
                adapter_chat.notifyItemInserted(0);
                list_chat.smoothScrollToPosition(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (gift_id.equals(DemoConstants.RED_CAR)) {
            final RedcarAnimatorView redcarAnimatorView = new RedcarAnimatorView(getApplicationContext(), width, height);
            redcarAnimatorView.addAnimatorLister(new AnimationListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    av_screen_layout.removeView(redcarAnimatorView);
                    localqueue.poll();
                    GifAnimation();
                }
            });
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            av_screen_layout.addView(redcarAnimatorView, layoutParams);
            redcarAnimatorView.startAnimator();
            JSONObject attrto = new JSONObject();
            try {
                attrto.put("user", peek.getJSONObject("user"));
                String gift_count_unit = peek.getJSONObject("command")
                                             .getJSONObject("gift")
                                             .getString("gift_count_unit");
                String name = peek.getJSONObject("command")
                                  .getJSONObject("gift").getString("gift_name");
                attrto.put("text", "送了" + 1 + gift_count_unit + name);
                attrto.put("type", 1);//礼物消息
                int giftPrice = peek.getJSONObject("command")
                                    .getJSONObject("gift").getInt("gift_price");
                mMeilizhiTextview.setText(
                        Integer.parseInt(mMeilizhiTextview.getText()
                                                          .toString()) +
                                giftPrice * 10 + "");
                messages.add(0, attrto);
                adapter_chat.notifyItemInserted(0);
                list_chat.smoothScrollToPosition(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (gift_id.equals(DemoConstants.AIRCRAFT)) {//航母
            final AircraftAnimationView aircraftAnimation = new AircraftAnimationView(this);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            //            layoutParams.bottomMargin = studio.archangel.toolkitv2.util.Utils.getPX(60);
            av_screen_layout.addView(aircraftAnimation, layoutParams);
            aircraftAnimation.setonListener(new AnimationListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    av_screen_layout.removeView(aircraftAnimation);
                    localqueue.poll();
                    GifAnimation();
                }
            });
            aircraftAnimation.start();
            JSONObject attrto = new JSONObject();
            try {
                attrto.put("user", peek.getJSONObject("user"));
                String gift_count_unit = peek.getJSONObject("command")
                                             .getJSONObject("gift")
                                             .getString("gift_count_unit");
                String name = peek.getJSONObject("command")
                                  .getJSONObject("gift").getString("gift_name");
                attrto.put("text", "送了" + 1 + gift_count_unit + name);
                attrto.put("type", 1);//礼物消息
                int giftPrice = peek.getJSONObject("command")
                                    .getJSONObject("gift").getInt("gift_price");
                mMeilizhiTextview.setText(
                        Integer.parseInt(mMeilizhiTextview.getText()
                                                          .toString()) +
                                giftPrice * 10 + "");
                messages.add(0, attrto);
                adapter_chat.notifyItemInserted(0);
                list_chat.smoothScrollToPosition(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (gift_id.equals(DemoConstants.FLOWER_RAIN)) {//花瓣
            FlowerAnimationView flowerAnimationView = new FlowerAnimationView(mContext, av_screen_layout, mHandler);
            flowerAnimationView
                    .setOnStopListener(new FlowerAnimationView.StopListener() {
                        @Override
                        public void onStop() {
                            localqueue.poll();
                            GifAnimation();
                        }
                    });

            flowerAnimationView.start();
            JSONObject attrto = new JSONObject();
            try {
                attrto.put("user", peek.getJSONObject("user"));
                String gift_count_unit = peek.getJSONObject("command")
                                             .getJSONObject("gift")
                                             .getString("gift_count_unit");
                String name = peek.getJSONObject("command")
                                  .getJSONObject("gift").getString("gift_name");
                attrto.put("text", "送了" + 1 + gift_count_unit + name);
                attrto.put("type", 1);//礼物消息
                int giftPrice = peek.getJSONObject("command")
                                    .getJSONObject("gift").getInt("gift_price");
                mMeilizhiTextview.setText(
                        Integer.parseInt(mMeilizhiTextview.getText()
                                                          .toString()) +
                                giftPrice * 10 + "");
                messages.add(0, attrto);
                adapter_chat.notifyItemInserted(0);
                list_chat.smoothScrollToPosition(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            if (null == getGift) {
                getGift = new GetGiftThree(mContext, mHandler, mMeilizhiTextview, giftbig_fl, list_chat, list_chat2, act_live_rv_gift, messages2, messages, messagesgift, adapter_chat, adapter_chat2, adapter_gift, giftMap, queuebig);
            }
            getGift.addMessageToList1(localqueue.peek(), queuebig);
        }
    }


    public boolean hideMsgIputKeyboard() {
        InputMethodManager input = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        return input.hideSoftInputFromWindow(getWindow().getDecorView()
                                                        .getWindowToken(), 0);
        // return false;
    }


    private void locateCameraPreview() {
        if (mDialogInit != null && mDialogInit.isShowing()) {
            mDialogInit.dismiss();
        }
    }


    AVEndpoint endpoint = null;


    //主动请求界面
    public void hostRequestView(String identifier) {
        Logger.out("request " + identifier);

        if (mQavsdkControl != null && mQavsdkControl.getAVContext() != null &&
                mQavsdkControl.getAVContext().getRoom() != null) {
            endpoint = ((AVRoomMulti) mQavsdkControl.getAVContext().getRoom())
                    .getEndpointById(identifier);
        }
        Logger.out("hostRequestView identifier " + identifier + " endpoint " +
                endpoint);
        if (endpoint != null) {
            Logger.out("request 不为空");
            AVView view = new AVView();
            view.videoSrcType = AVView.VIDEO_SRC_TYPE_CAMERA;//SDK1.2版本只支持摄像头视频源，所以当前只能设置为VIDEO_SRC_TYPE_CAMERA。
            view.viewSizeType = AVView.VIEW_SIZE_TYPE_BIG;
            //界面数
            mRequestViewList[0] = view;
            mRequestIdentifierList[0] = identifier;
            mRequestViewList[0].viewSizeType = AVView.VIEW_SIZE_TYPE_BIG;
            int i = AVEndpoint
                    .requestViewList(mRequestIdentifierList, mRequestViewList, 1, new AVEndpoint.RequestViewListCompleteCallback());
            Logger.out("JXH:requestviewlist " + i);
            Logger.out("JXH:requestviewlist " + i);
            synchronized (this) {
                if (i == AVError.AV_OK) {
                    isFirst = false;
                }
            }
            //成员模式请求界面
            //            mQavsdkControl.getAVVideoControl().enableCustomerRendMode();
            ctx.sendBroadcast(new Intent(Utils.ACTION_VIDEO_SHOW)
                    .putExtra(Utils.EXTRA_IDENTIFIER, identifier)
                    .putExtra(Utils.EXTRA_VIDEO_SRC_TYPE, view.videoSrcType));
        } else {
        }
    }


    public void requestMultiView(String identifier) {
        Logger.out("requestMultiView " + identifier + "  mMemberVideoCount  ");
        int mMemberVideoCount = mQavsdkControl.getSmallVideoView();

        AVEndpoint endpoint = ((AVRoomMulti) mQavsdkControl.getAVContext()
                                                           .getRoom())
                .getEndpointById(identifier);
        if (endpoint != null) {
            //界面参数
            AVView view = new AVView();
            view.videoSrcType = AVView.VIDEO_SRC_TYPE_CAMERA;
            view.viewSizeType = AVView.VIEW_SIZE_TYPE_BIG;

            //            Logger.out( "requestMultiView  " + identifier + "  mMemberVideoCount  " + mMemberVideoCount);
            //            for (int i = 0; i < mMemberVideoCount; i++) {
            //                mRequestViewList[i].viewSizeType = AVView.VIEW_SIZE_TYPE_BIG;
            //            }

            //界面参数
            mRequestViewList[mMemberVideoCount] = view;
            mRequestIdentifierList[mMemberVideoCount] = identifier;

            //请求次数
            mMemberVideoCount++;
            if (mMemberVideoCount > 3) {
                Toast.makeText(this, "requestCount cannot pass  4", Toast.LENGTH_LONG);
                return;
            }
            mQavsdkControl.setRequestCount(mMemberVideoCount);
            Logger.out("requestMultiView identifier " + identifier +
                    " mMemberVideoCount " + mMemberVideoCount);
            AVEndpoint
                    .requestViewList(mRequestIdentifierList, mRequestViewList, mMemberVideoCount, mRequestViewListCompleteCallback);

            //成员模式请求界面
            ctx.sendBroadcast(new Intent(Utils.ACTION_MEMBER_VIDEO_SHOW)
                    .putExtra(Utils.EXTRA_IDENTIFIER, identifier)
                    .putExtra(Utils.EXTRA_VIDEO_SRC_TYPE, view.videoSrcType));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.act_live_input_send:
                onSendMsg();
                break;
            case R.id.view_liv_send_button://连发礼物
                giftcount = 1;
                //                amountTransfer++;
                if (null == sendgifttimer) {
                    return;
                }

                sendgifttimer.cancel();
                sendgifttimer.start();

                Animation scaleAnimation = new ScaleAnimation(0.8f, 1f, 0.8f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                //设置动画时间
                scaleAnimation.setDuration(300);
                scaleAnimation.setRepeatCount(0);
                scaleAnimation.setInterpolator(new DecelerateInterpolator());
                view_liv_send_button.startAnimation(scaleAnimation);
                sendgift(1);
                break;
            case R.id.ll_give://连发礼物1
                giftcount = Integer.parseInt(tv_count1.getText().toString());
                //                amountTransfer += Integer.parseInt(tv_count1.getText().toString());
                sendgifttimer.cancel();
                sendgifttimer.start();
                Animation scaleAnimation1 = new ScaleAnimation(0.8f, 1f, 0.8f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                //设置动画时间
                scaleAnimation1.setDuration(300);
                scaleAnimation1.setRepeatCount(0);
                scaleAnimation1.setInterpolator(new DecelerateInterpolator());
                ll_give.startAnimation(scaleAnimation1);
                sendgift(1);
                break;
            case R.id.ll_give2://连发礼物9
                giftcount = Integer.parseInt(tv_count2.getText().toString());
                //                amountTransfer += Integer.parseInt(tv_count2.getText().toString());
                sendgifttimer.cancel();
                sendgifttimer.start();
                Animation scaleAnimation2 = new ScaleAnimation(0.8f, 1f, 0.8f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                //设置动画时间
                scaleAnimation2.setDuration(300);
                scaleAnimation2.setRepeatCount(0);
                scaleAnimation2.setInterpolator(new DecelerateInterpolator());
                ll_give2.startAnimation(scaleAnimation2);
                sendgift(1);
                break;
            case R.id.ll_give3://连发礼物99
                giftcount = Integer.parseInt(tv_count3.getText().toString());
                //                amountTransfer += Integer.parseInt(tv_count3.getText().toString());
                sendgifttimer.cancel();
                sendgifttimer.start();
                Animation scaleAnimation3 = new ScaleAnimation(0.8f, 1f, 0.8f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                //设置动画时间
                scaleAnimation3.setDuration(300);
                scaleAnimation3.setRepeatCount(0);
                scaleAnimation3.setInterpolator(new DecelerateInterpolator());
                ll_give3.startAnimation(scaleAnimation3);
                sendgift(1);
                break;
            case R.id.ll_give4://连发礼物999
                giftcount = Integer.parseInt(tv_count4.getText().toString());
                //                amountTransfer += Integer.parseInt(tv_count4.getText().toString());
                sendgifttimer.cancel();
                sendgifttimer.start();
                Animation scaleAnimation4 = new ScaleAnimation(0.8f, 1f, 0.8f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                //设置动画时间
                scaleAnimation4.setDuration(300);
                scaleAnimation4.setRepeatCount(0);
                scaleAnimation4.setInterpolator(new DecelerateInterpolator());
                ll_give4.startAnimation(scaleAnimation4);
                sendgift(1);
                break;
            case R.id.tv_report://举报
                reportAlertDialog();
                break;
            case R.id.ll_usercp://存在感
                UsercpDialog usercpDialog = new UsercpDialog(ctx, mMeilizhiTextview
                        .getText().toString(), mHostIdentifier);
                usercpDialog.getWindow()
                            .setLayout(getKjwidth(R.dimen.show_gerenziliao_width12), getKjwidth(R.dimen.show_gerenziliao_height12));
                usercpDialog.setCanceledOnTouchOutside(true);
                usercpDialog.show();
                break;
            case R.id.frag_progress:
                //                amountTransfer += giftcount;
                tv_lianchengcount.setText("x" + giftcount);
                sendgifttimer.cancel();
                sendgifttimer.start();
                Animation scaleAnimation5 = new ScaleAnimation(0.8f, 1f, 0.8f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                //设置动画时间
                scaleAnimation5.setDuration(300);
                scaleAnimation5.setRepeatCount(0);
                scaleAnimation5.setInterpolator(new DecelerateInterpolator());
                circleProgressbar.startAnimation(scaleAnimation5);
                sendgift(0);
                break;
            default:
                break;
        }
    }


    //发礼物
    private synchronized void sendgift(int flag) {
        try {
            if (amountTransfer == 0) {
                amountTransfer = giftcount;
            } else {
                amountTransfer += giftcount;
            }
            String gift_id = sendgiftattr.getJSONObject("command")
                                         .getJSONObject("gift")
                                         .getString("gift_id");
            int isGif = sendgiftattr.getJSONObject("command")
                                    .getJSONObject("gift").getInt("isGif");
            gift_price = sendgiftattr.getJSONObject("command")
                                     .getJSONObject("gift")
                                     .getInt("gift_price");
            int gift_type = sendgiftattr.getJSONObject("command")
                                        .getJSONObject("gift")
                                        .getInt("gift_type");
            if (gift_type != 2) {
                if (Integer.parseInt(tv_account.getText().toString()) <
                        gift_price * giftcount) {
                    sendgiftAlertDialog(gift_type);
                    sendgiftqueue.clear();
                    amountTransfer -= giftcount;
                    return;
                }
            } else {
                if (Integer.parseInt(tv_species_account.getText().toString()) <
                        gift_price * giftcount) {
                    sendgiftAlertDialog(gift_type);
                    sendgiftqueue.clear();
                    amountTransfer -= giftcount;
                    return;
                }
            }
            if (sendgiftqueue.size() == 0) {
                if (flag == 1) {
                    sendgiftqueue.offer(HXUtil
                            .createLiveChatGiftMessage(selected_gift, 0, amountTransfer, giftcount, 0, 0, 0.0));
                } else {
                    sendgiftattr.optJSONObject("command").optJSONObject("gift")
                                .put("searilNum", amountTransfer);
                    sendgiftattr.optJSONObject("command").optJSONObject("gift")
                                .put("gift_once_count", giftcount);
                    sendgiftqueue.offer(sendgiftattr);
                }
                sendgiftQueue();
            } else {
                if (flag == 1) {
                    sendgiftqueue.offer(HXUtil
                            .createLiveChatGiftMessage(selected_gift, 0, amountTransfer, giftcount, 0, 0, 0.0));
                } else {
                    sendgiftattr.optJSONObject("command").optJSONObject("gift")
                                .put("searilNum", amountTransfer);
                    sendgiftattr.optJSONObject("command").optJSONObject("gift")
                                .put("gift_once_count", giftcount);
                    sendgiftqueue.offer(sendgiftattr);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private synchronized void sendgiftQueue() {
        final JSONObject jsonObject = sendgiftqueue.peek();
        final String gift_id = jsonObject.optJSONObject("command")
                                         .optJSONObject("gift")
                                         .optString("gift_id");
        final int gift_type = jsonObject.optJSONObject("command")
                                        .optJSONObject("gift")
                                        .optInt("gift_type");
        final String gift_name = jsonObject.optJSONObject("command")
                                           .optJSONObject("gift")
                                           .optString("gift_name");
        final int gift_once_count = jsonObject.optJSONObject("command")
                                              .optJSONObject("gift")
                                              .optInt("gift_once_count");
        final int searilNum = jsonObject.optJSONObject("command")
                                        .optJSONObject("gift")
                                        .optInt("searilNum");
        int gift_price = jsonObject.optJSONObject("command")
                                   .optJSONObject("gift").optInt("gift_price");
        if (gift_type != 2) {
            if (Integer.parseInt(tv_account.getText().toString()) <
                    gift_price * gift_once_count) {
                sendgiftAlertDialog(gift_type);
                sendgiftqueue.clear();
                amountTransfer -= giftcount;
                return;
            }
        } else {
            if (Integer.parseInt(tv_species_account.getText().toString()) <
                    gift_price * gift_once_count) {
                sendgiftAlertDialog(gift_type);
                sendgiftqueue.clear();
                amountTransfer -= giftcount;
                return;
            }
        }
        HashMap<String, Object> per = new HashMap<String, Object>();
        per.put("user_id", mSelfUserInfo.user_id);
        per.put("receiver_id", mHostIdentifier);
        per.put("amount", gift_once_count);
        per.put("gift_id", gift_id);
        per.put("room_num", roomNum);
        HXJavaNet.post(HXJavaNet.url_givegift, per, new NetCallBack(this) {
            @Override
            public void onSuccess(int ret_code, String ret_data, String msg) {
                super.onSuccess(ret_code, ret_data, msg);
                if (ret_code == 406) {
                    sendgiftAlertDialog(gift_type);
                    sendgiftqueue.clear();
                    giftcount = 0;
                    amountTransfer = 0;
                } else if (ret_code == 200) {
                    int charm_value = 0;
                    int rate = 0;
                    try {
                        if (gift_id.equals(DemoConstants.AIRCRAFT)) {
                            HashMap<String, Object> per = new HashMap<>();
                            per.put("user_id", mSelfUserInfo.user_id);
                            per.put("receiver_id", mHostIdentifier);
                            per.put("amount", gift_once_count);
                            per.put("gift_id", gift_id);
                            HXJavaNet
                                    .post(HXJavaNet.url_send_gift_msg, per, new NetCallBack(AvActivity.this) {
                                        @Override
                                        public void onSuccess(int ret_code, String ret_data, String msg) {
                                            super.onSuccess(ret_code, ret_data, msg);
                                        }


                                        @Override
                                        public void onFailure(String msg) {
                                            super.onFailure(msg);
                                        }
                                    });
                        } else if (gift_id.equals(DemoConstants.FLOWER_RAIN)) {
                        } else if (gift_id.equals("80")) {//帝王套
                            String data = SharedPreferenceUtil
                                    .getString("data", null);
                            JSONArray ja = new JSONArray(data.toString());
                            ArrayList<Gift> list = new ArrayList<>();
                            for (int i = 0; i < ja.length(); i++) {
                                Gift obj = new Gift(ja.optJSONObject(i));
                                if (obj.type != 2) {
                                    list.add(obj);
                                }
                            }
                        }
                        sendgiftqueue.poll();
                        if (sendgiftqueue.size() > 0) {
                            sendgiftQueue();
                        }
                        JSONObject json = new JSONObject(ret_data);
                        charm_value = json.getInt("charm_value");
                        int diamond_amount = json.getInt("diamond_amount");
                        loveGradegift = json.optInt("loveGrade");
                        loveGradeRatiogift = json.optInt("loveGradeRatio");
                        if (gift_type != 2) {
                            tv_account.setText(diamond_amount + "");
                        } else {//黑豆
                            tv_species_account.setText(diamond_amount + "");
                        }
                        rate = json.getInt("rate");//黑豆获奖
                        int grade = json.getInt("grade");//等级
                        UserInfo userInfo = HXApp.getInstance().getUserInfo();
                        if (userInfo != null) {
                            userInfo.grade = grade;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    TIMMessage Tim = new TIMMessage();
                    TIMCustomElem elem = new TIMCustomElem();
                    if (null == jsonObject) {
                        return;
                    }
                    JSONObject jsonObjectcom = jsonObject
                            .optJSONObject("command");
                    try {
                        jsonObjectcom.put("charm_value", charm_value);
                        jsonObjectcom.put("winning_rate", rate);
                        jsonObjectcom.put("loveGrade", loveGradegift);
                        jsonObjectcom.put("loveGradeRatio", loveGradeRatiogift);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    elem.setData(jsonObject.toString().getBytes());
                    elem.setDesc(UNREAD);
                    if (1 == Tim.addElement(elem)) {
                        Notifier.showNormalMsg(mContext, "出错了...");
                        return;
                    }
                    if (mConversation == null) {
                        return;
                    }
                    mConversation
                            .sendMessage(Tim, new TIMValueCallBack<TIMMessage>() {
                                @Override
                                public void onError(int i, String s) {
                                    Logger.out(
                                            "send gift error" + i + ": " + s);
                                }


                                @Override
                                public void onSuccess(TIMMessage timMessage) {
                                    TIMElem elem = timMessage.getElement(0);
                                    try {
                                        JSONObject attr = new JSONObject(new String(((TIMCustomElem) elem)
                                                .getData(), "utf-8"));
                                        String newgift_id = attr
                                                .optJSONObject("command")
                                                .optJSONObject("gift")
                                                .optString("gift_id");
                                        String newuser_id = attr
                                                .optJSONObject("user")
                                                .optString("user_id");
                                        int isGif = attr
                                                .optJSONObject("command")
                                                .optJSONObject("gift")
                                                .optInt("isGif");
                                        int giftPrice = attr
                                                .optJSONObject("command")
                                                .optJSONObject("gift")
                                                .optInt("gift_price");
                                        String giftImage = attr
                                                .optJSONObject("command")
                                                .optJSONObject("gift")
                                                .optString("gif_image");
                                        if (isGif == 0 || (isGif == 1 &&
                                                                   giftImage
                                                                           .isEmpty())) {
                                            if (giftPrice < 10) {
                                                if (userid.equals(newuser_id) &&
                                                        giftid.equals(newgift_id)) {
                                                    queuebig.offer(attr);
                                                    userid = newuser_id;
                                                    giftid = newgift_id;
                                                } else {
                                                    queue.offer(attr);
                                                    userid = newuser_id;
                                                    giftid = newgift_id;
                                                }
                                            } else {
                                                queuebig.offer(attr);
                                            }
                                        } else {
                                            localqueue.offer(attr);
                                            if (localqueue.size() == 1) {
                                                GifAnimation();
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                    if (gift_type == 2) {
                        if (rate == 0) {
                            return;
                        }
                        TIMMessage Timmessage = new TIMMessage();
                        TIMCustomElem elemcustom = new TIMCustomElem();
                        elemcustom.setData(HXUtil.createLivetanmuwinningMessage(
                                "赠送" + giftcount + "个" + gift_name + ",中了" +
                                        rate + "倍大奖").toString().getBytes());
                        elemcustom.setDesc(UNREAD);
                        if (1 == Timmessage.addElement(elemcustom)) {
                            Notifier.showNormalMsg(mContext, "出错了...");
                            return;
                        }
                        mConversation
                                .sendMessage(Timmessage, new TIMValueCallBack<TIMMessage>() {
                                    @Override
                                    public void onError(int i, String s) {
                                        Logger.out(
                                                "send gift error" + i + ": " +
                                                        s);
                                    }


                                    @Override
                                    public void onSuccess(TIMMessage timMessage) {
                                        TIMElem elem = timMessage.getElement(0);
                                        try {
                                            JSONObject attr = new JSONObject(new String(((TIMCustomElem) elem)
                                                    .getData(), "utf-8"));
                                            JSONObject attrto = new JSONObject();
                                            attrto.put("user", attr
                                                    .getJSONObject("user"));
                                            String text = attr
                                                    .getJSONObject("command")
                                                    .getString("des");
                                            attrto.put("text", text);
                                            attrto.put("type", 4);//礼物消息
                                            messages.add(0, attrto);
                                            mHandler.sendEmptyMessage(chatmessage);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        if (rate >= HXApp.getInstance().wining) {//fadanmu
                            String user_name = mSelfUserInfo.nickname;
                            TIMMessage Nmsg = new TIMMessage();
                            TIMCustomElem elemd = new TIMCustomElem();
                            String message = user_name + "中" + rate + "倍大奖";
                            elemd.setData(HXUtil
                                    .createLivetanmuTextMessage(message, 1)
                                    .toString().getBytes());
                            elemd.setDesc(UNREAD);
                            if (1 == Nmsg.addElement(elemd)) {
                                Notifier.showNormalMsg(mContext, "出错了...");
                                return;
                            }
                            Nmsg.getSender();
                            mConversation
                                    .sendMessage(Nmsg, new TIMValueCallBack<TIMMessage>() {
                                        @Override
                                        public void onError(int i, String s) {
                                            if (i == 85) {
                                                mHandler.sendEmptyMessage(ERROR_MESSAGE_TOO_LONG);
                                            } else if (i == 6011) {
                                                mHandler.sendEmptyMessage(ERROR_ACCOUNT_NOT_EXIT);
                                            }
                                        }


                                        @Override
                                        public void onSuccess(TIMMessage timMessage) {
                                            try {
                                                TIMElem timCustomElem = timMessage
                                                        .getElement(0);
                                                if (null == barrageView) {
                                                    barrageView = new BarrageView(getApplication(), danmakuQueue, mTanmuContainer1, view_one, view_two, view_three, view_four);
                                                    mTanmuContainer1
                                                            .addView(barrageView);
                                                }
                                                barrageView
                                                        .sendTanmu(new String(((TIMCustomElem) timCustomElem)
                                                                .getData(), "utf-8"));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                        }
                    }
                } else if (ret_code == 0) {
                    sendgiftqueue.clear();
                }
            }


            @Override
            public void onFailure(String msg) {
                //if (sendgiftqueue != null) {
                //    sendgiftqueue.poll();
                //}
                if (sendgiftqueue.size() > 0) {
                    sendgiftQueue();
                }
                super.onFailure(msg);
            }
        });
    }


    //举报框
    private void reportAlertDialog() {
        dialog = new Dialog(this, R.style.dialog);
        dialog.setContentView(R.layout.exit_dialog);
        TextView messageTextView = (TextView) dialog.findViewById(R.id.message);
        Button exitOk = (Button) dialog.findViewById(R.id.btn_exit_ok);
        Button exitCancel = (Button) dialog.findViewById(R.id.btn_exit_cancel);
        messageTextView.setText("确认要举报吗？");
        exitOk.setText("是的");
        exitCancel.setText("取消");
        exitOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                HashMap<String, Object> pare = new HashMap<String, Object>();
                pare.put("room_num", roomNum);
                pare.put("user_id", mSelfUserInfo.user_id);
                pare.put("respondent_user_id", mHostIdentifier);

                HXJavaNet
                        .post(HXJavaNet.url_complain, pare, new NetCallBack(AvActivity.this) {
                            @Override
                            public void onSuccess(int ret_code, String ret_data, String msg) {
                                super.onSuccess(ret_code, ret_data, msg);
                                Notifier.showShortMsg(mContext, "举报成功");
                            }
                        });
            }
        });

        exitCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void sendgiftAlertDialog(int gift_type) {
        String figt_tishiyu = "嘿豆";
        if (gift_type != 2) {
            figt_tishiyu = "钻石";
        }

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


    public void PushStart() {
        int roomid = (int) mQavsdkControl.getAVContext().getRoom().getRoomId();
        TIMAvManager.RoomInfo roomInfo = TIMAvManager
                .getInstance().new RoomInfo();
        roomInfo.setRoomId(roomid);
        roomInfo.setRelationId(groupForPush);
        setParamAndPush(roomInfo);
    }


    public void PushStop() {
        int roomid = (int) mQavsdkControl.getAVContext().getRoom().getRoomId();
        Logger.out("Push roomid: " + roomid);
        Log.e("Push", "Push roomid: " + roomid);
        Logger.out("Push groupid: " + groupForPush);
        Log.e("Push", "Push groupid: " + groupForPush);
        Logger.out("Push mpush: " + mpush);
        Log.e("Push", "Push mpush: " + mpush);
        Logger.out("Push enviroment: " + mSelfUserInfo.Env);
        Log.e("Push", "Push enviroment: " + mSelfUserInfo.Env);
        TIMAvManager.RoomInfo roomInfo = TIMAvManager
                .getInstance().new RoomInfo();
        roomInfo.setRoomId(roomid);
        roomInfo.setRelationId(groupForPush);
        stopPushAction(roomInfo);
    }


    private void pushAction(TIMAvManager.RoomInfo roominfo) {
        //推流的接口
        TIMAvManager.getInstance()
                    .requestMultiVideoStreamerStart(roominfo, mStreamParam, new TIMValueCallBack<TIMAvManager.StreamRes>() {
                                @Override
                                public void onError(int i, String s) {
                                    Logger.out("url error " + i + " : " + s);
                                }


                                @Override
                                public void onSuccess(TIMAvManager.StreamRes streamRes) {
                                    List<TIMAvManager.LiveUrl> liveUrls = streamRes
                                            .getUrls();
                                    streamChannelID = streamRes.getChnlId();
                                    BigInteger unsignedNum = BigInteger
                                            .valueOf(streamChannelID);
                                    if (streamChannelID < 0) {
                                        unsignedNum = unsignedNum
                                                .add(BigInteger.ZERO.flipBit(64));
                                    }
                                    Logger.out("create channel succ. channelid: " +
                                            unsignedNum + ", addr size " +
                                            streamRes.getUrls().size());
                                    channelID = unsignedNum.toString();

                                    mpush = true;
                                    int length = liveUrls.size();
                                    String url = null;
                                    for (int i = 0; i < length; i++) {
                                        TIMAvManager.LiveUrl avUrl = liveUrls.get(i);
                                        url = avUrl.getUrl();
                                        Logger.out("url success " + " : " + url);
                                    }
                                    PushfinalUrl = url;
                                    sendChannelID(roomNum, mSelfUserInfo.user_id, channelID);
                                }
                            }

                    );
    }


    public String readUnsignedLong(long value) throws IOException {
        BigDecimal add = null;
        if (value >= 0) {
            return value + "";
        }
        long lowValue = value & 0x7fffffffffffffffL;
        try {
            add = BigDecimal.valueOf(lowValue)
                            .add(BigDecimal.valueOf(Long.MAX_VALUE))
                            .add(BigDecimal.valueOf(1));
        } catch (Exception e) {
        }
        if (add == null) {
            return "";
        }
        return add.toString();
    }


    public long readUnsignedLong1(long value) {
        if (value >= 0) {
            return value;
        }
        long unsignedValue = value & Long.MAX_VALUE;
        unsignedValue |= 0x8000000L;

        return unsignedValue;
    }


    //发送ChannelID到服务器
    private void sendChannelID(int roomNum, int userId, String channelID) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("room_num", roomNum);
        params.put("channel_id", channelID);
        HXJavaNet
                .post(HXJavaNet.url_liveEditChannelId, params, new NetCallBack(this));
    }


    private void stopPushAction(TIMAvManager.RoomInfo roomInfo) {
        Logger.out("Push stop Id " + streamChannelID);
        List<Long> myList = new ArrayList<>();
        myList.add(streamChannelID);
        TIMAvManager.getInstance()
                    .requestMultiVideoStreamerStop(roomInfo, myList, new TIMCallBack() {
                        @Override
                        public void onError(int i, String s) {
                            Logger.out("url stop error " + i + " : " + s);
                        }


                        @Override
                        public void onSuccess() {
                            mpush = false;
                            Logger.out("url stop success ");
                        }
                    });
    }


    //录制参数
    private void setRecordParam() {
        mRecordParam = TIMAvManager.getInstance().new RecordParam();
        mRecordParam.setFilename(
                mSelfUserInfo.identity + ":" + mSelfUserInfo.nickname);
        mRecordParam.setClassId(110);
        mRecordParam.setTransCode(false);
        mRecordParam.setSreenShot(false);
        mRecordParam.setWaterMark(true);
        mHandler.sendEmptyMessage(START_RECORD);
    }


    private void startDefaultRecord() {
        Logger.out("setDefaultRecordParam roomName" + mHXApp.getRoomName());
        mRecordParam = TIMAvManager.getInstance().new RecordParam();
        mRecordParam.setFilename(mHXApp.getRoomName());
        mRecordParam.setClassId(0);
        mRecordParam.setTransCode(false);
        mRecordParam.setSreenShot(false);
        mRecordParam.setWaterMark(false);
        mHandler.sendEmptyMessage(START_RECORD);
    }


    //开始录制
    public void startRecord() {
        if (!(mQavsdkControl.getAVContext() != null &&
                      mQavsdkControl.getAVContext().getRoom() != null)) {
            return;
        }
        int roomid = (int) mQavsdkControl.getAVContext().getRoom().getRoomId();
        Logger.out("roomid: " + roomid);
        Logger.out("groupid: " + groupForPush);

        TIMAvManager.RoomInfo roomInfo = TIMAvManager
                .getInstance().new RoomInfo();
        roomInfo.setRelationId(groupForPush);
        roomInfo.setRoomId(roomid);

        TIMAvManager.getInstance()

                    .requestMultiVideoRecorderStart(roomInfo, mRecordParam, new TIMCallBack() {
                        @Override
                        public void onError(int i, String s) {
                            Logger.out("Record error" + i + " : " + s);
                            Logger.out("record:开启录制失败");
                        }


                        @Override
                        public void onSuccess() {
                            mRecord = true;
                        }
                    });
    }


    private void setParamAndPush(final TIMAvManager.RoomInfo roominfo) {
        mStreamParam = TIMAvManager.getInstance().new StreamParam();
        mStreamParam.setChannelName("" + mHXApp.getRoomName());
        mStreamParam.setChannelDescr("" + mSelfUserInfo.signature);
        mStreamParam.setEncode(TIMAvManager.StreamEncode.HLS_AND_RTMP);
        pushAction(roominfo);
    }


    //停止录制
    private void stopRecord(final boolean close) {
        int roomid = (int) mQavsdkControl.getAVContext().getRoom().getRoomId();
        if (0 == roomid) {
            oncloseavactivity = false;
            onCloseVideo(true, false);
        }
        TIMAvManager.RoomInfo roomInfo = TIMAvManager
                .getInstance().new RoomInfo();
        roomInfo.setRelationId(groupForPush);
        roomInfo.setRoomId(roomid);
        TIMAvManager.getInstance()
                    .requestMultiVideoRecorderStop(roomInfo, new TIMValueCallBack<List<String>>() {
                        @Override
                        public void onError(int i, String s) {
                            Logger.out("stop record error " + i + " : " + s);
                            sendBroadcast(new Intent(Utils.ACTION_EXTRA_STOPRECORDD)
                                    .putExtra("true", true));
                        }


                        @Override
                        public void onSuccess(List<String> files) {
                            mRecord = false;
                            Log.e("list file:", files.toString());
                            for (String file : files) {
                                Logger.out(
                                        "stopRecord onSuccess file  " + file);
                            }
                            videoRecordId = files.get(0);

                            sendBroadcast(new Intent(Utils.ACTION_EXTRA_STOPRECORDD)
                                    .putExtra("true", true));
                        }
                    });
        Logger.out("success");
    }


    protected void onCheckedChanged(boolean checked) {
        AVAudioCtrl avAudioCtrl = mQavsdkControl.getAVContext().getAudioCtrl();
        if (avAudioCtrl == null) {
            return;
        }
        if (checked) {
            Logger.out("audio Mic set true ");
            avAudioCtrl.enableMic(true);
        } else {
            Logger.out("audio Mic set false ");
            avAudioCtrl.enableMic(false);
        }
    }


    //切换摄像头
    private void onSwitchCamera() {
        if ((System.currentTimeMillis() - sytem_datei) < 3000) {
            return;
        }
        sytem_datei = System.currentTimeMillis();
        boolean isFront = mQavsdkControl.getIsFrontCamera();
        Logger.out("onSwitchCamera 111111  " + isFront);

        mSwitchCameraErrorCode = mQavsdkControl.toggleSwitchCamera();
        Logger.out(
                "onSwitchCamera() switchCamera!!  " + mSwitchCameraErrorCode);
        refreshCameraUI();
        if (mSwitchCameraErrorCode != AVError.AV_OK) {
            showDialog(isFront
                       ? DIALOG_SWITCH_BACK_CAMERA_FAILED
                       : DIALOG_SWITCH_FRONT_CAMERA_FAILED);
            mQavsdkControl.setIsInSwitchCamera(false);
            refreshCameraUI();
        } else {

        }
    }


    //关闭视频
    private void onCloseVideo(final boolean mExitroom, boolean mExitroom1) {
        if (mIsSuccess) {
            if (null != sendgifttimer) {
                sendgifttimer.cancel();
                sendgifttimer = null;
            }
        }
        //handler.removeCallbacksAndMessages(null);
        mHandler.removeCallbacksAndMessages(null);

        //退出AV那边群
        if ((mQavsdkControl != null) &&
                (mQavsdkControl.getAVContext() != null) &&
                (mQavsdkControl.getAVContext().getAudioCtrl() != null)) {
            mQavsdkControl.getAVContext().getAudioCtrl().stopTRAEService();
        }

        HashMap<String, Object> jo = new HashMap<String, Object>();
        jo.put("room_num", roomNum);
        jo.put("user_id", mSelfUserInfo.user_id);

        String s = mMeilizhiTextview.getText().toString();
        int i = Integer.parseInt(s);
        i = i - charm_value;
        boolean saveRecord = false;
        if (second > 3000 && i > 0 && mRecord == false &&
                videoRecordId.length() > 0) {//停止录制成功，并且录制时间大于5分钟，魅力值增长大于0
            saveRecord = true;
        }
        final boolean finalSaveRecord = saveRecord;

        if (!mSelfUserInfo.isCreater && !isEnterroominterface) {
            mExitroom1 = true;
            isEnterroominterface = false;
        }

        if (!mExitroom1) {
            NetCallBack netCallBack = new NetCallBack(this) {
                @Override
                public void onSuccess(int ret_code, String ret_data, String msg) {
                    super.onSuccess(ret_code, ret_data, msg);
                    if (mExitroom) {
                        try {
                            JSONObject oo = new JSONObject(ret_data);
                            Intent intent = new Intent(getApplicationContext(), GameOverActivity.class);
                            intent.putExtra(Utils.EXTRA_ROOM_NUM_PHOTO, oo
                                    .optString("live_cover_image"));
                            intent.putExtra(Utils.EXTRA_LEAVE_MODE, LEVAE_MODE);
                            intent.putExtra("live_viewed_num", oo
                                    .optInt("live_viewed_num"));
                            intent.putExtra("live_praise_num", oo
                                    .optInt("live_praise_num"));
                            intent.putExtra("attention_amount_living", oo
                                    .optInt("attention_amount_living"));
                            intent.putExtra("live_user_nickname", oo
                                    .optString("live_user_nickname"));
                            intent.putExtra("charm_value", oo
                                    .optInt("charm_value"));
                            intent.putExtra("saveRecord", finalSaveRecord);
                            intent.putExtra("room_num", roomNum);
                            intent.putExtra("vid", videoRecordId);
                            intent.putExtra("duration", second);
                            intent.putExtra("user_id", mSelfUserInfo.user_id);
                            intent.putExtra("iscreate", true);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }


                @Override
                public void onFailure(String msg) {
                    super.onFailure(msg);
                    if (mSelfUserInfo.isCreater) {
                        startActivity(new Intent(AvActivity.this, GameOverActivity.class)
                                .putExtra(Utils.EXTRA_ROOM_NUM, roomNum)
                                .putExtra(Utils.EXTRA_LEAVE_MODE, LEVAE_MODE));
                    }
                }
            };
            WeakReference<NetCallBack> netCallBackWeakReference = new WeakReference<>(netCallBack);
            HXJavaNet.post(HXJavaNet.url_liveClose, jo, netCallBackWeakReference
                    .get());
        }

        int code = mQavsdkControl.exitRoom();

        if (code != AVError.AV_OK) {
            closeActivity();
            return;
        }
    }


    public void liveCloseInfo(boolean btrue) {
        //正常退出穿的是true  异常退出穿的是false;
        HashMap<String, Object> jo = new HashMap<String, Object>();
        jo.put("room_num", roomNum);
        if (btrue) {
            startActivity(new Intent(AvActivity.this, GameOverActivity.class)
                    .putExtra(Utils.EXTRA_ROOM_NUM_PHOTO, getIntent()
                            .getExtras().getString("photo"))
                    .putExtra(Utils.EXTRA_LEAVE_MODE, LEVAE_MODE)
                    .putExtra("live_viewed_num", perso_num_count)
                    .putExtra("live_user_nickname", getIntent().getExtras()
                                                               .getString("nickname"))
                    .putExtra("live_user_guanzhu", messageInfo.relation_type)
                    .putExtra("room_num", roomNum).putExtra("saveRecord", false)
                    .putExtra("user_id", mSelfUserInfo.user_id)
                    .putExtra("mhost_id", mHostIdentifier));
        } else {
            HXJavaNet
                    .post(HXJavaNet.url_liveCloseInfo, jo, new NetCallBack(this) {
                        @Override
                        public void onSuccess(int ret_code, String ret_data, String msg) {
                            super.onSuccess(ret_code, ret_data, msg);
                            try {
                                JSONObject oo = new JSONObject(ret_data);
                                startActivity(new Intent(AvActivity.this, GameOverActivity.class)
                                        .putExtra(Utils.EXTRA_ROOM_NUM_PHOTO, getIntent()
                                                .getExtras().getString("photo"))
                                        .putExtra(Utils.EXTRA_LEAVE_MODE, LEVAE_MODE)
                                        .putExtra("live_viewed_num", perso_num_count)
                                        .putExtra("live_user_nickname", oo
                                                .getString("live_user_nickname"))
                                        .putExtra("room_num", roomNum)
                                        .putExtra("saveRecord", false)
                                        .putExtra("user_id", mSelfUserInfo.user_id));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                        @Override
                        public void onFailure(String msg) {
                            super.onFailure(msg);
                            if (mSelfUserInfo.isCreater) {
                                startActivity(new Intent(AvActivity.this, GameOverActivity.class)
                                        .putExtra(Utils.EXTRA_ROOM_NUM, roomNum)
                                        .putExtra(Utils.EXTRA_LEAVE_MODE, LEVAE_MODE));
                            }
                        }
                    });
        }
    }


    private long monsendmsgListi;
    private List monsendmsgListmsg = new ArrayList();


    //  private  List monsendmsgListy=new ArrayList();
    private void onSendMsg() {
        if (mConversation == null) {
            mConversation = TIMManager.getInstance()
                                      .getConversation(TIMConversationType.Group, groupId);
            if (mConversation == null) {
                return;
            }
        }
        final String msgs = mEditTextInputMsg.getText().toString();
        mEditTextInputMsg.setText("");
        if (tanmuboolean) {
            if (Integer.parseInt(tv_account.getText().toString()) > 0) {
                HashMap<String, Object> parater = new HashMap<String, Object>();
                parater.put("user_id", mSelfUserInfo.user_id + "");
                HXJavaNet
                        .post(HXJavaNet.url_barragePay, parater, new NetCallBack(this) {
                            @Override
                            public void onSuccess(int ret_code, String ret_data, String msg) {
                                super.onSuccess(ret_code, ret_data, msg);
                                fix.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (msgs.length() == 0) {
                                            return;
                                        }
                                        if ((System.currentTimeMillis() -
                                                     monsendmsgListi) < 2000) {
                                            Notifier.showShortMsg(mContext, "发送消息太频繁");
                                            return;
                                        } else if ((System.currentTimeMillis() -
                                                            monsendmsgListi) >
                                                60000) {
                                            monsendmsgListmsg.clear();
                                            monsendmsgListmsg.add(msgs);
                                        } else if (monsendmsgListmsg
                                                .contains(msgs)) {
                                            Notifier.showShortMsg(mContext, "请勿连续发送重复消息");
                                            return;
                                        } else {
                                            if (monsendmsgListmsg.size() >= 3) {
                                                monsendmsgListmsg.add(0, msgs);
                                                monsendmsgListmsg
                                                        .remove(monsendmsgListmsg
                                                                .size() - 1);
                                            } else {
                                                monsendmsgListmsg.add(0, msgs);
                                            }
                                        }

                                        monsendmsgListi = System
                                                .currentTimeMillis();
                                        try {
                                            byte[] byte_num = msgs
                                                    .getBytes("utf8");
                                            if (byte_num.length > 160) {
                                                mHandler.sendEmptyMessage(ERROR_MESSAGE_TOO_LONG);
                                                return;
                                            }
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                            return;
                                        }

                                        TIMMessage Nmsg = new TIMMessage();
                                        TIMCustomElem elem = new TIMCustomElem();
                                        elem.setData(HXUtil
                                                .createLivetanmuTextMessage(Utils
                                                        .checkguanjianzi(msgs), 0)
                                                .toString().getBytes());
                                        elem.setDesc(UNREAD);
                                        if (1 == Nmsg.addElement(elem)) {
                                            Notifier.showNormalMsg(mContext, "出错了...");
                                            return;
                                        }
                                        Nmsg.getSender();
                                        mConversation
                                                .sendMessage(Nmsg, new TIMValueCallBack<TIMMessage>() {
                                                    @Override
                                                    public void onError(int i, String s) {
                                                        if (i == 85) {
                                                            mHandler.sendEmptyMessage(ERROR_MESSAGE_TOO_LONG);
                                                        } else if (i == 6011) {
                                                            mHandler.sendEmptyMessage(ERROR_ACCOUNT_NOT_EXIT);
                                                        }
                                                    }


                                                    @Override
                                                    public void onSuccess(TIMMessage timMessage) {
                                                        try {
                                                            tv_account.setText(
                                                                    (Integer.parseInt(tv_account
                                                                            .getText()
                                                                            .toString()) -
                                                                             1) +
                                                                            "");
                                                            TIMElem timCustomElem = timMessage
                                                                    .getElement(0);
                                                            JSONObject attr = new JSONObject(new String(((TIMCustomElem) timCustomElem)
                                                                    .getData(), "utf-8"));
                                                            danmakuQueue
                                                                    .offer(new String(((TIMCustomElem) timCustomElem)
                                                                            .getData(), "utf-8"));
                                                            messages.add(0, HXUtil
                                                                    .createLiveChatBasicMessageText(msgs));
                                                            mHandler.sendEmptyMessage(chatmessage);
                                                            //                                            adapter_chat.notifyItemInserted(0);
                                                            //                                            list_chat.smoothScrollToPosition(0);

                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        } catch (UnsupportedEncodingException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                                    }
                                });
                            }
                        });
            } else {
                Notifier.showNormalMsg(mContext, "钻石不足,请充值");
            }
        } else {
            if (msgs.length() > 0) {
                if ((System.currentTimeMillis() - monsendmsgListi) < 2000) {
                    Notifier.showShortMsg(mContext, "发送消息太频繁");
                    return;
                } else if ((System.currentTimeMillis() - monsendmsgListi) >
                        60000) {
                    monsendmsgListmsg.clear();
                    monsendmsgListmsg.add(msgs);
                } else if (monsendmsgListmsg.contains(msgs)) {
                    Notifier.showShortMsg(mContext, "请勿连续发送重复消息");
                    return;
                } else {
                    if (monsendmsgListmsg.size() >= 3) {
                        monsendmsgListmsg.add(0, msgs);
                        monsendmsgListmsg.remove(monsendmsgListmsg.size() - 1);
                    } else {
                        monsendmsgListmsg.add(0, msgs);
                    }
                }
                monsendmsgListi = System.currentTimeMillis();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (msgs.length() == 0) {
                            return;
                        }
                        try {
                            byte[] byte_num = msgs.getBytes("utf8");
                            if (byte_num.length > 160) {
                                mHandler.sendEmptyMessage(ERROR_MESSAGE_TOO_LONG);
                                return;
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            return;
                        }

                        TIMMessage Nmsg = new TIMMessage();
                        Nmsg.getSender();
                        TIMCustomElem mTimcustomelem = new TIMCustomElem();
                        mTimcustomelem.setData(HXUtil
                                .createLiveChatBasicMessageText(Utils
                                        .checkguanjianzi(msgs)).toString()
                                .getBytes());
                        mTimcustomelem.setDesc(UNREAD);
                        Nmsg.addElement(mTimcustomelem);

                      /*  if(monsendmsgListi>2){
                            monsendmsgListi=0;
                        }
                        if(monsendmsgListy.contains(msgs)){*/
                        mConversation
                                .sendMessage(Nmsg, new TIMValueCallBack<TIMMessage>() {
                                    @Override
                                    public void onError(int i, String s) {

                                        if (i == 85) {
                                            mHandler.sendEmptyMessage(ERROR_MESSAGE_TOO_LONG);
                                        } else if (i == 6011) {
                                            mHandler.sendEmptyMessage(ERROR_ACCOUNT_NOT_EXIT);
                                        }
                                        Logger.out(
                                                "send message failed. code: " +
                                                        i + " errmsg: " + s);
                                    }


                                    @Override
                                    public void onSuccess(TIMMessage timMessage) {
                                        Message msg11 = new Message();
                                        msg11.what = REFRESH_MESSAGE;
                                        msg11.obj = timMessage;
                                        mHandler.sendMessage(msg11);
                                    }
                                });
/*
                        }else{

                            Message msg11 = new Message();
                            msg11.what = REFRESH_MESSAGE;
                            msg11.obj = Nmsg;
                            mHandler.sendMessage(msg11);



                        }


                        monsendmsgListy.set(monsendmsgListi,msgs);

                        monsendmsgListi++;
*/

                    }
                }).start();
            }
        }
    }


    public void remove(int position) {
        if (messages2.size() > 0) {
            messages2.remove(position);
            adapter_chat2.notifyItemRemoved(position);
        }
    }


    public void removeone(int position) {
        if (messagesgift.size() > 0) {
            messagesgift.remove(position);
            adapter_gift.notifyItemRemoved(position);
        }
    }


    //主播切换返回主播间
    void addMessageToList(TIMMessage timMessage) {
        try {
            final TIMElem elem = timMessage.getElement(0);
            JSONObject attr = new JSONObject(new String(((TIMCustomElem) elem)
                    .getData(), "utf-8"));
            JSONObject attrr = new JSONObject();
            attrr.put("user", attr.getJSONObject("user"));
            attrr.put("text", attr.getJSONObject("command").getString("des"));
            attrr.put("type", 5);
            messages.add(0, attrr);
            adapter_chat.notifyItemInserted(0);
            list_chat.smoothScrollToPosition(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void addMessageToListtwo(TIMMessage timMessage) {
        final TIMElem elem = timMessage.getElement(0);
        if (elem.getClass().isAssignableFrom(TIMTextElem.class)) {
            TIMTextElem textElem = (TIMTextElem) elem;
            JSONObject attr = HXUtil
                    .createLiveChatBasicMessageText(textElem.getText());
            messages.add(0, attr);
            mHandler.sendEmptyMessage(chatmessage);
        } else {
            try {
                JSONObject attr = new JSONObject(new String(((TIMCustomElem) elem)
                        .getData(), "utf-8"));
                messages.add(0, attr);
                mHandler.sendEmptyMessage(chatmessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void handleCustomMsg(TIMElem elem) {
        Logger.out(" inviteVC handleCustomMsg  ");
        try {
            String customText = new String(((TIMCustomElem) elem)
                    .getData(), "UTF-8");
            Logger.out(" inviteVC handleCustomMsg  :" + customText);
            String splitItems[] = customText.split("&");
            //            if(splitItems.length<=3){
            //                splitItems[3]="";
            //            }
            int cmd = Integer.parseInt(splitItems[1]);
            for (int i = 0; i < splitItems.length; ++i) {
                Logger.out(" splitItems :" + splitItems[i] + " loop " + i);
            }
            switch (cmd) {
                case PRIASE_MSG:
                    int num = Integer.parseInt(splitItems[2]);
                    //                    praiseNum += num;
                    //                    tv_like_count.setText("" + praiseNum);
                    break;
                //用户登录消息
                case MEMBER_ENTER_MSG:
                    boolean isExist = false;
                    //判断是否已经群组存在
                    for (int i = 0; i < mMemberList.size(); ++i) {

                        String userPhone = mMemberList.get(i).userPhone;
                        if (userPhone.equals(splitItems[0])) {
                            isExist = true;
                            Logger.out(" willguo handleCustomMsg isExist = true  ");
                            break;
                        }
                    }
                    //不存在增加
                    if (!isExist) {
                        Logger.out("willguo handleCustomMsg  isExist = false");
                        MemberInfo member = null;
                        //包含完整信息
                        if (splitItems.length <= 3) {
                            member = new MemberInfo(splitItems[0], splitItems[2], "");
                        } else {
                            member = new MemberInfo(splitItems[0], splitItems[2], splitItems[3]);
                        }
                        if (!member.userPhone.equals(mSelfUserInfo.phone_num)) {
                            mMemberList.add(member);
                            mNormalMemberList.add(member);
                        }
                        //                        updateMemberHeadImage();
                        mHandler.sendEmptyMessage(UPDAT_MEMBER);
                    }
                    break;
                //用户登出消息
                case MEMBER_EXIT_MSG:
                    for (int i = 0; i < mMemberList.size(); ++i) {
                        String userPhone = mMemberList.get(i).userPhone;
                        if (userPhone.equals(splitItems[0])) {
                            Logger.out(
                                    "handleCustomMsg member leave userPhone " +
                                            userPhone);
                            mQavsdkControl.closeMemberView(userPhone);
                            mMemberList.remove(i);
                            viewIndexRemove(userPhone);
                            MemberInfo member = findMemberInfo(mVideoMemberList, userPhone);
                            if (member != null) {
                                Logger.out(
                                        "before  mVideoMemberList remove   " +
                                                mVideoMemberList.size());
                                mVideoMemberList.remove(member);
                                Logger.out("after mVideoMemberList remove " +
                                        mVideoMemberList.size());
                            } else {
                                MemberInfo normalMember = findMemberInfo(mNormalMemberList, userPhone);
                                mNormalMemberList.remove(normalMember);
                            }
                        }
                    }
                    updateMemberView();
                    break;
                case VIDEOCHAT_INVITE:
                    showInviteDialog();
                    break;
                case YES_I_JOIN:
                    //对方答应加入
                    String memberIdentifier = splitItems[0];
                    Logger.out(
                            "handleCustomMsg YES_I_JOIN+ " + memberIdentifier);
                    upMemberLevel(memberIdentifier);
                    //                    acceptHideMaskView(memberIdentifier);

                    requestMultiView(memberIdentifier);
                    //                    acceptHideMaskView(memberIdentifier);
                    break;
                case NO_I_REFUSE:
                    String memberIdentifier2 = splitItems[0];
                    //
                    //                    refuseHideMaskView(memberIdentifier2);
                    Toast.makeText(AvActivity.this, memberIdentifier2 +
                            "memberIdentifier2 refuese !", Toast.LENGTH_SHORT)
                         .show();
                    break;
                case MUTEVOICE:
                    AVAudioCtrl avAudioCtrl = mQavsdkControl.getAVContext()
                                                            .getAudioCtrl();
                    if (!OpenVoice) {
                        Toast.makeText(AvActivity.this, "host open your voice ", Toast.LENGTH_SHORT)
                             .show();
                        avAudioCtrl.enableMic(true);
                        OpenVoice = true;
                    } else {
                        Toast.makeText(AvActivity.this, "host close your voice ", Toast.LENGTH_SHORT)
                             .show();
                        avAudioCtrl.enableMic(false);
                        OpenVoice = false;
                    }
                    //关闭Mic
                    break;
                case UNMUTEVOICE:
                    Toast.makeText(AvActivity.this, "host allow your voice again ", Toast.LENGTH_SHORT)
                         .show();
                    //开启Mic
                    AVAudioCtrl avAudioCtrl2 = mQavsdkControl.getAVContext()
                                                             .getAudioCtrl();
                    avAudioCtrl2.enableMic(true);

                    break;
                case MUTEVIDEO:
                    if (!OpenVideo) {
                        //打开你的视频
                        Toast.makeText(AvActivity.this, "host open your camera  ", Toast.LENGTH_SHORT)
                             .show();
                        mQavsdkControl.toggleEnableCamera();
                        OpenVideo = true;
                    } else {
                        //关闭你的视频
                        Toast.makeText(AvActivity.this, "host close your camera ", Toast.LENGTH_SHORT)
                             .show();
                        mQavsdkControl.toggleEnableCamera();
                        OpenVideo = false;
                    }
                    break;
                case UNMUTEVIDEO://频道视频
                    Toast.makeText(AvActivity.this, "host allow your video again ", Toast.LENGTH_SHORT)
                         .show();
                    //开启Video
                    //                    if (mQavsdkControl.getIsInOnOffCamera() == false) {
                    mQavsdkControl.toggleEnableCamera();
                    //                    }
                    break;
                case CLOSEVIDEOSEND:
                    if (inviteDialog != null && inviteDialog.isShowing()) {
                        inviteDialog.dismiss();
                    }
                    Toast.makeText(AvActivity.this, "host close your video  ", Toast.LENGTH_SHORT)
                         .show();

                    if (mQavsdkControl.getIsEnableCamera() == true) {
                        mQavsdkControl.toggleEnableCamera();
                        OpenVideo = false;
                    }
                    AVAudioCtrl avAudioCtrl3 = mQavsdkControl.getAVContext()
                                                             .getAudioCtrl();
                    avAudioCtrl3.enableMic(false);
                    OpenVoice = false;

                default:
                    break;
            }
        } catch (UnsupportedEncodingException e) {
            Logger.out(" inviteVC handleCustomMsg  " + e.toString());
        }
    }


    private void showInviteDialog() {
        inviteDialog = new Dialog(this, R.style.dialog);
        //        inviteDialog.setContentView(R.layout.vc_invitedialog);
        inviteDialog.setCancelable(false);
        Button exitOk = (Button) inviteDialog
                .findViewById(R.id.btn_exit_cancel);
        exitOk.setVisibility(View.GONE);
        Button exitCancel = (Button) inviteDialog
                .findViewById(R.id.btn_exit_ok);
        exitCancel.setVisibility(View.GONE);
        exitOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //接受邀请打开自己摄像头
                if (mQavsdkControl.getIsEnableCamera() == false) {
                    mQavsdkControl.toggleEnableCamera();
                    OpenVideo = true;
                }
                //打开Mic
                AVAudioCtrl avAudioCtrl = mQavsdkControl.getAVContext()
                                                        .getAudioCtrl();
                avAudioCtrl.enableMic(true);
                OpenVoice = true;
                //回应消息
                anwserVCInvitation(mHostIdentifier, true);
                inviteDialog.dismiss();
            }
        });

        exitCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //   startOrientationListener();
                //回应拒绝消息
                anwserVCInvitation(mHostIdentifier, false);
                inviteDialog.dismiss();
            }
        });
        //        stopOrientationListener();
        inviteDialog.show();
    }


    private void updateMemberView() {

        if (members != null) {

            if (members.size() > 20) {
                //     Mersort(members, members.size());
                adapter_member.notifyDataSetChanged();
                //  adapter_member.notifyItemChanged(members.size() - 21);
            } else {
                adapter_member.notifyDataSetChanged();
            }
            //     list_member.smoothScrollToPosition(0);

        }

        Logger.out("IMGroupSystem updateMemberView memberNum " +
                mVideoMemberList.size());
        if (hostMember != null) {
            //            String headurl = HttpUtil.rootUrl + "?imagepath=" + hostMember.getHeadImagePath() + "&width=0&height=0";
            //            imageLoader.displayImage(headurl, hostHead);
            Logger.out("host avatar:" + hostMember.headImagePath);
            /**在此修复头像不显示问题，hostMember新new的对象没有赋值**/
            HXImageLoader.loadImage(hostHead, getIntent()
                    .getStringExtra("photo"), Utils.getPX(38), Utils.getPX(38));
        }
    }


    private long onSendPraise = System.currentTimeMillis();


    //发赞
    private void onSendPraise(boolean issend) {
        if (mSelfUserInfo.type == 2) {
            return;
        }
        if ((System.currentTimeMillis() - onSendPraise) < 200) {
            zan.addFavor();
            //            zan.addHeart(R.color.red);;
            spitCount++;
            //            wave_view.setPercent(spitCount * 10);
            if (spitCount * 10 > 90) {
                spitCount = 0;
                //                wave_view.setPercent(5);
                zan.addFavorbig();
            }
            // drawHeart(true);
            return;
        }
        onSendPraise = System.currentTimeMillis();
        TIMMessage Nmsg = new TIMMessage();
        TIMCustomElem ce = new TIMCustomElem();
        ce.setData((HXUtil.createLiveChatLikeMessage().toString().getBytes()));
        if (Nmsg.addElement(ce) != 0) {
            return;
        }
        if (mConversation == null) {
            return;
        }
        mConversation.sendMessage(Nmsg, new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {
                if (i == 85) {
                    mHandler.sendEmptyMessage(ERROR_MESSAGE_TOO_LONG);
                } else if (i == 6011) {
                    mHandler.sendEmptyMessage(ERROR_ACCOUNT_NOT_EXIT);
                }
            }


            @Override
            public void onSuccess(TIMMessage timMessage) {
                //                Message msg = new Message();
                //                msg.what = REFRESH_CHAT;
                //                msg.obj = timMessage;
                //                mHandler.sendMessage(msg);
                like_count++;
                zan.addFavor();
                //                priace_num.setText(like_count + "个赞");
                //      drawHeart(true);
            }
        });

        if (issend) {
            //第一次点赞
            this.issend = false;
            isshow = 0;
            //点赞告诉上线了
            onMemberlight();
            sendPraiseToServer();
        }
    }


    private void sendPraiseToServer() {
        HashMap<String, Object> para = new HashMap<>();
        JSONObject jo = new JSONObject();
        try {
            jo.put("room_num", roomNum);
            jo.put("user_id", mHostIdentifier);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        para.put("praisedata", jo);
        HXJavaNet.post(HXJavaNet.url_live_like, para, new NetCallBack(this) {
            @Override
            public void onSuccess(int ret_code, String ret_data, String msg) {
                super.onSuccess(ret_code, ret_data, msg);
                issend = false;
            }
        });
    }


    /**
     * 发一条消息告诉大家 自己上线了
     */

    private void onMemberEnter() {
        if (!mSelfUserInfo.isCreater) {

            TIMMessage Tim = new TIMMessage();
            TIMCustomElem elem = new TIMCustomElem();
            elem.setData(HXUtil.createLiveChatRoomMessageentroom(true,
                    perso_num_count + "").toString().getBytes());
            //        elem.setData(message.getBytes());
            elem.setDesc(UNREAD);
            if (1 == Tim.addElement(elem)) {
                Toast.makeText(getApplicationContext(), "enter error", Toast.LENGTH_SHORT)
                     .show();
            } else {
                mConversation
                        .sendMessage(Tim, new TIMValueCallBack<TIMMessage>() {
                            @Override
                            public void onError(int i, String s) {
                                Logger.out("enter error" + i + ": " + s);
                            }


                            @Override
                            public void onSuccess(TIMMessage timMessage) {
                                try {
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject
                                            .put("grade", mSelfUserInfo.grade);
                                    jsonObject.put("enter_message",
                                            mSelfUserInfo.nickname + "进入了房间");

                                    if (mSelfUserInfo.grade >= 10) {
                                        if (!mSelfUserInfo.isCreater) {
                                            if (localendterRoom.size() <= 1) {
                                                localendterRoom
                                                        .offer(jsonObject);
                                                enterRoomAnimation();
                                            } else {
                                                localendterRoom
                                                        .offer(jsonObject);
                                            }
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        }
        JSONObject user = HXUtil.createLiveChatBasicMessage()
                                .optJSONObject("user");
        if (members != null) {
            if (!mSelfUserInfo.isCreater) {
                boolean add = adapter_member.add(user, true);
                perso_num.setText(perso_num_count + "观众");
            }
        }
        //进房系统消息

    }


    public void TIMElemTypeJInfang() {
        JSONObject attr = new JSONObject();
        JSONObject user1 = new JSONObject();
        try {
            user1.put("user_id", "npc");
            user1.put("user_name", "系统消息");
            user1.put("user_is_vip", 1);
            attr.put("user", user1);
            attr.put("text", "禁止低俗言论、色情、赌博  毒品、暴力反动、政治军事等，敏感、冒充官方的直播内容；网管会进行24小时在线巡查哦！");
            attr.put("type", 6);
        } catch (JSONException e) {

            e.printStackTrace();
        }
        messages.add(0, attr);
        adapter_chat.notifyItemInserted(0);
        list_chat.smoothScrollToPosition(0);
    }


    //点一次点赞
    private void onMemberlight() {
        if (mSelfUserInfo.nickname == null) {
            mSelfUserInfo.nickname = "null";
        }

        if ("".equals(mSelfUserInfo.photo)) {
            mSelfUserInfo.photo = "null";
        }

        TIMMessage Tim = new TIMMessage();
        TIMTextElem elem = new TIMTextElem();
        elem.setText("点亮了❤");
        TIMCustomElem mTimcustomelem = new TIMCustomElem();
        mTimcustomelem.setData(HXUtil.createLiveChatBasicMessageText("点亮了❤")
                                     .toString().getBytes());
        mTimcustomelem.setDesc(UNREAD);
        Tim.addElement(elem);
        Tim.addElement(mTimcustomelem);

        mConversation.sendMessage(Tim, new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {
                Logger.out("enter error" + i + ": " + s);
            }


            @Override
            public void onSuccess(TIMMessage timMessage) {
                Message msg11 = new Message();
                msg11.what = REFRESH_MESSAGE;
                msg11.obj = timMessage;
                mHandler.sendMessage(msg11);
            }
        });
    }


    /**
     * 发起邀请消息
     */
    private void sendVCInvitation(String inviteIdentifier) {
        if (mSelfUserInfo.nickname == null) {

            mSelfUserInfo.nickname = "null";
        }

        if ("".equals(mSelfUserInfo.photo)) {
            mSelfUserInfo.photo = "null";
        }

        String message = mSelfUserInfo.phone_num + "&" +
                VIDEOCHAT_INVITE +
                "&" + mSelfUserInfo.nickname + "&" + inviteIdentifier +
                "&";

        Logger.out("inviteVC sendVCInvitation " + message);

        TIMMessage Tim = new TIMMessage();

        TIMCustomElem elem = new TIMCustomElem();

        elem.setData(message.getBytes());
        elem.setDesc(UNREAD);
        if (1 == Tim.addElement(elem)) {
            Toast.makeText(getApplicationContext(), "enter error", Toast.LENGTH_SHORT)
                 .show();
        } else {
            testConversation = TIMManager.getInstance()
                                         .getConversation(TIMConversationType.C2C, inviteIdentifier);
            testConversation
                    .sendMessage(Tim, new TIMValueCallBack<TIMMessage>() {
                        @Override
                        public void onError(int i, String s) {
                            Logger.out("enter error" + i + ": " + s);
                        }


                        @Override
                        public void onSuccess(TIMMessage timMessage) {
                            TIMCustomElem elem = (TIMCustomElem) (timMessage
                                                                          .getElement(0));
                            try {
                                String text = new String(elem
                                        .getData(), "utf-8");

                                Logger.out(
                                        "inviteVC sendVCInvitation send groupmsg enter  success :" +
                                                text);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }


    /**
     * 主播发送成员消息VOICE命令
     */
    private void sendVoiceACK(boolean isMute, String inviteIdentifier) {
        if (mSelfUserInfo.nickname == null) {
            mSelfUserInfo.nickname = "null";
        }
        if ("".equals(mSelfUserInfo.photo)) {
            mSelfUserInfo.photo = "null";
        }
        String message;
        if (isMute) {
            message = mSelfUserInfo.phone_num + "&" + MUTEVOICE + "&" +
                    mSelfUserInfo.nickname + "&" + inviteIdentifier + "&";
        } else {
            message = mSelfUserInfo.phone_num + "&" + UNMUTEVOICE + "&" +
                    mSelfUserInfo.nickname + "&" + inviteIdentifier + "&";
        }

        Logger.out("inviteVC sendVCInvitation " + message);
        TIMMessage Tim = new TIMMessage();
        TIMCustomElem elem = new TIMCustomElem();
        elem.setData(message.getBytes());
        elem.setDesc(UNREAD);
        if (1 == Tim.addElement(elem)) {
            Toast.makeText(getApplicationContext(), "enter error", Toast.LENGTH_SHORT)
                 .show();
        } else {
            //if (!inviteIdentifier.startsWith("86-")) {
            //    inviteIdentifier = inviteIdentifier;
            //}
            testConversation = TIMManager.getInstance()
                                         .getConversation(TIMConversationType.C2C, inviteIdentifier);
            testConversation
                    .sendMessage(Tim, new TIMValueCallBack<TIMMessage>() {
                        @Override
                        public void onError(int i, String s) {
                            Logger.out("enter error" + i + ": " + s);
                        }


                        @Override
                        public void onSuccess(TIMMessage timMessage) {
                            TIMCustomElem elem = (TIMCustomElem) (timMessage
                                                                          .getElement(0));
                            try {
                                String text = new String(elem
                                        .getData(), "utf-8");
                                Logger.out(
                                        "inviteVC sendVCInvitation send groupmsg enter  success :" +
                                                text);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }


    private void sendCloseVideoMsg(String inviteIdentifier) {
        if (mSelfUserInfo.nickname == null) {
            mSelfUserInfo.nickname = "null";
        }
        if ("".equals(mSelfUserInfo.photo)) {
            mSelfUserInfo.photo = "null";
        }
        String message = mSelfUserInfo.phone_num + "&" + CLOSEVIDEOSEND +
                "&" + mSelfUserInfo.nickname + "&" + inviteIdentifier +
                "&";

        Logger.out("inviteVC sendVCInvitation " + message);
        TIMMessage Tim = new TIMMessage();
        TIMCustomElem elem = new TIMCustomElem();
        elem.setData(message.getBytes());
        elem.setDesc(UNREAD);
        if (1 == Tim.addElement(elem)) {
            Toast.makeText(getApplicationContext(), "enter error", Toast.LENGTH_SHORT)
                 .show();
        } else {
            //if (!inviteIdentifier.startsWith("86-")) {
            //    inviteIdentifier = inviteIdentifier;
            //}
            testConversation = TIMManager.getInstance()
                                         .getConversation(TIMConversationType.C2C, inviteIdentifier);
            testConversation
                    .sendMessage(Tim, new TIMValueCallBack<TIMMessage>() {
                        @Override
                        public void onError(int i, String s) {
                            Logger.out("enter error" + i + ": " + s);
                        }


                        @Override
                        public void onSuccess(TIMMessage timMessage) {
                            TIMCustomElem elem = (TIMCustomElem) (timMessage
                                                                          .getElement(0));
                            try {
                                String text = new String(elem
                                        .getData(), "utf-8");
                                Logger.out(
                                        "inviteVC sendVCInvitation send groupmsg enter  success :" +
                                                text);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }


    private void anwserVCInvitation(String hostidentifier, boolean anwser) {
        if (mSelfUserInfo.nickname == null) {
            mSelfUserInfo.nickname = "null";
        }
        if ("".equals(mSelfUserInfo.photo)) {
            mSelfUserInfo.photo = "null";
        }
        String message;
        if (anwser) {
            message = mSelfUserInfo.phone_num + "&" + YES_I_JOIN + "&" +
                    mSelfUserInfo.nickname + "&" + hostidentifier + "&";
        } else {
            message = mSelfUserInfo.phone_num + "&" + NO_I_REFUSE + "&" +
                    mSelfUserInfo.nickname + "&" + hostidentifier + "&";
        }

        Logger.out("anwserVCInvitation " + message);
        TIMMessage Tim = new TIMMessage();
        TIMCustomElem elem = new TIMCustomElem();
        elem.setData(message.getBytes());
        elem.setDesc(UNREAD);
        if (1 == Tim.addElement(elem)) {
            Toast.makeText(getApplicationContext(), "enter error", Toast.LENGTH_SHORT)
                 .show();
        } else {
            testConversation = TIMManager.getInstance()
                                         .getConversation(TIMConversationType.C2C, hostidentifier);
            testConversation
                    .sendMessage(Tim, new TIMValueCallBack<TIMMessage>() {
                        @Override
                        public void onError(int i, String s) {
                            Logger.out("enter error" + i + ": " + s);
                        }


                        @Override
                        public void onSuccess(TIMMessage timMessage) {
                            TIMCustomElem elem = (TIMCustomElem) (timMessage
                                                                          .getElement(0));
                            try {
                                String text = new String(elem
                                        .getData(), "utf-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }


    /**
     * 发一条消息通知大家 自己下线了
     */
    private void onMemberExit() {
        //        mHandler.sendEmptyMessage(MEMBER_EXIT_COMPLETE);
        if (mConversation == null) {
            return;
        }

        TIMMessage Tim = new TIMMessage();
        TIMCustomElem elem = new TIMCustomElem();
        elem.setData(HXUtil.createLiveChatRoomMessage(false).toString()
                           .getBytes());
        //        elem.setData(message.getBytes());
        elem.setDesc(UNREAD);
        if (1 == Tim.addElement(elem)) {

        } else {
            mConversation.sendMessage(Tim, new TIMValueCallBack<TIMMessage>() {
                @Override
                public void onError(int i, String s) {
                    Logger.out("exit error" + i + ": " + s);
                }


                @Override
                public void onSuccess(TIMMessage timMessage) {
                }
            });
        }
        mMemberList.clear();
    }


    private void sendCloseMsg() {
        mHandler.sendEmptyMessage(CLOSE_VIDEO);
    }


    boolean isFirstget = true;


    @Override
    protected Dialog onCreateDialog(int id) {

        Dialog dialog = null;

        switch (id) {
            case DIALOG_INIT:
                dialog = mDialogInit = Utils
                        .newProgressDialog(this, R.string.interface_initialization);
                break;
            case DIALOG_AT_ON_CAMERA:
                dialog = mDialogAtOnCamera = Utils
                        .newProgressDialog(this, R.string.at_on_camera);
                break;
            case DIALOG_ON_CAMERA_FAILED:

                dialog = Utils
                        .newErrorDialogtwo(this, R.string.on_camera_failed);
                break;
            case DIALOG_AT_OFF_CAMERA:
                dialog = mDialogAtOffCamera = Utils
                        .newProgressDialog(this, R.string.at_off_camera);
                break;
            case DIALOG_OFF_CAMERA_FAILED:
                dialog = Utils.newErrorDialog(this, R.string.off_camera_failed);
                break;
            case DIALOG_AT_SWITCH_FRONT_CAMERA:
                dialog = mDialogAtSwitchFrontCamera = Utils
                        .newProgressDialog(this, R.string.at_switch_front_camera);
                break;
            case DIALOG_SWITCH_FRONT_CAMERA_FAILED:
                dialog = Utils
                        .newErrorDialog(this, R.string.switch_front_camera_failed);
                break;
            case DIALOG_AT_SWITCH_BACK_CAMERA:
                dialog = mDialogAtSwitchBackCamera = Utils
                        .newProgressDialog(this, R.string.at_switch_back_camera);
                break;
            case DIALOG_SWITCH_BACK_CAMERA_FAILED:
                dialog = Utils
                        .newErrorDialog(this, R.string.switch_back_camera_failed);
                break;
            case DIALOG_DESTROY:
                dialog = mDialogAtDestroy = Utils
                        .newProgressDialog(this, R.string.at_close_room);
                break;
            default:
                break;
        }
        return dialog;
    }


    //向用户服务器获取成员信息
    private void getMemberInfo(int user_id, int roomNum, int query_begin_count) {
        Logger.out("userServer getMemberInfo hasPullMemberList ? " +
                hasPullMemberList);
        if (members != null) {
            if (isFirstget) {
                isFirstget = false;
            }
            HashMap<String, Object> para = new HashMap<>();
            para.put("user_id", user_id);
            para.put("room_num", roomNum);
            para.put("query_begin_count", query_begin_count);
            para.put("limit", 20);
            NetCallBack netCallBack = new NetCallBack(this) {
                @Override
                public void onSuccess(int ret_code, String ret_data, String msg) {
                    super.onSuccess(ret_code, ret_data, msg);
                    if (act.get() == null) {
                        return;
                    }
                    try {
                        JSONArray array = new JSONArray(ret_data);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jo = array.getJSONObject(i);
                            JSONObject user = new JSONObject();
                            user.put("user_id", jo.optString("user_id"));
                            user.put("user_name", jo.optString("nickname"));
                            user.put("user_avatar", jo.optString("photo"));
                            user.put("address", jo.optString("address"));
                            user.put("ticket_amount", jo
                                    .optString("ticket_amount"));
                            user.put("fans_amount", jo.optInt("fans_amount"));
                            user.put("relation_type", jo
                                    .optInt("relation_type"));
                            user.put("grade", jo.optString("grade"));
                            user.put("gender", jo.optString("gender"));
                            user.put("send_out_vca", jo
                                    .optString("send_out_vca"));

                            if ((act.get().mSelfUserInfo.user_id + "")
                                    .equalsIgnoreCase(jo
                                            .optString("user_id"))) {
                                continue;
                            }
                            if (act.get().mHostIdentifier
                                    .equals(jo.optString("user_id"))) {
                                continue;
                            }
                            if (act.get().members == null) {
                                return;
                            }
                            boolean add = act.get().adapter_member
                                    .add(user, true);
                       /*     if (add) {
                                perso_num_count++;
                                perso_num.setText(perso_num_count + "观众");
                            }*/
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            WeakReference<NetCallBack> netCallBackWeakReference = new WeakReference<>(netCallBack);

            HXJavaNet
                    .post(HXJavaNet.url_onlineUsersInRoomByPage, para, netCallBackWeakReference
                            .get());
        }
    }


    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case DIALOG_ON_CAMERA_FAILED:
            case DIALOG_OFF_CAMERA_FAILED:
                //                ((AlertDialog) dialog).setMessage(getString(R.string.error_code_prefix) + mOnOffCameraErrorCode);
                break;
            case DIALOG_SWITCH_FRONT_CAMERA_FAILED:
            case DIALOG_SWITCH_BACK_CAMERA_FAILED:
                ((AlertDialog) dialog).setMessage(
                        getString(R.string.error_code_prefix) +
                                mSwitchCameraErrorCode);
                break;
            default:
                break;
        }
    }


    private void refreshCameraUI() {
        boolean isEnable = mQavsdkControl.getIsEnableCamera();
        boolean isFront = mQavsdkControl.getIsFrontCamera();
        boolean isInOnOffCamera = mQavsdkControl.getIsInOnOffCamera();
        boolean isInSwitchCamera = mQavsdkControl.getIsInSwitchCamera();

        if (isInOnOffCamera) {
            if (isEnable) {
                Utils.switchWaitingDialog(this, mDialogAtOffCamera, DIALOG_AT_OFF_CAMERA, true);
                Utils.switchWaitingDialog(this, mDialogAtOnCamera, DIALOG_AT_ON_CAMERA, false);
            } else {
                Utils.switchWaitingDialog(this, mDialogAtOffCamera, DIALOG_AT_OFF_CAMERA, false);
                //                Utils.switchWaitingDialog(this, mDialogAtOnCamera, DIALOG_AT_ON_CAMERA, true);
                Utils.switchWaitingDialog(this, mDialogAtOnCamera, DIALOG_AT_ON_CAMERA, false);
            }
        } else {
            Utils.switchWaitingDialog(this, mDialogAtOffCamera, DIALOG_AT_OFF_CAMERA, false);
            Utils.switchWaitingDialog(this, mDialogAtOnCamera, DIALOG_AT_ON_CAMERA, false);
        }

        if (isInSwitchCamera) {
            if (isFront) {

                Utils.switchWaitingDialog(this, mDialogAtSwitchBackCamera, DIALOG_AT_SWITCH_BACK_CAMERA, true);
                Utils.switchWaitingDialog(this, mDialogAtSwitchFrontCamera, DIALOG_AT_SWITCH_FRONT_CAMERA, false);
            } else {
                Utils.switchWaitingDialog(this, mDialogAtSwitchBackCamera, DIALOG_AT_SWITCH_BACK_CAMERA, false);
                Utils.switchWaitingDialog(this, mDialogAtSwitchFrontCamera, DIALOG_AT_SWITCH_FRONT_CAMERA, true);
            }
        } else {
            Utils.switchWaitingDialog(this, mDialogAtSwitchBackCamera, DIALOG_AT_SWITCH_BACK_CAMERA, false);
            Utils.switchWaitingDialog(this, mDialogAtSwitchFrontCamera, DIALOG_AT_SWITCH_FRONT_CAMERA, false);
        }
    }


    //传感器
    void registerOrientationListener() {
        if (mOrientationEventListener == null) {
            mOrientationEventListener = new VideoOrientationEventListener(super
                    .getApplicationContext(), SensorManager.SENSOR_DELAY_UI);
        }
    }


    void startOrientationListener() {
        if (mOrientationEventListener != null) {
            mOrientationEventListener.enable();
        }
    }


    void stopOrientationListener() {
        if (mOrientationEventListener != null) {
            mOrientationEventListener.disable();
        }
    }


    private void removeChatItem() {
        time += 2;
    }


    private void hostCloseAlertDialog() {

        final ShowDialog showDialog = new ShowDialog(this, "粉丝们正在赶来的路上,确定结束直播吗?", "结束直播", "继续直播");
        showDialog.setClicklistener(new ShowDialog.ClickListenerInterface() {
            @Override
            public void doConfirm() {
                stopRecord(false);
                PushStop();
                showDialog.dismiss();
            }


            @Override
            public void doCancel() {
                showDialog.dismiss();
            }
        });
        showDialog.show();
    }


    public void memberCloseAlertDialog() {

        onMemberExit();//下线通知
        av_video_glview.onPause();
        onCloseVideo(false, false);
    }


    private boolean oncloseavactivity = false;


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                boolean handled = false;

                if (v_gift_pad.getVisibility() != View.GONE) {
                    v_gift_pad.setVisibility(View.GONE);
                    view_liv_send_button.setVisibility(View.GONE);
                    fl_background.setVisibility(View.GONE);
                    ll_give.setVisibility(View.GONE);
                    ll_give2.setVisibility(View.GONE);
                    ll_give3.setVisibility(View.GONE);
                    ll_give4.setVisibility(View.GONE);
                    //                    v_chat_layout.setVisibility(View.VISIBLE);
                    b_message.setVisibility(View.VISIBLE);
                    amr_hangup.setVisibility(View.VISIBLE);
                    list_chat.setVisibility(View.VISIBLE);
                    act_live_private_message.setVisibility(View.VISIBLE);
                    if (!mSelfUserInfo.isCreater) {
                        b_fenxiang.setVisibility(View.VISIBLE);
                        b_gift.setVisibility(View.VISIBLE);
                    }
                    handled = true;
                    BurstsGift();
                }

                if (handled) {
                    break;
                }
                if (share_rl.getVisibility() == View.VISIBLE) {
                    memberCloseAlertDialog();
                    break;
                }
                if (mSelfUserInfo.isCreater) {
                    hostCloseAlertDialog();
                } else {
                    memberCloseAlertDialog();
                }
                break;
        }

        return false; //这一句很关键
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


    public MemberInfo findMemberInfo(ArrayList<MemberInfo> list, String id) {
        Logger.out("findMemberInfo id" + id);
        String identifier = "";
        if (id.startsWith("86-")) {
            identifier = id.substring(3);
        } else {
            identifier = id;
        }
        Logger.out("findMemberInfo identifier " + identifier);
        for (MemberInfo member : list) {

            if (member.userPhone.equals(identifier)) {
                return member;
            }
        }
        return null;
    }


    public void upMemberLevel(String identifier) {
        MemberInfo upMember = findMemberInfo(mNormalMemberList, identifier);
        mVideoMemberList.add(upMember);
        mNormalMemberList.remove(upMember);
    }


    public void downMemberLevel(String identifier) {
        MemberInfo upMember = findMemberInfo(mVideoMemberList, identifier);
        mNormalMemberList.add(upMember);
        mVideoMemberList.remove(upMember);
    }


    public ArrayList<MemberInfo> copyToNormalMember() {
        mNormalMemberList = new ArrayList<MemberInfo>();
        for (MemberInfo member : mMemberList) {
            mNormalMemberList.add(member);
        }
        return mNormalMemberList;
    }


    private void closeVideoMemberByHost(String identifer) {
        viewIndexRemove(identifer);
        sendCloseVideoMsg(identifer);
        mQavsdkControl.closeMemberView(identifer);
        downMemberLevel(identifer);
    }


    private void viewIndexRemove(String identifer) {
        if (viewIndex != null) {
            String id;
            if (identifer.startsWith("86-")) {
                id = identifer.substring(3);
            } else {
                id = identifer;
            }
            if (viewIndex.containsKey(id)) {
                viewIndex.remove(id);
            }
        }
    }


    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getRawX();
        int y = (int) ev.getRawY();
        if (tipsViewer.getVisibility() == View.VISIBLE ||
                tipsCreater.getVisibility() == View.VISIBLE) {
            tipsCreater.setVisibility(View.GONE);
            tipsViewer.setVisibility(View.GONE);
        }
        if (b_flash.getVisibility() == View.VISIBLE &&
                !studio.archangel.toolkitv2.util.Util
                        .isWithinViewsBound(x, y, v_function)) {
            b_flash.setVisibility(View.GONE);
            b_camera.setVisibility(View.GONE);
            return true;
        }
        reSolveEvent(ev);
        return super.dispatchTouchEvent(ev);
    }


    public boolean inView(int x, int y, View view) {
        int left, top, right, bottom;
        if (view == null) {
            return false;
        }
        left = (int) view.getX();
        top = view.getTop();
        right = left + view.getWidth();
        bottom = view.getBottom();
        Rect rect = new Rect(left, top, right, bottom);
        return rect.contains(x, y);
    }


    public boolean inView(int l, int t, int r, int b, int x, int y) {
        int left, top, right, bottom;
        left = l;
        top = t;
        right = r;
        bottom = b;
        Rect rect = new Rect(left, top, right, bottom);
        return rect.contains(x, y);
    }


    public void reSolveEvent(MotionEvent event) {
        final int action = event.getAction();
        Logger.out("action:" + action);
        if (action == MotionEvent.ACTION_UP) {
            Logger.out("ACTION_POINTER_UP");
            if (isScroll) {
                float ss = Math.abs(x_length) * 100 / width;
                final float dx = zan.getX() - qav_top_bar.getX();
                if (ss >= 20) {
                    if (isRight) {
                        isRight = false;

                        ValueAnimator valueAnimator = ValueAnimator
                                .ofInt((int) qav_top_bar.getX(), 0);
                        valueAnimator
                                .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator animation) {
                                        int animatedValue = (int) animation
                                                .getAnimatedValue();
                                        qav_top_bar.setX(animatedValue);
                                        qav_bottom_bar.setX(animatedValue);
                                        zan.setX(animatedValue + dx);
                                    }
                                });
                        valueAnimator.setDuration(150);
                        valueAnimator.start();
                    } else {
                        isRight = true;
                        ValueAnimator valueAnimator = ValueAnimator
                                .ofInt((int) qav_top_bar.getX(), width);
                        valueAnimator
                                .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator animation) {
                                        int animatedValue = (int) animation
                                                .getAnimatedValue();
                                        qav_top_bar.setX(animatedValue);
                                        qav_bottom_bar.setX(animatedValue);
                                        zan.setX(animatedValue + dx);
                                    }
                                });
                        valueAnimator.setDuration(150);
                        valueAnimator.start();
                    }
                } else {
                    if (isRight) {
                        ValueAnimator valueAnimator = ValueAnimator
                                .ofInt((int) qav_top_bar.getX(), width);
                        valueAnimator
                                .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator animation) {
                                        int animatedValue = (int) animation
                                                .getAnimatedValue();
                                        qav_top_bar.setX(animatedValue);
                                        qav_bottom_bar.setX(animatedValue);
                                        zan.setX(animatedValue + dx);
                                    }
                                });
                        valueAnimator.setDuration(150);
                        valueAnimator.start();
                    } else {
                        isRight = false;
                        ValueAnimator valueAnimator = ValueAnimator
                                .ofInt((int) qav_top_bar.getX(), 0);
                        valueAnimator
                                .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator animation) {
                                        int animatedValue = (int) animation
                                                .getAnimatedValue();
                                        qav_top_bar.setX(animatedValue);
                                        qav_bottom_bar.setX(animatedValue);
                                        zan.setX(animatedValue + dx);
                                    }
                                });
                        valueAnimator.setDuration(150);
                        valueAnimator.start();
                    }
                }
                x_length = 0;
                isScroll = false;
            }
            enableSclide = true;
        } else {
            gestureDetector.onTouchEvent(event);
        }
    }


    //摄像头旋转
    class VideoOrientationEventListener extends OrientationEventListener {
        boolean mbIsTablet = false;
        int mLastOrientation = -25;


        public VideoOrientationEventListener(Context context, int rate) {
            super(context, rate);
            mbIsTablet = PhoneStatusTools.isTablet(context);
        }


        @Override
        public void onOrientationChanged(int orientation) {
            if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
                mLastOrientation = orientation;
                return;
            }

            if (mLastOrientation < 0) {
                mLastOrientation = 0;
            }

            if (((orientation - mLastOrientation) < 20) &&
                    ((orientation - mLastOrientation) > -20)) {
                return;
            }

            if (mbIsTablet) {

                orientation -= 90;
                if (orientation < 0) {
                    orientation += 360;
                }
            }
            mLastOrientation = orientation;

            if (orientation > 314 || orientation < 45) {
                if (mQavsdkControl != null) {
                    mQavsdkControl.setRotation(0);
                }
                mRotationAngle = 0;
            } else if (orientation > 44 && orientation < 135) {
                if (mQavsdkControl != null) {
                    mQavsdkControl.setRotation(90);
                }
                mRotationAngle = 90;
            } else if (orientation > 134 && orientation < 225) {
                if (mQavsdkControl != null) {
                    mQavsdkControl.setRotation(180);
                }
                mRotationAngle = 180;
            } else {
                if (mQavsdkControl != null) {
                    mQavsdkControl.setRotation(270);
                }
                mRotationAngle = 270;
            }
        }
    }

    //获取de方法


    public int getKjwidth(int id) {
        Resources r = this.getResources();
        return (int) r.getDimension(id);
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.fade_out);
    }


    ImageView tanmubutton;


    private void initdanmu() {
        //弹幕
        tanmubutton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tanmuboolean) {
                    tanmubutton.setImageResource(R.drawable.tuanmubutton1);
                    mEditTextInputMsg.setHint("一条大喇叭，需要1钻石");
                    tanmuboolean = true;
                } else {

                    tanmubutton.setImageResource(R.drawable.tanmubutton);
                    mEditTextInputMsg.setHint("说点什么吧");
                    tanmuboolean = false;
                }
            }
        });
    }


    //提升管理员权限start
    private showPLPupwindows pw;

    private static class NetCallBack extends AngelNetCallBack {
        public WeakReference<AvActivity> act;


        public NetCallBack(AvActivity avActivity) {
            act = new WeakReference<AvActivity>(avActivity);
        }


        @Override
        public void onSuccess(int ret_code, String ret_data, String msg) {
        }


        @Override
        public void onFailure(String msg) {
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        UserInfo userInfo = HXApp.getInstance().getUserInfo();
        if (userInfo == null) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3) {
            getUserAccount();
        }

        // 管理员版本需要穿的图pain

        if (requestCode == 161) {
            File f = new File(
                    getDir("temp", Context.MODE_PRIVATE).getAbsolutePath() +
                            "/temp_" + System.currentTimeMillis() + ".jpg");
            final String cropped_file = f.getAbsolutePath();
            try {
                Uri originalUri = data.getData();        //获得图片的uri
                String[] proj = { MediaStore.Images.Media.DATA };
                //好像是android多媒体数据库的封装接口，具体的看Android文档
                Cursor cursor = managedQuery(originalUri, proj, null, null, null);
                //按我个人理解 这个是获得用户选择的图片的索引值
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                //将光标移至开头 ，这个很重要，不小心很容易引起越界
                cursor.moveToFirst();
                //最后根据索引值获取图片路径
                String path = cursor.getString(column_index);
                ImageUtils
                        .createImageThumbnail(mContext, path, cropped_file, 800, 100, 0);
            } catch (IOException e) {

                Log.e(TAG, e.toString());
            }
            String userID = String.valueOf(userInfo.user_id);
            HXUtil.getInstance()
                  .OSSUploadFile(mContext, cropped_file, userID, new AngelNetProgressCallBack() {
                      @Override
                      public void onProgress(long bytesRead, long contentLength, boolean done) {

                      }


                      @Override
                      public void onSuccess(int ret_code, String ret_data_suss, String msg) {
                          HashMap<String, Object> para = new HashMap<>();
                          para.put("handle_id", mSelfUserInfo.user_id);
                          para.put("handle_identity", mSelfUserInfo.identity);
                          para.put("screenshots", ret_data_suss);

                          switch (chechedidd) {
                              case R.id.seqingbaoli:
                                  chechedidd = 1;
                                  break;
                              case R.id.guanggaochaunxiao:
                                  chechedidd = 2;
                                  break;
                              case R.id.zhengzhimigan:
                                  chechedidd = 3;
                                  break;
                              case R.id.qifanbanquan:
                                  chechedidd = 4;
                                  break;
                          }

                          para.put("reason", chechedidd);
                          para.put("room_num", roomNum);
                          para.put("user_id", mHostIdentifier);
                          para.put("forbid_identity", mhostzhuidentity);
                          HXJavaNet
                                  .post(HXJavaNet.url_forbidUser, para, new NetCallBack(AvActivity.this) {
                                      @Override
                                      public void onSuccess(int ret_code, String ret_data, String msg) {
                                          if (ret_code == 200) {

                                              Notifier.showShortMsg(mContext, "封号成功");
                                          }
                                      }


                                      @Override
                                      public void onFailure(String msg) {

                                          Notifier.showShortMsg(mContext, "封号失败 ");
                                      }
                                  });
                      }


                      @Override
                      public void onFailure(String msg) {

                      }
                  });
        }

        if (requestCode == 0x2711 && resultCode == HXApp.result_ok) {
            if (data == null) {
                Notifier.showNormalMsg(mContext, "图片生成中遇到了问题...");
                return;
            }
            // 管理员版本需要穿的图pain
            File f = new File(
                    getDir("temp", Context.MODE_PRIVATE).getAbsolutePath() +
                            "/temp_" + System.currentTimeMillis() + ".jpg");
            final String cropped_file = f.getAbsolutePath();
            try {
                final String file = data.getStringExtra("file");   //获得图片的uri
                try {
                    ImageUtils
                            .createImageThumbnail(mContext, file, cropped_file, 800, 100, 0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (file == null) {
                    Notifier.showNormalMsg(mContext, "图片生成中遇到了问题...");
                    return;
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            HXUtil.getInstance().OSSUploadFile(mContext, cropped_file, String
                    .valueOf(userInfo.user_id), new AngelNetProgressCallBack() {
                @Override
                public void onProgress(long bytesRead, long contentLength, boolean done) {

                }


                @Override
                public void onSuccess(int ret_code, String ret_data_suss, String msg) {
                    HashMap<String, Object> para = new HashMap<>();
                    para.put("handle_id", mSelfUserInfo.user_id);
                    para.put("handle_identity", mSelfUserInfo.identity);
                    para.put("screenshots", ret_data_suss);
                    switch (chechedidd) {
                        case R.id.seqingbaoli:
                            chechedidd = 1;
                            break;
                        case R.id.guanggaochaunxiao:
                            chechedidd = 2;
                            break;
                        case R.id.zhengzhimigan:
                            chechedidd = 3;
                            break;
                        case R.id.qifanbanquan:
                            chechedidd = 4;
                            break;
                    }
                    para.put("reason", chechedidd);
                    para.put("room_num", roomNum);
                    para.put("user_id", mHostIdentifier);
                    para.put("forbid_identity", mhostzhuidentity);
                    HXJavaNet
                            .post(HXJavaNet.url_forbidUser, para, new NetCallBack(AvActivity.this) {
                                @Override
                                public void onSuccess(int ret_code, String ret_data, String msg) {
                                    if (ret_code == 200) {
                                        Notifier.showShortMsg(mContext, "封号成功");
                                    }
                                }


                                @Override
                                public void onFailure(String msg) {
                                    Notifier.showShortMsg(mContext, "封号失败 ");
                                }
                            });
                }


                @Override
                public void onFailure(String msg) {
                }
            });
        }
    }
}


