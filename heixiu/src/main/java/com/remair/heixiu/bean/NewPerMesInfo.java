package com.remair.heixiu.bean;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：JXH
 * 创建时间：2016/8/10 20:00
 * 修改人：LiuJun
 * 修改时间：2016/8/10 20:00
 * 修改备注：
 */
public class NewPerMesInfo implements Parcelable {

    /**
     * user_id : 1306
     * phone_num : 18612485932
     * photo : http://heixiu.oss-cn-beijing.aliyuncs.com/1306_head_20160716031959
     * high_photo : http://heixiu.oss-cn-beijing.aliyuncs.com/1306_cover_20160716031959
     * signature :
     * send_out_vca : 976291
     * gender : 0
     * identity : 0035884
     * attention_amount : 9
     * fans_amount : 130
     * replay_amount : 1
     * virtual_currency_amount : 775524
     * grade : 44
     * ticket_amount : 613844300
     * nickname : 读读记记九分裤咳咳疾风劲的韭菜鸡
     * relation_type : 1
     * address : 北京市
     * gift_list_refresh_tag : f1c686cd39b54f29acead56f444ce463
     * wx_withdraw_openid : oGzKFv5aqM1aIWxr6wcxsHTzru5E
     * forbid : 2
     * is_recharged : 0
     * type : 0
     * income_charm_value : 613837900
     * my_income : 2289489.2
     * my_fans_photo : ["http://heixiu.oss-cn-beijing.aliyuncs.com/test_1843_head_20160805223106","http://heixiu.oss-cn-beijing.aliyuncs.com/test_1294_1470497166840","http://heixiu.oss-cn-beijing.aliyuncs.com/test_1938_1470538272562"]
     * heidou_amount : 9887300
     * live_info : {"roomNum":18713,"userId":1306,"password":0,"type":0,"shareCount":0,"title":"test","groupId":"@TGS#3SNTEKBEN","praiseNum":0,"coverImage":"http://heixiu.oss-cn-beijing.aliyuncs.com/1306_cover_20160716031959","state":1,"beginTime":"2016-08-10 19:47:06","address":"北京市","viewingNum":0,"viewedNum":0,"duration":0,"charmValue":0,"heidouAmount":0,"channel_id":"16093425727656766528","ip":"106.38.128.185"}
     * send_heidou_amount : 925804
     * loveGrade : 1
     * loveGradeRatio : 0.0098
     */

    public int user_id;
    public String phone_num;
    public String photo;
    public String high_photo;
    public String signature;
    public int send_out_vca;
    public int gender;
    public String identity;
    public int attention_amount;
    public int fans_amount;
    public int replay_amount;
    public long virtual_currency_amount;
    public int grade;
    public int ticket_amount;
    public String nickname;
    public int relation_type;
    public String address;
    public String gift_list_refresh_tag;
    public String wx_withdraw_openid;
    public int forbid;
    public int is_recharged;
    public int type;
    public int income_charm_value;
    public double my_income;
    public int heidou_amount;
    public LiveInfoBean live_info;
    public SecretLiveInfoBean secret_live_info;
    public int send_heidou_amount;
    public int loveGrade;
    public double loveGradeRatio;
    public ArrayList<String> my_fans_photo;


    public NewPerMesInfo() {}


    @Override
    public int describeContents() { return 0; }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.user_id);
        dest.writeString(this.phone_num);
        dest.writeString(this.photo);
        dest.writeString(this.high_photo);
        dest.writeString(this.signature);
        dest.writeInt(this.send_out_vca);
        dest.writeInt(this.gender);
        dest.writeString(this.identity);
        dest.writeInt(this.attention_amount);
        dest.writeInt(this.fans_amount);
        dest.writeInt(this.replay_amount);
        dest.writeLong(this.virtual_currency_amount);
        dest.writeInt(this.grade);
        dest.writeInt(this.ticket_amount);
        dest.writeString(this.nickname);
        dest.writeInt(this.relation_type);
        dest.writeString(this.address);
        dest.writeString(this.gift_list_refresh_tag);
        dest.writeString(this.wx_withdraw_openid);
        dest.writeInt(this.forbid);
        dest.writeInt(this.is_recharged);
        dest.writeInt(this.type);
        dest.writeInt(this.income_charm_value);
        dest.writeDouble(this.my_income);
        dest.writeInt(this.heidou_amount);
        dest.writeParcelable(this.live_info, flags);
        dest.writeParcelable(this.secret_live_info, flags);
        dest.writeInt(this.send_heidou_amount);
        dest.writeInt(this.loveGrade);
        dest.writeDouble(this.loveGradeRatio);
        dest.writeStringList(this.my_fans_photo);
    }


    protected NewPerMesInfo(Parcel in) {
        this.user_id = in.readInt();
        this.phone_num = in.readString();
        this.photo = in.readString();
        this.high_photo = in.readString();
        this.signature = in.readString();
        this.send_out_vca = in.readInt();
        this.gender = in.readInt();
        this.identity = in.readString();
        this.attention_amount = in.readInt();
        this.fans_amount = in.readInt();
        this.replay_amount = in.readInt();
        this.virtual_currency_amount = in.readLong();
        this.grade = in.readInt();
        this.ticket_amount = in.readInt();
        this.nickname = in.readString();
        this.relation_type = in.readInt();
        this.address = in.readString();
        this.gift_list_refresh_tag = in.readString();
        this.wx_withdraw_openid = in.readString();
        this.forbid = in.readInt();
        this.is_recharged = in.readInt();
        this.type = in.readInt();
        this.income_charm_value = in.readInt();
        this.my_income = in.readDouble();
        this.heidou_amount = in.readInt();
        this.live_info = in.readParcelable(LiveInfoBean.class.getClassLoader());
        this.secret_live_info = in
                .readParcelable(SecretLiveInfoBean.class.getClassLoader());
        this.send_heidou_amount = in.readInt();
        this.loveGrade = in.readInt();
        this.loveGradeRatio = in.readDouble();
        this.my_fans_photo = in.createStringArrayList();
    }


    public static final Creator<NewPerMesInfo> CREATOR = new Creator<NewPerMesInfo>() {
        @Override
        public NewPerMesInfo createFromParcel(Parcel source) {return new NewPerMesInfo(source);}


        @Override
        public NewPerMesInfo[] newArray(int size) {return new NewPerMesInfo[size];}
    };
}
