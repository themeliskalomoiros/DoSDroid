package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

class BluetoothServerThread implements Runnable {
    private static final String TAG = "BluetoothServerThread";

    private BluetoothSocket socket;
    private PrintWriter out;

    BluetoothServerThread(@NonNull BluetoothSocket socket) {
        initializeFields(socket);
    }

    private void initializeFields(@NonNull BluetoothSocket socket) {
        this.socket = socket;
        out = new PrintWriter(getOutputStreamFrom(socket), true);
    }

    private OutputStream getOutputStreamFrom(@NonNull BluetoothSocket socket) {
        OutputStream outputStream = null;
        try {
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error obtaining outStream from socket", e);
        }
        return outputStream;
    }

    @Override
    public void run() {
        out.println(Attack.STARTED_PASS);
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
