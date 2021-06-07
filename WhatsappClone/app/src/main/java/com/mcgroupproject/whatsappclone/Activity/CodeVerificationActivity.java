package com.mcgroupproject.whatsappclone.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseActions.FirebaseProfileActions;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseListeners.ActionFailureListener;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseListeners.AuthSignInListener;
import com.mcgroupproject.whatsappclone.R;

public class CodeVerificationActivity extends AppCompatActivity {

    String codeSent;
    String phoneNumber;
    EditText codeField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_verification);
        codeSent = getIntent().getStringExtra("verificationID");
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        codeField = findViewById(R.id.codeField);
        ((TextView)findViewById(R.id.verification_phone)).setText(phoneNumber);
        ((TextView)findViewById(R.id.verification_confirm_phone)).setText(phoneNumber);
    }
    public void verifySignInCode(View view){
        String code = codeField.getText().toString();
        if(code.equals("")) {
            codeField.setError("Please Enter Code");
            return;
        }
        FirebaseProfileActions firebaseProfileActions = new FirebaseProfileActions(this);
        AuthSignInListener authSignInListener = new AuthSignInListener(this, codeField);
        firebaseProfileActions.SignInWithPhoneAuthCredentials(codeSent, code)
                .addOnCompleteListener(authSignInListener)
                .addOnFailureListener(new ActionFailureListener(this));
    }
    public void backToSignUpPage(View view) {
        finish();
    }
}