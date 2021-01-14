package com.example.hostel;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.example.hostel.dashboard.TAG;
import static com.example.hostel.myApp.Fcm_channel_id;

public class firebaseMsg extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "onMessageReceived: "+remoteMessage.getFrom());
        if (remoteMessage.getNotification()!=null){
            String title=remoteMessage.getNotification().getTitle();
            String body=remoteMessage.getNotification().getBody();
            Notification notification= new NotificationCompat.Builder(this,Fcm_channel_id)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(R.drawable.ic_dashboard)
                    .build();
            NotificationManager manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    manager.notify(100,notification);

            Intent dialogIntent = new Intent(this, MainActivity.class);
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(dialogIntent);
        }
        if (remoteMessage.getData().size()>0){
            for (String key:remoteMessage.getData().keySet()){
                Log.d(TAG, "onMessageReceived: "+key);
                Log.d(TAG, "onMessageReceived: "+remoteMessage.getData().get(key));
             }
        }


    }

    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);

    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d(TAG, "onNewToken: "+s);
    }
}
