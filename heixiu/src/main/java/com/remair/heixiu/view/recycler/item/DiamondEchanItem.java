package com.remair.heixiu.view.recycler.item;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import com.remair.heixiu.EventConstants;
import com.remair.heixiu.R;
import com.remair.heixiu.bean.Dimaondex;
import com.remair.heixiu.emoji.StringUtil;
import com.remair.heixiu.utils.RxViewUtil;
import com.remair.heixiu.view.recycler.SimpleItem;
import org.simple.eventbus.EventBus;
import rx.functions.Action1;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：yuliuyu
 * 创建时间：2016/8/29 16:41
 * 修改人：
 * 修改时间：2016/8/29 16:41
 * 修改备注：
 */
public class DiamondEchanItem extends SimpleItem<Dimaondex> {

    @BindView(R.id.chat_img_button) TextView mChatImgButton;
    @BindView(R.id.icon_first) ImageView mIconFirst;
    @BindView(R.id.chat_img) ImageView mChatImg;
    @BindView(R.id.TextView_chat_img_button) TextView mTextViewChatImgButton;


    @Override
    public int getLayoutResId() {
        return R.layout.item_recharge;
    }
     Context context;

    Dimaondex dimaondex;


    public  DiamondEchanItem(Context context){
        this.context=context;
    };

    @Override
    public void handleData(Dimaondex dimaondex, int i) {
        mRoot.setTag(dimaondex);
        this.dimaondex = dimaondex;
        if (dimaondex.number == 1) {
            mIconFirst.setVisibility(View.VISIBLE);
        } else {
            mIconFirst.setVisibility(View.GONE);
        }
        mChatImgButton.setText(dimaondex.number_value + "");


        if(dimaondex.type==1){
            mTextViewChatImgButton.setText("¥" +dimaondex.number + "");
        }else if(dimaondex.type==2||dimaondex.type==4){

            SpannableString spanString = StringUtil.stringToSpannableString(
                    dimaondex.number+ "00", context);
            Drawable d = context.getResources()
                                .getDrawable(R.drawable.cunzaiganbg);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d
                    .getIntrinsicHeight());
            ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
            spanString.setSpan(span, (dimaondex.number+"").length(),
                    (dimaondex.number+"").length()+
                            2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mTextViewChatImgButton.setText(spanString);

        }else if(dimaondex.type==3){
            SpannableString spanString = StringUtil.stringToSpannableString(
                    dimaondex.number+ "00", context);
            Drawable d = context.getResources()
                                .getDrawable(R.drawable.zhuanshibgbg);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d
                    .getIntrinsicHeight());
            ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
            spanString.setSpan(span, (dimaondex.number+"").length(),
                    (dimaondex.number+"").length()+
                            2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mTextViewChatImgButton.setText(spanString);
        }

        if (i == 0) {
            mChatImg.setImageResource(dimaondex.type < 3
                                      ? R.drawable.diamond1
                                      : R.drawable.heidou1);
        } else if (i == 1) {
            mChatImg.setImageResource(dimaondex.type < 3
                                      ? R.drawable.diamond2
                                      : R.drawable.heidou2);
        } else if (i == 2) {
            mChatImg.setImageResource(dimaondex.type < 3
                                      ? R.drawable.diamond3
                                      : R.drawable.heidou3);
        } else if (i == 3) {
            mChatImg.setImageResource(dimaondex.type < 3
                                      ? R.drawable.diamond4
                                      : R.drawable.heidou4);
        } else if (i == 4) {
            mChatImg.setImageResource(dimaondex.type < 3
                                      ? R.drawable.diamond5
                                      : R.drawable.heidou5);
        } else if (i == 5) {
            mChatImg.setImageResource(dimaondex.type < 3
                                      ? R.drawable.diamond6
                                      : R.drawable.heidou6);
        }
    }


    @Override
    public void setViews() {
        super.setViews();
        RxViewUtil.viewBindClick(mRoot, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

                EventBus.getDefault()
                        .post(dimaondex, EventConstants.DIANMONDVOIDCHECK);
            }
        });
    }
}
