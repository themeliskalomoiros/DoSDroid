package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.network_constraints;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.LinkedList;
import java.util.Queue;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.network_constraints.bluetooth.discoverable.BluetoothDiscoverableConstraint;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.network_constraints.bluetooth.enable.BluetoothEnableConstraint;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.network_constraints.bluetooth.setup.BluetoothSetupConstraint;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.network_constraints.internet.InternetConstraint;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.network_constraints.wifi_p2p.WifiP2pEnabledConstraint;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.network_constraints.wifi_p2p.WifiP2pGroupConstraint;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.server.Server;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.server.wifi.p2p.WifiP2pServer;

import static gr.kalymnos.sk3m3l10.ddosdroid.constants.NetworkTypes.BLUETOOTH;
import static gr.kalymnos.sk3m3l10.ddosdroid.constants.NetworkTypes.INTERNET;
import static gr.kalymnos.sk3m3l10.ddosdroid.constants.NetworkTypes.NSD;
import static gr.kalymnos.sk3m3l10.ddosdroid.constants.NetworkTypes.WIFI_P2P;

/*
 * Resolving Mechanism:
 *
 * Resolves the next constraint in queue, until all constraints are met. Then notifies Server.
 * One constraint fails to resolve and the process stops. Notifies Server about the failure.
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

    public final void setOnConstraintsResolveListener(OnConstraintsResolveListener listener) {
        callback = listener;
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

    @Override
    public void onConstraintResolved(Context context, NetworkConstraint constraint) {
        resolveNextConstraint();
    }

    @Override
    public void onConstraintResolveFailed(Context context, NetworkConstraint constraint) {
        callback.onConstraintResolveFailure();
    }

    protected final void addConstraint(NetworkConstraint constraint) {
        constraints.add(constraint);
    }

    public void releaseResources() {
        releaseConstraintsResources();
    }

    private void releaseConstraintsResources() {
        for (NetworkConstraint constraint : constraints) {
            constraint.releaseResources();
        }
    }

    public interface Builder {
        NetworkConstraintsResolver build(Context context, int networkType, Server server);

        NetworkConstraintsResolver build(Context context, int networkType);
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

        private static class InternetServerResolver extends NetworkConstraintsResolver {
            public InternetServerResolver(Context context) {
                addConstraint(getInternetConstraint(context));
            }

            @NonNull
            private InternetConstraint getInternetConstraint(Context context) {
                InternetConstraint constraint = new InternetConstraint(context);
                constraint.setOnResolveConstraintListener(this);
                return constraint;
            }
        }

        private static class BluetoothServerResolver extends NetworkConstraintsResolver {
            public BluetoothServerResolver(Context context) {
                addConstraint(getSetupConstraint(context));
                addConstraint(getBluetoothEnableConstraint(context));
                addConstraint(getBluetoothDiscoverabilityConstraint(context));
            }

            private BluetoothSetupConstraint getSetupConstraint(Context context) {
                BluetoothSetupConstraint constraint = new BluetoothSetupConstraint(context);
                constraint.setOnResolveConstraintListener(this);
                return constraint;
            }

            private BluetoothEnableConstraint getBluetoothEnableConstraint(Context context) {
                BluetoothEnableConstraint constraint = new BluetoothEnableConstraint(context);
                constraint.setOnResolveConstraintListener(this);
                return constraint;
            }

            @NonNull
            private BluetoothDiscoverableConstraint getBluetoothDiscoverabilityConstraint(Context context) {
                BluetoothDiscoverableConstraint constraint = new BluetoothDiscoverableConstraint(context);
                constraint.setOnResolveConstraintListener(this);
                return constraint;
            }
        }

        private static class WifiP2pServerResolver extends NetworkConstraintsResolver {
            public WifiP2pServerResolver(Context context, Server server) {
                addConstraint(getWifiP2pSetupConstraint(context));
                addConstraint(getWifiP2pGroupConstraint(context, (WifiP2pServer) server));
            }

            @NonNull
            private WifiP2pGroupConstraint getWifiP2pGroupConstraint(Context context, WifiP2pServer server) {
                WifiP2pGroupConstraint constraint = new WifiP2pGroupConstraint(context, server);
                constraint.setOnResolveConstraintListener(this);
                return constraint;
            }

            @NonNull
            private WifiP2pEnabledConstraint getWifiP2pSetupConstraint(Context context) {
                WifiP2pEnabledConstraint constraint = new WifiP2pEnabledConstraint(context);
                constraint.setOnResolveConstraintListener(this);
                return constraint;
            }
        }

        private static class NsdServerResolver extends NetworkConstraintsResolver {
            // TODO: set the NetworkConstraint's in the queue
        }

        @Override
        public NetworkConstraintsResolver build(Context context, int networkType) {
            switch (networkType) {
                case INTERNET:
                    return new InternetClientResolver(context);
                case BLUETOOTH:
                    return new BluetoothClientResolver(context);
                case WIFI_P2P:
                    return new WifiP2pClientResolver(context);
                case NSD:
                    return new NsdClientResolver();
                default:
                    throw new IllegalArgumentException(TAG + ": Unknown attack network type");
            }
        }

        private class InternetClientResolver extends NetworkConstraintsResolver {
            public InternetClientResolver(Context context) {
            }
        }

        private class BluetoothClientResolver extends NetworkConstraintsResolver {
            public BluetoothClientResolver(Context context) {
                addConstraint(getSetupConstraint(context));
                addConstraint(getBluetoothEnableConstraint(context));
            }

            private BluetoothSetupConstraint getSetupConstraint(Context context) {
                BluetoothSetupConstraint constraint = new BluetoothSetupConstraint(context);
                constraint.setOnResolveConstraintListener(this);
                return constraint;
            }

            private BluetoothEnableConstraint getBluetoothEnableConstraint(Context context) {
                BluetoothEnableConstraint constraint = new BluetoothEnableConstraint(context);
                constraint.setOnResolveConstraintListener(this);
                return constraint;
            }
        }

        private class WifiP2pClientResolver extends NetworkConstraintsResolver {
            public WifiP2pClientResolver(Context context) {
                addConstraint(getWifiP2pSetupConstraint(context));
            }

            @NonNull
            private NetworkConstraint getWifiP2pSetupConstraint(Context context) {
                NetworkConstraint constraint = new WifiP2pEnabledConstraint(context);
                constraint.setOnResolveConstraintListener(this);
                return constraint;
            }
        }

        private class NsdClientResolver extends NetworkConstraintsResolver {
        }
    }
}
