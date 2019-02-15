package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.network_constraints.NetworkConstraintsResolver;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.bluetooth.BluetoothServer;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.internet.InternetServer;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.nsd.NsdServer;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.wifi_p2p.WifiP2pServer;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.repository.AttackRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.repository.FirebaseRepository;
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
    public static final String ACTION_SERVER_STATUS = "action server status broadcasted";
    public static final String EXTRA_SERVER_STATUS = "extra server status";
    public static final String EXTRA_SERVER_WEBSITE = TAG + "extra website";

    protected Context context;
    protected Attack attack;
    protected AttackRepository repository;
    protected NetworkConstraintsResolver constraintsResolver;
    protected ExecutorService executor;

    public Server(Context context, Attack attack) {
        initializeFields(context, attack);
    }

    private void initializeFields(Context context, Attack attack) {
        this.context = context;
        this.attack = attack;
        this.executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        this.repository = new FirebaseRepository();
        initializeConstraintsResolver();
    }

    private void initializeConstraintsResolver() {
        NetworkConstraintsResolver.Builder builder = new NetworkConstraintsResolver.BuilderImp();
        constraintsResolver = builder.build(context, attack.getNetworkType(), this);
        constraintsResolver.setOnConstraintsResolveListener(this);
    }

    public abstract void start();

    public void stop() {
        constraintsResolver.releaseResources();
        shutdownThreadPool();
        repository.delete(attack.getPushId());
        repository.removeOnRepositoryChangeListener();
        context = null;
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

    public final String getAttackedWebsite() {
        return attack.getWebsite();
    }

    public interface Status {
        int RUNNING = 10;
        int STOPPED = 11;
        int ERROR = 12;
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

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof Server))
            return false;

        Server server = (Server) obj;
        return this.getAttackedWebsite().equals(server.getAttackedWebsite());
    }

    //  This technique is taken from the book Effective Java, Second Edition, Item 9
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.getAttackedWebsite().hashCode();
        return result;
    }
}
