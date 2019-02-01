package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.client;

import android.content.Context;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

public class Client implements ConnectionManager.ConnectionListener {
    private ConnectionManager connectionManager;
    private ClientConnectionListener callback;
    private Attack attack;

    public interface ClientConnectionListener {
        void onClientConnected(Client thisClient, Attack attack);

        void onClientConnectionError();

        void onClientDisconnected(Client thisClient, Attack attack);
    }

    public void setClientConnectionListener(ClientConnectionListener listener) {
        this.callback = listener;
    }

    public void connect(Context context, Attack attack) {
        this.attack = attack;
        initializeConnectionManagerIfNotNull(context);
        connectionManager.connect();
    }

    private void initializeConnectionManagerIfNotNull(Context context) {
        if (connectionManager == null) {
            ConnectionManager.Factory factory = new ConnectionManager.FactoryImp();
            connectionManager = factory.create(context, attack);
            connectionManager.setConnectionListener(this);
        }
    }

    public void disconnect() {
        connectionManager.disconnect();
    }

    @Override
    public void onConnected(Attack attack) {
        callback.onClientConnected(this, attack);
    }

    @Override
    public void onConnectionError() {
        callback.onClientConnectionError();
    }

    @Override
    public void onDisconnected(Attack attack) {
        callback.onClientDisconnected(this, attack);
    }
}