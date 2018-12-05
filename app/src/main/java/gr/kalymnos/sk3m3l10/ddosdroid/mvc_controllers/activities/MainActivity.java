package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import gr.kalymnos.sk3m3l10.ddosdroid.R;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_main.MainScreenViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_main.MainScreenViewMvcImpl;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.AttackConstants.AttackType.ATTACK_TYPE_KEY;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.AttackConstants.AttackType.TYPE_FETCH_JOINED;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.AttackConstants.AttackType.TYPE_FETCH_NOT_JOINED;

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
        startActivity(createAttackListsActivityIntent(TYPE_FETCH_NOT_JOINED,R.string.join_attack_label));
    }

    @Override
    public void onContributionClick() {
        startActivity(createAttackListsActivityIntent(TYPE_FETCH_JOINED, R.string.contributions_label));
    }

    @NonNull
    private Intent createAttackListsActivityIntent(int attackType, int titleRes) {
        Intent intent = new Intent(this, AllAttackListsActivity.class);
        intent.putExtras(createAttackListsActivityIntentBundle(attackType, getString(titleRes)));
        return intent;
    }

    @NonNull
    private Bundle createAttackListsActivityIntentBundle(int attackType, String title) {
        Bundle extras = new Bundle();
        extras.putInt(ATTACK_TYPE_KEY, attackType);
        extras.putString(AllAttackListsActivity.EXTRA_TITLE, title);
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
