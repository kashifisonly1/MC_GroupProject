package com.mcgroupproject.whatsappclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase db;
    FirebaseAuth mAuth;
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
        if(mAuth.getCurrentUser()==null)
        {
            Intent intent = new Intent(getBaseContext(), SignupPage.class);
            startActivity(intent);
        }
        TextView textView = findViewById(R.id.receiver);
        textView.setText("Welcome, " + mAuth.getCurrentUser().getDisplayName());
        db.getReference().child("users").child(mAuth.getUid()).child("Status").setValue("online").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
        db.getReference().child("users").child(mAuth.getUid()).child("Status").onDisconnect().setValue(ServerValue.TIMESTAMP.toString());
        DatabaseReference recRev = db.getReference("messages/"+mAuth.getUid());
        recRev.keepSynced(true);
        recRev.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    MessageModel msg = snapshot.getValue(MessageModel.class);
                    System.out.println(msg.toString());
                    Toast.makeText(getApplicationContext(), msg.toString(),Toast.LENGTH_LONG).show();
                    if(msg.type==null)
                        msg.type="";
                    if(msg.type.equals("message"))
                    {
                        String sender = msg.sender;
                        MessageModel newMSG = new MessageModel();
                        newMSG.msg = "delivered";
                        newMSG.type = "ack";
                        newMSG.msgID = msg.msgID;
                        newMSG.sender = mAuth.getUid();
                        newMSG.time = 912345;
                        sendAcknowledge(newMSG, sender);
                    }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                System.out.println("2");
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
        });
    }



    public void signout(View view) {
        mAuth.signOut();
        Intent intent = new Intent(getBaseContext(), SignupPage.class);
        startActivity(intent);
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
                   // for(DataSnapshot d:task.getResult().getChildren())
                     //   user = d.getValue(User.class);
                    intent.putExtra("phone", user.get("Phone"));
                    intent.putExtra("uid", user.get("UID"));
                    intent.putExtra("name", user.get("Name"));
                    intent.putExtra("status", user.get("Status"));
                    startActivity(intent);
                }
            }
        });
    }
    public void sendAcknowledge(MessageModel msg, String ID) {
        db.getReference("messages/"+ID).push().setValue(msg).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Acknowledge Sent", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Acknowledge NOT Sent", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }
}