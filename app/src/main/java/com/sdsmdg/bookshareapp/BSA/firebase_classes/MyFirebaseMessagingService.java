package com.sdsmdg.bookshareapp.BSA.firebase_classes;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.sdsmdg.bookshareapp.BSA.R;
import com.sdsmdg.bookshareapp.BSA.ui.MainActivity;

import java.util.ArrayList;


/**
 * Created by ajayrahul on 1/12/16.
 */
public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    public static ArrayList<String> notifications = new ArrayList<>();
    PendingIntent pendingIntent;
    final static String GROUP_KEY = "fcm_notifs";
    int size = 0;
    public int no_notifs = 0;


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "FCMSERVICE DESTROOOOOOYED ");

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "FCMSERVICE STARTED ");
//        sendNotification("Notif created","Yup");
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "FROM : " + remoteMessage.getFrom());

        //check if msg contains data..
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data  : " + remoteMessage.getData());
            String name = remoteMessage.getData().get("name");
            String body = remoteMessage.getData().get("body");
            String book = remoteMessage.getData().get("book");
            String title = remoteMessage.getData().get("title");
            String message = name + " " + body + " " + book;
//            String x = remoteMessage.getNotification().getBody();
//            Log.i(TAG, x + "-------kkn");
            sendNotification(title, message);
        }


        //chck for notifs..
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message data  : " + remoteMessage.getNotification().getBody());
//            sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(),count++);
        }

    }


    private void sendNotification(String title, String body) {

        Log.i("Reached here ", "inside send ----->");
//        Intent del = new Intent(this, MyBroadcastReceiver.class);
//        PendingIntent broadcastIntent = PendingIntent.getBroadcast(this, 0, del, 0);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("data", "open");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        pendingIntent = PendingIntent.getActivity(this, 0 /*request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Log.i("NOTIFICATION SIZE : ", notifications.size() + "-->");
        //setting sound :
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder;
        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setSound(notificationSound)
                .setContentIntent(pendingIntent);

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        if (notifications.size() == 0) {
            notificationBuilder.setContentTitle(title)
                    .setContentText(body);
        } else {
             size = notifications.size()+1;
            notificationBuilder.setContentTitle(title);
            inboxStyle.setBigContentTitle("You have " + size+ " Notifications.");
        }


        notifications.add(body);
        for (int i = 0; i < notifications.size(); i++) {
            inboxStyle.addLine(notifications.get(i));
        }

        notificationBuilder.setStyle(inboxStyle);
//        notificationBuilder.setDeleteIntent(broadcastIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());

    }


}
