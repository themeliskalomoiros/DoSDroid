package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.server.status;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.server.Server;

import static gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.server.Server.ACTION_SERVER_STATUS;

public abstract class ServerStatusReceiver extends BroadcastReceiver {
    private static final String TAG = "ServerStatusReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case ACTION_SERVER_STATUS:
                handleServerStatusAction(intent);
                break;
            default:
                throw new IllegalArgumentException(TAG + ": intent does not have " + ACTION_SERVER_STATUS);
        }
    }

    protected abstract void handleServerStatusAction(Intent intent);

    protected final String getServerWebsiteFrom(Intent intent) {
        return intent.getStringExtra(Server.EXTRA_SERVER_WEBSITE);
    }

    protected final int getServerStatusFrom(Intent intent) {
        return intent.getIntExtra(Server.EXTRA_SERVER_STATUS, Server.Status.ERROR);
    }
}
