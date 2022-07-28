package com.example.adm.Fragments.Notification;

public class NotifyList {
    private final String msg,date;

    public NotifyList(String date,String msg) {

        this.msg = msg;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getMsg() {
        return msg;
    }
}
