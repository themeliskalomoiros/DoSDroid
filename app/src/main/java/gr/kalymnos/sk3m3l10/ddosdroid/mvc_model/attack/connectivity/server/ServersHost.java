package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

import gr.kalymnos.sk3m3l10.ddosdroid.R;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities.AllAttackListsActivity;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.status.ServerStatusReceiver;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.status.repository.SharedPrefsStatusRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.status.repository.StatusRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.repository.AttackRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.repository.FirebaseRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

import static gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.ServersHost.ForegroundNotification.NOTIFICATION_ID;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.AttackType.TYPE_FETCH_OWNER;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.Extra.EXTRA_ATTACK;

public class ServersHost extends Service {
    private static final String TAG = "ServersHost";
    private static final String ACTION_START_SERVER = TAG + "start server action";
    private static final String ACTION_STOP_SERVER = TAG + "stop server action";
    private static final String ACTION_STOP_SERVICE = TAG + "stop service action";

    private Set<Server> servers;
    private AttackRepository attackRepo;
    private StatusRepository statusRepo;
    private ServerStatusReceiver statusReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        servers = new HashSet<>();
        attackRepo = new FirebaseRepository();
        statusRepo = new SharedPrefsStatusRepository(this);
        statusReceiver = new ServerStatusReceiver() {
            @Override
            protected void handleServerStatusAction(Intent intent) {
                switch (getServerStatusFrom(intent)) {
                    case Server.Status.RUNNING:
                        statusRepo.setToStarted(getServerIdFrom(intent));
                        startForeground(NOTIFICATION_ID, new ForegroundNotification().createNotification());
                        break;
                    case Server.Status.STOPPED:
                        break;
                    case Server.Status.ERROR:
                        break;
                }
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        switch (intent.getAction()) {
            case ACTION_START_SERVER:
                handleStartServerAction(intent);
                return START_STICKY;
            case ACTION_STOP_SERVER:
                handleStopServerAction(intent);
                return START_STICKY;
            case ACTION_STOP_SERVICE:
                stopSelf();
                return START_NOT_STICKY;
            default:
                return super.onStartCommand(intent, flags, startId);
        }
    }

    private void handleStartServerAction(Intent intent) {
        Server server = createServerFrom(intent);
        boolean serverAdded = servers.add(server);
        if (serverAdded) {
            server.start();
        }
    }

    private Server createServerFrom(Intent intent) {
        Attack attack = intent.getParcelableExtra(EXTRA_ATTACK);
        return new Server.BuilderImp().build(this, attack);
    }

    private void handleStopServerAction(Intent intent) {
        String serverId = intent.getStringExtra(Server.EXTRA_ID);
        executeStopProcedure(serverId);
        if (servers.size() == 0) {
            stopSelf();
        }
    }

    private void executeStopProcedure(String serverId) {
        try {
            Server server = extractServerFrom(servers, serverId);
            server.stop();
            statusRepo.setToStopped(server.getId());
            servers.remove(server);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private Server extractServerFrom(Set<Server> servers, String serverId) {
        for (Server server : servers) {
            if (serverId.equals(server.getId()))
                return server;
        }
        throw new IllegalArgumentException(TAG + ": No server with " + serverId + " id exists in " + servers);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopServers();
        setServersToStoppedStatus();
        // TODO: unregister receiver
    }

    private void setServersToStoppedStatus() {
        for (Server server : servers)
            statusRepo.setToStopped(server.getId());
    }

    private void stopServers() {
        for (Server server : servers)
            server.stop();
    }

    class ForegroundNotification {
        static final String CHANNEL_ID = TAG + "channel id";
        static final int NOTIFICATION_ID = 191919;
        static final int CONTENT_INTENT_REQUEST_CODE = 1932;
        static final int STOP_INTENT_REQUEST_CODE = 1933;

        Notification createNotification() {
            return createNotificationBuilder().build();
        }

        NotificationCompat.Builder createNotificationBuilder() {
            return new NotificationCompat.Builder(ServersHost.this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_swords)
                    .setContentTitle(getString(R.string.server_notification_title))
                    .setContentText(getString(R.string.server_notification_small_text))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.server_notification_big_text)))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(createContentPendingIntent())
                    .addAction(R.drawable.ic_stop, getString(R.string.shutdown_label), createStopServicePendingIntent());
        }

        PendingIntent createContentPendingIntent() {
            Intent intent = AllAttackListsActivity.Action.createIntent(ServersHost.this, TYPE_FETCH_OWNER, R.string.your_attacks_label);
            return PendingIntent.getActivity(ServersHost.this, CONTENT_INTENT_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        PendingIntent createStopServicePendingIntent() {
            Intent intent = new Intent(ServersHost.this, ServersHost.class);
            intent.setAction(ACTION_STOP_SERVICE);
            return PendingIntent.getService(ServersHost.this, STOP_INTENT_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
    }

    public static class Action {
        public static void startServer(Context context, Attack attack) {
            Intent intent = createStartServerIntent(context, attack);
            context.startService(intent);
        }

        @NonNull
        private static Intent createStartServerIntent(Context context, Attack attack) {
            Intent intent = new Intent(context, ServersHost.class);
            intent.putExtra(EXTRA_ATTACK, attack);
            intent.setAction(ACTION_START_SERVER);
            return intent;
        }

        public static void stopServer(Context context, String serverId) {
            Intent intent = createStopServerIntent(context, serverId);
            context.startService(intent);
        }

        @NonNull
        private static Intent createStopServerIntent(Context context, String serverId) {
            Intent intent = new Intent(context, ServersHost.class);
            intent.putExtra(Server.EXTRA_ID, serverId);
            intent.setAction(ACTION_STOP_SERVER);
            return intent;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
