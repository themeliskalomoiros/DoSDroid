package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.client;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Looper;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.network_constraints.NetworkConstraintsResolver;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

class WifiP2PConnectionManager extends ConnectionManager implements NetworkConstraintsResolver.OnConstraintsResolveListener {
    private NetworkConstraintsResolver constraintsResolver;
    private WifiP2pManager.Channel channel;
    private WifiP2pManager wifiP2pManager;

    WifiP2PConnectionManager(Context context, Attack attack) {
        super(context, attack);
        initializeFields(context, attack);
    }

    private void initializeFields(Context context, Attack attack) {
        initializeConstraintsResolver(context, attack);
        wifiP2pManager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
    }

    private void initializeConstraintsResolver(Context context, Attack attack) {
        NetworkConstraintsResolver.Builder builder = new NetworkConstraintsResolver.BuilderImp();
        constraintsResolver = builder.build(context, attack.getNetworkType());
        constraintsResolver.setOnConstraintsResolveListener(this);
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
        super.releaseResources();
    }

    @Override
    public void onConstraintsResolved() {
        channel = wifiP2pManager.initialize(context, Looper.getMainLooper(), null);
    }

    @Override
    public void onConstraintResolveFailure() {
        client.onManagerError();
        releaseResources();
    }
}
