package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.client;

import android.content.Context;
import android.util.Log;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.repository.AttackRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.repository.FirebaseRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.service.AttackScript;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.service.AttackService;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

public class Client implements ConnectionManager.ConnectionManagerListener, AttackRepository.OnRepositoryChangeListener {
    private static final String TAG = "Client";

    private Context context;
    private Attack attack;
    private AttackScript attackScript;
    private AttackRepository repository;
    private ConnectionManager connectionManager;
    private ClientConnectionListener callback;

    public interface ClientConnectionListener {
        void onClientConnected(Client thisClient, Attack attack);

        void onClientConnectionError();

        void onClientDisconnected(Client thisClient, Attack attack);
    }

    public Client(Context context, Attack attack) {
        initializeFields(context, attack);
    }

    private void initializeFields(Context context, Attack attack) {
        this.context = context;
        this.attack = attack;
        this.attackScript = new AttackScript(attack.getWebsite());
        this.repository = new FirebaseRepository();
        this.repository.addOnRepositoryChangeListener(this);
        initializeConnectionManager();
    }

    private void initializeConnectionManager() {
        ConnectionManager.Factory factory = new ConnectionManager.FactoryImp();
        connectionManager = factory.create(context, attack);
        connectionManager.setConnectionManagerListener(this);
    }

    public void setClientConnectionListener(ClientConnectionListener listener) {
        this.callback = listener;
    }

    public void connect() {
        connectionManager.connectToServer();
    }

    public void disconnect() {
        connectionManager.disconnectFromServer();
    }

    @Override
    public void onManagerConnection() {
        repository.startListenForChanges();
        attackScript.start();
        callback.onClientConnected(this, attack);
    }

    @Override
    public void onManagerError() {
        callback.onClientConnectionError();
    }

    @Override
    public void onManagerDisconnection() {
        connectionManager.releaseResources();
        callback.onClientDisconnected(this, attack);
    }

    private void releaseResources() {
        context = null;
        callback = null;
        attackScript.stopExecution();
        repository.stopListenForChanges();
    }

    public final String getAttackedWebsite() {
        return attack.getWebsite();
    }

    @Override
    public void onAttackUpload(Attack attack) {
        Log.d(TAG, "onAttackUpload()");
    }

    @Override
    public void onAttackUpdate(Attack changedAttack) {
        Log.d(TAG, "onAttackUpdate()");
    }

    @Override
    public void onAttackDelete(Attack deletedAttack) {
        AttackService.Action.stopAttack(attack, context);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof Client))
            return false;

        Client client = (Client) obj;
        return this.getAttackedWebsite().equals(client.getAttackedWebsite());
    }

    //  This technique is taken from the book Effective Java, Second Edition, Item 9
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + getAttackedWebsite().hashCode();
        return result;
    }
}