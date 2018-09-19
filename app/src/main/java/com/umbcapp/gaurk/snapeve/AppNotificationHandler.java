package com.umbcapp.gaurk.snapeve;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
    private String poster_id = "";
    private String user_grp_id = "";

    @Override
    public void onReceive(Context context, Bundle bundle) {
        ctx = context;
        nhTitle = bundle.getString("notification_title");
        nhMessage = bundle.getString("notification_message");
        nhTag = bundle.getString("tag");

        try {
            poster_id = bundle.getString("poster_id");
            user_grp_id = bundle.getString("user_grp_id");
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("in Notoification on Recive ");

        switch (nhTag) {
            case "New Event":
                if (!new SessionManager(ctx).getSpecificUserDetail(SessionManager.KEY_USER_ID).equals(poster_id)) {
                    System.out.println("New Event 1");
                    try {
                        System.out.println("New Event 2");
                        if (new SessionManager(ctx).getSpecificUserDetail(SessionManager.KEY_GRP_ID).equals(user_grp_id)) {
                            System.out.println("New Event 3");
                            nhTitle = "Group member posted an event";
                            processNotification();
                        } else {
                            System.out.println("New Event 4");
                            processNotification();
                        }
                    } catch (Exception e) {
                        System.out.println("New Event 5");
                        processNotification();
                    }
                }
                System.out.println("New Event 6");
                break;
            case "Action verified":
                System.out.println("Action verified 1");

                System.out.println("poster_id 1 " + poster_id);
                System.out.println("poster_id 2 " + new SessionManager(ctx).getSpecificUserDetail(SessionManager.KEY_USER_ID));

                if (new SessionManager(ctx).getSpecificUserDetail(SessionManager.KEY_USER_ID).equals(poster_id)) {
                    System.out.println("Action verified 2");
                    processNotification();
                }
                System.out.println("Action verified 3");
                break;
            case "Action spammed":
                if (!new SessionManager(ctx).getSpecificUserDetail(SessionManager.KEY_USER_ID).equals(poster_id)) {
                    processNotification();
                }
                break;
        }

    }

    private void processNotification() {

        setNotification();
        snapeveDatabaseRepository = new SnapeveDatabaseRepository(ctx);
        long curentTime = System.currentTimeMillis();
        System.out.println("System.currentTimeMillis() " + curentTime);
        snapeveDatabaseRepository.insertSnapeveNotification(nhTitle, nhMessage, nhTag, curentTime);

    }


    private void setNotification() {
        //channel ID is hardcoded. it works as there is not other channel ID
        Intent intent = new Intent(ctx, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
                intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx, "channel_id_x")
                .setSmallIcon(R.drawable.locations_24)
                .setContentTitle(nhTitle)
                .setContentText(nhMessage)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("-> " + nhMessage))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        mBuilder.setContentIntent(contentIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(ctx);
        notificationManager.notify((int) System.currentTimeMillis(), mBuilder.build());

    }

}
