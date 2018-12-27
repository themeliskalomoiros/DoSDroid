package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.constraints;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;

import gr.kalymnos.sk3m3l10.ddosdroid.utils.InternetConnectivity;

class InternetConstraint extends NetworkConstraint {
    private BroadcastReceiver wifiScanReceiver = null;

    @Override
    public void resolve(Context context) {
        if (!isResolved(context)) {

        }
    }

    private void initializeWifiScanReceiver() {
        wifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);
                if (success) {
                    // TODO: Handle scan success
                } else {
                    // TODO: Handle scan failure
                }
            }
        };
    }

    @Override
    public boolean isResolved(Context context) {
        return isConnected(context);
    }

    @Override
    public void clearResources(Context context) {

    }

    private boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return InternetConnectivity.isConnectionEstablishedOverWifi(cm.getActiveNetworkInfo());
    }
}
