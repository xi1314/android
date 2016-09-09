package com.remair.heixiu.net;

import com.remair.heixiu.BuildConfig;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.bean.AttentionBean;
import com.remair.heixiu.bean.ForbidBean;
import com.remair.heixiu.bean.FriendInfo;
import com.remair.heixiu.bean.LiveAttentionInfo;
import com.remair.heixiu.bean.LiveVideoBean;
import com.remair.heixiu.bean.NewPerMesInfo;
import com.remair.heixiu.bean.RankListBean;
import com.remair.heixiu.bean.RecommendBean;
import com.remair.heixiu.bean.UserInfo;
import com.remair.heixiu.utils.TransformUtils;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import org.json.JSONObject;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：LiuJun
 * 创建时间：16/8/24 16:18
 * 修改人：LiuJun
 * 修改时间：16/8/24 16:18
 * 修改备注：
 */
public class HXHttpUtil {

    /**
     * 每页条数
     */
    public static final int PAGE_LIMIT = 20;

    /**
     * 超时时间
     */
    private static final int DEFAULT_TIMEOUT = 10;
    private HXService mHXService;


    private HXHttpUtil() {
        OkHttpClient.Builder okhttpBuilder = new OkHttpClient.Builder();
        okhttpBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        okhttpBuilder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        okhttpBuilder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        okhttpBuilder.addInterceptor(new CommonInterceptor());
        if (BuildConfig.LOG_DEBUG) {
            okhttpBuilder.addInterceptor(new HttpLoggerInterceptor());
        }

        Retrofit retrofit = new Retrofit.Builder().client(okhttpBuilder.build())
                                                  //.addConverterFactory(FastJsonConverterFactory
                                                  //        .create())
                                                  .addConverterFactory(GsonConverterFactory.create())
                                                  .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                                                  .baseUrl(BuildConfig.DOMAIN_NAME).build();

        mHXService = retrofit.create(HXService.class);
    }


    private static class SingletonHolder {

        static HXHttpUtil INSTANCE = new HXHttpUtil();
    }


    public static HXHttpUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }


    /**
     * 手机号登录
     *
     * @param phone_num 手机号
     * @param pwd 密码
     */
    public Observable<UserInfo> loginPwd(String phone_num, String pwd) {
        return mHXService.loginPwd(phone_num, pwd, "android").compose(TransformUtils.<UserInfo>dataValidation())
                         .compose(TransformUtils.<UserInfo>defaultSchedulers());
    }


    public Observable<List<LiveVideoBean>> liveHotList(int userId) {
        return mHXService.liveHotList(userId).compose(TransformUtils.<List<LiveVideoBean>>dataValidation())
                         .compose(TransformUtils.<List<LiveVideoBean>>defaultSchedulers());
    }


    public Observable<LiveAttentionInfo> liveAttentionList(int userId) {
        return mHXService.liveAttentionList(userId).compose(TransformUtils.<LiveAttentionInfo>dataValidation())
                         .compose(TransformUtils.<LiveAttentionInfo>defaultSchedulers());
    }


    public Observable<List<LiveVideoBean>> liveLastestList(int userId) {
        return mHXService.liveLastestList(userId).compose(TransformUtils.<List<LiveVideoBean>>dataValidation())
                         .compose(TransformUtils.<List<LiveVideoBean>>defaultSchedulers());
    }


    /**
     * 个人信息
     *
     * @param userId 查看人的id
     * @param viewedUserId 被查看人的用户id
     */
    public Observable<NewPerMesInfo> getUserInfo(int userId, int viewedUserId) {
        return mHXService.getUserInfo(userId, viewedUserId).compose(TransformUtils.<NewPerMesInfo>dataValidation())
                         .compose(TransformUtils.<NewPerMesInfo>defaultSchedulers());
    }


    /**
     * 关注
     */
    public Observable<Object> concern(int userId, int viewedUserId) {
        return mHXService.concern(userId, viewedUserId).compose(TransformUtils.dataValidation())
                         .compose(TransformUtils.defaultSchedulers());
    }


    /**
     * 未关注
     */
    public Observable<Object> unconcern(int userId, int passive_user_id) {
        return mHXService.unconcern(userId, passive_user_id).compose(TransformUtils.dataValidation())
                         .compose(TransformUtils.defaultSchedulers());
    }


    /**
     * 拉黑
     */
    public Observable<Object> pullBlack(int userId, int viewedUserId) {
        return mHXService.pullBlack(userId, viewedUserId).compose(TransformUtils.dataValidation())
                         .compose(TransformUtils.defaultSchedulers());
    }


    /**
     * 关于我们
     */

    public Observable<JSONObject> url_about_us() {
        return mHXService.url_about_us().compose(TransformUtils.<JSONObject>dataValidation())
                         .compose(TransformUtils.<JSONObject>defaultSchedulers());
    }


    /**
     * 好友（关注人）列表
     *
     * @param viewedUserId 被查看人的用户id
     * @param page 页码
     */
    public Observable<List<FriendInfo>> friendList(int viewedUserId, int page) {
        UserInfo userInfo = HXApp.getInstance().getUserInfo();
        if (userInfo == null) {
            throw new RuntimeException("HXApp.getInstance().getUserInfo() is null ");
        }
        return mHXService.friendList(userInfo.user_id, viewedUserId, page, PAGE_LIMIT)
                         .compose(TransformUtils.<List<FriendInfo>>dataValidation())
                         .compose(TransformUtils.<List<FriendInfo>>defaultSchedulers());
    }


    /**
     * 编辑我们
     * 关于我们
     */
    public Observable<String> editUserPassword(String passwordOld, String passwordNew) {
        UserInfo userInfo = HXApp.getInstance().getUserInfo();
        if (userInfo == null) {
            throw new RuntimeException("HXApp.getInstance().getUserInfo() is null ");
        }
        return mHXService.editUserPassword(userInfo.user_id, passwordOld, passwordNew)
                         .compose(TransformUtils.<String>dataValidation())
                         .compose(TransformUtils.<String>defaultSchedulers());
    }


    /**
     * 黑名单
     */
    public Observable<List<AttentionBean>> blacklist(int page) {
        UserInfo userInfo = HXApp.getInstance().getUserInfo();
        if (userInfo == null) {
            throw new RuntimeException("HXApp.getInstance().getUserInfo() is null ");
        }
        return mHXService.blacklist(userInfo.user_id, page, PAGE_LIMIT)
                         .compose(TransformUtils.<List<AttentionBean>>dataValidation())
                         .compose(TransformUtils.<List<AttentionBean>>defaultSchedulers());
    }


    /**
     * 推送管理
     */
    public Observable<RecommendBean> pushManage(int userId) {
        return mHXService.pushManage(userId).compose(TransformUtils.<RecommendBean>dataValidation())
                         .compose(TransformUtils.<RecommendBean>defaultSchedulers());
    }


    /**
     * 封禁
     *
     * @param userId 被封禁人的id
     * @param roomNum 房间号
     * @param type 0位主播，1位观众
     * @param style 被封禁的原因
     * @param day 封禁时间
     */
    public Observable<Object> forbidUser(int userId, int roomNum, int type, int style, int day) {
        return mHXService.forbidUser(userId, roomNum, type, style, day).compose(TransformUtils.dataValidation())
                         .compose(TransformUtils.defaultSchedulers());
    }


    /**
     * 黑名单删除接口
     *
     * @param black_user_id 传别人的
     */
    public Observable pushBlack(int black_user_id) {
        UserInfo userInfo = HXApp.getInstance().getUserInfo();
        if (userInfo == null) {
            throw new RuntimeException("HXApp.getInstance().getUserInfo() is null ");
        }
        return mHXService.pushBlack(userInfo.user_id, black_user_id).compose(TransformUtils.<String>dataValidation())
                         .compose(TransformUtils.<String>defaultSchedulers());
    }


    /**
     * 粉丝列表
     *
     * @param viewedUserId 被查看人id
     */
    public Observable<List<FriendInfo>> fansList(int userId, int viewedUserId, int page) {
        return mHXService.fansList(userId, viewedUserId, page, PAGE_LIMIT)
                         .compose(TransformUtils.<List<FriendInfo>>dataValidation())
                         .compose(TransformUtils.<List<FriendInfo>>defaultSchedulers());
    }


    /**
     * 存在感粉丝榜
     *
     * @param userId 当前登录人id
     * @param viewedUserId 被查看人的id
     */
    public Observable<RankListBean> charmRanking(int userId, int viewedUserId, int page) {
        return mHXService.charmRanking(userId, viewedUserId, page, PAGE_LIMIT)
                         .compose(TransformUtils.<RankListBean>dataValidation())
                         .compose(TransformUtils.<RankListBean>defaultSchedulers());
    }


    /**
     * 存在感贡献榜
     *
     * @param userId 当前登录人id
     */
    public Observable<RankListBean> contributionRanking(int userId, int page) {
        return mHXService.contributionRanking(userId, page, PAGE_LIMIT)
                         .compose(TransformUtils.<RankListBean>dataValidation())
                         .compose(TransformUtils.<RankListBean>defaultSchedulers());
    }


    /**
     * 编辑是否接收关注用户的直播通知
     *
     * @param user_id 当前登录人id
     */
    public Observable<Object> editAttentionPush(int user_id, int accept_push, int relation_id) {
        return mHXService.editAttentionPush(user_id, accept_push, relation_id).compose(TransformUtils.dataValidation())
                         .compose(TransformUtils.defaultSchedulers());
    }


    /**
     * 用户反馈
     */
    public Observable<Object> feedback(String message, String phone, String network) {
        UserInfo userInfo = HXApp.getInstance().getUserInfo();
        if (userInfo == null) {
            throw new RuntimeException("HXApp.getInstance().getUserInfo() is null ");
        }

        return mHXService.feedback(userInfo.user_id, BuildConfig.VERSION_NAME, "android", message, phone, network)
                         .compose(TransformUtils.dataValidation()).compose(TransformUtils.defaultSchedulers());
    }


    /**
     * 推荐列表
     */
    public Observable<List<FriendInfo>> recommendUserList(int userId, int page) {
        return mHXService.recommendUserList(userId, page, PAGE_LIMIT)
                         .compose(TransformUtils.<List<FriendInfo>>dataValidation())
                         .compose(TransformUtils.<List<FriendInfo>>defaultSchedulers());
    }


    /**
     * 搜索用户
     *
     * @param keyword 输入的内容
     */
    public Observable<List<FriendInfo>> userSearch(int userId, String keyword, int page) {
        return mHXService.userSearch(userId, keyword, page, PAGE_LIMIT)
                         .compose(TransformUtils.<List<FriendInfo>>dataValidation())
                         .compose(TransformUtils.<List<FriendInfo>>defaultSchedulers());
    }


    public Observable<ForbidBean> forbidReason(int userId) {
        return mHXService.forbidReason(userId)
                         .compose(TransformUtils.<ForbidBean>dataValidation())
                         .compose(TransformUtils.<ForbidBean>defaultSchedulers());
    }
}
