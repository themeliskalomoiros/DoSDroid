package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.status;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.status.ServerStatusBroadcaster.ACTION_SERVER_STATUS;

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
}
