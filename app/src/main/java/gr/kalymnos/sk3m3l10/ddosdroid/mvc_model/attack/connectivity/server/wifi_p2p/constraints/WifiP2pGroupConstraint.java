package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.wifi_p2p.constraints;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.annotation.NonNull;
import android.util.Log;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.network_constraints.NetworkConstraint;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.wifi_p2p.WifiP2pServer;

// Resolves when the group is created successfully.

public class WifiP2pGroupConstraint extends NetworkConstraint {
    private static final String TAG = "MyWifiP2pGroupConstrai";

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
                callback.onConstraintResolved(context, WifiP2pGroupConstraint.this);
                Log.d(TAG, "WifiP2pEnabledConstraint is resolved.");
            }

            @Override
            public void onFailure(int reason) {
                Log.d(TAG, "WifiP2pGroupConstraint failed to resolve because of \"" + getReasonText(reason) + "\"");
                callback.onConstraintResolveFailed(context, WifiP2pGroupConstraint.this);
            }

            String getReasonText(int reason) {
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
    public void releaseResources() {
        super.releaseResources();
        server = null;
    }
}
