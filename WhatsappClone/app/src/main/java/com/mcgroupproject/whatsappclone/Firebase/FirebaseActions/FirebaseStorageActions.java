package com.mcgroupproject.whatsappclone.Firebase.FirebaseActions;

import android.app.Activity;
import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FirebaseStorageActions {
    private final Activity activity;
    private final FirebaseStorage firebaseStorage;
    private final FirebaseAuth firebaseAuth;

    public FirebaseStorageActions(Activity activity) {
        this.activity = activity;
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public Task<Uri> DownloadProfileImage(String userID) {
        return firebaseStorage.getReference().child("profile/"+ userID).getDownloadUrl();
    }

    public UploadTask UploadProfileImage(byte[] bytes) {
        StorageReference ref = firebaseStorage.getReference().child("profile/"+firebaseAuth.getUid());
        return ref.putBytes(bytes);
    }
}
