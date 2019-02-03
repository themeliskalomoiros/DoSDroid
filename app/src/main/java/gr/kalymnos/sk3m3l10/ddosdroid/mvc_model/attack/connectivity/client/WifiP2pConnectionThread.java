package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.client;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

public class WifiP2pConnectionThread extends Thread {
    private static final String TAG = "WifiP2pConnectionThread";
    private static final int TIMEOUT = 1000;
    private InetAddress groupOwnerAddress;
    private int groupOwnerPort;

    private Socket socket;
    private OnConnectionListener connectionListener;

    public interface OnConnectionListener {
        void onConnectionSuccess();

        void onConnectionFailure();
    }

    public WifiP2pConnectionThread(InetAddress groupOwnerAddress, int groupOwnerPort) {
        initializeFields(groupOwnerAddress, groupOwnerPort);
    }

    private void initializeFields(InetAddress groupOwnerAddress, int groupOwnerPort) {
        this.groupOwnerAddress = groupOwnerAddress;
        this.groupOwnerPort = groupOwnerPort;
        socket = new Socket();
    }

    public void setConnectionListener(OnConnectionListener connectionListener) {
        this.connectionListener = connectionListener;
    }

    @Override
    public void run() {
        boolean socketConnected = connectToServer();
        if (socketConnected) {
            Log.d(TAG, "Connection was successfull");
            handleConnectionSuccess();
        } else {
            Log.d(TAG, "Connection failed");
            connectionListener.onConnectionFailure();
        }
        closeSocket();
    }

    private boolean connectToServer() {
        try {
            socket.connect(new InetSocketAddress(groupOwnerAddress, groupOwnerPort), TIMEOUT);
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Error while socket.connect()", e);
            return false;
        }
    }

    private void handleConnectionSuccess() {
        BufferedReader reader = getBufferedReader();
        String serverResponse = readServerResponse(reader);
        boolean isValidResponse = serverResponse.equals(Attack.STARTED_PASS);
        if (isValidResponse) {
            connectionListener.onConnectionSuccess();
            Log.d(TAG, "Connection success");
        } else {
            connectionListener.onConnectionFailure();
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
            boolean responseIsValid = response.toString().equals(Attack.STARTED_PASS);
            if (responseIsValid) {
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
