package com.umbcapp.gaurk.snapeve.Controllers;

public class MessagesPersonalListItem {

    private String user_id;
    private String other_user_id;
    private String other_user_name;
    private String other_user_dp_url;
    private String last_msg_dt_time;
    private String last_msg_data;
    private boolean read_status;

    public MessagesPersonalListItem(String user_id, String other_user_id, String other_user_name, String other_user_dp_url, String last_msg_dt_time, String last_msg_data, boolean read_status) {
        this.user_id = user_id;
        this.other_user_id = other_user_id;
        this.other_user_name = other_user_name;
        this.other_user_dp_url = other_user_dp_url;
        this.last_msg_dt_time = last_msg_dt_time;
        this.last_msg_data = last_msg_data;
        this.read_status = read_status;
    }

    public String getOther_user_dp_url() {
        return other_user_dp_url;
    }

    public void setOther_user_dp_url(String other_user_dp_url) {
        this.other_user_dp_url = other_user_dp_url;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getOther_user_id() {
        return other_user_id;
    }

    public void setOther_user_id(String other_user_id) {
        this.other_user_id = other_user_id;
    }

    public String getOther_user_name() {
        return other_user_name;
    }

    public void setOther_user_name(String other_user_name) {
        this.other_user_name = other_user_name;
    }

    public String getLast_msg_dt_time() {
        return last_msg_dt_time;
    }

    public void setLast_msg_dt_time(String last_msg_dt_time) {
        this.last_msg_dt_time = last_msg_dt_time;
    }

    public String getLast_msg_data() {
        return last_msg_data;
    }

    public void setLast_msg_data(String last_msg_data) {
        this.last_msg_data = last_msg_data;
    }

    public boolean isRead_status() {
        return read_status;
    }

    public void setRead_status(boolean read_status) {
        this.read_status = read_status;
    }
}
