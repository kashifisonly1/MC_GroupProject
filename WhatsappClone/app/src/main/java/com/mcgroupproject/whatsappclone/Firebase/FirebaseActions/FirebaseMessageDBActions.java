package com.mcgroupproject.whatsappclone.Firebase.FirebaseActions;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseModels.FirebaseMessageModel;

import java.util.EventListener;

public class FirebaseMessageDBActions {
    Activity activity;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    public FirebaseMessageDBActions(Activity activity) {
        this.activity = activity;
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void DeleteOlderMessage(String last_push_id) {
        if(!last_push_id.equals("")){
            firebaseDatabase.getReference("/messages/"+firebaseAuth.getUid()).orderByKey().endAt(last_push_id).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    snapshot.getRef().removeValue().addOnCompleteListener(task -> { });
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });}
    }

    public DatabaseReference MessagesListener(String uid) {
        DatabaseReference recRev = firebaseDatabase.getReference("messages/"+uid);
        recRev.keepSynced(true);
        return recRev;
    }

    public Task<Void> SendMessage(FirebaseMessageModel msg, String ID) {
        return firebaseDatabase.getReference("messages/"+ID).push().setValue(msg);
    }

    public DatabaseReference AddNewMessage(String id) {
        return firebaseDatabase.getReference("messages/"+id).push();
    }
}
