package com.mcgroupproject.whatsappclone.Firebase.FirebaseListeners;

import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;

public class DownloadImageListener implements OnSuccessListener<Uri> {
    Activity activity;
    ImageView img;
    public DownloadImageListener(Activity activity, ImageView img) {
        this.activity = activity;
        this.img = img;
    }

    @Override
    public void onSuccess(Uri uri) {
        try {
            Glide.with(activity.getApplicationContext()).load(uri.toString()).into(img);
        }
        catch(Exception e){
            Toast.makeText(activity.getApplicationContext(), "SOMETHING WENT WRONG", Toast.LENGTH_SHORT).show();
        }
    }
}
