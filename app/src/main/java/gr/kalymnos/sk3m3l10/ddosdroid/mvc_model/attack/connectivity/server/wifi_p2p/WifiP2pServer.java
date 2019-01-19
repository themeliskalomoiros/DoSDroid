package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.wifi_p2p;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.Server;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.ServersHost;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.status.ServerStatusBroadcaster;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

import static android.net.wifi.p2p.WifiP2pManager.EXTRA_WIFI_STATE;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_STATE_DISABLED;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION;

// TODO: Server must publish its WipP2pConfig in the Attack.hostInfo so that
// the client should know which device to pick to connect automatically (not choose by the user).

public class WifiP2pServer extends Server {
    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel channel;
    private BroadcastReceiver wifiDirectReceiver;
    private WifiP2pManager.ConnectionInfoListener connectionInfoListener;

    public WifiP2pServer(Context context, Attack attack) {
        super(context, attack);
        setup();
    }

    private void setup() {
        wifiP2pManager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
        initializeWifiDirectReceiver();
        registerWifiDirectReceiver();
        channel = wifiP2pManager.initialize(context, Looper.getMainLooper(), null);
        initializeConnectionInfoListener();
    }

    private void initializeWifiDirectReceiver() {
        wifiDirectReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case WIFI_P2P_STATE_CHANGED_ACTION:
                        handleStateChangedAction(context, intent);
                        break;
                    case WIFI_P2P_CONNECTION_CHANGED_ACTION:
                        handleConnectionChangedAction(intent);
                        break;

                    case WIFI_P2P_THIS_DEVICE_CHANGED_ACTION:
                        break;
                    default:
                        throw new IllegalArgumentException(TAG + ": unknown action");
                }
            }

            private void handleStateChangedAction(Context context, Intent intent) {
                if (isStateDisabled(intent))
                    ServersHost.Action.stopServer(context, getId());
                return;
            }

            private boolean isStateDisabled(Intent intent) {
                return intent.getIntExtra(EXTRA_WIFI_STATE, -1) == WIFI_P2P_STATE_DISABLED;
            }

            private void handleConnectionChangedAction(Intent intent) {
                boolean isConnected = getNetworkInfoFrom(intent).isConnected();
                if (isConnected) {
                    wifiP2pManager.requestConnectionInfo(channel, connectionInfoListener);
                }
                return;
            }

            private NetworkInfo getNetworkInfoFrom(Intent intent) {
                return intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            }
        };
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

    private void initializeConnectionInfoListener() {
        connectionInfoListener = info -> {
            if (info.groupFormed && info.isGroupOwner) {
                ServerStatusBroadcaster.broadcastRunning(getId(), LocalBroadcastManager.getInstance(context));
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
//        ServerStatusBroadcaster.broadcastRunning(getId(), LocalBroadcastManager.getInstance(context));
    }

    @Override
    public void onConstraintResolveFailure() {
        ServerStatusBroadcaster.broadcastError(getId(), LocalBroadcastManager.getInstance(context));
    }

    public WifiP2pManager getWifiP2pManager() {
        return wifiP2pManager;
    }

    public WifiP2pManager.Channel getChannel() {
        return channel;
    }
}
