package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server;

import android.content.Context;
import android.util.Log;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

public class WifiP2pServer extends Server {
    private Context context;

    public WifiP2pServer(Attack attack, Context context) {
        super(attack);
        this.context = context;
    }

    @Override
    public void start() {
        // TODO: needs implementation
        Log.d(TAG, "Server started");
    }
}
