package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.client;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.network_constraints.NetworkConstraintsResolver;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attacks;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants;
import gr.kalymnos.sk3m3l10.ddosdroid.utils.BluetoothDeviceUtils;

class BluetoothConnectionManager extends ConnectionManager implements NetworkConstraintsResolver.OnConstraintsResolveListener,
        BluetoothConnectionThread.OnBluetoothConnectionListener {
    private NetworkConstraintsResolver constraintsResolver;
    private Thread discoveryTask;
    private BroadcastReceiver deviceDiscoveryReceiver;

    BluetoothConnectionManager(Context context, Attack attack) {
        super(context, attack);
        initializeFields(context, attack);
        registerDiscoveryReceiver(context);
    }

    private void initializeFields(Context context, Attack attack) {
        initializeResolver(context);
        initializeDiscoveryTask();
        initializeDiscoveryReceiver(attack);
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
                connectionListener.onConnectionError();
                disconnect();
            }
        });
    }

    private void initializeDiscoveryReceiver(Attack attack) {
        deviceDiscoveryReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice discoveredDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (isServerDeviceDiscovered(discoveredDevice)) {
                        context.unregisterReceiver(this);
                        BluetoothConnectionThread.startAnInstance(discoveredDevice, Attacks.getUUID(attack), BluetoothConnectionManager.this);
                    }
                }
            }

            private boolean isServerDeviceDiscovered(BluetoothDevice discoveredDevice) {
                String discoveredDeviceMacAddress = discoveredDevice.getAddress();
                String serverMacAddress = Attacks.getMacAddress(attack);
                return discoveredDeviceMacAddress.equals(serverMacAddress);
            }
        };
    }

    private void registerDiscoveryReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(deviceDiscoveryReceiver, filter);
    }

    @Override
    void connect() {
        constraintsResolver.resolveConstraints();
    }

    @Override
    void disconnect() {
        releaseResources();
    }

    @Override
    protected void releaseResources() {
        constraintsResolver.releaseResources();
        context.unregisterReceiver(deviceDiscoveryReceiver);
        super.releaseResources();
    }

    @Override
    public void onConstraintsResolved() {
        String serverMacAddress = Attacks.getMacAddress(attack);
        if (BluetoothDeviceUtils.isThisDevicePairedWith(serverMacAddress)) {
            BluetoothConnectionThread.startAnInstance(BluetoothDeviceUtils.getPairedBluetoothDeviceOf(serverMacAddress),
                    Attacks.getUUID(attack), this);
        } else {
            discoveryTask.start();
        }
    }

    @Override
    public void onConstraintResolveFailure() {
        connectionListener.onConnectionError();
        releaseResources();
    }

    @Override
    public void onBluetoothConnectionSuccess() {
        connectionListener.onConnected(attack);
    }

    @Override
    public void onBluetoothConnectionFailure() {
        connectionListener.onConnectionError();
    }
}
