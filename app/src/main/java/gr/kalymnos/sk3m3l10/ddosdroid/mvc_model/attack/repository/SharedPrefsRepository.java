package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.repository;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsRepository implements AttackStatusRepository {
    private SharedPreferences sharedPreferences;

    public SharedPrefsRepository(Context context) {
        sharedPreferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
    }

    @Override
    public boolean isActive(String attackid) {
        return sharedPreferences.getBoolean(attackid, false);
    }

    @Override
    public void setStatusOf(String attackId, boolean isActive) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(attackId, isActive);
        editor.apply();
    }
}
