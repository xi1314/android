package com.remair.heixiu.bean;

/**
 * 项目名称：heixu
 * 类描述：
 * 创建人：liuyu
 *
 */
public class Dimaondex {
    public int number;//要兑换的
    public int number_value;//下面需要消费的
    public int type;//1代表现金充值 //2代表存在感兑换钻石    //3代表钻石兑换黑豆//4代表存在感对黑豆

    public Dimaondex(int number, int number_value,int type) {
        this.number = number;
        this.number_value = number_value;
        this.type=type;
    }
}
