package com.mcgroupproject.whatsappclone;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.collect.MapMaker;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.mcgroupproject.whatsappclone.Fragments.SearchFragment;
import com.mcgroupproject.whatsappclone.database.*;

import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mcgroupproject.whatsappclone.Fragments.MainFragment;
import com.mcgroupproject.whatsappclone.Fragments.ProfileFragment;
import com.mcgroupproject.whatsappclone.model.User;
import com.mcgroupproject.whatsappclone.model.Message;
import com.mcgroupproject.whatsappclone.model.UserComparison;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    FirebaseDatabase db;
    FirebaseAuth mAuth;
    ChildEventListener listener;
    DatabaseReference recRev;
    FirebaseStorage storage;
    private String current_frame = "";
    private MainFragment chatFragment;
    private List<User> usersList;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UserDB.Init(getApplicationContext());
        MessageDB.Init(getApplicationContext());
        if(Firebase.auth==null)
            Firebase.init();
        mAuth = Firebase.auth;
        db = Firebase.db;
        storage = Firebase.storage;
        try{
            db.setPersistenceEnabled(true);
        }
        catch (Exception e){

        }
        if(mAuth.getCurrentUser()==null || (mAuth.getUid().equals(null)))
        {
            Intent intent = new Intent(getBaseContext(), SignupPage.class);
            startActivity(intent);
            finish();
            return;
        }
        usersList = UserDB.Get();
        usersList.sort(new UserComparison());
        chatFragment = new MainFragment(usersList);
        db.getReference().child("users").child(mAuth.getUid()).child("Status").setValue("online").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
        long time = Instant.now().toEpochMilli();
        db.getReference().child("users").child(mAuth.getUid()).child("Status").onDisconnect().setValue(Long.toString(time));
        recRev = db.getReference("messages/"+mAuth.getUid());
        recRev.keepSynced(true);
        listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MessageModel msg = snapshot.getValue(MessageModel.class);
                if(msg.type==null)
                    msg.type="";
                if(msg.type.equals("message"))
                {
                    Date date = new Date(msg.time);
                    SimpleDateFormat sdf = new SimpleDateFormat("MMMM d,yyyy", Locale.ENGLISH);
                    SimpleDateFormat tdf = new SimpleDateFormat(" h:mm,a", Locale.ENGLISH);
                    String timeVal = tdf.format(date);
                    String dateVal = sdf.format(date);
                    Message m=new Message(msg.msgID, msg.sender, mAuth.getUid(), msg.msg, 2, timeVal, dateVal, null, null, null, null, msg.time);
                    m.setPushId(snapshot.getKey());
                    boolean userID = UserDB.DoesUserExists(msg.sender);
                    if(!userID)
                    {
                        db.getReference("users/"+msg.sender).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if(task.isSuccessful())
                                {
                                    com.mcgroupproject.whatsappclone.User user = task.getResult().getValue(com.mcgroupproject.whatsappclone.User.class);
                                    com.mcgroupproject.whatsappclone.model.User userData = new User(user.UID, user.Phone, user.Name, m.getText(), Long.toString(msg.time), "https://scontent.fmux2-1.fna.fbcdn.net/v/t1.6435-1/c0.0.200.200a/p200x200/134265626_3018484121712082_1854137723180140704_n.jpg?_nc_cat=110&ccb=1-3&_nc_sid=7206a8&_nc_eui2=AeEN0rDDxRNgw6qqXm5RpHGCKCE4MRpc5YsoITgxGlzli4SUpEtDifDI8dTlWKvP7SV9E2Q6pJKs5udYWHM7Z12W&_nc_ohc=mHsfjuJJuVIAX9EKier&_nc_ht=scontent.fmux2-1.fna&tp=27&oh=c8447be514c7d9eef41f96e41db71215&oe=60D65660");
                                    if(UserDB.Add(userData))
                                    {    usersList.add(userData);}
                                    if(current_frame.equals("chatlist"))
                                        chatFragment.notifyChange();
                                    MessageDB.Add(m);
                                }
                            }
                        });
                    }
                    else{
                        for(int i =0; i<usersList.size(); i++) {
                            User user = usersList.get(i);
                            System.out.println(Long.parseLong(user.getDate()));
                            if (user.getUserID().equals(msg.sender)) {
                                if (Long.parseLong(user.getDate()) < msg.time) {
                                    user.setLastMessage(msg.msg);
                                    user.setDate(Long.toString(msg.time));
                                }
                                if (current_frame.equals( "chatlist"))
                                    chatFragment.notifyChange();
                                break;
                            }
                        }
                        MessageDB.Add(m);
                    }
                    String sender = msg.sender;
                    MessageModel newMSG = new MessageModel();
                    newMSG.msg = "3";
                    newMSG.type = "ack";
                    newMSG.msgID = msg.msgID;
                    newMSG.sender = mAuth.getUid();
                    newMSG.time = 912345;
                    sendAcknowledge(newMSG, sender);
                }
                else{
                    MessageDB.Update(msg.msgID, msg.msg);
                }
                usersList.sort(new UserComparison());
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
        if(!last_push_id.equals("")){
        db.getReference("/messages/"+mAuth.getUid()).orderByKey().endBefore(last_push_id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                snapshot.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) { }
                });
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
        });}
        recRev.orderByKey().startAfter(last_push_id).addChildEventListener(listener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(current_frame.equals("")) {
            current_frame = "chatlist";
            MainFragment fragment1 = chatFragment;
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

    public void sendAcknowledge(final MessageModel msg, String ID) {
        db.getReference("messages/"+ID).push().setValue(msg).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            MessageDB.Update(msg.msgID, msg.msg);
                        }
                        else{
                        }
                    }
                }
        );
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
        mAuth.signOut();
        UserDB.reset_db();
        MessageDB.reset_db();
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void chatlistFunction(MenuItem item) {
        if(current_frame.equals("chatlist"))
            return;
        current_frame = "chatlist";
        MainFragment fragment1 = chatFragment;
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