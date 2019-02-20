package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.server;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class AcceptClientThread extends Thread {
    private static final String TAG = "AcceptClientThread";

    private final ExecutorService executor;
    private final ServerSocket serverSocket;

    public AcceptClientThread(ExecutorService executor, ServerSocket serverSocket) {
        this.executor = executor;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                executor.execute(new ServerResponseThread(socket));
            } catch (IOException e) {
                Log.w(TAG, "Error on serverSocket.accept(). Maybe the serverSocket closed", e);
                break;
            }
        }
        Log.d(TAG, "exited.");
    }
}
