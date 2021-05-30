package com.mcgroupproject.whatsappclone.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.mcgroupproject.whatsappclone.Firebase;
import com.mcgroupproject.whatsappclone.MainActivity;
import com.mcgroupproject.whatsappclone.R;
import com.mcgroupproject.whatsappclone.User;

public class ProfileFragment extends Fragment {

    FirebaseAuth mAuth;
    EditText textView;
    FirebaseDatabase db;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mAuth = Firebase.auth;
        textView = view.findViewById(R.id.nameField);
        db = Firebase.db;
        textView.setText(mAuth.getCurrentUser().getDisplayName());
        Button button = (Button)view.findViewById(R.id.profile_btn_create);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setName(v);
            }
        });
        return view;
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
                    Toast.makeText(getContext(), "Could not change name", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), "Could not update data", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}