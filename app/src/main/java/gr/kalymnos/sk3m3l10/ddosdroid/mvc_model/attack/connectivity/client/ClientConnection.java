package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.client;

import android.content.Context;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.repository.AttackDeletionReporter;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.repository.FirebaseAttackDeletionReporter;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.BLUETOOTH;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.INTERNET;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.NSD;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.WIFI_P2P;

/*  This class offloads the connection implementations from Client class.*/

abstract class ClientConnection implements AttackDeletionReporter.AttackDeletionListener {
    private static final String TAG = "ClientConnection";

    protected Attack attack;
    protected Context context;
    protected ConnectionListener connectionListener;
    protected AttackDeletionReporter attackDeletionReporter;

    interface ConnectionListener {
        void onConnected(Attack attack);

        void onConnectionError();

        void onDisconnected(Attack attack);
    }

    ClientConnection(Context context, Attack attack) {
        this.context = context;
        this.attack = attack;
        this.attackDeletionReporter = new FirebaseAttackDeletionReporter();
        this.attackDeletionReporter.setAttackDeletionListener(this);
    }

    void setConnectionListener(ConnectionListener connectionListener) {
        this.connectionListener = connectionListener;
    }

    abstract void connect();

    abstract void disconnect();

    protected void releaseResources() {
        context = null;
        connectionListener = null;
        attackDeletionReporter.detach();
    }

    @Override
    public void onAttackDeleted(Attack attack) {
        disconnect();
    }

    interface Factory {
        ClientConnection create(Context context, Attack attack);
    }

    static class FactoryImp implements Factory {

        @Override
        public ClientConnection create(Context context, Attack attack) {
            switch (attack.getNetworkType()) {
                case INTERNET:
                    return new InternetClientConnection(context, attack);
                case BLUETOOTH:
                    return new BluetoothClientConnection(context, attack);
                case WIFI_P2P:
                    return new WifiP2pClientConnection(context, attack);
                case NSD:
                    return new NsdClientConnection(context, attack);
                default:
                    throw new IllegalArgumentException(TAG + "Unknown attack network type");
            }
        }
    }

}
