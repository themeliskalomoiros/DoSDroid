package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_phase;

import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gr.kalymnos.sk3m3l10.ddosdroid.R;

public class AttackPhaseViewMvcImp implements AttackPhaseViewMvc {
    private View root;
    private Toolbar toolbar;

    public AttackPhaseViewMvcImp(LayoutInflater inflater, ViewGroup container) {
        root = inflater.inflate(R.layout.screen_attack_phase, container, false);
        toolbar = root.findViewById(R.id.toolBar);
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
}
