package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.nsd.constraints;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;

import java.io.IOException;
import java.net.ServerSocket;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.network_constraints.NetworkConstraint;

public class NsdRegistrationConstraint extends NetworkConstraint {
    private static final String SERVICE_NAME = "DdosDroid";
    private static final String SERVICE_TYPE = String.format("_%s._%s.", SERVICE_NAME, "tcp");

    private ServerSocket serverSocket;
    private int localPort;
    private NsdManager.RegistrationListener registrationListener;

    public NsdRegistrationConstraint(Context context) {
        super(context);
        initializeServerSocket();
        registrationListener = new NsdManager.RegistrationListener() {
            @Override
            public void onRegistrationFailed(NsdServiceInfo nsdServiceInfo, int i) {

            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo nsdServiceInfo, int i) {

            }

            @Override
            public void onServiceRegistered(NsdServiceInfo nsdServiceInfo) {

            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo nsdServiceInfo) {

            }
        };
    }

    private void initializeServerSocket() {
        try {
            serverSocket = new ServerSocket(0); // system chooses an available port
            localPort = serverSocket.getLocalPort();
        } catch (IOException e) {
            callback.onConstraintResolveFailed(context, NsdRegistrationConstraint.this);
        }
    }

    private NsdServiceInfo createNsdServiceInfo() {
        NsdServiceInfo serviceInfo = new NsdServiceInfo();
        serviceInfo.setServiceName(SERVICE_NAME);
        serviceInfo.setServiceType(SERVICE_TYPE);
        serviceInfo.setPort(localPort);
        return serviceInfo;
    }

    @Override
    public void resolve() {

    }

    @Override
    public boolean isResolved() {
        return false;
    }

    @Override
    public void cleanResources() {

    }
}
