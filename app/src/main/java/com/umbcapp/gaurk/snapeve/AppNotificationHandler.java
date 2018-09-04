package com.umbcapp.gaurk.snapeve;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.microsoft.windowsazure.notifications.NotificationsHandler;
import com.umbcapp.gaurk.snapeve.DatabaseRepository.SnapeveDatabaseRepository;

import java.util.Calendar;

/**
 * Created by Amey on 29-07-2018.
 */

public class AppNotificationHandler extends NotificationsHandler {

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    Context ctx;
    private String nhMessage = "";
    private String nhTitle;
    private String nhTag;
    private SnapeveDatabaseRepository snapeveDatabaseRepository;

    @Override
    public void onReceive(Context context, Bundle bundle) {
        ctx = context;
        nhTitle = bundle.getString("notification_title");
        nhMessage = bundle.getString("notification_message");
        nhTag = bundle.getString("tag");
        System.out.println("in Notoification on Recive ");
//        sendNotification(nhMessage);

//        new SessionManager(ctx).getSpecificUserDetail()
        setNotification();

        snapeveDatabaseRepository = new SnapeveDatabaseRepository(ctx);

        long curentTime = System.currentTimeMillis();
        System.out.println("System.currentTimeMillis() " + curentTime);

        snapeveDatabaseRepository.insertSnapeveNotification(nhTitle, nhMessage, nhTag, curentTime);

    }


    private void setNotification() {
        //channel ID is hardcoded. it works as there is not other channel ID
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx, "channel_id_x")
                .setSmallIcon(R.drawable.locations_24)
                .setContentTitle(nhTitle)
                .setContentText(nhMessage)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("-> " + nhMessage))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(ctx);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify((int) System.currentTimeMillis(), mBuilder.build());

    }

//    private void sendNotification(String msg) {
//
//        Intent intent = new Intent(ctx, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        System.out.println(1);
//        mNotificationManager = (NotificationManager)
//                ctx.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
//                intent, PendingIntent.FLAG_ONE_SHOT);
//
//        System.out.println(2);
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(ctx)
//                        .setSmallIcon(R.mipmap.ic_launcher)
//                        .setContentTitle("Snapeve Notification")
//                        .setStyle(new NotificationCompat.BigTextStyle()
//                                .bigText(msg))
//                        .setSound(defaultSoundUri)
//                        .setContentText(msg);
//
//        System.out.println(3);
//        mBuilder.setContentIntent(contentIntent);
//        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
//        System.out.println(4);
//    }
}
