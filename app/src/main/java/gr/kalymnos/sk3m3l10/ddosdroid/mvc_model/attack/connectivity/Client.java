package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.annotation.NonNull;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants;

/*
 * When an attack is created a network type is specified (internet, bluetooth, wifi p2p, etc...).
 * The ddos botnet must be connected to this network type in order to follow that attack.
 * */

public abstract class Client {
    protected Context context;
    protected OnConnectionListener connectionListener;
    protected BroadcastReceiver connectivityReceiver;
    protected IntentFilter connectivityIntentFilter;

    public interface OnConnectionListener {
        void onAttackNetworkConnected();

        void onAttackNetworkDisconnected(CharSequence reason);
    }

    protected Client(@NonNull Context context, OnConnectionListener listener) {
        this.context = context;
        this.connectionListener = listener;
        this.initializeConnectivityReceiver();
        this.initializeConnectivityIntentFilter();
    }

    public abstract void connect();

    public abstract void dissconnect();

    public abstract boolean isConnected();

    protected abstract void initializeConnectivityReceiver();

    protected abstract void initializeConnectivityIntentFilter();

    public final void registerConnectionReceiver() {
        context.registerReceiver(connectivityReceiver, connectivityIntentFilter);
    }

    public final void unregisterConnectionReceiver() {
        context.unregisterReceiver(connectivityReceiver);
    }

    public final void unregisterConnectionListener() {
        connectionListener = null;
    }

    public interface AttackNetworkFactory {
        Client makeAttackNetwork(Context context, OnConnectionListener connectionListener, int attackNetworkType);
    }

    public static class AttackNetworkFactoryImp implements AttackNetworkFactory {
        private static final String TAG = "AttackNetworkFactoryImp";

        @Override
        public Client makeAttackNetwork(Context context, OnConnectionListener connectionListener, int attackNetworkType) {
            switch (attackNetworkType) {
                case Constants.NetworkType.INTERNET:
                    return new InternetClient(context, connectionListener);
                case Constants.NetworkType.WIFI_P2P:
                    return new WifiP2pClient(context, connectionListener);
                case Constants.NetworkType.NSD:
                    return new NsdClient(context, connectionListener);
                case Constants.NetworkType.BLUETOOTH:
                    return new BluetoothClient(context, connectionListener);
                default:
                    throw new UnsupportedOperationException(TAG + ": unknown attack type");
            }
        }
    }
}
