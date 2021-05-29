package com.mcgroupproject.whatsappclone.model;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.mcgroupproject.whatsappclone.R;
import com.mcgroupproject.whatsappclone.SignupPage;
import com.mcgroupproject.whatsappclone.activity_main_latest;

public class NotificationSenderClass {
    Context context; // must send context of the activity page so that notification can run
    public NotificationSenderClass(Context cntxt)
    {
        context=cntxt;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showNotification(int channel_ID, String channel_Desc,String messageSender,Notification.MessagingStyle.Message[] messages , int notification_ID)
    {
        //channel_ID will be different for different chats but group_ID(for notification) will remain same, meaning
        //if user is chatting with Ali then channel will be for Ali If Hamza then a new channel for Hamza
        // same for group which means number of groups=number of channels
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) //check for API >=25 || android version 8.0+
        {
            notificationManager.createNotificationChannel(new NotificationChannel("channel_"+channel_ID,
                    channel_Desc, NotificationManager.IMPORTANCE_HIGH));
        }
        //intent so that when user taps notification he is directed to chat page
        Intent intent = new Intent(context, activity_main_latest.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        Notification.MessagingStyle mStyle=new Notification.MessagingStyle(messageSender);
        for (Notification.MessagingStyle.Message message: messages
        ) {
            mStyle.addMessage(message);
        }
        android.app.Notification notification = new android.app.Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_logo_p).setColor(Color.argb(1,206,0,219))
                .setStyle(mStyle)
                .setPriority(Notification.PRIORITY_HIGH)
                .setGroup("group_01")
                .setContentIntent(pendingIntent)
                .build();
        notificationManager.notify(notification_ID, notification);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showSummaryNotification(int channel_ID, String channel_Desc,String messageSender,int message_numbers,Notification.MessagingStyle.Message[] messages,int notification_ID)
    {
        //channel_ID will be different for different chats in case of summary the channel must be same but group_ID(for notification) will remain same, meaning
        //if user is chatting with Ali then channel will be for Ali If Hamza then a new channel for Hamza
        // same for group which means number of groups=number of channels
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) //check for API >=25 || android version 8.0+
        {
            notificationManager.createNotificationChannel(new NotificationChannel("channel_"+channel_ID,
                    channel_Desc, NotificationManager.IMPORTANCE_HIGH));
        }
        //intent so that when user taps notification he is directed to chat page
        Intent intent = new Intent(context, activity_main_latest.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        Long tsLong = System.currentTimeMillis()/1000;//time stamp for current time
        Notification.Builder summaryNotificationBuilder =
                new Notification.Builder(context)
                        .setContentTitle("App")
                        //set content text to support devices running API level < 24
                        .setContentText( message_numbers +" new messages")
                        .setSmallIcon(R.drawable.ic_logo_p)
                        //specify which group this notification belongs to
                        .setGroup("group_01")
                        //set this notification as the summary for the group
                        .setGroupSummary(true)
                        .setColor(Color.argb(1,206,0,219))
                        .setContentIntent(pendingIntent);
        Notification.InboxStyle iStyle=new Notification.InboxStyle()
                .setSummaryText(message_numbers+" new messages");
        for (Notification.MessagingStyle.Message message: messages
             ) {
            iStyle.addLine(message.getText()+" "+message.getSender());
        }
        summaryNotificationBuilder.setStyle(iStyle);
        Notification summaryNotification= summaryNotificationBuilder.build();
        notificationManager.notify(notification_ID, summaryNotification);
    }
}
