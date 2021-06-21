package com.mcgroupproject.whatsappclone.Firebase.FirebaseListeners;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.UploadTask;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseActions.FirebaseProfileActions;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseActions.FirebaseUserDBActions;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseModels.FirebaseUserModel;

public class UploadImageListener implements OnCompleteListener<UploadTask.TaskSnapshot> {
    Activity activity;

    public UploadImageListener(Activity activity) {
        this.activity = activity;

    }

    @Override
    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
        if(!task.isSuccessful()) {
            Toast.makeText(activity.getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseUserDBActions firebaseUserDBActions = new FirebaseUserDBActions(activity);
        FirebaseUserModel user = new FirebaseUserModel();
        FirebaseProfileActions firebaseProfileActions = new FirebaseProfileActions(activity);
        FirebaseUser firebaseUser = firebaseProfileActions.GetCurrentUser();
        user.Name = firebaseUser.getDisplayName();
        user.Phone = firebaseUser.getPhoneNumber();
        user.UID = firebaseUser.getUid();
        user.Status = "online";
        firebaseUserDBActions.SetValues(user)
                .addOnCompleteListener(new SetUserDBData<>(activity))
                .addOnFailureListener(new ActionFailureListener(activity));
    }
}
