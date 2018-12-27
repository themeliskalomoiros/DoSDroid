package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.status;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.Server;

public class ServerStatusBroadcaster {

    public static void broadcastStatus(int status, String serverId, LocalBroadcastManager manager) {
        Intent intent = new Intent(Server.ACTION_SERVER_STATUS);
        intent.putExtra(Server.EXTRA_SERVER_STATUS, status);
        intent.putExtra(Server.EXTRA_ID, serverId);
        manager.sendBroadcast(intent);
    }
}
