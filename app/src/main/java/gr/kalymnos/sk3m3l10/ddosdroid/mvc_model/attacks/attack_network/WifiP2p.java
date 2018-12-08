package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attacks.attack_network;

import android.content.Context;
import android.support.annotation.NonNull;

class WifiP2p extends AttackNetwork {

    protected WifiP2p(@NonNull Context context, OnConnectionListener listener) {
        super(context, listener);
    }

    @Override
    public void connect() {
        connectionListener.onAttackNetworkConnected();
    }

    @Override
    public void dissconnect() {

    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    protected void initializeConnectivityReceiver() {

    }

    @Override
    protected void initializeConnectivityIntentFilter() {

    }
}
