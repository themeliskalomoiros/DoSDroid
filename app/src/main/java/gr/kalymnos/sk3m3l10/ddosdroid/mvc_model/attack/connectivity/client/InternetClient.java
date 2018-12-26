package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import gr.kalymnos.sk3m3l10.ddosdroid.R;
import gr.kalymnos.sk3m3l10.ddosdroid.utils.InternetConnectivity;

class InternetClient extends Client {

    public InternetClient(@NonNull Context context, @NonNull OnConnectionListener listener) {
        super(context, listener);
    }

    @Override
    public void connect() {
        connectionListener.onAttackNetworkConnected();
    }

    @Override
    public void dissconnect() {
        //  TODO: implementation needed
    }

    @Override
    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return InternetConnectivity.isConnectionEstablishedOverWifi(cm.getActiveNetworkInfo());
    }

    @Override
    protected void initializeConnectivityReceiver() {
        connectivityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case ConnectivityManager.CONNECTIVITY_ACTION:
                        if (InternetConnectivity.isConnectedToNetwork(getNetworkInfo(intent))) {
                            connectionListener.onAttackNetworkConnected();
                        } else {
                            connectionListener.onAttackNetworkDisconnected(context.getString(R.string.internet_disconnected_msg));
                        }
                        break;
                }
            }

            private NetworkInfo getNetworkInfo(Intent intent) {
                return intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            }
        };
    }

    @Override
    protected void initializeConnectivityIntentFilter() {
        connectivityIntentFilter = new IntentFilter();
        connectivityIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
    }
}
