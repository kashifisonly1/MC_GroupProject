package com.mcgroupproject.whatsappclone.Firebase.FirebaseListeners;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.mcgroupproject.whatsappclone.Activity.MessageActivity;
import com.mcgroupproject.whatsappclone.Database.UserDB;
import com.mcgroupproject.whatsappclone.Model.User;

import java.util.List;
import java.util.Map;

public class SearchUserListener  implements OnCompleteListener<DataSnapshot> {
    Activity activity;
    List<User> users;
    public SearchUserListener(Activity activity, List<User> users) {
        this.activity=activity;
        this.users = users;
    }
    @Override
    public void onComplete(@NonNull Task<DataSnapshot> task) {
        if (!task.isSuccessful()) {
            Toast.makeText(activity.getApplicationContext(), "Connection Error", Toast.LENGTH_LONG).show();
        }
        else {
            Map<String, Map<String, String>> map = (Map<String, Map<String, String>>)task.getResult().getValue();
            if(map==null)
            {
                Toast.makeText(activity.getApplicationContext(), "Please enter valid phone", Toast.LENGTH_LONG).show();
                return;
            }
            String key = map.keySet().iterator().next();
            Map<String, String> user = map.get(key);
            if(user.get("UID").equals(FirebaseAuth.getInstance().getUid())){
                Toast.makeText(activity.getApplicationContext(), "Please enter valid phone", Toast.LENGTH_LONG).show();
                return;
            }
            User new_user = new User(user.get("UID"), user.get("Phone"), user.get("Name"), null, null, "https://scontent.fmux2-1.fna.fbcdn.net/v/t1.6435-1/c0.0.200.200a/p200x200/134265626_3018484121712082_1854137723180140704_n.jpg?_nc_cat=110&ccb=1-3&_nc_sid=7206a8&_nc_eui2=AeEN0rDDxRNgw6qqXm5RpHGCKCE4MRpc5YsoITgxGlzli4SUpEtDifDI8dTlWKvP7SV9E2Q6pJKs5udYWHM7Z12W&_nc_ohc=mHsfjuJJuVIAX9EKier&_nc_ht=scontent.fmux2-1.fna&tp=27&oh=c8447be514c7d9eef41f96e41db71215&oe=60D65660");
            if(UserDB.Add(new_user))
                users.add(new_user);
            Intent intent = new Intent(activity, MessageActivity.class);
            intent.putExtra("phone", user.get("Phone"));
            intent.putExtra("uid", user.get("UID"));
            intent.putExtra("name", user.get("Name"));
            intent.putExtra("status", user.get("Status"));
            activity.startActivity(intent);
        }
    }
}