package com.remair.heixiu.giftview;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.remair.heixiu.R;
import com.remair.heixiu.adapters.LiveChatMessageAdapter;
import com.remair.heixiu.utils.SharedPreferenceUtil;
import com.remair.heixiu.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wsk on 16/5/10.
 */
public class GetGift {
    private Context context;
    private TextView meilizhi_textview;
    private Handler mHandler;
    private ImageView giftbig_fl;
    private RecyclerView list_chat, list_chat2, act_live_rv_gift;
    private ArrayList<JSONObject> messages2, messages, messagesgift;
    private LiveChatMessageAdapter adapter_chat, adapter_chat2, adapter_gift;
    private Map<String, List<String>> giftMap;
    private long startTime = 0;
    private long startTimetwo = 0;
    private long lonone, twotime;
    private final int delay = 301;//时间纪录
    private final int delaytwo = 302;//时间纪录
    private final int delayGIF = 303;//GIF
    private long secondtime = 4000;//礼物显示几秒
    private List<String> temp = null;

    public GetGift(Context context, Handler mHandler, TextView meilizhi_textview, ImageView giftbig_fl, RecyclerView list_chat, RecyclerView list_chat2, RecyclerView act_live_rv_gift, ArrayList<JSONObject> messages2, ArrayList<JSONObject> messages, ArrayList<JSONObject> messagesgift, LiveChatMessageAdapter adapter_chat, LiveChatMessageAdapter adapter_chat2, LiveChatMessageAdapter adapter_gift, Map<String, List<String>> giftMap) {
        this.context = context;
        this.mHandler = mHandler;
        this.meilizhi_textview = meilizhi_textview;
        this.giftbig_fl = giftbig_fl;
        this.list_chat = list_chat;
        this.list_chat2 = list_chat2;
        this.act_live_rv_gift = act_live_rv_gift;
        this.messages = messages;
        this.messages2 = messages2;
        this.messagesgift = messagesgift;
        this.adapter_chat = adapter_chat;
        this.adapter_chat2 = adapter_chat2;
        this.adapter_gift = adapter_gift;
        this.giftMap = giftMap;
    }


    //itm礼物接受
    public synchronized void addMessageToList1(JSONObject queue) {
        try {
//            SendGiftBean sendGiftBean= Utils.jsonToBean(queue.toString(), SendGiftBean.class);
            String newgift_id = queue.getJSONObject("command").getJSONObject("gift").getString("gift_id");
            String newuser_phone = queue.getJSONObject("user").getString("user_id");
            String gif_image = queue.getJSONObject("command").getJSONObject("gift").getString("gif_image");
            int isGif = queue.getJSONObject("command").getJSONObject("gift").getInt("isGif");

            String sign = queue.getJSONObject("command").getJSONObject("gift").getString("sign");

            final Double duration = queue.getJSONObject("command").getJSONObject("gift").getDouble("duration");
            int charm_value = queue.getJSONObject("command").getInt("charm_value");
            meilizhi_textview.setText(charm_value + "");

            if (isGif == 1) {
                giftbig_fl.setVisibility(View.VISIBLE);
//                String gifturlloat= Environment.getExternalStorageDirectory()+"/image/bb6936234053a8dd7efb0a00c16a6362.gif";
                String gifturlloat=SharedPreferenceUtil.getString(newgift_id,"");
//                if (null!=gifturlloat){
////                    Glide.with(this).load(R.mipmap.login).bitmapTransform(new BlurTransformation(this, 20), new CropCircleTransformation(this)).into(imageView);
////                    Glide.with(context).load(gifturlloat).bitmapTransform().into(giftbig_fl);
//                    Glide.with(context).load(gifturlloat).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(giftbig_fl);
//                    mHandler.sendEmptyMessageDelayed(delayGIF, new Double(duration * 1000 ).longValue());
//                }else{
//                    Glide.with(context).load(gif_image).centerCrop().crossFade().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(new GlideDrawableImageViewTarget(giftbig_fl) {
//                        @Override
//                        public void onResourceReady(GlideDrawable arg0, GlideAnimation<? super GlideDrawable> arg1) {
//                            super.onResourceReady(arg0, arg1);
//                            mHandler.sendEmptyMessageDelayed(delayGIF, new Double(duration * 1000 ).longValue());
//                        } });
//                }

                    }else{
                        if (sign.equals("gif")) {
                            String time = Utils.getStringDate();
                            sign = newuser_phone + "_" + time;
                        }
                        String longTime = sign.split("_")[1];
                        long longtime = Utils.getData(longTime).getTime();
                        String giftkey = newuser_phone + newgift_id;
                        if ((giftMap.keySet()).contains(giftkey)) {
                            int s = (giftMap.get(giftkey).size() - 1);
                            long ll = Long.parseLong(giftMap.get(giftkey).get((giftMap.get(giftkey).size() - 1)));
                            if (longtime - ll > 5000) {
                                giftMap.get(giftkey).clear();
                            }
                            giftMap.get(giftkey).add(longtime + "");
                        } else {
                            temp = new ArrayList<String>();
                            temp.add(longtime + "");
                            giftMap.put(giftkey, temp);
                        }
                        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
                        itemAnimator.setAddDuration(200);
                        itemAnimator.setMoveDuration(200);
                        list_chat2.setItemAnimator(itemAnimator);
                        List<String> users = new ArrayList<>();
                        List<String> gifts = new ArrayList<>();
                        if (messages2.size() == 0 && messagesgift.size() == 0) {
                            if (giftMap.get(giftkey).size() == 1) {
                                messages2.add(0, queue);
                                adapter_chat2.notifyItemInserted(0);
                                long lonone = System.currentTimeMillis();
                                mHandler.sendEmptyMessageDelayed(delay, secondtime);
                                startTime = lonone;
                            }
//                    else{
//                        View view = list_chat2.getChildAt(0);
//                        TextView tv_count = (TextView) view.findViewById(R.id.tv_count);
//                        tv_count.setText("X" + giftMap.get(giftkey).size());
//                        Animation scaleAnimation = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f);
//                        //设置动画时间
//                        scaleAnimation.setDuration(500);
//                        scaleAnimation.setInterpolator(new CycleInterpolator(1f));
//                        tv_count.startAnimation(scaleAnimation);
//                        SimpleDraweeView iv_gift = (SimpleDraweeView) view.findViewById(R.id.item_live_chat_gift);
//                        TranslateAnimation translateAnimation = new TranslateAnimation(0f, 0.0f, 0.1f, 0.1f);
//                        translateAnimation.setDuration(0);
//                        iv_gift.startAnimation(translateAnimation);
//                        lonone = System.currentTimeMillis();
//                        if (startTime != 0) {
//                            if ((lonone - startTime) <= secondtime) {
//                                mHandler.removeMessages(delay);
//                            }
//
//                        }
//                        mHandler.sendEmptyMessageDelayed(delay, secondtime);
//                    }

                        } else if (messages2.size() == 1 && messagesgift.size() == 0) {
                            JSONObject jsonObject = new JSONObject(messages2.get(0).toString());
                            String gift_id = jsonObject.getJSONObject("command").getJSONObject("gift").getString("gift_id");
                            String user_phone = jsonObject.getJSONObject("user").getString("user_id");
                            if (gift_id.equals(newgift_id) && user_phone.equals(newuser_phone)) {//累加第一item
                                View view = list_chat2.getChildAt(0);
                                StrokeTextView tv_count = (StrokeTextView) view.findViewById(R.id.tv_count);
                                tv_count.setText("X" + giftMap.get(giftkey).size());
                                Animation scaleAnimation = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f);
                                //设置动画时间
                                scaleAnimation.setDuration(500);
                                scaleAnimation.setRepeatCount(0);
                                scaleAnimation.setInterpolator(new CycleInterpolator(1f));
                                tv_count.startAnimation(scaleAnimation);
                                SimpleDraweeView iv_gift = (SimpleDraweeView) view.findViewById(R.id.item_live_chat_gift);
                                TranslateAnimation translateAnimation = new TranslateAnimation(0f, 0.0f, 0.1f, 0.1f);
                                translateAnimation.setDuration(0);
                                iv_gift.startAnimation(translateAnimation);
                                lonone = System.currentTimeMillis();
                                if (startTime != 0) {
                                    if ((lonone - startTime) <= secondtime) {
                                        mHandler.removeMessages(delay);
                                    }

                                }
                                mHandler.sendEmptyMessageDelayed(delay, secondtime);
                                startTime = lonone;
                            } else {
                                twotime = System.currentTimeMillis();
                                messagesgift.add(0, queue);
                                adapter_gift.notifyItemInserted(0);
                                mHandler.sendEmptyMessageDelayed(delaytwo, secondtime);
                                startTimetwo = twotime;
                            }
                        } else if (messages2.size() == 1 && messagesgift.size() == 1) {
                            JSONObject jsonObject = new JSONObject(messages2.get(0).toString());
                            String gift_id = jsonObject.getJSONObject("command").getJSONObject("gift").getString("gift_id");
                            String user_phone = jsonObject.getJSONObject("user").getString("user_id");

                            JSONObject jsonObjectGift = new JSONObject(messagesgift.get(0).toString());
                            String gift_id_two = jsonObjectGift.getJSONObject("command").getJSONObject("gift").getString("gift_id");
                            String user_phone_two = jsonObjectGift.getJSONObject("user").getString("user_id");
                            if (gift_id.equals(newgift_id) && user_phone.equals(newuser_phone)) {//累加第一item
                                View view = list_chat2.getChildAt(0);
                                StrokeTextView tv_count = (StrokeTextView) view.findViewById(R.id.tv_count);
                                tv_count.setText("X" + giftMap.get(giftkey).size());
                                Animation scaleAnimation = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f);
                                //设置动画时间
                                scaleAnimation.setDuration(500);
                                scaleAnimation.setRepeatCount(0);
                                scaleAnimation.setInterpolator(new CycleInterpolator(1f));
                                tv_count.startAnimation(scaleAnimation);
                                SimpleDraweeView iv_gift = (SimpleDraweeView) view.findViewById(R.id.item_live_chat_gift);
                                TranslateAnimation translateAnimation = new TranslateAnimation(0f, 0.0f, 0.1f, 0.1f);
                                translateAnimation.setDuration(0);
                                iv_gift.startAnimation(translateAnimation);
                                lonone = System.currentTimeMillis();
                                if (startTime != 0) {
                                    if ((lonone - startTime) <= secondtime) {
                                        mHandler.removeMessages(delay);
                                    }

                                }
                                mHandler.sendEmptyMessageDelayed(delay, secondtime);
                                startTime = lonone;
                            } else if (gift_id_two.equals(newgift_id) && user_phone_two.equals(newuser_phone)) {//添加第二条目

                                View view = act_live_rv_gift.getChildAt(0);
                                StrokeTextView tv_count = (StrokeTextView) view.findViewById(R.id.tv_count);
                                tv_count.setText("X" + giftMap.get(giftkey).size());
                                Animation scaleAnimation = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f);
                                //设置动画时间
                                scaleAnimation.setDuration(500);
                                scaleAnimation.setRepeatCount(0);
                                scaleAnimation.setInterpolator(new CycleInterpolator(1f));
                                tv_count.startAnimation(scaleAnimation);
                                SimpleDraweeView iv_gift = (SimpleDraweeView) view.findViewById(R.id.item_live_chat_gift);
                                TranslateAnimation translateAnimation = new TranslateAnimation(0f, 0.0f, 0.1f, 0.1f);
                                translateAnimation.setDuration(0);
                                iv_gift.startAnimation(translateAnimation);
                                twotime = System.currentTimeMillis();

                                if (startTimetwo != 0) {
                                    if ((twotime - startTimetwo) <= secondtime) {
                                        mHandler.removeMessages(delaytwo);
                                    }
                                }
                                mHandler.sendEmptyMessageDelayed(delaytwo, secondtime);
                                startTimetwo = twotime;
                            } else {
                                remove(0);
                                messages2.add(0, queue);
                                adapter_chat2.notifyItemInserted(0);
                                long lonone = System.currentTimeMillis();
                                mHandler.sendEmptyMessageDelayed(delay, secondtime);
                                startTime = lonone;
                            }
                        } else if (messages2.size() == 0 && messagesgift.size() == 1) {
                            JSONObject jsonObjectGift = new JSONObject(messagesgift.get(0).toString());
                            String gift_id_two = jsonObjectGift.getJSONObject("command").getJSONObject("gift").getString("gift_id");
                            String user_phone_two = jsonObjectGift.getJSONObject("user").getString("user_id");
                            if (gift_id_two.equals(newgift_id) && user_phone_two.equals(newuser_phone)) {//添加第二条目

                                View view = act_live_rv_gift.getChildAt(0);
                                StrokeTextView tv_count = (StrokeTextView) view.findViewById(R.id.tv_count);
                                tv_count.setText("X" + giftMap.get(giftkey).size());
                                Animation scaleAnimation = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f);
                                //设置动画时间
                                scaleAnimation.setDuration(500);
                                scaleAnimation.setRepeatCount(0);
                                scaleAnimation.setInterpolator(new CycleInterpolator(1f));
                                tv_count.startAnimation(scaleAnimation);
                                SimpleDraweeView iv_gift = (SimpleDraweeView) view.findViewById(R.id.item_live_chat_gift);
                                TranslateAnimation translateAnimation = new TranslateAnimation(0f, 0.0f, 0.1f, 0.1f);
                                translateAnimation.setDuration(0);
                                iv_gift.startAnimation(translateAnimation);
                                twotime = System.currentTimeMillis();

                                if (startTimetwo != 0) {
                                    if ((twotime - startTimetwo) <= secondtime) {
                                        mHandler.removeMessages(delaytwo);
                                    }
                                }
                                mHandler.sendEmptyMessageDelayed(delaytwo, secondtime);
                                startTimetwo = twotime;
                            } else {
                                messages2.add(0, queue);
                                adapter_chat2.notifyItemInserted(0);
                                long lonone = System.currentTimeMillis();
                                mHandler.sendEmptyMessageDelayed(delay, secondtime);
                                startTime = lonone;
                            }
                        }
                    }

                    //发送礼物后显示文字信息
                    JSONObject attrto = new JSONObject();

                    attrto.put("user",queue.getJSONObject("user"));
                    String gift_count = queue.getJSONObject("command").getJSONObject("gift").getString("gift_count");
                    String gift_count_unit = queue.getJSONObject("command").getJSONObject("gift").getString("gift_count_unit");
                    String name = queue.getJSONObject("command").getJSONObject("gift").getString("gift_name");
                    attrto.put("text","送了"+gift_count+gift_count_unit+name);
                    attrto.put("type",1);//礼物消息
                    messages.add(0,attrto);
                    adapter_chat.notifyItemInserted(0);
                    list_chat.scrollToPosition(0);

                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        public void remove ( int position){
            if (messages2.size() > 0) {
                messages2.remove(position);
                adapter_chat2.notifyItemRemoved(position);
            }
        }
    }
