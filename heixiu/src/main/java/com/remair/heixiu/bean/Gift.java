package com.remair.heixiu.bean;

import org.json.JSONException;
import org.json.JSONObject;
import studio.archangel.toolkitv2.interfaces.JSONizable;

/**
 * Created by Michael
 * gift model
 * {"gift_id":"16","name":"棒棒糖","description":"","price":1,"empiric_value":10,"unit_name":"个","image":"http:\/\/heixiu.oss-cn-beijing.aliyuncs.com\/gift%2F%E6%A3%92%E6%A3%92%E7%B3%96.png","animation":0,"animation_image":"","duration":0}
 */
public class Gift implements JSONizable {
    public String gift_id;
    public String name;
    public String count_unit;
    public String image;
    public String description;
    public int animation; //是否有动画效果
    public String animation_image;//动画效果的gif图片地址,这个只有在animation字段是1的时候有效
    public int price = 0;//所需金币的数量
    public int empiric_value = 0;//经验值
    public Double duration = 0d;//GIF播放时间
    public int select_animation;//0不展示，1展示
    public String select_animation_image;//选中效果图
    public int type;//礼物类型 0钻石普通道具1钻石豪华道具2嘿豆道具


    public Gift() {

    }


    public Gift(JSONObject jo) {
        if (jo == null) {
            return;
        }
        gift_id = jo.optString("gift_id");
        name = jo.optString("name");
        count_unit = jo.optString("unit_name");
        image = jo.optString("image");
        description = jo.optString("description");
        animation_image = jo.optString("animation_image");
        animation = jo.optInt("animation");
        price = jo.optInt("price");

        duration = jo.optDouble("duration");

        empiric_value = jo.optInt("empiric_value");
        select_animation = jo.optInt("select_animation");
        select_animation_image = jo.optString("select_animation_image");
        type = jo.optInt("type");
    }


    @Override
    public JSONObject toJson() {
        JSONObject jo = new JSONObject();
        try {
            jo.put("gift_id", gift_id);
            jo.put("name", name);
            jo.put("unit_name", count_unit);
            jo.put("image", image);
            jo.put("description", description);
            jo.put("animation", animation);
            jo.put("animation_image", animation_image);
            jo.put("price", price);
            jo.put("duration", duration);
            jo.put("empiric_value", empiric_value);
            jo.put("select_animation", select_animation);
            jo.put("select_animation_image", select_animation_image);
            jo.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
