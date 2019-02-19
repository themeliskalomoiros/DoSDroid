package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.server.bluetooth;

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
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.server.Server;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.server.status.ServerStatusBroadcaster;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attacks;
import gr.kalymnos.sk3m3l10.ddosdroid.utils.BluetoothDeviceUtil;

import static android.bluetooth.BluetoothAdapter.ACTION_STATE_CHANGED;
import static android.bluetooth.BluetoothAdapter.EXTRA_STATE;
import static android.bluetooth.BluetoothAdapter.STATE_OFF;
import static gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras.EXTRA_ATTACK_HOST_UUID;
import static gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras.EXTRA_MAC_ADDRESS;

public class BluetoothServer extends Server {
    private BroadcastReceiver bluetoothStateReceiver;
    private BluetoothServerSocket serverSocket;
    private Thread acceptClientThread;

    public BluetoothServer(Context context, Attack attack) {
        super(context, attack);
        initFields();
        context.registerReceiver(bluetoothStateReceiver, new IntentFilter(ACTION_STATE_CHANGED));
    }

    private void initFields() {
        initAcceptClientThread();
        initBluetoothReceiver();
    }

    private void initAcceptClientThread() {
        acceptClientThread = new Thread(() -> {
            while (true) {
                try {
                    BluetoothSocket socket = serverSocket.accept();
                    executor.execute(new BluetoothServerThread(socket));
                } catch (SocketException e) {
                    Log.w(TAG, "BluetoothServerSocket probably closed.", e);
                    break;
                } catch (IOException e) {
                    Log.w(TAG, "BluetoothServerSocket probably closed.", e);
                    break;
                }
            }
        });
    }

    private void initBluetoothReceiver() {
        bluetoothStateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean stateChanged = intent.getAction().equals(ACTION_STATE_CHANGED);
                if (stateChanged) {
                    stopIfStateIsOff(intent);
                }
            }

            private void stopIfStateIsOff(Intent intent) {
                int state = intent.getIntExtra(EXTRA_STATE, STATE_OFF);
                if (state == STATE_OFF) {
                    stop();
                }
            }
        };
    }

    @Override
    public void start() {
        constraintsResolver.resolveConstraints();
    }

    @Override
    public void stop() {
        closeServerSocket();
        context.unregisterReceiver(bluetoothStateReceiver);
        ServerStatusBroadcaster.broadcastStopped(getAttackingWebsite(), LocalBroadcastManager.getInstance(context));
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
        boolean serverSocketInitialized = initServerSocket();
        if (serverSocketInitialized) {
            acceptClientThread.start();
            repo.upload(attack);
            ServerStatusBroadcaster.broadcastRunning(getAttackingWebsite(), LocalBroadcastManager.getInstance(context));
        } else {
            ServerStatusBroadcaster.broadcastError(getAttackingWebsite(), LocalBroadcastManager.getInstance(context));
        }
    }

    private void setAttackHostInfo() {
        UUID uuid = UUID.randomUUID();
        String macAddress = BluetoothDeviceUtil.getLocalMacAddress(context);
        attack.addSingleHostInfo(EXTRA_ATTACK_HOST_UUID, uuid.toString());
        attack.addSingleHostInfo(EXTRA_MAC_ADDRESS, macAddress);
    }

    private boolean initServerSocket() {
        //  Tip: can be initialized inside constructor if attackHostInfo can be set inside constructor
        try {
            setAttackHostInfo();
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            serverSocket = adapter.listenUsingRfcommWithServiceRecord(BuildConfig.APPLICATION_ID, Attacks.getHostUUID(attack));
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Error creating BluetoothServerSocket", e);
            return false;
        }
    }

    @Override
    public void onConstraintResolveFailure() {
        ServerStatusBroadcaster.broadcastError(getAttackingWebsite(), LocalBroadcastManager.getInstance(context));
    }
}
