package com.mcgroupproject.whatsappclone.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mcgroupproject.whatsappclone.Firebase.FirebaseActions.FirebaseProfileActions;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseActions.FirebaseStorageActions;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseActions.FirebaseUserDBActions;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseListeners.ActionFailureListener;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseListeners.DownloadImageListener;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseListeners.UpdateProfileListener;
import com.mcgroupproject.whatsappclone.R;
import com.mcgroupproject.whatsappclone.Utilities.ImageUtility;

import java.io.IOException;

public class ProfileCreationActivity extends AppCompatActivity {

    EditText textView;
    ImageView img;
    FirebaseProfileActions firebaseProfileActions;
    FirebaseStorageActions firebaseStorageActions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creation);

        img = findViewById(R.id.creation_page_img);
        textView = findViewById(R.id.searchPhoneField);

        firebaseProfileActions = new FirebaseProfileActions(this);
        firebaseStorageActions = new FirebaseStorageActions(this);

        firebaseStorageActions.DownloadProfileImage(firebaseProfileActions.GetCurrentUser().getUid())
                .addOnSuccessListener(new DownloadImageListener(this, img))
                .addOnFailureListener(new ActionFailureListener(this));
        textView.setText(firebaseProfileActions.GetCurrentUser().getDisplayName());
    }

    public void setName(View view) {
        String name = textView.getText().toString();
        if(name.equals("")) {
            textView.setError("Please Enter Name");
            return;
        }
        byte[] imgBytes = ImageUtility.ImageToBytes(img);
        if(imgBytes==null)
            return;
        firebaseProfileActions.UpdateProfile(name)
            .addOnCompleteListener(new UpdateProfileListener<>(this, imgBytes))
            .addOnFailureListener(new ActionFailureListener(this));
    }

    public void upload_file(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100&&resultCode==RESULT_OK&&data!=null)
        {
            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                img.setImageBitmap(bitmap);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
}