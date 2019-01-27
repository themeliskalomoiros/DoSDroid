package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.client;

import android.content.Context;
import android.net.ConnectivityManager;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.utils.InternetConnectivity;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.Extra.EXTRA_ATTACK_STARTED;

class InternetClientConnection extends ClientConnection {

    InternetClientConnection(Context context, Attack attack) {
        super(context, attack);
    }

    @Override
    void connect() {
        boolean hasInternet = InternetConnectivity.hasInternetConnection(getConnectivityManager());
        if (hasInternet && isAttackStarted()) {
            connectionListener.onConnected();
            attackDeletionReporter.attach();
        } else {
            connectionListener.onConnectionError();
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
    void disconnect() {
        connectionListener.onDisconnected();
        releaseResources();
    }

    @Override
    protected void releaseResources() {
        super.releaseResources();
    }
}
