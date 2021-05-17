package com.mcgroupproject.whatsappclone;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChatPage extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase db;
    String phone;
    String id;
    TextView online_statusView;
    TextView nameView;
    TextView phoneView;
    EditText msgBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);
        db = Firebase.db;
        mAuth = Firebase.auth;
        phone = getIntent().getStringExtra("phone");
        id = getIntent().getStringExtra("uid");
        nameView = findViewById(R.id.msg_name_view);
        phoneView = findViewById(R.id.msg_phone_view);
        online_statusView = findViewById(R.id.msg_online_statusview);
        nameView.setText(getIntent().getStringExtra("name"));
        online_statusView.setText(getIntent().getStringExtra("status"));
        phoneView.setText(phone);
        msgBox = findViewById(R.id.msg_area);
        DatabaseReference  ref= db.getReference("users/"+id);
        ref.keepSynced(true);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user= snapshot.getValue(User.class);
                nameView.setText(user.Name);
                online_statusView.setText(user.Status);
                phoneView.setText(user.Phone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("errror");
            }
        });
    }

    public void sendMessage(View view) {
        String message = msgBox.getText().toString();
        String sender = mAuth.getUid();
        String msgID = "abc_123";
        String type = "message";
        MessageModel msg = new MessageModel();
        msg.msg = message;
        msg.msgID = msgID;
        msg.sender = sender;
        msg.time = 123456;//current time, will be adjust later
        msg.type = type;
        db.getReference("messages/"+id).push().setValue(msg).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Message NOT Sent", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }
}