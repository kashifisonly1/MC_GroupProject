package com.mcgroupproject.whatsappclone.Firebase.FirebaseActions;
import android.app.Activity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseListeners.PhoneAuthListener;

import java.util.concurrent.TimeUnit;

public class FirebaseProfileActions {
    private final long TIMEOUT;
    private final FirebaseAuth firebaseAuth;
    private final Activity activity;

    public FirebaseProfileActions(Activity activity) {
        firebaseAuth = FirebaseAuth.getInstance();
        this.activity = activity;
        TIMEOUT = 60;
    }

    public void FirebaseSendPhoneCode(String phoneNumber, PhoneAuthListener phoneAuthListener) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(TIMEOUT, TimeUnit.SECONDS)
                        .setActivity(activity)
                        .setCallbacks(phoneAuthListener)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public Task<AuthResult> SignInWithPhoneAuthCredentials(String codeSent, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        return firebaseAuth.signInWithCredential(credential);
    }

    public Task<Void> UpdateProfile(String name) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
        .setDisplayName(name)
        .build();
        return firebaseAuth.getCurrentUser().updateProfile(profileUpdates);
    }

    public FirebaseUser GetCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }
    public boolean isUserExists( ) { return !firebaseAuth.getUid().equals(null) && !firebaseAuth.getUid().equals(""); }
    public void Signout() { firebaseAuth.signOut(); }
}