package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attacks.attack_repo;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.Bot;
import gr.kalymnos.sk3m3l10.ddosdroid.utils.ThreadUtils;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.AttackConstants.NetworkType.BLUETOOTH;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.AttackConstants.NetworkType.INTERNET;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.AttackConstants.NetworkType.NSD;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.AttackConstants.NetworkType.WIFI_P2P;

public class FakeAttackRepo extends AttackRepository {

    private List<Attack> allAttacks = AttackCreator.createAttacks(200);

    public FakeAttackRepo(Activity controller) {
        super(controller);
    }

    @Override
    public void fetchAllAttacks() {
        new Thread(() -> {
            ThreadUtils.sleepOneSecond();
            if (callback != null) {
                controller.runOnUiThread(() -> callback.attacksFetchedSuccess(allAttacks));
            }
        }).start();
    }

    @Override
    public void fetchAllAttacksOf(int networkType) {
        new Thread(() -> {
            ThreadUtils.sleepOneSecond();
            if (callback != null) {
                List<Attack> attacks = new ArrayList<>();

                for (Attack attack : allAttacks) {
                    if (attack.hasNetworkTypeOf(networkType)) {
                        attacks.add(attack);
                    }
                }

                controller.runOnUiThread(() -> callback.attacksFetchedSuccess(attacks));
            }
        }).start();
    }

    @Override
    public void fetchJoinedAttakcsOf(String botId) {
        new Thread(() -> {
            ThreadUtils.sleepOneSecond();
            if (callback != null) {
                List<Attack> attacks = new ArrayList<>();

                for (Attack attack : allAttacks) {
                    boolean botJoinedAttack = !attack.isOwnedBy(botId) && attack.includes(botId);
                    if (botJoinedAttack) {
                        attacks.add(attack);
                    }
                }

                controller.runOnUiThread(() -> callback.attacksFetchedSuccess(attacks));
            }
        }).start();
    }

    @Override
    public void fetchJoinedAttakcsOf(String botId, int networkType) {
        new Thread(() -> {
            ThreadUtils.sleepOneSecond();
            if (callback != null) {
                List<Attack> attacks = new ArrayList<>();

                for (Attack attack : allAttacks) {
                    if (attack.hasNetworkTypeOf(networkType)) {
                        boolean botJoinedAttack = !attack.isOwnedBy(botId) && attack.includes(botId);

                        if (botJoinedAttack) {
                            attacks.add(attack);
                        }

                    }
                }

                controller.runOnUiThread(() -> callback.attacksFetchedSuccess(attacks));
            }
        }).start();
    }

    @Override
    public void fetchNotJoinedAttacksOf(String botId) {
        new Thread(() -> {
            ThreadUtils.sleepOneSecond();
            if (callback != null) {
                List<Attack> attacks = new ArrayList<>();
                for (Attack attack : allAttacks) {
                    if (!attack.isOwnedBy(botId)) {

                        if (!attack.includes(botId)) {
                            attacks.add(attack);
                        }

                    }
                }

                controller.runOnUiThread(() -> callback.attacksFetchedSuccess(attacks));
            }
        }).start();
    }

    @Override
    public void fetchNotJoinedAttacksOf(String botId, int networkType) {
        new Thread(() -> {
            ThreadUtils.sleepOneSecond();
            if (callback != null) {
                List<Attack> attacks = new ArrayList<>();
                for (Attack attack : allAttacks) {
                    if (attack.hasNetworkTypeOf(networkType)) {

                        if (!attack.isOwnedBy(botId)) {

                            if (!attack.includes(botId)) {
                                attacks.add(attack);
                            }

                        }

                    }
                }
                controller.runOnUiThread(() -> callback.attacksFetchedSuccess(attacks));
            }
        }).start();
    }

    @Override
    public void fetchLocalOwnerAttacks() {
        new Thread(() -> {
            ThreadUtils.sleepOneSecond();
            if (callback != null) {
                List<Attack> attacks = new ArrayList<>();
                for (Attack attack : allAttacks) {

                    if (attack.isOwnedBy(Bot.getLocalUserDDoSBot().getId())) {
                        attacks.add(attack);
                    }

                }
                controller.runOnUiThread(() -> callback.attacksFetchedSuccess(attacks));
            }
        }).start();
    }

    @Override
    public void fetchLocalOwnerAttacksOf(int networkType) {
        new Thread(() -> {
            ThreadUtils.sleepOneSecond();
            if (callback != null) {
                List<Attack> attacks = new ArrayList<>();
                for (Attack attack : allAttacks) {
                    if (attack.hasNetworkTypeOf(networkType)) {

                        if (attack.isOwnedBy(Bot.getLocalUserDDoSBot().getId())) {
                            attacks.add(attack);
                        }

                    }
                }
                controller.runOnUiThread(() -> callback.attacksFetchedSuccess(attacks));
            }
        }).start();
    }

    @Override
    public void uploadAttack(Attack attack) {

    }

    private static class AttackCreator {

        static List<Attack> createAttacks(int count) {
            Attack[] attacks = new Attack[count];
            populateArrayWithRandomAttacks(attacks);
            return Arrays.asList(attacks);
        }

        private static void populateArrayWithRandomAttacks(Attack[] attacks) {
            for (int i = 0; i < attacks.length; i++) {
                attacks[i] = createAttack(NetworkTypeCreator.createRandomNetworkType(), BotCreator.createRandomBot());
            }
        }

        static Attack createAttack(int networkType, Bot owner) {
            return new Attack(WebsiteCreator.createWebsite(), networkType, owner,
                    BotCreator.createRandomBotnet(new Random().nextInt(8)),
                    System.currentTimeMillis());
        }
    }

    private static class WebsiteCreator {
        private static final String PREFIX = "sport";
        private static final String SUFFIX = ".com";

        static String createWebsite() {
            return PREFIX + String.valueOf(new Random().nextInt(100)) + SUFFIX;
        }
    }

    private static class BotCreator {
        private static final String PREFIX = "bot ";

        static List<Bot> createRandomBotnet(int count) {
            Bot[] bots = new Bot[count];
            populateArrayWithRandomBots(bots);
            return Arrays.asList(bots);
        }

        private static void populateArrayWithRandomBots(Bot[] bots) {
            for (int i = 0; i < bots.length; i++) {
                bots[i] = createRandomBot();
            }
        }

        private static Bot createRandomBot() {
            return new Bot(PREFIX + String.valueOf(new Random().nextInt(16)));
        }
    }

    private static class NetworkTypeCreator {
        private static final String TAG = "NetworkTypeCreator";

        static int createRandomNetworkType() {
            int[] allNetworkTypes = {INTERNET, BLUETOOTH, NSD, WIFI_P2P};
            int randomChoice = new Random().nextInt(allNetworkTypes.length);
            Log.d(TAG, "reporting networktype " + allNetworkTypes[randomChoice]);
            return allNetworkTypes[randomChoice];
        }
    }
}
