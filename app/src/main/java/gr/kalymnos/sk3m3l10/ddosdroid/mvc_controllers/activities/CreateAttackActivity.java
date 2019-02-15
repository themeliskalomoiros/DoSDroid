package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments.AttackCreationFragment;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.ServerHost;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_phase.AttackPhaseViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_phase.AttackPhaseViewMvcImp;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

public class CreateAttackActivity extends AppCompatActivity implements
        AttackCreationFragment.OnAttackCreationListener {

    private AttackPhaseViewMvc viewMvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUi();
        getSupportFragmentManager().beginTransaction()
                .replace(viewMvc.getFragmentContainerId(), new AttackCreationFragment())
                .commit();
    }

    private void setupUi() {
        viewMvc = new AttackPhaseViewMvcImp(LayoutInflater.from(this), null);
        setContentView(viewMvc.getRootView());
    }

    @Override
    public void onAttackCreated(Attack attack) {
        ServerHost.Action.startServer(this, attack);
        finish();
    }
}
