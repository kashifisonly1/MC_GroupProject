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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class ProfileCreationPage extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText textView;
    FirebaseDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creation_page);
        mAuth = Firebase.auth;
        textView = findViewById(R.id.nameField);
        db = Firebase.db;
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
                    User user = new User();
                    user.UID= mAuth.getUid();
                    user.Name=mAuth.getCurrentUser().getDisplayName();
                    user.Phone=mAuth.getCurrentUser().getPhoneNumber();
                    user.Status="online";
                    updateDatabase(user);
                }else {
                    Toast.makeText(getApplicationContext(), "Could not change name", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void updateDatabase(User user)
    {
        db.getReference().child("users").child(user.UID)
        .setValue(user)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(getBaseContext(), chatList.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Could not update data", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}