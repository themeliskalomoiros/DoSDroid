package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.host_services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AttackService extends Service {
    private static final String TAG = "AttackService";

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static class Action {
        private static final String ACTION_START_ATTACK = TAG + "start attack action";
        private static final String ACTION_STOP_ATTACK = TAG + "stop attack action";
        private static final String ACTION_STOP_SERVICE = TAG + "stop service";
    }
}
