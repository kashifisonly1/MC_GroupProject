package com.mcgroupproject.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

public class CodeVerificationPage extends AppCompatActivity {

    FirebaseAuth mAuth;
    String codeSent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_verification_page);
        mAuth = FirebaseAuth.getInstance();
        codeSent = getIntent().getStringExtra("verificationID");
    }
    public void verifySignInCode(View view){
        EditText codeField = findViewById(R.id.codeField);
        String code = codeField.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(getBaseContext(), ProfileCreationPage.class);
                            startActivity(intent);
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                EditText codeField = findViewById(R.id.codeField);
                                codeField.setError("Invalid code");
                            }
                        }
                    }
                });
    }

}