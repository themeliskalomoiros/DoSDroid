package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attacks.attack_repo;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.Attack;

public class FirebaseRepository extends AttackRepository {
    private static final String NODE_ATTACKS = "attacks";

    private DatabaseReference rootRef;

    public FirebaseRepository() {
        this.rootRef = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void fetchAllAttacks() {

    }

    @Override
    public void fetchAllAttacksOf(int networkType) {

    }

    @Override
    public void fetchJoinedAttakcsOf(String botId) {

    }

    @Override
    public void fetchJoinedAttakcsOf(String botId, int networkType) {

    }

    @Override
    public void fetchNotJoinedAttacksOf(String botId) {

    }

    @Override
    public void fetchNotJoinedAttacksOf(String botId, int networkType) {

    }

    @Override
    public void fetchLocalOwnerAttacks() {

    }

    @Override
    public void fetchLocalOwnerAttacksOf(int networkType) {

    }

    @Override
    public void uploadAttack(Attack attack) {

    }
}
