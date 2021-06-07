package com.mcgroupproject.whatsappclone;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
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

//krwao
//initialize

public class activity_message extends Activity {

    FirebaseAuth mAuth;
    FirebaseDatabase db;
    String phone;
    String id;
    TextView online_statusView;
    TextView nameView;
    EditText msgBox;
    ImageView img;
    FirebaseStorage storage;
    ChildEventListener listener;
    DatabaseReference recRev;
    private RecyclerView recyclerView;
    private MessagesAdapter adapter;
    private List<Message> messageList =  new ArrayList<>();
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_message);
            MessageDB.Init(getApplicationContext());
            db = Firebase.db;
            mAuth = Firebase.auth;
            storage=Firebase.storage;
            img = findViewById(R.id.profile_image_link);
            phone = getIntent().getStringExtra("phone");
            id = getIntent().getStringExtra("uid");
            storage.getReference().child("profile/"+ id).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(getApplicationContext()).load(uri.toString()).into(img);
                }
            });
            //add wala error remove ho gya hai i think
            nameView = findViewById(R.id.chat_name);
            online_statusView = findViewById(R.id.status);
            String s = getIntent().getStringExtra("status");
            if(s.equals("online") || s.equals("Online") || s.equals("loading..."))
            {
                online_statusView.setText(s);
            }
            else{
                Date date = new Date(Long.parseLong(s));
                SimpleDateFormat sdf = new SimpleDateFormat("MMMM d,yyyy", Locale.ENGLISH);
                SimpleDateFormat tdf = new SimpleDateFormat(" h:mm a", Locale.ENGLISH);
                String timeVal = tdf.format(date);
                String dateVal = sdf.format(date);
                online_statusView.setText(dateVal + "  "+timeVal);
            }
            nameView.setText(getIntent().getStringExtra("name"));
            msgBox = findViewById(R.id.message_box_area);
            DatabaseReference ref= db.getReference("users/"+id);
            ref.keepSynced(true);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user= snapshot.getValue(User.class);
                    if(user==null)
                        return;
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
                        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d,yyyy", Locale.ENGLISH);
                        SimpleDateFormat tdf = new SimpleDateFormat(" h:mm,a", Locale.ENGLISH);
                        String timeVal = tdf.format(date);
                        String dateVal = sdf.format(date);
                        Message message=new Message(msg.msgID, msg.sender, mAuth.getUid(), msg.msg, 2, timeVal, dateVal, null, null, null, null, msg.time);
                        message.setPushId(snapshot.getKey());
                        MessageDB.Add(message);
                        messageList.add(message);
                        List<Message> mmm = new ArrayList<>();
                        mmm.add(message);
                        SendAcks(mmm);
                    }
                    else {
                        for(int i =0;i<messageList.size();i++)
                        {
                            Message m = messageList.get(i);
                            if(m.getId().equals(msg.msgID))
                            {
                               if(m.getStatus() >= Integer.parseInt(msg.msg))
                                   break;
                                m.setStatus(Integer.parseInt(msg.msg));
                            }
                        }
                        MessageDB.Update(msg.msgID, msg.msg);
                    }
                    adapter.notifyDataSetChanged();
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
            String last_push_id = MessageDB.GetLastPushID();
            recRev.orderByKey().startAfter(last_push_id).addChildEventListener(listener);
            List<Message> unread_msg = MessageDB.unreadMsg(id);
            List<Message> unseen_msg = MessageDB.unseenMsg();
            SendAcks(unread_msg);
            SendMsgs(unseen_msg);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void SendMsgs(List<Message> unseen_msg){
        for (Message m: unseen_msg) {
            System.out.println(m.getSenderID());
            MessageModel msg = new MessageModel();
            msg.sender=m.getSenderID();
            msg.type="message";
            msg.msg=m.getText();
            msg.time=Instant.now().toEpochMilli();
            msg.msgID=m.getId();
            DatabaseReference ref =  db.getReference("messages/"+id).push();
            String newKey = ref.getKey();
            m.setPushId(newKey);
            MessageDB.rm(m);
            MessageDB.Add(m);
            ref.setValue(msg).addOnCompleteListener(
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                MessageDB.Update(msg.msgID, "2");
                                for(int i =0;i<messageList.size();i++) {
                                    Message m = messageList.get(i);
                                    if (m.getId().equals(msg.msgID))
                                    {
                                        if((m.getStatus())>=Integer.parseInt("2"))
                                            break;
                                        m.setStatus(2);
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }
                            else{

                            }
                        }
                    }
            );
        }
    }
    private void SendAcks(List<Message> unread_msg){
        for (Message m: unread_msg) {
            m.setStatus(4);
            MessageModel msg = new MessageModel();
            msg.sender=mAuth.getUid();
            msg.type="ack";
            msg.msg="4";
            msg.time=m.getTimeInt();
            msg.msgID=m.getId();
            DatabaseReference ref =  db.getReference("messages/"+id).push();
            ref.setValue(msg).addOnCompleteListener(
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                MessageDB.Update(msg.msgID, "4");
                                for(int i =0;i<messageList.size();i++) {
                                    Message m = messageList.get(i);
                                    if (m.getId().equals(msg.msgID))
                                    {
                                        if((m.getStatus())>=Integer.parseInt("4"))
                                            break;
                                        m.setStatus(4);
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }
                            else{

                            }
                        }
                    }
            );
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(recRev!=null && listener!=null)
            recRev.removeEventListener(listener);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendMessage(View view) {
        String message = msgBox.getText().toString();
        if(message.equals(""))
            return;
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
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d,yyyy", Locale.ENGLISH);
        SimpleDateFormat tdf = new SimpleDateFormat(" h:mm,a", Locale.ENGLISH);
        String timeVal = tdf.format(date);
        String dateVal = sdf.format(date);
        Message m=new Message(msg.msgID, msg.sender, id, msg.msg, 1, timeVal, dateVal, null, null, null, null, msg.time);
        DatabaseReference ref =  db.getReference("messages/"+id).push();
        String push_id = ref.getKey();
        System.out.println(push_id);
        m.setPushId(push_id);
        MessageDB.Add(m);
        messageList.add(m);
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(messageList.size()-1);
        msgBox.setText("");
        ref.setValue(msg).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            MessageDB.Update(msg.msgID, "2");
                            for(int i =0;i<messageList.size();i++) {
                                Message m = messageList.get(i);
                                if (m.getId().equals(msg.msgID))
                                {
                                    if((m.getStatus())>=Integer.parseInt("2"))
                                        break;
                                    m.setStatus(2);
                                }
                            }
                            adapter.notifyDataSetChanged();
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
            adapter = new MessagesAdapter(messageList, this);
            recyclerView.setAdapter(adapter);
            recyclerView.scrollToPosition(messageList.size()-1);
    }

    public void GoBack(View view) {
        finish();
    }
}