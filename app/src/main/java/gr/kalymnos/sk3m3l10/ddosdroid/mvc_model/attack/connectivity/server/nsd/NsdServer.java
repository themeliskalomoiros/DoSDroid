package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.nsd;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.Server;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.ServersHost;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.status.ServerStatusBroadcaster;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

import static android.content.Context.NSD_SERVICE;

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
            Log.e(TAG, "Error initializing server socket", e);
        }
    }

    private void initializeRegistrationManager(Context context) {
        registrationListener = new NsdManager.RegistrationListener() {
            @Override
            public void onRegistrationFailed(NsdServiceInfo nsdServiceInfo, int i) {
                Log.e(TAG, "Nsd registration failed");
                ServersHost.Action.stopServer(context, getId());
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
    public void stop() {
        unregisterService();
        super.stop();
    }

    private void unregisterService() {
        NsdManager manager = (NsdManager) context.getSystemService(NSD_SERVICE);
        manager.unregisterService(registrationListener);
    }

    @Override
    public void onConstraintsResolved() {
        registerService();
    }

    private void registerService() {
        NsdManager manager = (NsdManager) context.getSystemService(NSD_SERVICE);
        manager.registerService(getNsdServiceInfo(), NsdManager.PROTOCOL_DNS_SD, registrationListener);
    }

    private NsdServiceInfo getNsdServiceInfo() {
        NsdServiceInfo serviceInfo = new NsdServiceInfo();
        serviceInfo.setServiceName(SERVICE_NAME);
        serviceInfo.setServiceType(SERVICE_TYPE);
        serviceInfo.setPort(localPort);
        return serviceInfo;
    }

    @Override
    public void onConstraintResolveFailure() {
        ServerStatusBroadcaster.broadcastError(getId(), LocalBroadcastManager.getInstance(context));
    }
}
