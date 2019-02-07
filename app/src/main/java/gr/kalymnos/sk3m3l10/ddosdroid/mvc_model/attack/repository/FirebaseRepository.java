package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.repository;

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

    public FirebaseRepository() {
        initializeAllAttacksRef();
    }

    private void initializeAllAttacksRef() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        allAttacksRef = firebaseDatabase.getReference().child(NODE_ATTACKS);
    }

    @Override
    public void startListenForChanges() {
        allAttacksRef.addChildEventListener(this);
    }

    @Override
    public void stopListenForChanges() {
        allAttacksRef.removeEventListener(this);
    }

    @Override
    public void upload(Attack attack) {
        allAttacksRef.child(attack.getPushId()).setValue(attack, null);
    }

    @Override
    public void update(Attack attack) {
        //  TODO: maybe can be refactored to allAttacksRef.child(attack.getPushId()).setValue(attack);
        allAttacksRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    if (snapshot.getKey().equals(attack.getPushId()))
                        snapshot.getRef().setValue(attack);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    //  Firebase's ChildEventListener methods
    @Override
    public void delete(String attackId) {
        DatabaseReference attackRef = allAttacksRef.child(attackId);
        attackRef.removeValue();
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Log.d(TAG, "onChildAdded()");
        Attack attack = dataSnapshot.getValue(Attack.class);
        onRepositoryChangeListener.onAttackUpload(attack);
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Log.d(TAG, "onChildChanged()");
        Attack attack = dataSnapshot.getValue(Attack.class);
        onRepositoryChangeListener.onAttackUpdate(attack);
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        Log.d(TAG, "onChildRemoved()");
        Attack attack = dataSnapshot.getValue(Attack.class);
        onRepositoryChangeListener.onAttackDelete(attack);
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
