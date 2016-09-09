package com.remair.heixiu.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.CycleInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.R;
import com.remair.heixiu.emoji.StringUtil;
import com.remair.heixiu.giftview.StrokeTextView;
import com.remair.heixiu.utils.HXImageLoader;
import com.remair.heixiu.utils.SharedPreferenceUtil;
import com.remair.heixiu.utils.Utils;
import com.remair.heixiu.utils.Xtgrade;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import studio.archangel.toolkitv2.adapters.CommonRecyclerAdapter;
import studio.archangel.toolkitv2.util.Logger;
import studio.archangel.toolkitv2.viewholders.AngelCommonViewHolder;

/**
 * Created by Michael
 * adapter for chat message model
 */
public class LiveChatMessageAdapter extends CommonRecyclerAdapter {

    public static final int TYPE_ITEM_TEXT = 1;
    public static final int TYPE_ITEM_ROOM = 2;
    public static final int TYPE_ITEM_GIFT = 3;
    public static final int TYPE_ITEM_ERROR = 4;
    private final int px40;
    private final int px65;


    public LiveChatMessageAdapter(Context c, List i) {
        super(c, new int[] { R.layout.item_live_chat_text,
                R.layout.item_live_chat_gift }, null, i, true);
        px40 = Utils.getPX(40);
        px65 = Utils.getPX(65);
    }


    @Override
    protected RecyclerView.ViewHolder onCreateHeaderVH(View v) {
        Logger.out("onCreateHeaderVH");
        return null;
    }


    @Override
    protected RecyclerView.ViewHolder onCreateItemVH(View v) {
        Logger.out("onCreateItemVH");
        return new TextItemViewHolder(v);
    }


    @Override
    public int getItemViewType(int position) {
        Logger.out("getItemViewType");
        if (isHeader(position)) {
            return TYPE_HEADER;
        } else {
            JSONObject attr = (JSONObject) items
                    .get(position - (hasHeader() ? 1 : 0));
            try {
                Logger.out(attr.toString(4));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (attr.has("text")) {
                return TYPE_ITEM_TEXT;
            } else if (attr.has("command")) {
                JSONObject command = attr.optJSONObject("command");
                String action = command.optString("name");
                if (action.equalsIgnoreCase("gift_send")) {
                    return TYPE_ITEM_GIFT;
                } else if (action.equalsIgnoreCase("room_enter") ||
                        action.equalsIgnoreCase("room_exit")) {
                    return TYPE_ITEM_ROOM;
                }
            }
        }
        return TYPE_ITEM_ERROR;
    }


    @Override
    protected RecyclerView.ViewHolder onCreateTypedItemVH(ViewGroup parent, int type) throws Exception {
        Logger.out("onCreateTypedItemVH");
        View view = null;
        switch (type) {
            case TYPE_ITEM_TEXT: {
                view = LayoutInflater.from(parent.getContext())
                                     .inflate(item_layout[0], parent, false);
                return new TextItemViewHolder(view);
            }

            case TYPE_ITEM_GIFT: {
                view = LayoutInflater.from(parent.getContext())
                                     .inflate(item_layout[1], parent, false);
                return new GiftItemViewHolder(view);
            }
            case TYPE_ITEM_ERROR:
            default:
                return new ErrorViewHolder(new View(parent.getContext()));
        }
    }


    public class TextItemViewHolder extends AngelCommonViewHolder {
        @BindView(R.id.item_live_chat_text) TextView tv_content;

        @BindView(R.id.item_live_chat_text1) TextView tv_content1;
        @BindView(R.id.tv_grade) TextView tv_grade;
        @BindView(R.id.iv_grade) SimpleDraweeView iv_grade;
        @BindView(R.id.el_grade) RelativeLayout el_grade;


        public TextItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }


        @Override
        public void render(final Context context, Object model) {
            final JSONObject attr = (JSONObject) model;
            JSONObject user = attr.optJSONObject("user");
            String name = user.optString("user_name");
            String text = attr.optString("text");
            int type = attr.optInt("type");

            switch (type) {
                case 0://聊天消息
                    SpannableString spanString = StringUtil
                            .stringToSpannableString(
                                    name + ":" + text, context);
                    ForegroundColorSpan span = new ForegroundColorSpan(Color
                            .parseColor("#ffffff"));
                    // SpannableString spannableString =new SpannableString();
                    spanString.setSpan(span, name.length(), spanString
                            .length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ForegroundColorSpan span1 = new ForegroundColorSpan(Color
                            .parseColor("#fc7572"));
                    spanString.setSpan(span1, 0, name
                            .length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    BackgroundColorSpan span3 = new BackgroundColorSpan(Color
                            .parseColor("#1c000000"));
                    spanString.setSpan(span3, 0, spanString
                            .length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    //  }
                    spanString
                            .setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, spanString
                                    .length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //粗体

                    tv_content.setText(spanString);
                    //  tv_content.append(spanString);
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tv_content
                            .getLayoutParams();
                    layoutParams.leftMargin = 5;
                    tv_content.requestLayout();
                    el_grade.setVisibility(View.VISIBLE);
                    tv_grade.setVisibility(View.VISIBLE);
                    iv_grade.setVisibility(View.VISIBLE);
                    Xtgrade.mChatMessagegrade(context, user
                            .optInt("grade"), iv_grade, tv_grade);
                    //if (HXApp.getInstance().getUserInfo().isCreater){
                    //    tv_content1.setText(R.string.anchor);
                    //}else{
                    //    tv_content1.setText(R.string.spectator);
                    //}
                    break;
                case 1://礼物消息
                    SpannableString msg = new SpannableString(
                            name + " " + text);
                    ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color
                            .parseColor("#ef51b0"));
                    msg.setSpan(foregroundColorSpan, 0, msg
                            .length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv_content.setText(msg);
                    RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) tv_content
                            .getLayoutParams();
                    layoutParams1.leftMargin = 5;
                    tv_content.requestLayout();
                    el_grade.setVisibility(View.VISIBLE);
                    tv_grade.setVisibility(View.VISIBLE);
                    iv_grade.setVisibility(View.VISIBLE);
                    Xtgrade.mChatMessagegrade(context, user
                            .optInt("grade"), iv_grade, tv_grade);
                    //tv_content1.setVisibility(View.VISIBLE);
                    //if (HXApp.getInstance().getUserInfo().isCreater){
                    //    tv_content1.setText(R.string.anchor);
                    //}else{
                    //    tv_content1.setText(R.string.spectator);
                    //}
                    break;
                case 2://分享消息
                case 3://关注信息
                    SpannableString msg1 = new SpannableString(text);
                    ForegroundColorSpan foregroundColorSpan1 = new ForegroundColorSpan(Color
                            .parseColor("#cca100"));
                    msg1.setSpan(foregroundColorSpan1, 0, msg1
                            .length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv_content.setText(msg1);
                    RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) tv_content
                            .getLayoutParams();
                    layoutParams2.leftMargin = 0;
                    tv_content.requestLayout();
                    tv_grade.setVisibility(View.GONE);
                    iv_grade.setVisibility(View.GONE);
                    el_grade.setVisibility(View.GONE);
                    tv_content1.setVisibility(View.GONE);
                    break;
                case 4://中奖消息
                    SpannableString msSg = new SpannableString(
                            name + " " + text);
                    ForegroundColorSpan foregroundColorSpanTO = new ForegroundColorSpan(Color
                            .parseColor("#ac004d"));
                    msSg.setSpan(foregroundColorSpanTO, 0, msSg
                            .length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv_content.setText(msSg);
                    RelativeLayout.LayoutParams layoutParamsTo = (RelativeLayout.LayoutParams) tv_content
                            .getLayoutParams();
                    layoutParamsTo.leftMargin = 5;
                    tv_content.requestLayout();
                    el_grade.setVisibility(View.VISIBLE);
                    tv_grade.setVisibility(View.GONE);
                    iv_grade.setVisibility(View.VISIBLE);
                    iv_grade.setImageResource(R.drawable.win_chat);
                    //                    Xtgrade.mXtgrade(user.optInt("grade"), iv_grade, tv_grade);
                    tv_content1.setVisibility(View.GONE);
                    break;
                case 5://官方
                    SpannableString message = new SpannableString(text);
                    ForegroundColorSpan foregroundColorSpanguan = new ForegroundColorSpan(Color
                            .parseColor("#FFD350"));
                    message.setSpan(foregroundColorSpanguan, 0, message
                            .length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv_content.setText(message);
                    RelativeLayout.LayoutParams layoutParamsguan = (RelativeLayout.LayoutParams) tv_content
                            .getLayoutParams();
                    layoutParamsguan.leftMargin = 5;
                    tv_content.requestLayout();
                    el_grade.setVisibility(View.VISIBLE);
                    tv_grade.setVisibility(View.VISIBLE);
                    iv_grade.setVisibility(View.VISIBLE);
                    iv_grade.setImageResource(R.drawable.grade_officer);
                    tv_grade.setText("官");
                    tv_content1.setVisibility(View.GONE);
                    break;
                case 6://系统提示
                    SpannableString msg6 = new SpannableString(text);
                    ForegroundColorSpan foregroundColorSpan6 = new ForegroundColorSpan(Color
                            .parseColor("#3cccb0"));
                    msg6.setSpan(foregroundColorSpan6, 0, msg6
                            .length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv_content.setText(msg6);
                    RelativeLayout.LayoutParams layoutParams6 = (RelativeLayout.LayoutParams) tv_content
                            .getLayoutParams();
                    layoutParams6.leftMargin = 0;
                    tv_content.requestLayout();
                    tv_grade.setVisibility(View.GONE);
                    iv_grade.setVisibility(View.GONE);
                    el_grade.setVisibility(View.GONE);
                    tv_content1.setVisibility(View.GONE);
                    break;
            }
        }
    }

    public class GiftItemViewHolder extends AngelCommonViewHolder {
        @BindView(R.id.item_live_chat_avatar) SimpleDraweeView iv_avatar;
        @BindView(R.id.item_live_chat_gift) SimpleDraweeView iv_gift;
        @BindView(R.id.item_live_chat_name) TextView tv_name;
        @BindView(R.id.item_live_chat_des) TextView tv_des;
        @BindView(R.id.tv_count) StrokeTextView tv_count;

        @BindView(R.id.fl_win_small) FrameLayout fl_win_small;
        @BindView(R.id.tv_winsmall) TextView tv_winsmall;


        public GiftItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }


        @Override
        public void render(final Context context, Object model) {
            final JSONObject attr = (JSONObject) model;
            JSONObject user = attr.optJSONObject("user");

            HXImageLoader.loadImage(iv_avatar, user
                    .optString("user_avatar"), px40, px40);

            tv_name.setText(user.optString("user_name"));

            JSONObject command = attr.optJSONObject("command");
            if (command == null ||
                    !command.optString("name").equalsIgnoreCase("gift_send")) {
                return;
            }
            JSONObject gift = command.optJSONObject("gift");
            int winning_rate = command.optInt("winning_rate");
            int searilNum = gift.optInt("searilNum");
            if (gift == null) {
                return;
            }
            int gift_type = gift.optInt("gift_type");
            int gift_count = gift.optInt("gift_once_count");
            tv_des.setText("赠送了" + gift.optString("gift_count") +
                    gift.optString("gift_count_unit") +
                    gift.optString("gift_name"));
            AnimationSet animationSet = new AnimationSet(true);
            Animation scaleAnimation = new ScaleAnimation(1.4f, 1.0f, 1.4f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            //设置动画时间
            scaleAnimation.setDuration(100);
            scaleAnimation.setInterpolator(new CycleInterpolator(1));
            scaleAnimation.setFillAfter(true);
            Animation translateAnimation1 = new TranslateAnimation(0, 10, 0, 10);
            translateAnimation1.setInterpolator(new CycleInterpolator(1));
            translateAnimation1.setDuration(100);
            animationSet.addAnimation(scaleAnimation);
            animationSet.addAnimation(translateAnimation1);
            tv_count.startAnimation(animationSet);

            final String gifturll = SharedPreferenceUtil
                    .getString(gift.optString("gift_id"), "");
            if (null != gifturll && !"".equals(gifturll)) {
                DraweeController controller = Fresco
                        .newDraweeControllerBuilder()
                        .setUri("file://" + gifturll)
                        .setAutoPlayAnimations(true).build();
                iv_gift.setController(controller);
            } else {
                HXImageLoader.loadImage(iv_gift, gift
                        .optString("gift_image"), px65, px65);
            }
            TranslateAnimation translateAnimation = new TranslateAnimation(-400f, 0.0f, 0.1f, 0.1f);
            translateAnimation.setDuration(250);
            iv_gift.startAnimation(translateAnimation);
            //            slotNumView.setBit(gift_count);
            if (gift_type == 2 && gift_count >= 1) {
                tv_count.setText("X" + searilNum);
                if (winning_rate > 0 &&
                        winning_rate < HXApp.getInstance().wining) {
                    fl_win_small.setVisibility(View.VISIBLE);
                    tv_winsmall.setText("恭喜获得" + winning_rate + "倍大奖");
                    ScaleAnimation animation = new ScaleAnimation(1.4f, 1.0f, 1.4f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(200);
                    tv_winsmall.setAnimation(animation);
                } else {
                    fl_win_small.setVisibility(View.INVISIBLE);
                }
            } else {
                tv_count.setText("X" + searilNum);
                fl_win_small.setVisibility(View.INVISIBLE);
                //                slotNumView.addCount(gift_count);
            }
        }
    }

    public class ErrorViewHolder extends AngelCommonViewHolder {

        public ErrorViewHolder(View v) {
            super(v);
        }


        @Override
        public void render(final Context context, Object model) {
            //do nothing
        }
    }
}
