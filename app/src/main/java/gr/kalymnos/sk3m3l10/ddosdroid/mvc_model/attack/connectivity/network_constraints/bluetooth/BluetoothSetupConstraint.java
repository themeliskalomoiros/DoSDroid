package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.network_constraints.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.network_constraints.NetworkConstraint;

public class BluetoothSetupConstraint extends NetworkConstraint {
    private BluetoothAdapter bluetoothAdapter;

    public BluetoothSetupConstraint(Context context) {
        super(context);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public void resolve() {
        if (isResolved()) {
            callback.onConstraintResolved(context, this);
        } else {
            callback.onConstraintResolveFailed(context, this);
        }
    }

    @Override
    public boolean isResolved() {
        return isBluetoothSupported();
    }

    private boolean isBluetoothSupported() {
        if (bluetoothAdapter == null) {
            return false;
        }
        return true;
    }

    @Override
    public void releaseResources() {
        super.releaseResources();
    }
}
