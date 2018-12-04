package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_phase_screen;

import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import gr.kalymnos.sk3m3l10.ddosdroid.R;

public class AttackInfoViewMvcImpl implements AttackInfoViewMvc {

    private View root;
    private TextView tvWebsite, tvWebsiteHint, tvAttackForce, tvAttackForceHint, tvBeginOrStopAttack;
    private FloatingActionButton fab;

    private OnBeginAttackButtonClickListener onBeginAttackButtonClickListener;

    public AttackInfoViewMvcImpl(LayoutInflater inflater, ViewGroup container) {
        initializeViews(inflater, container);
    }

    @Override
    public void bindWebsite(String website) {
        tvWebsite.setText(website);
    }

    @Override
    public void bindWebsiteHint(String text) {
        tvWebsiteHint.setText(text);
    }

    @Override
    public void bindAttackForceText(String text) {
        tvAttackForce.setText(text);
    }

    @Override
    public void bindAttackForceHint(String text) {
        tvAttackForceHint.setText(text);
    }

    @Override
    public void bindBeginOrStopAttackHeader(String text) {
        tvBeginOrStopAttack.setText(text);
    }

    @Override
    public void setOnBeginAttacButtonClickListener(OnBeginAttackButtonClickListener listener) {
        onBeginAttackButtonClickListener = listener;
    }

    @Override
    public void setFabIconToSwords() {
        fab.setImageResource(R.drawable.ic_swords);
    }

    @Override
    public void setFabIconToStop() {
        fab.setImageResource(R.drawable.ic_stop);
    }

    @Override
    public View getRootView() {
        return root;
    }

    private void initializeViews(LayoutInflater inflater, ViewGroup container) {
        root = inflater.inflate(R.layout.screen_part_attack_info, container, false);
        tvWebsite = root.findViewById(R.id.website_textview);
        tvWebsiteHint = root.findViewById(R.id.tv_website_hint);
        tvAttackForce = root.findViewById(R.id.tv_attack_force);
        tvAttackForceHint = root.findViewById(R.id.tv_attack_force_hint);
        tvBeginOrStopAttack = root.findViewById(R.id.tv_begin_stop_attack);
        initializeFab();
    }

    private void initializeFab() {
        fab = root.findViewById(R.id.fab);
        fab.setOnClickListener((view -> {
            if (onBeginAttackButtonClickListener != null) {
                onBeginAttackButtonClickListener.onBeginAttackButtonClick();
            }
        }));
    }
}
