package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.constraints;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.List;

import gr.kalymnos.sk3m3l10.ddosdroid.utils.InternetConnectivity;

class InternetConstraint extends NetworkConstraint {
    private BroadcastReceiver wifiScanReceiver = null;
    private WifiManager wifiManager = null;

    @Override
    public void resolve(Context context) {
        if (!isResolved(context)) {
            wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            initializeWifiScanReceiver();
            registeWifiScanReceiver(context);
        }
    }

    private void initializeWifiScanReceiver() {
        wifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);
                if (success) {
                    handleScanSucces();
                } else {
                    handleScanFailure();
                }
            }

            private void handleScanSucces() {
                List<ScanResult> results = wifiManager.getScanResults();

            }

            private void handleScanFailure() {
            }
        };
    }

    private void registeWifiScanReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        context.registerReceiver(wifiScanReceiver, filter);
    }

    @Override
    public boolean isResolved(Context context) {
        return isConnected(context);
    }

    @Override
    public void clearResources(Context context) {
        if (wifiScanReceiver != null)
            context.unregisterReceiver(wifiScanReceiver);
    }

    private boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return InternetConnectivity.isConnectionEstablishedOverWifi(cm.getActiveNetworkInfo());
    }
}
