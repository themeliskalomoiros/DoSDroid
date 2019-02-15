package gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack;

import android.support.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.bot.Bot;

import static gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras.EXTRA_ATTACK_HOST_UUID;
import static gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras.EXTRA_LOCAL_PORT;
import static gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras.EXTRA_MAC_ADDRESS;
import static gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras.EXTRA_SERVICE_NAME;
import static gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras.EXTRA_SERVICE_TYPE;

public final class Attacks {

    public static void addBot(Attack attack, Bot bot) {
        if (!attack.getBotIds().contains(bot.getId()))
            attack.getBotIds().add(bot.getId());
    }

    public static boolean includesBot(Attack attack, Bot bot) {
        return attack.getBotIds().contains(bot.getId());
    }

    public static boolean isAttackOwnedByBot(Attack attack, Bot bot) {
        String attackHostId = attack.getHostInfo().get(EXTRA_ATTACK_HOST_UUID);
        String localHostId = bot.getId();
        return attackHostId.equals(localHostId);
    }

    public static UUID getHostUUID(Attack attack) {
        return UUID.fromString(getHostUUIDText(attack));
    }

    public static String getHostUUIDText(Attack attack) {
        return attack.getHostInfo().get(EXTRA_ATTACK_HOST_UUID);
    }

    public static String getHostMacAddress(Attack attack) {
        return attack.getHostInfo().get(EXTRA_MAC_ADDRESS);
    }

    public static int getHostLocalPort(Attack attack) {
        return Integer.parseInt(attack.getHostInfo().get(EXTRA_LOCAL_PORT));
    }

    public static String getNsdServiceType(Attack attack) {
        return attack.getHostInfo().get(EXTRA_SERVICE_TYPE);
    }

    public static String getNsdServiceName(Attack attack) {
        return attack.getHostInfo().get(EXTRA_SERVICE_NAME);
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
