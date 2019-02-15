package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_join_attack;

import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import gr.kalymnos.sk3m3l10.ddosdroid.R;

public class JoinAttackInfoViewMvcImp implements JoinAttackInfoViewMvc {

    private View root;
    private TextView website, date, attackForce, networkConf;
    private FloatingActionButton joinFab;

    public JoinAttackInfoViewMvcImp(LayoutInflater inflater, ViewGroup container) {
        initializeViews(inflater, container);
    }

    @Override
    public void setOnJoinAttackClickListener(OnJoinAttackClickListener listener) {
        joinFab.setOnClickListener(view -> {
            if (listener != null) {
                listener.onJoinAttackClicked();
            }
        });
    }

    @Override
    public void bindWebsite(String website) {
        this.website.setText(website);
    }

    @Override
    public void bindWebsiteDate(String date) {
        String prefix = root.getContext().getResources().getString(R.string.attack_date_prefix);
        this.date.setText(prefix + " " + date);
    }

    @Override
    public void bindAttackForce(int count) {
        attackForce.setText(attackForce.getContext().getString(R.string.people_joined_this_attack) + " " + count);
    }

    @Override
    public void bindNetworkConfiguration(String networkConf) {
        this.networkConf.setText(networkConf);
    }

    @Override
    public View getRootView() {
        return root;
    }

    private void initializeViews(LayoutInflater inflater, ViewGroup container) {
        root = inflater.inflate(R.layout.screen_join_attack_info, container, false);
        website = root.findViewById(R.id.website_textview);
        date = root.findViewById(R.id.tv_website_hint);
        attackForce = root.findViewById(R.id.tv_attack_force);
        networkConf = root.findViewById(R.id.network_configuration);
        joinFab = root.findViewById(R.id.fab);
    }
}
