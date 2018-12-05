package gr.kalymnos.sk3m3l10.ddosdroid.pojos;

/*  This class represents a single bot which apparently
    belongs to a botnet of a Attack.*/

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.instance_id_provider.FakeInstanceIdProvider;

public class Bot {

    public interface OnJoinedAttackListener {
        void onJoinedAttackSuccess();

        void onJoinedAttackFail();
    }

    public interface OnLeftAttackListener {
        void onLeftAttackSuccess();

        void onLeftAttackFail();
    }

    private String id;

    public Bot(String instanceId) {
        this.id = instanceId;
    }

    public String getId() {
        return id;
    }

    public void joinAttack(String attackId, OnJoinedAttackListener listener) {

    }

    public void leaveAttack(String attackId, OnLeftAttackListener listener) {

    }

    public static Bot getLocalUserDDoSBot() {
        //  TODO: Replace with real InstanceIdProvider
        return new Bot(new FakeInstanceIdProvider().getInstanceId());
    }
}
