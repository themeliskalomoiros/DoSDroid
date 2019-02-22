package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.client.nsd;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.support.annotation.NonNull;
import android.util.Log;

import java.net.InetAddress;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.client.ServerConnection;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.client.wifi_p2p.SocketConnectionThread;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attacks;

/* May don't find a service in a public wifi network.
*  Many authors find Android's NSD implementation buggy.*/

public class NsdServerConnection extends ServerConnection implements NsdManager.DiscoveryListener,
        SocketConnectionThread.OnServerResponseListener {
    private static final String TAG = "NsdServerConnection";

    private NsdManager manager;

    public NsdServerConnection(Context context, Attack attack) {
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
        if (sameServiceAsServer(nsdServiceInfo.getServiceName(), nsdServiceInfo.getServiceType())) {
            manager.resolveService(nsdServiceInfo, getResolveListener());
        } else {
            Log.d(TAG, getUnknownServiceText(nsdServiceInfo));
        }
    }

    private boolean sameServiceAsServer(String deviceServiceName, String deviceServiceType) {
        String serverServiceName = Attacks.getNsdServiceName(attack);
        String serverServiceType = Attacks.getNsdServiceType(attack);
        boolean namesMatch = deviceServiceName.equals(serverServiceName);
        boolean typesMatch = deviceServiceType.equals(serverServiceType);
        return namesMatch && typesMatch;
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
                thread.setServerResponseListener(NsdServerConnection.this);
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
