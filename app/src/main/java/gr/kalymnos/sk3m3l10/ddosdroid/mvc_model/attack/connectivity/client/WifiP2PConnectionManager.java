package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.client;

import android.content.Context;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.network_constraints.NetworkConstraintsResolver;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

class WifiP2PConnectionManager extends ConnectionManager implements NetworkConstraintsResolver.OnConstraintsResolveListener {
    private NetworkConstraintsResolver constraintsResolver;

    WifiP2PConnectionManager(Context context, Attack attack) {
        super(context, attack);
        initializeConstraintsResolver(context, attack);
    }

    private void initializeConstraintsResolver(Context context, Attack attack) {
        NetworkConstraintsResolver.Builder builder = new NetworkConstraintsResolver.BuilderImp();
        constraintsResolver = builder.build(context, attack.getNetworkType());
        constraintsResolver.setOnConstraintsResolveListener(this);
    }

    @Override
    void connect() {

    }

    @Override
    void disconnect() {

    }

    @Override
    protected void releaseResources() {
        super.releaseResources();
    }

    @Override
    public void onConstraintsResolved() {

    }

    @Override
    public void onConstraintResolveFailure() {

    }
}
