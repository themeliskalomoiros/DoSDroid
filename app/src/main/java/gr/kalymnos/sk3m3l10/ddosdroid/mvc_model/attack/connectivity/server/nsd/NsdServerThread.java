package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.nsd;

import android.support.annotation.NonNull;

import java.net.Socket;

public class NsdServerThread implements Runnable {
    private Socket socket;

    public NsdServerThread(@NonNull Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

    }
}
