package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.job;

import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class AttackJobService extends JobService {
    private static final String TAG = "AttackJobService";

    @Override
    public boolean onStartJob(@NonNull JobParameters job) {
        Log.d(TAG,"job started");
        return false; // Answers to the question: "Is there still work going on?"
    }

    @Override
    public boolean onStopJob(@NonNull JobParameters job) {
        Log.d(TAG,"job stopped");
        return false; // Answers to the question: "Should this job be retried?"
    }
}
