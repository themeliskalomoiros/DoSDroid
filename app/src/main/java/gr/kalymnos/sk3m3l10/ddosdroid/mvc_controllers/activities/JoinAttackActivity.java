package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments.JoinAttackInfoFragment;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.client.Client;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_join_attack.JoinAttackViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_join_attack.JoinAttackViewMvcImp;

public class JoinAttackActivity extends AppCompatActivity implements Client.OnConnectionListener {

    private JoinAttackViewMvc viewMvc;
    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUi();
        client = new Client.ClientFactoryImp().createClient(this, this, 0);
        showJoinAttackInfoFragment();
    }

    private void setupUi() {
        viewMvc = new JoinAttackViewMvcImp(LayoutInflater.from(this), null);
        setSupportActionBar(viewMvc.getToolbar());
        setContentView(viewMvc.getRootView());
    }

    private void showJoinAttackInfoFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(viewMvc.getFragmentContainerId(), JoinAttackInfoFragment.getInstance(getIntent().getExtras()))
                .commit();
    }

    @Override
    public void onAttackNetworkConnected() {

    }

    @Override
    public void onAttackNetworkDisconnected(CharSequence reason) {

    }
}
