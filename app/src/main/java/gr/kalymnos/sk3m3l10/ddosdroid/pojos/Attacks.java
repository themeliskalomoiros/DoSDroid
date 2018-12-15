package gr.kalymnos.sk3m3l10.ddosdroid.pojos;

import android.util.Log;

import java.util.List;

public class Attacks {
    private static final String TAG = "Attacks";

    public static void removeBot(Attack attack, Bot bot) {
        attack.getBotIds().remove(bot.getId());
    }

    public static void addBot(Attack attack, Bot bot) {
        if (!attack.getBotIds().contains(bot.getId()))
            attack.getBotIds().add(bot.getId());
    }

    public static void addBotIds(Attack attack, List<String> botIds) {
        for (String id : botIds) {
            addBot(attack, new Bot(id));
        }
    }

    public static boolean includes(Attack attack, Bot bot) {
        Log.d(TAG, "Attacks.inclures returns " + attack.getBotIds().contains(bot.getId()));
        return attack.getBotIds().contains(bot.getId());
    }

    public static boolean ownedBy(Attack attack, Bot bot) {
        return attack.getOwner().getId().equals(bot.getId());
    }
}
