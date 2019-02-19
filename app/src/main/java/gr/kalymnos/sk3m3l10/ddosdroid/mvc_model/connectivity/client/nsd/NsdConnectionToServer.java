package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.client.nsd;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.support.annotation.NonNull;
import android.util.Log;

import java.net.InetAddress;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.client.ConnectionToServer;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.client.wifi_p2p.SocketConnectionThread;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attacks;

public class NsdConnectionToServer extends ConnectionToServer implements NsdManager.DiscoveryListener,
        SocketConnectionThread.OnServerResponseListener {
    private static final String TAG = "NsdConnectionToServer";

    private NsdManager manager;

    public NsdConnectionToServer(Context context, Attack attack) {
        super(context, attack);
        manager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
    }

    @Override
    public void connectToServer() {
        manager.discoverServices(Attacks.getNsdServiceType(attack), NsdManager.PROTOCOL_DNS_SD, this);
    }

    @Override
    public void disconnectFromServer() {
        connectionListener.onServerDisconnection();
    }

    @Override
    protected void releaseResources() {
        manager.stopServiceDiscovery(this);
        super.releaseResources();
    }

    @Override
    public void onDiscoveryStarted(String serviceType) {
        Log.d(TAG, "Discovery started");
    }

    @Override
    public void onDiscoveryStopped(String s) {
        Log.d(TAG, "Discovery stopped");
    }

    @Override
    public void onServiceFound(NsdServiceInfo nsdServiceInfo) {
        Log.d(TAG, "Service found");
        boolean sameServiceTypeAsServer = nsdServiceInfo.getServiceType().equals(Attacks.getNsdServiceType(attack));
        boolean sameServiceNameAsServer = nsdServiceInfo.getServiceName().equals(Attacks.getNsdServiceName(attack));
        if (sameServiceTypeAsServer && sameServiceNameAsServer) {
            manager.resolveService(nsdServiceInfo, getResolveListener());
        } else {
            Log.d(TAG, getUnknownServiceText(nsdServiceInfo));
        }
    }

    @NonNull
    private NsdManager.ResolveListener getResolveListener() {
        return new NsdManager.ResolveListener() {
            @Override
            public void onServiceResolved(NsdServiceInfo nsdServiceInfo) {
                Thread connectionThread = createConnectionThread(nsdServiceInfo);
                connectionThread.start();
            }

            private Thread createConnectionThread(NsdServiceInfo nsdServiceInfo) {
                int port = nsdServiceInfo.getPort();
                InetAddress inetAddress = nsdServiceInfo.getHost();
                SocketConnectionThread thread = new SocketConnectionThread(inetAddress, port);
                thread.setServerResponseListener(NsdConnectionToServer.this);
                return thread;
            }

            @Override
            public void onResolveFailed(NsdServiceInfo nsdServiceInfo, int errorCode) {
                Log.d(TAG, "Service resolve failed with error code: " + errorCode);
                connectionListener.onServerConnectionError();
            }
        };
    }

    @NonNull
    private String getUnknownServiceText(NsdServiceInfo nsdServiceInfo) {
        return String.format("Unknown service type or name.\nService name: \"%s\"\nService type:\"%s\"",
                nsdServiceInfo.getServiceName(), nsdServiceInfo.getServiceType());
    }

    @Override
    public void onServiceLost(NsdServiceInfo nsdServiceInfo) {
        Log.d(TAG, "Service lost");
        disconnectFromServer();
    }

    @Override
    public void onStartDiscoveryFailed(String s, int errorCode) {
        Log.d(TAG, "Start discovery failed with error code: " + errorCode);
        connectionListener.onServerConnectionError();
    }

    @Override
    public void onStopDiscoveryFailed(String s, int errorCode) {
        Log.d(TAG, "Stop discovery failed with error code: " + errorCode);
    }

    @Override
    public void onServerResponseReceived() {
        connectionListener.onServerConnection();
    }

    @Override
    public void onServerResponseError() {
        connectionListener.onServerConnectionError();
    }
}
