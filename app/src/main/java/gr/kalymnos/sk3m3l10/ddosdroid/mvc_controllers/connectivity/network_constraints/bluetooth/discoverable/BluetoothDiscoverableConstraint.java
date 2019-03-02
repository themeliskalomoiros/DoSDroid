package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.connectivity.network_constraints.bluetooth.discoverable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.connectivity.network_constraints.NetworkConstraint;

public class BluetoothDiscoverableConstraint extends NetworkConstraint {
    private static final String TAG = "BluetoothDiscoverabilit";

    private BroadcastReceiver discoverabilityReceiver;

    public BluetoothDiscoverableConstraint(Context context) {
        super(context);
        initDiscoverabilityReceiver();
        registerDiscoverabilityReceiver(context);
    }

    private void initDiscoverabilityReceiver() {
        discoverabilityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case BluetoothDiscoverableActivity.ACTION_DISCOVERABLE:
                        onResolveConstraintListener.onConstraintResolved(context, BluetoothDiscoverableConstraint.this);
                        break;
                    case BluetoothDiscoverableActivity.ACTION_NOT_DISCOVERABLE:
                        onResolveConstraintListener.onConstraintResolveFailed(context, BluetoothDiscoverableConstraint.this);
                        break;
                    default:
                        throw new IllegalArgumentException(TAG + "Unknown action");
                }
                LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
            }
        };
    }

    private void registerDiscoverabilityReceiver(Context context) {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        localBroadcastManager.registerReceiver(discoverabilityReceiver, getIntentFilter());
    }

    @NonNull
    private IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDiscoverableActivity.ACTION_DISCOVERABLE);
        filter.addAction(BluetoothDiscoverableActivity.ACTION_NOT_DISCOVERABLE);
        return filter;
    }

    @Override
    public void resolve() {
        BluetoothDiscoverableActivity.startInstance(context);
    }

    @Override
    public boolean isResolved() {
        return true;
    }

    @Override
    public void releaseResources() {
        super.releaseResources();
    }
}
