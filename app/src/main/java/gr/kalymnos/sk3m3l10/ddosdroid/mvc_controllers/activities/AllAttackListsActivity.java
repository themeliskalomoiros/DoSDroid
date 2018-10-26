package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_lists_screen.AllAttackListsViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_lists_screen.AllAttackListsViewMvcImpl;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack.ATTACK_TYPE_KEY;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack.TYPE_NONE;
import static gr.kalymnos.sk3m3l10.ddosdroid.utils.ValidationUtils.bundleIsValidAndContainsKey;

public class AllAttackListsActivity extends AppCompatActivity {

    private AllAttackListsViewMvc viewMvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeViewMvc(savedInstanceState);
        setContentView(viewMvc.getRootView());
    }

    private void initializeViewMvc(Bundle savedInstanceState) {
        viewMvc = new AllAttackListsViewMvcImpl(LayoutInflater.from(this), null,
                getSupportFragmentManager(), getAttacksType(getIntent().getExtras()));
        setSupportActionBar(viewMvc.getToolbar());
    }

    private int getAttacksType(Bundle bundle) {
        if (bundleIsValidAndContainsKey(bundle, ATTACK_TYPE_KEY)) {
            return bundle.getInt(ATTACK_TYPE_KEY);
        }
        return TYPE_NONE;
    }
}
