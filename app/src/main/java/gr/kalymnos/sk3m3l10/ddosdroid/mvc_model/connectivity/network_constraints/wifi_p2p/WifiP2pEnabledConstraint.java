package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.network_constraints.wifi_p2p;

import android.content.Context;
import android.content.IntentFilter;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.network_constraints.NetworkConstraint;

import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_STATE_ENABLED;

public class WifiP2pEnabledConstraint extends NetworkConstraint {
    private WifiStateReceiver wifiStateReceiver;

    public WifiP2pEnabledConstraint(Context context) {
        super(context);
        initializeWifiStateReceiver();
    }

    private void initializeWifiStateReceiver() {
        wifiStateReceiver = new WifiStateReceiver() {
            @Override
            protected void handleWifiState(Context context, int state) {
                if (state == WIFI_P2P_STATE_ENABLED) {
                    onResolveConstraintListener.onConstraintResolved(context, WifiP2pEnabledConstraint.this);
                } else {
                    onResolveConstraintListener.onConstraintResolveFailed(context, WifiP2pEnabledConstraint.this);
                }
            }
        };
    }

    @Override
    public void resolve() {
        context.registerReceiver(wifiStateReceiver, new IntentFilter(WIFI_P2P_STATE_CHANGED_ACTION));
    }

    @Override
    public boolean isResolved() {
        return false;
    }

    @Override
    public void releaseResources() {
        super.releaseResources();
    }
}
