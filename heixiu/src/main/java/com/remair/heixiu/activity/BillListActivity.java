package com.remair.heixiu.activity;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.widget.ImageButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.Bundler;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.base.HXBaseActivity;
import com.remair.heixiu.fragment.BillListFragment;
import com.remair.heixiu.utils.RxViewUtil;
import rx.functions.Action1;
import studio.archangel.toolkitv2.util.ui.SelectorProvider;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：JXH
 * 创建时间：2016/7/18 15:02
 * 修改人：JXH
 * 修改时间：2016/7/18 15:02
 * 修改备注：
 */
public class BillListActivity extends HXBaseActivity {
    @BindView(R.id.frag_home_pager) ViewPager fragHomePager;
    @BindView(R.id.frag_home_pager_tab) SmartTabLayout mFragHomePagerTab;
    @BindView(R.id.title_left_image) ImageButton mTitleLeftImage;


    @Override
    protected void initUI() {
        setContentView(R.layout.frag_home);
        ButterKnife.bind(this);
    }


    @Override
    protected void initData() {
        int viewed_user_id = getIntent().getIntExtra("viewed_user_id", 0);
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), FragmentPagerItems
                .with(getApplication())
                .add("粉丝榜", BillListFragment.class, new Bundler()
                        .putInt("type", 0)
                        .putInt("viewed_user_id", viewed_user_id).get())
                .add("贡献榜", BillListFragment.class, new Bundler()
                        .putInt("type", 1)
                        .putInt("viewed_user_id", viewed_user_id).get())
                .create());
        fragHomePager.setAdapter(adapter);
        mFragHomePagerTab.setDefaultTabTextColor(SelectorProvider
                .getColorStateList(ContextCompat
                        .getColor(this, R.color.white), ContextCompat
                        .getColor(this, R.color.white)));
        mFragHomePagerTab.setViewPager(fragHomePager);
        RxViewUtil.viewBindClick(mTitleLeftImage, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                onBackPressed();
            }
        });
    }
}
