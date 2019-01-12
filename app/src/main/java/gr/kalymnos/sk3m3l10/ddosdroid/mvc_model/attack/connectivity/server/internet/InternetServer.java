package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.internet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v4.content.LocalBroadcastManager;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.Server;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.ServersHost;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.status.ServerStatusBroadcaster;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

public class InternetServer extends Server {
    private BroadcastReceiver connectivityReceiver;

    public InternetServer(Context context, Attack attack) {
        super(context, attack);
        initializeConnectivityReceiver();
    }

    private void initializeConnectivityReceiver() {
        connectivityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (disconnectionHappened(intent)) {
                    ServersHost.Action.stopServer(context, getId());
                    context.unregisterReceiver(this);
                }
            }

            private boolean disconnectionHappened(Intent intent) {
                return intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION) && intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, true);
            }
        };
    }

    @Override
    public void start() {
        constraintsResolver.resolveConstraints();
    }

    @Override
    public void onConstraintsResolved() {
        ServerStatusBroadcaster.broadcastRunning(getId(), LocalBroadcastManager.getInstance(context));
    }

    @Override
    public void onConstraintResolveFailure() {
        ServerStatusBroadcaster.broadcastError(getId(), LocalBroadcastManager.getInstance(context));
    }
}
