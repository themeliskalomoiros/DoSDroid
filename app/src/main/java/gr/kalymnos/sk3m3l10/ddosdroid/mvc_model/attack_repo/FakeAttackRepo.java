package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack_repo;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSBot;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack.NetworkType.BLUETOOTH;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack.NetworkType.INTERNET;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack.NetworkType.NSD;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack.NetworkType.WIFI_P2P;

public class FakeAttackRepo extends AttackRepository {
    private static final long SLEEP_TIME_MILLIS = 1000;

    private List<DDoSAttack> allAttacks = AttackCreator.createAttacks(200);

    public FakeAttackRepo(Activity controller) {
        super(controller);
    }

    @Override
    public void fetchAllAttacks() {
        new Thread(() -> {
            sleep(SLEEP_TIME_MILLIS);
            if (callback != null) {
                controller.runOnUiThread(() -> callback.attacksFetchedSuccess(allAttacks));
            }
        }).start();
    }

    @Override
    public void fetchAllAttacksOf(int networkType) {
        new Thread(() -> {
            sleep(SLEEP_TIME_MILLIS);
            if (callback != null) {
                List<DDoSAttack> attacks = new ArrayList<>();

                for (DDoSAttack attack : allAttacks) {
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
            sleep(SLEEP_TIME_MILLIS);
            if (callback != null) {
                List<DDoSAttack> attacks = new ArrayList<>();

                for (DDoSAttack attack : allAttacks) {
                    boolean botJoinedAttack = !attack.isOwnedBy(botId) && attack.botBelongsToBotnet(botId);
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
            sleep(SLEEP_TIME_MILLIS);
            if (callback != null) {
                List<DDoSAttack> attacks = new ArrayList<>();

                for (DDoSAttack attack : allAttacks) {
                    if (attack.hasNetworkTypeOf(networkType)) {
                        boolean botJoinedAttack = !attack.isOwnedBy(botId) && attack.botBelongsToBotnet(botId);

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
            sleep(SLEEP_TIME_MILLIS);
            if (callback != null) {
                List<DDoSAttack> attacks = new ArrayList<>();
                for (DDoSAttack attack : allAttacks) {
                    if (!attack.isOwnedBy(botId)) {

                        if (!attack.botBelongsToBotnet(botId)) {
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
            sleep(SLEEP_TIME_MILLIS);
            if (callback != null) {
                List<DDoSAttack> attacks = new ArrayList<>();
                for (DDoSAttack attack : allAttacks) {
                    if (attack.hasNetworkTypeOf(networkType)) {

                        if (!attack.isOwnedBy(botId)) {

                            if (!attack.botBelongsToBotnet(botId)) {
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
            sleep(SLEEP_TIME_MILLIS);
            if (callback != null) {
                List<DDoSAttack> attacks = new ArrayList<>();
                for (DDoSAttack attack : allAttacks) {

                    if (attack.isOwnedBy(DDoSBot.getLocalUserDDoSBot().getId())) {
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
            sleep(SLEEP_TIME_MILLIS);
            if (callback != null) {
                List<DDoSAttack> attacks = new ArrayList<>();
                for (DDoSAttack attack : allAttacks) {
                    if (attack.hasNetworkTypeOf(networkType)) {

                        if (attack.isOwnedBy(DDoSBot.getLocalUserDDoSBot().getId())) {
                            attacks.add(attack);
                        }

                    }
                }
                controller.runOnUiThread(() -> callback.attacksFetchedSuccess(attacks));
            }
        }).start();
    }

    private static class AttackCreator {

        static List<DDoSAttack> createAttacks(int count) {
            DDoSAttack[] attacks = new DDoSAttack[count];
            populateArrayWithRandomAttacks(attacks);
            return Arrays.asList(attacks);
        }

        private static void populateArrayWithRandomAttacks(DDoSAttack[] attacks) {
            for (int i = 0; i < attacks.length; i++) {
                attacks[i] = createAttack(NetworkTypeCreator.createRandomNetworkType(), BotCreator.createRandomBot());
            }
        }

        static DDoSAttack createAttack(int networkType, DDoSBot owner) {
            return new DDoSAttack(WebsiteCreator.createWebsite(), networkType, owner,
                    BotCreator.createRandomBotnet(new Random().nextInt(8)),
                    createRandomBoolean(), System.currentTimeMillis());
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

        static List<DDoSBot> createRandomBotnet(int count) {
            DDoSBot[] bots = new DDoSBot[count];
            populateArrayWithRandomBots(bots);
            return Arrays.asList(bots);
        }

        private static void populateArrayWithRandomBots(DDoSBot[] bots) {
            for (int i = 0; i < bots.length; i++) {
                bots[i] = createRandomBot();
            }
        }

        private static DDoSBot createRandomBot() {
            return new DDoSBot(PREFIX + String.valueOf(new Random().nextInt(16)));
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

    static boolean createRandomBoolean() {
        return new Random().nextInt(2) > 0 ? true : false;
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Log.e(TAG, "Error while sleep()", e);
        }
    }
}
