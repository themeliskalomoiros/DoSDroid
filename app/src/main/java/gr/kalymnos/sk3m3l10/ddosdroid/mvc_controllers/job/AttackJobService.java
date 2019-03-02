package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.job;

import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.connectivity.host_services.AttackLaunchService;

public class AttackJobService extends JobService {
    private static final String TAG = "AttackJobService";

    @Override
    public boolean onStartJob(@NonNull JobParameters job) {
        Log.d(TAG, "job started");
        AttackLaunchService.Action.launch(job.getExtras(), this);
        jobFinished(job,false);
        return false; // Answers to the question: "Is there still work going on?"
    }

    @Override
    public boolean onStopJob(@NonNull JobParameters job) {
        AttackLaunchService.Action.stop(job.getExtras(), this);
        Log.d(TAG, "job stopped");
        return false; // Answers to the question: "Should this job be retried?"
    }
}
