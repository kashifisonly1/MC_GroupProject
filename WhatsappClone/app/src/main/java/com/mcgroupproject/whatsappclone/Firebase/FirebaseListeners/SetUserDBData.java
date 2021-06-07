package com.mcgroupproject.whatsappclone.Firebase.FirebaseListeners;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mcgroupproject.whatsappclone.Activity.MainActivity;

public class SetUserDBData<TResult> implements OnCompleteListener<TResult> {

    Activity activity;

    public SetUserDBData(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onComplete(@NonNull Task<TResult> task) {
        if (task.isSuccessful()) {
            Intent intent = new Intent(activity.getBaseContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        } else {
            Toast.makeText(activity.getApplicationContext(), "Could not update data", Toast.LENGTH_LONG).show();
        }
    }
}
