package com.mcgroupproject.whatsappclone.Firebase.FirebaseListeners;

import android.app.Activity;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.mcgroupproject.whatsappclone.Activity.ProfileCreationActivity;

public class AuthSignInListener implements OnCompleteListener<AuthResult> {
    private final Activity activity;
    private final EditText codeField;
    public AuthSignInListener(Activity activity, EditText codeField) {
        this.activity=activity;
        this.codeField = codeField;
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
            Intent intent = new Intent(activity.getBaseContext(), ProfileCreationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        } else {
            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                codeField.setError("Invalid code");
            }
            else {
                Toast.makeText(activity.getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
