package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack_network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.annotation.NonNull;

/*
* When an attack is created a network type is specified (internet, bluetooth, wifi p2p, etc...).
* The ddos botnet must be connected to this network type in order to follow that attack.
* */

public abstract class AttackNetworkType {

    public interface OnConnectionListener {
        void onConnected();

        void onDisconnected(CharSequence reason);
    }

    protected Context context;
    protected OnConnectionListener connectionListener;
    protected BroadcastReceiver connectivityReceiver;
    protected IntentFilter connectivityIntentFilter;

    protected AttackNetworkType(@NonNull Context context, OnConnectionListener listener) {
        this.context = context;
        this.connectionListener = listener;
        this.initializeConnectivityReceiver();
        this.initializeConnectivityIntentFilter();
    }

    public abstract void connect();

    public abstract void dissconnect();

    public abstract boolean isConnected();

    public final void registerConnectionReceiver() {
        context.registerReceiver(connectivityReceiver, connectivityIntentFilter);
    }

    public final void unregisterConnectionReceiver() {
        context.unregisterReceiver(connectivityReceiver);
    }

    public final void unregisterConnectionListener() {
        connectionListener = null;
    }

    protected abstract void initializeConnectivityReceiver();

    protected abstract void initializeConnectivityIntentFilter();
}
