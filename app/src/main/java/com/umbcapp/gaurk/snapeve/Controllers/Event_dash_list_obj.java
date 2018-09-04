package com.umbcapp.gaurk.snapeve.Controllers;

public class Event_dash_list_obj {

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
    int post_as;
    int scope;
    int location_type;
    String location_name;
    int event_type;
    String grp_name;
    String grp_id;
    String grp_dp_url;
    int user_like;
    int user_spam;
    int total_likes;
    int total_spam;
    int total_comments;

    public Event_dash_list_obj(String user_id, String user_dp_url, String user_name, String user_comment, String comment_time, String image_url, String post_id, String post_dt, String post_start_dt_time, String post_end_dt_time, Boolean all_day_status, int post_as, int scope, int location_type, String location_name, int event_type, String grp_name, String grp_id, String grp_dp_url, int user_like, int user_spam, int total_likes, int total_spam, int total_comments) {
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
        this.post_as = post_as;
        this.scope = scope;
        this.location_type = location_type;
        this.location_name = location_name;
        this.event_type = event_type;
        this.grp_name = grp_name;
        this.grp_id = grp_id;
        this.grp_dp_url = grp_dp_url;
        this.user_like = user_like;
        this.user_spam = user_spam;
        this.total_likes = total_likes;
        this.total_spam = total_spam;
        this.total_comments = total_comments;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public int getPost_as() {
        return post_as;
    }

    public void setPost_as(int post_as) {
        this.post_as = post_as;
    }

    public int getScope() {
        return scope;
    }

    public void setScope(int scope) {
        this.scope = scope;
    }

    public int getLocation_type() {
        return location_type;
    }

    public void setLocation_type(int location_type) {
        this.location_type = location_type;
    }

    public int getEvent_type() {
        return event_type;
    }

    public void setEvent_type(int event_type) {
        this.event_type = event_type;
    }

    public String getGrp_name() {
        return grp_name;
    }

    public void setGrp_name(String grp_name) {
        this.grp_name = grp_name;
    }

    public String getGrp_id() {
        return grp_id;
    }

    public void setGrp_id(String grp_id) {
        this.grp_id = grp_id;
    }

    public String getGrp_dp_url() {
        return grp_dp_url;
    }

    public void setGrp_dp_url(String grp_dp_url) {
        this.grp_dp_url = grp_dp_url;
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

    public int getUser_like() {
        return user_like;
    }

    public void setUser_like(int user_like) {
        this.user_like = user_like;
    }

    public int getUser_spam() {
        return user_spam;
    }

    public void setUser_spam(int user_spam) {
        this.user_spam = user_spam;
    }

    public int getTotal_likes() {
        return total_likes;
    }

    public void setTotal_likes(int total_likes) {
        this.total_likes = total_likes;
    }

    public int getTotal_spam() {
        return total_spam;
    }

    public void setTotal_spam(int total_spam) {
        this.total_spam = total_spam;
    }

    public int getTotal_comments() {
        return total_comments;
    }

    public void setTotal_comments(int total_comments) {
        this.total_comments = total_comments;
    }
}
