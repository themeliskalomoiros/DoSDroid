package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.host_services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import gr.kalymnos.sk3m3l10.ddosdroid.R;
import gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities.AllAttackListsActivity;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.server.Server;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

import static gr.kalymnos.sk3m3l10.ddosdroid.constants.ContentTypes.FETCH_ONLY_USER_OWN_ATTACKS;
import static gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras.EXTRA_ATTACK;
import static gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.host_services.ServerHost.ForegroundNotification.NOTIFICATION_ID;

public class ServerHost extends Service implements Server.OnServerStatusChangeListener {
    private static final String TAG = "MyServerHost";

    private Map<String, Server> servers;

    @Override
    public void onCreate() {
        super.onCreate();
        servers = new HashMap<>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        switch (intent.getAction()) {
            case Action.ACTION_START_SERVER:
                handleStartServerAction(intent);
                return START_STICKY;
            case Action.ACTION_STOP_SERVER:
                handleStopServerAction(intent);
                return START_STICKY;
            case Action.ACTION_STOP_SERVICE:
                stopSelf();
                return START_NOT_STICKY;
            default:
                return super.onStartCommand(intent, flags, startId);
        }
    }

    private void handleStartServerAction(Intent intent) {
        Server server = createServerFrom(intent);
        if (servers.containsKey(server.getKey())) {
            Toast.makeText(this, getString(R.string.already_attacking_label) + " " + server.getKey(), Toast.LENGTH_SHORT).show();
        } else {
            server.setServerStatusListener(this);
            servers.put(server.getKey(), server);
            server.start();
        }
    }

    private Server createServerFrom(Intent intent) {
        Attack attack = intent.getParcelableExtra(EXTRA_ATTACK);
        return new Server.BuilderImp().build(this, attack);
    }

    private void handleStopServerAction(Intent intent) {
        String websiteKey = intent.getStringExtra(Extras.EXTRA_WEBSITE);
        Server server = servers.get(websiteKey);
        servers.remove(websiteKey);
        server.stop();
    }

    @Override
    public void onServerRunning(String key) {
        startForeground(NOTIFICATION_ID, new ForegroundNotification().create());
    }

    @Override
    public void onServerStopped(String key) {
        if (servers.size() == 0)
            stopSelf();
    }

    @Override
    public void onServerError(String key) {
        servers.remove(key);
        Toast.makeText(ServerHost.this, getString(R.string.server_error_msg), Toast.LENGTH_LONG).show();
        if (servers.size() == 0)
            stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopServers();
        servers.clear();
    }

    private void stopServers() {
        for (Map.Entry<String, Server> entry : servers.entrySet())
            entry.getValue().stop();
    }

    public static class Action {
        private static final String ACTION_START_SERVER = TAG + "start server action";
        private static final String ACTION_STOP_SERVER = TAG + "stop server action";
        private static final String ACTION_STOP_SERVICE = TAG + "stop service action";

        public static void startServerOf(Attack attack, Context context) {
            Intent intent = createStartServerIntent(context, attack);
            context.startService(intent);
        }

        @NonNull
        private static Intent createStartServerIntent(Context context, Attack attack) {
            Intent intent = new Intent(context, ServerHost.class);
            intent.putExtra(EXTRA_ATTACK, attack);
            intent.setAction(ACTION_START_SERVER);
            return intent;
        }

        public static void stopServerOf(String serverWebsite, Context context) {
            Intent intent = createStopServerIntent(context, serverWebsite);
            context.startService(intent);
        }

        @NonNull
        private static Intent createStopServerIntent(Context context, String serverWebsite) {
            Intent intent = new Intent(context, ServerHost.class);
            intent.putExtra(Extras.EXTRA_WEBSITE, serverWebsite);
            intent.setAction(ACTION_STOP_SERVER);
            return intent;
        }
    }

    class ForegroundNotification {
        static final String CHANNEL_ID = TAG + "channel id";
        static final int NOTIFICATION_ID = 191919;
        static final int CONTENT_INTENT_REQUEST_CODE = 1932;
        static final int STOP_INTENT_REQUEST_CODE = 1933;

        Notification create() {
            return createNotificationBuilder().build();
        }

        NotificationCompat.Builder createNotificationBuilder() {
            return new NotificationCompat.Builder(ServerHost.this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_www_icon)
                    .setContentTitle(getString(R.string.server_notification_title))
                    .setContentText(getString(R.string.server_notification_small_text))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.server_notification_big_text)))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(getContentPendingIntent())
                    .addAction(R.drawable.ic_stop, getString(R.string.shutdown_label), getStopServicePendingIntent());
        }

        PendingIntent getContentPendingIntent() {
            Intent intent = AllAttackListsActivity.Action.createIntent(ServerHost.this, FETCH_ONLY_USER_OWN_ATTACKS, R.string.your_attacks_label);
            return PendingIntent.getActivity(ServerHost.this, CONTENT_INTENT_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        PendingIntent getStopServicePendingIntent() {
            Intent intent = new Intent(ServerHost.this, ServerHost.class);
            intent.setAction(Action.ACTION_STOP_SERVICE);
            return PendingIntent.getService(ServerHost.this, STOP_INTENT_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
