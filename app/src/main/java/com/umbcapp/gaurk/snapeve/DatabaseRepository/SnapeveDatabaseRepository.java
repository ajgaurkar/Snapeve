package com.umbcapp.gaurk.snapeve.DatabaseRepository;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.umbcapp.gaurk.snapeve.Controllers.SnapEveSession;
import com.umbcapp.gaurk.snapeve.Controllers.SnapeveNotification;
import com.umbcapp.gaurk.snapeve.Database.SnapeveDatabase;

import java.util.List;

public class SnapeveDatabaseRepository {

    private String DB_NAME = "snapevedb";
    private SnapeveDatabase snapeveDatabase;

    public SnapeveDatabaseRepository(Context context) {
        snapeveDatabase = Room.databaseBuilder(context, SnapeveDatabase.class, DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    public void insertSnapeveNotification(String notificationTitle, String notificationDesc, String notificationTag, long not_receive_time) {
        SnapeveNotification snapeveNotification = new SnapeveNotification();
        snapeveNotification.setNotificationTitle(notificationTitle);
        snapeveNotification.setNotificationDescription(notificationDesc);
        snapeveNotification.setNotificationTag(notificationTag);
        snapeveNotification.setNotificationTimeStamp(not_receive_time);
        insertTask(snapeveNotification);
    }

    public void insertSnapeveSession(String activityCode, long startTime, long endTime, long duration, int uploadStatus) {
        SnapEveSession snapEveSession = new SnapEveSession();
        snapEveSession.setStartTime(startTime);
        snapEveSession.setEndTime(endTime);
        snapEveSession.setDuration(duration);
        snapEveSession.setUploadStatus(uploadStatus);
        snapEveSession.setActivityCode(activityCode);
        insertSession(snapEveSession);
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

    public void insertSession(final SnapEveSession session) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                snapeveDatabase.snapeveSessionDao().insertSession(session);
                return null;
            }
        }.execute();
    }
    public void deleteSession(final SnapEveSession session) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                snapeveDatabase.snapeveSessionDao().deleteSession(session);
                return null;
            }
        }.execute();
    }


    public LiveData<List<SnapeveNotification>> getTasks() {
        return snapeveDatabase.snapeveNotificationDao().fetchAllNotification();
    }

    public LiveData<List<SnapEveSession>> getSessiondata() {
        return snapeveDatabase.snapeveSessionDao().fetchSession();
    }



}
