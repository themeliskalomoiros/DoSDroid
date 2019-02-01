package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.client;

import android.content.Context;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.repository.AttackDeletionReporter;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.repository.FirebaseAttackDeletionReporter;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

public class Client implements ConnectionManager.ConnectionListener, AttackDeletionReporter.AttackDeletionListener {
    private Attack attack;
    private AttackDeletionReporter attackDeletionReporter;
    private ConnectionManager connectionManager;
    private ClientConnectionListener callback;

    public interface ClientConnectionListener {
        void onClientConnected(Client thisClient, Attack attack);

        void onClientConnectionError();

        void onClientDisconnected(Client thisClient, Attack attack);
    }

    public void setClientConnectionListener(ClientConnectionListener listener) {
        this.callback = listener;
    }

    public void connect(Context context, Attack attack) {
        initializeFields(context, attack);
        attackDeletionReporter.attach();
        connectionManager.connect();
    }

    private void initializeFields(Context context, Attack attack) {
        this.attack = attack;
        this.attackDeletionReporter = new FirebaseAttackDeletionReporter();
        this.attackDeletionReporter.setAttackDeletionListener(this);
        initializeConnectionManagerIfNotNull(context);
    }

    private void initializeConnectionManagerIfNotNull(Context context) {
        if (connectionManager == null) {
            ConnectionManager.Factory factory = new ConnectionManager.FactoryImp();
            connectionManager = factory.create(context, attack);
            connectionManager.setConnectionListener(this);
        }
    }

    public void disconnect() {
        attackDeletionReporter.detach();
        connectionManager.disconnect();
    }

    @Override
    public void onConnected() {
        callback.onClientConnected(this, attack);
    }

    @Override
    public void onConnectionError() {
        callback.onClientConnectionError();
    }

    @Override
    public void onDisconnected() {
        callback.onClientDisconnected(this, attack);
    }

    @Override
    public void onAttackDeleted(Attack attack) {
        disconnect();
    }
}