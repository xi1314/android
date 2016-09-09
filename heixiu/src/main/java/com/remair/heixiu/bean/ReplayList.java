package com.remair.heixiu.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class ReplayList implements ConcernListBean, Parcelable {
    public String begin_time;//开始时间
    public String cover_image;//封面图片
    public int duration;//持续时间
    public String praise_num;//点赞数
    public int room_num;//房间id
    public String title;//直播标题
    public String url;//播放地址
    public int viewing_num;//当前观看人数


    @Override
    public int describeContents() { return 0; }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.begin_time);
        dest.writeString(this.cover_image);
        dest.writeInt(this.duration);
        dest.writeString(this.praise_num);
        dest.writeInt(this.room_num);
        dest.writeString(this.title);
        dest.writeString(this.url);
        dest.writeInt(this.viewing_num);
    }


    public ReplayList() {}


    protected ReplayList(Parcel in) {
        this.begin_time = in.readString();
        this.cover_image = in.readString();
        this.duration = in.readInt();
        this.praise_num = in.readString();
        this.room_num = in.readInt();
        this.title = in.readString();
        this.url = in.readString();
        this.viewing_num = in.readInt();
    }


    public static final Creator<ReplayList> CREATOR = new Creator<ReplayList>() {
        @Override
        public ReplayList createFromParcel(Parcel source) {return new ReplayList(source);}


        @Override
        public ReplayList[] newArray(int size) {return new ReplayList[size];}
    };
}