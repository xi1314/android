package com.remair.heixiu.sqlite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.tencent.TIMMessageStatus;

/**
 * Created by wsk on 16/4/29.
 */
@DatabaseTable(tableName = "message")
public class MessageInfoDB {
    @DatabaseField(id = true, canBeNull = false)
    private String uuid;//ID
    @DatabaseField
    private String userid;//用户id
    @DatabaseField
    private String message;//发送内容
    @DatabaseField
    private TIMMessageStatus sendstatue;//发送状态
    @DatabaseField
    private int issend;//O本人发，1其他发
    @DatabaseField
    private String messagetype;//消息类型
    @DatabaseField
    private int type;//0 系统消息 1 普通消息
    @DatabaseField
    private long createtime;//发送时间
    @DatabaseField
    private long time;//录音时间
    //为了删除功能，加上message
//    @DatabaseField
//    private TIMMessage timMessage;
//
//    public TIMMessage getTimMessage() {
//        return timMessage;
//    }
//
//    public void setTimMessage(TIMMessage timMessage) {
//        this.timMessage = timMessage;
//    }


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TIMMessageStatus getSendstatue() {
        return sendstatue;
    }

    public void setSendstatue(TIMMessageStatus sendstatue) {
        this.sendstatue = sendstatue;
    }

    public int getIssend() {
        return issend;
    }

    public void setIssend(int issend) {
        this.issend = issend;
    }

    public String getMessagetype() {
        return messagetype;
    }

    public void setMessagetype(String messagetype) {
        this.messagetype = messagetype;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }
}
