package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.host_services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;

import static gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras.EXTRA_ATTACK;
import static gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras.EXTRA_WEBSITE;

public class AttackLaunchService extends Service {
    private static final String TAG = "AttackLaunchService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String website = intent.getStringExtra(EXTRA_WEBSITE);
        String attackId = intent.getStringExtra(EXTRA_ATTACK);
        switch (intent.getAction()) {
            case Action.ACTION_START_ATTACK:
                break;
        }
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static class Action {
        private static final String ACTION_START_ATTACK = TAG + "start attack action";
        private static final String ACTION_STOP_ATTACK = TAG + "stop attack action";
        private static final String ACTION_STOP_SERVICE = TAG + "stop service";

        public static void launch(Bundle attackBundle, Context context) {
            throwIfInvalid(attackBundle);
            Intent intent = getStartAttackIntent(attackBundle, context);
            context.startService(intent);
        }

        @NonNull
        private static Intent getStartAttackIntent(Bundle attackBundle, Context context) {
            Intent intent = new Intent(context, AttackLaunchService.class);
            intent.setAction(ACTION_START_ATTACK);
            intent.putExtras(attackBundle);
            return intent;
        }

        private static void throwIfInvalid(Bundle extras) {
            boolean invalidBundle = extras == null || extras.size() == 0;
            if (invalidBundle)
                throw new UnsupportedOperationException(TAG + ": Not a valid bundle");
        }
    }
}
