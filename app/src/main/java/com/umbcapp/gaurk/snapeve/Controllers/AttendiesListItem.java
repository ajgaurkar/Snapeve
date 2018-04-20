package com.umbcapp.gaurk.snapeve.Controllers;

public class AttendiesListItem {

    String user_id;
    String user_name;
    int request_status;
    int attend_status;
    String image_url;

    public AttendiesListItem(String user_id, String user_name, int request_status, int attend_status, String image_url) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.request_status = request_status;
        this.attend_status = attend_status;
        this.image_url = image_url;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getRequest_status() {
        return request_status;
    }

    public void setRequest_status(int request_status) {
        this.request_status = request_status;
    }

    public int getAttend_status() {
        return attend_status;
    }

    public void setAttend_status(int attend_status) {
        this.attend_status = attend_status;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
