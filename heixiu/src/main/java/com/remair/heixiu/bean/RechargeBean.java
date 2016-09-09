package com.remair.heixiu.bean;

/**
 * Created by JXHIUUI on 2016/6/11.
 */
public class RechargeBean {

    public int money;
    public int count;
    public int type;//0钻石1魔币
    public int grade;


    public RechargeBean(int money, int count, int type, int grade) {
        this.money = money;
        this.count = count;
        this.type = type;
        this.grade = grade;
    }
}
