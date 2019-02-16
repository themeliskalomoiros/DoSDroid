package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.network_constraints.wifi_p2p;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.network_constraints.NetworkConstraint;

import static android.net.wifi.p2p.WifiP2pManager.EXTRA_WIFI_STATE;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_STATE_ENABLED;

public class WifiP2pEnabledConstraint extends NetworkConstraint {
    private static final String TAG = "MyWifiP2pEnabledConstra";

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
                    onResolveConstraintListener.onConstraintResolved(context, WifiP2pEnabledConstraint.this);
                    Log.d(TAG, "WifiP2pEnabledConstraint is resolved.");
                } else {
                    onResolveConstraintListener.onConstraintResolveFailed(context, WifiP2pEnabledConstraint.this);
                    Log.d(TAG, "WifiP2pEnabledConstraint failed to resolved.");
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
