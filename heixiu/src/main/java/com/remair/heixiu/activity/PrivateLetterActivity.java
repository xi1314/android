package com.remair.heixiu.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.Bundler;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.remair.heixiu.R;
import com.remair.heixiu.fragment.ShowFriendListFragment;
import com.remair.heixiu.giftview.NoScrollViewPager;
import studio.archangel.toolkitv2.util.ui.SelectorProvider;

/**
 * Created by wsk on 16/2/27.
 */
public class PrivateLetterActivity extends FragmentActivity {
    @BindView(R.id.activity_priletter_pager_tab)
    SmartTabLayout tab;
    @BindView(R.id.activity_priletter_pager)
    NoScrollViewPager pager;
    @BindView(R.id.ib_back)
    ImageView ib_back;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_priletters);
        ButterKnife.bind(this);

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("已关注", ShowFriendListFragment.class, new Bundler().putInt("type", 0).get())
                .add("未关注", ShowFriendListFragment.class, new Bundler().putInt("type", 1).get())
                .create());
        pager.setAdapter(adapter);
        tab.setDefaultTabTextColor(SelectorProvider.getColorStateList(getResources().getColor(R.color.white), getResources().getColor(R.color.white)));
        tab.setViewPager(pager);
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.fade_out);
    }
}
