package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

import gr.kalymnos.sk3m3l10.ddosdroid.R;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities.AllAttackListsActivity;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.status.ServerStatusReceiver;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.status.repository.SharedPrefsStatusRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.status.repository.StatusRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

import static gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.ServersHost.ForegroundNotification.NOTIFICATION_ID;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.ContentType.FETCH_ONLY_USER_OWN_ATTACKS;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.Extra.EXTRA_ATTACK;

public class ServersHost extends Service {
    private static final String TAG = "ServersHost";

    private Set<Server> servers;
    private StatusRepository statusRepo;
    private ServerStatusReceiver statusReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeFields();
        registerServerStatusReceiver();
    }

    private void initializeFields() {
        servers = new HashSet<>();
        statusRepo = new SharedPrefsStatusRepository(this);
        initializeStatusReceiver();
    }

    private void initializeStatusReceiver() {
        statusReceiver = new ServerStatusReceiver() {
            @Override
            protected void handleServerStatusAction(Intent intent) {
                switch (getServerStatusFrom(intent)) {
                    case Server.Status.RUNNING:
                        statusRepo.setToStarted(getServerWebsiteFrom(intent));
                        startForeground(NOTIFICATION_ID, new ForegroundNotification().createNotification());
                        break;
                    case Server.Status.STOPPED:
                        executeStopProcedure(getServerWebsiteFrom(intent));
                        break;
                    case Server.Status.ERROR:
                        Toast.makeText(ServersHost.this, getString(R.string.server_error_msg), Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
    }

    private void registerServerStatusReceiver() {
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Server.ACTION_SERVER_STATUS);
        manager.registerReceiver(statusReceiver, filter);
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
        boolean serverAdded = servers.add(server);
        if (serverAdded) {
            server.start();
        } else {
            Toast.makeText(this, R.string.already_attacking_label, Toast.LENGTH_SHORT).show();
        }
    }

    private Server createServerFrom(Intent intent) {
        Attack attack = intent.getParcelableExtra(EXTRA_ATTACK);
        return new Server.BuilderImp().build(this, attack);
    }

    private void handleStopServerAction(Intent intent) {
        String serverWebsite = intent.getStringExtra(Server.EXTRA_SERVER_WEBSITE);
        executeStopProcedure(serverWebsite);
    }

    private void executeStopProcedure(String serverWebsite) {
        try {
            Server server = extractServerFrom(servers, serverWebsite);
            server.stop();
            statusRepo.setToStopped(server.getAttackedWebsite());
            servers.remove(server);
            if (servers.size() == 0) {
                stopSelf();
            }
        } catch (IllegalArgumentException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private Server extractServerFrom(Set<Server> servers, String serverWebsite) {
        for (Server server : servers) {
            if (serverWebsite.equals(server.getAttackedWebsite()))
                return server;
        }
        throw new IllegalArgumentException(TAG + ": No server with " + serverWebsite + " exists in " + servers);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopServers();
        setServersToStoppedStatus();
        unregisterStatusReceiver();
    }

    private void stopServers() {
        for (Server server : servers)
            server.stop();
    }

    private void setServersToStoppedStatus() {
        for (Server server : servers)
            statusRepo.setToStopped(server.getAttackedWebsite());
    }

    private void unregisterStatusReceiver() {
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.unregisterReceiver(statusReceiver);
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
                    .setSmallIcon(R.drawable.ic_www_icon)
                    .setContentTitle(getString(R.string.server_notification_title))
                    .setContentText(getString(R.string.server_notification_small_text))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.server_notification_big_text)))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(createContentPendingIntent())
                    .addAction(R.drawable.ic_stop, getString(R.string.shutdown_label), createStopServicePendingIntent());
        }

        PendingIntent createContentPendingIntent() {
            Intent intent = AllAttackListsActivity.Action.createIntent(ServersHost.this, FETCH_ONLY_USER_OWN_ATTACKS, R.string.your_attacks_label);
            return PendingIntent.getActivity(ServersHost.this, CONTENT_INTENT_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        PendingIntent createStopServicePendingIntent() {
            Intent intent = new Intent(ServersHost.this, ServersHost.class);
            intent.setAction(Action.ACTION_STOP_SERVICE);
            return PendingIntent.getService(ServersHost.this, STOP_INTENT_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
    }

    public static class Action {
        private static final String ACTION_START_SERVER = TAG + "start server action";
        private static final String ACTION_STOP_SERVER = TAG + "stop server action";
        private static final String ACTION_STOP_SERVICE = TAG + "stop service action";

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

        public static void stopServer(Context context, String serverWebsite) {
            Intent intent = createStopServerIntent(context, serverWebsite);
            context.startService(intent);
        }

        @NonNull
        private static Intent createStopServerIntent(Context context, String serverWebsite) {
            Intent intent = new Intent(context, ServersHost.class);
            intent.putExtra(Server.EXTRA_SERVER_WEBSITE, serverWebsite);
            intent.setAction(ACTION_STOP_SERVER);
            return intent;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
