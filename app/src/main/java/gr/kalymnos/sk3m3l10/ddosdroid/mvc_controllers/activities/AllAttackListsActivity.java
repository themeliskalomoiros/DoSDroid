package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_lists.AllAttackListsViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_lists.AllAttackListsViewMvcImpl;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack.AttackType.ATTACK_TYPE_KEY;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack.AttackType.TYPE_NONE;
import static gr.kalymnos.sk3m3l10.ddosdroid.utils.ValidationUtils.bundleIsValidAndContainsKey;

public class AllAttackListsActivity extends AppCompatActivity {
    private static final String TAG = "AllAttackListsActivity";
    public static final String EXTRA_TITLE = TAG + "title key";

    private AllAttackListsViewMvc viewMvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUi();
    }

    private void setupUi() {
        initializeViewMvc();
        viewMvc.bindToolbarTitle(getIntent().getStringExtra(EXTRA_TITLE));
        setSupportActionBar(viewMvc.getToolbar());
        setContentView(viewMvc.getRootView());
    }

    private void initializeViewMvc() {
        viewMvc = new AllAttackListsViewMvcImpl(LayoutInflater.from(this), null,
                getSupportFragmentManager(), getAttacksType(getIntent().getExtras()));
    }

    private int getAttacksType(Bundle bundle) {
        if (bundleIsValidAndContainsKey(bundle, ATTACK_TYPE_KEY)) {
            return bundle.getInt(ATTACK_TYPE_KEY);
        }
        return TYPE_NONE;
    }
}
