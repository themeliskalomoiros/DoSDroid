package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.wifi_p2p;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.Server;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.ServersHost;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.status.ServerStatusBroadcaster;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

import static android.net.wifi.p2p.WifiP2pManager.EXTRA_WIFI_STATE;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_STATE_DISABLED;

// TODO: Server must publish its WipP2pConfig in the Attack.hostInfo so that
// the client should know which device to pick to connect automatically (not choose by the user).

public class WifiP2pServer extends Server {
    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel channel;
    private BroadcastReceiver wifiDirectReceiver;

    public WifiP2pServer(Context context, Attack attack) {
        super(context, attack);
        wifiP2pManager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
        initializeWifiDirectReceiver();
    }

    private void initializeWifiDirectReceiver() {
        wifiDirectReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case WIFI_P2P_STATE_CHANGED_ACTION:
                        if (isStateDisabled(intent))
                            ServersHost.Action.stopServer(context, getId());
                        break;
                    default:
                        throw new IllegalArgumentException(TAG + ": unknown action");
                }
            }

            private boolean isStateDisabled(Intent intent) {
                return intent.getIntExtra(EXTRA_WIFI_STATE, -1) == WIFI_P2P_STATE_DISABLED;
            }
        };
    }

    @Override
    public void start() {
        constraintsResolver.resolveConstraints();
    }

    @Override
    public void stop() {
        context.unregisterReceiver(wifiDirectReceiver);
        super.stop();
    }

    @Override
    public void onConstraintsResolved() {
        registerWifiDirectReceiver();
        channel = wifiP2pManager.initialize(context, Looper.getMainLooper(), null);
        ServerStatusBroadcaster.broadcastRunning(getId(), LocalBroadcastManager.getInstance(context));
    }

    private void registerWifiDirectReceiver() {
        context.registerReceiver(wifiDirectReceiver, getIntentFilter());
    }

    @NonNull
    private IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WIFI_P2P_STATE_CHANGED_ACTION);
        return filter;
    }

    @Override
    public void onConstraintResolveFailure() {
        ServerStatusBroadcaster.broadcastError(getId(), LocalBroadcastManager.getInstance(context));
    }
}
