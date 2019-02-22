package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.client;

import android.content.Context;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack_job.AttackScript;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

/* Note:
 * Client gets disconnected by itself when the attack that is
 * currently following is deleted from the AttackRepository.
 *
 * Details:
 * The above is usually the way where the client knows its server was shutdown.
 * They don't keep a connection for long. Their connection is live until the client
 * receives a response from server. After that client starts attacking.*/

public class Client implements ConnectionToServer.ConnectionToServerListener{
    private static final String TAG = "MyClient";

    private Context context;
    private Attack attack;
    private AttackScript attackScript;

    private ConnectionToServer serverConnection;
    private ClientConnectionListener clientConnectionListener;

    public interface ClientConnectionListener {
        void onClientConnected(String key);

        void onClientConnectionError(String key);

        void onClientDisconnected(Attack attack);
    }

    public Client(Context context, Attack attack) {
        initFields(context, attack);
    }

    private void initFields(Context context, Attack attack) {
        this.context = context;
        this.attack = attack;
        this.attackScript = new AttackScript(attack.getWebsite());
        initServerConnection();
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
        if (!attackScript.isAlive()) {
            attackScript.start();
            clientConnectionListener.onClientConnected(attack.getWebsite());
        }
    }

    @Override
    public void onServerConnectionError() {
        serverConnection.releaseResources();
        clientConnectionListener.onClientConnectionError(attack.getWebsite());
    }

    @Override
    public void onServerDisconnection() {
        releaseResources();
        clientConnectionListener.onClientDisconnected(attack);
        nullReferences();
    }

    private void releaseResources() {
        serverConnection.releaseResources();
        attackScript.stopAttack();
    }

    private void nullReferences() {
        context = null;
        clientConnectionListener = null;
    }

    public final Attack getAttack() {
        return attack;
    }
}