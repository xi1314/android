package com.remair.heixiu.bean;

/**
 * 关注列表 Bean对象
 * Created by JXHIUUI on 2016/3/4.
 */
public class AttentionBean {
    public int relation_id;//用户关系主键
    public int user_id;
    public String photo;//头像路径
    public String nickname;//昵称
    public String signature;//签名
    public int gender;//性别
    public int grade;//等级
    public int accept_push;//接受推送

    public AttentionBean(int relation_id, int user_id, String photo, String nickname, String signature, int gender, int grade) {
        this.relation_id = relation_id;
        this.user_id = user_id;
        this.photo = photo;
        this.nickname = nickname;
        this.signature = signature;
        this.gender = gender;
        this.grade = grade;
    }

    public AttentionBean() {
    }

    @Override
    public String toString() {
        return "AttentionBean{" +
                "relation_id='" + relation_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", photo='" + photo + '\'' +
                ", nickname='" + nickname + '\'' +
                ", signature='" + signature + '\'' +
                ", gender=" + gender +
                ", grade=" + grade +
                '}';
    }
}
