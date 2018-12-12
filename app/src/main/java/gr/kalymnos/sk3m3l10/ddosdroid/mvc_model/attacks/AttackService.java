package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attacks;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.AttackConstants;

public class AttackService extends Service {
    private static final String TAG = "AttackService";

    public static final int ATTACK_LIMIT = 10;
    private static final String ACTION_START_ATTACK = TAG + "start attack action";
    private static final String ACTION_STOP_ATTACK = TAG + "stop attack action";

    private ExecutorService executor;
    private Map<String, Future> tasks;

    @Override
    public void onCreate() {
        super.onCreate();
        executor = Executors.newFixedThreadPool(ATTACK_LIMIT);
        tasks = new HashMap<>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Attack attack = intent.getParcelableExtra(AttackConstants.Extra.EXTRA_ATTACK);
        switch (intent.getAction()) {
            case ACTION_START_ATTACK:
                handleStartAction(attack);
                return START_REDELIVER_INTENT;
            case ACTION_STOP_ATTACK:
                handleStopAttack(attack);
                if (isLastAttack((Attack) intent.getParcelableExtra(AttackConstants.Extra.EXTRA_ATTACK))) {
                    return START_NOT_STICKY;
                }
                return START_REDELIVER_INTENT;
            default:
                return super.onStartCommand(intent, flags, startId);
        }
    }

    private void handleStartAction(Attack attack) {

    }

    private void handleStopAttack(Attack attack) {
        // TODO: implementation needed
    }

    private boolean isLastAttack(Attack attack) {
        // TODO: implementation needed
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        shutdownThreadPool();
    }

    private void shutdownThreadPool() {
        // https://www.baeldung.com/java-executor-service-tutorial
        executor.shutdown();
        try {
            if (executor.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static class AttackServiceManager {
        public static void startAttack(Attack attack, Context context) {
            context.startService(createIntent(context, attack, ACTION_START_ATTACK));
        }

        public static void stopAttack(Attack attack, Context context) {
            context.startService(createIntent(context, attack, ACTION_STOP_ATTACK));
        }

        private static Intent createIntent(Context context, Attack attack, String action) {
            Intent intent = new Intent(context, AttackService.class);
            intent.setAction(action);
            intent.putExtra(AttackConstants.Extra.EXTRA_ATTACK, attack);
            return intent;
        }
    }
}
