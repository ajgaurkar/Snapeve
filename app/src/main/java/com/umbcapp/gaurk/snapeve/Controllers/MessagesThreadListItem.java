package com.umbcapp.gaurk.snapeve.Controllers;

public class MessagesThreadListItem {

    String msg_payload;
    int sent_or_rceived;
    long msg_time;

    public MessagesThreadListItem(String msg_payload, int sent_or_rceived, long msg_time) {
        this.msg_payload = msg_payload;
        this.sent_or_rceived = sent_or_rceived;
        this.msg_time = msg_time;
    }

    public String getMsg_payload() {
        return msg_payload;
    }

    public void setMsg_payload(String msg_payload) {
        this.msg_payload = msg_payload;
    }

    public int getSent_or_rceived() {
        return sent_or_rceived;
    }

    public void setSent_or_rceived(int sent_or_rceived) {
        this.sent_or_rceived = sent_or_rceived;
    }

    public long getMsg_time() {
        return msg_time;
    }

    public void setMsg_time(long msg_time) {
        this.msg_time = msg_time;
    }
}
