package com.umbcapp.gaurk.snapeve.Controllers;

public class SignUpGrpListItem {

    String grpId;
    int membersCount;
    String grpName;
    String grpDpUrl;

    public SignUpGrpListItem(String grpId, int membersCount, String grpName, String grpDpUrl) {
        this.grpId = grpId;
        this.membersCount = membersCount;
        this.grpName = grpName;
        this.grpDpUrl = grpDpUrl;
    }

    public String getGrpId() {
        return grpId;
    }

    public void setGrpId(String grpId) {
        this.grpId = grpId;
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
