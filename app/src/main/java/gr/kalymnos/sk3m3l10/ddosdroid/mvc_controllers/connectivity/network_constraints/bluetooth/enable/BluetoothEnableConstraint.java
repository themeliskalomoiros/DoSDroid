package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.connectivity.network_constraints.bluetooth.enable;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.connectivity.network_constraints.NetworkConstraint;

public class BluetoothEnableConstraint extends NetworkConstraint {
    private static final String TAG = "BluetoothEnableConstrai";

    private BluetoothAdapter bluetoothAdapter;
    private BroadcastReceiver bluetoothEnableReceiver;

    public BluetoothEnableConstraint(Context context) {
        super(context);
        initFields();
        registerBluetoothEnableReceiver(context);
    }

    private void initFields() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        initBluetoothEnableReceiver();
    }

    private void initBluetoothEnableReceiver() {
        bluetoothEnableReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case EnableBluetoothActivity.ACTION_ENABLED:
                        onResolveConstraintListener.onConstraintResolved(context, BluetoothEnableConstraint.this);
                        break;
                    case EnableBluetoothActivity.ACTION_DISABLED:
                        onResolveConstraintListener.onConstraintResolveFailed(context, BluetoothEnableConstraint.this);
                        break;
                    default:
                        throw new IllegalArgumentException(TAG + "Unknown action");
                }
                unregisterReceiver(context);
            }

            private void unregisterReceiver(Context context) {
                LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
                localBroadcastManager.unregisterReceiver(bluetoothEnableReceiver);
            }
        };
    }

    private void registerBluetoothEnableReceiver(Context context) {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        localBroadcastManager.registerReceiver(bluetoothEnableReceiver, getIntentFilter());
    }

    @NonNull
    private IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(EnableBluetoothActivity.ACTION_ENABLED);
        filter.addAction(EnableBluetoothActivity.ACTION_DISABLED);
        return filter;
    }

    @Override
    public void resolve() {
        if (isResolved()) {
            onResolveConstraintListener.onConstraintResolved(context, this);
        } else {
            EnableBluetoothActivity.startInstance(context);
        }
    }


    @Override
    public boolean isResolved() {
        return bluetoothAdapter.isEnabled();
    }

    @Override
    public void releaseResources() {
        super.releaseResources();
    }
}
