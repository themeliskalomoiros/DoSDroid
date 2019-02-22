package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.host_services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.util.Map;

import gr.kalymnos.sk3m3l10.ddosdroid.R;
import gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities.AllAttackListsActivity;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.job.AttackJobScheduler;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.client.Client;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.repository.AttackRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.repository.FirebaseRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.job.persistance.JobPersistance;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.job.persistance.PrefsJobPersistance;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attacks;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.bot.Bots;

import static gr.kalymnos.sk3m3l10.ddosdroid.constants.ContentTypes.FETCH_ONLY_USER_JOINED_ATTACKS;
import static gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.host_services.JoinAttackService.ForegroundNotification.NOTIFICATION_ID;

public class JoinAttackService extends Service implements Client.ClientConnectionListener,
        AttackRepository.OnRepositoryChangeListener {
    private static final String TAG = "JoinAttackService";

    private AttackJobScheduler jobScheduler;
    private AttackRepository attackRepo;
    private JobPersistance jobPersist;

    @Override
    public void onCreate() {
        super.onCreate();
        initRepos();
        jobScheduler = new AttackJobScheduler(this);
        attackRepo.startListenForChanges();
    }

    private void initRepos() {
        attackRepo = new FirebaseRepository();
        attackRepo.setOnRepositoryChangeListener(this);
        jobPersist = new PrefsJobPersistance(getSharedPreferences(JobPersistance.FILE_NAME, MODE_PRIVATE));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Attack attack = intent.getParcelableExtra(Extras.EXTRA_ATTACK);
        switch (intent.getAction()) {
            case Action.ACTION_JOIN_ATTACK:
                handleJoinAttackAction(attack);
                break;
            case Action.ACTION_LEAVE_ATTACK:
                handleLeaveAttackAction(attack);
                break;
            case Action.ACTION_STOP_SERVICE:
                removeLocalBotAndUpdateAttacks();
                stopSelf(); // onDestroy() will be called clearing resources
                break;
        }
        return START_NOT_STICKY;
    }

    private void handleJoinAttackAction(Attack attack) {
        if (jobPersist.has(attack.getPushId())) {
            Toast.makeText(this, getString(R.string.already_attacking_label)
                    + " " + attack.getWebsite(), Toast.LENGTH_SHORT).show();
        } else {
            connectClient(attack);
        }
    }

    private void connectClient(Attack attack) {
        Client client = new Client(this, attack, this);
        client.connect();
    }

    private void handleLeaveAttackAction(Attack attack) {
        cancelJobOf(attack);
        removeLocalBotFrom(attack);
        if (jobPersist.size() == 0)
            stopSelf();
    }

    private void cancelJobOf(Attack attack) {
        jobScheduler.cancel(attack.getPushId());
        jobPersist.delete(attack.getPushId());
    }

    private void removeLocalBotFrom(Attack attack) {
        attack.getBotIds().remove(Bots.localId());
        attackRepo.update(attack);
    }

    private void removeLocalBotAndUpdateAttacks() {
        Map<String, ?> jobKeys = jobPersist.map();
        for (Map.Entry<String, ?> entry : jobKeys.entrySet()) {
            attackRepo.removeLocalBotAndUpdate(entry.getKey());
        }
    }

    @Override
    public void onClientConnection(Client client) {
        scheduleJob(client.getAttack());
        updateAttackWithCurrentUser(client.getAttack());
        client.removeClientConnectionListener();
        startForeground(NOTIFICATION_ID, new ForegroundNotification().create());
    }

    private void scheduleJob(Attack attack) {
        jobScheduler.schedule(attack);
        jobPersist.save(attack.getPushId());
    }

    private void updateAttackWithCurrentUser(Attack attack) {
        Attacks.addBot(attack, Bots.local());
        attackRepo.update(attack);
    }

    @Override
    public void onClientConnectionError(Client client) {
        client.removeClientConnectionListener();
        showToastOnUIThread(R.string.client_connection_error_msg);
        if (jobPersist.size() == 0)
            stopSelf();
    }

    private void showToastOnUIThread(int msgRes) {
        Runnable displayToast = () -> Toast.makeText(this, msgRes, Toast.LENGTH_SHORT).show();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(displayToast);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        attackRepo.stopListenForChanges();
        attackRepo.removeOnRepositoryChangeListener();
        cancelAllJobs();
    }

    private void cancelAllJobs() {
        jobScheduler.cancelAll();
        jobPersist.clear();
    }

    @Override
    public void onAttackDelete(Attack attack) {
        Action.leave(attack, this);
    }

    @Override
    public void onAttackUpload(Attack attack) {
    }

    @Override
    public void onAttackUpdate(Attack attack) {
    }

    public static class Action {
        private static final String ACTION_JOIN_ATTACK = TAG + "join attack action";
        private static final String ACTION_LEAVE_ATTACK = TAG + "leave attack action";
        private static final String ACTION_STOP_SERVICE = TAG + "stop service action";

        public static void join(Attack attack, Context context) {
            context.startService(createIntentWithAttackExtra(context, attack, ACTION_JOIN_ATTACK));
        }

        public static void leave(Attack attack, Context context) {
            context.startService(createIntentWithAttackExtra(context, attack, ACTION_LEAVE_ATTACK));
        }

        private static Intent createIntentWithAttackExtra(Context context, Attack attack, String action) {
            Intent intent = new Intent(context, JoinAttackService.class);
            intent.setAction(action);
            intent.putExtra(Extras.EXTRA_ATTACK, attack);
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
            return new NotificationCompat.Builder(JoinAttackService.this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_fist)
                    .setContentTitle(getString(R.string.client_notification_title))
                    .setContentText(getString(R.string.client_notification_small_text))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.client_notification_big_text)))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(getContentPendingIntent())
                    .addAction(R.drawable.ic_stop, getString(R.string.shutdown_label), getStopServicePendingIntent());
        }

        PendingIntent getContentPendingIntent() {
            Intent intent = AllAttackListsActivity.Action.createIntent(JoinAttackService.this, FETCH_ONLY_USER_JOINED_ATTACKS, R.string.joined_attacks_label);
            return PendingIntent.getActivity(JoinAttackService.this, CONTENT_INTENT_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        PendingIntent getStopServicePendingIntent() {
            Intent intent = new Intent(JoinAttackService.this, JoinAttackService.class);
            intent.setAction(Action.ACTION_STOP_SERVICE);
            return PendingIntent.getService(JoinAttackService.this, STOP_INTENT_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
    }
}
