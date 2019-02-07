package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.client;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.network_constraints.NetworkConstraintsResolver;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attacks;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants;
import gr.kalymnos.sk3m3l10.ddosdroid.utils.BluetoothDeviceUtils;

class BluetoothConnectionManager extends ConnectionManager implements NetworkConstraintsResolver.OnConstraintsResolveListener,
        BluetoothConnectionThread.OnBluetoothConnectionListener {
    private static final String TAG = "BluetoothConnectionMana";

    private Thread discoveryTask;
    private NetworkConstraintsResolver constraintsResolver;
    private BroadcastReceiver deviceDiscoveryReceiver, permissionReceiver;

    BluetoothConnectionManager(Context context, Attack attack) {
        super(context, attack);
        initializeFields(context, attack);
        registerReceivers(context);

    }

    private void initializeFields(Context context, Attack attack) {
        initializeResolver(context);
        initializeDiscoveryTask();
        initializeDiscoveryReceiver(attack);
        initializePermissionReceiver();
    }

    private void initializeResolver(Context context) {
        NetworkConstraintsResolver.Builder builder = new NetworkConstraintsResolver.BuilderImp();
        constraintsResolver = builder.build(context, Constants.NetworkType.BLUETOOTH);
        constraintsResolver.setOnConstraintsResolveListener(this);
    }

    private void initializeDiscoveryTask() {
        discoveryTask = new Thread(() -> {
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            boolean discoveryInitiated = adapter.startDiscovery();
            if (!discoveryInitiated) {
                Log.d(TAG, "Device discovery failed to initiate");
                client.onManagerError();
                releaseResources();
            } else {
                Log.d(TAG, "Device discovery initiated");
            }
        });
    }

    private void initializeDiscoveryReceiver(Attack attack) {
        deviceDiscoveryReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    Log.d(TAG, "A device discovered");
                    BluetoothDevice discoveredDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (isServerDeviceDiscovered(discoveredDevice)) {
                        Log.d(TAG, "Server device discovered, proceeding to connection");
                        BluetoothConnectionThread.startAnInstance(discoveredDevice, Attacks.getHostUUID(attack), BluetoothConnectionManager.this);
                        context.unregisterReceiver(this);
                    }
                }
            }

            private boolean isServerDeviceDiscovered(BluetoothDevice discoveredDevice) {
                String discoveredDeviceMacAddress = discoveredDevice.getAddress();
                String serverMacAddress = Attacks.getHostMacAddress(attack);
                return discoveredDeviceMacAddress.equals(serverMacAddress);
            }
        };
    }

    private void initializePermissionReceiver() {
        permissionReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case RequestLocationPermissionForBluetoothActivity.ACTION_PERMISSION_GRANTED:
                        Log.d(TAG, "Permission granted, starting device discovery.");
                        discoveryTask.start();
                        break;
                    case RequestLocationPermissionForBluetoothActivity.ACTION_PERMISSION_DENIED:
                        Log.d(TAG, "Permission denied, reporting connection error.");
                        client.onManagerError();
                        releaseResources();
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
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(deviceDiscoveryReceiver, filter);
    }

    private void registerPermissionReceiver(Context context) {
        IntentFilter filter = getIntentFilterForPermissionReceiver();
        LocalBroadcastManager.getInstance(context).registerReceiver(permissionReceiver, filter);
    }

    @NonNull
    private IntentFilter getIntentFilterForPermissionReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(RequestLocationPermissionForBluetoothActivity.ACTION_PERMISSION_GRANTED);
        filter.addAction(RequestLocationPermissionForBluetoothActivity.ACTION_PERMISSION_DENIED);
        return filter;
    }

    @Override
    void connect() {
        constraintsResolver.resolveConstraints();
    }

    @Override
    void disconnect() {
        client.onManagerDisconnection();
        releaseResources();
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
        String serverMacAddress = Attacks.getHostMacAddress(attack);
        if (BluetoothDeviceUtils.isLocalDevicePairedWith(serverMacAddress)) {
            Log.d(TAG, "Server device is already paired with device, proceeding to connection");
            BluetoothConnectionThread.startAnInstance(BluetoothDeviceUtils.getPairedBluetoothDeviceOf(serverMacAddress),
                    Attacks.getHostUUID(attack), this);
        } else {
            Log.d(TAG, "Server device was not paired with local device. proceeding to device discovery to find it");
            RequestLocationPermissionForBluetoothActivity.requestUserPermission(context);
        }
    }

    @Override
    public void onConstraintResolveFailure() {
        client.onManagerError();
        releaseResources();
    }

    @Override
    public void onBluetoothConnectionSuccess() {
        client.onManagerConnection();
    }

    @Override
    public void onBluetoothConnectionFailure() {
        client.onManagerError();
    }
}
