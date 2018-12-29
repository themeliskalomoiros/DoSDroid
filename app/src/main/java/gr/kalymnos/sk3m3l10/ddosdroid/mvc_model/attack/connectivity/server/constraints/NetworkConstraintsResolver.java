package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.constraints;

import android.content.Context;

import java.util.LinkedList;
import java.util.Queue;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.BLUETOOTH;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.INTERNET;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.NSD;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.WIFI_P2P;

public class NetworkConstraintsResolver implements NetworkConstraint.OnResolveConstraintListener{
    private static final String TAG = "NetworkConstraintsResol";

    private Queue<NetworkConstraint> constraints;

    private NetworkConstraintsResolver() {
        constraints = new LinkedList<>();
    }

    public void resolveConstraints(Context context) {
        while (!constraints.isEmpty())
            constraints.poll().resolve(context);
    }

    protected void addConstraint(NetworkConstraint constraint) {
        constraints.add(constraint);
    }

    @Override
    public void onConstraintResolved(Context context, NetworkConstraint constraint) {

    }

    @Override
    public void onConstraintResolveFailed(Context context, NetworkConstraint constraint) {

    }

    public interface Builder {
        NetworkConstraintsResolver build(int networkType);
    }

    public static class BuilderImp implements Builder {

        @Override
        public NetworkConstraintsResolver build(int networkType) {
            switch (networkType) {
                case INTERNET:
                    return new InternetConstraintResolver();
                case BLUETOOTH:
                    return new BluetoothConstraintResolver();
                case WIFI_P2P:
                    return new WifiP2pConstraintResolver();
                case NSD:
                    return new NsdConstraintResolver();
                default:
                    throw new IllegalArgumentException(TAG + ": Unknown attack network type");
            }
        }
    }

    private static class InternetConstraintResolver extends NetworkConstraintsResolver {
        public InternetConstraintResolver() {
            addConstraint(new InternetConstraint());
        }
    }

    private static class BluetoothConstraintResolver extends NetworkConstraintsResolver {
        // TODO: set the NetworkConstraint's in the queue
    }

    private static class WifiP2pConstraintResolver extends NetworkConstraintsResolver {
        // TODO: set the NetworkConstraint's in the queue
    }

    private static class NsdConstraintResolver extends NetworkConstraintsResolver {
        // TODO: set the NetworkConstraint's in the queue
    }
}
