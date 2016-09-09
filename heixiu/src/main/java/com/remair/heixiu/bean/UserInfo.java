package com.remair.heixiu.bean;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class UserInfo implements Parcelable {
    /**
     * income_charm_value : 0
     * my_fans_photo : []
     * password : true
     */

    public long income_charm_value;//可提现魅力值
    public boolean password;//是否设置密码
    public List<String> my_fans_photo;//个人中心榜单前3位头像
    public int type;//判断是否加封杀接口
    public String mConstellation = "null";
    public int mLogo;
    public int mViewerCount = 0;
    public String mUserSig = "";
    public String groupid;
    public String high_photo = "";
    public int Env;
    public int user_id;//用户ID
    public String nickname;//用户昵称
    public int grade;//等级
    public String tlsSig;// 登录直播SDK的签名
    public long virtual_currency_amount;//虚拟货币数量
    public String empiric_value;//总经验值
    public int gender;//性别,-1未设定,0男,1女
    public int online;//是否在线
    public String signature;//签名
    public String photo;//头像地址
    public String address;//所在城市
    public int accept_push;//是否接收推送总开关
    public String phone_num;//手机号
    public int ticket_amount;// 票数
    public long send_heidou_amount;
    public int is_recharged;//是否第一次充值 0:没有充值过,1充值过
    public String send_out_vca; //送出货币数量
    public String identity; //身份ID
    public int attention_amount; //关注数量
    public int fans_amount; //粉丝数量
    public String wx_withdraw_openid;//是否绑定微信支付
    public int replay_amount;
    public String gift_list_refresh_tag;//更新礼物列表的标识,
    public int forbid; // 是否被封号 0:未封号,1封号,
    public int relation_type;
    public Boolean isCreater = false; //ture-直播创建者，false-直播观众
    public double my_income;//累计收入
    public int loveGrade;//七夕等级
    public double loveGradeRatio;//房间比率进度条
    public long heidou_amount;//嘿豆总数
    public long charm_value_valid;
    public long ticket_mount;//票数
    public String device;//设备类型
    public String token;


    public UserInfo() {}


    @Override
    public int describeContents() { return 0; }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.income_charm_value);
        dest.writeByte(this.password ? (byte) 1 : (byte) 0);
        dest.writeStringList(this.my_fans_photo);
        dest.writeInt(this.type);
        dest.writeString(this.mConstellation);
        dest.writeInt(this.mLogo);
        dest.writeInt(this.mViewerCount);
        dest.writeString(this.mUserSig);
        dest.writeString(this.groupid);
        dest.writeString(this.high_photo);
        dest.writeInt(this.Env);
        dest.writeInt(this.user_id);
        dest.writeString(this.nickname);
        dest.writeInt(this.grade);
        dest.writeString(this.tlsSig);
        dest.writeLong(this.virtual_currency_amount);
        dest.writeString(this.empiric_value);
        dest.writeInt(this.gender);
        dest.writeInt(this.online);
        dest.writeString(this.signature);
        dest.writeString(this.photo);
        dest.writeString(this.address);
        dest.writeInt(this.accept_push);
        dest.writeString(this.phone_num);
        dest.writeInt(this.ticket_amount);
        dest.writeLong(this.send_heidou_amount);
        dest.writeInt(this.is_recharged);
        dest.writeString(this.send_out_vca);
        dest.writeString(this.identity);
        dest.writeInt(this.attention_amount);
        dest.writeInt(this.fans_amount);
        dest.writeString(this.wx_withdraw_openid);
        dest.writeInt(this.replay_amount);
        dest.writeString(this.gift_list_refresh_tag);
        dest.writeInt(this.forbid);
        dest.writeInt(this.relation_type);
        dest.writeValue(this.isCreater);
        dest.writeDouble(this.my_income);
        dest.writeInt(this.loveGrade);
        dest.writeDouble(this.loveGradeRatio);
        dest.writeLong(this.heidou_amount);
        dest.writeLong(this.charm_value_valid);
        dest.writeLong(this.ticket_mount);
        dest.writeString(this.device);
        dest.writeString(this.token);
    }


    protected UserInfo(Parcel in) {
        this.income_charm_value = in.readLong();
        this.password = in.readByte() != 0;
        this.my_fans_photo = in.createStringArrayList();
        this.type = in.readInt();
        this.mConstellation = in.readString();
        this.mLogo = in.readInt();
        this.mViewerCount = in.readInt();
        this.mUserSig = in.readString();
        this.groupid = in.readString();
        this.high_photo = in.readString();
        this.Env = in.readInt();
        this.user_id = in.readInt();
        this.nickname = in.readString();
        this.grade = in.readInt();
        this.tlsSig = in.readString();
        this.virtual_currency_amount = in.readLong();
        this.empiric_value = in.readString();
        this.gender = in.readInt();
        this.online = in.readInt();
        this.signature = in.readString();
        this.photo = in.readString();
        this.address = in.readString();
        this.accept_push = in.readInt();
        this.phone_num = in.readString();
        this.ticket_amount = in.readInt();
        this.send_heidou_amount = in.readLong();
        this.is_recharged = in.readInt();
        this.send_out_vca = in.readString();
        this.identity = in.readString();
        this.attention_amount = in.readInt();
        this.fans_amount = in.readInt();
        this.wx_withdraw_openid = in.readString();
        this.replay_amount = in.readInt();
        this.gift_list_refresh_tag = in.readString();
        this.forbid = in.readInt();
        this.relation_type = in.readInt();
        this.isCreater = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.my_income = in.readDouble();
        this.loveGrade = in.readInt();
        this.loveGradeRatio = in.readDouble();
        this.heidou_amount = in.readLong();
        this.charm_value_valid = in.readLong();
        this.ticket_mount = in.readLong();
        this.device = in.readString();
        this.token = in.readString();
    }


    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel source) {return new UserInfo(source);}


        @Override
        public UserInfo[] newArray(int size) {return new UserInfo[size];}
    };
}
