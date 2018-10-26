package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_lists_screen;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gr.kalymnos.sk3m3l10.ddosdroid.R;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments.AttackListFragment;

public class AllAttackListsViewMvcImpl implements AllAttackListsViewMvc {

    private View root;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private MyPagerAdapter pagerAdapter;

    public AllAttackListsViewMvcImpl(LayoutInflater inflater, ViewGroup container, FragmentManager fragmentManager) {
        initializeViews(inflater, container, fragmentManager);
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

    private void initializeViews(LayoutInflater inflater, ViewGroup container, FragmentManager fragmentManager) {
        root = inflater.inflate(R.layout.screen_attack_lists, container, false);
        toolbar = root.findViewById(R.id.toolBar);
        initializeViewPager(fragmentManager);
    }

    private void initializeViewPager(FragmentManager fragmentManager) {
        String[] tabTitles = root.getContext().getResources().getStringArray(R.array.network_technologies_titles);
        pagerAdapter = new MyPagerAdapter(fragmentManager, tabTitles);
        viewPager = root.findViewById(R.id.viewPager);
        viewPager.setAdapter(pagerAdapter);
    }

    private static class MyPagerAdapter extends FragmentPagerAdapter {

        private String[] titles;

        public MyPagerAdapter(FragmentManager fm, String[] titles) {
            super(fm);
            this.titles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            return new AttackListFragment();
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
