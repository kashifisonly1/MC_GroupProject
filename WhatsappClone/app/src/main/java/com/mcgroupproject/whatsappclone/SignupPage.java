package com.mcgroupproject.whatsappclone;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.Person;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import io.grpc.internal.SharedResourceHolder;

public class SignupPage extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        mAuth = Firebase.auth;

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void ShowNotification(View view) {
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            notificationManager.createNotificationChannel(new NotificationChannel("channel_01",
                    "Whatever", NotificationManager.IMPORTANCE_HIGH));
            // Create a notification and set the notification channel.

        }
        Long tsLong = System.currentTimeMillis()/1000;
        Notification.MessagingStyle.Message message1 =
                new Notification.MessagingStyle.Message("asdasd",tsLong,"asdasda");
        Notification.MessagingStyle.Message message2 =
                new Notification.MessagingStyle.Message("12ssasadd",tsLong,"asdasd");
        android.app.Notification notification = new android.app.Notification.Builder(SignupPage.this)
                .setSmallIcon(R.drawable.ic_logo_p).setColor(Color.argb(1,206,0,219))
                .setStyle(new Notification.MessagingStyle("asdasd")
                        .addMessage(message1)
                .addMessage(message2))
                .build();


        notificationManager.notify(1, notification);
    }

    public void sendVerificationCode(View view){
        EditText phone = findViewById(R.id.phoneField);
        String phoneNumber = phone.getText().toString();
        if(phoneNumber=="")
            phone.setError("Please enter phone number");
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }



    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            Intent intent = new Intent(getBaseContext(), ProfileCreationPage.class);
            startActivity(intent);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                Toast toast = Toast.makeText(getApplicationContext(), "INVALID PHONE NUMBER REQUEST", Toast.LENGTH_LONG);
                toast.show();
            } else if (e instanceof FirebaseTooManyRequestsException) {
                Toast toast = Toast.makeText(getApplicationContext(), "TOO MANY REQUESTS", Toast.LENGTH_LONG);
                toast.show();
            }
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            EditText phone = findViewById(R.id.phoneField);
            String phoneNumber = phone.getText().toString();
            Intent intent = new Intent(getBaseContext(), CodeVerificationPage.class);
            intent.putExtra("verificationID", verificationId);
            intent.putExtra("phoneNumber",phoneNumber);
            startActivity(intent);
        }

    };
}