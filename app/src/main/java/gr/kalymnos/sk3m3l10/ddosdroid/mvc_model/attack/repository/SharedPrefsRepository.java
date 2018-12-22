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
    public void setToActive(String attackId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(attackId, true);
    }

    @Override
    public void setToInActive(String attackId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(attackId, false);
    }
}
