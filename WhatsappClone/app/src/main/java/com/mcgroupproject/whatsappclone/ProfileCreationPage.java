package com.mcgroupproject.whatsappclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProfileCreationPage extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText textView;
    ImageView img;
    FirebaseDatabase db;
    FirebaseStorage storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creation_page);
        mAuth = Firebase.auth;

        storage=Firebase.storage;
        img = findViewById(R.id.creation_page_img);
        textView = findViewById(R.id.nameField);
        db = Firebase.db;
        storage.getReference().child("profile/"+ mAuth.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri.toString()).into(img);
            }
        });

        storage = Firebase.storage;
        textView.setText(mAuth.getCurrentUser().getDisplayName());
    }

    public void setName(View view) {
        String name = textView.getText().toString();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        mAuth.getCurrentUser().updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    User user = new User();
                    user.UID= mAuth.getUid();
                    user.Name=mAuth.getCurrentUser().getDisplayName();
                    user.Phone=mAuth.getCurrentUser().getPhoneNumber();
                    user.Status="online";
                    updateDatabase(user);
                }else {
                    Toast.makeText(getApplicationContext(), "Could not change name", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void updateDatabase(User user)
    {
        img.setDrawingCacheEnabled(true);
        img.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable)img.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        StorageReference ref = storage.getReference().child("profile/"+mAuth.getUid());
        ref.putBytes(bytes).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Could not upload file", Toast.LENGTH_LONG).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                Toast.makeText(getApplicationContext(), "file uploaded", Toast.LENGTH_LONG).show();
                db.getReference().child("users").child(user.UID)
                        .setValue(user)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Could not update data", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
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
                e.printStackTrace();
            }
        }
    }
}