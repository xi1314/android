package com.remair.heixiu.bean;

import java.util.ArrayList;

/**
 * 项目名称：Android
 * 类描述：注册推荐列表
 * 创建人：wsk
 * 创建时间：16/8/29 下午2:25
 * 修改人：wsk
 * 修改时间：16/8/29 下午2:25
 * 修改备注：
 */
public class RecommendBean {
    /**
     * accept_push : 1
     * items : [{"relation_id":4,"gender":1,"empiric":1,"signature":"","photo":"","accept_push":0}]
     */

    public int accept_push;
    public ArrayList<ItemsBean> items;

    public static class ItemsBean {
        public int relation_id;
        public int gender;
        public int empiric;
        public String signature;
        public String photo;
        public int accept_push;
        public int user_id;
        public String nickname;//昵称
    }
}
