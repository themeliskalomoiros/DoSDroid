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

import gr.kalymnos.sk3m3l10.ddosdroid.R;
import gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities.AllAttackListsActivity;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack_job.AttackJobScheduler;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.client.Client;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.persistance.attack.AttackRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.persistance.attack.FirebaseRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.persistance.job.JobPersistance;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.persistance.job.PrefsJobPersistance;
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
    private JobPersistance jobPersit;

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
        jobPersit = new PrefsJobPersistance(getSharedPreferences(JobPersistance.FILE_NAME, MODE_PRIVATE));
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
                handleStartAttackAction(attack);
                break;
            case Action.ACTION_LEAVE_ATTACK:
                handleStopAttackAction(attack);
                break;
            case Action.ACTION_STOP_SERVICE:
                stopSelf(); // onDestroy() will be called clearing resources
                break;
        }
        return START_NOT_STICKY;
    }

    private void handleStartAttackAction(Attack attack) {
        if (jobPersit.has(attack.getWebsite())) {
            Toast.makeText(this, getString(R.string.already_attacking_label)
                    + " " + attack.getWebsite(), Toast.LENGTH_SHORT).show();
        } else {
            Client client = new Client(this, attack, this);
            client.connect();
            startForeground(NOTIFICATION_ID,new ForegroundNotification().create());
        }
    }

    private void handleStopAttackAction(Attack attack) {
        cancelJobOf(attack);
        removeLocalBotFrom(attack);
        showToastOnUIThread(this, R.string.left_attack_label);
    }

    private void cancelJobOf(Attack attack) {
        jobScheduler.cancel(attack.getPushId());
        jobPersit.delete(attack.getPushId());
    }

    private void removeLocalBotFrom(Attack attack) {
        attack.getBotIds().remove(Bots.localId());
        attackRepo.update(attack);
    }

    @Override
    public void onClientConnection(Client client) {
        scheduleJob(client.getAttack());
        updateAttackWithCurrentUser(client.getAttack());
        showToastOnUIThread(this, R.string.client_connected_msg);
        client.removeClientConnectionListener();
    }

    private void scheduleJob(Attack attack) {
        jobScheduler.schedule(attack);
        jobPersit.save(attack.getPushId());
    }

    private void updateAttackWithCurrentUser(Attack attack) {
        Attacks.addBot(attack, Bots.local());
        attackRepo.update(attack);
    }

    @Override
    public void onClientConnectionError(Client client) {
        client.removeClientConnectionListener();
        showToastOnUIThread(this, R.string.client_connection_error_msg);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        attackRepo.stopListenForChanges();
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

    private static void showToastOnUIThread(Context context, int msgRes) {
        Runnable displayToast = () -> Toast.makeText(context, msgRes, Toast.LENGTH_SHORT).show();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(displayToast);
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
