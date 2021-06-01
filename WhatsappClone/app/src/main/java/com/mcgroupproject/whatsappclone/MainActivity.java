package com.mcgroupproject.whatsappclone;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import com.mcgroupproject.whatsappclone.database.*;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.mcgroupproject.whatsappclone.Fragments.MainFragment;
import com.mcgroupproject.whatsappclone.Fragments.ProfileFragment;
import com.mcgroupproject.whatsappclone.model.Message;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.EventListener;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    FirebaseDatabase db;
    FirebaseAuth mAuth;
    ChildEventListener listener;
    DatabaseReference recRev;
    private String current_frame = "chatlist";
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(Firebase.auth==null)
            Firebase.init();
        mAuth = Firebase.auth;
        db = Firebase.db;
        try{
            db.setPersistenceEnabled(true);
        }
        catch (Exception e){

        }
        if(mAuth.getCurrentUser()==null || (mAuth.getUid().equals(null)))
        {
            Intent intent = new Intent(getBaseContext(), SignupPage.class);
            startActivity(intent);
            finish();
            return;
        }
        db.getReference().child("users").child(mAuth.getUid()).child("Status").setValue("online").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
        long time = Instant.now().toEpochMilli();
        db.getReference().child("users").child(mAuth.getUid()).child("Status").onDisconnect().setValue(Long.toString(time));
        recRev = db.getReference("messages/"+mAuth.getUid());
        recRev.keepSynced(true);
        listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MessageModel msg = snapshot.getValue(MessageModel.class);
                if(msg.type==null)
                    msg.type="";
                if(msg.type.equals("message"))
                {
                    Date date = new Date(msg.time);
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE,MMMM d,yyyy", Locale.ENGLISH);
                    SimpleDateFormat tdf = new SimpleDateFormat(" h:mm,a", Locale.ENGLISH);
                    String timeVal = tdf.format(date);
                    String dateVal = sdf.format(date);
                    Message m=new Message(msg.msgID, msg.sender, mAuth.getUid(), msg.msg, 2, timeVal, dateVal, null, null, null, null);
                    MessageDB.Add(m);
                    String sender = msg.sender;
                    MessageModel newMSG = new MessageModel();
                    newMSG.msg = "3";
                    newMSG.type = "ack";
                    newMSG.msgID = msg.msgID;
                    newMSG.sender = mAuth.getUid();
                    newMSG.time = 912345;
                    sendAcknowledge(newMSG, sender);
                }
                else{
                    MessageDB.Update(msg.msgID, msg.msg);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        recRev.addChildEventListener(listener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        current_frame="chatlist";
        MainFragment fragment1 = new MainFragment();
        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_chatlist, fragment1);
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(recRev!=null&&listener!=null)
            recRev.removeEventListener(listener);
    }

    public void searchUser(View view) {
        EditText searchUSerText = findViewById(R.id.search_user);
        String phone = searchUSerText.getText().toString();
        Intent intent = new Intent(getBaseContext(), ChatPage.class);
        db.getReference().child("users").orderByChild("Phone").equalTo(phone)
        .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_LONG).show();
                }
                else {
                    Map<String, Map<String, String>> map = (Map)task.getResult().getValue();
                    if(map==null)
                    {
                        Toast.makeText(getApplicationContext(), "Please enter valid phone", Toast.LENGTH_LONG).show();
                        return;
                    }
                    String key = map.keySet().iterator().next();
                    Map<String, String> user = map.get(key);
                    System.out.println(user);
                    intent.putExtra("phone", user.get("Phone"));
                    intent.putExtra("uid", user.get("UID"));
                    intent.putExtra("name", user.get("Name"));
                    intent.putExtra("status", user.get("Status"));
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    public void sendAcknowledge(final MessageModel msg, String ID) {
        db.getReference("messages/"+ID).push().setValue(msg).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            MessageDB.Update(msg.msgID, msg.msg);
                        }
                        else{
                        }
                    }
                }
        );
    }

    public void profileFunction(MenuItem item) {
        if(current_frame=="profile")
            return;
        current_frame = "profile";
        ProfileFragment fragment1 = new ProfileFragment();
        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_chatlist, fragment1);
        transaction.commit();
    }
    public void logoutFunction(MenuItem item)
    {
        mAuth.signOut();
        recreate();
    }
    public void chatlistFunction(MenuItem item) {
        if(current_frame=="chatlist")
            return;
        current_frame = "chatlist";
        MainFragment fragment1 = new MainFragment();
        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_chatlist, fragment1);
        transaction.commit();
    }
}