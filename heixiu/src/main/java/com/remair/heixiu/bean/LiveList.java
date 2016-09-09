package com.remair.heixiu.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class LiveList implements ConcernListBean,Parcelable {
    public String address;//地址
    public String cover_image;//封面图片
    public String grade;//等级
    public String group_id;//组id
    public String nickname;//昵称
    public String photo;//头像
    public int praise_num;//点赞数
    public int room_num;//房间id
    public int state;//
    public String title;//直播标题
    public int user_id;//用户id
    public int viewed_num;//所有观看人数
    public int viewing_num;//当前观看人数
    public int liveType;
    public String identity;


    public LiveList() {}


    @Override
    public int describeContents() { return 0; }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.address);
        dest.writeString(this.cover_image);
        dest.writeString(this.grade);
        dest.writeString(this.group_id);
        dest.writeString(this.nickname);
        dest.writeString(this.photo);
        dest.writeInt(this.praise_num);
        dest.writeInt(this.room_num);
        dest.writeInt(this.state);
        dest.writeString(this.title);
        dest.writeInt(this.user_id);
        dest.writeInt(this.viewed_num);
        dest.writeInt(this.viewing_num);
        dest.writeInt(this.liveType);
        dest.writeString(this.identity);
    }


    protected LiveList(Parcel in) {
        this.address = in.readString();
        this.cover_image = in.readString();
        this.grade = in.readString();
        this.group_id = in.readString();
        this.nickname = in.readString();
        this.photo = in.readString();
        this.praise_num = in.readInt();
        this.room_num = in.readInt();
        this.state = in.readInt();
        this.title = in.readString();
        this.user_id = in.readInt();
        this.viewed_num = in.readInt();
        this.viewing_num = in.readInt();
        this.liveType = in.readInt();
        this.identity = in.readString();
    }


    public static final Creator<LiveList> CREATOR = new Creator<LiveList>() {
        @Override
        public LiveList createFromParcel(Parcel source) {return new LiveList(source);}


        @Override
        public LiveList[] newArray(int size) {return new LiveList[size];}
    };
}
