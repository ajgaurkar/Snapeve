package com.umbcapp.gaurk.snapeve.Controllers;

public class LeaderboardListItem {

    String userId;
    String userName;
    int userRank;
    String userGroup;
    String userPicUrl;

    public LeaderboardListItem(String userId, String userName, int userRank, String userGroup, String userPicUrl) {
        this.userId = userId;
        this.userName = userName;
        this.userRank = userRank;
        this.userGroup = userGroup;
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

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    public String getUserPicUrl() {
        return userPicUrl;
    }

    public void setUserPicUrl(String userPicUrl) {
        this.userPicUrl = userPicUrl;
    }
}
