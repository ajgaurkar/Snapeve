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

    @com.google.gson.annotations.SerializedName("user_pass")
    String user_pass;

    @com.google.gson.annotations.SerializedName("reset_pass_flag")
    boolean reset_pass_flag;

    public boolean getReset_pass_flag() {
        return reset_pass_flag;
    }

    public void setReset_pass_flag(boolean reset_pass_flag) {
        this.reset_pass_flag = reset_pass_flag;
    }

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

    public String getUser_pass() {
        return user_pass;
    }

    public void setUser_pass(String user_pass) {
        this.user_pass = user_pass;
    }
}
