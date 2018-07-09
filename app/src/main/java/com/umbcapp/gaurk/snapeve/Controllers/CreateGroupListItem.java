package com.umbcapp.gaurk.snapeve.Controllers;

public class CreateGroupListItem {

    String userName;
    String userFirstName;
    String userLastName;
    String userId;
    int userReqStatus;
    String userEmail;
    String userPicUrl;


    public CreateGroupListItem(String userName, String userFirstName, String userLastName, String userId, int userReqStatus, String userEmail, String userPicUrl) {
        this.userName = userName;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userId = userId;
        this.userReqStatus = userReqStatus;
        this.userEmail = userEmail;
        this.userPicUrl = userPicUrl;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
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
