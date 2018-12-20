package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.HashMap;
import java.util.Map;

public class ServersContainer extends Service {
    private static final String TAG = "ServersContainer";
    public static final String ACTION_START_SERVER = TAG + "start server action";

    private Map<String, Server> serversMap;

    public ServersContainer() {
        serversMap = new HashMap<>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        switch (intent.getAction()) {
            case ACTION_START_SERVER:
                handleStartServerAction(intent);
                return START_STICKY;
            default:
                return super.onStartCommand(intent, flags, startId);
        }
    }

    private void handleStartServerAction(Intent intent) {
        // TODO: implementation needed
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
