package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.connectivity.client.wifi;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.connectivity.server.Server;

public class SocketConnectionThread extends Thread {
    private static final String TAG = "SocketConnectionThread";
    private static final int TIMEOUT = 1000;
    private InetAddress host;
    private int port;

    private Socket socket;
    private OnServerResponseListener serverResponseListener;

    public interface OnServerResponseListener {
        void onValidServerResponse();

        void onErrorServerResponse();
    }

    public SocketConnectionThread(InetAddress host, int port) {
        Log.d(TAG, "Initialized");
        initFields(host, port);
    }

    private void initFields(InetAddress host, int port) {
        this.host = host;
        this.port = port;
        socket = new Socket();
    }

    public void setServerResponseListener(OnServerResponseListener serverResponseListener) {
        this.serverResponseListener = serverResponseListener;
    }

    @Override
    public void run() {
        boolean connected = connectToServer();
        if (connected) {
            Log.d(TAG, "Connection was successfull");
            reportServerResponse();
        } else {
            Log.d(TAG, "Connection failed");
            serverResponseListener.onErrorServerResponse();
        }
        closeSocket();
    }

    private boolean connectToServer() {
        try {
            socket.connect(new InetSocketAddress(host, port), TIMEOUT);
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Error while socket.connect()", e);
            return false;
        }
    }

    private void reportServerResponse() {
        if (Server.isValid(getResponse())) {
            serverResponseListener.onValidServerResponse();
        } else {
            serverResponseListener.onErrorServerResponse();
        }
    }

    @NonNull
    private String getResponse() {
        BufferedReader reader = getBufferedReader();
        return readServerResponse(reader);
    }

    private BufferedReader getBufferedReader() {
        try {
            return new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new UnsupportedOperationException(TAG + "Error getting InputStream from socket", e);
        }
    }

    private String readServerResponse(BufferedReader reader) {
        StringBuilder response = new StringBuilder();
        String data = null;
        try {
            while ((data = reader.readLine()) != null) {
                response.append(data);
            }
            /* Control may never reach this statement.
             * Server is sending its response to the client and quickly closes its socket.
             * This results the client socket to be closed as well (throwing IOException)
             * */
            return response.toString();
        } catch (IOException e) {
            if (Server.isValid(response.toString())) {
                return response.toString();
            } else {
                Log.d(TAG, "response = " + response);
                Log.w(TAG, "Error reading line from BufferedReader", e);
                return "";
            }
        }
    }

    private void closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            Log.e(TAG, "Error closing socket", e);
        }
    }
}
