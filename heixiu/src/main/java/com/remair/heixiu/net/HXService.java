package com.remair.heixiu.net;

import com.remair.heixiu.bean.AttentionBean;
import com.remair.heixiu.bean.AuthStatusBean;
import com.remair.heixiu.bean.CaseHistoryInfo;
import com.remair.heixiu.bean.CertifitionBean;
import com.remair.heixiu.bean.ConfigBean;
import com.remair.heixiu.bean.ExchageDimo;
import com.remair.heixiu.bean.FriendInfo;
import com.remair.heixiu.bean.HttpResult;
import com.remair.heixiu.bean.LiveAttentionInfo;
import com.remair.heixiu.bean.LiveVideoBean;
import com.remair.heixiu.bean.LoginCommonBean;
import com.remair.heixiu.bean.NewPerMesInfo;
import com.remair.heixiu.bean.Profitincomebean;
import com.remair.heixiu.bean.RankListBean;
import com.remair.heixiu.bean.RecommendBean;
import com.remair.heixiu.bean.ReplayBean;
import com.remair.heixiu.bean.RoomNumBean;
import com.remair.heixiu.bean.SendCodeBean;
import com.remair.heixiu.bean.UserInfo;
import com.remair.heixiu.bean.WechatReqPayBean;
import com.remair.heixiu.bean.WhellBean;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：LiuJun
 * 创建时间：16/8/24 12:10
 * 修改人：LiuJun
 * 修改时间：16/8/24 12:10
 * 修改备注：
 */
public interface HXService {

    @FormUrlEncoded
    @POST("loginPwd")
    Observable<HttpResult<UserInfo>> loginPwd(@Field("phone_num") String phone_num, @Field("pwd") String pwd, @Field("device") String device);

    @FormUrlEncoded
    @POST("liveHotList")
    Observable<HttpResult<List<LiveVideoBean>>> liveHotList(@Field("user_id") int userId);

    @FormUrlEncoded
    @POST("liveAttentionList")
    Observable<HttpResult<LiveAttentionInfo>> liveAttentionList(@Field("user_id") int userId);

    @FormUrlEncoded
    @POST("liveLastestList")
    Observable<HttpResult<List<LiveVideoBean>>> liveLastestList(@Field("user_id") int userId);

    @POST("aboutUs")
    Observable<HttpResult<JSONObject>> url_about_us();

    @FormUrlEncoded
    @POST("friendList")
    Observable<HttpResult<List<FriendInfo>>> friendList(@Field("user_id") int userId, @Field("viewed_user_id") int viewedUserId, @Field("page") int page, @Field("limit") int limit);

    @FormUrlEncoded
    @POST("getUserInfo")
    Observable<HttpResult<NewPerMesInfo>> getUserInfo(@Field("user_id") int userId, @Field("viewed_user_id") int viewedUserId);

    @FormUrlEncoded
    @POST("getUserInfo")
    Observable<HttpResult<UserInfo>> getUserInfos(@Field("user_id") int userId, @Field("viewed_user_id") int viewedUserId);

    @FormUrlEncoded
    @POST("blacklist")
    Observable<HttpResult<List<AttentionBean>>> blacklist(@Field("user_id") int userId, @Field("page") int page, @Field("limit") int limit);

    @FormUrlEncoded
    @POST("concern")
    Observable<HttpResult<Object>> concern(@Field("user_id") int userId, @Field("passive_user_id") int passive_user_id);

    @FormUrlEncoded
    @POST("unconcern")
    Observable<HttpResult<Object>> unconcern(@Field("user_id") int userId, @Field("passive_user_id") int passive_user_id);

    @FormUrlEncoded
    @POST("forbidUser")
    Observable<HttpResult<Object>> forbidUser(@Field("handle_id") int handle_id, @Field("handle_identity") String handle_identity, @Field("user_id") int userId, @Field("forbid_identity") String forbid_identity, @Field("room_num") int roomNum, @Field("type") int type, @Field("reason") int style, @Field("forbidDays") int day, @Field("screenshots") String url);

    @FormUrlEncoded

    @POST("pushBlack")
    Observable<HttpResult<String>> pushBlack(@Field("user_id") int userId, @Field("black_user_id") int black_user_id);

    @FormUrlEncoded
    @POST("pullBlack")
    Observable<HttpResult<Object>> pullBlack(@Field("user_id") int userId, @Field("black_user_id") int viewedUserId);

    @FormUrlEncoded
    @POST("fansList")
    Observable<HttpResult<List<FriendInfo>>> fansList(@Field("user_id") int userId, @Field("viewed_user_id") int viewedUserId, @Field("page") int page, @Field("limit") int limit);

    @FormUrlEncoded
    @POST("editUserPassword")
    Observable<HttpResult<String>> editUserPassword(@Field("user_id") int userId, @Field("passwordOld") String passwordOld, @Field("passwordNew") String passwordNew);

    @FormUrlEncoded
    @POST("bindWxPhoneAndPsd")
    Observable<HttpResult<String>> bindWxPhoneAndPsd(@Field("user_id") int userId, @Field("phone") String passwordOld, @Field("password") String passwordNew);

    @FormUrlEncoded
    @POST("bindWxPhoneAndPsd")
    Observable<HttpResult<String>> bindWxPhoneAndPsd(@Field("user_id") int userId, @Field("phone") String passwordOld, @Field("password") String passwordNew, @Field("code") String code);

    @FormUrlEncoded
    @POST("pushManage")
    Observable<HttpResult<RecommendBean>> pushManage(@Field("user_id") int userId);

    @FormUrlEncoded
    @POST("editAttentionPush")
    Observable<HttpResult<Object>> editAttentionPush(@Field("user_id") int userId, @Field("accept_push") int accept_push, @Field("relation_id") int relation_id);

    @FormUrlEncoded
    @POST("charmRanking")
    Observable<HttpResult<RankListBean>> charmRanking(@Field("user_id") int userId, @Field("viewed_user_id") int viewedUserId, @Field("page") int page, @Field("limit") int limit);

    @FormUrlEncoded
    @POST("contributionRanking")
    Observable<HttpResult<RankListBean>> contributionRanking(@Field("user_id") int userId, @Field("page") int page, @Field("limit") int limit);

    @FormUrlEncoded
    @POST("feedback")
    Observable<HttpResult<Object>> feedback(@Field("user_id") int user_id, @Field("version") String version, @Field("device") String device, @Field("message") String message, @Field("contact_way") String phone, @Field("network") String network);

    @FormUrlEncoded
    @POST("recommendUserList")
    Observable<HttpResult<List<FriendInfo>>> recommendUserList(@Field("user_id") int userId, @Field("page") int page, @Field("limit") int limit);

    @FormUrlEncoded
    @POST("userSearch")
    Observable<HttpResult<List<FriendInfo>>> userSearch(@Field("user_id") int userId, @Field("keyword") String keyword, @Field("page") int page, @Field("limit") int limit);

    @FormUrlEncoded
    @POST("loginCommon")
    Observable<HttpResult<LoginCommonBean>> loginCommon(@Field("user_id") int userId);

    @FormUrlEncoded
    @POST("config")
    Observable<HttpResult<ConfigBean>> config(@Field("version_code") String version_code, @Field("op") int op);

    @FormUrlEncoded
    @POST("v1.2.0/authStatus")
    Observable<HttpResult<AuthStatusBean>> authStatus(@Field("user_id") int userId);

    @FormUrlEncoded
    @POST("v1.5.0/firstSimpleAuth")
    Observable<HttpResult<Object>> firstSimpleAuth(@Field("user_id") int userId, @Field("real_name") String realName, @Field("id_card_no") String idCardNo);

    @FormUrlEncoded
    @POST("v1.2.0/carouselList")
    Observable<HttpResult<List<WhellBean>>> carouselList(@Field("user_id") int userId);

    @FormUrlEncoded
    @POST("v1.2.0/authInfo")
    Observable<HttpResult<CertifitionBean>> authInfo(@Field("user_id") int userId);

    @FormUrlEncoded
    @POST("v1.2.0/auth")
    Observable<HttpResult<Object>> auth(@Field("user_id") int userId, @Field("real_name") String real_name, @Field("id_card_no") String id_cardId, @Field("bank_card_no") String bankCark, @Field("phone") String phoneNumber, @Field("id_card_front") String photo, @Field("id_card_back") String photoreverse, @Field("id_card_hand") String photohand);

    @FormUrlEncoded
    @POST("concernBatch")
    Observable<HttpResult<Object>> concernBatch(@Field("user_id") int userId, @Field("passive_user_id_list") String passive_userId);

    @FormUrlEncoded
    @POST("loginSendSms")
    Observable<HttpResult<SendCodeBean>> loginSendSms(@Field("phone_num") String phoneNum);

    @FormUrlEncoded
    @POST("forgetPwdSendSms")
    Observable<HttpResult<SendCodeBean>> forgetPwdSendSms(@Field("phone_num") String phoneNum);

    @FormUrlEncoded
    @POST("loginSinaMicroBlog")
    Observable<HttpResult<UserInfo>> loginSinaMicroBlog(@Field("gender") int gender, @Field("open_id") String openId, @Field("nickname") String nickName, @Field("photo") String photo, @Field("device") String device);

    @FormUrlEncoded
    @POST("v1.2.0/loginWx")
    Observable<HttpResult<UserInfo>> loginWx(@Field("gender") int gender, @Field("unionid") Object unionid, @Field("open_id") String openId, @Field("nickname") String nickName, @Field("photo") String photo, @Field("device") String device);

    @FormUrlEncoded
    @POST("loginQq")
    Observable<HttpResult<UserInfo>> loginQq(@Field("gender") int gender, @Field("open_id") String openId, @Field("nickname") String nickName, @Field("photo") String photo, @Field("device") String device);

    @FormUrlEncoded
    @POST("resetPwd")
    Observable<HttpResult<Object>> resetPwd(@Field("phone_num") String phoneNum, @Field("sms_code") String smsCode, @Field("pwd") String pwd);

    @FormUrlEncoded
    @POST("v1.2.0/exchange")
    Observable<HttpResult<ExchageDimo>> exchange(@Field("user_id") int userId, @Field("charm_value") int charm_value);

    @FormUrlEncoded
    @POST("newExchange")
    Observable<HttpResult<ExchageDimo>> newExchange(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("v1.2.0/income")
    Observable<HttpResult<Profitincomebean>> income(@Field("user_id") int userId);

    @FormUrlEncoded
    @POST("randomRoomNum")
    Observable<HttpResult<RoomNumBean>> randomRoomNum(@Field("user_id") int userId);

    @FormUrlEncoded
    @POST("v2.0.2/editUser")
    Observable<HttpResult<Object>> editUer(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("grade")
    Observable<HttpResult<JSONObject>> grade(@Field("user_id") int userId);

    @FormUrlEncoded
    @POST("v1.3.0/rechargeHistory")
    Observable<HttpResult<List<CaseHistoryInfo>>> rechargeHistory(@Field("user_id") int userId, @Field("type") int type);

    @FormUrlEncoded
    @POST("heidouChargeHistory")
    Observable<HttpResult<List<CaseHistoryInfo>>> heidouChargeHistory(@Field("user_id") int userId, @Field("type") int type);

    @FormUrlEncoded
    @POST("regist")
    Observable<HttpResult<Object>> regist(@Field("phone_num") String phone, @Field("sms_code") String code, @Field("pwd") String pwd);

    @FormUrlEncoded
    @POST("registSendSms")
    Observable<HttpResult<Object>> registSendSms(@Field("phone_num") String num);

    @FormUrlEncoded
    @POST("friendByPhone")
    Observable<HttpResult<List<FriendInfo>>> friendByPhone(@Field("user_id") int userId, @Field("phone_num") String phone);

    @FormUrlEncoded
    @POST("deleteRecordMovie")
    Observable<HttpResult<Object>> deleteRecordMovie(@Field("roomNums") String roomNum);

    @FormUrlEncoded
    @POST("editRecordCount")
    Observable<HttpResult<Object>> editRecordCount(@Field("roomNums") int roomNum);

    @FormUrlEncoded
    @POST("recordLiveinfo")
    Observable<HttpResult<List<ReplayBean>>> recordLiveinfo(@Field("user_id") int userId, @Field("type") int type);

    @FormUrlEncoded
    @POST("v1.2.0/income")
    Observable<HttpResult<Profitincomebean>> income();

    @FormUrlEncoded
    @POST("v1.2.0/wxRecharge")
    Observable<HttpResult<WechatReqPayBean>> wxRecharge(@Field("user_id") int userId, @Field("money") int money, @Field("device") String device);

    @FormUrlEncoded
    @POST("v1.2.0/income")
    Observable<HttpResult<Profitincomebean>> income1();
}
