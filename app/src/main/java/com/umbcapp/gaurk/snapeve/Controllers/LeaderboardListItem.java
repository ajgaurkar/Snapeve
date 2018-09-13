package com.umbcapp.gaurk.snapeve.Controllers;

public class LeaderboardListItem {

    String userId;
    String userName;
    int userPoints;
    int userSequenceNo;
    String userGroup;
    String userPicUrl;

    public LeaderboardListItem(int userSequenceNo, String userId, String userName, int userPoints, String userGroup, String userPicUrl) {
        this.userId = userId;
        this.userName = userName;
        this.userPoints = userPoints;
        this.userSequenceNo = userSequenceNo;
        this.userGroup = userGroup;
        this.userPicUrl = userPicUrl;
    }

    public int getUserPoints() {
        return userPoints;
    }

    public void setUserPoints(int userPoints) {
        this.userPoints = userPoints;
    }

    public int getUserSequenceNo() {
        return userSequenceNo;
    }

    public void setUserSequenceNo(int userSequenceNo) {
        this.userSequenceNo = userSequenceNo;
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
        return userPoints;
    }

    public void setUserRank(int userRank) {
        this.userPoints = userRank;
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
