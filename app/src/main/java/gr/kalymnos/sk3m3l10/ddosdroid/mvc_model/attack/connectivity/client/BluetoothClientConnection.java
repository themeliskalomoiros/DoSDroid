package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.client;

import android.content.Context;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.network_constraints.NetworkConstraintsResolver;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants;
import gr.kalymnos.sk3m3l10.ddosdroid.utils.BluetoothUtils;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.Extra.EXTRA_MAC_ADDRESS;
import static gr.kalymnos.sk3m3l10.ddosdroid.utils.BluetoothUtils.isThisDevicePairedWith;

class BluetoothClientConnection extends ClientConnection implements NetworkConstraintsResolver.OnConstraintsResolveListener {
    private NetworkConstraintsResolver constraintsResolver;

    BluetoothClientConnection(Context context, Attack attack) {
        super(context, attack);
        initializeResolver(context);
    }

    private void initializeResolver(Context context) {
        NetworkConstraintsResolver.Builder builder = new NetworkConstraintsResolver.BuilderImp();
        constraintsResolver = builder.build(context, Constants.NetworkType.BLUETOOTH);
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
