package com.umbcapp.gaurk.snapeve.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.umbcapp.gaurk.snapeve.Controllers.SnapEveSession;
import com.umbcapp.gaurk.snapeve.Controllers.SnapeveNotification;
import com.umbcapp.gaurk.snapeve.DaoClasses.SnapeveNotificationDao;
import com.umbcapp.gaurk.snapeve.DaoClasses.SnapeveSessionDao;

@Database(entities = {SnapeveNotification.class,SnapEveSession.class}, version = 2, exportSchema = false)
public abstract class SnapeveDatabase extends RoomDatabase {

    public abstract SnapeveNotificationDao snapeveNotificationDao();

    public abstract SnapeveSessionDao snapeveSessionDao();


}
