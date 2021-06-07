package com.mcgroupproject.whatsappclone.Firebase.FirebaseListeners;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseActions.FirebaseStorageActions;

public class UpdateProfileListener<TResult> implements OnCompleteListener<TResult> {

    Activity activity;
    byte[] bytes;
    public UpdateProfileListener(Activity activity, byte[] imageBytes) {
        this.activity = activity;
        this.bytes = imageBytes;
    }

    @Override
    public void onComplete(@NonNull Task<TResult> task) {
        if(task.isSuccessful()){
            FirebaseStorageActions firebaseStorageActions = new FirebaseStorageActions(activity);
            firebaseStorageActions.UploadProfileImage(bytes)
            .addOnCompleteListener(new UploadImageListener(activity))
            .addOnFailureListener(new ActionFailureListener(activity));
        }else {
            Toast.makeText(activity.getApplicationContext(), "Could not change name", Toast.LENGTH_LONG).show();
        }
    }
}
