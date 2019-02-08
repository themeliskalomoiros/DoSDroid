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
    private OnBluetoothConnectionListener bluetoothConnectionListener;

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

    private void setOnBluetoothConnectionListener(OnBluetoothConnectionListener onBluetoothConnectionListener) {
        this.bluetoothConnectionListener = onBluetoothConnectionListener;
    }

    @Override
    public void run() {
        //  First cancel device discovery because it slows down the connection
        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();

        if (bluetoothSocket == null) {
            bluetoothConnectionListener.onBluetoothConnectionFailure();
            return;
        }

        boolean connectionSuccess = connectToServer();
        if (connectionSuccess) {
            Log.d(TAG, "Connection was successfull");
            String response = readServerResponse(getBufferedReader());
            handleResponse(response);
        } else {
            Log.d(TAG, "Connection failed");
            bluetoothConnectionListener.onBluetoothConnectionFailure();
        }

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

    private void handleResponse(String response) {
        boolean isValidResponse = response.equals(Attack.STARTED_PASS);
        if (isValidResponse) {
            Log.d(TAG, "Received valid server response");
            bluetoothConnectionListener.onBluetoothConnectionSuccess();
        } else {
            Log.d(TAG, "Received wrong server response");
            bluetoothConnectionListener.onBluetoothConnectionFailure();
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

    private void releaseResources() {
        closeSocket();
        bluetoothConnectionListener = null;
    }

    private void closeSocket() {
        try {
            bluetoothSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Error closing bluetoothSocket", e);
        }
    }
}
