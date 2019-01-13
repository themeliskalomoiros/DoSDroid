package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.repository;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attacks;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.hostinfo.HostInfo;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.hostinfo.HostInfoHelper;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.bot.Bot;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.bot.Bots;

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
        attacksRef.child(attack.getPushId()).setValue(attack, (error, ref) -> onAttackUploadedListener.onAttackUploaded(attack));
    }

    @Override
    public void updateAttack(Attack attack) {
        attacksRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals(attack.getPushId())) {
                        snapshot.getRef().setValue(attack);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void deleteAttack(String pushId) {
        DatabaseReference attackRef = attacksRef.child(pushId);
        attackRef.removeValue();
    }

    private abstract class AbstractValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List<Attack> attacks = extractAttacksFrom(dataSnapshot);
            onAttacksFetchListener.attacksFetched(attacks);
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
                attacks.add(new AttackResolver(child).resolveInstance());
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
                Attack attack = new AttackResolver(child).resolveInstance();
                if (attack.getNetworkType() == networkType) {
                    attacks.add(attack);
                }
            }
            return attacks;
        }
    }

    private class JoinedAttacksValueEventListenerOfBot extends AbstractValueEventListener {

        protected String botId;

        public JoinedAttacksValueEventListenerOfBot(String botId) {
            this.botId = botId;
        }

        @Override
        protected List<Attack> extractAttacksFrom(DataSnapshot dataSnapshot) {
            List<Attack> attacks = new ArrayList<>();
            for (DataSnapshot child : dataSnapshot.getChildren()) {
                Attack attack = new AttackResolver(child).resolveInstance();
                if (botJoinedAttack(attack)) {
                    attacks.add(attack);
                }
            }
            return attacks;
        }

        protected final boolean botJoinedAttack(Attack attack) {
            return Attacks.includes(attack, new Bot(botId)) && !Attacks.ownedBy(attack, new Bot(botId));
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
                Attack attack = new AttackResolver(child).resolveInstance();
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
                Attack attack = new AttackResolver(child).resolveInstance();
                if (botNotBelongTo(attack)) {
                    attacks.add(attack);
                }
            }
            return attacks;
        }

        protected final boolean botNotBelongTo(Attack attack) {
            return !Attacks.includes(attack, new Bot(botId)) && !Attacks.ownedBy(attack, new Bot(botId));
        }
    }

    private class NotJoinedAttacksValueEventListenerOfBotAndNetworkType extends NotJoinedAttacksValueEventListenerOfBot {

        private int networkType;

        public NotJoinedAttacksValueEventListenerOfBotAndNetworkType(String botId, int networkType) {
            super(botId);
            this.networkType = networkType;
        }

        @Override
        protected List<Attack> extractAttacksFrom(DataSnapshot dataSnapshot) {
            List<Attack> attacks = new ArrayList<>();
            for (DataSnapshot child : dataSnapshot.getChildren()) {
                Attack attack = new AttackResolver(child).resolveInstance();
                if (botNotBelongTo(attack) && attack.getNetworkType() == networkType) {
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
                Attack attack = new AttackResolver(snapshot).resolveInstance();
                if (createdByLocalUser(attack)) {
                    attacks.add(attack);
                }
            }
            return attacks;
        }

        protected boolean createdByLocalUser(Attack attack) {
            String hostInfo = attack.getHostInfo().getUuid();
            String localUser = Bots.getLocalUser().getUuid();
            return hostInfo.equals(localUser);
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
                Attack attack = new AttackResolver(snapshot).resolveInstance();
                if (createdByLocalUser(attack) && attack.getNetworkType() == networkType) {
                    attacks.add(attack);
                }
            }
            return attacks;
        }
    }

    private class AttackResolver {
        private DataSnapshot snapshot;

        AttackResolver(DataSnapshot snapshot) {
            initializeSnapshot(snapshot);
        }

        private void initializeSnapshot(DataSnapshot snapshot) {
            if (invalidSnapshot(snapshot))
                throw new IllegalArgumentException(TAG + ": wrong attack snapshot");
            this.snapshot = snapshot;
        }

        private boolean invalidSnapshot(DataSnapshot snapshot) {
            return !snapshot.getRef().getParent().getKey().equals(NODE_ATTACKS);
        }

        Attack resolveInstance() {
            String pushId = snapshot.getKey();
            String website = snapshot.child("website").getValue(String.class);
            long timeMillis = snapshot.child("timeMillis").getValue(Long.class);
            int networkType = snapshot.child("networkType").getValue(Integer.class);
            HostInfo creator = new HostInfoResolver(snapshot.child("hostInfo"), networkType).resolveInstance();
            List<String> botIds = (List<String>) snapshot.child("botIds").getValue();
            return new Attack(pushId, website, networkType, timeMillis, creator, botIds);
        }
    }
}
