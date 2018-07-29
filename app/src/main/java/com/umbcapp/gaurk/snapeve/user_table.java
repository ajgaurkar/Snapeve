package com.umbcapp.gaurk.snapeve;

import java.util.Date;

/**
 * Created by Amey on 24-07-2018.
 */

public class user_table {

    @com.google.gson.annotations.SerializedName("id")
    String id;
    @com.google.gson.annotations.SerializedName("dp_url")
    String dp_url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDp_url() {
        return dp_url;
    }

    public void setDp_url(String dp_url) {
        this.dp_url = dp_url;
    }
}
