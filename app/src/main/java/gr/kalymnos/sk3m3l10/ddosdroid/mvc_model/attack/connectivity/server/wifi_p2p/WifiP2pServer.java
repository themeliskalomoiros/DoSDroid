package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.wifi_p2p;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.Server;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.status.ServerStatusBroadcaster;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

public class WifiP2pServer extends Server {
    //  TODO: must implement a receiver that listens when the wifi is disabled
    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel channel;
    private BroadcastReceiver wifiDirectReceiver;

    public WifiP2pServer(Context context, Attack attack) {
        super(context, attack);
        wifiP2pManager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
    }

    @Override
    public void start() {
        constraintsResolver.resolveConstraints();
    }

    @Override
    public void onConstraintsResolved() {
        channel = wifiP2pManager.initialize(context, Looper.getMainLooper(), null);
        ServerStatusBroadcaster.broadcastRunning(getId(), LocalBroadcastManager.getInstance(context));
    }

    @Override
    public void onConstraintResolveFailure() {
        ServerStatusBroadcaster.broadcastError(getId(), LocalBroadcastManager.getInstance(context));
    }
}
