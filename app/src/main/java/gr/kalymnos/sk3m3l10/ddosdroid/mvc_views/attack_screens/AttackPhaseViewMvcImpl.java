package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_screens;

import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import gr.kalymnos.sk3m3l10.ddosdroid.R;

public class AttackPhaseViewMvcImpl implements AttackPhaseViewMvc {

    private View root;
    private Toolbar toolbar;
    private FrameLayout fragmentContainer;

    public AttackPhaseViewMvcImpl(LayoutInflater inflater, ViewGroup container) {
        initializeViews(inflater, container);
    }

    @Override
    public void bindToolbarSubtitle(CharSequence subtitle) {
        toolbar.setSubtitle(subtitle);
    }

    @Override
    public View getFragmentContainer() {
        return fragmentContainer;
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
        root = inflater.inflate(R.layout.screen_attack_phase, container, false);
        toolbar = root.findViewById(R.id.toolBar);
        fragmentContainer = root.findViewById(R.id.fragment_container);
    }
}
