package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments.JoinAttackInfoFragment;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_join_attack.JoinAttackViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_join_attack.JoinAttackViewMvcImp;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack.Extra.EXTRA_ATTACK;

public class JoinAttackActivity extends AppCompatActivity {

    private JoinAttackViewMvc viewMvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUi();
        showJoinAttackInfoFragment();
    }

    private void setupUi() {
        viewMvc = new JoinAttackViewMvcImp(LayoutInflater.from(this), null);
        setSupportActionBar(viewMvc.getToolbar());
        setContentView(viewMvc.getRootView());
    }

    private void showJoinAttackInfoFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(viewMvc.getFragmentContainerId(),JoinAttackInfoFragment.getInstance(getIntent().getExtras()))
                .commit();
    }
}
