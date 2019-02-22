package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.client;

import android.content.Context;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.client.bluetooth.BluetoothServerConnection;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.client.internet.InternetServerConnection;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.client.nsd.NsdServerConnection;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.client.wifi_p2p.WifiP2PServerConnection;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

import static gr.kalymnos.sk3m3l10.ddosdroid.constants.NetworkTypes.BLUETOOTH;
import static gr.kalymnos.sk3m3l10.ddosdroid.constants.NetworkTypes.INTERNET;
import static gr.kalymnos.sk3m3l10.ddosdroid.constants.NetworkTypes.NSD;
import static gr.kalymnos.sk3m3l10.ddosdroid.constants.NetworkTypes.WIFI_P2P;

/*  This class offloads the connection implementations from Client.*/

public abstract class ServerConnection {
    protected static final String TAG = "MyServerConnection";

    public Attack attack;
    protected Context context;
    public ConnectionToServerListener connectionListener;

    public interface ConnectionToServerListener {
        void onServerConnection();

        void onServerConnectionError();

        //  TODO: Probably not needed
        void onServerDisconnection();
    }

    public ServerConnection(Context context, Attack attack) {
        this.context = context;
        this.attack = attack;
    }

    public void setConnectionToServerListener(ConnectionToServerListener listener) {
        this.connectionListener = listener;
    }

    public abstract void connectToServer();

    public abstract void disconnectFromServer();

    protected void releaseResources() {
        context = null;
        connectionListener = null;
    }

    public interface Factory {
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
