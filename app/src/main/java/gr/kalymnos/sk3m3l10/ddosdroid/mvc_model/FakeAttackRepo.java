package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
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

    private Activity controller;
    private List<DDoSAttack> allAttacks;
    private OnAttacksFetchListener mCallback;

    private Thread fetchAllAttacksThread, fetchFollowingAttacksThread;

    public FakeAttackRepo(Activity controller) {
        this.allAttacks = getRandomFakeAttackList(16);
        this.controller = controller;
    }

    @Override
    public void fetchAllAttacks() {
        if (fetchAllAttacksThread == null) {
            fetchAllAttacksThread = new Thread(getFetchAllAttacksTask());
            fetchAllAttacksThread.start();
        }
    }

    @Override
    public void fetchAllAttacksOf(int networkType) {
        fetchAllAttacks();
    }

    @Override
    public void fetchFollowingAttakcsOf(String botId) {
        if (fetchFollowingAttacksThread == null) {
            fetchFollowingAttacksThread = new Thread(getFetchFollowingAttacksTask(botId));
            fetchFollowingAttacksThread.start();
        }
    }

    @Override
    public void fetchFollowingAttakcsOf(String botId, int networkType) {
        fetchFollowingAttakcsOf(botId);
    }

    @Override
    public void fetchNotFollowingAttacksOf(String botId) {
        fetchFollowingAttakcsOf(botId);
    }

    @Override
    public void fetchNotFollowingAttacksOf(String botId, int networkType) {
        fetchFollowingAttakcsOf(botId);
    }

    @Override
    public void fetchOwnerAttacks() {
        fetchAllAttacks();
    }

    @Override
    public void fetchOwnerAttacksOf(int networkType) {
        fetchOwnerAttacks();
    }

    @Override
    public void registerOnAttacksFetchListener(OnAttacksFetchListener listener) {
        mCallback = listener;
    }

    @Override
    public void unRegisterOnAttacksFetchListener() {
        // First remove the listener's strong reference so the GC can collect the latter
        mCallback = null;
        // Cancel workers execution (by interrupting them)
        cancelWorkerThreads();
    }

    private void cancelWorkerThreads() {
        if (fetchAllAttacksThread != null) {
            fetchAllAttacksThread.interrupt();
            fetchAllAttacksThread = null;
        }
        if (fetchFollowingAttacksThread != null) {
            fetchFollowingAttacksThread.interrupt();
            fetchFollowingAttacksThread = null;
        }
    }

    private List<DDoSAttack> getRandomFakeAttackList(int size) {
        Random random = new Random();
        String websitePrefix = "website";
        String websiteSuffix = ".com";

        int[] networkTypes = {INTERNET, WIFI_P2P, NSD, BLUETOOTH};

        List<DDoSAttack> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(new DDoSAttack(websitePrefix + i + websiteSuffix,
                    networkTypes[random.nextInt(networkTypes.length)],
                    new DDoSBot("bot" + (random.nextInt(3) + 1)),
                    getRandomFakeBotnet(random.nextInt(7) + 3),
                    i % 2 == 0 ? true : false, System.currentTimeMillis()));
        }

        return list;
    }

    private List<DDoSBot> getRandomFakeBotnet(int size) {
        Random random = new Random();
        String prefix = "bot";

        List<DDoSBot> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(new DDoSBot(prefix + (random.nextInt(10) + 1)));
        }

        return list;
    }

    private Runnable getFetchAllAttacksTask() {
        return () -> {
            try {
                Log.d(TAG,"FetchAllAttacksThread says Hello!!!");
                Thread.sleep(SLEEP_TIME_MILLIS);
                if (mCallback != null) {
                    controller.runOnUiThread(() -> mCallback.attacksFetchedSuccess(allAttacks));
                }
            } catch (InterruptedException e) {
                //  If we are interrupted we just end the thread by returning
                Log.d(TAG, "fetchAllAttacksThread was interrupted");
                return;
            }
        };
    }

    private Runnable getFetchFollowingAttacksTask(String botId) {
        return () -> {
            try {
                Log.d(TAG,"FetchFollowingAttacksThread says Hello!!!");
                Thread.sleep(SLEEP_TIME_MILLIS);
                if (mCallback != null) {
                    List<DDoSAttack> followingAttacks = new ArrayList<>();
                    for (DDoSAttack attack : allAttacks) {
                        if (attack.getOwner().getId().equals(botId)) {
                            followingAttacks.add(attack);
                        }
                    }
                    controller.runOnUiThread(() -> mCallback.attacksFetchedSuccess(followingAttacks));
                }
            } catch (InterruptedException e) {
                //  If we are interrupted we just end the thread by returning
                Log.d(TAG, "fetchFollowingAttacksThread was interrupted");
                return;
            }
        };
    }
}
