package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.annotation.NonNull;

/**
 * Attacks are created specified by a network that a a follower must connect
 * in order to be part of this attack. This class represents this Network.
 */

public abstract class Network {

    public interface OnConnectionListener {
        void onConnected();

        void onDisconnected(CharSequence reason);
    }

    protected Context context;
    protected OnConnectionListener connectionListener;
    protected BroadcastReceiver connectivityReceiver;
    protected IntentFilter connectivityIntentFilter;

    protected Network(@NonNull Context context, OnConnectionListener listener) {
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
