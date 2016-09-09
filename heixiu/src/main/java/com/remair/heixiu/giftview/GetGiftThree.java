package com.remair.heixiu.giftview;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.badoo.mobile.util.WeakHandler;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.R;
import com.remair.heixiu.adapters.LiveChatMessageAdapter;
import com.remair.heixiu.utils.SharedPreferenceUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wsk on 16/5/10.
 */
public class GetGiftThree {
    private Context context;
    private TextView meilizhi_textview;
    private WeakHandler mHandler;
    //    private ImageView giftbig_fl;
    private SimpleDraweeView giftbig_fl;
    private RecyclerView list_chat, list_chat2, act_live_rv_gift;
    private ArrayList<JSONObject> messages2, messages, messagesgift;
    private LiveChatMessageAdapter adapter_chat, adapter_chat2, adapter_gift;
    private Map<String, List<String>> giftMap;
    private Queue<JSONObject> queuebig;
    private long startTime = 0;
    private long startTimetwo = 0;
    private long lonone, twotime;
    private final int delay = 301;//时间纪录
    private final int delaytwo = 302;//时间纪录
    private final int delayGIF = 303;//GIF
    private final int delaywin = 305;//中奖
    private long secondtime = 4000;//礼物显示几秒
    private static final int chatmessage = 250;
    private List<String> temp = null;


    public GetGiftThree(Context context, WeakHandler mHandler, TextView meilizhi_textview, SimpleDraweeView giftbig_fl, RecyclerView list_chat, RecyclerView list_chat2, RecyclerView act_live_rv_gift, ArrayList<JSONObject> messages2, ArrayList<JSONObject> messages, ArrayList<JSONObject> messagesgift, LiveChatMessageAdapter adapter_chat, LiveChatMessageAdapter adapter_chat2, LiveChatMessageAdapter adapter_gift, Map<String, List<String>> giftMap, Queue<JSONObject> queuebig) {
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
        this.queuebig = queuebig;
    }


    //itm礼物接受
    public synchronized void addMessageToList1(JSONObject queue, Queue<JSONObject> queuebig) {
        try {
            if (queue == null) {
                return;
            }
            String newgift_id = queue.optJSONObject("command")
                                     .optJSONObject("gift")
                                     .optString("gift_id");
            String newuser_phone = queue.optJSONObject("user")
                                        .optString("user_id");
            final String gif_image = queue.optJSONObject("command")
                                          .optJSONObject("gift")
                                          .optString("gif_image");
            int isGif = queue.optJSONObject("command").optJSONObject("gift")
                             .optInt("isGif");
            int searilNum = queue.optJSONObject("command").optJSONObject("gift")
                                 .optInt("searilNum");
            final Double duration = queue.optJSONObject("command")
                                         .optJSONObject("gift")
                                         .optDouble("duration");
            int charm_value = queue.optJSONObject("command")
                                   .optInt("charm_value");
            final int winning_rate = queue.optJSONObject("command")
                                          .optInt("winning_rate");
            int giftrices = queue.optJSONObject("command").optJSONObject("gift")
                                 .optInt("gift_price");
            int gift_type = queue.optJSONObject("command").optJSONObject("gift")
                                 .optInt("gift_type");//类型
            int gift_count = queue.optJSONObject("command")
                                  .optJSONObject("gift")
                                  .optInt("gift_count");//个数
            int gift_once_count = queue.optJSONObject("command")
                                       .optJSONObject("gift")
                                       .optInt("gift_once_count");//个数
            if (gift_once_count == 0) {
                gift_once_count = 1;
            }
            if (gift_type != 2) {
                int giftId = Integer.parseInt(newgift_id);
                if (giftId >= 150 && giftId < 200) {
                } else {
                    meilizhi_textview.setText(
                            (Integer.parseInt(meilizhi_textview.getText()
                                                               .toString()) +
                                     giftrices * gift_once_count * 10) + "");
                }
            }

            if (isGif == 1) {
                giftbig_fl.setVisibility(View.VISIBLE);
                final String gifturlloat = SharedPreferenceUtil
                        .getString(newgift_id, "");
                if (null != gifturlloat && !"".equals(gifturlloat)) {
                    DraweeController controller = Fresco
                            .newDraweeControllerBuilder()
                            .setUri("file://" + gifturlloat)
                            .setAutoPlayAnimations(true).build();
                    giftbig_fl.setController(controller);
                    //                    Glide.with(context).load("file://"+gifturlloat).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(giftbig_fl);
                    Message message = new Message();
                    message.obj = "file://" + gifturlloat;
                    message.what = delayGIF;
                    mHandler.sendMessageDelayed(message, new Double(
                            duration * 1000).longValue());
                    //                    mHandler.sendMessageDelayed(delayGIF, new Double(duration * 1000).longValue());
                } else {
                    Uri uri = Uri.parse(gif_image);
                    ControllerListener listener = new BaseControllerListener() {
                        @Override
                        public void onFinalImageSet(String id, Object imageInfo, Animatable animatable) {
                            super.onFinalImageSet(id, imageInfo, animatable);
                            Message message = new Message();
                            message.obj = gif_image;
                            message.what = delayGIF;
                            mHandler.sendMessageDelayed(message, new Double(
                                    duration * 1000).longValue());
                            //                            mHandler.sendEmptyMessageDelayed(delayGIF, new Double(duration * 1000).longValue());
                        }


                        @Override
                        public void onFailure(String id, Throwable throwable) {
                            super.onFailure(id, throwable);
                        }


                        @Override
                        public void onIntermediateImageFailed(String id, Throwable throwable) {
                            super.onIntermediateImageFailed(id, throwable);
                        }
                    };
                    DraweeController controller = Fresco
                            .newDraweeControllerBuilder().setUri(uri)
                            .setAutoPlayAnimations(true)
                            .setControllerListener(listener).build();
                    giftbig_fl.setController(controller);
                }
            } else {
                if (queuebig.size() == 0) {
                    RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
                    itemAnimator.setAddDuration(200);
                    itemAnimator.setMoveDuration(200);
                    list_chat2.setItemAnimator(itemAnimator);

                    if (messages2.size() == 0 && messagesgift.size() == 0) {

                        messages2.add(0, queue);
                        adapter_chat2.notifyItemInserted(0);
                        long lonone = System.currentTimeMillis();
                        mHandler.sendEmptyMessageDelayed(delay, secondtime);
                        startTime = lonone;
                    } else if (messages2.size() == 1 &&
                            messagesgift.size() == 0) {
                        JSONObject jsonObject = new JSONObject(messages2.get(0)
                                                                        .toString());
                        String gift_id = jsonObject.getJSONObject("command")
                                                   .getJSONObject("gift")
                                                   .getString("gift_id");
                        String user_phone = jsonObject.getJSONObject("user")
                                                      .getString("user_id");
                        if (gift_id.equals(newgift_id) &&
                                user_phone.equals(newuser_phone)) {//累加第一item
                            additem(searilNum, gift_once_count, list_chat2, winning_rate);
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
                    } else if (messages2.size() == 1 &&
                            messagesgift.size() == 1) {
                        JSONObject jsonObject = new JSONObject(messages2.get(0)
                                                                        .toString());
                        String gift_id = jsonObject.getJSONObject("command")
                                                   .getJSONObject("gift")
                                                   .getString("gift_id");
                        String user_phone = jsonObject.getJSONObject("user")
                                                      .getString("user_id");

                        JSONObject jsonObjectGift = new JSONObject(messagesgift
                                .get(0).toString());
                        String gift_id_two = jsonObjectGift
                                .getJSONObject("command").getJSONObject("gift")
                                .getString("gift_id");
                        String user_phone_two = jsonObjectGift
                                .getJSONObject("user").getString("user_id");
                        if (gift_id.equals(newgift_id) &&
                                user_phone.equals(newuser_phone)) {//累加第一item
                            additem(searilNum, gift_once_count, list_chat2, winning_rate);
                            lonone = System.currentTimeMillis();
                            if (startTime != 0) {
                                if ((lonone - startTime) <= secondtime) {
                                    mHandler.removeMessages(delay);
                                }
                            }
                            mHandler.sendEmptyMessageDelayed(delay, secondtime);
                            startTime = lonone;
                        } else if (gift_id_two.equals(newgift_id) &&
                                user_phone_two.equals(newuser_phone)) {//添加第二条目
                            additem(searilNum, gift_once_count, act_live_rv_gift, winning_rate);
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
                    } else if (messages2.size() == 0 &&
                            messagesgift.size() == 1) {
                        JSONObject jsonObjectGift = new JSONObject(messagesgift
                                .get(0).toString());
                        String gift_id_two = jsonObjectGift
                                .getJSONObject("command").getJSONObject("gift")
                                .getString("gift_id");
                        String user_phone_two = jsonObjectGift
                                .getJSONObject("user").getString("user_id");
                        if (gift_id_two.equals(newgift_id) &&
                                user_phone_two.equals(newuser_phone)) {//添加第二条目
                            additem(searilNum, gift_once_count, act_live_rv_gift, winning_rate);

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
                } else {//大礼物时
                    RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
                    itemAnimator.setAddDuration(200);
                    itemAnimator.setMoveDuration(200);
                    list_chat2.setItemAnimator(itemAnimator);
                    int giftrice = queue.getJSONObject("command")
                                        .getJSONObject("gift")
                                        .getInt("gift_price");
                    if (giftrice < 10) {
                        if (messages2.size() == 0 && messagesgift.size() == 0) {
                            messages2.add(0, queue);
                            adapter_chat2.notifyItemInserted(0);
                            long lonone = System.currentTimeMillis();
                            mHandler.sendEmptyMessageDelayed(delay, secondtime);
                            startTime = lonone;
                        } else if (messages2.size() == 1 &&
                                messagesgift.size() == 0) {
                            JSONObject jsonObject = new JSONObject(messages2
                                    .get(0).toString());
                            String gift_id = jsonObject.getJSONObject("command")
                                                       .getJSONObject("gift")
                                                       .getString("gift_id");
                            String user_phone = jsonObject.getJSONObject("user")
                                                          .getString("user_id");
                            if (gift_id.equals(newgift_id) && user_phone
                                    .equals(newuser_phone)) {//累加第一item
                                additem(searilNum, gift_once_count, list_chat2, winning_rate);
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
                        } else if (messages2.size() == 1 &&
                                messagesgift.size() == 1) {
                            JSONObject jsonObject = new JSONObject(messages2
                                    .get(0).toString());
                            String gift_id = jsonObject.getJSONObject("command")
                                                       .getJSONObject("gift")
                                                       .getString("gift_id");
                            String user_phone = jsonObject.getJSONObject("user")
                                                          .getString("user_id");

                            JSONObject jsonObjectGift = new JSONObject(messagesgift
                                    .get(0).toString());
                            String gift_id_two = jsonObjectGift
                                    .getJSONObject("command")
                                    .getJSONObject("gift").getString("gift_id");
                            String user_phone_two = jsonObjectGift
                                    .getJSONObject("user").getString("user_id");
                            if (gift_id.equals(newgift_id) && user_phone
                                    .equals(newuser_phone)) {//累加第一item
                                additem(searilNum, gift_once_count, list_chat2, winning_rate);
                                lonone = System.currentTimeMillis();
                                if (startTime != 0) {
                                    if ((lonone - startTime) <= secondtime) {
                                        mHandler.removeMessages(delay);
                                    }
                                }
                                mHandler.sendEmptyMessageDelayed(delay, secondtime);
                                startTime = lonone;
                            } else if (gift_id_two.equals(newgift_id) &&
                                    user_phone_two
                                            .equals(newuser_phone)) {//添加第二条目
                                additem(searilNum, gift_once_count, act_live_rv_gift, winning_rate);
                                twotime = System.currentTimeMillis();

                                if (startTimetwo != 0) {
                                    if ((twotime - startTimetwo) <=
                                            secondtime) {
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
                        } else if (messages2.size() == 0 &&
                                messagesgift.size() == 1) {
                            JSONObject jsonObjectGift = new JSONObject(messagesgift
                                    .get(0).toString());
                            String gift_id_two = jsonObjectGift
                                    .getJSONObject("command")
                                    .getJSONObject("gift").getString("gift_id");
                            String user_phone_two = jsonObjectGift
                                    .getJSONObject("user").getString("user_id");
                            if (gift_id_two.equals(newgift_id) && user_phone_two
                                    .equals(newuser_phone)) {//添加第二条目
                                additem(searilNum, gift_once_count, act_live_rv_gift, winning_rate);
                                twotime = System.currentTimeMillis();
                                if (startTimetwo != 0) {
                                    if ((twotime - startTimetwo) <=
                                            secondtime) {
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
                    } else {//大礼物显示
                        if (messagesgift.size() == 0) {
                            messagesgift.add(0, queue);
                            adapter_gift.notifyItemInserted(0);
                            twotime = System.currentTimeMillis();
                            mHandler.sendEmptyMessageDelayed(delaytwo, secondtime);
                            startTimetwo = twotime;
                        } else if (messagesgift.size() == 1 &&
                                messages2.size() == 0) {
                            JSONObject jsonObjectGift = new JSONObject(messagesgift
                                    .get(0).toString());
                            String gift_id_two = jsonObjectGift
                                    .getJSONObject("command")
                                    .getJSONObject("gift").getString("gift_id");
                            String user_phone_two = jsonObjectGift
                                    .getJSONObject("user").getString("user_id");
                            if (gift_id_two.equals(newgift_id) &&
                                    user_phone_two.equals(newuser_phone)) {
                                additem(searilNum, gift_once_count, act_live_rv_gift, winning_rate);
                                twotime = System.currentTimeMillis();

                                if (startTimetwo != 0) {
                                    if ((twotime - startTimetwo) <=
                                            secondtime) {
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
                        } else if (messages2.size() == 1 &&
                                messagesgift.size() == 1) {
                            JSONObject jsonObject = new JSONObject(messages2
                                    .get(0).toString());
                            String gift_id = jsonObject.getJSONObject("command")
                                                       .getJSONObject("gift")
                                                       .getString("gift_id");
                            String user_phone = jsonObject.getJSONObject("user")
                                                          .getString("user_id");

                            JSONObject jsonObjectGift = new JSONObject(messagesgift
                                    .get(0).toString());
                            String gift_id_two = jsonObjectGift
                                    .getJSONObject("command")
                                    .getJSONObject("gift").getString("gift_id");
                            String user_phone_two = jsonObjectGift
                                    .getJSONObject("user").getString("user_id");
                            if (gift_id.equals(newgift_id) && user_phone
                                    .equals(newuser_phone)) {//累加第一item
                                additem(searilNum, gift_once_count, list_chat2, winning_rate);
                                lonone = System.currentTimeMillis();
                                if (startTime != 0) {
                                    if ((lonone - startTime) <= secondtime) {
                                        mHandler.removeMessages(delay);
                                    }
                                }
                                mHandler.sendEmptyMessageDelayed(delay, secondtime);
                                startTime = lonone;
                            } else if (gift_id_two.equals(newgift_id) &&
                                    user_phone_two
                                            .equals(newuser_phone)) {//添加第二条目
                                additem(searilNum, gift_once_count, act_live_rv_gift, winning_rate);
                                twotime = System.currentTimeMillis();

                                if (startTimetwo != 0) {
                                    if ((twotime - startTimetwo) <=
                                            secondtime) {
                                        mHandler.removeMessages(delaytwo);
                                    }
                                }
                                mHandler.sendEmptyMessageDelayed(delaytwo, secondtime);
                                startTimetwo = twotime;
                            } else {
                                removeone(0);
                                twotime = System.currentTimeMillis();
                                messagesgift.add(0, queue);
                                adapter_gift.notifyItemInserted(0);
                                mHandler.sendEmptyMessageDelayed(delaytwo, secondtime);
                                startTimetwo = twotime;
                            }
                        }
                    }
                }
            }

            //            int winning_rate = queue.getJSONObject("command").optInt("winning_rate");//中奖倍数
            if (gift_type == 2 && winning_rate >= HXApp.getInstance().wining) {
                Message message = new Message();
                message.what = delaywin;
                message.arg1 = winning_rate;
                mHandler.sendMessage(message);
            }

            //发送礼物后显示文字信息
            JSONObject attrto = new JSONObject();
            if (gift_type == 2) {
                //                if (HXApp.getInstance().getUserInfo().isCreater()){
                String gift_count_unit = queue.getJSONObject("command")
                                              .getJSONObject("gift")
                                              .getString("gift_count_unit");
                String name = queue.getJSONObject("command")
                                   .getJSONObject("gift")
                                   .getString("gift_name");
                //                    attrto.put("text", "送给您" + gift_count + gift_count_unit + name);
                //                    attrto.put("type", 1);//礼物消息
                //                    messages.add(0, attrto);
                //                    adapter_chat.notifyItemInserted(0);
                //                    list_chat.scrollToPosition(0);
                attrto.put("user", queue.getJSONObject("user"));
                attrto.put("text", "送给您" + gift_count + gift_count_unit + name);
                //                    attrto.put("text", "送给您" + gift_once_count*giftrices+"个嘿豆");
                attrto.put("type", 1);//礼物消息
                messages.add(0, attrto);
                mHandler.sendEmptyMessage(chatmessage);
                //adapter_chat.notifyItemInserted(0);
                //list_chat.scrollToPosition(0);
                //                }
            } else {
                attrto.put("user", queue.getJSONObject("user"));
                String gift_count_unit = queue.optJSONObject("command")
                                              .optJSONObject("gift")
                                              .optString("gift_count_unit");
                String name = queue.optJSONObject("command")
                                   .optJSONObject("gift")
                                   .optString("gift_name");
                attrto.put("text",
                        "送了" + gift_once_count + gift_count_unit + name);
                attrto.put("type", 1);//礼物消息
                messages.add(0, attrto);
                mHandler.sendEmptyMessage(chatmessage);
                //adapter_chat.notifyItemInserted(0);
                //list_chat.scrollToPosition(0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void additem(int searilNum, int gift_once_count, RecyclerView recyclerView, int winning_rate) {
        if (null == recyclerView.findViewHolderForAdapterPosition(0)) {
            return;
        }
        View view = recyclerView.findViewHolderForAdapterPosition(0).itemView;
        if (null == view) {
            return;
        }
        StrokeTextView tv_count = (StrokeTextView) view
                .findViewById(R.id.tv_count);
        FrameLayout fl_win_small = (FrameLayout) view
                .findViewById(R.id.fl_win_small);
        TextView tv_winsmall = (TextView) view.findViewById(R.id.tv_winsmall);
        if (gift_once_count > 1) {
            tv_count.setText("X" + searilNum);
        } else {
            tv_count.setText("X" + searilNum);
        }
        Animation scaleAnimation = new ScaleAnimation(2.0f, 1f, 2.0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        //设置动画时间
        scaleAnimation.setDuration(300);
        scaleAnimation.setRepeatCount(0);
        //        scaleAnimation.setInterpolator(new AccelerateInterpolator((float) 0.2));
        tv_count.startAnimation(scaleAnimation);
        ImageView iv_gift = (ImageView) view
                .findViewById(R.id.item_live_chat_gift);
        TranslateAnimation translateAnimation = new TranslateAnimation(0f, 0.0f, 0.1f, 0.1f);
        translateAnimation.setDuration(0);
        iv_gift.startAnimation(translateAnimation);
        if (winning_rate > 0 && winning_rate < HXApp.getInstance().wining) {
            fl_win_small.setVisibility(View.VISIBLE);
            tv_winsmall.setText("恭喜获得" + winning_rate + "倍大奖");
            ScaleAnimation animation = new ScaleAnimation(1.4f, 1.0f, 1.4f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(200);
            tv_winsmall.setAnimation(animation);
        } else {
            fl_win_small.setVisibility(View.INVISIBLE);
        }
    }


    public void remove(int position) {
        if (messages2.size() > 0) {
            messages2.remove(position);
            adapter_chat2.notifyItemRemoved(position);
        }
    }


    public void removeone(int position) {
        if (messagesgift.size() > 0) {
            messagesgift.remove(position);
            adapter_gift.notifyItemRemoved(position);
        }
    }
}
