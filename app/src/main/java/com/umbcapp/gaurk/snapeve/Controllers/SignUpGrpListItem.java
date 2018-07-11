package com.umbcapp.gaurk.snapeve.Controllers;

public class SignUpGrpListItem {

    String grpId;
    int membersCount;
    String grpName;
    String grpDpUrl;
    int accept_or_request_flag;

    public SignUpGrpListItem(String grpId, int membersCount, String grpName, String grpDpUrl, int accept_or_request_flag) {
        this.grpId = grpId;
        this.membersCount = membersCount;
        this.grpName = grpName;
        this.grpDpUrl = grpDpUrl;
        this.accept_or_request_flag = accept_or_request_flag;
    }

    public int getAccept_or_request_flag() {
        return accept_or_request_flag;
    }

    public void setAccept_or_request_flag(int accept_or_request_flag) {
        this.accept_or_request_flag = accept_or_request_flag;
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
