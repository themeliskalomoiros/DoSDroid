package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSBot;

public class FakeAttackRepo implements AttackRepository {

    private static final long SLEEP_TIME_MILLIS = 1600;

    private List<DDoSAttack> allAttacks;
    private OnAttacksFetchListener mCallback;

    public FakeAttackRepo() {
        this.allAttacks = getFakeAttackList(16);
    }

    @Override
    public void fetchAllAttacks() {
        Thread worker = new Thread(() -> {
            try {
                Thread.sleep(SLEEP_TIME_MILLIS);
                if (mCallback != null) {
                    mCallback.attacksFetchedSuccess(allAttacks);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        worker.start();
    }

    @Override
    public void fetchFollowingAttakcs(String botId) {
        Thread worker = new Thread(() -> {
            try {
                Thread.sleep(SLEEP_TIME_MILLIS);
                if (mCallback != null) {
                    List<DDoSAttack> list = new ArrayList<>();
                    for (DDoSAttack attack : allAttacks) {
                        if (attack.getOwner().getId().equals(botId)) {
                            list.add(attack);
                        }
                    }
                    mCallback.attacksFetchedSuccess(allAttacks);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        worker.start();
    }

    @Override
    public void setOnAttacksFetchListener(OnAttacksFetchListener listener) {
        mCallback = listener;
    }

    @Override
    public void removeOnAttacksFetchListener() {
        
    }

    private List<DDoSAttack> getFakeAttackList(int size) {
        Random random = new Random();
        String websitePrefix = "website";
        String websiteSuffix = ".com";

        List<DDoSAttack> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(new DDoSAttack(websitePrefix + i + websiteSuffix,
                    getFakeBotnet(random.nextInt(7) + 3),
                    getFakeBotnet(7).get(3),
                    i % 2 == 0 ? true : false, System.currentTimeMillis()));
        }

        return list;
    }

    private List<DDoSBot> getFakeBotnet(int size) {
        Random random = new Random();
        String prefix = "botnet";

        List<DDoSBot> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(new DDoSBot(prefix + (random.nextInt(10) + 1)));
        }

        return list;
    }
}
