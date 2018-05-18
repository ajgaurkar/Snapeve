package com.umbcapp.gaurk.snapeve.Controllers;

public class SimilarPostsListItem {


    String post_id;
    String initializer_id;
    String img_url;
    String img_description;
    String post_dt;
    String post_from_time;
    String post_to_time;
    int likes_count;
    int comments_count;
    int spam_count;

    public SimilarPostsListItem(String post_id, String initializer_id, String img_url, String img_description, String post_dt, String post_from_time, String post_to_time, int likes_count, int comments_count, int spam_count) {
        this.post_id = post_id;
        this.initializer_id = initializer_id;
        this.img_url = img_url;
        this.img_description = img_description;
        this.post_dt = post_dt;
        this.post_from_time = post_from_time;
        this.post_to_time = post_to_time;
        this.likes_count = likes_count;
        this.comments_count = comments_count;
        this.spam_count = spam_count;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getInitializer_id() {
        return initializer_id;
    }

    public void setInitializer_id(String initializer_id) {
        this.initializer_id = initializer_id;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getImg_description() {
        return img_description;
    }

    public void setImg_description(String img_description) {
        this.img_description = img_description;
    }

    public String getPost_dt() {
        return post_dt;
    }

    public void setPost_dt(String post_dt) {
        this.post_dt = post_dt;
    }

    public String getPost_from_time() {
        return post_from_time;
    }

    public void setPost_from_time(String post_from_time) {
        this.post_from_time = post_from_time;
    }

    public String getPost_to_time() {
        return post_to_time;
    }

    public void setPost_to_time(String post_to_time) {
        this.post_to_time = post_to_time;
    }

    public int getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(int likes_count) {
        this.likes_count = likes_count;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public int getSpam_count() {
        return spam_count;
    }

    public void setSpam_count(int spam_count) {
        this.spam_count = spam_count;
    }
}
