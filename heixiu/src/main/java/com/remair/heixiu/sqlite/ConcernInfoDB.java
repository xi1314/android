package com.remair.heixiu.sqlite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.remair.heixiu.bean.PrivateMessage;

/**
 * Created by wsk on 16/4/28.
 * 私聊关注列表
 */

@DatabaseTable(tableName = "concern")
public class ConcernInfoDB extends PrivateMessage {
    @DatabaseField(id = true, canBeNull = false)
    public String user_id;//用户id
    @DatabaseField
    public String userinfo;//用户信息
    @DatabaseField
    public String unread;//是否读
    @DatabaseField
    public String hxtype;//类型0系统，1其他信息
    @DatabaseField
    public int relation;//关系
    @DatabaseField
    public String lastmessage;//最后一条消息
    @DatabaseField
    public long updatetime;//更新时间

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(String userinfo) {
        this.userinfo = userinfo;
    }

    public String getUnread() {
        return unread;
    }

    public void setUnread(String unread) {
        this.unread = unread;
    }

    public String getHxtype() {
        return hxtype;
    }

    public void setHxtype(String hxtype) {
        this.hxtype = hxtype;
    }

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }

    public String getLastmessage() {
        return lastmessage;
    }

    public void setLastmessage(String lastmessage) {
        this.lastmessage = lastmessage;
    }

    public long getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(long updatetime) {
        this.updatetime = updatetime;
    }
}
