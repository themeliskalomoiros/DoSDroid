package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.nsd;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.ATTACK_STARTED;

public class NsdServerThread implements Runnable {
    private static final String TAG = "NsdServerThread";

    private Socket socket;
    private OutputStream out;

    public NsdServerThread(@NonNull Socket socket) {
        this.socket = socket;
        initializeOutputStream(socket);
    }

    private void initializeOutputStream(@NonNull Socket socket) {
        try {
            out = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
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

    private void closeSocket(@NonNull Socket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
