package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.status;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.Server;

import static gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.Server.Status.ERROR;
import static gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.Server.Status.RUNNING;
import static gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.Server.Status.STOPPED;

public class ServerStatusBroadcaster {

    public static void broadcastRunning(String serverWebsite, LocalBroadcastManager manager) {
        Intent intent = createIntent(RUNNING, serverWebsite);
        manager.sendBroadcast(intent);
    }

    public static void broadcastStopped(String serverWebsite, LocalBroadcastManager manager) {
        Intent intent = createIntent(STOPPED, serverWebsite);
        manager.sendBroadcast(intent);
    }

    public static void broadcastError(String serverWebsite, LocalBroadcastManager manager) {
        Intent intent = createIntent(ERROR, serverWebsite);
        manager.sendBroadcast(intent);
    }

    @NonNull
    private static Intent createIntent(int status, String serverWebsite) {
        Intent intent = new Intent(Server.ACTION_SERVER_STATUS);
        intent.putExtra(Server.EXTRA_SERVER_STATUS, status);
        intent.putExtra(Server.EXTRA_SERVER_WEBSITE, serverWebsite);
        return intent;
    }
}
