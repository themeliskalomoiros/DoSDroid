package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

public class FirebaseAttackRepositoryReported extends AttackRepositoryReporter implements ChildEventListener {
    private DatabaseReference allAttacksRef;

    public FirebaseAttackRepositoryReported() {
        initializeAllAttacksRef();
    }

    private void initializeAllAttacksRef() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        allAttacksRef = firebaseDatabase.getReference().child(FirebaseRepository.NODE_ATTACKS);
    }

    @Override
    public void attach() {
        allAttacksRef.addChildEventListener(this);
    }

    @Override
    public void detach() {
        allAttacksRef.removeEventListener(this);
        this.onAttackNodeListener = null;
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Attack attack = dataSnapshot.getValue(Attack.class);
        onAttackNodeListener.onAttackAdded(attack);
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Attack attack = dataSnapshot.getValue(Attack.class);
        onAttackNodeListener.onAttackChanged(attack);
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        Attack attack = dataSnapshot.getValue(Attack.class);
        onAttackNodeListener.onAttackDeleted(attack);
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
