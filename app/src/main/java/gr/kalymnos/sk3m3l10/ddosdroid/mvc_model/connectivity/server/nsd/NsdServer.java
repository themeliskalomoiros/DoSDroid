package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.server.nsd;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.server.Server;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.server.status.ServerStatusBroadcaster;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.bot.Bots;

import static android.content.Context.NSD_SERVICE;
import static gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras.EXTRA_ATTACK_HOST_UUID;
import static gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras.EXTRA_SERVICE_NAME;
import static gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras.EXTRA_SERVICE_TYPE;

public class NsdServer extends Server {
    private static final String INITIAL_SERVICE_NAME = "DdosDroid"; // It can be change due to colisions
    private static final String SERVICE_TYPE = String.format("_%s._%s.", INITIAL_SERVICE_NAME, "tcp");

    private ServerSocket serverSocket;
    private Thread acceptClientThread;
    private int localPort;

    private String nsdServiceName;
    private NsdManager.RegistrationListener registrationListener;

    public NsdServer(Context context, Attack attack) {
        super(context, attack);
        initFields();
    }

    private void initFields() {
        initServerSocketAndCachePort();
        initAcceptClientThread();
        initRegistrationListener(context);
    }

    private void initServerSocketAndCachePort() {
        try {
            serverSocket = new ServerSocket(0); // system chooses an available port
            localPort = serverSocket.getLocalPort();
        } catch (IOException e) {
            Log.e(TAG, "Error initializing server socket", e);
        }
    }

    private void initAcceptClientThread() {
        acceptClientThread = new Thread(() -> {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    executor.execute(new NsdServerThread(socket));
                } catch (SocketException e) {
                    Log.w(TAG, "Error on serverSocket.accept(). Maybe the serverSocket closed.", e);
                    break;
                } catch (IOException e) {
                    Log.w(TAG, "Error on serverSocket.accept(). Maybe the serverSocket closed", e);
                    break;
                }
            }
        });
    }

    private void initRegistrationListener(Context context) {
        registrationListener = new NsdManager.RegistrationListener() {

            @Override
            public void onServiceRegistered(NsdServiceInfo nsdServiceInfo) {
                nsdServiceName = nsdServiceInfo.getServiceName();
                uploadAttack();
                acceptClientThread.start();
                ServerStatusBroadcaster.broadcastRunning(getAttackedWebsite(), LocalBroadcastManager.getInstance(context));
            }

            private void uploadAttack() {
                addHostInfoToAttack();
                repository.upload(attack);
            }

            private void addHostInfoToAttack() {
                attack.addSingleHostInfo(EXTRA_SERVICE_NAME, nsdServiceName);
                attack.addSingleHostInfo(EXTRA_SERVICE_TYPE, SERVICE_TYPE);
                attack.addSingleHostInfo(EXTRA_ATTACK_HOST_UUID, Bots.local().getId());
            }

            @Override
            public void onRegistrationFailed(NsdServiceInfo nsdServiceInfo, int i) {
                Log.e(TAG, "Nsd registration failed");
                ServerStatusBroadcaster.broadcastError(getAttackedWebsite(), LocalBroadcastManager.getInstance(context));
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo nsdServiceInfo, int i) {
                Log.e(TAG, "Nsd unregistration failed");
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo nsdServiceInfo) {
                Log.d(TAG, "Nsd service unregistered successfully");
                ServerStatusBroadcaster.broadcastStopped(getAttackedWebsite(), LocalBroadcastManager.getInstance(context));
            }
        };
    }

    @Override
    public void start() {
        constraintsResolver.resolveConstraints();
    }

    @Override
    public void stop() {
        closeServerSocket();
        unregisterService();
        super.stop();
    }

    private void closeServerSocket() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Error closing server socket", e);
        }
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
        serviceInfo.setServiceName(INITIAL_SERVICE_NAME);
        serviceInfo.setServiceType(SERVICE_TYPE);
        serviceInfo.setPort(localPort);
        return serviceInfo;
    }

    @Override
    public void onConstraintResolveFailure() {
        ServerStatusBroadcaster.broadcastError(getAttackedWebsite(), LocalBroadcastManager.getInstance(context));
    }
}
