package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.nsd;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.Server;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.status.ServerStatusBroadcaster;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

public class NsdServer extends Server {
    private static final String SERVICE_NAME = "DdosDroid";
    private static final String SERVICE_TYPE = String.format("_%s._%s.", SERVICE_NAME, "tcp");

    private ServerSocket serverSocket;
    private int localPort;

    private NsdManager.RegistrationListener registrationListener;
    private String serviceName;

    public NsdServer(Context context, Attack attack) {
        super(context, attack);
        initializeServerSocket();
        initializeRegistrationManager(context);
    }

    private void initializeServerSocket() {
        try {
            serverSocket = new ServerSocket(0); // system chooses an available port
            localPort = serverSocket.getLocalPort();
        } catch (IOException e) {
            Log.e(TAG, "Error initializing server socket");
        }
    }

    private void initializeRegistrationManager(Context context) {
        registrationListener = new NsdManager.RegistrationListener() {
            @Override
            public void onRegistrationFailed(NsdServiceInfo nsdServiceInfo, int i) {
                Log.e(TAG, "Nsd registration failed");
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo nsdServiceInfo, int i) {
                Log.e(TAG, "Nsd unregistration failed");
            }

            @Override
            public void onServiceRegistered(NsdServiceInfo nsdServiceInfo) {
                serviceName = nsdServiceInfo.getServiceName();
                ServerStatusBroadcaster.broadcastRunning(getId(), LocalBroadcastManager.getInstance(context));
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo nsdServiceInfo) {
                ServerStatusBroadcaster.broadcastStopped(getId(), LocalBroadcastManager.getInstance(context));
            }
        };
    }

    @Override
    public void start() {
        constraintsResolver.resolveConstraints();
    }

    @Override
    public void onConstraintsResolved() {
        ServerStatusBroadcaster.broadcastRunning(getId(), LocalBroadcastManager.getInstance(context));
    }

    @Override
    public void onConstraintResolveFailure() {
        ServerStatusBroadcaster.broadcastError(getId(), LocalBroadcastManager.getInstance(context));
    }
}
