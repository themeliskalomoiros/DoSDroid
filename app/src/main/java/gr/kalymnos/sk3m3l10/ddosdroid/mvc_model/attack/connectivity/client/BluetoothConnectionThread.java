package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.client;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

class BluetoothConnectionThread extends Thread {
    private static final String TAG = "BluetoothConnectionThre";

    private BluetoothSocket bluetoothSocket;
    private OnBluetoothConnectionListener callback;

    interface OnBluetoothConnectionListener {
        void onBluetoothConnectionSuccess();

        void onBluetoothConnectionFailure();
    }

    static void startAnInstance(BluetoothDevice discoveredDevice, UUID uuid, OnBluetoothConnectionListener listener) {
        BluetoothConnectionThread instance = new BluetoothConnectionThread(discoveredDevice, uuid);
        instance.setOnBluetoothConnectionListener(listener);
        instance.start();
    }

    private BluetoothConnectionThread(BluetoothDevice device, UUID serverUUID) {
        initializeBluetoothSocket(device, serverUUID);
    }

    private void initializeBluetoothSocket(BluetoothDevice device, UUID serverUUID) {
        try {
            this.bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(serverUUID);
        } catch (IOException e) {
            Log.e(TAG, "Error when initializing bluetoothSocket", e);
        }
    }

    public void setOnBluetoothConnectionListener(OnBluetoothConnectionListener onBluetoothConnectionListener) {
        this.callback = onBluetoothConnectionListener;
    }

    @Override
    public void run() {
        //  First cancel device discovery because it slows down the connection
        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();

        boolean connectionSuccess = connectToServer();
        handleConnectionResult(connectionSuccess);

        closeBluetoothSocket();
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

    private void handleConnectionResult(boolean connectionSuccess) {
        if (connectionSuccess) {
            handleConnectionSuccess();
        } else {
            callback.onBluetoothConnectionFailure();
        }
    }

    private void handleConnectionSuccess() {
        BufferedReader reader = getBufferedReader();
        String serverResponse = readServerResponse(reader);
        boolean isValidResponse = serverResponse.equals(Attack.STARTED_PASS);
        if (isValidResponse) {
            callback.onBluetoothConnectionSuccess();
        } else {
            callback.onBluetoothConnectionFailure();
        }
    }

    private BufferedReader getBufferedReader() {
        try {
            return new BufferedReader(new InputStreamReader(bluetoothSocket.getInputStream()));
        } catch (IOException e) {
            Log.e(TAG, "Error getting InputStream from bluetoothSocket", e);
            return null;
        }
    }

    private String readServerResponse(BufferedReader reader) {
        try {
            StringBuilder response = new StringBuilder();
            String data;
            while ((data = reader.readLine()) != null) {
                response.append(data);
            }
            return response.toString();
        } catch (IOException e) {
            Log.e(TAG, "Error reading line from BufferedReader", e);
            return null;
        }
    }

    private void closeBluetoothSocket() {
        try {
            bluetoothSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Error closing bluetoothSocket", e);
        }
    }
}
