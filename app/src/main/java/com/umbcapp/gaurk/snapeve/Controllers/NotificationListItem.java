package com.umbcapp.gaurk.snapeve.Controllers;

public class NotificationListItem {

    private String user_id;
    private String notification_id;
    private int notification_type;
    private String header;
    private String payload;
    private String dt_time;
    private boolean read_status;

    public NotificationListItem(String user_id, String notification_id, int notification_type, String header, String payload, String dt_time, boolean read_status) {
        this.user_id = user_id;
        this.notification_id = notification_id;
        this.notification_type = notification_type;
        this.header = header;
        this.payload = payload;
        this.dt_time = dt_time;
        this.read_status = read_status;
    }

    public int getNotification_type() {
        return notification_type;
    }

    public void setNotification_type(int notification_type) {
        this.notification_type = notification_type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getDt_time() {
        return dt_time;
    }

    public void setDt_time(String dt_time) {
        this.dt_time = dt_time;
    }

    public boolean isRead_status() {
        return read_status;
    }

    public void setRead_status(boolean read_status) {
        this.read_status = read_status;
    }
}
