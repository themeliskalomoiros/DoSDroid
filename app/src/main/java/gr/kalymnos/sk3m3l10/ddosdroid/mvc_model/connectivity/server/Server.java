package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.server;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.network_constraints.NetworkConstraintsResolver;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.server.bluetooth.BluetoothServer;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.server.internet.InternetServer;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.server.wifi.nsd.NsdServer;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.server.wifi.p2p.WifiP2pServer;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.repository.AttackRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.repository.FirebaseRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

import static gr.kalymnos.sk3m3l10.ddosdroid.constants.NetworkTypes.BLUETOOTH;
import static gr.kalymnos.sk3m3l10.ddosdroid.constants.NetworkTypes.INTERNET;
import static gr.kalymnos.sk3m3l10.ddosdroid.constants.NetworkTypes.NSD;
import static gr.kalymnos.sk3m3l10.ddosdroid.constants.NetworkTypes.WIFI_P2P;

/*Scenario to eliminate duplication:
 * Server should not abstract anymore since all the subclasses are implementing
 * the abstract methods the same way.
 * */

public abstract class Server implements NetworkConstraintsResolver.OnConstraintsResolveListener {
    protected static final String TAG = "MyServer";
    public static final String RESPONSE = "this_attack_has_started";
    private static final int THREAD_POOL_SIZE = 10;
    public static final String ACTION_SERVER_STATUS = "action_server_status_broadcasted";

    protected Attack attack;
    protected AttackRepository repo;

    protected Context context;
    protected ExecutorService executor;
    protected NetworkConstraintsResolver constraintsResolver;
    protected ServerStatusListener statusListener;

    public interface ServerStatusListener {
        void onServerRunning(String key);

        void onServerStopped(String key);

        void onServerError(String key);
    }

    public Server(Context context, Attack attack) {
        initFields(context, attack);
    }

    private void initFields(Context context, Attack attack) {
        this.context = context;
        this.attack = attack;
        this.executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        this.repo = new FirebaseRepository();
        initConstraintsResolver();
    }

    private void initConstraintsResolver() {
        NetworkConstraintsResolver.Builder builder = new NetworkConstraintsResolver.BuilderImp();
        constraintsResolver = builder.build(context, attack.getNetworkType(), this);
        constraintsResolver.setOnConstraintsResolveListener(this);
    }

    public void setServerStatusListener(ServerStatusListener statusListener) {
        this.statusListener = statusListener;
    }

    public abstract void start();

    public void stop() {
        constraintsResolver.releaseResources();
        shutdownThreadPool();
        repo.delete(attack.getPushId());
        repo.removeOnRepositoryChangeListener();
        context = null;
        statusListener = null;
    }

    private void shutdownThreadPool() {
        // https://www.baeldung.com/java-executor-service-tutorial
        executor.shutdown();
        try {
            if (executor.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }

    public final String getAttackingWebsite() {
        return attack.getWebsite();
    }

    public interface Builder {
        Server build(Context context, Attack attack);
    }

    public static class BuilderImp implements Server.Builder {

        @Override
        public Server build(Context context, Attack attack) {
            switch (attack.getNetworkType()) {
                case INTERNET:
                    return new InternetServer(context, attack);
                case BLUETOOTH:
                    return new BluetoothServer(context, attack);
                case WIFI_P2P:
                    return new WifiP2pServer(context, attack);
                case NSD:
                    return new NsdServer(context, attack);
                default:
                    throw new IllegalArgumentException(TAG + ": Unknown attack network type");
            }
        }
    }

    public static boolean isValid(String serverResponse) {
        return serverResponse.equals(RESPONSE);
    }
}
