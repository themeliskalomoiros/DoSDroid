package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.Server;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.ServersHost;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.status.ServerStatusBroadcaster;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

import static android.bluetooth.BluetoothAdapter.ACTION_STATE_CHANGED;
import static android.bluetooth.BluetoothAdapter.EXTRA_STATE;
import static android.bluetooth.BluetoothAdapter.STATE_OFF;

public class BluetoothServer extends Server {
    private BroadcastReceiver bluetoothStateReceiver;

    public BluetoothServer(Context context, Attack attack) {
        super(context, attack);
        initializeBluetoothReceiver();
        registerBluetoothStateReceiver(context);
    }

    private void initializeBluetoothReceiver() {
        bluetoothStateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean stateChanged = intent.getAction().equals(ACTION_STATE_CHANGED);
                if (stateChanged){
                    int state = intent.getIntExtra(EXTRA_STATE,STATE_OFF);
                    if (state == STATE_OFF){
                        ServersHost.Action.stopServer(context,getId());
                    }
                }
            }
        };
    }

    private void registerBluetoothStateReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_STATE_CHANGED);
        context.registerReceiver(bluetoothStateReceiver,filter);
    }

    @Override
    public void start() {
        constraintsResolver.resolveConstraints();
    }

    @Override
    public void stop() {
        context.unregisterReceiver(bluetoothStateReceiver);
        super.stop();
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
