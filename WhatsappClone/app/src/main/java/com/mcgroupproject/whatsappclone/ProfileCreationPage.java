package com.mcgroupproject.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ProfileCreationPage extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creation_page);
        mAuth = FirebaseAuth.getInstance();
        textView = findViewById(R.id.nameField);
        textView.setText(mAuth.getCurrentUser().getDisplayName());

    }

    public void setName(View view) {
        String name = textView.getText().toString();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        mAuth.getCurrentUser().updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(), "Could not change name", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}