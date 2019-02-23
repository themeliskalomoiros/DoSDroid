package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.host_services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import gr.kalymnos.sk3m3l10.ddosdroid.R;
import gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.client.Client;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.job.AttackJobScheduler;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.repository.AttackRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.repository.FirebaseRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attacks;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.bot.Bots;

public class JoinAttackService extends Service implements Client.ClientConnectionListener {
    private static final String TAG = "JoinAttackService";

    private AttackRepository attackRepo;
    private AttackJobScheduler jobScheduler;

    @Override
    public void onCreate() {
        super.onCreate();
        attackRepo = new FirebaseRepository();
        jobScheduler = new AttackJobScheduler(this);
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
                connectClient(attack);
                break;
            case Action.ACTION_LEAVE_ATTACK:
                jobScheduler.cancel(attack.getPushId());
                removeLocalBotFrom(attack);
                break;
        }
        return START_NOT_STICKY;
    }

    private void connectClient(Attack attack) {
        Client client = new Client(this, attack, this);
        client.connect();
    }

    private void removeLocalBotFrom(Attack attack) {
        attack.getBotIds().remove(Bots.localId());
        attackRepo.update(attack);
    }

    @Override
    public void onClientConnection(Client client) {
        client.removeClientConnectionListener();
        jobScheduler.schedule(client.getAttack());
        updateAttackWithCurrentUser(client.getAttack());
        stopSelf();
    }

    private void updateAttackWithCurrentUser(Attack attack) {
        Attacks.addBot(attack, Bots.local());
        attackRepo.update(attack);
    }

    @Override
    public void onClientConnectionError(Client client) {
        client.removeClientConnectionListener();
        showToastOnUIThread(R.string.client_connection_error_msg);
        stopSelf();
    }

    private void showToastOnUIThread(int msgRes) {
        Runnable displayToast = () -> Toast.makeText(this, msgRes, Toast.LENGTH_SHORT).show();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(displayToast);
    }

    public static class Action {
        private static final String ACTION_JOIN_ATTACK = TAG + "join attack action";
        private static final String ACTION_LEAVE_ATTACK = TAG + "leave attack action";

        public static void join(Attack attack, Context context) {
            context.startService(createIntentWithAttackExtra(context, attack, ACTION_JOIN_ATTACK));
        }

        //  TODO: Is this useful? Maybe AttackLaunchService should have this functionality
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
}
