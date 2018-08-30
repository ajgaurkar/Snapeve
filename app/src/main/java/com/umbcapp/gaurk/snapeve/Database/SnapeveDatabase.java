package com.umbcapp.gaurk.snapeve.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.umbcapp.gaurk.snapeve.Controllers.SnapeveNotification;
import com.umbcapp.gaurk.snapeve.DaoClasses.SnapeveNotificationDao;

@Database(entities = {SnapeveNotification.class}, version = 1, exportSchema = false)
public abstract class SnapeveDatabase extends RoomDatabase {

    public abstract SnapeveNotificationDao snapeveNotificationDao();
}
