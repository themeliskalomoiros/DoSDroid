package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model;

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

public class FakeAttackRepo implements AttackRepository {

    private static final String TAG = FakeAttackRepo.class.getSimpleName();
    private static final long SLEEP_TIME_MILLIS = 1000;

    /*
     * Static fields of FakeAttackRepo will be initialized from a static block
     * */
    private static List<DDoSAttack> allAttacks;

    static {
        allAttacks = AttackCreator.createAttacks(20);
    }

    private OnAttacksFetchListener callback;
    private Activity controller;

    public FakeAttackRepo(Activity controller) {
        this.controller = controller;
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
                    if (attack.getNetworkType() == networkType) {
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
                    if (attack.botBelongsToBotnet(botId)) {
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
                    boolean sameBotAndNetworkType = attack.botBelongsToBotnet(botId)
                            && attack.getNetworkType() == networkType;
                    if (sameBotAndNetworkType) {
                        attacks.add(attack);
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
                    if (!attack.botBelongsToBotnet(botId)) {
                        attacks.add(attack);
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
                    boolean sameBotAndNetworkType = attack.botBelongsToBotnet(botId)
                            && attack.getNetworkType() == networkType;
                    if (!sameBotAndNetworkType) {
                        attacks.add(attack);
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
                    boolean ownerIsLocalUser = attack.getOwner().equals(DDoSBot.getLocalUserDDoSBot());
                    if (ownerIsLocalUser) {
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
                    boolean ownerIsLocalUser = attack.getOwner().equals(DDoSBot.getLocalUserDDoSBot());
                    boolean sameNetworkType = attack.getNetworkType() == networkType;
                    if (ownerIsLocalUser && sameNetworkType) {
                        attacks.add(attack);
                    }
                }
                controller.runOnUiThread(() -> callback.attacksFetchedSuccess(attacks));
            }
        }).start();
    }

    @Override
    public void registerOnAttacksFetchListener(OnAttacksFetchListener listener) {
        callback = listener;
    }

    @Override
    public void unRegisterOnAttacksFetchListener() {
        callback = null;
    }

    private static class AttackCreator {

        static List<DDoSAttack> createAttacks(int count) {
            DDoSAttack[] attacks = new DDoSAttack[count];
            for (int i = 0; i < attacks.length; i++) {
                attacks[i] = createAttack(NetworkTypeCreator.createRandomNetworkType(), BotCreator.createRandomBot());
            }
            return Arrays.asList(attacks);
        }

        static DDoSAttack createAttack(int networkType, DDoSBot owner) {
            return new DDoSAttack("sample.com", networkType, owner,
                    BotCreator.createBotnet(new Random().nextInt(30)),
                    createRandomBoolean(), System.currentTimeMillis());
        }

    }

    private static class BotCreator {

        static List<DDoSBot> createBotnet(int count) {
            DDoSBot[] bots = new DDoSBot[count];
            for (int i = 0; i < bots.length; i++) {
                bots[i] = createRandomBot();
            }
            return Arrays.asList(bots);
        }

        static DDoSBot createRandomBot() {
            Random random = new Random();
            boolean isOwner = createRandomBoolean();

            if (isOwner) {
                return DDoSBot.getLocalUserDDoSBot();
            } else {
                return new DDoSBot(String.valueOf(random.nextInt(30)));
            }
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

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Log.e(TAG, "Error while sleep()", e);
        }
    }

    static boolean createRandomBoolean() {
        return new Random().nextInt(2) > 0 ? true : false;
    }
}
