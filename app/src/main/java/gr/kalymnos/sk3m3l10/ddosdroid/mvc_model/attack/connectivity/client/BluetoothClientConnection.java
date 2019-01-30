package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.client;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.network_constraints.NetworkConstraintsResolver;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.Extra.EXTRA_MAC_ADDRESS;
import static gr.kalymnos.sk3m3l10.ddosdroid.utils.BluetoothUtils.isThisDevicePairedWith;

class BluetoothClientConnection extends ClientConnection implements NetworkConstraintsResolver.OnConstraintsResolveListener {
    private NetworkConstraintsResolver constraintsResolver;
    private Thread discoveryTask;

    BluetoothClientConnection(Context context, Attack attack) {
        super(context, attack);
        initializeResolver(context);
        initializeDiscoveryTask();
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
        String serverMacAddress = attack.getHostInfo().get(EXTRA_MAC_ADDRESS);
        if (isThisDevicePairedWith(serverMacAddress)) {
            //  TODO: connect with the device
        } else {
            //  TODO: initiate device discovery
        }
    }

    @Override
    public void onConstraintResolveFailure() {
        connectionListener.onConnectionError();
        releaseResources();
    }
}
