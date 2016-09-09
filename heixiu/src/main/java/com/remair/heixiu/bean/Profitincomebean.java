package com.remair.heixiu.bean;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：heixu
 * 类描述：
 * 创建人：Liuyu
 * 创建时间：2016/9/2 15:22
 * 修改人：Liuyu
 * 修改时间：2016/9/2 15:22
 * 修改备注：
 */
public class Profitincomebean implements Parcelable{
    public double today_money;
    public double total_money;
    public long charm_value_valid;
    public double month_already_total_money;
    public List<Week_list> week_list;

    public static class Week_list implements Parcelable {
        public int id;

        public int userId;

        public double income;

        public String time;


        @Override
        public int describeContents() { return 0; }


        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeInt(this.userId);
            dest.writeDouble(this.income);
            dest.writeString(this.time);
        }


        public Week_list() {}


        protected Week_list(Parcel in) {
            this.id = in.readInt();
            this.userId = in.readInt();
            this.income = in.readDouble();
            this.time = in.readString();
        }


        public static final Creator<Week_list> CREATOR = new Creator<Week_list>() {
            @Override
            public Week_list createFromParcel(Parcel source) {return new Week_list(source);}


            @Override
            public Week_list[] newArray(int size) {return new Week_list[size];}
        };
    }


    @Override
    public int describeContents() { return 0; }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.today_money);
        dest.writeDouble(this.total_money);
        dest.writeLong(this.charm_value_valid);
        dest.writeDouble(this.month_already_total_money);
        dest.writeList(this.week_list);
    }


    public Profitincomebean() {}


    protected Profitincomebean(Parcel in) {
        this.today_money = in.readDouble();
        this.total_money = in.readDouble();
        this.charm_value_valid = in.readLong();
        this.month_already_total_money = in.readDouble();
        this.week_list = new ArrayList<Week_list>();
        in.readList(this.week_list, Week_list.class.getClassLoader());
    }


    public static final Creator<Profitincomebean> CREATOR = new Creator<Profitincomebean>() {
        @Override
        public Profitincomebean createFromParcel(Parcel source) {return new Profitincomebean(source);}


        @Override
        public Profitincomebean[] newArray(int size) {return new Profitincomebean[size];}
    };
}
