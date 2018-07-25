package com.umbcapp.gaurk.snapeve.Controllers;

public class Event_dash_list_obj {
//
//    String img_description;
//    String img_url;
//    String src_user_name;
//
//
//    public Event_dash_list_obj(String img_description, String img_url, String src_user_name) {
//        this.img_description = img_description;
//        this.img_url = img_url;
//        this.src_user_name = src_user_name;
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
//    public String getSrc_user_name() {
//        return src_user_name;
//    }
//
//    public void setSrc_user_name(String src_user_name) {
//        this.src_user_name = src_user_name;
//    }

    String user_id;
    String user_dp_url;
    String user_name;
    String user_comment;
    String comment_time;
    String image_url;
    String post_id;
    String post_dt;
    String post_start_dt_time;
    String post_end_dt_time;
    Boolean all_day_status;

    public Event_dash_list_obj(String user_id, String user_dp_url, String user_name, String user_comment, String comment_time, String image_url, String post_id, String post_dt, String post_start_dt_time, String post_end_dt_time, Boolean all_day_status) {
        this.user_id = user_id;
        this.user_dp_url = user_dp_url;
        this.user_name = user_name;
        this.user_comment = user_comment;
        this.comment_time = comment_time;
        this.image_url = image_url;
        this.post_id = post_id;
        this.post_dt = post_dt;
        this.post_start_dt_time = post_start_dt_time;
        this.post_end_dt_time = post_end_dt_time;
        this.all_day_status = all_day_status;
    }

    public String getUser_dp_url() {
        return user_dp_url;
    }

    public void setUser_dp_url(String user_dp_url) {
        this.user_dp_url = user_dp_url;
    }

    public Boolean getAll_day_status() {
        return all_day_status;
    }

    public void setAll_day_status(Boolean all_day_status) {
        this.all_day_status = all_day_status;
    }

    public String getPost_start_dt_time() {
        return post_start_dt_time;
    }

    public void setPost_start_dt_time(String post_start_dt_time) {
        this.post_start_dt_time = post_start_dt_time;
    }

    public String getPost_end_dt_time() {
        return post_end_dt_time;
    }

    public void setPost_end_dt_time(String post_end_dt_time) {
        this.post_end_dt_time = post_end_dt_time;
    }

    public String getPost_dt() {
        return post_dt;
    }

    public void setPost_dt(String post_dt) {
        this.post_dt = post_dt;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
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
