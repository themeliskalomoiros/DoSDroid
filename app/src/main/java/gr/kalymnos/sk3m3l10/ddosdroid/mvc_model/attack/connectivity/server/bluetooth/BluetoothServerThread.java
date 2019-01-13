package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.ATTACK_STARTED;

class BluetoothServerThread implements Runnable {
    private static final String TAG = "BluetoothServerThread";

    private BluetoothSocket socket;
    private OutputStream out;

    BluetoothServerThread(@NonNull BluetoothSocket socket) {
        initializeFields(socket);
    }

    private void initializeFields(@NonNull BluetoothSocket socket) {
        this.socket = socket;
        initializeOutputStream(socket);
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
        releaseResources();
    }

    private void writeAttackStarted() {
        try {
            out.write(ATTACK_STARTED);
        } catch (IOException e) {
            Log.e(TAG, "Error writing to output stream", e);
        }
    }

    private void releaseResources() {
        closeOutputStream();
        closeSocket(socket); // has no effect if the stream is closed
    }

    private void closeOutputStream() {
        try {
            out.close();
        } catch (IOException e) {
            Log.e(TAG, "Error closing output stream");
        }
    }

    private void closeSocket(@NonNull BluetoothSocket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
