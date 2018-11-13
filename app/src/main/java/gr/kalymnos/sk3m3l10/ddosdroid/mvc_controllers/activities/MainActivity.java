package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.main_screen.MainScreenViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.main_screen.MainScreenViewMvcImpl;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack.AttackType.ATTACK_TYPE_KEY;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack.AttackType.TYPE_FETCH_FOLLOWING;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack.AttackType.TYPE_FETCH_NOT_FOLLOWING;

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
        startAllAttackListsActivity(TYPE_FETCH_NOT_FOLLOWING);
    }

    @Override
    public void onFollowingAttacksClick() {
        startAllAttackListsActivity(TYPE_FETCH_FOLLOWING);
    }

    private void startAllAttackListsActivity(int typeFetchFollowing) {
        startActivity(createAllAttackListsActivityIntent(typeFetchFollowing));
    }

    @NonNull
    private Intent createAllAttackListsActivityIntent(int typeFetchFollowing) {
        Bundle extras = new Bundle();
        extras.putInt(ATTACK_TYPE_KEY, typeFetchFollowing);
        Intent intent = new Intent(this, AttackListsActivity.class);
        intent.putExtras(extras);
        return intent;
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
