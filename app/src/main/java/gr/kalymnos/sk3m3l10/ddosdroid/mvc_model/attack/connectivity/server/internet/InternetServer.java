package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.internet;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.Server;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.utils.InternetConnectivity;

public class InternetServer extends Server {
    private Context context;

    public InternetServer(Attack attack, Context context) {
        super(attack);
        this.context = context;
    }

    @Override
    public void start() {
        constraintsResolver.resolveConstraints(context);
    }
}
