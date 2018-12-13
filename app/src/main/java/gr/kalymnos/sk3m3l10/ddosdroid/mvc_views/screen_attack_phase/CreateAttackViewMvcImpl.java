package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_phase;

import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import gr.kalymnos.sk3m3l10.ddosdroid.R;

public class CreateAttackViewMvcImpl implements CreateAttackViewMvc {

    private View root;
    private Toolbar toolbar;

    public CreateAttackViewMvcImpl(LayoutInflater inflater, ViewGroup container) {
        initializeViews(inflater, container);
    }

    @Override
    public void bindToolbarSubtitle(CharSequence subtitle) {
        toolbar.setSubtitle(subtitle);
    }

    @Override
    public int getFragmentContainerId() {
        return R.id.fragment_container;
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
        root = inflater.inflate(R.layout.screen_create_attack, container, false);
        toolbar = root.findViewById(R.id.toolBar);
    }
}
