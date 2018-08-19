package com.umbcapp.gaurk.snapeve.Controllers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdminLeaveGroupMemberListItem {

    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_id")
    @Expose
    private String userId;

    public AdminLeaveGroupMemberListItem(String userName, String userId) {
        this.userName = userName;
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
