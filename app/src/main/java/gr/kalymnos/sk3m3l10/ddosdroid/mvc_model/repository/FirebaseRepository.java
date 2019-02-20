package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

public class FirebaseRepository extends AttackRepository implements ChildEventListener {
    private static final String TAG = "FirebaseAttackRepositor";
    private static final String NODE_ATTACKS = "attacks";

    private DatabaseReference allAttacksRef;
    private boolean addedChildEventListener;

    public FirebaseRepository() {
        initAllAttacksRef();
    }

    private void initAllAttacksRef() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        allAttacksRef = firebaseDatabase.getReference().child(NODE_ATTACKS);
    }

    @Override
    public void startListenForChanges() {
        if (!addedChildEventListener) {
            allAttacksRef.addChildEventListener(this);
            addedChildEventListener = true;
        }
    }

    @Override
    public void stopListenForChanges() {
        if (addedChildEventListener) {
            allAttacksRef.removeEventListener(this);
        }
    }

    @Override
    public void upload(Attack attack) {
        Log.d(TAG, "upload()");
        allAttacksRef.child(attack.getPushId()).setValue(attack, null);
    }

    @Override
    public void update(Attack attack) {
        Log.d(TAG, "update()");
        allAttacksRef.addListenerForSingleValueEvent(getValueEventListenerOf(attack));
    }

    @NonNull
    private ValueEventListener getValueEventListenerOf(Attack attack) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot attacksSnapshot) {
                if (attackExistsInRepo(attacksSnapshot))
                    allAttacksRef.child(attack.getPushId()).setValue(attack, null);
            }

            private boolean attackExistsInRepo(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot attackSnapshot : dataSnapshot.getChildren()) {
                    Attack temp = attackSnapshot.getValue(Attack.class);
                    if (temp.equals(attack))
                        return true;
                }
                return false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
    }

    @Override
    public void delete(String attackId) {
        Log.d(TAG, "delete()");
        DatabaseReference attackRef = allAttacksRef.child(attackId);
        attackRef.removeValue();
    }

    //  Firebase's ChildEventListener methods
    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Log.d(TAG, "onChildAdded()");
        Attack attack = dataSnapshot.getValue(Attack.class);
        repositoryListener.onAttackUpload(attack);
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Log.d(TAG, "onChildChanged()");
        Attack attack = dataSnapshot.getValue(Attack.class);
        repositoryListener.onAttackUpdate(attack);
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        Log.d(TAG, "onChildRemoved()");
        Attack attack = dataSnapshot.getValue(Attack.class);
        repositoryListener.onAttackDelete(attack);
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
