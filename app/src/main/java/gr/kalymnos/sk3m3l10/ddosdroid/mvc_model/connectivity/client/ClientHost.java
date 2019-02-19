package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.client;

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

import java.util.HashMap;
import java.util.Map;

import gr.kalymnos.sk3m3l10.ddosdroid.R;
import gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities.AllAttackListsActivity;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.repository.AttackRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.repository.FirebaseRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attacks;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.bot.Bots;

import static gr.kalymnos.sk3m3l10.ddosdroid.constants.ContentTypes.FETCH_ONLY_USER_JOINED_ATTACKS;
import static gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.client.ClientHost.ForegroundNotification.NOTIFICATION_ID;

public class ClientHost extends Service implements Client.ClientConnectionListener {
    private static final String TAG = "ClientHost";

    private Map<String, Client> clients;
    private AttackRepository repo;

    @Override
    public void onCreate() {
        super.onCreate();
        clients = new HashMap<>();
        repo = new FirebaseRepository();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Attack attack = intent.getParcelableExtra(Extras.EXTRA_ATTACK);
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
        if (clients.containsKey(attack.getWebsite())) {
            Toast.makeText(this, getString(R.string.already_attacking_label) + " " + client.getAttackedWebsite(), Toast.LENGTH_SHORT).show();
        } else {
            client.connect();
        }
    }

    private void handleStopAttackAction(Attack attack) {
        Client client = clients.get(attack.getWebsite());
        clients.remove(client);
        client.disconnect();
    }

    @Override
    public void onClientConnected(Client thisClient, Attack attack) {
        //  TODO: This is not called from main thread, maybe it will arise problems!
        clients.put(attack.getWebsite(), thisClient);
        updateAttackWithCurrentUser(attack);
        //  TODO: Better way to do is waiting for the attack to update and then start foreground
        startForeground(NOTIFICATION_ID, new ForegroundNotification().createNotification());
    }

    private void updateAttackWithCurrentUser(Attack attack) {
        Attacks.addBot(attack, Bots.local());
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
        //  TODO: This is not called from main thread, maybe it will arise problems!
        updateAttackWithoutCurrentUser(attack);
        if (clients.size() == 0)
            stopSelf();
    }

    private void updateAttackWithoutCurrentUser(Attack attack) {
        attack.getBotIds().remove(Bots.localId());
        repo.update(attack);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disconnectClients();
        clients.clear();
    }

    private void disconnectClients() {
        for (Map.Entry<String, Client> entry : clients.entrySet()) {
            entry.getValue().disconnect();
        }
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
            return new NotificationCompat.Builder(ClientHost.this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_fist)
                    .setContentTitle(getString(R.string.client_notification_title))
                    .setContentText(getString(R.string.client_notification_small_text))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.client_notification_big_text)))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(createContentPendingIntent())
                    .addAction(R.drawable.ic_stop, getString(R.string.shutdown_label), createStopServicePendingIntent());
        }

        PendingIntent createContentPendingIntent() {
            Intent intent = AllAttackListsActivity.Action.createIntent(ClientHost.this, FETCH_ONLY_USER_JOINED_ATTACKS, R.string.joined_attacks_label);
            return PendingIntent.getActivity(ClientHost.this, CONTENT_INTENT_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        PendingIntent createStopServicePendingIntent() {
            Intent intent = new Intent(ClientHost.this, ClientHost.class);
            intent.setAction(Action.ACTION_STOP_SERVICE);
            return PendingIntent.getService(ClientHost.this, STOP_INTENT_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
    }

    public static class Action {
        private static final String ACTION_START_ATTACK = TAG + "start attack action";
        private static final String ACTION_STOP_ATTACK = TAG + "stop attack action";
        private static final String ACTION_STOP_SERVICE = TAG + "stop service action";

        public static void createClientOf(Attack attack, Context context) {
            context.startService(createIntentWithAttackExtra(context, attack, ACTION_START_ATTACK));
        }

        public static void stopClientOf(Attack attack, Context context) {
            context.startService(createIntentWithAttackExtra(context, attack, ACTION_STOP_ATTACK));
        }

        private static Intent createIntentWithAttackExtra(Context context, Attack attack, String action) {
            Intent intent = new Intent(context, ClientHost.class);
            intent.setAction(action);
            intent.putExtra(Extras.EXTRA_ATTACK, attack);
            return intent;
        }

        public static void stopService(Context context) {
            context.startService(createStopServiceIntent(context));
        }

        @NonNull
        private static Intent createStopServiceIntent(Context context) {
            Intent intent = new Intent(context, ClientHost.class);
            intent.setAction(ACTION_STOP_SERVICE);
            return intent;
        }

    }
}
