package com.remair.heixiu.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class SecretLiveInfoBean implements Parcelable {
    /**
     * roomNum : 18713
     * userId : 1306
     * password : 0
     * type : 0
     * shareCount : 0
     * title : test
     * groupId : @TGS#3SNTEKBEN
     * praiseNum : 0
     * coverImage : http://heixiu.oss-cn-beijing.aliyuncs.com/1306_cover_20160716031959
     * state : 1
     * beginTime : 2016-08-10 19:47:06
     * address : 北京市
     * viewingNum : 0
     * viewedNum : 0
     * duration : 0
     * charmValue : 0
     * heidouAmount : 0
     * channel_id : 16093425727656766528
     * ip : 106.38.128.185
     */
    public int roomNum;
    public int userId;
    public int password;
    public int type;
    public int shareCount;
    public String title;
    public String groupId;
    public int praiseNum;
    public String coverImage;
    public int state;
    public String beginTime;
    public String address;
    public int viewingNum;
    public int viewedNum;
    public int duration;
    public int charmValue;
    public int heidouAmount;
    public String channel_id;
    public String ip;


    @Override
    public int describeContents() { return 0; }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.roomNum);
        dest.writeInt(this.userId);
        dest.writeInt(this.password);
        dest.writeInt(this.type);
        dest.writeInt(this.shareCount);
        dest.writeString(this.title);
        dest.writeString(this.groupId);
        dest.writeInt(this.praiseNum);
        dest.writeString(this.coverImage);
        dest.writeInt(this.state);
        dest.writeString(this.beginTime);
        dest.writeString(this.address);
        dest.writeInt(this.viewingNum);
        dest.writeInt(this.viewedNum);
        dest.writeInt(this.duration);
        dest.writeInt(this.charmValue);
        dest.writeInt(this.heidouAmount);
        dest.writeString(this.channel_id);
        dest.writeString(this.ip);
    }


    public SecretLiveInfoBean() {}


    protected SecretLiveInfoBean(Parcel in) {
        this.roomNum = in.readInt();
        this.userId = in.readInt();
        this.password = in.readInt();
        this.type = in.readInt();
        this.shareCount = in.readInt();
        this.title = in.readString();
        this.groupId = in.readString();
        this.praiseNum = in.readInt();
        this.coverImage = in.readString();
        this.state = in.readInt();
        this.beginTime = in.readString();
        this.address = in.readString();
        this.viewingNum = in.readInt();
        this.viewedNum = in.readInt();
        this.duration = in.readInt();
        this.charmValue = in.readInt();
        this.heidouAmount = in.readInt();
        this.channel_id = in.readString();
        this.ip = in.readString();
    }


    public static final Creator<SecretLiveInfoBean> CREATOR = new Creator<SecretLiveInfoBean>() {
        @Override
        public SecretLiveInfoBean createFromParcel(Parcel source) {return new SecretLiveInfoBean(source);}


        @Override
        public SecretLiveInfoBean[] newArray(int size) {return new SecretLiveInfoBean[size];}
    };
}