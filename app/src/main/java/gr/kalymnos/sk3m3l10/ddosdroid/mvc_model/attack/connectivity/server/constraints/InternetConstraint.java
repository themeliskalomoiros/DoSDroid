package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.constraints;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities.WifiScanResultsActivity;
import gr.kalymnos.sk3m3l10.ddosdroid.utils.InternetConnectivity;

import static android.net.wifi.WifiManager.SCAN_RESULTS_AVAILABLE_ACTION;
import static gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities.WifiScanResultsActivity.ACTION_SCAN_RESULT_CANCELLED;
import static gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities.WifiScanResultsActivity.ACTION_SCAN_RESULT_CHOSEN;
import static gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities.WifiScanResultsActivity.ACTION_SCAN_RESULT_CONNECTION_ERROR;

class InternetConstraint extends NetworkConstraint {
    private static final String TAG = "InternetConstraint";
    private BroadcastReceiver wifiScanReceiver = null, wifiConnectionReceiver = null;
    private WifiManager wifiManager = null;

    @Override
    public void resolve(Context context) {
        if (isResolved(context)) {
            callback.onConstraintResolved(context, this);
        } else {
            scanForWifiNetworks(context);
        }
    }

    private void scanForWifiNetworks(Context context) {
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        initializeWifiScanReceiver();
        registeWifiScanReceiver(context);
    }

    private void initializeWifiScanReceiver() {
        wifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);
                if (success) {
                    handleScanSuccess(context);
                } else {
                    handleScanFailure();
                }
            }

            private void handleScanSuccess(Context context) {
                initializeWifiConnectionReceiver();
                registerWifiConnectionReceiver(context);
                List<String> SSIDs = createSSIDsFrom(wifiManager.getScanResults());
                WifiScanResultsActivity.startInstance(context, SSIDs);
            }

            private void initializeWifiConnectionReceiver() {
                wifiConnectionReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        switch (intent.getAction()) {
                            case ACTION_SCAN_RESULT_CHOSEN:
                                callback.onConstraintResolved(context, InternetConstraint.this);
                                break;
                            case ACTION_SCAN_RESULT_CANCELLED:
                                // TODO: implementation needed
                                break;
                            case ACTION_SCAN_RESULT_CONNECTION_ERROR:
                                // TODO: implementation needed
                                break;
                            default:
                                throw new IllegalArgumentException(TAG + ": unknown action " + intent.getAction());
                        }
                    }
                };
            }

            private void registerWifiConnectionReceiver(Context context) {
                IntentFilter filter = createWifiConnectionReceiverIntentFilter();
                LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
                manager.registerReceiver(wifiConnectionReceiver, filter);
            }

            @NonNull
            private List<String> createSSIDsFrom(List<ScanResult> results) {
                List<String> SSIDs = new ArrayList<>();
                for (ScanResult result : results) {
                    SSIDs.add(result.SSID);
                }
                return SSIDs;
            }

            private void handleScanFailure() {
            }
        };
    }

    private void registeWifiScanReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(SCAN_RESULTS_AVAILABLE_ACTION);
        context.registerReceiver(wifiScanReceiver, filter);
    }

    @NonNull
    private IntentFilter createWifiConnectionReceiverIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiScanResultsActivity.ACTION_SCAN_RESULT_CHOSEN);
        filter.addAction(WifiScanResultsActivity.ACTION_SCAN_RESULT_CANCELLED);
        filter.addAction(WifiScanResultsActivity.ACTION_SCAN_RESULT_CONNECTION_ERROR);
        return filter;
    }

    @Override
    public boolean isResolved(Context context) {
        return isConnected(context);
    }

    @Override
    public void clearResources(Context context) {
        unregisterReceivers(context);
    }

    private void unregisterReceivers(Context context) {
        if (wifiScanReceiver != null)
            context.unregisterReceiver(wifiScanReceiver);

        if (wifiConnectionReceiver != null) {
            LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
            manager.unregisterReceiver(wifiConnectionReceiver);
        }
    }

    private boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return InternetConnectivity.isConnectionEstablishedOverWifi(cm.getActiveNetworkInfo());
    }
}
