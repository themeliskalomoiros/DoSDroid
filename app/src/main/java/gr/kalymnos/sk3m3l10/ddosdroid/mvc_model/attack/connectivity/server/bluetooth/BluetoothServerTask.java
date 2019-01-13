package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.ATTACK_STARTED;

class BluetoothServerTask implements Runnable {
    private static final String TAG = "BluetoothServerTask";

    private BluetoothSocket socket;
    private InputStream in;
    private OutputStream out;

    BluetoothServerTask(@NonNull BluetoothSocket socket) {
        initializeFields(socket);
    }

    private void initializeFields(@NonNull BluetoothSocket socket) {
        this.socket = socket;
        initializeInputStream(socket);
        initializeOutputStream(socket);
    }

    private void initializeInputStream(@NonNull BluetoothSocket socket) {
        try {
            in = socket.getInputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error obtaining inputStream from socket", e);
        }
    }

    private void initializeOutputStream(@NonNull BluetoothSocket socket) {
        try {
            out = socket.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error obtaining outStream from socket", e);
        }
    }

    @Override
    public void run() {
        writeAttackStarted();
    }

    private void writeAttackStarted() {
        try {
            out.write(ATTACK_STARTED);
        } catch (IOException e) {
            Log.e(TAG, "Error writing to output stream", e);
        }
    }
}
