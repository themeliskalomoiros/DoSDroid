package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.server.wifi_p2p;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;

import gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras;

class AcceptClientThread extends Thread {
    private static final String TAG = "AcceptClientThread";

    private int localPort;
    private ServerSocket serverSocket;
    private final ExecutorService executor;

    AcceptClientThread(ExecutorService executor) {
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
