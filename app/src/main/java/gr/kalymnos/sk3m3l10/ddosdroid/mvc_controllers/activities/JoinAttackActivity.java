package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import gr.kalymnos.sk3m3l10.ddosdroid.R;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments.JoinAttackInfoFragment;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.client.Client;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.repository.AttackRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.repository.FirebaseRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_join_attack.JoinAttackViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_join_attack.JoinAttackViewMvcImp;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attacks;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.bot.Bots;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.Extra.EXTRA_ATTACK;

public class JoinAttackActivity extends AppCompatActivity implements Client.OnConnectionListener,
        JoinAttackInfoFragment.OnJoinAttackButtonClickListener {
    private JoinAttackViewMvc viewMvc;
    private AttackRepository repo;
    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeFields();
        setupUi();
    }

    private void initializeFields() {
        client = new Client.ClientFactoryImp().createClient(this, this, 0);
        repo = new FirebaseRepository();
        viewMvc = new JoinAttackViewMvcImp(LayoutInflater.from(this), null);
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
    protected void onDestroy() {
        super.onDestroy();
        client.unregisterConnectionListener();
    }

    @Override
    public void onAttackNetworkConnected() {
        Snackbar.make(viewMvc.getRootView(), R.string.server_connection_msg, Snackbar.LENGTH_SHORT)
                .show();
        startJoinProcedure();
    }

    @Override
    public void onAttackNetworkDisconnected(CharSequence reason) {
        Snackbar.make(viewMvc.getRootView(), R.string.server_disconnection_msg, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void onJoinAttackButtonClicked() {
        client.connect();
    }

    private void startJoinProcedure() {
        Attacks.addBot(getAttack(), Bots.getLocalUser());
        repo.updateAttack(getAttack());
    }

    private Attack getAttack() {
        return getIntent().getParcelableExtra(EXTRA_ATTACK);
    }
}
