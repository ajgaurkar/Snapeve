package com.umbcapp.gaurk.snapeve.Controllers;

public class CommentsListItem {

    String src_user_id;
    String trgt_user_id;
    String src_user_name;
    String trgt_user_name;
    String user_comment;
    String comment_time;
    String image_url;

    public CommentsListItem(String src_user_id, String trgt_user_id, String src_user_name, String trgt_user_name, String user_comment, String comment_time, String image_url) {
        this.src_user_id = src_user_id;
        this.trgt_user_id = trgt_user_id;
        this.src_user_name = src_user_name;
        this.trgt_user_name = trgt_user_name;
        this.user_comment = user_comment;
        this.comment_time = comment_time;
        this.image_url = image_url;
    }

    public String getSrc_user_id() {
        return src_user_id;
    }

    public String getTrgt_user_id() {
        return trgt_user_id;
    }

    public String getSrc_user_name() {
        return src_user_name;
    }

    public String getTrgt_user_name() {
        return trgt_user_name;
    }

    public String getUser_comment() {
        return user_comment;
    }

    public String getComment_time() {
        return comment_time;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setSrc_user_id(String src_user_id) {
        this.src_user_id = src_user_id;
    }

    public void setTrgt_user_id(String trgt_user_id) {
        this.trgt_user_id = trgt_user_id;
    }

    public void setSrc_user_name(String src_user_name) {
        this.src_user_name = src_user_name;
    }

    public void setTrgt_user_name(String trgt_user_name) {
        this.trgt_user_name = trgt_user_name;
    }

    public void setUser_comment(String user_comment) {
        this.user_comment = user_comment;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
