package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.network_constraints.NetworkConstraint;

public class BluetoothEnableConstraint extends NetworkConstraint {
    private BluetoothAdapter bluetoothAdapter;

    public BluetoothEnableConstraint(Context context) {
        super(context);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public void resolve() {
        if (isResolved()){
            callback.onConstraintResolved(context,this);
        }else{
            context.startActivity(createEnableBluetoothIntent());
        }
    }

    private Intent createEnableBluetoothIntent() {
        Intent enableIntent = new Intent(context,EnableBluetoothActivity.class);
        enableIntent.setAction(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        return enableIntent;
    }

    @Override
    public boolean isResolved() {
        return bluetoothAdapter.isEnabled();
    }

    @Override
    public void cleanResources() {

    }
}
