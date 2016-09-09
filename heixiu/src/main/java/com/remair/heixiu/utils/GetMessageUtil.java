package com.remair.heixiu.utils;

import android.content.Context;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.HXJavaNet;
import com.remair.heixiu.sqlite.ConcernInfoDB;
import com.remair.heixiu.sqlite.ConcernInfoDao;
import com.remair.heixiu.sqlite.MessageInfoDB;
import com.remair.heixiu.sqlite.UnFollowConcernInfoDB;
import com.remair.heixiu.sqlite.UnFollowConcernInfoDao;
import com.tencent.TIMConversation;
import com.tencent.TIMCustomElem;
import com.tencent.TIMElem;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageStatus;
import com.tencent.TIMValueCallBack;
import java.util.HashMap;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import studio.archangel.toolkitv2.util.networking.AngelNetCallBack;

/**
 * Created by wsk on 16/5/2.
 */
public class GetMessageUtil {

    public static void getMessage(final TIMConversation mConversation, final int issend, final int sendstate) {
        if (mConversation == null) {
            return;
        }
        mConversation
                .getMessage(1, null, new TIMValueCallBack<List<TIMMessage>>() {
                    @Override
                    public void onError(int code, String desc) {
                        //                mPBLoadData.setVisibility(View.GONE);
                        //                mIsLoading = false;
                    }


                    @Override
                    public void onSuccess(List<TIMMessage> msgs) {

                        final List<TIMMessage> tmpMsgs = msgs;
                        if (msgs.size() > 0) {
                            mConversation.getUnreadMessageNum();
                            mConversation
                                    .setReadMessage(msgs.get(msgs.size() - 1));
                        }
                        //                if( !mBNerverLoadMore && (msgs.size() < mLoadMsgNum) )
                        //                    listChatEntity.clear();
                        for (int i = 0; i < tmpMsgs.size(); i++) {
                            TIMMessage msg = tmpMsgs.get(i);
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
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
    }


    //保存数据库
    public static void showPerInfoCard(String user_id, final String viewed_user_id, final String tex, final Context context, final TIMMessage message) {
        HashMap<String, Object> para = new HashMap<>();
        para.put("user_id", user_id);
        para.put("viewed_user_id", viewed_user_id);

        HXJavaNet.post(HXJavaNet.url_getuserinfo, para, new AngelNetCallBack() {
            @Override
            public void onSuccess(int code, String data, String msg) {
                if (code == 200) {
                    try {
                        JSONObject jo = new JSONObject(data);
                        JSONObject user = new JSONObject();
                        user.put("user_id", jo.optString("user_id"));
                        user.put("user_avatar", jo.optString("photo"));
                        user.put("user_name", jo.optString("nickname"));
                        user.put("address", jo.optString("address"));
                        user.put("ticket_amount", jo
                                .optString("ticket_amount"));
                        user.put("fans_amount", jo.optInt("fans_amount"));
                        user.put("relation_type", jo.optInt("relation_type"));
                        user.put("grade", jo.optString("grade"));
                        user.put("gender", jo.optString("gender"));
                        user.put("send_out_vca", jo.optString("send_out_vca"));
                        user.put("identity", jo.optString("identity"));

                        savaData(jo.optInt("relation_type"), tex, user
                                .toString(), context, viewed_user_id, message);
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


    public static void savaData(int relation, String text, String userInfo, final Context context, String viewed_user_id, TIMMessage message) {
        try {
            if (!viewed_user_id
                    .equals(HXApp.getInstance().getUserInfo().user_id + "")) {
                if (relation == 1) {
                    ConcernInfoDao concernInfoDao = new ConcernInfoDao(context);
                    ConcernInfoDB concernInfoDB = new ConcernInfoDB();
                    concernInfoDB.setUser_id(viewed_user_id);
                    concernInfoDB.setUserinfo(userInfo);
                    concernInfoDB.setLastmessage(text);
                    concernInfoDB.setHxtype("");
                    concernInfoDB.setUnread("");
                    concernInfoDB.setRelation(relation);
                    concernInfoDB.setUpdatetime(message.timestamp());

                    concernInfoDao.addorupdate(concernInfoDB, viewed_user_id);
                } else if (relation == 0) {

                    UnFollowConcernInfoDao unFollowConcernInfoDao = new UnFollowConcernInfoDao(context);
                    UnFollowConcernInfoDB unFollowConcernInfoDB = new UnFollowConcernInfoDB();
                    unFollowConcernInfoDB.setUser_id(viewed_user_id);
                    unFollowConcernInfoDB.setUserinfo(userInfo);
                    unFollowConcernInfoDB.setLastmessage(text);
                    unFollowConcernInfoDB.setHxtype("");
                    unFollowConcernInfoDB.setUnread("");
                    unFollowConcernInfoDB.setRelation(relation);
                    unFollowConcernInfoDB.setUpdatetime(message.timestamp());
                    unFollowConcernInfoDao
                            .addorupdate(unFollowConcernInfoDB, viewed_user_id);
                } else {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //有个人信息的时候用
    public static void savaDatatwo(int relation, String text, final Context context, String viewed_user_id) {
        try {
            if (relation == 1) {
                ConcernInfoDao concernInfoDao = new ConcernInfoDao(context);
                concernInfoDao.Modify(viewed_user_id, text);
            } else if (relation == 0) {
                UnFollowConcernInfoDao unFollowConcernInfoDao = new UnFollowConcernInfoDao(context);
                unFollowConcernInfoDao.Modify(viewed_user_id, text);
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void chageSavaData(final Context context, String userid, boolean flag) {
        if (flag) {//由未关注转为关注
            try {
                UnFollowConcernInfoDao unFollowConcernInfoDao = new UnFollowConcernInfoDao(context);
                UnFollowConcernInfoDB coninfo = unFollowConcernInfoDao
                        .getUnFollowConcernInfoByUId(userid);
                if (null != coninfo) {
                    ConcernInfoDao concernInfoDao = new ConcernInfoDao(context);
                    ConcernInfoDB concernInfoDB = new ConcernInfoDB();
                    concernInfoDB.setUser_id(coninfo.getUser_id());
                    concernInfoDB.setUserinfo(coninfo.getUserinfo());
                    concernInfoDB.setLastmessage(coninfo.getLastmessage());
                    concernInfoDB.setHxtype("");
                    concernInfoDB.setUnread("");
                    concernInfoDB.setRelation(1);
                    concernInfoDB.setUpdatetime(coninfo.getUpdatetime());

                    concernInfoDao.addorupdate(concernInfoDB, userid);
                    unFollowConcernInfoDao
                            .deleteUnFollowConcernInfoByUId(userid);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {//由关注转为未关注
            try {
                ConcernInfoDao concernInfoDao = new ConcernInfoDao(context);
                ConcernInfoDB info = concernInfoDao.getConcernInfoByUId(userid);
                if (null != info) {
                    UnFollowConcernInfoDao unFollowConcernInfoDao = new UnFollowConcernInfoDao(context);
                    UnFollowConcernInfoDB unFollowConcernInfoDB = new UnFollowConcernInfoDB();
                    unFollowConcernInfoDB.setUser_id(info.getUser_id());
                    unFollowConcernInfoDB.setUserinfo(info.getUserinfo());
                    unFollowConcernInfoDB.setLastmessage(info.getLastmessage());
                    unFollowConcernInfoDB.setHxtype("");
                    unFollowConcernInfoDB.setUnread("");
                    unFollowConcernInfoDB.setRelation(0);
                    unFollowConcernInfoDB.setUpdatetime(info.getUpdatetime());

                    unFollowConcernInfoDao
                            .addorupdate(unFollowConcernInfoDB, userid);
                    concernInfoDao.deleteConcernInfoByUId(userid);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
