package com.mcgroupproject.whatsappclone;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mcgroupproject.whatsappclone.adapter.MessagesAdapter;
import com.mcgroupproject.whatsappclone.database.MessageDB;
import com.mcgroupproject.whatsappclone.model.Message;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class activity_message extends Activity {

    FirebaseAuth mAuth;
    FirebaseDatabase db;
    String phone;
    String id;
    TextView online_statusView;
    TextView nameView;
    EditText msgBox;
    ChildEventListener listener;
    DatabaseReference recRev;
    private RecyclerView recyclerView;
    private List<Message> messageList =  new ArrayList<>();
        @RequiresApi(api = Build.VERSION_CODES.O)
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
            online_statusView.setText("loading...");
            nameView.setText(getIntent().getStringExtra("name"));
            msgBox = findViewById(R.id.message_box_area);
            DatabaseReference ref= db.getReference("users/"+id);
            ref.keepSynced(true);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user= snapshot.getValue(User.class);
                    nameView.setText(user.Name);
                    online_statusView.setText(user.Status);
                    if(user.Status.equals("Online") || user.Status.equals("online"))
                        online_statusView.setText(user.Status);
                    else
                    {
                        Date date = new Date(Long.parseLong(user.Status));
                        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d,yyyy", Locale.ENGLISH);
                        SimpleDateFormat tdf = new SimpleDateFormat(" h:mm a", Locale.ENGLISH);
                        String timeVal = tdf.format(date);
                        String dateVal = sdf.format(date);
                        online_statusView.setText(dateVal + "  "+timeVal);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    System.out.println("errror");
                }
            });
            recyclerView = (RecyclerView) findViewById(R.id.messages_recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            getMessagesList();
            long time = Instant.now().toEpochMilli();
            db.getReference().child("users").child(mAuth.getUid()).child("Status").onDisconnect().setValue(Long.toString(time));
            recRev = db.getReference("messages/"+mAuth.getUid());
            recRev.keepSynced(true);
            listener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    MessageModel msg = snapshot.getValue(MessageModel.class);
                    if(!msg.sender.equals(id))
                        return;
                    if(msg.type==null)
                        msg.type="";
                    if(msg.type.equals("message")) {
                        Date date = new Date(msg.time);
                        SimpleDateFormat sdf = new SimpleDateFormat("EEEE,MMMM d,yyyy", Locale.ENGLISH);
                        SimpleDateFormat tdf = new SimpleDateFormat(" h:mm,a", Locale.ENGLISH);
                        String timeVal = tdf.format(date);
                        String dateVal = sdf.format(date);
                        Message message=new Message(msg.msgID, msg.sender, mAuth.getUid(), msg.msg, 2, timeVal, dateVal, null, null, null, null);
                        MessageDB.Add(message);
                        messageList.add(message);
                    }
                    else {
                        for(int i =0;i<messageList.size();i++)
                        {
                            Message m = messageList.get(i);
                            if(m.getId().equals(msg.msgID))
                                m.setStatus(Integer.parseInt(msg.msg));
                        }
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
    protected void onDestroy() {
        super.onDestroy();
        recRev.removeEventListener(listener);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendMessage(View view) {
        String message = msgBox.getText().toString();
        String sender = mAuth.getUid();
        String msgID = (Instant.now().toEpochMilli())+mAuth.getUid();
        String type = "message";
        MessageModel msg = new MessageModel();
        msg.msg = message;
        msg.msgID = msgID;
        msg.sender = sender;
        msg.time = Instant.now().toEpochMilli();//current time, will be adjust later
        msg.type = type;
        Date date = new Date(msg.time);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE,MMMM d,yyyy", Locale.ENGLISH);
        SimpleDateFormat tdf = new SimpleDateFormat(" h:mm,a", Locale.ENGLISH);
        String timeVal = tdf.format(date);
        String dateVal = sdf.format(date);
        Message m=new Message(msg.msgID, msg.sender, id, msg.msg, 1, timeVal, dateVal, null, null, null, null);
        MessageDB.Add(m);
        messageList.add(m);
        msgBox.setText("");
        db.getReference("messages/"+id).push().setValue(msg).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            MessageDB.Update(msg.msgID, "2");
                            for(int i =0;i<messageList.size();i++) {
                                Message m = messageList.get(i);
                                if (m.getId().equals(msg.msgID))
                                    m.setStatus(2);
                            }
                        }
                        else{

                        }
                    }
                }
        );
    }

    private void getMessagesList() {
            List<Message> l = MessageDB.Get(id);
            System.out.println(l.size());
            for(int i = 0; i<l.size(); i++)
                messageList.add(l.get(i));
            recyclerView.setAdapter(new MessagesAdapter(messageList, this));
    }
}