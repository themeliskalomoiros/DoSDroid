package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities.attack_creation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments.AttackCreationFragment;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.host_services.ServerHost;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_phase.AttackPhaseViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_phase.AttackPhaseViewMvcImp;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

public class CreateAttackActivity extends AppCompatActivity implements AttackCreationFragment.OnAttackCreationListener {
    private AttackPhaseViewMvc viewMvc;
    private AttackCreationFragment creationFragment;
    private DatePicker datePicker;
    private TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFragments();
        viewMvc = new AttackPhaseViewMvcImp(LayoutInflater.from(this), null);
        setupUiFrom(viewMvc);
    }

    private void initFragments() {
        creationFragment = new AttackCreationFragment();
        datePicker = new DatePicker();
        timePicker = new TimePicker();
    }

    private void setupUiFrom(AttackPhaseViewMvc viewMvc) {
        setContentView(viewMvc.getRootView());
        showAttackCreationFragment(viewMvc);
    }

    private void showAttackCreationFragment(AttackPhaseViewMvc viewMvc) {
        getSupportFragmentManager().beginTransaction()
                .replace(viewMvc.getFragmentContainerId(), creationFragment)
                .commit();
    }

    @Override
    public void onAttackCreated(Attack attack) {
        ServerHost.Action.startServerOf(attack, this);
        finish();
    }

    @Override
    public void onDatePickerClicked() {
        datePicker.show(getSupportFragmentManager(), DatePicker.TAG);
    }

    @Override
    public void onTimePickerClicked() {
        timePicker.show(getSupportFragmentManager(), TimePicker.TAG);
    }
}
