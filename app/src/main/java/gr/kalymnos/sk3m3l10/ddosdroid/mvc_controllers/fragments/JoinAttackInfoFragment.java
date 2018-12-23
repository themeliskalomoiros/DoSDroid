package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.client.Client;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.client.OnOwnerAttackResponseReceiveListener;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.repository.AttackRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.repository.FirebaseRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_join_attack.JoinAttackInfoViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_join_attack.JoinAttackInfoViewMvcImp;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attacks;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.NetworkTypeTranslator;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.bot.Bots;
import gr.kalymnos.sk3m3l10.ddosdroid.utils.DateFormatter;

public class JoinAttackInfoFragment extends Fragment
        implements JoinAttackInfoViewMvc.OnJoinAttackButtonClickListener,
        Client.OnConnectionListener, OnOwnerAttackResponseReceiveListener {

    private JoinAttackInfoViewMvc viewMvc;
    private Attack attack;
    private Client client;
    private AttackRepository attackRepository;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeFieldsExceptViewMvc();
    }

    private void initializeFieldsExceptViewMvc() {
        attack = getArguments().getParcelable(Constants.Extra.EXTRA_ATTACK);
        attackRepository = new FirebaseRepository();
        client = new Client.ClientFactoryImp()
                .createClient(getContext(), this, attack.getNetworkType());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initializeViewMvc(inflater, container);
        bindAttackToUi();
        return viewMvc.getRootView();
    }

    private void initializeViewMvc(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        viewMvc = new JoinAttackInfoViewMvcImp(inflater, container);
        viewMvc.setOnJoinAttackClickListener(this);
    }

    private void bindAttackToUi() {
        viewMvc.bindAttackForce(attack.getBotIds().size());
        viewMvc.bindNetworkConfiguration(NetworkTypeTranslator.translate(attack.getNetworkType()));
        viewMvc.bindWebsite(attack.getWebsite());
        viewMvc.bindWebsiteDate(DateFormatter.getDate(getContext().getResources().getConfiguration(), attack.getTimeMillis()));
    }

    @Override
    public void onJoinAttackButtonClicked() {
        if (!client.isConnected()) {
            client.connect();
        } else {
            startJoinProcedure();
        }
    }

    private void startJoinProcedure() {
        Attacks.addBot(attack, Bots.getLocalUser());
        attackRepository.updateAttack(attack);
    }

    @Override
    public void onAttackNetworkConnected() {
        startJoinProcedure();
    }

    @Override
    public void onAttackNetworkDisconnected(CharSequence reason) {
        Toast.makeText(getContext(), "Client disconnected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onOwnerAttackResponseReceived(boolean attackEnabled) {

    }

    public static JoinAttackInfoFragment getInstance(Bundle args) {
        JoinAttackInfoFragment instance = new JoinAttackInfoFragment();
        instance.setArguments(args);
        return instance;
    }
}
