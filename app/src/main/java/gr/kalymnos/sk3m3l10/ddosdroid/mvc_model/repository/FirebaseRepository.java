package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

public class FirebaseRepository extends AttackRepository implements ChildEventListener {
    private static final String TAG = "FirebaseAttackRepositor";
    private static final String NODE_ATTACKS = "attacks";

    private DatabaseReference allAttacksRef;
    private boolean addedChildEventListener;

    public FirebaseRepository() {
        initializeAllAttacksRef();
    }

    private void initializeAllAttacksRef() {
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
        allAttacksRef.child(attack.getPushId()).setValue(attack, null);
    }

    @Override
    public void update(Attack attack) {
        upload(attack); // For firebase its pretty much the same, this database is kind of json file
    }

    @Override
    public void delete(String attackId) {
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
