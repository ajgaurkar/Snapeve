package com.umbcapp.gaurk.snapeve.Controllers;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.umbcapp.gaurk.snapeve.TimestampConverter;

import java.io.Serializable;
import java.util.Date;

@Entity
public class SnapeveNotification implements Serializable {


    @PrimaryKey(autoGenerate = true)
    private int id;

    private String notificationTitle;
    private String notificationDescription;
    private String notificationTag;
    private long notificationTimeStamp;

    public String getNotificationTag() {
        return notificationTag;
    }

    public void setNotificationTag(String notificationTag) {
        this.notificationTag = notificationTag;
    }

    @ColumnInfo(name = "created_at")
    @TypeConverters({TimestampConverter.class})
    private Date createdAt;

    @ColumnInfo(name = "modified_at")
    @TypeConverters({TimestampConverter.class})
    private Date modifiedAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getNotificationDescription() {
        return notificationDescription;
    }

    public void setNotificationDescription(String notificationDescription) {
        this.notificationDescription = notificationDescription;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public long getNotificationTimeStamp() {
        return notificationTimeStamp;
    }

    public void setNotificationTimeStamp(long notificationTimeStamp) {
        this.notificationTimeStamp = notificationTimeStamp;
    }
}
