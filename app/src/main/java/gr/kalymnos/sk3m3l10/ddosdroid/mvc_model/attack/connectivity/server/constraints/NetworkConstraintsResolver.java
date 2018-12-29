package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.constraints;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.LinkedList;
import java.util.Queue;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.BLUETOOTH;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.INTERNET;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.NSD;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.WIFI_P2P;

/*
 * Resolving Mechanism:
 *
 * Resolves the next constraint the queue. The step is repeated until all constraints in the queue
 * are met and then the Server is notified.
 *
 * On the other hand it takes just one failure and the process stops immediately. Server is also
 * notified about the failure.
 */

public class NetworkConstraintsResolver implements NetworkConstraint.OnResolveConstraintListener {
    private static final String TAG = "NetworkConstraintsResol";

    private Queue<NetworkConstraint> constraints;
    protected OnConstraintsResolveListener callback;
    private boolean constraintFailed;

    public interface OnConstraintsResolveListener {
        void onConstraintsResolved();

        void onConstraintResolveFailure();
    }

    private NetworkConstraintsResolver() {
        constraints = new LinkedList<>();
    }

    public void resolveConstraints() {
        resolveNextConstraint();
    }

    private void resolveNextConstraint() {
        if (!constraintFailed) {
            if (constraints.isEmpty()) {
                callback.onConstraintsResolved();
            } else {
                constraints.poll().resolve();
            }
        } else {
            callback.onConstraintResolveFailure();
        }
    }

    @Override
    public void onConstraintResolved(Context context, NetworkConstraint constraint) {
        resolveNextConstraint();
    }

    @Override
    public void onConstraintResolveFailed(Context context, NetworkConstraint constraint) {
        constraintFailed = true;
        resolveNextConstraint();
    }

    protected void addConstraint(NetworkConstraint constraint) {
        constraints.add(constraint);
    }

    public void setOnConstraintsResolveListener(OnConstraintsResolveListener listener) {
        callback = listener;
    }

    public interface Builder {
        NetworkConstraintsResolver build(Context context, int networkType);
    }

    public static class BuilderImp implements Builder {

        @Override
        public NetworkConstraintsResolver build(Context context, int networkType) {
            switch (networkType) {
                case INTERNET:
                    return new InternetConstraintResolver(context);
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
        public InternetConstraintResolver(Context context) {
            addConstraint(createInternetConstraint(context));
        }

        @NonNull
        private InternetConstraint createInternetConstraint(Context context) {
            InternetConstraint constraint = new InternetConstraint(context);
            constraint.setOnResolveConstraintListener(this);
            return constraint;
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
