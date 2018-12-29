package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server;

import android.content.Context;
import android.util.Log;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

public class WifiP2pServer extends Server {

    public WifiP2pServer(Context context, Attack attack) {
        super(context, attack);
    }

    @Override
    public void start() {
        // TODO: needs implementation
        Log.d(TAG, "Server started");
    }

    @Override
    public void onConstraintsResolved() {

    }

    @Override
    public void onConstraintResolveFailure() {

    }
}
