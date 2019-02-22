package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack_job;

import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class AttackJobService extends JobService {

    @Override
    public boolean onStartJob(@NonNull JobParameters job) {
        return false; // Answers to the question: "Is there still work going on?"
    }

    @Override
    public boolean onStopJob(@NonNull JobParameters job) {
        return false; // Answers to the question: "Should this job be retried?"
    }
}
