package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.connectivity.client.internet;

import android.content.Context;
import android.net.ConnectivityManager;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.connectivity.client.ServerConnection;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.connectivity.server.Server;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.utils.connectivity.InternetConnectivity;

import static gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras.EXTRA_ATTACK_STARTED;

public class InternetServerConnection extends ServerConnection {

    public InternetServerConnection(Context context, Attack attack) {
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

    private ConnectivityManager getConnectivityManager() {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    private boolean isAttackStarted() {
        String attackStartedPass = attack.getHostInfo().get(EXTRA_ATTACK_STARTED);
        return Server.isValid(attackStartedPass);
    }
}
