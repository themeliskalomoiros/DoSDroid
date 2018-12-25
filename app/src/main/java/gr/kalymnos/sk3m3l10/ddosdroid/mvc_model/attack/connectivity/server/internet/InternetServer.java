package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.internet;

import android.util.Log;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.Server;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

public class InternetServer extends Server {
    public InternetServer(Attack attack) {
        super(attack);
    }

    @Override
    public void start() {
        // TODO: needs implementation
        Log.d(TAG, "Server started");
    }

    @Override
    public void stop() {
        super.stop();
        Log.d(TAG, "Server stoped");
    }
}
