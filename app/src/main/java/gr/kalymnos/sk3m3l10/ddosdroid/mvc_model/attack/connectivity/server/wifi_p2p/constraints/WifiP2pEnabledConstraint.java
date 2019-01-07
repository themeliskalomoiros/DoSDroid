package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.wifi_p2p.constraints;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.network_constraints.NetworkConstraint;

import static android.net.wifi.p2p.WifiP2pManager.EXTRA_WIFI_STATE;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_STATE_ENABLED;

public class WifiP2pEnabledConstraint extends NetworkConstraint {
    private static final String TAG = "WifiP2pEnabledConstraint";

    private BroadcastReceiver wifiStateReceiver;

    public WifiP2pEnabledConstraint(Context context) {
        super(context);
        initializeWifiStateReceiver();
    }

    private void initializeWifiStateReceiver() {
        wifiStateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case WIFI_P2P_STATE_CHANGED_ACTION:
                        int state = intent.getIntExtra(EXTRA_WIFI_STATE, -1);
                        handleWifiState(context, state);
                        break;
                    default:
                        throw new IllegalArgumentException(TAG + ": unknown action");
                }
                context.unregisterReceiver(wifiStateReceiver);
            }

            private void handleWifiState(Context context, int state) {
                if (state == WIFI_P2P_STATE_ENABLED) {
                    callback.onConstraintResolved(context, WifiP2pEnabledConstraint.this);
                } else {
                    callback.onConstraintResolveFailed(context, WifiP2pEnabledConstraint.this);
                }
            }
        };
    }

    @Override
    public void resolve() {
        registerWifiStateReceiver(context);
    }

    private void registerWifiStateReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WIFI_P2P_STATE_CHANGED_ACTION);
        context.registerReceiver(wifiStateReceiver, filter);
    }

    @Override
    public boolean isResolved() {
        return false;
    }

    @Override
    public void cleanResources() {

    }
}
