package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.job;

import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.connectivity.host_services.AttackLaunchService;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

public final class AttackJobScheduler {
    private static final int ONE_MINUTE_IN_SECONDS = 60;

    int windowStart, windowEnd; // In seconds
    private FirebaseJobDispatcher dispatcher;

    public AttackJobScheduler(Context context) {
        this.dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
    }

    synchronized public void schedule(Attack attack) {
        calculateTriggerWindows(attack);
        Job attackJob = jobFrom(attack, windowStart, windowEnd);
        dispatcher.mustSchedule(attackJob);
    }

    private void calculateTriggerWindows(Attack attack) {
        long currentTimeMillis = System.currentTimeMillis();
        long launchTimeMillis = attack.getLaunchTimestamp();
        long jobStartsAfterMillis = launchTimeMillis - currentTimeMillis;
        boolean attackNotStartedYet = jobStartsAfterMillis >= 0;
        if (attackNotStartedYet) {
            windowStart = (int) TimeUnit.MILLISECONDS.toSeconds(jobStartsAfterMillis);
            windowEnd = windowStart + ONE_MINUTE_IN_SECONDS;
        }
    }

    @NonNull
    private Job jobFrom(Attack attack, int windowStart, int windowEnd) {
        return dispatcher.newJobBuilder()
                .setService(AttackJobService.class)
                .setTag(attack.getPushId())
                .setRecurring(false)
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                .setTrigger(Trigger.executionWindow(windowStart, windowEnd))
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setReplaceCurrent(false)
                .setExtras(AttackLaunchService.Action.getBundleForAction(attack.getPushId(), attack.getWebsite()))
                .build();
    }

    public void cancel(String jobTag) {
        dispatcher.cancel(jobTag);
    }

}
