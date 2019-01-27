package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.client;

import android.content.Context;
import android.net.ConnectivityManager;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.utils.InternetConnectivity;

class InternetClientConnection extends ClientConnection {

    InternetClientConnection(Context context, Attack attack) {
        super(context, attack);
    }

    @Override
    void connect() {
        boolean hasInternet = InternetConnectivity.hasInternetConnection(getConnectivityManager());
        if (hasInternet) {
            connectionListener.onConnected();
        } else {
            connectionListener.onConnectionError();
        }
    }

    private ConnectivityManager getConnectivityManager() {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    void disconnect() {
        connectionListener.onDisconnected();
    }

    @Override
    protected void releaseResources() {
        super.releaseResources();
    }
}
