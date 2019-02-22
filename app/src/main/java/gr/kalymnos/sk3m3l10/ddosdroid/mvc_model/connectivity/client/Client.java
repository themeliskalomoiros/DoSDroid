package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.client;

import android.content.Context;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

/* Note:
 * Client gets disconnected by itself when the attack that is
 * currently following is deleted from the AttackRepository.
 *
 * Details:
 * The above is usually the way where the client knows its server was shutdown.
 * They don't keep a connection for long. Their connection is live until the client
 * receives a response from server. After that client starts attacking.*/

public class Client implements ServerConnection.ConnectionToServerListener {
    private static final String TAG = "MyClient";

    private Context context;
    private Attack attack;

    private ServerConnection serverConnection;
    private ClientConnectionListener clientConnectionListener;

    public interface ClientConnectionListener {
        void onClientConnection(Client client);

        void onClientConnectionError(Client client);
    }

    public Client(Context context, Attack attack) {
        initFields(context, attack);
    }

    private void initFields(Context context, Attack attack) {
        this.context = context;
        this.attack = attack;
        initServerConnection();
    }

    private void initServerConnection() {
        ServerConnection.Factory factory = new ServerConnection.FactoryImp();
        serverConnection = factory.create(context, attack);
        serverConnection.setConnectionToServerListener(this);
    }

    public void setClientConnectionListener(ClientConnectionListener listener) {
        this.clientConnectionListener = listener;
    }

    public void connect() {
        serverConnection.connectToServer();
    }

    @Override
    public void onServerConnection() {
        clientConnectionListener.onClientConnection(this);
    }

    @Override
    public void onServerConnectionError() {
        serverConnection.releaseResources();
        clientConnectionListener.onClientConnectionError(this);
    }

    @Override
    public void onServerDisconnection() {
        releaseResources();
        nullReferences();
    }

    private void releaseResources() {
        serverConnection.releaseResources();
    }

    private void nullReferences() {
        context = null;
        clientConnectionListener = null;
    }

    public final Attack getAttack() {
        return attack;
    }
}