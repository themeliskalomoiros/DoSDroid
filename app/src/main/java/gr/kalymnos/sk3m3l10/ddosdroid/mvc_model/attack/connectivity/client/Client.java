package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.client;

import android.content.Context;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

public class Client implements ClientConnection.ConnectionListener {
    private ClientConnection clientConnection;

    public void connect(Context context, Attack attack) {
        initializeClientConnectionIfNotNull(context, attack);
        clientConnection.connect();
    }

    private void initializeClientConnectionIfNotNull(Context context, Attack attack) {
        if (clientConnection == null) {
            ClientConnection.Factory factory = new ClientConnection.FactoryImp();
            clientConnection = factory.create(context, attack);
            clientConnection.setConnectionListener(this);
        }
    }

    public void disconnect() {
        clientConnection.disconnect();
    }

    @Override
    public void onConnected() {

    }

    @Override
    public void onConnectionError() {

    }

    @Override
    public void onDisconnected() {

    }
}