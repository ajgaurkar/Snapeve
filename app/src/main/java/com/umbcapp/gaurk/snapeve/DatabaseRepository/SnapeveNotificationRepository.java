package com.umbcapp.gaurk.snapeve.DatabaseRepository;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.umbcapp.gaurk.snapeve.Controllers.SnapeveNotification;
import com.umbcapp.gaurk.snapeve.Database.SnapeveDatabase;

import java.util.List;

public class SnapeveNotificationRepository {

    private String DB_NAME = "snapevedb";
    private SnapeveDatabase snapeveDatabase;

    public SnapeveNotificationRepository(Context context) {
        snapeveDatabase = Room.databaseBuilder(context, SnapeveDatabase.class, DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }


    public void insertSnapeveNotification(String notificationTitle, String notificationDesc, String notificationTag) {
        SnapeveNotification snapeveNotification = new SnapeveNotification();
        snapeveNotification.setNotificationTitle(notificationTitle);
        snapeveNotification.setNotificationDescription(notificationDesc);
        snapeveNotification.setNotificationTag(notificationTag);
        insertTask(snapeveNotification);
    }

    public void insertTask(final SnapeveNotification notification) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                snapeveDatabase.snapeveNotificationDao().insertTask(notification);
                return null;
            }
        }.execute();
    }

    public LiveData<List<SnapeveNotification>> getTasks() {
        return snapeveDatabase.snapeveNotificationDao().fetchAllNotification();
    }

}
