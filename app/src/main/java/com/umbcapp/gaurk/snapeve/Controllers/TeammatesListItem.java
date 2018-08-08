package com.umbcapp.gaurk.snapeve.Controllers;

public class TeammatesListItem {

    String userId;
    String userName;
    int userRank;
    String userFullName;
    String userPicUrl;

    public TeammatesListItem(String userId, String userName, int userRank, String userFullName, String userPicUrl) {
        this.userId = userId;
        this.userName = userName;
        this.userRank = userRank;
        this.userFullName = userFullName;
        this.userPicUrl = userPicUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserRank() {
        return userRank;
    }

    public void setUserRank(int userRank) {
        this.userRank = userRank;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserPicUrl() {
        return userPicUrl;
    }

    public void setUserPicUrl(String userPicUrl) {
        this.userPicUrl = userPicUrl;
    }
}
