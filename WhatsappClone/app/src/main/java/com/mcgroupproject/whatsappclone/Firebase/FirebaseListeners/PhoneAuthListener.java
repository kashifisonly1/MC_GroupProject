package com.mcgroupproject.whatsappclone.Firebase.FirebaseListeners;

import android.app.Activity;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mcgroupproject.whatsappclone.Activity.CodeVerificationActivity;
import com.mcgroupproject.whatsappclone.R;

public class PhoneAuthListener extends PhoneAuthProvider.OnVerificationStateChangedCallbacks {

    private final Activity activity;
    private final FirebaseAuth firebaseAuth;
    private final EditText phoneView;

    public PhoneAuthListener(Activity activity, EditText phoneView) {
        this.activity = activity;
        firebaseAuth = FirebaseAuth.getInstance();
        this.phoneView=phoneView;
    }

    @Override
    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
        firebaseAuth.signInWithCredential(phoneAuthCredential)
        .addOnCompleteListener(activity, new AuthSignInListener(activity, null));
    }

    @Override
    public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
        super.onCodeAutoRetrievalTimeOut(s);
        Toast toast = Toast.makeText(activity.getApplicationContext(), "CODE TIMEOUT", Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onCodeSent(@NonNull String verificationID, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
        super.onCodeSent(verificationID, forceResendingToken);
        EditText phone = activity.findViewById(R.id.phoneField);
        String phoneNumber = phone.getText().toString();
        Intent intent = new Intent(activity.getBaseContext(), CodeVerificationActivity.class);
        intent.putExtra("verificationID", verificationID);
        intent.putExtra("phoneNumber", phoneNumber);
        activity.startActivity(intent);
    }

    @Override
    public void onVerificationFailed(@NonNull FirebaseException e) {
        if (e instanceof FirebaseAuthInvalidCredentialsException) {
            phoneView.setError("Invalid Phone Number");
        } else if (e instanceof FirebaseTooManyRequestsException) {
            Toast toast = Toast.makeText(activity.getApplicationContext(), "TOO MANY REQUESTS", Toast.LENGTH_LONG);
            toast.show();
        }
        else if(e instanceof Exception){
            Toast toast = Toast.makeText(activity.getApplicationContext(), "Something Went Wrong", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
