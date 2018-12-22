package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server;

import android.util.Log;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

public class NsdServer extends Server {
    public NsdServer(Attack attack) {
        super(attack);
    }

    @Override
    public void start() {
        // TODO: needs implementation
        Log.d(TAG, "Server started");
    }

    @Override
    public void stop() {
        // TODO: needs implementation
        Log.d(TAG, "Server stoped");
    }
}
