package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.client;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.network_constraints.NetworkConstraintsResolver;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attacks;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants;

import static gr.kalymnos.sk3m3l10.ddosdroid.utils.BluetoothUtils.isThisDevicePairedWith;

class BluetoothClientConnection extends ClientConnection implements NetworkConstraintsResolver.OnConstraintsResolveListener {
    private NetworkConstraintsResolver constraintsResolver;
    private Thread discoveryTask;
    private BroadcastReceiver deviceDiscoveryReceiver;

    BluetoothClientConnection(Context context, Attack attack) {
        super(context, attack);
        initializeFields(context, attack);
    }

    private void initializeFields(Context context, Attack attack) {
        initializeResolver(context);
        initializeDiscoveryTask();
        initializeDiscoveryReceiver(attack);
    }

    private void initializeResolver(Context context) {
        NetworkConstraintsResolver.Builder builder = new NetworkConstraintsResolver.BuilderImp();
        constraintsResolver = builder.build(context, Constants.NetworkType.BLUETOOTH);
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
                    String discoveredDeviceMacAddress = discoveredDevice.getAddress();
                    String serverMacAddress = Attacks.getMacAddress(attack);
                    boolean serverDeviceDiscovered = discoveredDeviceMacAddress.equals(serverMacAddress);
                    if (serverDeviceDiscovered) {
                        //  TODO: connect with the server device
                    }
                }
            }
        };
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
        super.releaseResources();
    }

    @Override
    public void onConstraintsResolved() {
        String serverMacAddress = Attacks.getMacAddress(attack);
        if (isThisDevicePairedWith(serverMacAddress)) {
            //  TODO: connect with the server device
        } else {
            discoveryTask.start();
        }
    }

    @Override
    public void onConstraintResolveFailure() {
        connectionListener.onConnectionError();
        releaseResources();
    }
}
