package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.service;

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

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants;

import static gr.kalymnos.sk3m3l10.ddosdroid.utils.ValidationUtils.mapHasItems;

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
        Attack attack = intent.getParcelableExtra(Constants.Extra.EXTRA_ATTACK);
        switch (intent.getAction()) {
            case ACTION_START_ATTACK:
                handleStartAction(attack);
                return START_REDELIVER_INTENT;
            case ACTION_STOP_ATTACK:
                handleStopAttack(attack);
                if (isLastAttack(attack)) {
                    stopSelf();
                    return START_NOT_STICKY;
                }
                return START_REDELIVER_INTENT;
            default:
                return super.onStartCommand(intent, flags, startId);
        }
    }

    private void handleStartAction(Attack attack) {
        boolean firstTimeExecutingAttack = !mapHasItems(tasks) || !tasks.containsKey(attack.getPushId());
        if (firstTimeExecutingAttack) {
            Future future = executor.submit(new AttackScript(attack.getWebsite()));
            tasks.put(attack.getPushId(), future);
        }
    }

    private void handleStopAttack(Attack attack) {
        if (mapHasItems(tasks) && tasks.containsKey(attack.getPushId())) {
            Future attackFuture = tasks.get(attack.getPushId());
            attackFuture.cancel(true);
            if (attackFuture.isDone()) {
                tasks.remove(attack.getPushId());
            }
        }
    }

    private boolean isLastAttack(Attack attack) {
        return tasks.containsKey(attack.getPushId()) && tasks.size() == 1;
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

    public static class Action {
        public static void startAttack(Attack attack, Context context) {
            context.startService(createIntent(context, attack, ACTION_START_ATTACK));
        }

        public static void stopAttack(Attack attack, Context context) {
            context.startService(createIntent(context, attack, ACTION_STOP_ATTACK));
        }

        private static Intent createIntent(Context context, Attack attack, String action) {
            Intent intent = new Intent(context, AttackService.class);
            intent.setAction(action);
            intent.putExtra(Constants.Extra.EXTRA_ATTACK, attack);
            return intent;
        }
    }
}
