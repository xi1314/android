package com.remair.heixiu.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.FriendMessageActivity;
import com.remair.heixiu.emoji.StringUtil;
import com.remair.heixiu.sqlite.MessageInfoDB;
import com.remair.heixiu.sqlite.MessageInfoDao;
import com.remair.heixiu.utils.CofyUtil;
import com.remair.heixiu.utils.HXImageLoader;
import com.remair.heixiu.utils.HXUtil;
import com.remair.heixiu.utils.SharedPreferenceUtil;
import com.remair.heixiu.utils.Utils;
import com.tencent.TIMMessageStatus;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import studio.archangel.toolkitv2.util.Util;
import studio.archangel.toolkitv2.util.text.Notifier;

/**
 * Created by wsk on 16/3/4.
 */
public class ChatMessageListAdapter extends BaseAdapter {

    private static String TAG = ChatMessageListAdapter.class.getSimpleName();
    private static final int ITEMCOUNT = 8;
    private final int px42;
    private final int px39;
    private List<MessageInfoDB> listMessage = null;
    private LayoutInflater inflater;
    public static final int TYPE_TEXT_SEND = 0;
    public static final int TYPE_TEXT_RECV = 1;
    public static final int TYPE_IMAGE_SEND = 2;
    public static final int TYPE_IMAGE_RECV = 3;
    public static final int TYPE_SOUND_SEND = 4;
    public static final int TYPE_SOUND_RECV = 5;
    public static final int TYPE_GIFT_SEND = 6;
    public static final int TYPE_GIFT_RECV = 7;
    public static final int TYPE_SYSTEM_FORBIT_RECV = 8;
    private Context context;
    private boolean mIsVoicePalying = false;
    private ImageView currentPalyingIV;
    private AnimationDrawable currentAnimation;
    private String user_photo;
    PopupWindow pw;
    private MediaPlayer mPlayer;


    public ChatMessageListAdapter(Context context, List<MessageInfoDB> messages, String user_photo, PopupWindow pw) {
        this.listMessage = messages;
        this.context = context;
        this.user_photo = user_photo;
        this.pw = pw;
        inflater = LayoutInflater.from(context);
        px42 = Utils.getPX(42);
        px39 = Util.getPX(39);
    }


    public int getCount() {
        if (listMessage != null) {
            return listMessage.size();
        }
        return 0;
    }


    public Object getItem(int position) {
        if (listMessage != null) {
            return listMessage.get(position);
        }
        return null;
    }


    public long getItemId(int position) {
        return position;
    }


    public int getItemViewType(int position) {
        MessageInfoDB entity = listMessage.get(position);
        if (entity.getMessagetype().equals("text")) {
            return entity.getIssend() == 0 ? TYPE_TEXT_SEND : TYPE_TEXT_RECV;
        } else if (entity.getMessagetype().equals("photo")) {
            return entity.getIssend() == 0 ? TYPE_IMAGE_SEND : TYPE_IMAGE_RECV;
        } else if (entity.getMessagetype().equals("sound")) {
            return entity.getIssend() == 0 ? TYPE_SOUND_SEND : TYPE_SOUND_RECV;
        } else if (entity.getMessagetype().equals("gift")) {
            return entity.getIssend() == 0 ? TYPE_GIFT_SEND : TYPE_GIFT_RECV;
        } else if (entity.getMessagetype().equals("systemforbit")) {
            return TYPE_SYSTEM_FORBIT_RECV;
        }
        return -1;
    }


    public int getViewTypeCount() {
        return ITEMCOUNT;
    }


    private View dynamicCreateView(int position) {
        int type = getItemViewType(position);
        switch (type) {
            case TYPE_TEXT_SEND:
                return inflater.inflate(R.layout.chat_item_text_right, null);
            case TYPE_TEXT_RECV:
                return inflater.inflate(R.layout.chat_item_text_left, null);
            case TYPE_IMAGE_SEND:
                return inflater.inflate(R.layout.chat_item_pic_right, null);
            case TYPE_IMAGE_RECV:
                return inflater.inflate(R.layout.chat_item_pic_left, null);
            case TYPE_SOUND_SEND:
                return inflater.inflate(R.layout.chat_item_sound_right, null);
            case TYPE_SOUND_RECV:
                return inflater.inflate(R.layout.chat_item_sound_left, null);
            case TYPE_GIFT_SEND:
                return inflater.inflate(R.layout.chat_item_gift_right, null);
            case TYPE_GIFT_RECV:
                return inflater.inflate(R.layout.chat_item_gift_left, null);
            case TYPE_SYSTEM_FORBIT_RECV:
                return inflater.inflate(R.layout.chat_item_gift_left, null);

            default:
                return inflater.inflate(R.layout.chat_item_text_right, null);
        }
    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        final MessageInfoDB entity = listMessage.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = dynamicCreateView(position);
            viewHolder = new ViewHolder();
            viewHolder.tv_sendtime = (TextView) convertView
                    .findViewById(R.id.tv_sendtime);
            viewHolder.iv_avatar = (SimpleDraweeView) convertView
                    .findViewById(R.id.iv_avatar);
            viewHolder.pbSending = (ProgressBar) convertView
                    .findViewById(R.id.pb_status);
            if (entity.getIssend() == 0) {
                viewHolder.iv_msg_status = (ImageView) convertView
                        .findViewById(R.id.iv_msg_status);
            }
            if (entity.getMessagetype().equals("photo")) {//图片
                viewHolder.iv_chat_item_content = (SimpleDraweeView) convertView
                        .findViewById(R.id.iv_chat_item_content);
            }
            if (entity.getMessagetype().equals("text")) {//文字
                viewHolder.tv_chatcontent = (TextView) convertView
                        .findViewById(R.id.tv_chatcontent);
            }
            if (entity.getMessagetype().equals("sound")) {//语音
                viewHolder.rl_chat_item_content = (RelativeLayout) convertView
                        .findViewById(R.id.rl_chat_item_content);
                viewHolder.iv_chat_itemsount_content = (ImageView) convertView
                        .findViewById(R.id.iv_chat_item_content);
                viewHolder.tv_total_time = (TextView) convertView
                        .findViewById(R.id.tv_total_time);
            }
            if (entity.getMessagetype().equals("gift")) {//礼物
                viewHolder.tv_giftname = (TextView) convertView
                        .findViewById(R.id.tv_giftname);
                viewHolder.tv_strokecount = (TextView) convertView
                        .findViewById(R.id.tv_strokecount);
                viewHolder.iv_gift_pic = (SimpleDraweeView) convertView
                        .findViewById(R.id.iv_gift_pic);
                viewHolder.rl_chat_gift = (RelativeLayout) convertView
                        .findViewById(R.id.rl_chat_gift);
            }
            if (entity.getMessagetype().equals("systemforbit")) {//系统封号
                viewHolder.tv_chatcontent = (TextView) convertView
                        .findViewById(R.id.tv_chatcontent);
                viewHolder.tv_forbit_timetwo = (TextView) convertView
                        .findViewById(R.id.tv_forbit_timetwo);
                viewHolder.tv_forbit_daytwo = (TextView) convertView
                        .findViewById(R.id.tv_forbit_daytwo);
                viewHolder.rl_content = (RelativeLayout) convertView
                        .findViewById(R.id.rl_content);
            }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (position == 0) {
            viewHolder.tv_sendtime
                    .setText(HXUtil.GetStringFormat(entity.getCreatetime()));
        } else {
            if (HXUtil.LongInterval(entity.getCreatetime(), listMessage
                    .get(position - 1).getCreatetime())) {
                viewHolder.tv_sendtime.setText(HXUtil
                        .GetStringFormat(entity.getCreatetime()));
                viewHolder.tv_sendtime.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tv_sendtime.setVisibility(View.GONE);
            }
        }
        if ((viewHolder.iv_msg_status != null) &&
                (entity.getSendstatue() == TIMMessageStatus.SendFail)) {
            viewHolder.iv_msg_status.setVisibility(View.VISIBLE);
        } else if (viewHolder.iv_msg_status != null) {
            viewHolder.iv_msg_status.setVisibility(View.GONE);
            viewHolder.pbSending.setVisibility(View.GONE);
        } else if ((viewHolder.iv_msg_status != null) &&
                (entity.getSendstatue() == TIMMessageStatus.Sending)) {
            viewHolder.pbSending.setVisibility(View.VISIBLE);
        }

        if (entity.getIssend() == 0) {
            HXImageLoader.loadImage(viewHolder.iv_avatar, HXApp.getInstance()
                                                               .getUserInfo().photo, px42, px42);
        } else {
            if ("".equals(user_photo)) {
                viewHolder.iv_avatar.setImageResource(R.drawable.head_bat);
            } else {
                HXImageLoader
                        .loadImage(viewHolder.iv_avatar, user_photo, px42, px42);
            }
        }
        viewHolder.iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (entity.getIssend() == 0) {
                    Intent it = new Intent(context, FriendMessageActivity.class);
                    it.putExtra("viewed_user_id", HXApp.getInstance()
                                                       .getUserInfo().user_id);
                    context.startActivity(it);
                } else {
                    Intent it = new Intent(context, FriendMessageActivity.class);
                    it.putExtra("viewed_user_id", Integer
                            .parseInt(entity.getUserid()));
                    context.startActivity(it);
                }
            }
        });
        if (entity.getMessagetype().equals("text")) {//文字
            final SpannableString spanString = StringUtil
                    .stringToSpannableString(entity.getMessage(), context);
            viewHolder.tv_chatcontent.setText(spanString);
            viewHolder.tv_chatcontent.setTag(viewHolder.tv_chatcontent);
            final TextView final_openLight = viewHolder.tv_chatcontent;
            viewHolder.tv_chatcontent
                    .setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            showPopupWindow((TextView) final_openLight
                                    .getTag(), entity, spanString.toString());
                            return true;
                        }
                    });
        } else if (entity.getMessagetype().equals("photo")) {

            HXImageLoader.loadImage(viewHolder.iv_chat_item_content, entity
                    .getMessage(), Util.getPX(150), Util.getPX(200));

            final View final_openLight = viewHolder.iv_chat_item_content;
            viewHolder.iv_chat_item_content
                    .setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            showPopupWindow(final_openLight, entity, "");
                            return true;
                        }
                    });
        } else if (entity.getMessagetype().equals("sound")) {
            if (entity.getIssend() == 0) {
                viewHolder.iv_chat_itemsount_content
                        .setImageResource(R.drawable.soundself3);
            } else {
                viewHolder.iv_chat_itemsount_content
                        .setImageResource(R.drawable.sounds3);
            }
            long longtime = entity.getTime();
            long minitetime = longtime / 60;
            long miaotime = longtime % 60;
            if (minitetime > 0) {
                viewHolder.tv_total_time
                        .setText(minitetime + "‘" + miaotime + "\"");
            } else {
                viewHolder.tv_total_time.setText(miaotime + "\"");
            }
            final ImageView im = viewHolder.iv_chat_itemsount_content;
            viewHolder.rl_chat_item_content
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                if (currentPalyingIV == im && mIsVoicePalying) {
                                    stopCurrentPttMedia(
                                            entity.getIssend() == 0);
                                    return;
                                }
                                mPlayer = new MediaPlayer();
                                mPlayer.setDataSource(entity.getMessage());
                                mPlayer.prepare();
                                mPlayer.start();
                                mIsVoicePalying = true;

                                int animationResId;
                                if (entity.getIssend() == 0) {
                                    animationResId = R.drawable.anim_sound_self;
                                } else {
                                    animationResId = R.drawable.anim_sound_user;
                                }
                                final AnimationDrawable animateDrawable = (AnimationDrawable) context
                                        .getResources()
                                        .getDrawable(animationResId);
                                currentPalyingIV = im;
                                currentAnimation = animateDrawable;

                                im.setImageDrawable(animateDrawable);
                                currentAnimation.start();
                                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        // TODO Auto-generated method stub
                                        mIsVoicePalying = false;
                                        if (mPlayer != null) {
                                            mPlayer.release();
                                            mPlayer = null;
                                        }
                                        //	animateDrawable.stop();
                                        currentAnimation.stop();
                                        if (entity.getIssend() == 0) {
                                            currentPalyingIV
                                                    .setImageResource(R.drawable.soundself3);
                                        } else {
                                            currentPalyingIV
                                                    .setImageResource(R.drawable.sounds3);
                                        }
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            viewHolder.rl_chat_item_content
                    .setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            showPopupWindow(im, entity, "");
                            return true;
                        }
                    });
        } else if (entity.getMessagetype().equals("gift")) {
            try {
                String message = entity.getMessage();
                JSONObject jsonObject = new JSONObject(message);
                JSONObject giftJson = jsonObject.optJSONObject("giftElem");
                String name = giftJson.optString("name");
                String gift_id = giftJson.optString("gift_id");
                int price = giftJson.optInt("price");
                int type = giftJson.optInt("type");
                String gift_count_unit = giftJson.optString("unit_name");
                String select_animation_image = giftJson
                        .optString("select_animation_image");
                int giftCount = jsonObject.optInt("giftCount");
                int giftReward = jsonObject.optInt("giftReward");

                final String gifturll = SharedPreferenceUtil
                        .getString(gift_id, "");
                if (null != gifturll && !"".equals(gifturll)) {
                    DraweeController controller = Fresco
                            .newDraweeControllerBuilder()
                            .setUri("file://" + gifturll)
                            .setAutoPlayAnimations(true).build();
                    viewHolder.iv_gift_pic.setController(controller);
                } else {

                    HXImageLoader
                            .loadImage(viewHolder.iv_gift_pic, select_animation_image, px39, px39);
                }
                viewHolder.tv_giftname
                        .setText("送了" + giftCount + gift_count_unit + name);
                if (type != 2) {
                    if (entity.getIssend() == 0) {
                        viewHolder.tv_strokecount
                                .setText("你的经验值+" + price * 10 * giftCount);
                    } else {
                        viewHolder.tv_strokecount
                                .setText("你的存在感+" + price * 10 * giftCount);
                    }
                } else {
                    if (entity.getIssend() == 0) {
                        if (giftReward > 0) {
                            viewHolder.tv_strokecount.setText(
                                    "你发送了" + price * giftCount + "个嘿豆,中了" +
                                            giftReward + "倍大奖");
                        } else {
                            viewHolder.tv_strokecount.setText("");
                        }
                    } else {
                        viewHolder.tv_strokecount.setText(
                                "你的嘿豆增加+" + (int) (price * giftCount * 0.2));
                    }
                }
                final View im = viewHolder.rl_chat_gift;
                viewHolder.rl_chat_gift
                        .setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                showPopupWindow(im, entity, "");
                                return true;
                            }
                        });
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //
        }else if (entity.getMessagetype().equals("systemforbit")){//系统封号
            String content=entity.getMessage();
            try {
                JSONObject jsonObject= new JSONObject(content);

                viewHolder.tv_chatcontent.setText(jsonObject.optString("forbid_reason"));
                viewHolder.tv_forbit_timetwo.setText(jsonObject.optString("forbid_time"));
                viewHolder.tv_forbit_daytwo.setText(jsonObject.optString("forbid_penalize"));
                final View im = viewHolder.rl_content;
                viewHolder.rl_content
                        .setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                showPopupWindow(im, entity, "");
                                return true;
                            }
                        });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return convertView;
    }


    static class ViewHolder {
        public SimpleDraweeView iv_avatar;//左边
        public SimpleDraweeView iv_chat_item_content;//图片
        public TextView tv_chatcontent;
        public TextView tv_sendtime;
        public ImageView iv_msg_status;//右边
        public RelativeLayout rl_chat_item_content;
        public ImageView iv_chat_itemsount_content;
        public TextView tv_total_time;
        public TextView tv_giftname;
        public TextView tv_strokecount;
        public TextView tv_forbit_timetwo;
        public TextView tv_forbit_daytwo;
        public SimpleDraweeView iv_gift_pic;

        public ProgressBar pbSending;
        public RelativeLayout rl_chat_gift;
        public RelativeLayout rl_content;
    }


    private void stopCurrentPttMedia(boolean bSelf) {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
        currentAnimation.stop();
        if (bSelf) {
            currentPalyingIV.setImageResource(R.drawable.soundself3);
        } else {
            currentPalyingIV.setImageResource(R.drawable.sounds3);
        }
        mIsVoicePalying = false;
    }


    private void showPopupWindow(View v, final MessageInfoDB entity, final String spanString) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.chat_popupwindow, null);
        TextView tv_chat_copy = (TextView) view.findViewById(R.id.tv_chat_copy);
        TextView tv_chat_delete = (TextView) view
                .findViewById(R.id.tv_chat_delete);

        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        pw = new PopupWindow(view, Util.getPX(100), Util.getPX(40));
        //        WindowManager.LayoutParams params = context.getWindow().getAttributes();
        //        params.alpha = 0.7f;
        if (spanString.equals("")) {
            tv_chat_copy.setVisibility(View.GONE);
        } else {
            tv_chat_copy.setVisibility(View.VISIBLE);
        }
        tv_chat_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CofyUtil.copy(spanString, context);
                Notifier.showShortMsg(context, "已复制到剪切板");
                pw.dismiss();
            }
        });
        tv_chat_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    listMessage.remove(entity);
                    //                    entity.getTimMessage().remove();
                    MessageInfoDao messageInfoDao = new MessageInfoDao(context);
                    messageInfoDao.deleteMessage(entity.getUuid());
                    notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                pw.dismiss();
            }
        });
        // 点击外部可以被关闭
        pw.setOutsideTouchable(true);
        pw.setBackgroundDrawable(new BitmapDrawable());

        pw.setFocusable(true);// 设置PopupWindow允许获得焦点, 默认情况下popupWindow中的控件不可以获得焦点, 例外: Button, ImageButton..
        int px20 = Util.getPX(20);
        int px82 = Util.getPX(82);
        if (entity.getIssend() == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                pw.showAsDropDown(v, -px20, -px82, Gravity.BOTTOM);
            } else {
                pw.showAsDropDown(v, -px20, -px82);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                pw.showAsDropDown(v, px20, -px82, Gravity.BOTTOM);
            } else {
                pw.showAsDropDown(v, px20, -px82);
            }
        }
    }
}