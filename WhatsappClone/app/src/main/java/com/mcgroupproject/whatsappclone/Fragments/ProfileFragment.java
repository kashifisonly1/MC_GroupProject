package com.mcgroupproject.whatsappclone.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseActions.FirebaseProfileActions;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseActions.FirebaseStorageActions;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseListeners.ActionFailureListener;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseListeners.DownloadImageListener;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseListeners.UpdateProfileListener;
import com.mcgroupproject.whatsappclone.R;
import com.mcgroupproject.whatsappclone.Utilities.ImageUtility;
import java.io.IOException;

public class ProfileFragment extends Fragment {

    EditText textView;
    FirebaseProfileActions firebaseProfileActions;
    FirebaseStorageActions firebaseStorageActions;
    Context AppContext;
    ImageView img;
    Activity activity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = getActivity();
        firebaseProfileActions = new FirebaseProfileActions(activity);
        firebaseStorageActions = new FirebaseStorageActions(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        textView = view.findViewById(R.id.searchPhoneField);
        AppContext = getActivity().getApplicationContext();
        img = view.findViewById(R.id.profile_img);
        textView.setText(firebaseProfileActions.GetCurrentUser().getDisplayName());
        firebaseStorageActions.DownloadProfileImage(firebaseProfileActions.GetCurrentUser().getUid())
                .addOnSuccessListener(new DownloadImageListener(activity, img))
                .addOnFailureListener(new ActionFailureListener(activity));
        Button button = (Button)view.findViewById(R.id.profile_btn_create);
        button.setOnClickListener(this::setName);
        img.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 100);
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(getActivity()!=null&&requestCode==100&&resultCode== Activity.RESULT_OK &&data!=null)
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
        if(name.equals("")) {
            textView.setError("Please Enter Name");
            return;
        }
        byte[] imgBytes = ImageUtility.ImageToBytes(img);
        if(imgBytes==null) return;
        firebaseProfileActions.UpdateProfile(name)
                .addOnCompleteListener(new UpdateProfileListener<>(activity, imgBytes))
                .addOnFailureListener(new ActionFailureListener(activity));
    }
}