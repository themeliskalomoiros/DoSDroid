package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.bluetooth.constraints;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.network_constraints.NetworkConstraint;

public class BluetoothDiscoverabilityConstraint extends NetworkConstraint {
    private static final String TAG = "BluetoothDiscoverabilit";
    public static final String ACTION_DISCOVERABILITY_ENABLED = "action discoverability enabled";
    public static final String ACTION_DISCOVERABILITY_DISABLED = "action discoverability disabled";

    private BroadcastReceiver discoverabilityReceiver;

    public BluetoothDiscoverabilityConstraint(Context context) {
        super(context);
        initializeDiscoverabilityReceiver();
        registerDiscoverabilityReceiver(context);
    }

    private void initializeDiscoverabilityReceiver() {
        discoverabilityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case ACTION_DISCOVERABILITY_ENABLED:
                        callback.onConstraintResolved(context, BluetoothDiscoverabilityConstraint.this);
                        break;
                    case ACTION_DISCOVERABILITY_DISABLED:
                        callback.onConstraintResolveFailed(context, BluetoothDiscoverabilityConstraint.this);
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
        filter.addAction(ACTION_DISCOVERABILITY_ENABLED);
        filter.addAction(ACTION_DISCOVERABILITY_DISABLED);
        return filter;
    }

    @Override
    public void resolve() {
        context.startActivity(getDiscoverabilityIntent());
    }

    @NonNull
    private Intent getDiscoverabilityIntent() {
        Intent intent = new Intent(context, BluetoothDiscoverabilityActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
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
