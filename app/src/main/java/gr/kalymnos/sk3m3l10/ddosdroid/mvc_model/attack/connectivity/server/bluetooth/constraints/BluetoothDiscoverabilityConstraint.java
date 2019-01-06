package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.bluetooth.constraints;

import android.content.Context;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.network_constraints.NetworkConstraint;

public class BluetoothDiscoverabilityConstraint extends NetworkConstraint {

    public BluetoothDiscoverabilityConstraint(Context context) {
        super(context);
    }

    @Override
    public void resolve() {

    }

    @Override
    public boolean isResolved() {
        return true;
    }

    @Override
    public void cleanResources() {

    }
}
