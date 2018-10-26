package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_lists_screen;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gr.kalymnos.sk3m3l10.ddosdroid.R;

public class AllAttackListsViewMvcImpl implements AllAttackListsViewMvc {

    private View root;
    private Toolbar toolbar;
    private ViewPager viewPager;

    public AllAttackListsViewMvcImpl(LayoutInflater inflater, ViewGroup container) {
        initializeViews(inflater, container);
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

    private void initializeViews(LayoutInflater inflater, ViewGroup container) {
        root = inflater.inflate(R.layout.screen_attack_lists, container, false);
        toolbar = root.findViewById(R.id.toolBar);
        viewPager=root.findViewById(R.id.viewPager);
    }


}
