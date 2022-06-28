package com.example.kcs.Fragment.PlaceOrders.Session;

public class SelectedSessionList {
    private String session_title, time, bolen, s_count;

    public SelectedSessionList() {

    }


    public void setSession_title(String session_title) {
        this.session_title = session_title;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setBolen(String bolen) {
        this.bolen = bolen;
    }

    public void setS_count(String s_count) {
        this.s_count = s_count;
    }

    public String getBolen() {
        return bolen;
    }

    public String getSession_title() {
        return session_title;
    }

    public String getTime() {
        return time;
    }

    public String getS_count() {
        return s_count;
    }
}
