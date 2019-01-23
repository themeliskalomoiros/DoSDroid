package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.wifi_p2p;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.nsd.NsdServerThread;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

class AcceptClientThread extends Thread {
    private static final String TAG = "AcceptClientThread";

    private final ExecutorService executor;
    private final Attack attack;
    private boolean started;

    private ServerSocket serverSocket;
    private int localPort;

    AcceptClientThread(Attack attack, ExecutorService executor) {
        this.attack = attack;
        this.executor = executor;
        initializeServerSocket();
    }

    private void initializeServerSocket() {
        try {
            serverSocket = new ServerSocket(0);
            localPort = serverSocket.getLocalPort();
        } catch (IOException e) {
            Log.e(TAG, "Error creating server socket.");
        }
    }

    @Override
    public void run() {
        started = true;
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                executor.execute(new WifiP2pServerThread(socket));
            } catch (SocketException e) {
                Log.d(TAG, "Error on serverSocket.accept(). Maybe the serverSocket closed.", e);
                break;
            } catch (IOException e) {
                Log.d(TAG, "Error on serverSocket.accept(). Maybe the serverSocket closed", e);
                break;
            }
        }
    }

    public boolean hasStarted() {
        return started;
    }
}
