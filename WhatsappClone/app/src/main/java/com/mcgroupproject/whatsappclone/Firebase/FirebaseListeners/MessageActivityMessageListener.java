package com.mcgroupproject.whatsappclone.Firebase.FirebaseListeners;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.mcgroupproject.whatsappclone.Activity.MessageActivity;
import com.mcgroupproject.whatsappclone.Database.MessageDB;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseModels.FirebaseMessageModel;
import com.mcgroupproject.whatsappclone.Utilities.DatetimeUtility;
import java.time.Instant;

public class MessageActivityMessageListener  implements ChildEventListener {
    MessageActivity activity;

    public MessageActivityMessageListener(MessageActivity activity) {
        this.activity = activity;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        FirebaseMessageModel msg = snapshot.getValue(FirebaseMessageModel.class);
        if(!msg.sender.equals(activity.id))
            return;
        if(msg.type==null)
            msg.type="";
        if(msg.type.equals("message")) {
            String timeVal = DatetimeUtility.GetTime(msg.time);
            String dateVal = DatetimeUtility.GetDate(msg.time);
            com.mcgroupproject.whatsappclone.Model.Message message=new com.mcgroupproject.whatsappclone.Model.Message(msg.msgID, msg.sender, activity.firebaseProfileActions.GetCurrentUser().getUid(), msg.msg, 2, timeVal, dateVal, null, null, null, null, msg.time);
            message.setPushId(snapshot.getKey());

            activity.messageList.add(message);
            activity.adapter.notifyDataSetChanged();
            activity.recyclerView.scrollToPosition(activity.messageList.size()-1);

            String sender = msg.sender;
            FirebaseMessageModel newMSG = new FirebaseMessageModel();
            newMSG.msg = "4";
            newMSG.type = "ack";
            newMSG.msgID = msg.msgID;
            newMSG.sender = activity.firebaseProfileActions.GetCurrentUser().getUid();
            newMSG.time = Instant.now().toEpochMilli();
            activity.firebaseMessageDBActions.SendMessage(newMSG, sender).addOnCompleteListener(
                    task -> {
                        if(task.isSuccessful()){
                            for(int i =0;i<activity.messageList.size();i++)
                            {
                                com.mcgroupproject.whatsappclone.Model.Message m = activity.messageList.get(i);
                                if(m.getId().equals(newMSG.msgID))
                                {
                                    if(m.getStatus() >= Integer.parseInt("4"))
                                        break;
                                    m.setStatus(Integer.parseInt("4"));
                                }
                            }
                            MessageDB.Update(msg.msgID, "4");
                            activity.adapter.notifyDataSetChanged();
                        }
                    }
            );
        }
        else {
            for(int i =0;i<activity.messageList.size();i++)
            {
                com.mcgroupproject.whatsappclone.Model.Message m = activity.messageList.get(i);
                if(m.getId().equals(msg.msgID))
                {
                    if(m.getStatus() >= Integer.parseInt(msg.msg))
                        break;
                    m.setStatus(Integer.parseInt(msg.msg));
                }
            }
            MessageDB.Update(msg.msgID, msg.msg);
        }
        activity.adapter.notifyDataSetChanged();
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
}