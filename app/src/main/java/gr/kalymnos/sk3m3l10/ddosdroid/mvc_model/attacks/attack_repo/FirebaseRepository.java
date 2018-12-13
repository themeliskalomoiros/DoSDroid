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
        attacksRef.addListenerForSingleValueEvent(new AttacksValueEventListenerOfNetworkType(networkType));
    }

    @Override
    public void fetchJoinedAttakcsOf(String botId) {
        attacksRef.addListenerForSingleValueEvent(new JoinedAttacksValueEventListenerOfBot(botId));
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

    private class AttacksValueEventListenerOfNetworkType extends AbstractValueEventListener {

        private int networkType;

        public AttacksValueEventListenerOfNetworkType(int networkType) {
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

    private class JoinedAttacksValueEventListenerOfBot extends AbstractValueEventListener {

        private String botId;

        public JoinedAttacksValueEventListenerOfBot(String botId) {
            this.botId = botId;
        }

        @Override
        protected List<Attack> extractAttacksFrom(DataSnapshot dataSnapshot) {
            List<Attack> attacks = new ArrayList<>();
            for (DataSnapshot child : dataSnapshot.getChildren()) {
                Attack attack = child.getValue(Attack.class);
                if (botJoinedAttack(attack)) {
                    attacks.add(attack);
                }
            }
            return attacks;
        }

        protected boolean botJoinedAttack(Attack attack) {
            return !attack.isOwnedBy(botId) && attack.includes(botId);
        }
    }

    private class JoinedAttacksValueEventListenerOfBotAndNetworkType extends JoinedAttacksValueEventListenerOfBot {

        private int networkType;

        public JoinedAttacksValueEventListenerOfBotAndNetworkType(String botId, int networkType) {
            super(botId);
            this.networkType = networkType;
        }

        @Override
        protected List<Attack> extractAttacksFrom(DataSnapshot dataSnapshot) {
            List<Attack> attacks = new ArrayList<>();
            for (DataSnapshot child : dataSnapshot.getChildren()) {
                Attack attack = child.getValue(Attack.class);
                if (botJoinedAttack(attack) && attack.getNetworkType() == networkType) {
                    attacks.add(attack);
                }
            }
            return attacks;
        }
    }
}
