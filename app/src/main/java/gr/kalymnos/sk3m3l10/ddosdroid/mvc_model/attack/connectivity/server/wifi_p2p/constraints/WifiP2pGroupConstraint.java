package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.wifi_p2p.constraints;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.annotation.NonNull;
import android.util.Log;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.network_constraints.NetworkConstraint;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.wifi_p2p.WifiP2pServer;

// Resolves when the group is created successfully.

public class WifiP2pGroupConstraint extends NetworkConstraint {
    private static final String TAG = "WifiP2pGroupConstraint";

    private WifiP2pServer server;

    public WifiP2pGroupConstraint(Context context, WifiP2pServer server) {
        super(context);
        this.server = server;
    }

    @Override
    public void resolve() {
        createGroup();
    }

    private void createGroup() {
        server.getWifiP2pManager().createGroup(server.getChannel(), getGroupInitiationListener());
    }

    @NonNull
    private WifiP2pManager.ActionListener getGroupInitiationListener() {
        return new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Initiating group creation.");
                callback.onConstraintResolved(context, WifiP2pGroupConstraint.this);
            }

            @Override
            public void onFailure(int reason) {
                Log.d(TAG, "Group creation failed with reason: " + getReason(reason));
                callback.onConstraintResolveFailed(context, WifiP2pGroupConstraint.this);
            }

            String getReason(int reason) {
                if (reason == WifiP2pManager.BUSY)
                    return "the operation failed because the framework is busy and unable to service the request";
                if (reason == WifiP2pManager.P2P_UNSUPPORTED)
                    return " the operation failed because p2p is unsupported on the device.";
                return "the operation failed due to an internal error. ";

            }
        };
    }

    @Override
    public boolean isResolved() {
        return false;
    }

    @Override
    public void cleanResources() {
        server = null;
    }
}
