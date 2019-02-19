package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.client;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.server.Server;

class BluetoothConnectionThread extends Thread {
    private static final String TAG = "BluetoothConnectionThre";

    private BluetoothSocket bluetoothSocket;
    private OnBluetoothServerResponseListener serverResponseListener;

    interface OnBluetoothServerResponseListener {
        void onServerResponseReceived();

        void onServerResponseError();
    }

    static void startInstance(BluetoothDevice discoveredDevice, UUID uuid, OnBluetoothServerResponseListener listener) {
        BluetoothConnectionThread instance = new BluetoothConnectionThread(discoveredDevice, uuid);
        instance.setOnBluetoothConnectionListener(listener);
        instance.start();
    }

    private BluetoothConnectionThread(BluetoothDevice device, UUID serverUUID) {
        initBluetoothSocket(device, serverUUID);
    }

    private void initBluetoothSocket(BluetoothDevice device, UUID serverUUID) {
        try {
            this.bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(serverUUID);
        } catch (IOException e) {
            Log.e(TAG, "Error when initializing bluetoothSocket", e);
        }
    }

    private void setOnBluetoothConnectionListener(OnBluetoothServerResponseListener listener) {
        this.serverResponseListener = listener;
    }

    @Override
    public void run() {
        //  First cancel device discovery because it slows down the connection
        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
        if (bluetoothSocket == null) {
            serverResponseListener.onServerResponseError();
            return;
        }

        boolean connected = connectToServer();
        handleConnectionResult(connected);
        releaseResources();
    }

    private boolean connectToServer() {
        try {
            bluetoothSocket.connect();
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Error when bluetoothSocket.connect()", e);
            return false;
        }
    }

    private void handleConnectionResult(boolean connected) {
        if (connected) {
            BufferedReader bufferedReader = getBufferedReader();
            String response = readServerResponse(bufferedReader);
            handleResponse(response);
        } else {
            serverResponseListener.onServerResponseError();
        }
    }

    private BufferedReader getBufferedReader() {
        try {
            return new BufferedReader(new InputStreamReader(bluetoothSocket.getInputStream()));
        } catch (IOException e) {
            throw new UnsupportedOperationException(TAG + "Error getting InputStream from bluetoothSocket", e);
        }
    }

    private String readServerResponse(BufferedReader reader) {
        StringBuilder response = new StringBuilder();
        String data = null;
        try {
            while ((data = reader.readLine()) != null) {
                response.append(data);
            }
            // May never reach: Server after sending response closes its socket.
            // Then client socket is throwing IOException.
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


    private void handleResponse(String response) {
        if (Server.isValid(response)) {
            serverResponseListener.onServerResponseReceived();
        } else {
            serverResponseListener.onServerResponseError();
        }
    }

    private void releaseResources() {
        closeSocket();
        serverResponseListener = null;
    }

    private void closeSocket() {
        try {
            bluetoothSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Error closing bluetoothSocket", e);
        }
    }
}
