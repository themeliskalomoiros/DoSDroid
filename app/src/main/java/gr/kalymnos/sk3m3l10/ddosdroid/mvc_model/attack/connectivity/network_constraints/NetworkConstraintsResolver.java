package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.network_constraints;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.LinkedList;
import java.util.Queue;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.Server;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.network_constraints.bluetooth.BluetoothDiscoverabilityConstraint;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.network_constraints.bluetooth.BluetoothEnableConstraint;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.network_constraints.bluetooth.BluetoothSetupConstraint;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.network_constraints.internet.InternetConstraint;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.wifi_p2p.WifiP2pServer;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.network_constraints.wifi_p2p.WifiP2pEnabledConstraint;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.network_constraints.wifi_p2p.WifiP2pGroupConstraint;

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
        if (constraints.isEmpty()) {
            callback.onConstraintsResolved();
        } else {
            constraints.poll().resolve();
        }
    }

    public void releaseResources() {
        for (NetworkConstraint constraint : constraints) {
            constraint.releaseResources();
        }
    }

    @Override
    public void onConstraintResolved(Context context, NetworkConstraint constraint) {
        resolveNextConstraint();
    }

    @Override
    public void onConstraintResolveFailed(Context context, NetworkConstraint constraint) {
        callback.onConstraintResolveFailure();
    }

    protected void addConstraint(NetworkConstraint constraint) {
        constraints.add(constraint);
    }

    public void setOnConstraintsResolveListener(OnConstraintsResolveListener listener) {
        callback = listener;
    }

    public interface Builder {
        NetworkConstraintsResolver build(Context context, int networkType, Server server);
    }

    public static class BuilderImp implements Builder {

        @Override
        public NetworkConstraintsResolver build(Context context, int networkType, Server server) {
            switch (networkType) {
                case INTERNET:
                    return new InternetServerResolver(context);
                case BLUETOOTH:
                    return new BluetoothServerResolver(context);
                case WIFI_P2P:
                    return new WifiP2pServerResolver(context, server);
                case NSD:
                    return new NsdServerResolver();
                default:
                    throw new IllegalArgumentException(TAG + ": Unknown attack network type");
            }
        }
    }

    private static class InternetServerResolver extends NetworkConstraintsResolver {
        public InternetServerResolver(Context context) {
            addConstraint(createInternetConstraint(context));
        }

        @NonNull
        private InternetConstraint createInternetConstraint(Context context) {
            InternetConstraint constraint = new InternetConstraint(context);
            constraint.setOnResolveConstraintListener(this);
            return constraint;
        }
    }

    private static class BluetoothServerResolver extends NetworkConstraintsResolver {
        public BluetoothServerResolver(Context context) {
            addConstraint(createSetupConstraint(context));
            addConstraint(createBluetoothEnableConstraint(context));
            addConstraint(createBluetoothDiscoverabilityConstraint(context));
        }

        private BluetoothSetupConstraint createSetupConstraint(Context context) {
            BluetoothSetupConstraint constraint = new BluetoothSetupConstraint(context);
            constraint.setOnResolveConstraintListener(this);
            return constraint;
        }

        private BluetoothEnableConstraint createBluetoothEnableConstraint(Context context) {
            BluetoothEnableConstraint constraint = new BluetoothEnableConstraint(context);
            constraint.setOnResolveConstraintListener(this);
            return constraint;
        }

        @NonNull
        private BluetoothDiscoverabilityConstraint createBluetoothDiscoverabilityConstraint(Context context) {
            BluetoothDiscoverabilityConstraint constraint = new BluetoothDiscoverabilityConstraint(context);
            constraint.setOnResolveConstraintListener(this);
            return constraint;
        }
    }

    private static class WifiP2pServerResolver extends NetworkConstraintsResolver {
        public WifiP2pServerResolver(Context context, Server server) {
            addConstraint(createWifiP2pSetupConstraint(context));
            addConstraint(createWifiP2pGroupConstraint(context, (WifiP2pServer) server));
        }

        @NonNull
        private WifiP2pGroupConstraint createWifiP2pGroupConstraint(Context context, WifiP2pServer server) {
            WifiP2pGroupConstraint constraint = new WifiP2pGroupConstraint(context, server);
            constraint.setOnResolveConstraintListener(this);
            return constraint;
        }

        @NonNull
        private WifiP2pEnabledConstraint createWifiP2pSetupConstraint(Context context) {
            WifiP2pEnabledConstraint constraint = new WifiP2pEnabledConstraint(context);
            constraint.setOnResolveConstraintListener(this);
            return constraint;
        }
    }

    private static class NsdServerResolver extends NetworkConstraintsResolver {
        // TODO: set the NetworkConstraint's in the queue
    }
}
