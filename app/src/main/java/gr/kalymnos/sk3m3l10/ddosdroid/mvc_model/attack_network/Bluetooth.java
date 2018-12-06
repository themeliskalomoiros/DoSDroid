package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack_network;

import android.content.Context;
import android.support.annotation.NonNull;

class Bluetooth extends AttackNetwork {

    protected Bluetooth(@NonNull Context context, OnConnectionListener listener) {
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
