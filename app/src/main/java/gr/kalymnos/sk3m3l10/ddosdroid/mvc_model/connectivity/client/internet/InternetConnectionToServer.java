package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.client.internet;

import android.content.Context;
import android.net.ConnectivityManager;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.client.ConnectionToServer;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.server.Server;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.utils.InternetConnectivity;

import static gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras.EXTRA_ATTACK_STARTED;

public class InternetConnectionToServer extends ConnectionToServer {

    public InternetConnectionToServer(Context context, Attack attack) {
        super(context, attack);
    }

    @Override
    public void connectToServer() {
        boolean hasInternet = InternetConnectivity.hasInternetConnection(getConnectivityManager());
        if (hasInternet && isAttackStarted()) {
            connectionListener.onServerConnection();
        } else {
            connectionListener.onServerConnectionError();
        }
    }

    private boolean isAttackStarted() {
        String attackStartedPass = attack.getHostInfo().get(EXTRA_ATTACK_STARTED);
        return Server.isValid(attackStartedPass);
    }

    private ConnectivityManager getConnectivityManager() {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    public void disconnectFromServer() {
        connectionListener.onServerDisconnection();
        releaseResources();
    }
}
