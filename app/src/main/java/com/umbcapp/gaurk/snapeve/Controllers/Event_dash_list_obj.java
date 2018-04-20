package com.umbcapp.gaurk.snapeve.Controllers;

public class Event_dash_list_obj {
//
//    String img_description;
//    String img_url;
//    String user_name;
//
//
//    public Event_dash_list_obj(String img_description, String img_url, String user_name) {
//        this.img_description = img_description;
//        this.img_url = img_url;
//        this.user_name = user_name;
//    }
//
//    public String getImg_url() {
//        return img_url;
//    }
//
//    public void setImg_url(String img_url) {
//        this.img_url = img_url;
//    }
//
//    public String getImg_description() {
//        return img_description;
//    }
//
//    public void setImg_description(String img_description) {
//        this.img_description = img_description;
//    }
//
//    public String getUser_name() {
//        return user_name;
//    }
//
//    public void setUser_name(String user_name) {
//        this.user_name = user_name;
//    }

    String user_id;
    String user_name;
    String user_comment;
    String comment_time;
    String image_url;

    public Event_dash_list_obj(String user_id, String user_name, String user_comment, String comment_time, String image_url) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_comment = user_comment;
        this.comment_time = comment_time;
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

    public String getUser_comment() {
        return user_comment;
    }

    public void setUser_comment(String user_comment) {
        this.user_comment = user_comment;
    }

    public String getComment_time() {
        return comment_time;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
