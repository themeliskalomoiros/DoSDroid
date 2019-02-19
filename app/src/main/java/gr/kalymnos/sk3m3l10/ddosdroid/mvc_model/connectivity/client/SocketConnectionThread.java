package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.client;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.server.Server;

public class SocketConnectionThread extends Thread {
    private static final String TAG = "SocketConnectionThread";
    private static final int TIMEOUT = 1000;
    private InetAddress host;
    private int port;

    private Socket socket;
    private OnServerResponseListener serverResponseListener;

    public interface OnServerResponseListener {
        void onServerResponseReceived();

        void onServerResponseError();
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
        boolean socketConnected = connectToServer();
        if (socketConnected) {
            Log.d(TAG, "Connection was successfull");
            handleConnectionSuccess();
        } else {
            Log.d(TAG, "Connection failed");
            serverResponseListener.onServerResponseError();
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

    private void handleConnectionSuccess() {
        BufferedReader reader = getBufferedReader();
        String serverResponse = readServerResponse(reader);
        if (Server.isValid(serverResponse)) {
            serverResponseListener.onServerResponseReceived();
            Log.d(TAG, "Connection success");
        } else {
            serverResponseListener.onServerResponseError();
            Log.d(TAG, "Connection failure");
        }
    }

    private BufferedReader getBufferedReader() {
        try {
            return new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new UnsupportedOperationException(TAG + "Error getting InputStream from socket", e);
        }
    }

    private String readServerResponse(BufferedReader reader) {
        Log.d(TAG, "readServerResponse() called");
        StringBuilder response = new StringBuilder();
        String data = null;
        try {
            while ((data = reader.readLine()) != null) {
                response.append(data);
            }
            /*May never reach this statement:
             * Server is sending its response to the client and quickly closing its socket.
             * This results the client socket closes as well (throwing an IOException)
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
