package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.server.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.server.Server;

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
