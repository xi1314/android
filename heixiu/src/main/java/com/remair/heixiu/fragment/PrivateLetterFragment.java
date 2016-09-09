package com.remair.heixiu.fragment;

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
import com.remair.heixiu.R;
import com.remair.heixiu.fragment.base.HXBaseFragment;
import studio.archangel.toolkitv2.util.ui.SelectorProvider;

/**
 * Created by wsk on 16/2/27.
 */
public class PrivateLetterFragment extends HXBaseFragment {
    @BindView(R.id.frag_home_pager_tab) SmartTabLayout frag_private_pager_tab;
    @BindView(R.id.frag_home_pager) ViewPager pager;
    @BindView(R.id.title_left_image) ImageButton title_left_image;
    @BindView(R.id.title_right_image) ImageButton title_right_image;
    View rootView;


    @Override
    public View getRootView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.frag_home, container, false);
        ButterKnife.bind(this, view);
        title_left_image.setVisibility(View.INVISIBLE);
        return view;
    }


    @Override
    public void initData() {
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getChildFragmentManager(), FragmentPagerItems
                .with(getContext())
                .add(getString(R.string.concern), ShowFriendListFragment.class, new Bundler()
                        .putInt("type", 0).get())
                .add(getString(R.string.unconcern), ShowFriendListFragment.class, new Bundler()
                        .putInt("type", 1).get()).create());
        if (pager == null) {
            if (rootView != null) {
                pager = (ViewPager) rootView.findViewById(R.id.frag_home_pager);
            } else {
                return;
            }
        }
        pager.setAdapter(adapter);
        frag_private_pager_tab.setDefaultTabTextColor(SelectorProvider
                .getColorStateList(getResources()
                        .getColor(R.color.white), getResources()
                        .getColor(R.color.white)));

        frag_private_pager_tab.setViewPager(pager);
    }
}
