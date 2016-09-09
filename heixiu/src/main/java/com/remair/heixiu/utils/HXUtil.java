package com.remair.heixiu.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Looper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.badoo.mobile.util.WeakHandler;
import com.remair.heixiu.BuildConfig;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.HXJavaNet;
import com.remair.heixiu.bean.Gift;
import com.remair.heixiu.bean.UserInfo;
import com.remair.heixiu.oss.HXOSSManager;
import com.tencent.TIMImage;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.Executor;
import okhttp3.Call;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import studio.archangel.toolkitv2.interfaces.AngelNetProgressCallBack;
import studio.archangel.toolkitv2.util.Logger;
import studio.archangel.toolkitv2.util.networking.AngelNetCallBack;

/**
 * Created by Michael
 * some common utils
 */
public class HXUtil extends studio.archangel.toolkitv2.util.Util implements Executor {
    static HXUtil instance;
    WeakHandler handler = null;
    LinkedList<String> upload_files, sent_files;
    private static String timeStamp;
    private static String fileurl = Environment.getExternalStorageDirectory() +
            "/heixiuimage";
    //    Environment.getExternalStorageDirectory()


    private HXUtil() {
        handler = new WeakHandler(Looper.getMainLooper());
        upload_files = new LinkedList<>();
        sent_files = new LinkedList<>();
        timeStamp = "";
    }


    public static HXUtil getInstance() {
        if (instance == null) {
            instance = new HXUtil();
        }
        return instance;
    }


    public static JSONObject createLiveChatBasicMessageText(String command) {
        JSONObject attr = createLiveChatBasicMessage();
        try {
            attr.put("text", command);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return attr;
    }


    public static JSONObject createLiveChatBasicMessage() {
        JSONObject attr = new JSONObject();
        JSONObject user = new JSONObject();
        UserInfo info = HXApp.getInstance().getUserInfo();
        if (info == null) {
            return attr;
        }
        try {
            user.put("user_id", info.user_id + "");
            user.put("user_avatar", info.photo);
            user.put("user_name", info.nickname);
            user.put("user_is_vip", 0);
            user.put("type", info.type);
            user.put("identity", info.identity);
            user.put("appVersion", "and " + BuildConfig.VERSION_NAME);
            user.put("address", info.address);
            user.put("ticket_amount", info.ticket_amount);
            user.put("relation_type", info.relation_type);
            user.put("send_out_vca", info.send_out_vca);
            user.put("attention_amount", info.attention_amount);
            user.put("fans_amount", info.fans_amount);
            user.put("grade", info.grade);
            user.put("send_heidou_amount", info.send_heidou_amount);
            attr.put("user", user);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return attr;
    }


    public static JSONObject createLiveChatBasicMessage1() {
        JSONObject attr = new JSONObject();
        JSONObject user = new JSONObject();
        UserInfo info = HXApp.getInstance().getUserInfo();
        try {
            user.put("appVersion", BuildConfig.VERSION_NAME);
            attr.put("user", user);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return attr;
    }


    //中奖消息
    public static JSONObject createLivetanmuwinningMessage(String text) {
        JSONObject attr = createLiveChatBasicMessage();
        JSONObject command = new JSONObject();
        try {
            command.put("name", "winning");
            command.put("des", text);
            attr.put("command", command);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return attr;
    }


    public static JSONObject createLivetanmuTextMessage(String text, int type) {
        JSONObject attr = createLiveChatBasicMessage();
        JSONObject command = new JSONObject();
        try {
            command.put("name", "trumpet_send");
            command.put("des", text);
            command.put("type", type);
            attr.put("command", command);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return attr;
    }


    //发送图片
    public static JSONObject createPhotoMessage(TIMImage timImage) {
        JSONObject attr = createLiveChatBasicMessage();
        JSONObject command = new JSONObject();
        JSONObject photo = new JSONObject();
        try {
            command.put("name", "photo");
            photo.put("picWidth", timImage.getWidth());
            photo.put("picHeight", timImage.getHeight());
            command.put("photoElem", photo);
            attr.put("command", command);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return attr;
    }


    //发送语音
    public static JSONObject createsoundMessage(long timSound) {
        JSONObject attr = createLiveChatBasicMessage();
        JSONObject command = new JSONObject();
        JSONObject sound = new JSONObject();
        try {
            command.put("name", "sound");
            sound.put("duration", timSound);
            command.put("soundElem", sound);
            attr.put("command", command);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return attr;
    }


    public static JSONObject createLiveChatTextMessage(String text) {
        JSONObject attr = createLiveChatBasicMessage();
        try {
            attr.put("text", text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return attr;
    }


    public static JSONObject createLiveChatGiftMessage(Gift gift, int charm_value, int searilNum, int count, int rate, int segrade, double prosses) {
        JSONObject attr = createLiveChatBasicMessage();
        UserInfo info = HXApp.getInstance().getUserInfo();
        if (searilNum == 1) {
            String time = Utils.getStringDate();
            timeStamp = info.user_id + "_" + time;
        }
        JSONObject command = new JSONObject();
        try {
            command.put("name", "gift_send");
            JSONObject g = new JSONObject();
            if (gift.animation == 1) {//GIF
                g.put("gift_id", gift.gift_id);
                g.put("gift_name", gift.name);
                g.put("gift_price", gift.price);
                g.put("gift_image", gift.select_animation_image);
                g.put("gift_count", 1);
                g.put("gift_count_unit", gift.count_unit);
                g.put("gif_image", gift.animation_image);
                g.put("isGif", 1);
                g.put("sign", "gif");
                g.put("duration", gift.duration);
                g.put("searilNum", searilNum);
                g.put("gift_type", gift.type);
                g.put("gift_once_count", count);
            } else {
                g.put("gift_id", gift.gift_id);
                g.put("gift_name", gift.name);
                g.put("gift_price", gift.price);
                g.put("gift_image", gift.select_animation_image);
                g.put("gift_count", 1);
                g.put("gift_count_unit", gift.count_unit);
                g.put("gif_image", gift.animation_image);
                g.put("isGif", 0);
                g.put("sign", timeStamp);
                g.put("duration", gift.duration);
                g.put("searilNum", searilNum);
                g.put("gift_type", gift.type);
                g.put("gift_once_count", count);
            }
            command.put("gift", g);
            command.put("charm_value", charm_value);
            command.put("heidou_num", count * gift.price);
            command.put("winning_rate", rate);
            command.put("loveGrade", segrade);
            command.put("loveGradeRatio", prosses);

            attr.put("command", command);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return attr;
    }


    //私聊发礼物
    public static JSONObject createPrivateChatGiftMessage(Gift gift, int giftCount, int searilNum, int reward) {
        JSONObject attr = createLiveChatBasicMessage();
        UserInfo info = HXApp.getInstance().getUserInfo();
        if (searilNum == 1) {
            String time = Utils.getStringDate();
            timeStamp = info.user_id + "_" + time;
        }
        JSONObject command = new JSONObject();
        try {
            command.put("name", "gift");
            JSONObject g = new JSONObject();
            g.put("gift_id", gift.gift_id);
            g.put("name", gift.name);
            g.put("price", gift.price);
            g.put("image", gift.select_animation_image);
            g.put("description", gift.description);
            g.put("animation", gift.animation);
            g.put("gif_image", gift.animation_image);
            g.put("unit_name", gift.count_unit);
            g.put("empiric_value", gift.empiric_value);
            g.put("select_animation", gift.select_animation);
            g.put("select_animation_image", gift.select_animation_image);
            g.put("type", gift.type);

            command.put("giftElem", g);
            command.put("giftCount", giftCount);
            command.put("giftSearilNum", searilNum);
            command.put("giftSign", timeStamp);
            command.put("giftReward", reward);

            attr.put("command", command);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return attr;
    }


    public static JSONObject createLiveChatRoomMessage(boolean enter) {
        JSONObject attr = createLiveChatBasicMessage();
        JSONObject command = new JSONObject();
        try {
            command.put("name", "room_" + (enter ? "enter" : "exit"));
            command.put("des",
                    attr.optJSONObject("user").optString("user_name") +
                            (enter ? "进入" : "离开") + "了房间");
            attr.put("command", command);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return attr;
    }


    public static JSONObject createLiveChatRoomMessageentroom(boolean enter, String WatchingUserCount) {
        JSONObject attr = createLiveChatBasicMessage();
        JSONObject command = new JSONObject();
        try {
            command.put("name", "room_" + (enter ? "enter" : "exit"));
            command.put("des",
                    attr.optJSONObject("user").optString("user_name") +
                            (enter ? "进入" : "离开") + "了房间");
            command.put("WatchingUserCount", WatchingUserCount);
            command.put("animationType", HXApp.getInstance().animationType);
            attr.put("command", command);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return attr;
    }


    public static JSONObject outLiveChatRoomMessage(boolean goout) {
        JSONObject attr = createLiveChatBasicMessage();
        JSONObject command = new JSONObject();
        try {
            command.put("name", "author_state");//主播暂离一小会,马上回来~/主播已回到直播
            command.put("des",
                    attr.optJSONObject("user").optString("user_name") +
                            (goout ? "主播暂离一小会,马上回来~" : "主播即将回到直播"));
            attr.put("command", command);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return attr;
    }


    public static JSONObject addmessageimbenji(String text, String ttext, String user_id) {

        JSONObject attr = createLiveChatBasicMessage();
        JSONObject command = new JSONObject();
        try {
            command.put("name", "network_status");//主播暂离一小会,马上回来~/主播已回到直播
            command.put("desc", text);
            command.put("type", ttext);
            command.put("user_id", user_id);
            attr.put("command", command);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return attr;
    }


    public static JSONObject chatRoomMessagelight() {
        JSONObject attr = createLiveChatBasicMessage();
        JSONObject command = new JSONObject();
        try {
            command.put("name", "room_enter");
            command.put("des",
                    attr.optJSONObject("user").optString("user_name") +
                            "点亮了❤️");
            attr.put("command", command);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return attr;
    }


    public static JSONObject createLiveChatLikeMessage() {
        JSONObject attr = createLiveChatBasicMessage1();
        JSONObject command = new JSONObject();
        try {
            command.put("name", "like");
            attr.put("command", command);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return attr;
    }

    //禁言


    public static JSONObject createLiveChatsilenceMessage(String text, String type, String user_id) {
        JSONObject attr = createLiveChatBasicMessage();
        JSONObject command = new JSONObject();
        try {
            command.put("name", "silence");
            command.put("des", text);
            command.put("type", type);

            command.put("user_id", user_id);

            attr.put("command", command);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return attr;
    }

    //管理


    public static JSONObject createadminChatsilenceMessage(String text, String user_id, String type) {
        JSONObject attr = createLiveChatBasicMessage();
        JSONObject command = new JSONObject();
        try {
            command.put("name", "editManager");
            command.put("message", text);
            command.put("user_id", user_id);
            command.put("type", type);
            command.put("des", "admin");

            attr.put("command", command);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return attr;
    }


    //获取礼物数据
    public static void fetchGiftData(final OnDataFetchedListener listener) {
        UserInfo userInfo = HXApp.getInstance().getUserInfo();
        if (userInfo == null) {
            return;
        }
        String url_gift_list_refresh_tag = userInfo.gift_list_refresh_tag;
        final String data = SharedPreferenceUtil.getString("data", null);
        if (SharedPreferenceUtil.getString("gift_list_refresh_tag", "")
                                .equals(url_gift_list_refresh_tag) &&
                null != data) {
            if (listener != null) {
                listener.onDataFetched(data);
            }
        } else {
            HashMap<String, Object> par = new HashMap<>();
            par.put("user_id", userInfo.user_id);
            HXJavaNet
                    .post(HXJavaNet.url_meta_gift, par, new AngelNetCallBack() {
                        @Override
                        public void onSuccess(int ret_code, String ret_data, String msg) {
                            Logger.out(ret_code + " " + ret_data + " " + msg);
                            if (ret_code == 200) {

                                SharedPreferenceUtil
                                        .putLong("gift_data_fetch_date", System
                                                .currentTimeMillis());
                                SharedPreferenceUtil
                                        .putString("data", ret_data);
                                if (listener != null) {
                                    listener.onDataFetched(ret_data);
                                }
                                try {
                                    JSONArray jsonArray = new JSONArray();
                                    JSONArray ja = new JSONArray(ret_data);
                                    for (int i = 0; i < ja.length(); i++) {
                                        JSONObject jsonObject = ja
                                                .optJSONObject(i);
                                        if (jsonObject.getInt("animation") ==
                                                1) {//animation_image  gift_id
                                            DownLoad.to(jsonObject
                                                            .optString("animation_image"), fileurl,
                                                    jsonObject
                                                            .optString("gift_id") +
                                                            ".gif", jsonObject);
                                        } else {
                                            DownLoad.to(jsonObject
                                                            .optString("gift_image"), fileurl,
                                                    jsonObject
                                                            .optString("gift_id") +
                                                            ".gif", jsonObject);
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Logger.out("error");
                            }
                        }


                        @Override
                        public void onFailure(String msg) {
                            getgiftData(listener);
                        }
                    });
            SharedPreferenceUtil.setContext(HXApp.getInstance());
            SharedPreferenceUtil
                    .putString("gift_list_refresh_tag", url_gift_list_refresh_tag);
        }
    }


    private static void getgiftData(final OnDataFetchedListener listener) {
        UserInfo userInfo = HXApp.getInstance().getUserInfo();
        if (userInfo == null) {
            return;
        }
        HashMap<String, Object> par = new HashMap<>();
        par.put("user_id", userInfo.user_id);
        HXJavaNet.post(HXJavaNet.url_meta_gift, par, new AngelNetCallBack() {
            @Override
            public void onSuccess(int ret_code, String ret_data, String msg) {
                Logger.out(ret_code + " " + ret_data + " " + msg);
                if (ret_code == 200) {
                    SharedPreferences.Editor editor = HXApp.getInstance()
                                                           .getPreference()
                                                           .edit();
                    editor.putLong("gift_data_fetch_date", System
                            .currentTimeMillis());
                    editor.putString("data", ret_data);
                    editor.apply();
                    if (listener != null) {
                        listener.onDataFetched(ret_data);
                    }
                } else {
                    Logger.out("error");
                }
            }


            @Override
            public void onFailure(String msg) {

            }
        });
    }


    public static void download(String url, String name, final JSONArray jsonArray, final JSONObject jsonObject) {
        FileCallBack callback = new FileCallBack(Environment
                .getExternalStorageDirectory().getAbsolutePath(), name) {
            @Override
            public void onError(Call call, Exception e) {

            }


            @Override
            public void onResponse(final File response) {
                Logger.out("下载成功=====" + response.getPath());
                try {
                    jsonObject.put("animation_image", response.getPath());
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void inProgress(float progress, long total) {

            }
        };
        OkHttpUtils.get().url(url).build().execute(callback);
    }


    @Override
    public void execute(Runnable r) {
        handler.post(r);
    }


    public void OSSUploadFiles(Context c, ArrayList<String> files_path, String user_id, final AngelNetProgressCallBack callback) {
        long total = 0;
        for (String file_path : files_path) {
            File f = new File(file_path);
            total = total + f.length();
        }
        upload_files.clear();
        sent_files.clear();
        upload_files.addAll(files_path);
        if (upload_files.size() <= 0) {
            upload_files.clear();
            return;
        }
        uploadSingleFile(user_id, callback, c, true);
    }


    void uploadSingleFile(final String user_id, final AngelNetProgressCallBack callback, final Context c, boolean first_time) {
        String file_path = upload_files.get(0);
        if (first_time && callback != null) {
            execute(new Runnable() {
                @Override
                public void run() {
                    callback.onStart();
                }
            });
        }

        String objectKey = "heixiu" + user_id +
                "_" +
                System.currentTimeMillis();
        try {
            HXOSSManager
                    .asyncUpload(HXApp.BUCKET_NAME, objectKey, file_path, new OSSProgressCallback<PutObjectRequest>() {
                        @Override
                        public void onProgress(PutObjectRequest putObjectRequest, final long l, final long l1) {
                            if (callback != null) {
                                execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onProgress(l, l1, false);
                                    }
                                });
                            }
                        }
                    }, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                        @Override
                        public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                            upload_files.remove(0);
                            final String url = HXOSSManager
                                    .getPublicURL(HXApp.BUCKET_NAME, putObjectRequest
                                            .getObjectKey());
                            sent_files.add(url);
                            if (upload_files.size() == 0) {
                                if (callback != null) {
                                    final JSONArray ja = new JSONArray();
                                    for (String file : sent_files) {
                                        ja.put(file);
                                    }
                                    execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            callback.onSuccess(1001, ja
                                                    .toString(), "");
                                        }
                                    });
                                }
                            } else {
                                uploadSingleFile(user_id, callback, c, false);
                            }
                        }


                        @Override
                        public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                            if (callback != null) {
                                execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onFailure("文件上传失败");
                                    }
                                });
                            }
                            if (e != null) {
                                e.printStackTrace();
                            }
                            if (e1 != null) {
                                e1.printStackTrace();
                            }
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void OSSUploadFile(final Context c, String file_path, String user_id, final AngelNetProgressCallBack callback) {
        String objectKey = "heixiu" + user_id +
                "_" +
                System.currentTimeMillis();
        try {
            if (callback != null) {
                execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onStart();
                    }
                });
            }
            HXOSSManager
                    .asyncUpload(HXApp.BUCKET_NAME, objectKey, file_path, new OSSProgressCallback<PutObjectRequest>() {
                        @Override
                        public void onProgress(PutObjectRequest putObjectRequest, final long byteCount, final long totalSize) {
                            if (callback != null) {
                                execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onProgress(byteCount, totalSize, false);
                                    }
                                });
                            }
                        }
                    }, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                        @Override
                        public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                            final String url = HXOSSManager
                                    .getPublicURL(HXApp.BUCKET_NAME, putObjectRequest
                                            .getObjectKey());
                            if (callback != null) {
                                execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onSuccess(200, url, "");
                                    }
                                });
                            }
                        }


                        @Override
                        public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                            if (e != null) {
                                e.printStackTrace();
                            }
                            if (e1 != null) {
                                e1.printStackTrace();
                            }
                            if (callback != null) {
                                execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onFailure("文件上传失败");
                                    }
                                });
                            }
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public final static long DIS_INTERVAL = 300;


    public static String GetStringFormat(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd HH:mm");
        Date dt = new Date(millis * 1000);
        return sdf.format(dt);
    }


    public static boolean LongInterval(long current, long last) {
        return (current - last) > DIS_INTERVAL ? true : false;
    }


    /**
     * 因为输入法造成的内存泄漏
     */
    public static void fixInputMethodManagerLeak(Context destContext) {
        if (destContext == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) destContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        String[] arr = new String[] { "mCurRootView", "mServedView",
                "mNextServedView" };
        Field f = null;
        Object obj_get = null;
        for (int i = 0; i < arr.length; i++) {
            String param = arr[i];
            try {
                f = imm.getClass().getDeclaredField(param);
                if (f.isAccessible() == false) {
                    f.setAccessible(true);
                } // author: sodino mail:sodino@qq.com
                obj_get = f.get(imm);
                if (obj_get != null && obj_get instanceof View) {
                    View v_get = (View) obj_get;
                    if (v_get.getContext() ==
                            destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
                        f.set(imm, null); // 置空，破坏掉path to gc节点
                    } else {
                        // 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
                        break;
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}
