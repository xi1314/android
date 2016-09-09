package com.remair.heixiu.activity;

import android.support.v4.view.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.Bundler;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.base.HXBaseActivity;
import com.remair.heixiu.fragment.RecHistFragment;
import studio.archangel.toolkitv2.util.text.Notifier;
import studio.archangel.toolkitv2.util.ui.SelectorProvider;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：JXH
 * 创建时间：16/4/7 19:28
 * 修改人：JXH
 * 修改时间：16/4/7 19:28
 * 修改备注：
 */
public class CaseHistryActivity extends HXBaseActivity {
    public int type;//0 钻石 1 嘿豆 2存在感兑换
    @BindView(R.id.frag_home_pager_tab) SmartTabLayout mFragHomePagerTab;
    @BindView(R.id.viewpager) ViewPager mViewpager;
    private FragmentPagerItemAdapter adapter;


    @Override
    protected void initUI() {
        setContentView(R.layout.case_histry_activity);
        ButterKnife.bind(this);
    }


    @Override
    protected void initData() {
        type = getIntent().getIntExtra("type", -1);
        if (type == -1) {
            Notifier.showShortMsg(getApplicationContext(), "请重试");
            return;
        } else if (type == 0) {//钻石充值
            adapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), FragmentPagerItems
                    .with(this).add("充值", RecHistFragment.class, new Bundler()
                            .putInt("type", 0).get())
                    .add("兑换", RecHistFragment.class, new Bundler()
                            .putInt("type", 1).get()).create());
        } else if (type == 1) {//嘿豆记录
            adapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), FragmentPagerItems
                    .with(this).add("钻石", RecHistFragment.class, new Bundler()
                            .putInt("type", 0).get())
                    .add("存在感", RecHistFragment.class, new Bundler()
                            .putInt("type", 1).get()).create());
        } else if (type == 2) {//存在感兑换
            adapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), FragmentPagerItems
                    .with(this).add("钻石", RecHistFragment.class, new Bundler()
                            .putInt("type", 0).get())
                    .add("嘿豆", RecHistFragment.class, new Bundler()
                            .putInt("type", 1).get()).create());
        }

        mViewpager.setAdapter(adapter);
        mFragHomePagerTab.setDefaultTabTextColor(SelectorProvider
                .getColorStateList(getResources()
                        .getColor(R.color.white), getResources()
                        .getColor(R.color.white)));
        mFragHomePagerTab.setViewPager(mViewpager);
    }


    @OnClick(R.id.title_left_image)
    public void onClick() {
        finish();
    }
}
