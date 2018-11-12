package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_lists_screen;

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
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments.attack_list.BluetoothAttackListFragment;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments.attack_list.InternetAttackListFragment;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments.attack_list.NSDAttackListFragment;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments.attack_list.WiFiP2PAttackListFragment;

public class AttackListsViewMvcImpl implements AttackListsViewMvc {

    private View root;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private MyPagerAdapter pagerAdapter;

    private int attacksType;

    public AttackListsViewMvcImpl(LayoutInflater inflater, ViewGroup container, FragmentManager fragmentManager, int attacksType) {
        this.attacksType = attacksType;
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
        pagerAdapter = new MyPagerAdapter(fragmentManager, tabTitles, attacksType);
        viewPager = root.findViewById(R.id.viewPager);
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = root.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private static class MyPagerAdapter extends FragmentPagerAdapter {

        private String[] titles;
        private int attacksType;

        public MyPagerAdapter(FragmentManager fm, String[] titles, int attacksType) {
            super(fm);
            this.titles = titles;
            this.attacksType = attacksType;
        }

        @Override
        public Fragment getItem(int position) {
            switch (titles[position]) {
                // These cases are copied from R.arrays.network_technologies_titles
                case "INTERNET":
                    return InternetAttackListFragment.getInstance(attacksType);
                case "WiFi P2P":
                    return WiFiP2PAttackListFragment.getInstance(attacksType);
                case "NSD":
                    return NSDAttackListFragment.getInstance(attacksType);
                case "Bluetooth":
                    return BluetoothAttackListFragment.getInstance(attacksType);
                default:
                    return AttackListFragment.getInstance(attacksType);
            }
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
