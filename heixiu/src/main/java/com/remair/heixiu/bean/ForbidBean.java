package com.remair.heixiu.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：Jw
 * 创建时间：16/9/2 下午5:07
 * 修改人：
 * 修改时间：16/9/2 下午5:07
 * 修改备注：
 */
public class ForbidBean implements Parcelable {
    public int forbid_penalize;//封禁时间
    public String forbid_reason;
    public int forbid_freeTime;//几天解封


    @Override
    public int describeContents() { return 0; }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.forbid_penalize);
        dest.writeString(this.forbid_reason);
        dest.writeInt(this.forbid_freeTime);
    }


    public ForbidBean() {}


    protected ForbidBean(Parcel in) {
        this.forbid_penalize = in.readInt();
        this.forbid_reason = in.readString();
        this.forbid_freeTime = in.readInt();
    }


    public static final Parcelable.Creator<ForbidBean> CREATOR = new Parcelable.Creator<ForbidBean>() {
        @Override
        public ForbidBean createFromParcel(Parcel source) {return new ForbidBean(source);}


        @Override
        public ForbidBean[] newArray(int size) {return new ForbidBean[size];}
    };
}
