package com.remair.heixiu.fragment;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.Bundler;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.SearchActivity;
import com.remair.heixiu.fragment.base.HXBaseFragment;
import com.remair.heixiu.utils.RxViewUtil;
import studio.archangel.toolkitv2.util.ui.SelectorProvider;

/**
 * Created by Michael
 * home tab in home page
 */
public class HomeFragment extends HXBaseFragment {
    public int mCurrentItem;
    @BindView(R.id.frag_home_pager_tab) SmartTabLayout frag_home_pager_tab;
    @BindView(R.id.frag_home_pager) ViewPager pager;
    @BindView(R.id.title_left_image) ImageButton title_left_image;
    @BindView(R.id.title_right_image) ImageButton title_right_image;
    private boolean isGrid = true;


    @Override
    public void onPause() {
        super.onPause();
        mCurrentItem = pager.getCurrentItem();
    }


    @Override
    public View getRootView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.frag_home, container, false);
        ButterKnife.bind(this, view);
        title_left_image.setImageResource(R.drawable.icon_search_wihte);
        title_right_image.setVisibility(View.VISIBLE);
        title_right_image.setImageResource(R.drawable.change_layout);
        return view;
    }


    @Override
    public void initData() {
        final FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getChildFragmentManager(), FragmentPagerItems
                .with(getContext())
                .add("热门", ShowListFragmentcopy.class, new Bundler()
                        .putInt("type", 0).get())
                .add("关注", ConcernListFragment.class, new Bundler()
                        .putInt("type", 1).get())
                .add("最新", ShowListFragment.class, new Bundler()
                        .putInt("type", 2).get()).create());
        pager.setAdapter(adapter);

        frag_home_pager_tab.setDefaultTabTextColor(SelectorProvider
                .getColorStateList(getResources()
                        .getColor(R.color.white), getResources()
                        .getColor(R.color.white)));
        frag_home_pager_tab.setViewPager(pager);
        pager.setCurrentItem(mCurrentItem);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }


            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mCurrentItem = 0;
                    title_right_image.setVisibility(View.VISIBLE);
                    title_right_image
                            .setImageResource(R.drawable.change_layout);
                } else if (position == 1) {
                    mCurrentItem = 1;
                    title_right_image.setVisibility(View.INVISIBLE);
                } else if (position == 2) {
                    mCurrentItem = 2;
                    title_right_image.setVisibility(View.INVISIBLE);
                }
            }


            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        RxViewUtil.viewBindClick(title_left_image, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getContext(), SearchActivity.class);
                startActivity(it);
            }
        });
        final ShowListFragmentcopy showListFragmentcopy = (ShowListFragmentcopy) adapter
                .getItem(0);
        RxViewUtil.viewBindClick(title_right_image, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (HXApp.getInstance().ListOrGrid) {
                    showListFragmentcopy.onClick();
                    HXApp.getInstance().ListOrGrid = false;
                    title_right_image.setImageResource(R.drawable.change);
                } else {
                    showListFragmentcopy.onClick();
                    HXApp.getInstance().ListOrGrid = true;
                    title_right_image
                            .setImageResource(R.drawable.change_layout);
                }
            }
        });
    }
}
