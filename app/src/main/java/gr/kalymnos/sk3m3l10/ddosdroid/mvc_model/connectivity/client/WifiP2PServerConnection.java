package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.client;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Looper;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

class WifiP2PServerConnection extends ServerConnection {
    private WifiDirectReceiver receiver;

    WifiP2PServerConnection(Context context, Attack attack) {
        super(context, attack);
        initReceiver(context);
    }

    private void initReceiver(Context context) {
        WifiP2pManager manager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
        WifiP2pManager.Channel channel = manager.initialize(context, Looper.getMainLooper(), null);
        receiver = new WifiDirectReceiver(this, manager, channel);
    }

    @Override
    void connectToServer() {
        context.registerReceiver(receiver, WifiDirectReceiver.getIntentFilter());
    }

    @Override
    void disconnectFromServer() {
        serverConnectionListener.onServerDisconnection();
    }

    @Override
    protected void releaseResources() {
        receiver.releaseWifiP2pResources();
        context.unregisterReceiver(receiver);
        super.releaseResources();
    }
}
