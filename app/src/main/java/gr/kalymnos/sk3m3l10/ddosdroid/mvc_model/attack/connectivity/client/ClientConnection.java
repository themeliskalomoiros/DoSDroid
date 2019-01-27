package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.client;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

abstract class ClientConnection {
    private Attack attack;
    private ConnectionListener connectionListener;

    interface ConnectionListener {
        void onConnected();

        void onConnectionError();

        void onDisconnected();

    }

    ClientConnection(Attack attack) {
        this.attack = attack;
    }

    void setConnectionListener(ConnectionListener connectionListener) {
        this.connectionListener = connectionListener;
    }

    abstract void connect();

    abstract void disconnect();

}
