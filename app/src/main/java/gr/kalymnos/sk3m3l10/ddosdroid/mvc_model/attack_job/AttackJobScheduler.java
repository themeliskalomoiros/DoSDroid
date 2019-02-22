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

    private FirebaseJobDispatcher dispatcher;

    public AttackJobScheduler(Context context) {
        this.dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
    }

    public void schedule(Attack attack) {
        long launchTime = attack.getLaunchTimestamp();
        long plusOneMinute = launchTime + ONE_MINUTE_IN_MILLI;
        Job attackJob = jobFrom(attack, (int) launchTime, (int) plusOneMinute);
        dispatcher.mustSchedule(attackJob);
    }

    @NonNull
    private Job jobFrom(Attack attack, int windowStart, int windowEnd) {
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

    public void cancel(String jobTag) {
        dispatcher.cancel(jobTag);
    }

    public void cancelAll(){
        dispatcher.cancelAll();
    }

}
