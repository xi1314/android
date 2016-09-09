package com.remair.heixiu.bean;

/**
 * Created by JXHIUUI on 2016/4/26.
 */
public class ReplayBean {

    /*
    {
        "roomNum": 7192,
        "title": "",
        "beginTime": "2016-05-04 16:59:51",
        "endTime": "2016-05-04 17:00:03",
        "vid": "200006314_ae693ebc154d4e6e8c8b7aa0151db316",
        "url": "http://200006314.vod.myqcloud.com/200006314_ae693ebc154d4e6e8c8b7aa0151db316.f0.mp4",
        "duration": 10,
        "image_url": "http://p.qpic.cn/videoyun/0/200006314_ae693ebc154d4e6e8c8b7aa0151db316_1/640",
        "fileName": "JXHIUUI_2016-05-04-16-59-52_2016-05-04-17-00-02",
        "record_count": 0,
        "userEntity": {
            "userId": 1294,
            "nickname": "JXHIUUI",
            "photo": "http://q.qlogo.cn/qqapp/1105162427/81422C0DA7EEC35470F5589BDDAB7B2C/40"
        }
    }
    * */
    public String fileName;
    public int duration;
    public String image_url;
    public String url;
    public String roomNum;
    public int record_count;
    public UserEntity userEntity;
    public String beginTime;
    public String endTime;

    public class UserEntity {
        public int userId;
        public String photo;
        public String nickname;
    }

}
