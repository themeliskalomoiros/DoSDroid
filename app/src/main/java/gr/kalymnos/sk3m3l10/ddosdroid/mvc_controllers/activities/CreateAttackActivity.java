package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.Toast;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments.AttackCreationFragment;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments.AttackInfoFragment;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_phase_screen.CreateAttackViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_phase_screen.CreateAttackViewMvcImpl;

public class CreateAttackActivity extends AppCompatActivity implements AttackInfoFragment.OnBeginAttackButtonClickListener,
        AttackCreationFragment.OnAttackCreationButtonClickListener {

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
    public void onAttackCreationButtonClicked(String website) {
        getSupportFragmentManager().beginTransaction()
                .replace(viewMvc.getFragmentContainerId(), AttackInfoFragment.getInstance(website))
                .commit();
    }

    @Override
    public void onBeginAttackButtonClick() {
        Toast.makeText(this, "attack begin", Toast.LENGTH_SHORT).show();
    }

    private void setupUi() {
        viewMvc = new CreateAttackViewMvcImpl(LayoutInflater.from(this), null);
        setContentView(viewMvc.getRootView());
    }
}
