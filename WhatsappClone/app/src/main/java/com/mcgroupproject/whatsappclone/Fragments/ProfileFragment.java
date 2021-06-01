package com.mcgroupproject.whatsappclone.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mcgroupproject.whatsappclone.Firebase;
import com.mcgroupproject.whatsappclone.MainActivity;
import com.mcgroupproject.whatsappclone.R;
import com.mcgroupproject.whatsappclone.User;
import com.mcgroupproject.whatsappclone.chatList;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ProfileFragment extends Fragment {

    FirebaseAuth mAuth;
    EditText textView;
    FirebaseStorage storage;
    ImageView img;
    FirebaseDatabase db;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mAuth = Firebase.auth;
        textView = view.findViewById(R.id.nameField);
        db = Firebase.db;
        storage = Firebase.storage;
        img = view.findViewById(R.id.profile_img);
        textView.setText(mAuth.getCurrentUser().getDisplayName());
        storage.getReference().child("profile/"+ mAuth.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getContext()).load(uri.toString()).into(img);
            }
        });
        Button button = (Button)view.findViewById(R.id.profile_btn_create);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setName(v);
            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 100);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100&&resultCode==getActivity().RESULT_OK&&data!=null)
        {
            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                ((ImageView)img).setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
                    Toast.makeText(getContext(), "Could not change name", Toast.LENGTH_LONG).show();
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
                Toast.makeText(getContext(), "Could not upload file", Toast.LENGTH_LONG).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                Toast.makeText(getContext(), "file uploaded", Toast.LENGTH_LONG).show();
                db.getReference().child("users").child(user.UID)
                .setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), "Could not update data", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

}