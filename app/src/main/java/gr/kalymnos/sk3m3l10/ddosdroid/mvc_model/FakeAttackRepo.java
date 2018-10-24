package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSBot;

public class FakeAttackRepo implements AttackRepository {

    List<DDoSAttack> allAttacks;

    public FakeAttackRepo() {
        this.allAttacks = getFakeAttackList(16);
    }

    @Override
    public List<DDoSAttack> getAllAttacks() {
        return allAttacks;
    }

    @Override
    public List<DDoSAttack> getFollowingAttakcs(String botId) {
        List<DDoSAttack> list = new ArrayList<>();
        for (DDoSAttack attack : allAttacks) {
            if (attack.getOwner().getId().equals(botId)) {
                list.add(attack);
            }
        }
        return list;
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
