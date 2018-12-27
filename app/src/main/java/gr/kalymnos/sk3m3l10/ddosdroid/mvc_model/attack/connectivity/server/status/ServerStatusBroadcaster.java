package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.status;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.Server;

public class ServerStatusBroadcaster {
    public static final String ACTION_SERVER_STATUS = "action server status broadcasted";
    public static final String EXTRA_SERVER_STATUS = "extra server status";

    public static void broadcastStatus(Server.Status status, LocalBroadcastManager manager) {
        Intent intent = new Intent(ACTION_SERVER_STATUS);
        intent.putExtra(EXTRA_SERVER_STATUS, status);
        manager.sendBroadcast(intent);
    }
}
