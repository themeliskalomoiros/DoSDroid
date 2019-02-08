package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

import gr.kalymnos.sk3m3l10.ddosdroid.R;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities.AllAttackListsActivity;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.client.Client;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.repository.AttackRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.repository.FirebaseRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attacks;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.bot.Bots;

import static gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.service.AttackService.ForegroundNotification.NOTIFICATION_ID;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.ContentType.FETCH_ONLY_USER_JOINED_ATTACKS;

public class AttackService extends Service implements Client.ClientConnectionListener {
    private static final String TAG = "AttackService";

    private Set<Client> clients;
    private AttackRepository repo;

    @Override
    public void onCreate() {
        super.onCreate();
        clients = new HashSet<>();
        repo = new FirebaseRepository();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Attack attack = intent.getParcelableExtra(Constants.Extra.EXTRA_ATTACK);
        switch (intent.getAction()) {
            case Action.ACTION_START_ATTACK:
                handleStartAttackAction(attack);
                return START_REDELIVER_INTENT;
            case Action.ACTION_STOP_ATTACK:
                handleStopAttackAction(attack);
                return START_REDELIVER_INTENT;
            case Action.ACTION_STOP_SERVICE:
                stopSelf(); // onDestroy() will be called clearing resources
                return START_NOT_STICKY;
            default:
                return super.onStartCommand(intent, flags, startId);
        }
    }

    private void handleStartAttackAction(Attack attack) {
        Client client = new Client(this, attack);
        client.setClientConnectionListener(this);
        if (clients.contains(client)) {
            Toast.makeText(this, R.string.already_attacking_label, Toast.LENGTH_SHORT).show();
        } else {
            client.connect();
        }
    }

    private void handleStopAttackAction(Attack attack) {
        Client client = getClientFromCollection(attack);
        client.disconnect();
    }

    private Client getClientFromCollection(Attack attack) {
        for (Client client : clients) {
            boolean sameWebsite = client.getAttackedWebsite().equals(attack.getWebsite());
            if (sameWebsite)
                return client;
        }
        throw new UnsupportedOperationException(TAG + ": No Client for attack(" + attack.getPushId() + ") in the collection");
    }

    @Override
    public void onClientConnected(Client thisClient, Attack attack) {
        boolean clientAdded = clients.add(thisClient);
        if (clientAdded) {
            addLocalBotAndUpdate(attack);
            startForeground(NOTIFICATION_ID, new ForegroundNotification().createNotification());
        }
    }

    private void addLocalBotAndUpdate(Attack attack) {
        Attacks.addBot(attack, Bots.getLocalUser());
        repo.update(attack);
    }

    @Override
    public void onClientConnectionError() {
        //  Not called from main thread
        displayErrorToastOnUIThread();
    }

    private void displayErrorToastOnUIThread() {
        Runnable displayToast = () -> Toast.makeText(this, R.string.client_connection_error_msg, Toast.LENGTH_SHORT).show();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(displayToast);
    }

    @Override
    public void onClientDisconnected(Client thisClient, Attack attack) {
        thisClient.releaseResources();
        clients.remove(thisClient);
        removeBotFromAttackAndUpdate(attack);
        if (clients.size() == 0) {
            Action.stopService(this);
        }
    }

    private void removeBotFromAttackAndUpdate(Attack attack) {
        attack.getBotIds().remove(Bots.getLocalUserId());
        repo.update(attack);
    }

    class ForegroundNotification {
        static final String CHANNEL_ID = TAG + "channel id";
        static final int NOTIFICATION_ID = 291919;
        static final int CONTENT_INTENT_REQUEST_CODE = 2932;
        static final int STOP_INTENT_REQUEST_CODE = 2933;

        Notification createNotification() {
            return createNotificationBuilder().build();
        }

        NotificationCompat.Builder createNotificationBuilder() {
            return new NotificationCompat.Builder(AttackService.this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_fist)
                    .setContentTitle(getString(R.string.client_notification_title))
                    .setContentText(getString(R.string.client_notification_small_text))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.client_notification_big_text)))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(createContentPendingIntent())
                    .addAction(R.drawable.ic_stop, getString(R.string.shutdown_label), createStopServicePendingIntent());
        }

        PendingIntent createContentPendingIntent() {
            Intent intent = AllAttackListsActivity.Action.createIntent(AttackService.this, FETCH_ONLY_USER_JOINED_ATTACKS, R.string.joined_attacks_label);
            return PendingIntent.getActivity(AttackService.this, CONTENT_INTENT_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        PendingIntent createStopServicePendingIntent() {
            Intent intent = new Intent(AttackService.this, AttackService.class);
            intent.setAction(Action.ACTION_STOP_SERVICE);
            return PendingIntent.getService(AttackService.this, STOP_INTENT_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
    }

    public static class Action {
        private static final String ACTION_STOP_ATTACK = TAG + "stop attack action";
        private static final String ACTION_STOP_SERVICE = TAG + "stop service action";
        private static final String ACTION_START_ATTACK = TAG + "start attack action";

        public static void startAttack(Attack attack, Context context) {
            context.startService(createIntentWithAttackExtra(context, attack, ACTION_START_ATTACK));
        }

        public static void stopAttack(Attack attack, Context context) {
            context.startService(createIntentWithAttackExtra(context, attack, ACTION_STOP_ATTACK));
        }

        private static Intent createIntentWithAttackExtra(Context context, Attack attack, String action) {
            Intent intent = new Intent(context, AttackService.class);
            intent.setAction(action);
            intent.putExtra(Constants.Extra.EXTRA_ATTACK, attack);
            return intent;
        }

        public static void stopService(Context context) {
            context.startService(createStopServiceIntent(context));
        }

        @NonNull
        private static Intent createStopServiceIntent(Context context) {
            Intent intent = new Intent(context, AttackService.class);
            intent.setAction(ACTION_STOP_SERVICE);
            return intent;
        }

    }
}
