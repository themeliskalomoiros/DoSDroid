package gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.bot.Bot;

public final class Attacks {
    private static final String TAG = "Attacks";

    public static void removeBot(Attack attack, Bot bot) {
        attack.getBotIds().remove(bot.getUuid());
    }

    public static void addBot(Attack attack, Bot bot) {
        if (!attack.getBotIds().contains(bot.getUuid()))
            attack.getBotIds().add(bot.getUuid());
    }

    public static void addBotIds(Attack attack, List<String> botIds) {
        for (String id : botIds) {
            addBot(attack, new Bot(id));
        }
    }

    public static boolean includes(Attack attack, Bot bot) {
        Log.d(TAG, "Attacks.inclures returns " + attack.getBotIds().contains(bot.getUuid()));
        return attack.getBotIds().contains(bot.getUuid());
    }

    public static boolean ownedBy(Attack attack, Bot bot) {
        return attack.getHostInfo().getUuid().equals(bot.getUuid());
    }

    public static String createPushId() {
        return getAttackDatabaseReference().push().getKey();
    }

    @NonNull
    private static DatabaseReference getAttackDatabaseReference() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        return database.getReference().child("attacks");
    }
}
