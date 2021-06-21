package com.mcgroupproject.whatsappclone.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.mcgroupproject.whatsappclone.Firebase.FirebaseActions.FirebaseMessageDBActions;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseActions.FirebaseProfileActions;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseActions.FirebaseUserDBActions;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseListeners.MainActivityMessageListener;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseModels.FirebaseMessageModel;
import com.mcgroupproject.whatsappclone.Fragments.SearchFragment;
import com.mcgroupproject.whatsappclone.Database.*;

import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.mcgroupproject.whatsappclone.Fragments.ChatFragment;
import com.mcgroupproject.whatsappclone.Fragments.ProfileFragment;
import com.mcgroupproject.whatsappclone.Model.Message;
import com.mcgroupproject.whatsappclone.Model.User;
import com.mcgroupproject.whatsappclone.Utilities.UserComparison;
import com.mcgroupproject.whatsappclone.R;

import java.io.IOException;
import java.time.Instant;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    ChildEventListener listener;
    DatabaseReference recRev;
    FirebaseUserDBActions firebaseUserDBActions;
    FirebaseProfileActions firebaseProfileActions;
    FirebaseMessageDBActions firebaseMessageDBActions;
    public String current_frame = "";
    public ChatFragment chatFragment;
    public List<User> usersList;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseProfileActions = new FirebaseProfileActions(this);
        firebaseUserDBActions = new FirebaseUserDBActions(this);
        firebaseMessageDBActions = new FirebaseMessageDBActions(this);

        UserDB.Init(getApplicationContext());
        MessageDB.Init(getApplicationContext());

        if(firebaseProfileActions.GetCurrentUser()==null || !firebaseProfileActions.isUserExists())
        {
            Intent intent = new Intent(getBaseContext(), SignUpActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        usersList = UserDB.Get();
        usersList.sort(new UserComparison());
        chatFragment = new ChatFragment(usersList);

        firebaseUserDBActions.SetUserOnline(firebaseProfileActions.GetCurrentUser().getUid());
        firebaseUserDBActions.SetDisconnection();
        recRev = firebaseMessageDBActions.MessagesListener(firebaseProfileActions.GetCurrentUser().getUid());

        listener = new MainActivityMessageListener(this);

        String last_push_id = MessageDB.GetLastPushID(firebaseProfileActions.GetCurrentUser().getUid());
        firebaseMessageDBActions.DeleteOlderMessage(last_push_id);
        recRev.orderByKey().startAfter(last_push_id).addChildEventListener(listener);

        List<Message> unseenMsg = MessageDB.unseenMsg();
        List<Message> undeliveredMsg = MessageDB.undeliveredMsg(firebaseProfileActions.GetCurrentUser().getUid());
        SendAgainMessage(unseenMsg);
        SendDeliveredAcknowledge(undeliveredMsg);;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void SendAgainMessage(List<com.mcgroupproject.whatsappclone.Model.Message> unseen_msg){
        for (com.mcgroupproject.whatsappclone.Model.Message m : unseen_msg) {
            FirebaseMessageModel msg = new FirebaseMessageModel();
            msg.sender=m.getSenderID();
            msg.type="message";
            msg.msg=m.getText();
            msg.time=Instant.now().toEpochMilli();
            msg.msgID=m.getId();
            DatabaseReference ref =  firebaseMessageDBActions.AddNewMessage(m.getReceiverID());
            String newKey = ref.getKey();
            m.setPushId(newKey);
            MessageDB.rm(m);
            MessageDB.Add(m);
            ref.setValue(msg).addOnCompleteListener(
                    task -> {
                        if(task.isSuccessful()){
                            MessageDB.Update(msg.msgID, "2");
                        }
                    }
            );
        }
    }

    private void SendDeliveredAcknowledge(List<com.mcgroupproject.whatsappclone.Model.Message> unread_msg){
        for (com.mcgroupproject.whatsappclone.Model.Message m: unread_msg) {
            FirebaseMessageModel msg = new FirebaseMessageModel();
            msg.sender=firebaseProfileActions.GetCurrentUser().getUid();
            msg.type="ack";
            msg.msg="3";
            msg.time=m.getTimeInt();
            msg.msgID=m.getId();
            DatabaseReference ref =  firebaseMessageDBActions.AddNewMessage(m.getSenderID());
            ref.setValue(msg).addOnCompleteListener(
                    task -> {
                        if(task.isSuccessful()){
                            MessageDB.Update(msg.msgID, "3");
                        }
                    }
            );
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(current_frame.equals("")) {
            current_frame = "chatlist";
            ChatFragment fragment1 = chatFragment;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_chatlist, fragment1);
            transaction.commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(recRev!=null&&listener!=null)
            recRev.removeEventListener(listener);
    }

    public void profileFunction(MenuItem item) {
        if(current_frame.equals("profile"))
            return;
        current_frame = "profile";
        ProfileFragment fragment1 = new ProfileFragment();
        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_chatlist, fragment1);
        transaction.commit();
    }
    public void logoutFunction(MenuItem item)
    {
        firebaseProfileActions.Signout();
        UserDB.reset_db();
        MessageDB.reset_db();
        Intent intent = new Intent(getBaseContext(), SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100&&resultCode==RESULT_OK&&data!=null)
        {
            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                ((ImageView)findViewById(R.id.profile_img)).setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onResume() {
        super.onResume();
        for(int i = 0; i<usersList.size(); i++){
            usersList.set(i, UserDB.GetUser(usersList.get(i)));
        }
        usersList.sort(new UserComparison());
        chatFragment.notifyChange();
        List<Message> unseenMsg = MessageDB.unseenMsg();
        List<Message> undeliveredMsg = MessageDB.undeliveredMsg(firebaseProfileActions.GetCurrentUser().getUid());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            SendAgainMessage(unseenMsg);
        }
        SendDeliveredAcknowledge(undeliveredMsg);;
    }

    public void chatlistFunction(MenuItem item) {
        if(current_frame.equals("chatlist"))
            return;
        current_frame = "chatlist";
        ChatFragment fragment1 = chatFragment;
        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_chatlist, fragment1);
        transaction.commit();
        chatFragment.notifyChange();
    }
    public void searchPersonFunction(MenuItem item){
        if(current_frame.equals("search"))
            return;
        current_frame = "search";
        SearchFragment frag = new SearchFragment(usersList);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_chatlist, frag);
        transaction.commit();
    }
}