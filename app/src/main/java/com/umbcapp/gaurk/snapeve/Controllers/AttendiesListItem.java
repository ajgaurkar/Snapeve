package com.umbcapp.gaurk.snapeve.Controllers;

public class AttendiesListItem {

    String user_id;
    String user_name;
    int attend_status;
    String image_url;
    int req_status;
    String req_sender_id;
    String requester_id;

    public AttendiesListItem(String user_id, String user_name, int attend_status, String image_url, int req_status, String req_sender_id, String requester_id) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.attend_status = attend_status;
        this.image_url = image_url;
        this.req_status = req_status;
        this.req_sender_id = req_sender_id;
        this.requester_id = requester_id;
    }


    public int getReq_status() {
        return req_status;
    }

    public void setReq_status(int req_status) {
        this.req_status = req_status;
    }

    public String getReq_sender_id() {
        return req_sender_id;
    }

    public void setReq_sender_id(String req_sender_id) {
        this.req_sender_id = req_sender_id;
    }

    public String getRequester_id() {
        return requester_id;
    }

    public void setRequester_id(String requester_id) {
        this.requester_id = requester_id;
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
