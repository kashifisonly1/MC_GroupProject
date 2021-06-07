package com.mcgroupproject.whatsappclone.Activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseActions.FirebaseMessageDBActions;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseActions.FirebaseProfileActions;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseActions.FirebaseStorageActions;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseActions.FirebaseUserDBActions;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseListeners.MessageActivityMessageListener;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseListeners.UserStatusListener;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseModels.FirebaseMessageModel;
import com.mcgroupproject.whatsappclone.Adapter.MessagesAdapter;
import com.mcgroupproject.whatsappclone.Database.MessageDB;
import com.mcgroupproject.whatsappclone.R;
import com.mcgroupproject.whatsappclone.Utilities.DatetimeUtility;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends Activity {

    public String phone;
    public String id;
    public TextView online_statusView;
    public TextView nameView;
    public EditText msgBox;
    public ImageView img;
    public ChildEventListener listener;
    public DatabaseReference recRev;
    public DatabaseReference userRef;
    public ValueEventListener userListener;
    public RecyclerView recyclerView;
    public MessagesAdapter adapter;
    public List<com.mcgroupproject.whatsappclone.Model.Message> messageList =  new ArrayList<>();

    public FirebaseMessageDBActions firebaseMessageDBActions;
    public FirebaseProfileActions firebaseProfileActions;
    public FirebaseStorageActions firebaseStorageActions;
    public FirebaseUserDBActions firebaseUserDBActions;

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_message);

            MessageDB.Init(getApplicationContext());

            firebaseMessageDBActions = new FirebaseMessageDBActions(this);
            firebaseProfileActions = new FirebaseProfileActions(this);
            firebaseStorageActions = new FirebaseStorageActions(this);
            firebaseUserDBActions = new FirebaseUserDBActions(this);

            img = findViewById(R.id.profile_image_link);
            phone = getIntent().getStringExtra("phone");
            id = getIntent().getStringExtra("uid");
            nameView = findViewById(R.id.chat_name);
            online_statusView = findViewById(R.id.status);

            String s = getIntent().getStringExtra("status");
            if(s.equals("online") || s.equals("Online") || s.equals("loading..."))
            {
                online_statusView.setText(s);
            }
            else{
                String timeVal = DatetimeUtility.GetTime(Long.parseLong(s));
                String dateVal = DatetimeUtility.GetDate(Long.parseLong(s));
                online_statusView.setText(dateVal + "  "+timeVal);
            }

            nameView.setText(getIntent().getStringExtra("name"));
            msgBox = findViewById(R.id.message_box_area);

            userRef = firebaseUserDBActions.UserStatusListening(id);
            userListener = new UserStatusListener(this);
            userRef.addValueEventListener(userListener);

            recyclerView = (RecyclerView) findViewById(R.id.messages_recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            getMessagesList();

            listener = new MessageActivityMessageListener(this);
            recRev = firebaseMessageDBActions.MessagesListener(firebaseProfileActions.GetCurrentUser().getUid());
            String last_push_id = MessageDB.GetLastPushID(firebaseProfileActions.GetCurrentUser().getUid());
            recRev.orderByKey().startAfter(last_push_id).addChildEventListener(listener);

            List<com.mcgroupproject.whatsappclone.Model.Message> unread_msg = MessageDB.unreadMsg(id);
            SendSeenAcknowledge(unread_msg);
    }

    private void SendSeenAcknowledge(List<com.mcgroupproject.whatsappclone.Model.Message> unread_msg){
        for (com.mcgroupproject.whatsappclone.Model.Message m: unread_msg) {
            m.setStatus(4);
            FirebaseMessageModel msg = new FirebaseMessageModel();
            msg.sender=firebaseProfileActions.GetCurrentUser().getUid();
            msg.type="ack";
            msg.msg="4";
            msg.time=m.getTimeInt();
            msg.msgID=m.getId();
            DatabaseReference ref =  firebaseMessageDBActions.AddNewMessage(id);
            ref.setValue(msg).addOnCompleteListener(
                    task -> {
                        if(task.isSuccessful()){
                            MessageDB.Update(msg.msgID, "4");
                            for(int i =0;i<messageList.size();i++) {
                                com.mcgroupproject.whatsappclone.Model.Message m1 = messageList.get(i);
                                if (m1.getId().equals(msg.msgID))
                                {
                                    if((m1.getStatus())>=Integer.parseInt("4"))
                                        break;
                                    m1.setStatus(4);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                        else{

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
        if(userRef!=null && userListener!=null)
            userRef.removeEventListener(userListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendMessage(View view) {
        String message = msgBox.getText().toString().trim();
        if(message.equals(""))
            return;
        String sender = firebaseProfileActions.GetCurrentUser().getUid();
        String msgID = (Instant.now().toEpochMilli())+firebaseProfileActions.GetCurrentUser().getUid();
        String type = "message";
        FirebaseMessageModel msg = new FirebaseMessageModel();
        msg.msg = message;
        msg.msgID = msgID;
        msg.sender = sender;
        msg.time = Instant.now().toEpochMilli();
        msg.type = type;
        String timeVal = DatetimeUtility.GetTime(msg.time);
        String dateVal = DatetimeUtility.GetDate(msg.time);
        com.mcgroupproject.whatsappclone.Model.Message m=new com.mcgroupproject.whatsappclone.Model.Message(msg.msgID, msg.sender, id, msg.msg, 1, timeVal, dateVal, null, null, null, null, msg.time);
        DatabaseReference ref =  firebaseMessageDBActions.AddNewMessage(id);
        String push_id = ref.getKey();
        m.setPushId(push_id);
        if(MessageDB.Add(m));
            messageList.add(m);
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(messageList.size()-1);
        msgBox.setText("");
        ref.setValue(msg).addOnCompleteListener(
                task -> {
                    if(task.isSuccessful()){
                        MessageDB.Update(msg.msgID, "2");
                        for(int i =0;i<messageList.size();i++) {
                            com.mcgroupproject.whatsappclone.Model.Message m1 = messageList.get(i);
                            if (m1.getId().equals(msg.msgID))
                            {
                                if((m1.getStatus())>=Integer.parseInt("2"))
                                    break;
                                m1.setStatus(2);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
        );
    }

    private void getMessagesList() {
            List<com.mcgroupproject.whatsappclone.Model.Message> l = MessageDB.Get(id);
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