package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras;
import gr.kalymnos.sk3m3l10.ddosdroid.constants.Special;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments.JoinAttackInfoFragment;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.client.ClientHost;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_join_attack.JoinAttackViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_join_attack.JoinAttackViewMvcImp;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

public class JoinAttackActivity extends AppCompatActivity implements JoinAttackInfoFragment.OnJoinAttackButtonClickListener {
    private JoinAttackViewMvc viewMvc;

    public static void startAnInstance(Context context, Attack attack) {
        Intent intent = getIntent(context, attack);
        context.startActivity(intent);
    }

    @NonNull
    private static Intent getIntent(Context context, Attack attack) {
        //  Why this way? see constants.Special.BUNDLE_SAMSUNG_BUG_KEY for details
        Bundle bundle = new Bundle();
        bundle.putParcelable(Extras.EXTRA_ATTACK, attack);
        Intent intent = new Intent(context, JoinAttackActivity.class);
        intent.putExtra(Special.BUNDLE_SAMSUNG_BUG_KEY, bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewMvc = new JoinAttackViewMvcImp(LayoutInflater.from(this), null);
        setupUi();
    }

    private void setupUi() {
        setSupportActionBar(viewMvc.getToolbar());
        setContentView(viewMvc.getRootView());
        showJoinAttackInfoFragment();
    }

    private void showJoinAttackInfoFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(viewMvc.getFragmentContainerId(), JoinAttackInfoFragment.getInstance(getIntent().getExtras()))
                .commit();
    }

    @Override
    public void onJoinAttackButtonClicked(Attack attack) {
        ClientHost.Action.createClientOf(attack, this);
        finish();
    }
}
