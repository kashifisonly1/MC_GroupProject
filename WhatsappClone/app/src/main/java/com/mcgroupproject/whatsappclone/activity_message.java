package com.mcgroupproject.whatsappclone;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mcgroupproject.whatsappclone.adapter.MessagesAdapter;
import com.mcgroupproject.whatsappclone.model.Message;

import java.util.ArrayList;
import java.util.List;

public class activity_message extends Activity {

    FirebaseAuth mAuth;
    FirebaseDatabase db;
    String phone;
    String id;
    TextView online_statusView;
    TextView nameView;
    EditText msgBox;
    private RecyclerView recyclerView;
    private List<Message> messageList =  new ArrayList<>();
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_message);

            db = Firebase.db;
            mAuth = Firebase.auth;
            phone = getIntent().getStringExtra("phone");
            id = getIntent().getStringExtra("uid");
            nameView = findViewById(R.id.chat_name);
            online_statusView = findViewById(R.id.status);
            nameView.setText(getIntent().getStringExtra("name"));
            online_statusView.setText(getIntent().getStringExtra("status"));
            msgBox = findViewById(R.id.message_box_area);
            DatabaseReference ref= db.getReference("users/"+id);
            ref.keepSynced(true);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user= snapshot.getValue(User.class);
                    nameView.setText(user.Name);
                    online_statusView.setText(user.Status);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    System.out.println("errror");
                }
            });
            recyclerView = (RecyclerView) findViewById(R.id.messages_recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            getMessagesList();
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

    private void getMessagesList() {

            messageList.add(new Message("1","1","1","this message is not yet sent",1,"12:30AM","1/1/21","1","s1","r1","rep1"));
            messageList.add(new Message("2","2","2","this message sent",2,"01:23AM","2/3/21","2","s2","r2","rep2"));
            messageList.add(new Message("3","3","3","this message delivered",3,"03:43PM","3/23/21","3","s3","r3","rep3"));
            messageList.add(new Message("4","4","4","this is is seen by the receiver",4,"12:00AM","4/12/21","4","s4","r4","rep4"));
            recyclerView.setAdapter(new MessagesAdapter(messageList, this));
    }
}
