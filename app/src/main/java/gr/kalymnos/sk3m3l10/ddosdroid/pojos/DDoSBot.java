package gr.kalymnos.sk3m3l10.ddosdroid.pojos;

/*  This class represents a single bot which apparently
    belongs to a botnet of a DDoSAttack.*/

import com.google.firebase.iid.FirebaseInstanceId;

public class DDoSBot {

    public interface OnJoinedAttackListener {
        void onJoinedAttackSuccess();

        void onJoinedAttackFail();
    }

    public interface OnLeftAttackListener {
        void onLeftAttackSuccess();

        void onLeftAttackFail();
    }

    private String id;

    public DDoSBot(String instanceId) {
        this.id = instanceId;
    }

    public String getId() {
        return id;
    }

    public void joinAttack(String attackId, OnJoinedAttackListener listener) {

    }

    public void leaveAttack(String attackId, OnLeftAttackListener listener) {

    }

    public static DDoSBot getLocalUserDDoSBot() {
        String instanceId = FirebaseInstanceId.getInstance().getId();
        return new DDoSBot(instanceId);
    }
}
