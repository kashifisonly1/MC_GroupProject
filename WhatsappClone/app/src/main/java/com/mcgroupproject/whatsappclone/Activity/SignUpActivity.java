package com.mcgroupproject.whatsappclone.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseActions.FirebaseProfileActions;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseListeners.PhoneAuthListener;
import com.mcgroupproject.whatsappclone.R;

public class SignUpActivity extends AppCompatActivity {

    EditText PhoneView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        PhoneView = findViewById(R.id.phoneField);
    }
    public void sendVerificationCode(View view){
        String phoneNumber = PhoneView.getText().toString();
        if(phoneNumber.equals("") || phoneNumber.equals(null)) {
            PhoneView.setError("Please enter phone number");
            return;
        }
        FirebaseProfileActions firebaseProfileActions = new FirebaseProfileActions(this);
        PhoneAuthListener phoneAuthListener = new PhoneAuthListener(this, PhoneView);
        firebaseProfileActions.FirebaseSendPhoneCode(phoneNumber, phoneAuthListener);
    }
}