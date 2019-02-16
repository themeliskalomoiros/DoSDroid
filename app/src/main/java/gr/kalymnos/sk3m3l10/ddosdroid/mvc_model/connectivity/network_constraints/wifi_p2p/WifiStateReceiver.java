package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.network_constraints.wifi_p2p;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static android.net.wifi.p2p.WifiP2pManager.EXTRA_WIFI_STATE;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION;

public abstract class WifiStateReceiver extends BroadcastReceiver {
    private static final String TAG = "WifiStateReceiver";

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
        context.unregisterReceiver(this);
    }

    protected abstract void handleWifiState(Context context, int state);
}
