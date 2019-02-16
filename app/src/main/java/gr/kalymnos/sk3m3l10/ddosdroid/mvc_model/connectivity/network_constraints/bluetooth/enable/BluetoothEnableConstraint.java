package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.network_constraints.bluetooth.enable;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.network_constraints.NetworkConstraint;

public class BluetoothEnableConstraint extends NetworkConstraint {
    private static final String TAG = "BluetoothEnableConstrai";
    public static final String ACTION_BLUETOOTH_ENABLED = "action bluetooth enabled";
    public static final String ACTION_BLUETOOTH_DISABLED = "action bluetooth disabled";

    private BluetoothAdapter bluetoothAdapter;
    private BroadcastReceiver bluetoothEnableReceiver;

    public BluetoothEnableConstraint(Context context) {
        super(context);
        initializeFields();
        registerBluetoothEnableReceiver(context);
    }

    private void initializeFields() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        initializeBluetoothEnableReceiver();
    }

    private void initializeBluetoothEnableReceiver() {
        bluetoothEnableReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case ACTION_BLUETOOTH_ENABLED:
                        onResolveConstraintListener.onConstraintResolved(context, BluetoothEnableConstraint.this);
                        break;
                    case ACTION_BLUETOOTH_DISABLED:
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
        filter.addAction(ACTION_BLUETOOTH_ENABLED);
        filter.addAction(ACTION_BLUETOOTH_DISABLED);
        return filter;
    }

    @Override
    public void resolve() {
        if (isResolved()) {
            onResolveConstraintListener.onConstraintResolved(context, this);
        } else {
            context.startActivity(createEnableBluetoothIntent());
        }
    }

    private Intent createEnableBluetoothIntent() {
        Intent enableIntent = new Intent(context, EnableBluetoothActivity.class);
        enableIntent.setAction(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        enableIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return enableIntent;
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
