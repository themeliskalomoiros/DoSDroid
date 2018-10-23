package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments.AttackCreationFragment;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_screens.AttackPhaseViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_screens.AttackPhaseViewMvcImpl;

public class AttackPhaseActivity extends AppCompatActivity {

    private AttackPhaseViewMvc viewMvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewMvc = new AttackPhaseViewMvcImpl(LayoutInflater.from(this),null);
        getSupportFragmentManager().beginTransaction().add(viewMvc.getFragmentContainerId(),new AttackCreationFragment()).commit();
        setContentView(viewMvc.getRootView());
    }
}
