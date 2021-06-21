package com.mcgroupproject.whatsappclone.Firebase.FirebaseListeners;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;

public class ActionFailureListener implements OnFailureListener {
    Activity activity;

    public ActionFailureListener(Activity activity) {
        this.activity=activity;
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        Toast.makeText(activity.getApplicationContext(), "COULD NOT PERFORM ACTION", Toast.LENGTH_LONG).show();
    }
}
