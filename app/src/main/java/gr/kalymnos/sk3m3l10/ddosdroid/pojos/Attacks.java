package gr.kalymnos.sk3m3l10.ddosdroid.pojos;

public class Attacks {

    public static void removeBot(Attack attack, Bot bot) {
        attack.getBotIds().remove(bot.getId());
    }

    public static void addBot(Attack attack, Bot bot) {
        if (!attack.getBotIds().contains(bot.getId()))
            attack.getBotIds().add(bot.getId());
    }

    public static boolean includes(Attack attack, Bot bot) {
        return attack.getBotIds().contains(bot.getId());
    }

    public static boolean ownedBy(Attack attack, Bot bot) {
        return attack.getOwner().getId().equals(bot.getId());
    }
}
