package com.umbcapp.gaurk.snapeve.DaoClasses;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.umbcapp.gaurk.snapeve.Controllers.SnapeveNotification;

import java.util.List;

@Dao
public interface SnapeveNotificationDao {

    @Insert
    Long insertTask(SnapeveNotification notification);


    @Query("SELECT * FROM SnapeveNotification ORDER BY created_at desc")
    LiveData<List<SnapeveNotification>> fetchAllNotification();


    @Query("SELECT * FROM SnapeveNotification WHERE id =:taskId")
    LiveData<SnapeveNotification> fetchNotitifcationWithId(int taskId);


    @Update
    void updateTask(SnapeveNotification notification);


    @Delete
    void deleteTask(SnapeveNotification notification);
}
