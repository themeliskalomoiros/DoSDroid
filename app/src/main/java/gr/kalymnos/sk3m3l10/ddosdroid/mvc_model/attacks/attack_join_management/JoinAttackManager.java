package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attacks.attack_join_management;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attacks.attack_network.AttackNetwork;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.Attack;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.AttackConstants.NetworkType.BLUETOOTH;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.AttackConstants.NetworkType.INTERNET;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.AttackConstants.NetworkType.NSD;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.AttackConstants.NetworkType.WIFI_P2P;


public abstract class JoinAttackManager {
    private static final String TAG = "JoinAttackManager";

    public interface OnJoinAttackListener {
        void onAttackJoined();

        void onAttackJoinedFailed(String reason);
    }

    protected OnJoinAttackListener onJoinAttackListener;

    protected JoinAttackManager() {
    }

    public abstract void joinAttack(Attack attack, AttackNetwork network);

    public void addOnJoinAttackListener(OnJoinAttackListener listener) {
        onJoinAttackListener = listener;
    }

    public void removeOnJoinAttackListener() {
        onJoinAttackListener = null;
    }


    public interface Builder {
        public JoinAttackManager build(int attackNetworkType);
    }

    public static class BuilderImp implements Builder {

        @Override
        public JoinAttackManager build(int attackNetworkType) {
            switch (attackNetworkType) {
                case INTERNET:
                    return null;
                case WIFI_P2P:
                    //  TODO: need to implement
                    return null;
                case NSD:
                    //  TODO: need to implement
                    return null;
                case BLUETOOTH:
                    //  TODO: need to implement
                    return null;
                default:
                    throw new UnsupportedOperationException(TAG + ": Unknown attack network type.");
            }
        }
    }
}
