package com.mcgroupproject.whatsappclone.Firebase.FirebaseActions;

import android.app.Activity;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseModels.FirebaseUserModel;

import java.time.Instant;

public class FirebaseUserDBActions {
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    Activity activity;

    public FirebaseUserDBActions(Activity activity) {
        this.activity = activity;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        try{
            firebaseDatabase.setPersistenceEnabled(true);
        }
        catch (Exception e) {

        }
    }

    public Task<Void> SetValues(FirebaseUserModel user) {
        return firebaseDatabase.getReference().child("users").child(user.UID).setValue(user);
    }

    public Task<DataSnapshot> SearchUserByPhone(String phone) {
        return firebaseDatabase.getReference("/users").orderByChild("Phone").equalTo(phone).get();
    }
    public Task<DataSnapshot> SearchUserByID(String uid) {
        return firebaseDatabase.getReference("users/"+uid).get();
    }

    public void SetUserOnline(String uid) {
        firebaseDatabase.getReference().child("users").child(uid).child("Status").setValue("online").addOnCompleteListener(task -> {

        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void SetDisconnection() {
        long time = Instant.now().toEpochMilli();
        firebaseDatabase.getReference().child("users").child(firebaseAuth.getUid()).child("Status").onDisconnect().setValue(Long.toString(time));

    }

    public DatabaseReference UserStatusListening(String id) {
        DatabaseReference userRef = firebaseDatabase.getReference("users/"+id);
        userRef.keepSynced(true);
        return userRef;
    }
}
