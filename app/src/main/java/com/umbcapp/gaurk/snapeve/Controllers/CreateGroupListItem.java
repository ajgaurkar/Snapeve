package com.umbcapp.gaurk.snapeve.Controllers;

public class CreateGroupListItem {

    String userName;
    String userId;
    int userReqStatus;
    String userEmail;
    String userPicUrl;


    public CreateGroupListItem(String userName, String userId, int userReqStatus, String userEmail, String userPicUrl) {
        this.userName = userName;
        this.userId = userId;
        this.userReqStatus = userReqStatus;
        this.userEmail = userEmail;
        this.userPicUrl = userPicUrl;
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

    public int getUserReqStatus() {
        return userReqStatus;
    }

    public void setUserReqStatus(int userReqStatus) {
        this.userReqStatus = userReqStatus;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPicUrl() {
        return userPicUrl;
    }

    public void setUserPicUrl(String userPicUrl) {
        this.userPicUrl = userPicUrl;
    }
}
