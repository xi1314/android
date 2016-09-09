package com.remair.heixiu.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.remair.heixiu.EventConstants;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.base.HXBaseActivity;
import com.remair.heixiu.adapters.MyFragmentPagerAdapter;
import com.remair.heixiu.bean.Dimaondex;
import com.remair.heixiu.bean.ExchageDimo;
import com.remair.heixiu.bean.UserInfo;
import com.remair.heixiu.net.HXHttpUtil;
import com.remair.heixiu.net.HttpSubscriber;
import com.remair.heixiu.utils.RxViewUtil;
import com.remair.heixiu.view.ShowDialog;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.simple.eventbus.Subscriber;
import rx.functions.Action1;
import studio.archangel.toolkitv2.util.text.Notifier;

public class MineHeidouActivity extends HXBaseActivity {
    @BindView(R.id.text_balanceview) TextView mTextBalanceview;
    @BindView(R.id.text_balance) TextView mTextBalance;
    @BindView(R.id.rl_background) RelativeLayout mRlBackground;
    @BindView(R.id.tv_title) TextView mTvTitle;
    @BindView(R.id.Imagediamondexchange) ImageView mImagediamondexchange;
    @BindView(R.id.tv_title2) TextView mTvTitle2;
    @BindView(R.id.Imagediamondexchange2) ImageView mImagediamondexchange2;
    @BindView(R.id.iv_mark) ImageView mIvMark;
    @BindView(R.id.iv_mark2) ImageView mIvMark2;
    @BindView(R.id.radiobutton) ImageView mRadiobutton;
    @BindView(R.id.textconvertible) TextView mTextconvertible;
    @BindView(R.id.viewpager) ViewPager mViewpager;
    List<View> listViews;

    @BindView(R.id.relat_diamondexchange) RelativeLayout mRelatDiamondexchange;
    @BindView(R.id.relat_diamondexchange2)
    RelativeLayout mRelatDiamondexchange2;
    @BindView(R.id.title_txt) TextView mTitleTxt;
    @BindView(R.id.title_left_image) ImageButton mTitleLeftImage;
    @BindView(R.id.title_right_text) TextView mTitleRightText;


    @Override
    protected void initUI() {
        setContentView(R.layout.activity_frag_mine_newcopy);
        ButterKnife.bind(this);
        context = MineHeidouActivity.this;
        mSelfUserInfo = ((HXApp) getApplication()).getUserInfo();

        mTitleTxt.setText("我的嘿豆");
        mTitleRightText.setVisibility(View.VISIBLE);
        mTitleRightText.setText("兑换记录");
        RxViewUtil.viewBindClick(mTitleRightText, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(context, CaseHistryActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
            }
        });
    }


    MyFragmentPagerAdapter mAdapter;


    @Override
    protected void initData() {
        mTextBalanceview.setText("嘿豆余额");
        mTextBalance.setText(mSelfUserInfo.heidou_amount + "");//余额
        mTvTitle.setText("钻石兑换");
        mTvTitle2.setText("存在感兑换");

        mImagediamondexchange2.setImageResource(R.drawable.cunzaigandjdjx);
        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), 2);
        mViewpager.setAdapter(mAdapter);
        mViewpager.setCurrentItem(0);
        mIvMark2.setVisibility(View.GONE);
        mRadiobutton.setVisibility(View.GONE);
        mTextconvertible
                .setText("可兑换钻石：" + mSelfUserInfo.virtual_currency_amount);
        RxViewUtil
                .viewBindClick(mRelatDiamondexchange, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewpager.setCurrentItem(MinediamondActivity.PAGE_ONE);
                        position0();
                    }
                });
        RxViewUtil
                .viewBindClick(mRelatDiamondexchange2, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewpager.setCurrentItem(MinediamondActivity.PAGE_TWO);
                        position1();
                    }
                });

        mViewpager
                .setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                    public void onPageSelected(int position) {
                        //当viewPager发生改变时，同时改变tabhost上面的currentTab
                        if (position == 0) {
                            position0();
                        } else {
                            position1();
                        }
                    }


                    public void onPageScrolled(int arg0, float arg1, int arg2) {
                    }


                    public void onPageScrollStateChanged(int arg0) {
                    }
                });
    }


    public void position0() {
        mIvMark2.setVisibility(View.GONE);
        mIvMark.setVisibility(View.VISIBLE);
        mTextconvertible
                .setText("可兑换钻石：" + mSelfUserInfo.virtual_currency_amount);
        mRadiobutton.setVisibility(View.GONE);
    }


    public void position1() {
        mIvMark2.setVisibility(View.VISIBLE);
        mIvMark.setVisibility(View.GONE);
        mTextconvertible.setText("可兑换存在感:" +
                mSelfUserInfo.income_charm_value + "");
        mRadiobutton.setVisibility(View.GONE);
    }


    Context context = null;

    UserInfo mSelfUserInfo;
    boolean b;

    ShowDialog DialognewPerCard;


    @Subscriber(tag = EventConstants.DIANMONDVOIDCHECK)
    public void setOnhidouLiseten(final Dimaondex dimaondex) {

        DialognewPerCard = new ShowDialog(context,
                "确定要兑换" + dimaondex.number_value +
                        "嘿豆吗？", "确定", "取消");

        DialognewPerCard
                .setClicklistener(new ShowDialog.ClickListenerInterface() {
                    @Override
                    public void doConfirm() {

                        if (dimaondex.type == 3) {

                            Map<String, String> jo = new HashMap<String, String>();
                            if (mSelfUserInfo.virtual_currency_amount <
                                    dimaondex.number) {
                                Notifier.showLongMsg(context, "兑换失败可兑换的钻石不足！");
                                return;
                            }
                            jo.put("user_id", mSelfUserInfo.user_id + "");
                            jo.put("charm_diamond", dimaondex.number + "");
                            jo.put("type", "0");
                            HXHttpUtil.getInstance().newExchange(jo)
                                      .subscribe(new HttpSubscriber<ExchageDimo>() {
                                          @Override
                                          public void onNext(ExchageDimo exchageDimo) {

                                              mSelfUserInfo.heidou_amount = exchageDimo.heidou_amount;
                                              mSelfUserInfo.virtual_currency_amount = exchageDimo.charm_diamond_valid;
                                              HXApp.getInstance()
                                                   .setUserInfo(mSelfUserInfo);
                                              mTextBalance.setText(
                                                      mSelfUserInfo.heidou_amount +
                                                              "");
                                              mTextconvertible.setText(
                                                      "可兑换钻石：" +
                                                              mSelfUserInfo.virtual_currency_amount);
                                          }
                                      });
                        } else if (dimaondex.type == 4) {
                            Map<String, String> jo = new HashMap<String, String>();
                            if (mSelfUserInfo.income_charm_value <
                                    dimaondex.number) {
                                Notifier.showLongMsg(context,
                                        "兑换失败可兑换的存在感不足！");
                                return;
                            }
                            jo.put("user_id", mSelfUserInfo.user_id + "");
                            jo.put("charm_diamond", dimaondex.number + "");
                            jo.put("type", "1");
                            HXHttpUtil.getInstance().newExchange(jo)
                                      .subscribe(new HttpSubscriber<ExchageDimo>() {
                                          @Override
                                          public void onNext(ExchageDimo exchageDimo) {

                                              mSelfUserInfo.heidou_amount = exchageDimo.heidou_amount;
                                              mSelfUserInfo.income_charm_value = exchageDimo.charm_diamond_valid;
                                              HXApp.getInstance()
                                                   .setUserInfo(mSelfUserInfo);
                                              mTextBalance.setText(
                                                      mSelfUserInfo.heidou_amount +
                                                              "");
                                              mTextconvertible
                                                      .setText("可兑换存在感:" +
                                                              mSelfUserInfo.income_charm_value +
                                                              "");
                                          }
                                      });
                        }
                        DialognewPerCard.dismiss();
                    }


                    @Override
                    public void doCancel() {
                        DialognewPerCard.dismiss();
                    }
                });

        DialognewPerCard.show();
    }
}
