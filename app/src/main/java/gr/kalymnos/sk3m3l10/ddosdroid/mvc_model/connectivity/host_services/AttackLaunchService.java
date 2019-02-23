package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.host_services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.widget.Toast;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

import static gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras.EXTRA_ATTACK;

public class AttackLaunchService extends Service {
    private static final String TAG = "AttackLaunchService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Attack attack = intent.getExtras().getParcelable(EXTRA_ATTACK);
        switch (intent.getAction()) {
            case Action.ACTION_START_ATTACK:
                Toast.makeText(this, "The attacks website is " + attack.getWebsite(), Toast.LENGTH_LONG).show();
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
