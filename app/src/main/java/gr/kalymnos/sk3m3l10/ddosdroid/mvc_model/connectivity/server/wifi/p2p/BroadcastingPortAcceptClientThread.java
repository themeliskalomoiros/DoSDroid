package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.server.wifi.p2p;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;

import gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.server.wifi.connection_threads.AcceptClientThread;

/*  This thread first broadcasts the local port and then
 *   starts accepting client connection request.*/

public class BroadcastingPortAcceptClientThread extends AcceptClientThread {
    public static final String ACTION_LOCAL_PORT_OBTAINED = "action local port obtained";

    private int localPort;
    private final LocalBroadcastManager manager;

    public BroadcastingPortAcceptClientThread(ExecutorService executor, ServerSocket serverSocket, LocalBroadcastManager manager) {
        super(executor, serverSocket);
        this.manager = manager;
        localPort = serverSocket.getLocalPort();
    }

    @Override
    public void run() {
        broadcast(localPort);
        super.run();
    }

    private void broadcast(int port) {
        Intent intent = new Intent(ACTION_LOCAL_PORT_OBTAINED);
        intent.putExtra(Extras.EXTRA_LOCAL_PORT, port);
        manager.sendBroadcast(intent);
    }
}
