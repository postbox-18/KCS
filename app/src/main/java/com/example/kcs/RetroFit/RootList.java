package com.example.kcs.RetroFit;

public class RootList {
    public String to;
    public Notification notification;

    public Notification getnotification() {
        return notification;
    }

    public String getto() {
        return to;
    }

    public void setnotification(Notification notification) {
        this.notification = notification;
    }

    public void setto(String to) {
        this.to = to;
    }
}
