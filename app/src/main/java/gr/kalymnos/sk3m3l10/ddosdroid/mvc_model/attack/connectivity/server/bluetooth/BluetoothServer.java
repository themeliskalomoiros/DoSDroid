package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.Server;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.ServersHost;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.status.ServerStatusBroadcaster;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

public class BluetoothServer extends Server {
    private BroadcastReceiver bluetoothStateReceiver;

    public BluetoothServer(Context context, Attack attack) {
        super(context, attack);
        initializeBluetoothReceiver();
    }

    private void initializeBluetoothReceiver() {
        bluetoothStateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean stateChanged = intent.getAction().equals(BluetoothAdapter.ACTION_STATE_CHANGED);
                if (stateChanged){
                    int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,BluetoothAdapter.STATE_OFF);
                    if (state == BluetoothAdapter.STATE_OFF){
                        ServersHost.Action.stopServer(context,getId());
                    }
                }
            }
        };
    }

    @Override
    public void start() {
        constraintsResolver.resolveConstraints();
    }

    @Override
    public void onConstraintsResolved() {
        ServerStatusBroadcaster.broadcastRunning(getId(), LocalBroadcastManager.getInstance(context));
    }

    @Override
    public void onConstraintResolveFailure() {
        ServerStatusBroadcaster.broadcastError(getId(), LocalBroadcastManager.getInstance(context));
    }
}
