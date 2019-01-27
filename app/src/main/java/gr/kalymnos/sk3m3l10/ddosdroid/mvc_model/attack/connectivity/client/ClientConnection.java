package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.client;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.BLUETOOTH;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.INTERNET;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.NSD;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.WIFI_P2P;

abstract class ClientConnection {
    private static final String TAG = "ClientConnection";

    private Attack attack;
    private ConnectionListener connectionListener;

    interface ConnectionListener {
        void onConnected();

        void onConnectionError();

        void onDisconnected();

    }

    ClientConnection(Attack attack) {
        this.attack = attack;
    }

    void setConnectionListener(ConnectionListener connectionListener) {
        this.connectionListener = connectionListener;
    }

    abstract void connect();

    abstract void disconnect();

    protected abstract void releaseResources();

    interface Factory {
        ClientConnection create(Attack attack);
    }

    static class FactoryImp implements Factory {

        @Override
        public ClientConnection create(Attack attack) {
            switch (attack.getNetworkType()) {
                case INTERNET:
                    return new InternetClientConnection(attack);
                case BLUETOOTH:
                    return new BluetoothClientConnection(attack);
                case WIFI_P2P:
                    return new WifiP2pClientConnection(attack);
                case NSD:
                    return new NsdClientConnection(attack);
                default:
                    throw new IllegalArgumentException(TAG + "Unknown attack network type");
            }
        }
    }

}
