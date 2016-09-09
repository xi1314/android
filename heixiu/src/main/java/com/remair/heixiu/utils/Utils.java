package com.remair.heixiu.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import com.google.gson.Gson;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.R;
import com.tencent.av.sdk.AVConstants;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static final String EXTRA_RELATION_ID = "relationId";
    public static final String EXTRA_AV_ERROR_RESULT = "av_error_result";
    //独立模式
    //    public static int APP_ID_TEXT = 1400001862;
    //    public static String UID_TYPE = "1019";
    //托管模式
    //	public static final String APP_ID_TEXT = "1400001692";
    //	public static final String UID_TYPE = "884";
    public static final String EXTRA_VIDEO_SRC_TYPE = "videoSrcType";
    public static final String EXTRA_IS_ENABLE = "isEnable";
    public static final String EXTRA_IS_FRONT = "isFront";
    public static final String EXTRA_IDENTIFIER = "identifier";
    public static final String EXTRA_IS_ASKFOR_MEMVIDEO = "askfor_memvideo";
    public static final String EXTRA_SELF_IDENTIFIER = "selfIdentifier";

    public static final String EXTRA_ROOM_ID = "roomId";
    public static final String EXTRA_IS_VIDEO = "isVideo";
    public static final String EXTRA_IDENTIFIER_LIST_INDEX = "QQIdentifier";
    public static final String JSON_KEY_DATA = "data";
    public static final String JSON_KEY_CODE = "code";
    public static final String JSON_KEY_LOGIN_DATA = "logindata";
    public static final String JSON_KEY_VERSION = "version";
    public static final String JSON_KEY_FORCE = "force";
    public static final String JSON_KEY_USER_INFO = "userinfo";
    public static final String EXTRA_LIVE_VIDEO_INFO = "LiveVideoInfo";
    public static final String EXTRA_USER_PHONE = "userphone";
    public static final String EXTRA_PASSWORD = "password";
    public static final String EXTRA_USER_NAME = "username";
    public static final String EXTRA_USER_SIG = "usersig";
    public static final String EXTRA_SEX = "sex";
    public static final String EXTRA_CONSTELLATION = "constellation";
    public static final String EXTRA_PRAISE_NUM = "praisenum";
    public static final String EXTRA_VIEWER_NUM = "viewernum";
    public static final String EXTRA_SIGNATURE = "signature";
    public static final String EXTRA_ADDRESS = "address";
    public static final String EXTRA_HEAD_IMAGE_PATH = "headimagepath";
    public static final String EXTRA_GROUP_ID = "groupid";
    public static final String EXTRA_PROGRAM_ID = "programid";
    public static final String EXTRA_ROOM_NUM = "roomnum";
    public static final String EXTRA_mHostIdentifier = "mHostIdentifier";

    public static final String EXTRA_ROOM_NUM_PHOTO = "photo";
    public static final String EXTRA_LIVE_TITLE = "livetitle";
    public static final String EXTRA_SUBJECT = "subject";
    public static final String EXTRA_LIVEPHONE = "livephone";
    public static final String EXTRA_LEAVE_MODE = "leave_mode";
    public static final String EXTRA_REPLAYID = "replayid";
    public static final String EXTRA_RECORDTIME = "duration";
    public static final String ACTION_EXTRA_STOPRECORDD = "stoprecord";
    public static final int SHOW_RESULT_CODE = 10000;
    public static final int EDIT_RESULT_CODE = 20000;
    public static final int VIEW_RESULT_CODE = 30000;
    public static final int AUDIO_VOICE_CHAT_MODE = 0;
    public static final int AUDIO_MEDIA_PLAY_RECORD = AUDIO_VOICE_CHAT_MODE + 1;
    public static final int AUDIO_MEDIA_PLAYBACK = AUDIO_MEDIA_PLAY_RECORD + 1;
    public static final int TRUSTEESHIP = 1;
    public static final int INTEGERATE = TRUSTEESHIP + 1;
    public static final int ENV_FORMAL = 0;
    public static final int ENV_TEST = ENV_FORMAL + 1;
    private static final String TAG = "Utils";
    private static final String PACKAGE = "com.tencent.avsdk";
    public static final String ACTION_START_CONTEXT_COMPLETE = PACKAGE +
            ".ACTION_START_CONTEXT_COMPLETE";
    public static final String ACTION_START_CONTEXT_COMPLETE_NEW = PACKAGE +
            ".ACTION_START_CONTEXT_COMPLETE_NEW";
    public static final String ACTION_CLOSE_CONTEXT_COMPLETE = PACKAGE +
            ".ACTION_CLOSE_CONTEXT_COMPLETE";
    public static final String ACTION_ROOM_CREATE_COMPLETE = PACKAGE +
            ".ACTION_ROOM_CREATE_COMPLETE";
    public static final String ACTION_CLOSE_ROOM_COMPLETE = PACKAGE +
            ".ACTION_CLOSE_ROOM_COMPLETE";
    public static final String ACTION_SURFACE_CREATED = PACKAGE +
            ".ACTION_SURFACE_CREATED";
    public static final String ACTION_MEMBER_CHANGE = PACKAGE +
            ".ACTION_MEMBER_CHANGE";
    public static final String ACTION_SHOW_VIDEO_MEMBER_INFO = PACKAGE +
            ".ACTION_SHOW_VIDEO_MEMBER_INFO";
    public static final String ACTION_VIDEO_SHOW = PACKAGE +
            ".ACTION_VIDEO_SHOW";
    public static final String ACTION_MEMBER_VIDEO_SHOW = PACKAGE +
            ".ACTION_MEMBER_VIDEO_SHOW";
    public static final String ACTION_REQUEST_MEMBER_VIEW = PACKAGE +
            ".ACTION_REQUEST_MEMBER_VIEW";
    public static final String ACTION_VIDEO_CLOSE = PACKAGE +
            ".ACTION_VIDEO_CLOSE";
    public static final String ACTION_ENABLE_CAMERA_COMPLETE = PACKAGE +
            ".ACTION_ENABLE_CAMERA_COMPLETE";
    public static final String ACTION_SWITCH_CAMERA_COMPLETE = PACKAGE +
            ".ACTION_SWITCH_CAMERA_COMPLETE";
    public static final String ACTION_OUTPUT_MODE_CHANGE = PACKAGE +
            ".ACTION_OUTPUT_MODE_CHANGE";
    public static final String ACTION_ENABLE_EXTERNAL_CAPTURE_COMPLETE =
            PACKAGE + ".ACTION_ENABLE_EXTERNAL_CAPTURE_COMPLETE";
    public static final String ACTION_CREATE_GROUP_ID_COMPLETE = PACKAGE +
            ".ACTION_CREATE_GROUP_ID_COMPLETE";
    public static final String ACTION_CREATE_ROOM_NUM_COMPLETE = PACKAGE +
            ".ACTION_CREATE_ROOM_NUM_COMPLETE";
    public static final String ACTION_INSERT_ROOM_TO_SERVER_COMPLETE = PACKAGE +
            ".ACTION_INSERT_ROOM_TO_SERVER_COMPLETE";
    public static final String ACTION_INVITE_MEMBER_VIDEOCHAT = PACKAGE +
            ".ACTION_INVITE_MEMBER_VIDEOCHAT";
    public static final String ACTION_CLOSE_MEMBER_VIDEOCHAT = PACKAGE +
            ".ACTION_CLOSE_MEMBER_VIDEOCHAT";
    public static final String ACTION_SEMI_AUTO_RECV_CAMERA_VIDEO = PACKAGE +
            ".ACTION_SEMI_AUTO_RECV_CAMERA_VIDEO";
    public static final String ACTION_HAVE_REMOTEVIDEO = "action_have_remotevideo";

    public static final String[] mlist = new String[] { "妈逼", "麻痹", "他妈的", "操你",
            "草你", "看胸", "shit", "fuck", "日你", "公安", "机关", "政府", "党", "喷子", "啪啪",
            "房事", "丰满", "人流", "美胸", "增粗", "堕胎", "妊娠", "情趣", "内衣", "内裤", "肛裂",
            "性趣", "爆", "白带", "催情", "乳房", "奶头", "乳头", "是鸡", "是鸭", "癌症", "满足",
            "强奸", "狐臭", "糜烂", "成人", "痔疮", "勃起", "早泄", "生殖器", "激情", "又粗又大", "排卵",
            "性交", "痛经", "性福", "避孕", "妇科病", "性爱", "计生", "无痛", "个人护理", "前列腺",
            "医院", "射精", "射", "包茎", "乳腺", "包皮", "硬", "停经", "阴茎", "公关", "偷看",
            "透视", "缩阴", "遗精", "性病", "阳萎", "聚光体", "妇科", "男科", "泌尿", "阳痿", "持久",
            "阴道", "增大", "意外怀孕", "脱", "bb", "逼", "傻逼", "脑残", "智障", "扣逼", "抠逼",
            "共产党", "总理", "中央", "艹你", "肏", "国产视频", "一对一", "裸聊", "裸体", "骗子", "走私",
            "撸", "插你", "擦你", "做爱", "打飞机", "搞你", "中共", "湿了", "奶子", "被子掀开", "走光",
            "成人视频", "鸡巴", "漏逼", "漏胸", "黑木耳", "咪咪", "穴", "土豪直播", "花椒", "映客",
            "摸奶", "自慰", "自摸", "意淫", "一直播", "YY直播", "yy直播" };

    public static final String[] mlist1 = new String[] { "阿扁推翻", "阿宾", "阿賓",
            "挨了一炮", "爱液横流", "安街逆", "安局办公楼", "安局豪华", "安门事", "安眠藥", "案的准确", "八九民",
            "八九学", "八九政治", "把", "助考", "助考网", "专业办理", "专业代", "专业代写", "专业助",
            "转是政府", "赚钱资料", "装弹甲", "装枪套", "装消音", "着护士的胸" };


    public static String checkguanjianzi(String s) {
        String ss = s;
        long i = SystemClock.currentThreadTimeMillis();
        for (String ii : mlist) {
            if (s.contains(ii)) {
                ss = s.replaceAll(ii, "*******".substring(0, ii.length()));
                s = ss;
            }
        }
        i = SystemClock.currentThreadTimeMillis() - i;
        return ss;
    }


    public static ProgressDialog newProgressDialog(Context context, int titleId) {
        ProgressDialog result = new ProgressDialog(context);
        result.setTitle(titleId);
        result.setIndeterminate(true);
        result.setCancelable(false);

        return result;
    }


    public static AlertDialog newErrorDialog(Context context, int titleId) {
        return new AlertDialog.Builder(context).setTitle(titleId)
                                               .setMessage(R.string.error_code_prefix)
                                               .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                   @Override
                                                   public void onClick(DialogInterface dialog, int which) {
                                                   }
                                               }).create();
    }


    public static AlertDialog newErrorDialogtwo(final Context context, int titleId) {
        return new AlertDialog.Builder(context).setTitle(titleId)
                                               .setMessage("去打开设置")
                                               .setMessage("去设置")
                                               .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                   @Override
                                                   public void onClick(DialogInterface dialog, int which) {
                                                       FileUtils.AppManagerUtil
                                                               .showInstalledAppDetails(context, context
                                                                       .getPackageName());
                                                   }
                                               }).create();
    }


    public static void switchWaitingDialog(Context ctx, ProgressDialog waitingDialog, int dialogId, boolean isToShow) {
        if (isToShow) {
            if (waitingDialog == null || !waitingDialog.isShowing()) {
                if (ctx instanceof Activity) {
                    Activity ctx2 = (Activity) ctx;
                    if (ctx2.isFinishing() == true) {
                        return;
                    }
                    ((Activity) ctx).showDialog(dialogId);
                }
            }
        } else {
            if (waitingDialog != null && waitingDialog.isShowing()) {
                waitingDialog.dismiss();
            }
        }
    }


    /**
     * 网络是否正常
     *
     * @param context Context
     * @return true 表示网络可用
     */
    public static int getNetWorkType(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();

            if (type.equalsIgnoreCase("WIFI")) {
                return AVConstants.NETTYPE_WIFI;
            } else if (type.equalsIgnoreCase("MOBILE")) {
                NetworkInfo mobileInfo = manager
                        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (mobileInfo != null) {
                    switch (mobileInfo.getType()) {
                        case ConnectivityManager.TYPE_MOBILE:// 手机网络
                            switch (mobileInfo.getSubtype()) {
                                case TelephonyManager.NETWORK_TYPE_UMTS:
                                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                                case TelephonyManager.NETWORK_TYPE_HSDPA:
                                case TelephonyManager.NETWORK_TYPE_HSUPA:
                                case TelephonyManager.NETWORK_TYPE_HSPA:
                                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                                case TelephonyManager.NETWORK_TYPE_EHRPD:
                                case TelephonyManager.NETWORK_TYPE_HSPAP:
                                    return AVConstants.NETTYPE_3G;
                                case TelephonyManager.NETWORK_TYPE_CDMA:
                                case TelephonyManager.NETWORK_TYPE_GPRS:
                                case TelephonyManager.NETWORK_TYPE_EDGE:
                                case TelephonyManager.NETWORK_TYPE_1xRTT:
                                case TelephonyManager.NETWORK_TYPE_IDEN:
                                    return AVConstants.NETTYPE_2G;
                                case TelephonyManager.NETWORK_TYPE_LTE:
                                    return AVConstants.NETTYPE_4G;
                                default:
                                    return AVConstants.NETTYPE_NONE;
                            }
                    }
                }
            }
        }

        return AVConstants.NETTYPE_NONE;
    }


    /*
     * 获取网络类型
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的  
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用  
                    return true;
                }
            }
        }
        return false;
    }


    public static <T> T jsonToBean(String json, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(json, clazz);
    }


    public static String beanTojson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }


    /**
     * 获取现在时间
     *
     * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }


    /**
     * 获取现在时间
     *
     * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
    public static String getStringYers() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        String dateString = formatter.format(currentTime);
        return dateString;
    }


    public static String getStringDates() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }


    /**
     * long转换string
     */
    public static String getlongDates(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
        //前面的lSysTime是秒数，先乘1000得到毫秒数，再转为java.util.Date类型
        java.util.Date dt = new Date(time);
        String sDateTime = sdf.format(dt);  //得到精确到秒的表示：08/31/2006 21:08:00
        return sDateTime;
    }


    /**
     * String转换成Date类型
     **/
    public static Date getData(String str) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            date = sdf.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * 将dp转换为px
     *
     * @param dipValue dp值
     * @return 相应的px
     */
    public static int getPX(float dipValue) {
        return getPX(HXApp.getInstance(), dipValue);
    }


    /**
     * 将dp转换为px
     *
     * @param dipValue dp值
     * @return 相应的px
     */
    public static int getPX(Context c, float dipValue) {
        final float scale = c.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    public static int[] getScreenSize(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return new int[] { displayMetrics.widthPixels,
                displayMetrics.heightPixels };
    }


    /**
     * 隐藏软键盘
     *
     * @param v 正在输入内容（调用软键盘）的控件
     * @param c 上下文
     */
    public static void hideInputBoard(View v, Context c) {
        InputMethodManager imm = (InputMethodManager) c
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


    /**
     * 隐藏软键盘
     *
     * @param a 正在显示软键盘的界面
     * 有可能没效果！
     */
    public static void hideInputBoard(Activity a) {
        InputMethodManager imm = (InputMethodManager) a
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = a.getCurrentFocus();
        if (view == null) {
            view = new View(a);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}