package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.connectivity.server.wifi.connection_threads;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.connectivity.server.Server;

public class ServerResponseThread implements Runnable {
    private static final String TAG = "ServerResponseThread";

    private Socket socket;
    private PrintWriter out;

    public ServerResponseThread(@NonNull Socket socket) {
        this.socket = socket;
        out = new PrintWriter(getOutputStreamFrom(socket), true);
    }

    private OutputStream getOutputStreamFrom(@NonNull Socket socket) {
        try {
            return socket.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error obtaining outStream from socket", e);
            return null;
        }
    }

    @Override
    public void run() {
        out.println(Server.RESPONSE);
        closeSocket();
    }

    private void closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
