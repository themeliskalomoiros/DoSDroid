package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.client;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

public class Client implements ClientConnection.ConnectionListener {
    private ClientConnection clientConnection;

    public void connect(Attack attack) {
        initializeClientConnectionIfNotNull(attack);
        clientConnection.connect();
    }

    private void initializeClientConnectionIfNotNull(Attack attack) {
        if (clientConnection == null) {
            ClientConnection.Factory factory = new ClientConnection.FactoryImp();
            clientConnection = factory.create(attack);
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