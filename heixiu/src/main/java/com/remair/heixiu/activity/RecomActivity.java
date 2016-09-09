package com.remair.heixiu.activity;

import android.support.v4.view.ViewPager;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.Bundler;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.base.HXBaseActivity;
import com.remair.heixiu.fragment.RecomFragment;
import studio.archangel.toolkitv2.util.ui.SelectorProvider;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：JXH
 * 创建时间：2016/5/26 12:14
 * 修改人：JXH
 * 修改时间：2016/5/26 12:14
 * 修改备注：
 */
public class RecomActivity extends HXBaseActivity {

    @BindView(R.id.title_txt) TextView mTitleTxt;
    @BindView(R.id.title_left_image) ImageButton mTitleLeftImage;
    @BindView(R.id.activity_recom_tab) SmartTabLayout mActivityRecomTab;
    @BindView(R.id.recom_pager) ViewPager mRecomPager;


    @Override
    protected void initUI() {
        setContentView(R.layout.act_recom);
        ButterKnife.bind(this);
    }


    @Override
    protected void initData() {
        mTitleTxt.setText(getString(R.string.recom_fr));
        int current = getIntent().getIntExtra("current", -1);
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), FragmentPagerItems
                .with(this).add("手机通讯录", RecomFragment.class, new Bundler()
                        .putInt("type", 0).get())
                .add("新浪微博", RecomFragment.class, new Bundler()
                        .putInt("type", 1).get()).create());
        mRecomPager.setAdapter(adapter);
        mActivityRecomTab.setDefaultTabTextColor(SelectorProvider
                .getColorStateList(getResources()
                        .getColor(R.color.white), getResources()
                        .getColor(R.color.white)));
        mActivityRecomTab.setViewPager(mRecomPager);
        if (current != -1) {
            if (current < adapter.getCount()) {
                mRecomPager.setCurrentItem(current);
            }
        }
    }


    @OnClick(R.id.title_left_image)
    public void onClick() {
        finish();
    }
}
