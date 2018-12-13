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
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.Bot;

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
        attacksRef.addListenerForSingleValueEvent(new JoinedAttacksValueEventListenerOfBotAndNetworkType(botId, networkType));
    }

    @Override
    public void fetchNotJoinedAttacksOf(String botId) {
        attacksRef.addListenerForSingleValueEvent(new NotJoinedAttacksValueEventListenerOfBot(botId));
    }

    @Override
    public void fetchNotJoinedAttacksOf(String botId, int networkType) {
        attacksRef.addListenerForSingleValueEvent(new NotJoinedAttacksValueEventListenerOfBotAndNetworkType(botId, networkType));
    }

    @Override
    public void fetchLocalOwnerAttacks() {
        attacksRef.addListenerForSingleValueEvent(new LocalOwnerAttacksValueEventListener());
    }

    @Override
    public void fetchLocalOwnerAttacksOf(int networkType) {
        attacksRef.addListenerForSingleValueEvent(new LocalOwnerAttacksValueEventListenerOfNetworkType(networkType));
    }

    @Override
    public void uploadAttack(Attack attack) {
        String pushId = attacksRef.push().getKey();
        attack.setPushId(pushId);
        attacksRef.child(pushId).setValue(attack, (error,ref)->onAttackUploadedListener.onAttackUploaded(attack));
    }

    @Override
    public void updateAttack(Attack attack) {
        attacksRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if (snapshot.getKey().equals(attack.getPushId())){
                        snapshot.getRef().setValue(attack);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                
            }
        });
    }

    private abstract class AbstractValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List<Attack> attacks = extractAttacksFrom(dataSnapshot);
            onAttacksFetchListener.attacksFetchedSuccess(attacks);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            onAttacksFetchListener.attacksFetchedFail(databaseError.getMessage());
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

        protected int networkType;

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

    private class NotJoinedAttacksValueEventListenerOfBot extends JoinedAttacksValueEventListenerOfBot {

        public NotJoinedAttacksValueEventListenerOfBot(String botId) {
            super(botId);
        }

        @Override
        protected List<Attack> extractAttacksFrom(DataSnapshot dataSnapshot) {
            List<Attack> attacks = new ArrayList<>();
            for (DataSnapshot child : dataSnapshot.getChildren()) {
                Attack attack = child.getValue(Attack.class);
                if (!botJoinedAttack(attack)) {
                    attacks.add(attack);
                }
            }
            return attacks;
        }
    }

    private class NotJoinedAttacksValueEventListenerOfBotAndNetworkType extends JoinedAttacksValueEventListenerOfBotAndNetworkType {

        public NotJoinedAttacksValueEventListenerOfBotAndNetworkType(String botId, int networkType) {
            super(botId, networkType);
        }

        @Override
        protected List<Attack> extractAttacksFrom(DataSnapshot dataSnapshot) {
            List<Attack> attacks = new ArrayList<>();
            for (DataSnapshot child : dataSnapshot.getChildren()) {
                Attack attack = child.getValue(Attack.class);
                if (!botJoinedAttack(attack) && attack.getNetworkType() == networkType) {
                    attacks.add(attack);
                }
            }
            return attacks;
        }
    }

    private class LocalOwnerAttacksValueEventListener extends AbstractValueEventListener {

        @Override
        protected List<Attack> extractAttacksFrom(DataSnapshot dataSnapshot) {
            List<Attack> attacks = new ArrayList<>();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                Attack attack = snapshot.getValue(Attack.class);
                if (belongsToLocalOwner(attack)) {
                    attacks.add(attack);
                }
            }
            return attacks;
        }

        protected boolean belongsToLocalOwner(Attack attack) {
            return attack.getOwner().getId().equals(Bot.getLocalUser());
        }
    }

    private class LocalOwnerAttacksValueEventListenerOfNetworkType extends LocalOwnerAttacksValueEventListener {
        private int networkType;

        public LocalOwnerAttacksValueEventListenerOfNetworkType(int networkType) {
            this.networkType = networkType;
        }

        @Override
        protected List<Attack> extractAttacksFrom(DataSnapshot dataSnapshot) {
            List<Attack> attacks = new ArrayList<>();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                Attack attack = snapshot.getValue(Attack.class);
                if (belongsToLocalOwner(attack) && attack.getNetworkType() == networkType) {
                    attacks.add(attack);
                }
            }
            return attacks;
        }
    }
}
