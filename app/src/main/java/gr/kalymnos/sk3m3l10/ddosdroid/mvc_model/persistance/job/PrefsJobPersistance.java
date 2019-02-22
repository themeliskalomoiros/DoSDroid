package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.persistance.job;

import android.content.SharedPreferences;

import gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras;

public class PrefsJobPersistance extends JobPersistance {
    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;

    public PrefsJobPersistance(SharedPreferences preferences) {
        this.preferences = preferences;
        this.editor = this.preferences.edit();
    }

    @Override
    public boolean exists(String jobTag) {
        String value = preferences.getString(Extras.EXTRA_JOB, "");
        return value.equals(jobTag);
    }

    @Override
    public void save(String jobTag) {
        editor.putString(Extras.EXTRA_JOB, jobTag);
        commit(jobTag, editor);
    }

    private void commit(String jobTag, SharedPreferences.Editor editor) {
        new Thread(() -> {
            boolean saved = editor.commit();
            if (saved) {
                listener.onJobSave(jobTag);
            } else {
                listener.onJobSaveError(jobTag);
            }
        }).start();
    }

    @Override
    public void delete(String jobTag) {
        editor.remove(jobTag);
        editor.apply();
    }
}
