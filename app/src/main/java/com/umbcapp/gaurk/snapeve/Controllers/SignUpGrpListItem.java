package com.umbcapp.gaurk.snapeve.Controllers;

public class SignUpGrpListItem {

    int membersCount;
    String grpName;
    String grpDpUrl;

    public SignUpGrpListItem(int membersCount, String grpName, String grpDpUrl) {
        this.membersCount = membersCount;
        this.grpName = grpName;
        this.grpDpUrl = grpDpUrl;
    }

    public int getMembersCount() {
        return membersCount;
    }

    public void setMembersCount(int membersCount) {
        this.membersCount = membersCount;
    }

    public String getGrpName() {
        return grpName;
    }

    public void setGrpName(String grpName) {
        this.grpName = grpName;
    }

    public String getGrpDpUrl() {
        return grpDpUrl;
    }

    public void setGrpDpUrl(String grpDpUrl) {
        this.grpDpUrl = grpDpUrl;
    }
}
