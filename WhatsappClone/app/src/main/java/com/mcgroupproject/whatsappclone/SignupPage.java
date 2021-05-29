package com.mcgroupproject.whatsappclone;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.Person;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import com.mcgroupproject.whatsappclone.model.NotificationSenderClass;

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
    @RequiresApi(api = Build.VERSION_CODES.O)//must be included where ever this function is typed
    public void showNotification(View view)//view added as a parameter to test it by clicking onclick on the next button of this page's activity
    {
        Long tsLong = System.currentTimeMillis()/1000;//current time stamp
        NotificationSenderClass ncs= new NotificationSenderClass(this);//context of the current page must be included in constructor
        Notification.MessagingStyle.Message[] messages = new Notification.MessagingStyle.Message[5];//5 messages array set as default can be canged but 3 will be shown in notification
        messages[0]= new Notification.MessagingStyle.Message("this is my message i wanted to share with you",tsLong,"Kashif");
        messages[1]= new Notification.MessagingStyle.Message("this is my message i wanted to share with you",tsLong,"Kashif");
        messages[2]= new Notification.MessagingStyle.Message("this is my message i wanted to share with you",tsLong,"Kashif");
        messages[3]= new Notification.MessagingStyle.Message("this is my message i wanted to share with you",tsLong,"Kashif");
        messages[4]= new Notification.MessagingStyle.Message("this is my message i wanted to share with you",tsLong,"Kashif");
        ncs.showNotification(1,"whatever","Ali",messages,1);//message channel must be different for different chats and notification ID must be unique everytime a new notification is created
        ncs.showNotification(2,"whatever","Baka",messages,2);
        ncs.showNotification(3,"whatever","aho",messages,3);//notifications more than 2 are not visible only 2 is limit while messages limit is 3
        //be careful messages sent as parameter in above notifications must be normal messages that were sent in app
        //messages array passed as parameter in below function is different right now messages array was passed just for test but when trying to use function
        //a new array of messages with just summary of the latest message and its sender must be included no need to every messages lates one is enough
        //well dont know if that is going to even work or not XD but still be careful while sending messages as a parameter in function below as it is different then the array given above
        ncs.showSummaryNotification(1,"whatever","Ali",10,messages,2);
        //message_numbers = number of messages not read by user yet
    }
/*  old code -- left here in case something happens to new one--
@RequiresApi(api = Build.VERSION_CODES.O)
    public void ShowNotification(View view) {
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            notificationManager.createNotificationChannel(new NotificationChannel("channel_01",
                    "Whatever", NotificationManager.IMPORTANCE_HIGH));
            // Create a notification and set the notification channel.

        }
        Intent intent = new Intent(this, activity_main_latest.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Long tsLong = System.currentTimeMillis()/1000;
        Notification.MessagingStyle.Message message1 =
                new Notification.MessagingStyle.Message("this is my message i wanted to share with you",tsLong,"Kashif");
        Notification.MessagingStyle.Message message2 =
                new Notification.MessagingStyle.Message("This is the second message i wanted to share with you",tsLong,"Atif");
        Notification.MessagingStyle.Message message3 =
                new Notification.MessagingStyle.Message("this is third message i wanted to share with you",tsLong,"Usama");
        Notification.MessagingStyle.Message message4 =
                new Notification.MessagingStyle.Message("This is the fourth message i wanted to share with you",tsLong,"Gull Sher");
        android.app.Notification notification1 = new android.app.Notification.Builder(SignupPage.this)
                .setSmallIcon(R.drawable.ic_logo_p).setColor(Color.argb(1,206,0,219))
                .setStyle(new Notification.MessagingStyle("asdasd")
                        .addMessage(message1)
                .addMessage(message2))
                .setPriority(Notification.PRIORITY_HIGH)
                .setGroup("group_01")
                .setContentIntent(pendingIntent)
                .build();
        android.app.Notification notification2 = new android.app.Notification.Builder(SignupPage.this)
                .setSmallIcon(R.drawable.ic_logo_p).setColor(Color.argb(1,206,0,219))
                .setStyle(new Notification.MessagingStyle("asdasd")
                        .addMessage(message3)
                        .addMessage(message4))
                .setPriority(Notification.PRIORITY_HIGH)
                .setGroup("group_01")
                .setContentIntent(pendingIntent)
                .build();

        Notification summaryNotification =
                new Notification.Builder(SignupPage.this)
                        .setContentTitle("ContentTiitle")
                        //set content text to support devices running API level < 24
                        .setContentText("four new messages")
                        .setSmallIcon(R.drawable.ic_logo_p)
                        //build summary info into InboxStyle template
                        .setStyle(new Notification.InboxStyle()
                                .addLine(message1.getText()+""+message1.getSender())
                                .addLine(message2.getText()+""+message2.getSender())
                                .setSummaryText("4 new messages"))
                        //specify which group this notification belongs to
                        .setGroup("group_01")
                        //set this notification as the summary for the group
                        .setGroupSummary(true)
                        .setColor(Color.argb(1,206,0,219))
                        .setContentIntent(pendingIntent)
                        .build();

        notificationManager.notify(1, notification1);
        notificationManager.notify(2, notification2);

        notificationManager.notify(3, summaryNotification);
    }
*/
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