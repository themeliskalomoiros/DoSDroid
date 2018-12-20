package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.Toast;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments.AttackCreationFragment;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments.AttackInfoFragment;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.ServersHost;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_phase.CreateAttackViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_phase.CreateAttackViewMvcImpl;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

public class CreateAttackActivity extends AppCompatActivity implements AttackInfoFragment.OnBeginAttackButtonClickListener,
        AttackCreationFragment.OnAttackCreationListener {

    private CreateAttackViewMvc viewMvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUi();
        getSupportFragmentManager().beginTransaction()
                .replace(viewMvc.getFragmentContainerId(), new AttackCreationFragment())
                .commit();
    }

    @Override
    public void onBeginAttackButtonClick(Attack attack) {

    }

    @Override
    public void onAttackCreated(Attack attack) {
        getSupportFragmentManager().beginTransaction()
                .replace(viewMvc.getFragmentContainerId(), AttackInfoFragment.Builder.build(attack))
                .commit();
    }

    private void setupUi() {
        viewMvc = new CreateAttackViewMvcImpl(LayoutInflater.from(this), null);
        setContentView(viewMvc.getRootView());
    }
}
