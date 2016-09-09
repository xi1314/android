package com.remair.heixiu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

public class Frag_caseActivity extends HXBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
  /*      setContentView(R.layout.activity_frag_mine_new);
        EventBus.getDefault().register(this);
        Util.setupActionBar(this, "存在感兑换");
        AngelActionBar aab = getAngelActionBar();
        aab.setRightText("兑换记录");
        aab.setTextColor(getResources().getColor(R.color.white));

        aab.setRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getSelf(), CaseHistryActivity.class);
                intent.putExtra("type", 2);
                startActivity(intent);
            }
        });

        context = Frag_caseActivity.this;

        pager = (ViewPager) findViewById(R.id.viewpager);
        rl_background = (RelativeLayout) findViewById(R.id.rl_background);

        mSelfUserInfo = ((HXApp) getApplication()).getUserInfo();

        text_zuanshi_viewnumber = (TextView) findViewById(R.id.text_zuanshi_viewnumber);
        text_zuanshi_view = (TextView) findViewById(R.id.text_zuanshi_view);
        text_zuanshi_view.setText("可兑换存在感");

        text_zuanshi_viewnumber.setText(mSelfUserInfo.charm_value_valid + "");

        keduihuanzuanshi = (TextView) findViewById(R.id.keduihuanzuanshi);
        keduihuanzuanshi.setText("嘿豆余额:" + mSelfUserInfo.heidou_amount);

        //定放一个放view的list，用于存放viewPager用到的view
        listViews = new ArrayList<View>();

        manager = new LocalActivityManager(this, true);
        manager.dispatchCreate(savedInstanceState);

        Intent i1 = new Intent(context, T2Activity.class);
        listViews.add(getView("A", i1));
        Intent i2 = new Intent(context, T4Activity.class);
        listViews.add(getView("B", i2));
*//*        Intent i3 = new Intent(context, T3Activity.class);
        listViews.add(getView("C", i3));*//*

        tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();
        tabHost.setup(manager);

        //这儿主要是自定义一下tabhost中的tab的样式
        RelativeLayout tabIndicator1 = (RelativeLayout) LayoutInflater
                .from(this).inflate(R.layout.tabwidget, null);
        TextView tvTab1 = (TextView) tabIndicator1.findViewById(R.id.tv_title);
        tvTab1.setText("兑换嘿豆");
        iv_mark1 = (ImageView) tabIndicator1.findViewById(R.id.iv_mark);
        ImageView iv1 = (ImageView) tabIndicator1
                .findViewById(R.id.xianjinchongzhipp);

        iv1.setImageResource(R.drawable.cunzaigandjdjx);
        RelativeLayout tabIndicator2 = (RelativeLayout) LayoutInflater
                .from(this).inflate(R.layout.tabwidget, null);

        ImageView iv = (ImageView) tabIndicator2
                .findViewById(R.id.xianjinchongzhipp);
        iv_mark2 = (ImageView) tabIndicator2.findViewById(R.id.iv_mark);

        iv.setImageResource(R.drawable.cunzaigandjdjx);
        TextView tvTab2 = (TextView) tabIndicator2.findViewById(R.id.tv_title);
        tvTab2.setText("兑换钻石");

        iv_mark1.setVisibility(View.VISIBLE);
        iv_mark2.setVisibility(View.GONE);

   *//*     RelativeLayout tabIndicator3 = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.tabwidget,null);
        TextView tvTab3 = (TextView)tabIndicator3.findViewById(R.id.tv_title);
        tvTab3.setText("第三页");*//*

        Intent intent = new Intent(context, EmptyActivity.class);
        //注意这儿Intent中的activity不能是自身 比如“A”对应的是T1Activity，后面intent就new的T3Activity的。
        tabHost.addTab(tabHost.newTabSpec("A").setIndicator(tabIndicator1)
                              .setContent(intent));

        tabHost.addTab(tabHost.newTabSpec("B").setIndicator(tabIndicator2)
                              .setContent(intent));
        //     tabHost.addTab(tabHost.newTabSpec("C").setIndicator(tabIndicator3).setContent(intent));

        pager.setAdapter(new MyPageAdapter(listViews));
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            public void onPageSelected(int position) {
                //当viewPager发生改变时，同时改变tabhost上面的currentTab
                tabHost.setCurrentTab(position);
            }


            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }


            public void onPageScrollStateChanged(int arg0) {
            }
        });

        //点击tabhost中的tab时，要切换下面的viewPager
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            public void onTabChanged(String tabId) {

                if ("A".equals(tabId)) {
                    pager.setCurrentItem(0);
                    iv_mark1.setVisibility(View.VISIBLE);
                    iv_mark2.setVisibility(View.GONE);
                    keduihuanzuanshi.setText(
                            "嘿豆余额:" + mSelfUserInfo.heidou_amount + "");
                    b = false;
                }
                if ("B".equals(tabId)) {
                    keduihuanzuanshi.setText("钻石余额:" +
                            mSelfUserInfo.virtual_currency_amount + "");
                    b = true;
                    pager.setCurrentItem(1);
                    iv_mark2.setVisibility(View.VISIBLE);
                    iv_mark1.setVisibility(View.GONE);
                }
            }
        });
    }


    private View getView(String id, Intent intent) {
        return manager.startActivity(id, intent).getDecorView();
    }


    private class MyPageAdapter extends PagerAdapter {

        private List<View> list;


        private MyPageAdapter(List<View> list) {
            this.list = list;
        }


        public void destroyItem(View view, int position, Object arg2) {
            ViewPager pViewPager = ((ViewPager) view);
            pViewPager.removeView(list.get(position));
        }


        public void finishUpdate(View arg0) {
        }


        @Override
        public int getCount() {
            return list.size();
        }


        @Override
        public Object instantiateItem(View view, int position) {
            ViewPager pViewPager = ((ViewPager) view);
            pViewPager.addView(list.get(position));
            return list.get(position);
        }


        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Subscriber(tag = EventConstants.UPDATE_HEIDOU_OR_MONEY)
    public void setOnhidouLiseten(String str) {
        mSelfUserInfo = ((HXApp) getApplication()).getUserInfo();
        text_zuanshi_viewnumber.setText(mSelfUserInfo.charm_value_valid + "");
        if (b) {
            keduihuanzuanshi
                    .setText("钻石余额:" + mSelfUserInfo.virtual_currency_amount +
                            "");
        } else {
            keduihuanzuanshi.setText("嘿豆余额:" + mSelfUserInfo.heidou_amount +
                    "");
        }
        */
    }


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
        context = Frag_caseActivity.this;
        mSelfUserInfo = ((HXApp) getApplication()).getUserInfo();
        mTitleTxt.setText("存在感兑换");
        mTitleRightText.setVisibility(View.VISIBLE);
        mTitleRightText.setText("兑换记录");
        RxViewUtil.viewBindClick(mTitleRightText, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

                Intent intent = new Intent(context, CaseHistryActivity.class);
                intent.putExtra("type", 2);
                startActivity(intent);
            }
        });
    }


    MyFragmentPagerAdapter mAdapter;


    @Override
    protected void initData() {
        mTextBalanceview.setText("可兑换存在感");
        mTextBalance.setText(mSelfUserInfo.income_charm_value + "");//余额
        mTvTitle.setText("兑换嘿豆");
        mTvTitle2.setText("兑换钻石");
        mImagediamondexchange.setImageResource(R.drawable.cunzaigandjdjx);
        mImagediamondexchange2.setImageResource(R.drawable.cunzaigandjdjx);
        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), 3);
        mViewpager.setAdapter(mAdapter);
        mViewpager.setCurrentItem(0);
        mIvMark2.setVisibility(View.GONE);
        mRadiobutton.setVisibility(View.GONE);
        mTextconvertible.setText("嘿豆余额：" + mSelfUserInfo.heidou_amount);
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

        mTextconvertible.setText("嘿豆余额：" + mSelfUserInfo.heidou_amount);
        mRadiobutton.setVisibility(View.GONE);
    }


    public void position1() {
        mIvMark2.setVisibility(View.VISIBLE);
        mIvMark.setVisibility(View.GONE);

        mTextconvertible.setText("钻石余额:" +
                mSelfUserInfo.virtual_currency_amount + "");
        mRadiobutton.setVisibility(View.GONE);
    }


    Context context = null;

    UserInfo mSelfUserInfo;
    boolean b;

    ShowDialog DialognewPerCard;


    @Subscriber(tag = EventConstants.DIANMONDVOIDCHECK)
    public void setOnhidouLiseten(final Dimaondex dimaondex) {

        if (dimaondex.type == 2) {
            DialognewPerCard = new ShowDialog(context,
                    "确定要兑换" + dimaondex.number_value +
                            "钻石吗？", "确定", "取消");
        } else {
            DialognewPerCard = new ShowDialog(context,
                    "确定要兑换" + dimaondex.number_value +
                            "嘿豆吗？", "确定", "取消");
        }
        DialognewPerCard
                .setClicklistener(new ShowDialog.ClickListenerInterface() {
                    @Override
                    public void doConfirm() {
                        if (dimaondex.type == 2) {
                            if (mSelfUserInfo.income_charm_value <
                                    dimaondex.number) {
                                Notifier.showLongMsg(context, "兑换失败可兑换的存在感不足！");
                                DialognewPerCard.dismiss();
                                return;
                            }

                            HXHttpUtil.getInstance().exchange(dimaondex.number)
                                      .subscribe(new HttpSubscriber<ExchageDimo>() {
                                          @Override
                                          public void onNext(ExchageDimo exchageDimo) {
                                              mSelfUserInfo.virtual_currency_amount = exchageDimo.my_diamond;
                                              mSelfUserInfo.income_charm_value = exchageDimo.charm_value_valid;
                                              HXApp.getInstance()
                                                   .setUserInfo(mSelfUserInfo);
                                              mTextBalance.setText(
                                                      mSelfUserInfo.income_charm_value +
                                                              "");//余额
                                              mTextconvertible.setText("钻石余额:" +
                                                      mSelfUserInfo.virtual_currency_amount +
                                                      "");

                                              DialognewPerCard.dismiss();
                                          }


                                          @Override
                                          public void onError(Throwable e) {
                                              super.onError(e);
                                              DialognewPerCard.dismiss();
                                          }
                                      });
                        } else if (dimaondex.type == 4) {
                            Map<String, String> jo = new HashMap<String, String>();
                            if (mSelfUserInfo.income_charm_value <
                                    dimaondex.number) {
                                Notifier.showLongMsg(context, "兑换失败可兑换的存在感不足！");
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
                                                      mSelfUserInfo.income_charm_value +
                                                              "");
                                              mTextconvertible.setText("嘿豆余额：" +
                                                      mSelfUserInfo.heidou_amount);
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
