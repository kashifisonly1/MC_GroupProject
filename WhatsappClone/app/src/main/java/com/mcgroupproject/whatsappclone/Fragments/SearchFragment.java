package com.mcgroupproject.whatsappclone.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.mcgroupproject.whatsappclone.Firebase;
import com.mcgroupproject.whatsappclone.R;
import com.mcgroupproject.whatsappclone.activity_message;
import com.mcgroupproject.whatsappclone.database.UserDB;
import com.mcgroupproject.whatsappclone.model.User;

import java.util.List;
import java.util.Map;

public class SearchFragment extends Fragment {
    FirebaseDatabase db;
    FirebaseAuth mauth;
    List<User> users;
    public SearchFragment(){}
    public SearchFragment(List<User> users) {
        this.users=users;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mauth = Firebase.auth;
        db = Firebase.db;
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        view.findViewById(R.id.searchPhoneBTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText t =  view.findViewById(R.id.searchPhoneField);
                db.getReference("/users").orderByChild("Phone").equalTo(t.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Map<String, Map<String, String>> map = (Map)task.getResult().getValue();
                            if(map==null)
                            {
                                Toast.makeText(getContext(), "Please enter valid phone", Toast.LENGTH_LONG).show();
                                return;
                            }
                            String key = map.keySet().iterator().next();
                            Map<String, String> user = map.get(key);
                            if(user.get("UID").equals(mauth.getUid())){
                                Toast.makeText(getContext(), "Please enter valid phone", Toast.LENGTH_LONG).show();
                                return;
                            }
                            User u = new User(user.get("UID"), user.get("Phone"), user.get("Name"), null, null, null);
                            if(UserDB.Add(u))
                                users.add(u);
                            Intent intent = new Intent(getContext(), activity_message.class);
                            intent.putExtra("phone", user.get("Phone"));
                            intent.putExtra("uid", user.get("UID"));
                            intent.putExtra("name", user.get("Name"));
                            intent.putExtra("status", user.get("Status"));
                            startActivity(intent);
                        }
                    }
                });
            }
        });
        return view;
    }
}