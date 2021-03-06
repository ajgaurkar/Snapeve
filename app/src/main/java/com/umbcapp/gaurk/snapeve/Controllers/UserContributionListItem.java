package com.umbcapp.gaurk.snapeve.Controllers;

public class UserContributionListItem {

    String post_id;
    String user_id;
    String user_name;
    String first_name;
    String last_name;
    String created_at_dt_time;
    String image_url;
    String dp_url;
    String description;
    int total_likes;
    int total_spam;
    int total_comments;
    int post_as;

    public UserContributionListItem(String post_id, String user_id, String user_name, String first_name, String last_name, String created_at_dt_time, String image_url, String dp_url, String description, int total_likes, int total_spam, int total_comments, int post_as) {
        this.post_id = post_id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.first_name = first_name;
        this.last_name = last_name;
        this.created_at_dt_time = created_at_dt_time;
        this.image_url = image_url;
        this.dp_url = dp_url;
        this.description = description;
        this.total_likes = total_likes;
        this.total_spam = total_spam;
        this.total_comments = total_comments;
        this.post_as = post_as;
    }

    public int getPost_as() {
        return post_as;
    }

    public void setPost_as(int post_as) {
        this.post_as = post_as;
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

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getCreated_at_dt_time() {
        return created_at_dt_time;
    }

    public void setCreated_at_dt_time(String created_at_dt_time) {
        this.created_at_dt_time = created_at_dt_time;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDp_url() {
        return dp_url;
    }

    public void setDp_url(String dp_url) {
        this.dp_url = dp_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
