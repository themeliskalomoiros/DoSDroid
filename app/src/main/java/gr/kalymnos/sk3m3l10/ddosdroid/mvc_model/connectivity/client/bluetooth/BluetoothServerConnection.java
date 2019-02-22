package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.client.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import gr.kalymnos.sk3m3l10.ddosdroid.constants.NetworkTypes;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.client.ServerConnection;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.network_constraints.NetworkConstraintsResolver;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attacks;

import static gr.kalymnos.sk3m3l10.ddosdroid.utils.connectivity.BluetoothDeviceUtil.getPairedDeviceOf;
import static gr.kalymnos.sk3m3l10.ddosdroid.utils.connectivity.BluetoothDeviceUtil.isLocalDevicePairedWithServerOf;

public class BluetoothServerConnection extends ServerConnection implements NetworkConstraintsResolver.OnConstraintsResolveListener,
        BluetoothConnectionThread.OnBluetoothServerResponseListener {
    private Thread discoveryTask;
    private NetworkConstraintsResolver constraintsResolver;
    private BroadcastReceiver deviceDiscoveryReceiver, permissionReceiver;

    public BluetoothServerConnection(Context context, Attack attack) {
        super(context, attack);
        initFields(context, attack);
        registerReceivers(context);
    }

    private void initFields(Context context, Attack attack) {
        initResolver(context);
        initDiscoveryTask();
        initDiscoveryReceiver(attack);
        initPermissionReceiver();
    }

    private void initResolver(Context context) {
        NetworkConstraintsResolver.Builder builder = new NetworkConstraintsResolver.BuilderImp();
        constraintsResolver = builder.build(context, NetworkTypes.BLUETOOTH);
        constraintsResolver.setOnConstraintsResolveListener(this);
    }

    private void initDiscoveryTask() {
        discoveryTask = new Thread(() -> {
            boolean started = startDiscovery();
            if (!started) {
                Log.d(TAG, "Device discovery failed to initiate");
                connectionListener.onServerConnectionError();
            }
        });
    }

    private boolean startDiscovery() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        return adapter.startDiscovery();
    }

    private void initDiscoveryReceiver(Attack attack) {
        deviceDiscoveryReceiver = new BroadcastReceiver() {
            private boolean firstTimeDiscoveredServer = true;

            @Override
            public void onReceive(Context context, Intent intent) {
                boolean foundDevice = BluetoothDevice.ACTION_FOUND.equals(intent.getAction());
                if (foundDevice) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (isServer(device) && firstTimeDiscoveredServer) {
                        BluetoothConnectionThread.startInstance(device, Attacks.getHostUUID(attack), BluetoothServerConnection.this);
                        firstTimeDiscoveredServer = false;
                    }
                }
            }

            private boolean isServer(BluetoothDevice device) {
                String deviceAddress = device.getAddress(); // MAC address
                String serverAddress = Attacks.getHostMacAddress(attack);
                return deviceAddress.equals(serverAddress);
            }
        };
    }

    private void initPermissionReceiver() {
        permissionReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case RequestLocationPermissionForBluetoothActivity.ACTION_PERMISSION_GRANTED:
                        discoveryTask.start();
                        break;
                    case RequestLocationPermissionForBluetoothActivity.ACTION_PERMISSION_DENIED:
                        connectionListener.onServerConnectionError();
                        break;
                    default:
                        throw new IllegalArgumentException(TAG + ": Unknown action");
                }
            }
        };
    }

    private void registerReceivers(Context context) {
        registerDiscoveryReceiver(context);
        registerPermissionReceiver(context);
    }

    private void registerDiscoveryReceiver(Context context) {
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(deviceDiscoveryReceiver, filter);
    }

    private void registerPermissionReceiver(Context context) {
        IntentFilter filter = getPermissionReceiverFilter();
        LocalBroadcastManager.getInstance(context).registerReceiver(permissionReceiver, filter);
    }

    @NonNull
    private IntentFilter getPermissionReceiverFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(RequestLocationPermissionForBluetoothActivity.ACTION_PERMISSION_GRANTED);
        filter.addAction(RequestLocationPermissionForBluetoothActivity.ACTION_PERMISSION_DENIED);
        return filter;
    }

    @Override
    public void connectToServer() {
        constraintsResolver.resolveConstraints();
    }

    @Override
    public void disconnectFromServer() {
        connectionListener.onServerDisconnection();
    }

    @Override
    protected void releaseResources() {
        constraintsResolver.releaseResources();
        unregisterReceivers();
        super.releaseResources();
    }

    private void unregisterReceivers() {
        try {
            context.unregisterReceiver(deviceDiscoveryReceiver);
            LocalBroadcastManager.getInstance(context).unregisterReceiver(permissionReceiver);
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "Tried to unregister an already unregisted receiver");
        }
    }

    @Override
    public void onConstraintsResolved() {
        String serverAddress = Attacks.getHostMacAddress(attack);
        if (isLocalDevicePairedWithServerOf(serverAddress)) {
            BluetoothConnectionThread.startInstance(getPairedDeviceOf(serverAddress), Attacks.getHostUUID(attack), this);
        } else {
            RequestLocationPermissionForBluetoothActivity.requestPermission(context);
        }
    }

    @Override
    public void onConstraintResolveFailure() {
        connectionListener.onServerConnectionError();
    }

    @Override
    public void onServerResponseReceived() {
        connectionListener.onServerConnection();
    }

    @Override
    public void onServerResponseError() {
        connectionListener.onServerConnectionError();
    }
}
