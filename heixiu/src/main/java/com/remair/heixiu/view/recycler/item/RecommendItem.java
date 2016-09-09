package com.remair.heixiu.view.recycler.item;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.R;
import com.remair.heixiu.bean.RecommendBean;
import com.remair.heixiu.net.HXHttpUtil;
import com.remair.heixiu.net.HttpSubscriber;
import com.remair.heixiu.utils.HXImageLoader;
import com.remair.heixiu.utils.RxViewUtil;
import com.remair.heixiu.utils.Utils;
import com.remair.heixiu.utils.Xtgrade;
import com.remair.heixiu.view.LoadingDialog;
import com.remair.heixiu.view.recycler.SimpleItem;
import rx.functions.Action1;

/**
 * 项目名称：Android
 * 类描述：推送管理
 * 创建人：wsk
 * 创建时间：16/8/29 下午3:00
 * 修改人：wsk
 * 修改时间：16/8/29 下午3:00
 * 修改备注：
 */
public class RecommendItem extends SimpleItem<RecommendBean.ItemsBean> {
    @BindView(R.id.chat_img) SimpleDraweeView chat_img;//头像
    @BindView(R.id.chat_name) TextView chat_name;//名称
    @BindView(R.id.chat_sex) ImageView chat_sex;//性别
    @BindView(R.id.tv_grade) TextView tv_grade;//等级
    @BindView(R.id.chat_msg) TextView chat_msg;//签名
    @BindView(R.id.iv_push) ImageView iv_push;
    @BindView(R.id.iv_grade) SimpleDraweeView iv_grade;
    private int px42;
    private RecommendBean.ItemsBean mItemsBean;
    Context mContext;
    RecommendListener mRecommendListener;
    LoadingDialog loadingDialog;
    int mPosition;


    public RecommendItem(Context context, RecommendListener recommendListener) {
        this.mContext = context;
        this.mRecommendListener = recommendListener;
    }


    @Override
    public int getLayoutResId() {
        return R.layout.item_push;
    }


    @Override
    public void setViews() {
        super.setViews();
        px42 = Utils.getPX(42);
        RxViewUtil.viewBindClick(iv_push, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (mItemsBean != null) {
                    onPushClick();
                }
            }
        });
    }


    private void onPushClick() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(mContext, R.style.dialog);
        }
        loadingDialog.show();
        if (1 == mItemsBean.accept_push) {//关闭接受

            HXHttpUtil.getInstance()
                      .editAttentionPush(HXApp.getInstance().getUserInfo().user_id,
                              0, mItemsBean
                              .relation_id)
                      .subscribe(new HttpSubscriber<Object>() {
                          @Override
                          public void onNext(Object o) {
                              iv_push.setImageDrawable(HXApp.getInstance()
                                                            .getResources()
                                                            .getDrawable(R.drawable.icon_push_off));
                              mItemsBean.accept_push = 0;
                              if (mRecommendListener != null) {
                                  mRecommendListener.unRecommend(mPosition);
                              }
                              loadingDialog.todismiss();
                          }
                          @Override
                          public void onError(Throwable e) {
                              super.onError(e);
                              loadingDialog.todismiss();
                          }
                      });
        } else {//接受

            HXHttpUtil.getInstance()
                      .editAttentionPush(HXApp.getInstance().getUserInfo().user_id, 1, mItemsBean.relation_id)
                      .subscribe(new HttpSubscriber<Object>() {
                          @Override
                          public void onNext(Object o) {
                              iv_push.setImageDrawable(HXApp.getInstance()
                                                            .getResources()
                                                            .getDrawable(R.drawable.icon_push_on));
                              mItemsBean.accept_push = 1;
                              if (mRecommendListener != null) {
                                  mRecommendListener.recommend(mPosition);
                              }
                              loadingDialog.todismiss();
                          }
                          @Override
                          public void onError(Throwable e) {
                              super.onError(e);
                              loadingDialog.todismiss();
                          }
                      });
        }
    }

    @Override
    public void handleData(RecommendBean.ItemsBean itemsBean, int i) {
        mItemsBean = itemsBean;
        mPosition = i;
        HXImageLoader.loadImage(chat_img, itemsBean.photo, px42, px42);
        chat_name.setText(itemsBean.nickname);
        tv_grade.setText(itemsBean.empiric + "");
        chat_msg.setText(itemsBean.signature);
        Xtgrade.mXtgrade(itemsBean.empiric, iv_grade, tv_grade);
        int gender = itemsBean.gender;
        if (0 == gender) {
            chat_sex.setImageDrawable(HXApp.getInstance().getResources()
                                           .getDrawable(R.drawable.sex_woman));
        } else {
            chat_sex.setImageDrawable(HXApp.getInstance().getResources()
                                           .getDrawable(R.drawable.sex_man));
        }
        int accept_push = itemsBean.accept_push;
        if (0 == accept_push) {
            iv_push.setImageDrawable(HXApp.getInstance().getResources()
                                          .getDrawable(R.drawable.icon_push_off));
        } else {
            iv_push.setImageDrawable(HXApp.getInstance().getResources()
                                          .getDrawable(R.drawable.icon_push_on));
        }
    }


    public interface RecommendListener {
        void recommend(int postion);

        void unRecommend(int postion);
    }
}
