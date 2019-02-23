package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities.attack_creation;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments.AttackCreationFragment;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.host_services.HostAttackService;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_phase.AttackPhaseViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_phase.AttackPhaseViewMvcImp;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

public class CreateAttackActivity extends AppCompatActivity implements AttackCreationFragment.OnAttackCreationListener,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
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
        HostAttackService.Action.host(attack, this);
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

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        DatePicker.Date date = new DatePicker.Date(year, month, dayOfMonth);
        creationFragment.setDate(date);
    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        TimePicker.Time time = new TimePicker.Time(hourOfDay, minute);
        creationFragment.setTime(time);
    }
}
