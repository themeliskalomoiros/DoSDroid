package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.persistance.job;

import android.content.SharedPreferences;

/*
 * This class just save the job tags as keys,
 * no need to worry about the value.
 * */
public class PrefsJobPersistance implements JobPersistance {
    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;

    public PrefsJobPersistance(SharedPreferences preferences) {
        this.preferences = preferences;
        this.editor = this.preferences.edit();
    }

    @Override
    public int size() {
        return preferences.getAll().size();
    }

    @Override
    public boolean has(String jobTag) {
        return preferences.contains(jobTag);
    }

    @Override
    public void save(String jobTag) {
        editor.putBoolean(jobTag, true);
        editor.apply();
    }

    @Override
    public void delete(String jobTag) {
        editor.remove(jobTag);
        editor.apply();
    }
}
