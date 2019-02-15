package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_lists;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gr.kalymnos.sk3m3l10.ddosdroid.R;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments.attack_list.AttackListFragment;

public class AllAttackListsViewMvcImpl implements AllAttackListsViewMvc {
    private View root;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private MyPagerAdapter pagerAdapter;

    public AllAttackListsViewMvcImpl(LayoutInflater inflater, ViewGroup container, FragmentManager fragmentManager, String[] tabTitles, int contentType) {
        initializeFields(inflater, container, fragmentManager, tabTitles, contentType);
        setupViewPager();
    }

    private void initializeFields(LayoutInflater inflater, ViewGroup container, FragmentManager fragmentManager, String[] tabTitles, int contentType) {
        pagerAdapter = new MyPagerAdapter(fragmentManager, tabTitles, contentType);
        initializeViews(inflater, container);
    }

    private void initializeViews(LayoutInflater inflater, ViewGroup container) {
        root = inflater.inflate(R.layout.screen_all_attack_lists, container, false);
        toolbar = root.findViewById(R.id.toolBar);
        viewPager = root.findViewById(R.id.viewPager);
    }

    private void setupViewPager() {
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = root.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void bindToolbarSubtitle(String subtitle) {
        toolbar.setSubtitle(subtitle);
    }

    @Override
    public void bindToolbarTitle(String title) {
        toolbar.setTitle(title);
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public View getRootView() {
        return root;
    }

    private static class MyPagerAdapter extends FragmentPagerAdapter {
        private String[] titles;
        private int contentType;

        public MyPagerAdapter(FragmentManager fm, String[] titles, int contentType) {
            super(fm);
            this.titles = titles;
            this.contentType = contentType;
        }

        @Override
        public Fragment getItem(int position) {
            AttackListFragment.Builder builder = new AttackListFragment.BuilderImp();
            return builder.build(titles[position], contentType);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int i) {
            return titles[i];
        }

        @Override
        public int getCount() {
            return titles.length;
        }
    }
}
