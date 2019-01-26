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
import java.net.SocketException;
import java.util.UUID;

import gr.kalymnos.sk3m3l10.ddosdroid.BuildConfig;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.Server;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.ServersHost;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.status.ServerStatusBroadcaster;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attacks;

import static android.bluetooth.BluetoothAdapter.ACTION_STATE_CHANGED;
import static android.bluetooth.BluetoothAdapter.EXTRA_STATE;
import static android.bluetooth.BluetoothAdapter.STATE_OFF;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.Extra.EXTRA_ATTACK_HOST_UUID;

public class BluetoothServer extends Server {
    private BroadcastReceiver bluetoothStateReceiver;
    private BluetoothServerSocket serverSocket;
    private Thread acceptClientThread;

    public BluetoothServer(Context context, Attack attack) {
        super(context, attack);
        initializeAcceptClientThread();
        initializeBluetoothReceiver();
        registerBluetoothStateReceiver(context);
    }

    private void initializeAcceptClientThread() {
        acceptClientThread = new Thread(() -> {
            while (true) {
                try {
                    BluetoothSocket socket = serverSocket.accept();
                    executor.execute(new BluetoothServerThread(socket));
                } catch (SocketException e) {
                    Log.e(TAG, "BluetoothServerSocket probably closed.", e);
                    break;
                } catch (IOException e) {
                    Log.e(TAG, "BluetoothServerSocket probably closed.", e);
                    break;
                }
            }
        });
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
        closeServerSocket();
        context.unregisterReceiver(bluetoothStateReceiver);
        super.stop();
    }

    private void closeServerSocket() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Error while closing BluetoothServerSocket", e);
        }
    }

    @Override
    public void onConstraintsResolved() {
        ServerStatusBroadcaster.broadcastRunning(getId(), LocalBroadcastManager.getInstance(context));
        uploadAttack();
        initializeServerSocket();
        acceptClientThread.start(); // start accepting clients
    }

    private void uploadAttack() {
        UUID uuid = UUID.randomUUID();
        attack.addSingleHostInfo(EXTRA_ATTACK_HOST_UUID, uuid.toString());
        attackRepo.uploadAttack(attack);
    }

    private void initializeServerSocket() {
        try {
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            serverSocket = adapter.listenUsingRfcommWithServiceRecord(BuildConfig.APPLICATION_ID, Attacks.getUUID(attack));
        } catch (IOException e) {
            Log.e(TAG, "Error creating BluetoothServerSocket", e);
            ServersHost.Action.stopServer(context, getId());
        }
    }

    @Override
    public void onConstraintResolveFailure() {
        ServerStatusBroadcaster.broadcastError(getId(), LocalBroadcastManager.getInstance(context));
    }
}
