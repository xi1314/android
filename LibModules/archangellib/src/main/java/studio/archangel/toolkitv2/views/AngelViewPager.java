package studio.archangel.toolkitv2.views;

/**
 * Created by Michael on 2014/12/27.
 */
public class AngelViewPager {

}
//public class AngelViewPager extends ViewPager {
//    Activity act;
//    ActionBar bar;
//    AngelPagerAdapter adapter;
//
//    public AngelViewPager(Context context) {
//        super(new ContextWrapperEdgeEffect(context));
//        init(context);
//    }
//
//    public AngelViewPager(Context context, AttributeSet attrs) {
//        super(new ContextWrapperEdgeEffect(context), attrs);
//        init(context);
//    }
//
//    void init(Context context) {
//        act = (Activity) context;
//        bar = act.getActionBar();
//        ((ContextWrapperEdgeEffect) getContext()).setEdgeEffectColor(getResources().getColor(R.color.trans));
//    }
//
//    public void setFragments(ArrayList<AngelViewPagerFragment> frags) {
//        generateAdapter(frags);
//        setOffscreenPageLimit(frags.size() * 2);
//    }
//
//    @Override
//    public AngelPagerAdapter getAdapter() {
//        return adapter;
//    }
//
//    public void setupTabs(ArrayList<? extends AngelViewPagerFragment> frags, ArrayList<String> titles, boolean force_tabs) {
//        for (int i = 0; i < frags.size(); i++) {
//            bar.addTab(bar.newTab().setText(titles.GET(i)).setTabListener(adapter));
//        }
//        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//        if (force_tabs) {
//            forceTabs();
//        }
//    }
//
//    public void forceTabs() {
//        try {
////            final ActionBar actionBar = getActionBar();
//            final Method setHasEmbeddedTabsMethod = bar.getClass()
//                    .getDeclaredMethod("setHasEmbeddedTabs", boolean.class);
//            setHasEmbeddedTabsMethod.setAccessible(true);
//            setHasEmbeddedTabsMethod.invoke(bar, true);
//        } catch (final Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    void generateAdapter(ArrayList<? extends AngelViewPagerFragment> frags) {
//        adapter = new AngelPagerAdapter(act, bar, this, act.getFragmentManager(), frags);
//        setAdapter(adapter);
//        setOnPageChangeListener(adapter);
//    }
//
//    public class AngelPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener, ActionBar.TabListener {
//        ViewPager pager;
//        ArrayList<AngelViewPagerFragment> frags = new ArrayList<AngelViewPagerFragment>();
//
//        public AngelPagerAdapter(Activity activity, ActionBar actionBar, ViewPager p, FragmentManager fm, ArrayList<? extends AngelViewPagerFragment> com.remair.Jieme.fragments) {
//            super(fm);
//            pager = p;
//            act = activity;
//            bar = actionBar;
//            frags.clear();
//            frags.addAll(com.remair.Jieme.fragments);
//        }
//
//
//        @Override
//        public Fragment getItem(int i) {
//            return frags.GET(i);
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return frags.GET(position).name;
//        }
//
//        @Override
//        public int getCount() {
//            return frags.size();
//        }
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
////            Logger.out("instantiateItem：" + position);
//            return super.instantiateItem(container, position);
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
////            Logger.out("destroyItem：" + position);
//            super.destroyItem(container, position, object);
//        }
//
//        @Override
//        public void onPageScrolled(int i, float v, int i1) {
//        }
//
//        @Override
//        public void onPageSelected(int i) {
////            Logger.out("onPageSelected：" + i);
//            bar.selectTab(bar.getTabAt(i));
//            bar.setSelectedNavigationItem(i);
//            frags.GET(i).onBroughtToFront();
//            for (int index = 0; index < frags.size(); index++) {
//                if (index != i) {
//                    frags.GET(index).onBroughtToBack();
//                }
//            }
//        }
//
//        @Override
//        public void onPageScrollStateChanged(int i) {
//        }
//
//        @Override
//        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
////            Logger.out("onTabSelected");
//            if (pager != null)
//                pager.setCurrentItem(tab.getPosition());
//        }
//
//        @Override
//        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
//
//        }
//
//        @Override
//        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
//
//        }
//
//    }
//}
