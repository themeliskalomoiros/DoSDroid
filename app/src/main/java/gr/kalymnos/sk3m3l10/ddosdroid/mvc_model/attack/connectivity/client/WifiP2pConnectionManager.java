package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.client;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Looper;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

class WifiP2pConnectionManager extends ConnectionManager {
    private static final String TAG = "WifiP2pConnectionManage";

    private WifiDirectReceiver receiver;

    WifiP2pConnectionManager(Context context, Attack attack) {
        super(context, attack);
        initializeReceiver(context);
    }

    private void initializeReceiver(Context context) {
        WifiP2pManager manager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
        WifiP2pManager.Channel channel = manager.initialize(context, Looper.getMainLooper(), null);
        receiver = new WifiDirectReceiver(this, manager, channel);
    }

    @Override
    void connect() {
        context.registerReceiver(receiver, WifiDirectReceiver.getIntentFilter());
    }

    @Override
    void disconnect() {
        client.onManagerDisconnection();
        releaseResources();
    }

    @Override
    protected void releaseResources() {
        receiver.releaseWifiP2pResources();
        context.unregisterReceiver(receiver);
        super.releaseResources();
    }
}
