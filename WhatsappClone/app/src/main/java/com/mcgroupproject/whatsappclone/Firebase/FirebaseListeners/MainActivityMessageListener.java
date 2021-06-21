package com.mcgroupproject.whatsappclone.Firebase.FirebaseListeners;

import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.mcgroupproject.whatsappclone.Activity.MainActivity;
import com.mcgroupproject.whatsappclone.Database.MessageDB;
import com.mcgroupproject.whatsappclone.Database.UserDB;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseActions.FirebaseMessageDBActions;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseActions.FirebaseProfileActions;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseActions.FirebaseUserDBActions;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseModels.FirebaseMessageModel;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseModels.FirebaseUserModel;
import com.mcgroupproject.whatsappclone.Model.User;
import com.mcgroupproject.whatsappclone.Utilities.DatetimeUtility;
import com.mcgroupproject.whatsappclone.Utilities.UserComparison;
import java.time.Instant;

public class MainActivityMessageListener implements ChildEventListener {
    MainActivity activity;
    FirebaseUserDBActions firebaseUserDBActions;
    FirebaseProfileActions profileActions;
    FirebaseMessageDBActions firebaseMessageDBActions;

    public MainActivityMessageListener(MainActivity activity) {
        this.activity = activity;
        firebaseUserDBActions = new FirebaseUserDBActions(activity);
        profileActions = new FirebaseProfileActions(activity);
        firebaseMessageDBActions = new FirebaseMessageDBActions(activity);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
         FirebaseMessageModel msg = snapshot.getValue(FirebaseMessageModel.class);
        if(msg.type==null)
            msg.type="";
        if(msg.type.equals("message"))
        {
            String dateVal = DatetimeUtility.GetDate(msg.time);
            String timeVal = DatetimeUtility.GetTime(msg.time);
            com.mcgroupproject.whatsappclone.Model.Message m=new com.mcgroupproject.whatsappclone.Model.Message(msg.msgID, msg.sender, profileActions.GetCurrentUser().getUid(), msg.msg, 2, timeVal, dateVal, null, null, null, null, msg.time);
            m.setPushId(snapshot.getKey());
            boolean userID = UserDB.DoesUserExists(msg.sender);
            if(!userID)
                AddUserInLocalDB(msg);
            else
                UpdateUserLastMessage(msg);
            MessageDB.Add(m);
            String sender = msg.sender;
            FirebaseMessageModel newMSG = new FirebaseMessageModel();
            newMSG.msg = "3";
            newMSG.type = "ack";
            newMSG.msgID = msg.msgID;
            newMSG.sender = profileActions.GetCurrentUser().getUid();
            newMSG.time = Instant.now().toEpochMilli();
            firebaseMessageDBActions.SendMessage(newMSG, sender).addOnCompleteListener(
                task -> {
                    if(task.isSuccessful()){
                        MessageDB.Update(newMSG.msgID, "3");
                    }
                }
            );
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void AddUserInLocalDB(FirebaseMessageModel msg) {
        firebaseUserDBActions.SearchUserByID(msg.sender)
        .addOnCompleteListener((OnCompleteListener<DataSnapshot>) task -> {
            if(task.isSuccessful())
            {
                FirebaseUserModel user = task.getResult().getValue(FirebaseUserModel.class);
                User userData = new User(user.UID, user.Phone, user.Name, msg.msg, Long.toString(msg.time), "https://scontent.fmux2-1.fna.fbcdn.net/v/t1.6435-1/c0.0.200.200a/p200x200/134265626_3018484121712082_1854137723180140704_n.jpg?_nc_cat=110&ccb=1-3&_nc_sid=7206a8&_nc_eui2=AeEN0rDDxRNgw6qqXm5RpHGCKCE4MRpc5YsoITgxGlzli4SUpEtDifDI8dTlWKvP7SV9E2Q6pJKs5udYWHM7Z12W&_nc_ohc=mHsfjuJJuVIAX9EKier&_nc_ht=scontent.fmux2-1.fna&tp=27&oh=c8447be514c7d9eef41f96e41db71215&oe=60D65660");
                if(UserDB.Add(userData)) {
                    activity.usersList.add(userData);
                    activity.usersList.sort(new UserComparison());
                    activity.chatFragment.notifyChange();
                }
                if(activity.current_frame.equals("chatlist")) activity.chatFragment.notifyChange();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void UpdateUserLastMessage(FirebaseMessageModel msg) {
        for(int i =0; i<activity.usersList.size(); i++) {
            User user = activity.usersList.get(i);
            if (user.getUserID().equals(msg.sender)) {
                if (Long.parseLong(user.getDate()) < msg.time) {
                    user.setLastMessage(msg.msg);
                    user.setDate(Long.toString(msg.time));
                }
                if (activity.current_frame.equals( "chatlist"))
                {
                    activity.usersList.sort(new UserComparison());
                    activity.chatFragment.notifyChange();
                }
                break;
            }
        }
    }
}
