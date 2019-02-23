package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.host_services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import java.util.HashMap;
import java.util.Map;

import gr.kalymnos.sk3m3l10.ddosdroid.R;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities.AllAttackListsActivity;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.job.AttackScript;

import static gr.kalymnos.sk3m3l10.ddosdroid.constants.ContentTypes.FETCH_ONLY_USER_JOINED_ATTACKS;
import static gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras.EXTRA_ATTACK;
import static gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras.EXTRA_WEBSITE;
import static gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.host_services.AttackLaunchService.ForegroundNotification.NOTIFICATION_ID;

public class AttackLaunchService extends Service {
    private static final String TAG = "AttackLaunchService";

    private Map<String, AttackScript> scripts;

    @Override
    public void onCreate() {
        super.onCreate();
        scripts = new HashMap<>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String attackId = intent.getStringExtra(EXTRA_ATTACK);
        switch (intent.getAction()) {
            case Action.ACTION_START_ATTACK:
                handleStartAction(intent, attackId);
                break;
            case Action.ACTION_STOP_ATTACK:
                handleStopAction(attackId);
                break;
            case Action.ACTION_STOP_SERVICE:
                stopSelf();
                break;
        }
        return START_NOT_STICKY;
    }

    private void handleStartAction(Intent intent, String attackId) {
        String website = intent.getStringExtra(EXTRA_WEBSITE);
        cachedAndStartScript(website, attackId);
        startForeground(NOTIFICATION_ID, new ForegroundNotification().create());
    }

    private void cachedAndStartScript(String website, String attackId) {
        AttackScript script = new AttackScript(website);
        scripts.put(attackId, script);
        script.start();
    }

    private void handleStopAction(String attackId) {
        if (scripts.containsKey(attackId))
            stopScriptOf(attackId);

        if (scripts.size() == 0)
            stopSelf();
    }

    private void stopScriptOf(String attackId) {
        AttackScript script = scripts.get(attackId);
        script.stopAttacking();
        scripts.remove(script);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopAllScripts();
        scripts.clear();
    }

    private void stopAllScripts() {
        for (Map.Entry<String, AttackScript> entry : scripts.entrySet())
            entry.getValue().stopAttacking();
    }

    public static class Action {
        private static final String ACTION_START_ATTACK = TAG + "start attack action";
        private static final String ACTION_STOP_ATTACK = TAG + "stop attack action";
        private static final String ACTION_STOP_SERVICE = TAG + "stop service";

        public static void launch(Bundle extras, Context context) {
            throwIfInvalid(extras);
            context.startService(getIntentOf(ACTION_START_ATTACK, extras, context));
        }

        private static void throwIfInvalid(Bundle extras) {
            boolean invalidBundle = extras == null || extras.size() == 0;
            if (invalidBundle)
                throw new UnsupportedOperationException(TAG + ": Not a valid bundle");
        }

        public static void stop(Bundle extras, Context context) {
            context.startService(getIntentOf(ACTION_STOP_ATTACK, extras, context));
        }

        private static Intent getIntentOf(String action, Bundle extras, Context context) {
            Intent intent = new Intent(context, AttackLaunchService.class);
            intent.setAction(action);
            intent.putExtras(extras);
            return intent;
        }
    }

    class ForegroundNotification {
        static final String CHANNEL_ID = TAG + "channel id";
        static final int NOTIFICATION_ID = 291919;
        static final int CONTENT_INTENT_REQUEST_CODE = 2932;
        static final int STOP_INTENT_REQUEST_CODE = 2933;

        Notification create() {
            return createNotificationBuilder().build();
        }

        NotificationCompat.Builder createNotificationBuilder() {
            return new NotificationCompat.Builder(AttackLaunchService.this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_fist)
                    .setContentTitle(getString(R.string.client_notification_title))
                    .setContentText(getString(R.string.client_notification_small_text))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.client_notification_big_text)))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(getContentPendingIntent())
                    .addAction(R.drawable.ic_stop, getString(R.string.shutdown_label), getStopServicePendingIntent());
        }

        PendingIntent getContentPendingIntent() {
            Intent intent = AllAttackListsActivity.Action.createIntent(AttackLaunchService.this, FETCH_ONLY_USER_JOINED_ATTACKS, R.string.joined_attacks_label);
            return PendingIntent.getActivity(AttackLaunchService.this, CONTENT_INTENT_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        PendingIntent getStopServicePendingIntent() {
            Intent intent = new Intent(AttackLaunchService.this, AttackLaunchService.class);
            intent.setAction(Action.ACTION_STOP_SERVICE);
            return PendingIntent.getService(AttackLaunchService.this, STOP_INTENT_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
