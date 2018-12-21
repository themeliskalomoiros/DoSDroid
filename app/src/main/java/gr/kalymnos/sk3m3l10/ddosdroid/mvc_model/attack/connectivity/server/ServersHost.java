package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import java.util.HashSet;
import java.util.Set;

import gr.kalymnos.sk3m3l10.ddosdroid.R;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities.AllAttackListsActivity;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.AttackType.TYPE_FETCH_OWNER;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.Extra.EXTRA_ATTACK;

public class ServersHost extends Service {
    private static final String TAG = "ServersHost";
    private static final String ACTION_START_SERVER = TAG + "start server action";
    private static final String ACTION_STOP_SERVER = TAG + "stop server action";

    private Set<Server> servers;

    @Override
    public void onCreate() {
        super.onCreate();
        servers = new HashSet<>();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (Server server : servers){
            server.stop();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        switch (intent.getAction()) {
            case ACTION_START_SERVER:
                handleStartServerAction(intent);
                startForeground(ForegroundNotification.NOTIFICATION_ID,
                        new ForegroundNotification().createNotification());
                return START_STICKY;
            case ACTION_STOP_SERVER:
                handleStopServerAction(intent);
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
        return new Server.BuilderImp().build(attack);
    }

    private void handleStopServerAction(Intent intent) {
        String serverId = intent.getStringExtra(Server.EXTRA_ID);
        stopAndRemoveServerFromCollection(serverId);
        if (servers.size() == 0) {
            stopSelf();
        }
    }

    private void stopAndRemoveServerFromCollection(String serverId) {
        for (Server server : servers) {
            if (server.getId().equals(serverId)) {
                server.stop();
                servers.remove(server);
            }
        }
    }

    private class ForegroundNotification {
        static final String CHANNEL_ID = TAG + "channel id";
        static final int NOTIFICATION_ID = 191919;
        static final int PENDING_INTENT_REQUEST_CODE = 1932;

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
                    .setContentIntent(createPendingIntent());
        }

        PendingIntent createPendingIntent(){
            Intent intent = AllAttackListsActivity.Action.createIntent(ServersHost.this,TYPE_FETCH_OWNER,R.string.your_attacks_label);
            return PendingIntent.getActivity(ServersHost.this,PENDING_INTENT_REQUEST_CODE,intent,PendingIntent.FLAG_UPDATE_CURRENT);
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
