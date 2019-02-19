package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.server.wifi_p2p;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;

class AcceptClientThread extends Thread {
    private static final String TAG = "AcceptClientThread";
    public static final String ACTION_LOCAL_PORT_OBTAINED = "action local port obtained";

    private int localPort;
    private ServerSocket serverSocket;
    private final ExecutorService executor;
    private final LocalBroadcastManager manager;

    AcceptClientThread(ExecutorService executor, LocalBroadcastManager manager) {
        this.executor = executor;
        this.manager = manager;
        initializeServerSocket();
        localPort = serverSocket.getLocalPort();
    }

    private void initializeServerSocket() {
        try {
            serverSocket = new ServerSocket(0);
        } catch (IOException e) {
            Log.e(TAG, "Error creating server socket.");
        }
    }

    @Override
    public void run() {
        manager.sendBroadcast(new Intent(ACTION_LOCAL_PORT_OBTAINED));
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                executor.execute(new WifiP2pServerThread(socket));
            } catch (SocketException e) {
                Log.w(TAG, "Error on serverSocket.accept(). Maybe the serverSocket closed.", e);
                break;
            } catch (IOException e) {
                Log.w(TAG, "Error on serverSocket.accept(). Maybe the serverSocket closed", e);
                break;
            }
        }
    }

    public void close() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Error when closing server socket");
        }
    }
}
