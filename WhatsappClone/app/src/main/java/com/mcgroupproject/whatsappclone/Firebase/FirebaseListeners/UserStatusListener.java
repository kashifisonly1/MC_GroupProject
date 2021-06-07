package com.mcgroupproject.whatsappclone.Firebase.FirebaseListeners;

import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.mcgroupproject.whatsappclone.Activity.MessageActivity;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseModels.FirebaseUserModel;
import com.mcgroupproject.whatsappclone.Utilities.DatetimeUtility;

public class UserStatusListener implements ValueEventListener {
    MessageActivity activity;

    public UserStatusListener(MessageActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        FirebaseUserModel user= snapshot.getValue(FirebaseUserModel.class);
        if(user==null)
            return;
        activity.nameView.setText(user.Name);
        activity.online_statusView.setText(user.Status);
        if(user.Status.equals("online") || user.Status.equals("Online") || user.Status.equals("loading..."))
        {
            activity.online_statusView.setText(user.Status);
        }
        else{
            String timeVal = DatetimeUtility.GetTime(Long.parseLong(user.Status));
            String dateVal = DatetimeUtility.GetDate(Long.parseLong(user.Status));
            activity.online_statusView.setText(dateVal + "  "+timeVal);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}
