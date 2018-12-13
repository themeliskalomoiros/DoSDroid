package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attacks.attack_repo;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.Attack;

public class FirebaseRepository extends AttackRepository {
    private static final String NODE_ATTACKS = "attacks";

    private DatabaseReference rootRef, attacksRef;

    public FirebaseRepository() {
        this.rootRef = FirebaseDatabase.getInstance().getReference();
        this.attacksRef = rootRef.child(NODE_ATTACKS);
    }

    @Override
    public void fetchAllAttacks() {
        attacksRef.addListenerForSingleValueEvent(new AttacksValueEventListener());
    }

    @Override
    public void fetchAllAttacksOf(int networkType) {
        attacksRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Attack> attacks = extractAttacksFrom(dataSnapshot, networkType);
                callback.attacksFetchedSuccess(attacks);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.attacksFetchedFail(databaseError.getMessage());
            }

            private List<Attack> extractAttacksFrom(DataSnapshot dataSnapshot, int networkType) {
                List<Attack> attacks = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Attack attack = snapshot.getValue(Attack.class);
                    if (attack.getNetworkType() == networkType) {
                        attacks.add(attack);
                    }
                }
                return attacks;
            }
        });
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

    private abstract class AbstractValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List<Attack> attacks = extractAttacksFrom(dataSnapshot);
            callback.attacksFetchedSuccess(attacks);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            callback.attacksFetchedFail(databaseError.getMessage());
        }

        protected abstract List<Attack> extractAttacksFrom(DataSnapshot dataSnapshot);
    }

    private class AttacksValueEventListener extends AbstractValueEventListener {

        @Override
        protected List<Attack> extractAttacksFrom(DataSnapshot dataSnapshot) {
            List<Attack> attacks = new ArrayList<>();
            for (DataSnapshot child : dataSnapshot.getChildren()) {
                attacks.add(child.getValue(Attack.class));
            }
            return attacks;
        }
    }

    private class AttacksValueEventListenerWithNetworkType extends AbstractValueEventListener {

        private int networkType;

        public AttacksValueEventListenerWithNetworkType(int networkType) {
            this.networkType = networkType;
        }

        @Override
        protected List<Attack> extractAttacksFrom(DataSnapshot dataSnapshot) {
            List<Attack> attacks = new ArrayList<>();
            for (DataSnapshot child : dataSnapshot.getChildren()) {
                Attack attack = child.getValue(Attack.class);
                if (attack.getNetworkType() == networkType) {
                    attacks.add(attack);
                }
            }
            return attacks;
        }
    }
}
