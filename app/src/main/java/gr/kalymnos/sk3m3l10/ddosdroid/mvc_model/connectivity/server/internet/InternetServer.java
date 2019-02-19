package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.server.internet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v4.content.LocalBroadcastManager;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.server.Server;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.server.status.ServerStatusBroadcaster;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.bot.Bots;

import static gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras.EXTRA_ATTACK_HOST_UUID;
import static gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras.EXTRA_ATTACK_STARTED;

public class InternetServer extends Server {
    private BroadcastReceiver connectivityReceiver;

    public InternetServer(Context context, Attack attack) {
        super(context, attack);
        initConnectivityReceiver();
    }

    private void initConnectivityReceiver() {
        //  Is registered only after a successful start, which happens in onConstraintsResolved()
        connectivityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (disconnectionHappened(intent)) {
                    ServerStatusBroadcaster.broadcastStopped(getAttackingWebsite(), LocalBroadcastManager.getInstance(context));
                }
            }

            private boolean disconnectionHappened(Intent intent) {
                return intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)
                        && intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            }
        };
    }

    @Override
    public void start() {
        constraintsResolver.resolveConstraints();
    }

    @Override
    public void stop() {
        context.unregisterReceiver(connectivityReceiver);
        super.stop();
    }

    @Override
    public void onConstraintsResolved() {
        ServerStatusBroadcaster.broadcastRunning(getAttackingWebsite(), LocalBroadcastManager.getInstance(context));
        uploadAttack();
        registerReceiver();
    }

    private void uploadAttack() {
        attack.addSingleHostInfo(EXTRA_ATTACK_HOST_UUID, Bots.local().getId());
        attack.addSingleHostInfo(EXTRA_ATTACK_STARTED, Server.RESPONSE);
        repo.upload(attack);
    }

    private void registerReceiver() {
        context.registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onConstraintResolveFailure() {
        ServerStatusBroadcaster.broadcastError(getAttackingWebsite(), LocalBroadcastManager.getInstance(context));
    }
}
