package com.example.kcs.RetroFit;

public class Notification {


    // string variables for our title and body
    private String title;
    private String body;

    public Notification(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String gettitle() {
        return title;
    }

    public void settitle(String title) {
        this.title = title;
    }

    public String getbody() {
        return body;
    }

    public void setbody(String body) {
        this.body = body;
    }

}
