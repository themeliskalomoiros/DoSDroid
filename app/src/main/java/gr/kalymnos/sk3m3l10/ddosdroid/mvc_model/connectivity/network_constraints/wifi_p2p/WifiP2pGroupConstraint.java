package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.network_constraints.wifi_p2p;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.annotation.NonNull;
import android.util.Log;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.network_constraints.NetworkConstraint;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.server.wifi.p2p.WifiP2pServer;
import gr.kalymnos.sk3m3l10.ddosdroid.utils.connectivity.WifiP2pUtil;

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
                onResolveConstraintListener.onConstraintResolved(context, WifiP2pGroupConstraint.this);
            }

            @Override
            public void onFailure(int reason) {
                Log.d(TAG, "WifiP2pGroupConstraint failed to resolve because of \"" + WifiP2pUtil.failureTextFrom(reason) + "\"");
                onResolveConstraintListener.onConstraintResolveFailed(context, WifiP2pGroupConstraint.this);
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
