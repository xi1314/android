package com.remair.heixiu.sqlite;

import android.content.Context;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by wsk on 16/4/29.
 */
public class MessageInfoDao {
    private Context context;
    private Dao<MessageInfoDB, String> DaoOpe;
    private DatabaseHelper helper;


    public MessageInfoDao(Context context) {
        this.context = context;
        try {
            helper = DatabaseHelper.getHelper(context);
            DaoOpe = helper.getDao(MessageInfoDB.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*** 通过UserId获取数据 **/
    public List<MessageInfoDB> getMessageInfoByUId(String user_id) throws Exception {
        if (null != user_id) {
            List<MessageInfoDB> info = DaoOpe.queryBuilder().where()
                                             .eq("userid", user_id).query();
            return info;
        }
        return null;
    }


    public void deleteMessage(String userid) throws SQLException {
        DaoOpe.deleteById(userid);
    }


    public void deleteMessageall(String userid) throws SQLException {
        DaoOpe.deleteBuilder().where().eq("userid", userid).clear();
    }


    /*** 获取所有数据 **/
    public List<MessageInfoDB> getMessageInfoAll() throws Exception {
        List<MessageInfoDB> infoList = DaoOpe.queryForAll();
        return infoList;
    }


    public List<MessageInfoDB> getMessageInfo(long offset, long limit, String userid) {
        try {
            List<MessageInfoDB> infoList = DaoOpe.queryBuilder()
                                                 .orderBy("createtime", false)
                                                 .offset(offset).limit(limit)
                                                 .where().eq("userid", userid)
                                                 .query();
            return infoList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /*** 添加数据 **/
    public void addorupdate(MessageInfoDB info, String uuid) {
        try {
            MessageInfoDB messageinfo = DaoOpe.queryForId(uuid);
            if (null == messageinfo) {
                DaoOpe.create(info);
            } else {
                messageinfo.setSendstatue(info.getSendstatue());
                DaoOpe.createOrUpdate(messageinfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
