package com.remair.heixiu.view.recycler.item;

import android.content.Context;
import android.text.SpannableString;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.remair.heixiu.R;
import com.remair.heixiu.bean.PrivateMessage;
import com.remair.heixiu.emoji.StringUtil;
import com.remair.heixiu.sqlite.UnFollowConcernInfoDB;
import com.remair.heixiu.utils.HXImageLoader;
import com.remair.heixiu.utils.HXUtil;
import com.remair.heixiu.utils.RxViewUtil;
import com.remair.heixiu.utils.SharedPreferenceUtil;
import com.remair.heixiu.utils.Utils;
import com.remair.heixiu.utils.Xtgrade;
import com.remair.heixiu.view.recycler.SimpleItem;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：wsk
 * 创建时间：16/8/31 上午10:37
 * 修改人：wsk
 * 修改时间：16/8/31 上午10:37
 * 修改备注：
 */
public class UnFollowInfoItem extends SimpleItem<PrivateMessage> {
    @BindView(R.id.chat_img) SimpleDraweeView chat_img;
    @BindView(R.id.chat_msg_count) TextView chat_msg_count;
    @BindView(R.id.chat_name) TextView chat_name;
    @BindView(R.id.chat_sex) ImageView chat_sex;
    @BindView(R.id.chat_levl) SimpleDraweeView chat_levl;
    @BindView(R.id.chat_grad) TextView chat_grad;
    @BindView(R.id.chat_msg) TextView chat_msg;
    @BindView(R.id.chat_msg_tip) RelativeLayout chat_msg_tip;
    @BindView(R.id.rl_grade) RelativeLayout rl_grade;
    @BindView(R.id.chat_private) TextView chat_private;
    private Context mContext;
    View.OnClickListener mItemOnClickListener;
    View.OnLongClickListener mLongClickListener;


    public UnFollowInfoItem(Context context,View.OnClickListener itemOnClick) {
        mContext=context;
        mItemOnClickListener=itemOnClick;
    }
    @Override
    public void setViews() {
        super.setViews();
        RxViewUtil.viewBindClick(mRoot, mItemOnClickListener);
        RxViewUtil.viewBindClick(mRoot, mLongClickListener);
    }


    @Override
    public int getLayoutResId() {
        return R.layout.item_chat;
    }


    @Override
    public void handleData(PrivateMessage privateMessage, int i) {
        UnFollowConcernInfoDB concerinfo = (UnFollowConcernInfoDB) privateMessage;
        mRoot.setTag(concerinfo);
        String userInfo = concerinfo.getUserinfo();

        try {
            final int relation = concerinfo.getRelation();
            JSONObject jsonObject = new JSONObject(userInfo);
            final String poto = jsonObject.optString("user_avatar");
            final String name = jsonObject.optString("user_name");
            int gender = jsonObject.optInt("gender");
            int grade = jsonObject.optInt("grade");
            final String user_id = jsonObject.optString("user_id");
            if ("-1".equals(concerinfo.getHxtype())) {
                chat_private.setVisibility(View.VISIBLE);
                chat_sex.setVisibility(View.GONE);
                rl_grade.setVisibility(View.GONE);
                chat_msg.setVisibility(View.GONE);
                chat_msg_tip.setVisibility(View.GONE);
                HXImageLoader
                        .loadImage(chat_img, poto, Utils.getPX(42), Utils
                                .getPX(42));
                chat_name.setText(name);
                chat_private
                        .setBackgroundResource(R.drawable.shape_chat_type);
            } else {
                chat_private.setVisibility(View.VISIBLE);
                chat_sex.setVisibility(View.VISIBLE);
                rl_grade.setVisibility(View.VISIBLE);
                chat_msg.setVisibility(View.VISIBLE);
                chat_private.setText(HXUtil
                        .GetStringFormat(concerinfo.getUpdatetime()));
                if ("1".equals(user_id)) {
                    chat_name.setText(name);
                    final SpannableString spanString = StringUtil
                            .stringToSpannableString(concerinfo
                                    .getLastmessage(), mContext);
                    chat_msg.setText(spanString);
                    chat_sex.setVisibility(View.GONE);
                    chat_levl.setVisibility(View.GONE);
                    chat_grad.setVisibility(View.GONE);
                    if (SharedPreferenceUtil.getLong(user_id, 0) > 0) {
                        chat_msg_tip.setVisibility(View.VISIBLE);
                        chat_msg_count.setText(
                                SharedPreferenceUtil.getLong(user_id, 0) +
                                        "");
                    } else {
                        chat_msg_tip.setVisibility(View.GONE);
                    }
                } else {
                    chat_sex.setVisibility(View.VISIBLE);
                    chat_levl.setVisibility(View.VISIBLE);
                    chat_grad.setVisibility(View.VISIBLE);
                    HXImageLoader.loadImage(chat_img, poto, Utils
                            .getPX(42), Utils.getPX(42));
                    if (gender == 0) {
                        chat_sex.setImageResource(R.drawable.sex_man);
                    } else {
                        chat_sex.setImageResource(R.drawable.sex_woman);
                    }
                    Xtgrade.mXtgrade(grade, chat_levl, chat_grad);
                    chat_name.setText(name);
                    final SpannableString spanString = StringUtil
                            .stringToSpannableString(concerinfo
                                    .getLastmessage(), mContext);
                    chat_msg.setText(spanString);
                    if (SharedPreferenceUtil.getLong(user_id, 0) > 0) {
                        chat_msg_tip.setVisibility(View.VISIBLE);
                        chat_msg_count.setText(
                                SharedPreferenceUtil.getLong(user_id, 0) +
                                        "");
                    } else {
                        chat_msg_tip.setVisibility(View.GONE);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
