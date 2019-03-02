package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.connectivity.server.internet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.connectivity.server.Server;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.bot.Bots;

import static gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras.EXTRA_ATTACK_HOST_UUID;
import static gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras.EXTRA_ATTACK_STARTED;

public class InternetServer extends Server {
    private BroadcastReceiver connectivityReceiver;

    public InternetServer(Context context, Attack attack) {
        super(context, attack);
        initReceiver();
    }

    private void initReceiver() {
        connectivityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (internetLost(intent)) {
                    statusListener.onServerStopped(attack.getWebsite());
                }
            }

            private boolean internetLost(Intent intent) {
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
        statusListener.onServerStopped(attack.getWebsite());
        super.stop();
    }

    @Override
    public void onConstraintsResolved() {
        statusListener.onServerRunning(attack.getWebsite());
        uploadAttack();
        registerReceiver();
    }

    private void uploadAttack() {
        setHostInfoTo(attack);
        repo.upload(attack);
    }

    private void setHostInfoTo(Attack attack) {
        attack.addSingleHostInfo(EXTRA_ATTACK_HOST_UUID, Bots.local().getId());
        attack.addSingleHostInfo(EXTRA_ATTACK_STARTED, Server.RESPONSE);
    }

    private void registerReceiver() {
        context.registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onConstraintResolveFailure() {
        statusListener.onServerError(attack.getWebsite());
    }
}
