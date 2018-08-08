package com.umbcapp.gaurk.snapeve;

public class feedback_table {


    @com.google.gson.annotations.SerializedName("id")
    String id;
    @com.google.gson.annotations.SerializedName("title")
    String title;
    @com.google.gson.annotations.SerializedName("description")
    String description;
    @com.google.gson.annotations.SerializedName("category")
    String category;
    @com.google.gson.annotations.SerializedName("attachment_url")
    String attachment_url;
    @com.google.gson.annotations.SerializedName("postdate")
    String postdate;
    @com.google.gson.annotations.SerializedName("submitted_by")
    String submitted_by;

    public feedback_table() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAttachment_url() {
        return attachment_url;
    }

    public void setAttachment_url(String attachment_url) {
        this.attachment_url = attachment_url;
    }

    public String getPostdate() {
        return postdate;
    }

    public void setPostdate(String postdate) {
        this.postdate = postdate;
    }

    public String getSubmitted_by() {
        return submitted_by;
    }

    public void setSubmitted_by(String submitted_by) {
        this.submitted_by = submitted_by;
    }
}
