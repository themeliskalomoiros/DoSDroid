package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.wifi_p2p.constraints;

import android.content.Context;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.network_constraints.NetworkConstraint;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.wifi_p2p.WifiP2pServer;

public class WifiP2pGroupConstraint extends NetworkConstraint {
    private WifiP2pServer server;

    public WifiP2pGroupConstraint(Context context, WifiP2pServer server) {
        super(context);
    }

    @Override
    public void resolve() {

    }

    @Override
    public boolean isResolved() {
        return false;
    }

    @Override
    public void cleanResources() {

    }
}
