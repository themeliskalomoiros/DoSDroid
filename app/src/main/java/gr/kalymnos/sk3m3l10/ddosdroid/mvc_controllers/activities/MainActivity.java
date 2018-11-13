package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.main_screen.MainScreenViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.main_screen.MainScreenViewMvcImpl;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack.AttackType.ATTACK_TYPE_KEY;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack.AttackType.TYPE_FETCH_JOINED;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack.AttackType.TYPE_FETCH_NOT_JOINED;

public class MainActivity extends AppCompatActivity implements MainScreenViewMvc.OnOptionClickListener {

    private MainScreenViewMvc viewMvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUi();
    }

    @Override
    public void onCreateAttackClick() {
        startActivity(new Intent(this, CreateAttackActivity.class));
    }

    @Override
    public void onJoinAttackClick() {
        startActivity(createAttackListsActivityIntent(TYPE_FETCH_NOT_JOINED));
    }

    @Override
    public void onContributionClick() {
        startActivity(createAttackListsActivityIntent(TYPE_FETCH_JOINED));
    }

    @NonNull
    private Intent createAttackListsActivityIntent(int attackType) {
        Intent intent = new Intent(this, AllAttackListsActivity.class);
        intent.putExtras(createAttackListsActivityIntentBundle(attackType));
        return intent;
    }

    @NonNull
    private Bundle createAttackListsActivityIntentBundle(int attackType) {
        Bundle extras = new Bundle();
        extras.putInt(ATTACK_TYPE_KEY, attackType);
        return extras;
    }

    private void setupUi() {
        initializeViewMvc();
        setSupportActionBar(viewMvc.getToolbar());
        setContentView(viewMvc.getRootView());
    }

    private void initializeViewMvc() {
        viewMvc = new MainScreenViewMvcImpl(LayoutInflater.from(this), null);
        viewMvc.setOnOptionClickListener(this);
    }
}
