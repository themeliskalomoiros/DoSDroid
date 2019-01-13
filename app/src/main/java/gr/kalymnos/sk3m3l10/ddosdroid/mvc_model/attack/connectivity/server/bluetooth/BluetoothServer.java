package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

import gr.kalymnos.sk3m3l10.ddosdroid.BuildConfig;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.Server;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.ServersHost;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.status.ServerStatusBroadcaster;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

import static android.bluetooth.BluetoothAdapter.ACTION_STATE_CHANGED;
import static android.bluetooth.BluetoothAdapter.EXTRA_STATE;
import static android.bluetooth.BluetoothAdapter.STATE_OFF;

public class BluetoothServer extends Server {
    private BroadcastReceiver bluetoothStateReceiver;
    private BluetoothServerSocket serverSocket;
    private Thread acceptSocketThread;

    public BluetoothServer(Context context, Attack attack) {
        super(context, attack);
        initializeAcceptSocketThread();
        initializeBluetoothReceiver();
        registerBluetoothStateReceiver(context);
    }

    private void initializeAcceptSocketThread() {
        acceptSocketThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    BluetoothSocket socket = serverSocket.accept();
                    executor.execute(new BluetoothServerTask(socket));
                } catch (IOException e) {
                    Log.e(TAG, "Error creating BluetoothSocket", e);
                }
            }
            closeServerSocket();
        });
    }

    private void closeServerSocket() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Error while closing BluetoothServerSocket", e);
        }
    }

    private void initializeBluetoothReceiver() {
        bluetoothStateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean stateChanged = intent.getAction().equals(ACTION_STATE_CHANGED);
                if (stateChanged) {
                    int state = intent.getIntExtra(EXTRA_STATE, STATE_OFF);
                    if (state == STATE_OFF) {
                        ServersHost.Action.stopServer(context, getId());
                    }
                }
            }
        };
    }

    private void registerBluetoothStateReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_STATE_CHANGED);
        context.registerReceiver(bluetoothStateReceiver, filter);
    }

    @Override
    public void start() {
        constraintsResolver.resolveConstraints();
    }

    @Override
    public void stop() {
        acceptSocketThread.interrupt();
        context.unregisterReceiver(bluetoothStateReceiver);
        super.stop();
    }

    @Override
    public void onConstraintsResolved() {
        ServerStatusBroadcaster.broadcastRunning(getId(), LocalBroadcastManager.getInstance(context));
        initializeServerSocket();
        acceptSocketThread.start(); // start accepting clients
    }

    private void initializeServerSocket() {
        try {
            UUID uuid = UUID.fromString(attack.getPushId());
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            serverSocket = adapter.listenUsingRfcommWithServiceRecord(BuildConfig.APPLICATION_ID, uuid);
        } catch (IOException e) {
            Log.e(TAG, "Error creating BluetoothServerSocket", e);
            ServerStatusBroadcaster.broadcastStopped(getId(), LocalBroadcastManager.getInstance(context));
        }
    }

    @Override
    public void onConstraintResolveFailure() {
        ServerStatusBroadcaster.broadcastError(getId(), LocalBroadcastManager.getInstance(context));
    }
}
