package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.status;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.Server;

import static gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.Server.Status.ERROR;
import static gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.Server.Status.RUNNING;
import static gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.Server.Status.STOPPED;

public class ServerStatusBroadcaster {

    public static void broadcastRunning(String serverId, LocalBroadcastManager manager) {
        Intent intent = createIntent(RUNNING, serverId);
        manager.sendBroadcast(intent);
    }

    public static void broadcastStopped(String serverId, LocalBroadcastManager manager) {
        Intent intent = createIntent(STOPPED, serverId);
        manager.sendBroadcast(intent);
    }

    public static void broadcastError(String serverId, LocalBroadcastManager manager) {
        Intent intent = createIntent(ERROR, serverId);
        manager.sendBroadcast(intent);
    }

    @NonNull
    private static Intent createIntent(int status, String serverId) {
        Intent intent = new Intent(Server.ACTION_SERVER_STATUS);
        intent.putExtra(Server.EXTRA_SERVER_STATUS, status);
        intent.putExtra(Server.EXTRA_ID, serverId);
        return intent;
    }
}
