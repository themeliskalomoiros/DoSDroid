package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_join_attack;

import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import gr.kalymnos.sk3m3l10.ddosdroid.R;

public class JoinAttackViewMvcImp implements JoinAttackViewMvc {
    private View root;
    private Toolbar toolbar;
    private FrameLayout fragmentContainer;

    public JoinAttackViewMvcImp(LayoutInflater inflater, ViewGroup container) {
        root = inflater.inflate(R.layout.screen_join_attack, container, false);
        toolbar = root.findViewById(R.id.toolBar);
        fragmentContainer = root.findViewById(R.id.fragment_container);
    }

    @Override
    public int getFragmentContainerId() {
        return fragmentContainer.getId();
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
