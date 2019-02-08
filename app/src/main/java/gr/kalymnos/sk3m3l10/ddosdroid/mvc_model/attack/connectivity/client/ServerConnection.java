package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.client;

import android.content.Context;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.BLUETOOTH;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.INTERNET;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.NSD;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.WIFI_P2P;

/*  This class offloads the connection implementations from Client class.*/

abstract class ServerConnection {
    protected static final String TAG = "MyServerConnection";

    protected Attack attack;
    protected Context context;
    protected ServerConnectionListener serverConnectionListener;

    interface ServerConnectionListener {
        void onServerConnection();

        void onServerConnectionError();

        //  TODO: Probably not needed
        void onServerDisconnection();
    }

    ServerConnection(Context context, Attack attack) {
        this.context = context;
        this.attack = attack;
    }

    void setServerConnectionListener(ServerConnectionListener listener) {
        this.serverConnectionListener = listener;
    }

    abstract void connectToServer();

    abstract void disconnectFromServer();

    protected void releaseResources() {
        context = null;
        serverConnectionListener = null;
    }

    interface Factory {
        ServerConnection create(Context context, Attack attack);
    }

    static class FactoryImp implements Factory {

        @Override
        public ServerConnection create(Context context, Attack attack) {
            switch (attack.getNetworkType()) {
                case INTERNET:
                    return new InternetServerConnection(context, attack);
                case BLUETOOTH:
                    return new BluetoothServerConnection(context, attack);
                case WIFI_P2P:
                    return new WifiP2PServerConnection(context, attack);
                case NSD:
                    return new NsdServerConnection(context, attack);
                default:
                    throw new IllegalArgumentException(TAG + "Unknown attack network type");
            }
        }
    }

}
