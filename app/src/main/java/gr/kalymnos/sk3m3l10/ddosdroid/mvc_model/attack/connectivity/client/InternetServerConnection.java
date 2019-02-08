package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.client;

import android.content.Context;
import android.net.ConnectivityManager;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.utils.InternetConnectivity;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.Extra.EXTRA_ATTACK_STARTED;

class InternetServerConnection extends ServerConnection {

    InternetServerConnection(Context context, Attack attack) {
        super(context, attack);
    }

    @Override
    void connectToServer() {
        boolean hasInternet = InternetConnectivity.hasInternetConnection(getConnectivityManager());
        if (hasInternet && isAttackStarted()) {
            serverConnectionListener.onServerConnection();
        } else {
            serverConnectionListener.onServerConnectionError();
        }
    }

    private boolean isAttackStarted() {
        String attackStartedPass = attack.getHostInfo().get(EXTRA_ATTACK_STARTED);
        return attackStartedPass.equals(Attack.STARTED_PASS);
    }

    private ConnectivityManager getConnectivityManager() {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    void disconnectFromServer() {
        serverConnectionListener.onServerDisconnection();
    }
}
