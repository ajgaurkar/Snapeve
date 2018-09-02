package com.umbcapp.gaurk.snapeve.DaoClasses;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.umbcapp.gaurk.snapeve.Controllers.SnapEveSession;
import com.umbcapp.gaurk.snapeve.Controllers.SnapeveNotification;

import java.util.List;

@Dao
public interface SnapeveSessionDao {
    @Insert
    Long insertSession(SnapEveSession session);


    @Query("SELECT * FROM SnapEveSession")
    LiveData<List<SnapEveSession>> fetchSession();


//    @Query("SELECT * FROM SnapeveNotification WHERE id =:taskId")
//    LiveData<SnapeveNotification> fetchNotitifcationWithId(int taskId);

    @Update
    void updateSession(SnapEveSession session);

    @Delete
    void deleteSession(SnapEveSession session);
}
