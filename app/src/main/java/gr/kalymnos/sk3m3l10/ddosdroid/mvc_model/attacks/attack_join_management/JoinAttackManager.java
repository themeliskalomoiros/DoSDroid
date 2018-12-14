package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attacks.attack_join_management;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attacks.attack_network.AttackNetwork;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.Attack;

public abstract class JoinAttackManager {
    protected OnJoinAttackListener onJoinAttackListener;

    private JoinAttackManager(){}

    public interface OnJoinAttackListener {
        void onAttackJoined();

        void onAttackJoinedFailed(String reason);
    }

    public abstract void joinAttack(Attack attack, AttackNetwork network);

    public void addOnJoinAttackListener(OnJoinAttackListener listener) {
        onJoinAttackListener = listener;
    }

    public void removeOnJoinAttackListener() {
        onJoinAttackListener = null;
    }
}
